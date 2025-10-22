package com.example.domartorders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private OrdersAdapter adapter;
    private final List<Order> allOrders = new ArrayList<>();
    private final List<Order> visibleOrders = new ArrayList<>();

    private DatabaseReference activeOrdersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.parent_rv);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.action_bar)));
        }
        if (getSupportActionBar() != null) getSupportActionBar().setLogo(R.drawable.logo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrdersAdapter(visibleOrders, this);
        recyclerView.setAdapter(adapter);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.bringToFront();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            spinner.setElevation(8f);
        }
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.status_array, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.dropdownitem);
        spinner.setAdapter(spinnerAdapter);
        spinner.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                applyStatusFilter(label);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Dane
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Nie zalogowano użytkownika.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, StartLoginActivity.class));
            finish();
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference byUidRef = db.child("orders").child(currentUser.getUid());
        DatabaseReference globalRef = db.child("orders");

        progressBar.setVisibility(View.VISIBLE);

        byUidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    activeOrdersRef = byUidRef;
                    loadOrdersOnce();
                } else {
                    globalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override public void onDataChange(@NonNull DataSnapshot snGlobal) {
                            if (snGlobal.hasChildren()) {
                                activeOrdersRef = globalRef;
                                loadOrdersOnce();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Brak zamówień w bazie.", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadOrdersOnce() {
        if (activeOrdersRef == null) return;

        progressBar.setVisibility(View.VISIBLE);

        activeOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                allOrders.clear();

                boolean looksLikeOrdersList = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.hasChild("nr") || child.hasChild("id") || child.hasChild("status")) {
                        looksLikeOrdersList = true;
                        break;
                    }
                }

                if (looksLikeOrdersList) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Order o = child.getValue(Order.class);
                        if (o != null) allOrders.add(o);
                    }
                } else {
                    for (DataSnapshot uidNode : snapshot.getChildren()) {
                        for (DataSnapshot orderNode : uidNode.getChildren()) {
                            Order o = orderNode.getValue(Order.class);
                            if (o != null) allOrders.add(o);
                        }
                    }
                }

                visibleOrders.clear();
                visibleOrders.addAll(allOrders);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (visibleOrders.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Brak zamówień w bazie.", Toast.LENGTH_LONG).show();
                }
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private String[] mapLabelToStatuses(String label) {
        switch (label) {
            case "Wszystkie":              return null; // brak filtra
            case "Zakończone":             return new String[]{"DONE", "SENT"}; // gotowe/wyjechało
            case "W trakcie realizacji":   return new String[]{"IN_PROGRESS", "STARTED"};
            case "Wyjechało":              return new String[]{"SENT"};
            case "Gotowe":                 return new String[]{"DONE"};
            case "Częściowo wyjechało":    return new String[]{"PARTIAL_SENT", "PARTIAL"}; // jeśli kiedyś się pojawi
            default:                       return null;
        }
    }

    private void applyStatusFilter(String label) {
        String[] codes = mapLabelToStatuses(label);
        visibleOrders.clear();
        if (codes == null) {
            visibleOrders.addAll(allOrders);
        } else {
            for (Order o : allOrders) {
                String s = o.getStatus() == null ? "" : o.getStatus();
                for (String c : codes) {
                    if (s.equalsIgnoreCase(c)) {
                        visibleOrders.add(o);
                        break;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"UseCompatLoadingForDrawables"})
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Wyszukaj numer zamówienia");
        searchView.setInputType(InputType.TYPE_CLASS_PHONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
            @Override public boolean onQueryTextChange(String newText) {
                // prosty search po "nr"
                String q = newText == null ? "" : newText.trim();
                visibleOrders.clear();
                if (q.isEmpty()) {
                    visibleOrders.addAll(allOrders);
                } else {
                    for (Order o : allOrders) {
                        String nr = o.getNr() == null ? "" : o.getNr();
                        if (nr.contains(q)) visibleOrders.add(o);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutItem) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, StartLoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

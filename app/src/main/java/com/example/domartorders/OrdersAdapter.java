package com.example.domartorders;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private final List<Order> data;
    private final Context context;

    public OrdersAdapter(List<Order> data, Context context) {
        this.data = data;
        this.context = context;
        setHasStableIds(false);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrdersAdapter.ViewHolder holder, final int position) {
        Order order = data.get(position);
        String nr = safe(order.getNr());
        String displayNr = !nr.isEmpty() ? nr : String.valueOf(order.getId());

        holder.orderNumberTextView.setText("Zamówienie nr " + (displayNr.isEmpty() ? "----" : displayNr));

        holder.titleTextView.setText("");
        holder.titleTextView.setVisibility(View.GONE);

        holder.orderNumberTextView.setSingleLine(true);
        holder.orderNumberTextView.setEllipsize(android.text.TextUtils.TruncateAt.END);


        String statusPl = mapStatusToPl(order.getStatus());
        holder.statusTextView.setText(statusPl);

        String rodzajPl = mapDeliveryToPl(order.getRodzaj_dost());
        holder.rodzajDostawyTextView.setText(rodzajPl);

        holder.formaZaplatyTextView.setText(isEmpty(order.getForma_zap()) ? "brak" : order.getForma_zap());

        holder.adresDostawyTextView.setText(isEmpty(order.getAdres_dostawy()) ? "----" : order.getAdres_dostawy());
        holder.cenaBruttoTextView.setText(order.getCena_brutto() == 0 ? "----" : String.valueOf(order.getCena_brutto()));
        holder.walutaTextView.setText(isEmpty(order.getWaluta()) ? "----" : order.getWaluta());
        holder.odbiorcaTextView.setText(isEmpty(order.getOdbiorca()) ? "brak" : order.getOdbiorca());

        String dataZmiana = formatDateOrEmpty(order.getData_zmiana());
        holder.dataZmianaTextView.setText(dataZmiana.isEmpty() ? "brak" : dataZmiana);

        holder.child_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        loadOrderDetails(order.getId(), holder);

        holder.imageButton.setOnClickListener(v -> {
            YoYo.with(Techniques.RotateIn).duration(200).repeat(0).playOn(holder.imageButton);
            order.setExpanded(!order.isExpanded());
            notifyItemChanged(position);
        });
        holder.orderNumberTextView.setOnClickListener(v -> {
            order.setExpanded(!order.isExpanded());
            notifyItemChanged(position);
        });

        boolean isExpanded = order.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Order> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout expandableLayout;
        ImageButton imageButton;
        TextView titleTextView, adresDostawyTextView, cenaBruttoTextView, dataZmianaTextView, formaZaplatyTextView,
                odbiorcaTextView, rodzajDostawyTextView, statusTextView, walutaTextView, orderNumberTextView;
        RecyclerView child_rv;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            orderNumberTextView   = itemView.findViewById(R.id.orderNumberTextView);
            expandableLayout      = itemView.findViewById(R.id.expandableLayout);
            titleTextView         = itemView.findViewById(R.id.textView1);
            adresDostawyTextView  = itemView.findViewById(R.id.adresTextView);
            cenaBruttoTextView    = itemView.findViewById(R.id.plotTextView);
            dataZmianaTextView    = itemView.findViewById(R.id.data_przyjecia_zamowienia);
            formaZaplatyTextView  = itemView.findViewById(R.id.formaZaplatyTextView_validate);
            odbiorcaTextView      = itemView.findViewById(R.id.zamawiajacy);
            rodzajDostawyTextView = itemView.findViewById(R.id.rodzajDostawyText_validate);
            statusTextView        = itemView.findViewById(R.id.textView25);
            walutaTextView        = itemView.findViewById(R.id.walutaTextView_validate);
            child_rv              = itemView.findViewById(R.id.child_rv);
            imageButton           = itemView.findViewById(R.id.imageButton);
        }
    }


    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s) || s.trim().isEmpty();
    }

    private static String formatDateOrEmpty(long millis) {
        if (millis <= 0) return "";
        try {
            return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(millis));
        } catch (Exception e) {
            return "";
        }
    }

    private static String mapStatusToPl(String status) {
        String s = safe(status).toUpperCase(Locale.ROOT);
        switch (s) {
            case "READY":
            case "IN_STOCK":
                return "Gotowe";
            case "PARTIALLY_SENT":
            case "PARTIAL_SENT":
            case "PARTIAL":
                return "Częściowo wyjechało";
            case "SENT":
                return "Wyjechało";
            case "IN_PROGRESS":
            case "MODIFIED":
            case "DURING_THE_WITHDRAWAL":
            case "PLACED":
            case "STARTED":
                return "W trakcie realizacji";
            case "WITHDRAWN":
            case "DONE":
                return "Zakończone";
            default:
                return s.isEmpty() ? "—" : s; // pokaż oryginalny kod, jeśli nieznany
        }
    }

    private static String mapDeliveryToPl(String rodzaj) {
        String r = safe(rodzaj).toUpperCase(Locale.ROOT);
        switch (r) {
            case "COURIER":              return "Wysyłka kurierem";
            case "COURIER_DOMARTSTYL":   return "Transport DomArtStyl";
            case "COLLECT_IN_PERSON":    return "Odbiór osobisty";
            case "CUSTOMER_COURIER":     return "Odbiór kurierem";
            case "TO_BE_DETERMINED":     return "Do ustalenia";
            default:                     return isEmpty(rodzaj) ? "—" : rodzaj;
        }
    }


    private void loadOrderDetails(long orderId, ViewHolder holder) {
        List<OrderActivity> orderActivityList = new ArrayList<>();
        List<PositionActivityMerge> mergedList = new ArrayList<>();
        holder.child_rv.setAdapter(new OrdersChildAdapter(mergedList, holder.child_rv.getContext()));

        FirebaseDatabase.getInstance().getReference()
                .child("czynnosci")
                .child(String.valueOf(orderId))
                .get()
                .addOnSuccessListener(taskSnap -> {
                    if (taskSnap != null && taskSnap.exists()) {
                        for (DataSnapshot itemSnapshot : taskSnap.getChildren()) {
                            OrderActivity act = itemSnapshot.getValue(OrderActivity.class);
                            if (act != null) orderActivityList.add(act);
                        }
                    }

                    FirebaseDatabase.getInstance().getReference()
                            .child("poz_zlec")
                            .child(String.valueOf(orderId))
                            .get()
                            .addOnSuccessListener(posSnap -> {
                                mergedList.clear();
                                if (posSnap != null && posSnap.exists()) {
                                    for (DataSnapshot itemSnapshot : posSnap.getChildren()) {
                                        OrderPosition orderPosition = itemSnapshot.getValue(OrderPosition.class);
                                        if (orderPosition != null) {
                                            mergedList.add(new PositionActivityMerge(
                                                    orderPosition,
                                                    findActivityFor(orderPosition.getZlec_id(), orderActivityList)
                                            ));
                                        }
                                    }
                                }
                                holder.child_rv.setAdapter(new OrdersChildAdapter(mergedList, holder.child_rv.getContext()));
                            });
                });
    }

    private OrderActivity findActivityFor(long positionId, List<OrderActivity> orderActivities) {
        for (OrderActivity a : orderActivities) {
            if (a != null && a.getZlec_id() == positionId) return a;
        }
        return null;
    }
}


package com.example.domartorders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartLoginActivity extends Activity {

    private static final String TAG = StartLoginActivity.class.getSimpleName();
    TextInputLayout editEmailText;
    TextInputLayout editPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        editEmailText = findViewById(R.id.text_input_email);
        editPasswordText = findViewById(R.id.text_input_password);
        Button loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener(v -> {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(0)
                    .playOn(findViewById(R.id.loginButton));

            if (!validateEmail() | !validatePassword()) {
                return;
            }

            String login = editEmailText.getEditText().getText().toString();
            String passwordForLogin = editPasswordText.getEditText().getText().toString();
            if (login.trim().length() > 0 && passwordForLogin.trim().length() > 0) {
                firebaseAuth.signInWithEmailAndPassword(editEmailText.getEditText().getText().toString(), editPasswordText.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete success: ");
                            startActivity(new Intent(StartLoginActivity.this, GalleryActivity.class));
                            StartLoginActivity.this.finish();
                        } else {
                            Toast.makeText(StartLoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onComplete fail :");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(StartLoginActivity.this, MainActivity.class));
            StartLoginActivity.this.finish();
        } else {
            //zostaje w tej aktywności
        }
    }

    public void onClick(View view) {
        startActivity(new Intent(StartLoginActivity.this, RegistrationActivity.class));
    }

    private boolean validateEmail() {
        String email = editEmailText.getEditText().getText().toString().trim();
        String checkEmail = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";

        if (email.isEmpty()) {
            editEmailText.setError("Wprowadź email");
            return false;
        } else if (!email.matches(checkEmail)) {
            editEmailText.setError("Nieprawidłowy email");
            return false;
        } else {
            editEmailText.setError(null);
            editEmailText.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = editPasswordText.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            editPasswordText.setError("Wprowadź hasło");
            return false;
        } else {
            editPasswordText.setError(null);
            editPasswordText.setErrorEnabled(false);
            return true;
        }
    }
}




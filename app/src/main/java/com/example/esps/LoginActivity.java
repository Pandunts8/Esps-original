package com.example.esps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email_et);
        editTextPassword = findViewById(R.id.password_et);
        buttonLogin = findViewById(R.id.sign_in);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.sign_up_now);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                boolean isValid = true;


                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Please enter a valid email address.");
                    progressBar.setVisibility(View.GONE);
                    isValid = false;
                }

                if (password.length() < 5) {
                    editTextPassword.setError("Password must be more than 5 characters");
                    progressBar.setVisibility(View.GONE);
                    isValid = false;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Please enter  password.");
                    progressBar.setVisibility(View.GONE);
                    isValid = false;
                }

                if(isValid) {
                    loginUser(email,password);
                }
            }
        });
    }
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (mAuth.getCurrentUser() != null && !mAuth.getCurrentUser().isEmailVerified()) {
                mAuth.signOut();
                Toast.makeText(this, "Email isn't verified",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!task.isSuccessful()) {
                Toast.makeText(this, "Login failed: " + Objects.
                                requireNonNull(task.getException()).getMessage(),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Logged in Successfully",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
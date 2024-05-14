package com.example.esps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextUsername, repeatPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    private  FirebaseFirestore mDb;

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

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email_et);
        editTextPassword = findViewById(R.id.password_et);
        buttonReg = findViewById(R.id.sign_up);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.sign_in_now);
        editTextUsername = findViewById(R.id.username_et);
        repeatPassword = findViewById(R.id.repeatPassword_et);
        mDb = FirebaseFirestore.getInstance();


        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i)) || source.charAt(i) == '$' || source.charAt(i) == '%' || source.charAt(i) == '>') {
                        return "";
                    }
                }
                return null;
            }
        };

        editTextUsername.setFilters(new InputFilter[] { filter });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String repeatPass = repeatPassword.getText().toString().trim();

                boolean isValid = true;

                if (TextUtils.isEmpty(username)) {
                    editTextUsername.setError("Please enter your username");
                    progressBar.setVisibility(View.GONE);
                    isValid = false;
                }


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

                if (TextUtils.isEmpty(email)) {
                    editTextPassword.setError("Please enter  password.");
                    progressBar.setVisibility(View.GONE);
                    isValid = false;
                }

                if(!password.equals(repeatPass)) {
                    repeatPassword.setError("Passwords do not match");
                    progressBar.setVisibility(View.GONE);
                    isValid = false;
                }

                if(isValid){createUser(username,email,password);}
            }
        });
    }


    public void createUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this,
                        "The email address is already in use by another account.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                    .addOnCompleteListener(task1 -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(this ,
                                    "Error! Maybe this account already exists?",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(this , "User registered successfully." +
                                "Please verify your Email", Toast.LENGTH_SHORT).show();
                        createUserInDB(username, email,
                                password, mAuth.getCurrentUser());
                        mAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);{
                            startActivity(intent);
                            finish();
                        }
                    });
        });
    }

    public void createUserInDB(final String username, final String email,
                               final String password, FirebaseUser currentUser) {
        String userId = currentUser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("password", password);

        mDb.collection("Users").document(userId).set(user);
    }
}
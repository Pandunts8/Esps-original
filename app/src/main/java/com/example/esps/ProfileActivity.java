package com.example.esps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.BiConsumer;

public class ProfileActivity extends AppCompatActivity {
    private TextView mUsernameTextView, mEmailTextView;
    private  FirebaseFirestore mDb;
    private Button logout;
    private final FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mDb = FirebaseFirestore.getInstance();
        mUsernameTextView = findViewById(R.id.name);
        mEmailTextView = findViewById(R.id.email);
        logout = findViewById(R.id.logout);

        updateProfileUI((username, email) -> {

                mUsernameTextView.setText(username);
                mEmailTextView.setText(email);


        });
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            mUsernameTextView.setText("");
            mEmailTextView.setText("");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void updateProfileUI(BiConsumer<String, String> callback) {

        if (mCurrentUser == null) {
            return;
        }
        String userId = mCurrentUser.getUid();

        mDb.collection("Users").document(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                Log.d("FirebaseService", "get failed with ",
                        task.getException());
                return;
            }

            DocumentSnapshot document = task.getResult();
            if (!document.exists()) {
                Log.d("FirebaseService", "No such user");
            }

            String username = document.getString("username");
            String email = document.getString("email");
            if (callback != null) {
                callback.accept(username, email);
            }
        });
    }
}
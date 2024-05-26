package com.example.esps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Consumer;



public class MainActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    private FirebaseFirestore mDb;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btnCustomer);
        mDb = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompanyListActivity.class);
                startActivity(intent);
            }

        });
        Button button1 = findViewById(R.id.btnSeller);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompanyListActivity1.class);

                startActivity(intent);
            }

        });

        Button button2 = findViewById(R.id.btnadmin);
        checkIfUserIsAdmin(isAdmin -> {
            if (!isAdmin) {
                button2.setVisibility(View.GONE);
                return;
            }
            button2.setVisibility(View.VISIBLE);
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CompanyListAdminka.class);
                startActivity(intent);
            }

        });
    }
    private void checkIfUserIsAdmin(Consumer<Boolean> callback) {
        if (mCurrentUser == null) {
            callback.accept(false);
            return;
        }
        String userId = mCurrentUser.getUid();
        mDb.collection("Users").document(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                callback.accept(false);
                return;
            }
            Boolean isAdmin = task.getResult().getBoolean("admin");
            callback.accept(isAdmin != null && isAdmin);
        });
    }
}
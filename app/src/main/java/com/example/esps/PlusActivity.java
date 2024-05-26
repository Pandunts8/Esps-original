package com.example.esps;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlusActivity extends AppCompatActivity {
    private TextView textViewEnergyPrice, textViewPower, textViewProductivity, textViewUnitCost, textViewMaintenanceCost;
    private DatabaseReference databaseReference;
    private String companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        // Initialize views
        textViewEnergyPrice = findViewById(R.id.editTextEnergyPrice);
        textViewPower = findViewById(R.id.editTextPower);
        textViewProductivity = findViewById(R.id.editTextProductivity);
        textViewUnitCost = findViewById(R.id.editTextUnitCost);
        textViewMaintenanceCost = findViewById(R.id.editTextMaintenanceCost);

        // Get company ID from the intent
        companyId = getIntent().getStringExtra("COMPANY_ID");

        // Check if companyId is null
        if (companyId == null || companyId.isEmpty()) {
            System.out.println("Error: Company ID is null or empty");
            return;
        }

        // Initialize Firebase Database reference to the company's data
        databaseReference = FirebaseDatabase.getInstance().getReference("General/companies").child(companyId);

        // Load data from Firebase
        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        textViewEnergyPrice.setText(String.valueOf(dataSnapshot.child("energyPrice").getValue(Double.class)));
                        textViewPower.setText(String.valueOf(dataSnapshot.child("solarPower").getValue(Double.class)));
                        textViewProductivity.setText(String.valueOf(dataSnapshot.child("production").getValue(Double.class)));
                        textViewUnitCost.setText(String.valueOf(dataSnapshot.child("unitCost").getValue(Double.class)));
                        textViewMaintenanceCost.setText(String.valueOf(dataSnapshot.child("maintenanceCost").getValue(Double.class)));
                    } catch (Exception e) {
                        System.out.println("Error parsing data: " + e.getMessage());
                    }
                } else {
                    System.out.println("Data snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

package com.example.esps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyListAdminka extends AppCompatActivity {

    private ListView listViewCompanies;
    private List<Company> companies;
    private DatabaseReference databaseCompanies;
    private CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list_adminka);

        listViewCompanies = findViewById(R.id.listViewCompanies);
        companies = new ArrayList<>();
        adapter = new CompanyAdapter(this, companies);
        listViewCompanies.setAdapter(adapter);
        databaseCompanies = FirebaseDatabase.getInstance().getReference("companies");

        listViewCompanies.setOnItemClickListener((adapterView, view, position, id) -> {
            Company selectedCompany = companies.get(position);
            Intent detailIntent = new Intent(CompanyListAdminka.this, Adminka.class);
            detailIntent.putExtra("company", selectedCompany); // Ensure that Company implements Parcelable
            startActivity(detailIntent);
        });

        readDataFromDatabase();
    }

    private void readDataFromDatabase() {
        databaseCompanies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companies.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Company company = snapshot.getValue(Company.class);
                    if (company != null) {
                        companies.add(company);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CompanyListAdminka.this, "Failed to load companies.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


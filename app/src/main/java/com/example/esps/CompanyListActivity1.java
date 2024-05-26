package com.example.esps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyListActivity1 extends AppCompatActivity {

    private ListView listViewCompanies;
    private List<Company> companies;
    private DatabaseReference databaseCompanies;
    private CompanyAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list1);
        ImageView image = findViewById(R.id.profileimage);
        listViewCompanies = findViewById(R.id.listViewCompanies);
        companies = new ArrayList<>();
        adapter = new CompanyAdapter(this, companies);
        listViewCompanies.setAdapter(adapter);
        databaseCompanies = FirebaseDatabase.getInstance().getReference("General/companies");

        listViewCompanies.setOnItemClickListener((adapterView, view, position, id) -> {
            Company selectedCompany = companies.get(position);
            Intent detailIntent = new Intent(CompanyListActivity1.this, CompanyDetailActivity.class);
            detailIntent.putExtra("COMPANY", selectedCompany);
            startActivity(detailIntent);
        });

        image.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        readDataFromDatabase();

    }

    public void addcompany(View view) {
        startActivity(new Intent(CompanyListActivity1.this, SellerActivity.class));
    }

    private void readDataFromDatabase() {
        databaseCompanies.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companies.clear();
                for (DataSnapshot companySnapshot : dataSnapshot.getChildren()) {
                    try {
                        Company company = companySnapshot.getValue(Company.class);
                        if (company != null && company.getStatus() == "Accepted" ) {
                            companies.add(company);
                        }
                    } catch (DatabaseException e) {
                        Log.e("CompanyListActivity", "Error parsing company data", e);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("CompanyListActivity", "Failed to read value.", databaseError.toException());
                Toast.makeText(CompanyListActivity1.this, "Failed to load companies.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

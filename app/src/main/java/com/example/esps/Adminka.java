package com.example.esps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class Adminka extends AppCompatActivity {

    private TextView textViewCompanyName, textViewCompanyAddress, textViewCompanyPower, textViewCompanyProductivity,
            textViewCompanyLicensePeriod, textViewCompanyTariff, textViewCompanySpecification, textViewCompanyInsurance;
    private ImageView imageViewCompany;
    private DatabaseReference databaseCompany;
    private String companyId;
    private Company company;
    private Button confirm, decline;
    private DatabaseReference databaseCompanies;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminka);

        textViewCompanyName = findViewById(R.id.textViewCompanyName);
        textViewCompanyAddress = findViewById(R.id.textViewCompanyAddress);
        textViewCompanyPower = findViewById(R.id.textViewCompanyPower);
        textViewCompanyProductivity = findViewById(R.id.textViewCompanyProductivity);
        textViewCompanyLicensePeriod = findViewById(R.id.textViewCompanyLicensePeriod);
        textViewCompanyTariff = findViewById(R.id.textViewCompanyTariff);
        textViewCompanySpecification = findViewById(R.id.textViewCompanySpecification);
        textViewCompanyInsurance = findViewById(R.id.textViewCompanyInsurance);
        imageViewCompany = findViewById(R.id.imageViewCompany);
        databaseCompanies = FirebaseDatabase.getInstance().getReference("General/companies");

        decline = findViewById(R.id.decline);
        confirm = findViewById(R.id.confirm);
        decline.setOnClickListener(view -> updateCompanyStatus("Declined"));
        confirm.setOnClickListener(view -> updateCompanyStatus("Accepted"));

        company = getIntent().getParcelableExtra("COMPANY");
        companyId = company.getId();
        if (companyId != null) {
            databaseCompany = FirebaseDatabase.getInstance().getReference("General/companies").child(companyId);
            loadCompanyDetails();
        }
    }

    private void updateCompanyStatus(String status) {
        if (companyId != null) {
            databaseCompanies.child(companyId).child("status").setValue(status)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Adminka.this, "Status updated to " + status, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Adminka.this, CompanyListAdminka.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {Toast.makeText(Adminka.this,
                            "Failed to update status: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Adminka.this, CompanyListAdminka.class);
                        startActivity(intent);

                    });
        } else {
            Toast.makeText(this, "Error: Company ID is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCompanyDetails() {
        databaseCompany.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Company company = dataSnapshot.getValue(Company.class);
                if (company != null) {
                    textViewCompanyName.setText("Կազմակերպություն: " + company.getName());
                    textViewCompanyAddress.setText(company.getAddress());
                    textViewCompanyPower.setText(company.getPower() + " KW");
                    textViewCompanyProductivity.setText(company.getProductivity() + " units");
                    textViewCompanyLicensePeriod.setText(company.getLicensePeriod() + " years");
                    textViewCompanyTariff.setText(company.getPowerPurchaseTariff() + " per unit");
                    textViewCompanySpecification.setText(company.getTechnicalSpecification());
                    textViewCompanyInsurance.setText(company.getInsurance());

                    if (company.getImageUrl() != null && !company.getImageUrl().isEmpty()) {
                        new DownloadImageTask(imageViewCompany).execute(company.getImageUrl());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("CompanyDetailActivity", "Database error: " + databaseError.getMessage(), databaseError.toException());
                Toast.makeText(Adminka.this, "Failed to load company details. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

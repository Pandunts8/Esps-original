package com.example.esps;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class CompanyDetailActivity extends AppCompatActivity {

    private TextView textViewCompanyName, textViewCompanyAddress, textViewCompanyPower, textViewCompanyProductivity,
            textViewCompanyLicensePeriod, textViewCompanyTariff, textViewCompanySpecification, textViewCompanyInsurance;
    private ImageView imageViewCompany;
    private DatabaseReference databaseCompany;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);

        // Initialize views
        textViewCompanyName = findViewById(R.id.textViewCompanyName);
        textViewCompanyAddress = findViewById(R.id.textViewCompanyAddress);
        textViewCompanyPower = findViewById(R.id.textViewCompanyPower);
        textViewCompanyProductivity = findViewById(R.id.textViewCompanyProductivity);
        textViewCompanyLicensePeriod = findViewById(R.id.textViewCompanyLicensePeriod);
        textViewCompanyTariff = findViewById(R.id.textViewCompanyTariff);
        textViewCompanySpecification = findViewById(R.id.textViewCompanySpecification);
        textViewCompanyInsurance = findViewById(R.id.textViewCompanyInsurance);
        imageViewCompany = findViewById(R.id.imageViewCompany);

        Button button1 = findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDetailActivity.this, Calculator.class);
                startActivity(intent);
            }

        });
            // Get the company ID from the Intent extras
        String companyId = getIntent().getStringExtra("COMPANY_ID");
        if (companyId != null) {
            databaseCompany = FirebaseDatabase.getInstance().getReference("companies").child(companyId);
            loadCompanyDetails();
        }
    }

    private void loadCompanyDetails() {
        databaseCompany.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Company company = dataSnapshot.getValue(Company.class);
                if (company != null) {
                    textViewCompanyName.setText(company.getName());
                    textViewCompanyAddress.setText(company.getAddress());
                    textViewCompanyPower.setText(String.valueOf(company.getPower()));
                    textViewCompanyProductivity.setText(String.valueOf(company.getProductivity()));
                    textViewCompanyLicensePeriod.setText(String.valueOf(company.getLicensePeriod()));
                    textViewCompanyTariff.setText(String.valueOf(company.getPowerPurchaseTariff()));
                    textViewCompanySpecification.setText(company.getTechnicalSpecification());
                    textViewCompanyInsurance.setText(company.getInsurance());

                    if (company.getImageUrl() != null && !company.getImageUrl().isEmpty()) {
                        new DownloadImageTask(imageViewCompany).execute(company.getImageUrl());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log the error to the console
                Log.e("CompanyDetailActivity", "Database error: " + databaseError.getMessage(), databaseError.toException());

                // Show a user-friendly error message
                runOnUiThread(() -> Toast.makeText(CompanyDetailActivity.this, "Failed to load company details. Please try again later.", Toast.LENGTH_LONG).show());
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

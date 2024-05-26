package com.example.esps;

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

public class CompanyDetailActivity extends AppCompatActivity {

    private TextView textViewCompanyName, textViewCompanyAddress, textViewCompanyPower, textViewCompanyProductivity,
            textViewCompanyLicensePeriod, textViewCompanyTariff, textViewCompanySpecification, textViewCompanyInsurance;
    private ImageView imageViewCompany;
    private DatabaseReference databaseCompany;
    private String companyId;
    private Company company;

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

        Button buttonPlus = findViewById(R.id.button_plus);
        buttonPlus.setOnClickListener(v -> {
            Log.d("CompanyDetailActivity", "Plus button clicked");
            if (companyId != null) {
                Log.d("CompanyDetailActivity", "Company ID: " + companyId);
                openPlusActivity();
            } else {
                Log.e("CompanyDetailActivity", "Company ID is null");
            }
        });


        Button buttonCalculator = findViewById(R.id.calculator);
        buttonCalculator.setOnClickListener(v -> openCalculator());

        company = getIntent().getParcelableExtra("COMPANY");
        companyId = company.getId();
        if (companyId != null) {
            databaseCompany = FirebaseDatabase.getInstance().getReference("General/companies").child(companyId);
            loadCompanyDetails();
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
                Toast.makeText(CompanyDetailActivity.this, "Failed to load company details. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openPlusActivity() {
        Intent intent = new Intent(CompanyDetailActivity.this, PlusActivity.class);
        intent.putExtra("COMPANY_ID", companyId);
        startActivity(intent);
    }

    private void openCalculator() {
        Intent intent = new Intent(CompanyDetailActivity.this, Calculator.class);
        intent.putExtra("COMPANY", company);
        startActivity(intent);
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

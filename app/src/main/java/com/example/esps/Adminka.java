package com.example.esps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Adminka extends AppCompatActivity {

    private ImageView companyImageView;
    private TextView textViewCompanyName, textViewCompanyAddress, textViewCompanyPower,
            textViewCompanyProductivity, textViewCompanyLicensePeriod,
            textViewCompanyTariff, textViewCompanySpecification, textViewCompanyInsurance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminka);

        // Initialize views
        companyImageView = findViewById(R.id.imageViewCompany);
        textViewCompanyName = findViewById(R.id.textViewCompanyName);
        textViewCompanyAddress = findViewById(R.id.textViewCompanyAddress);
        textViewCompanyPower = findViewById(R.id.textViewCompanyPower);
        textViewCompanyProductivity = findViewById(R.id.textViewCompanyProductivity);
        textViewCompanyLicensePeriod = findViewById(R.id.textViewCompanyLicensePeriod);
        textViewCompanyTariff = findViewById(R.id.textViewCompanyTariff);
        textViewCompanySpecification = findViewById(R.id.textViewCompanySpecification);
        textViewCompanyInsurance = findViewById(R.id.textViewCompanyInsurance);

        // Get the data from intent
        Company company = getIntent().getParcelableExtra("company");
        if (company != null) {
            textViewCompanyName.setText(company.getName());
            textViewCompanyAddress.setText(company.getAddress());
            textViewCompanyPower.setText(String.valueOf(company.getPower()));
            textViewCompanyProductivity.setText(String.valueOf(company.getProductivity()));
            textViewCompanyLicensePeriod.setText(String.valueOf(company.getLicensePeriod()));
            textViewCompanyTariff.setText(String.valueOf(company.getPowerPurchaseTariff()));
            textViewCompanySpecification.setText(company.getTechnicalSpecification());
            textViewCompanyInsurance.setText(company.getInsurance());
            new ImageLoadTask(company.getImageUrl()).execute();
        } else {
            companyImageView.setImageResource(R.drawable.ic_launcher_background); // Default image if none is provided
        }
    }

    private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private String url;

        public ImageLoadTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL connection = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream input = httpURLConnection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                companyImageView.setImageBitmap(result);
            } else {
                companyImageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}

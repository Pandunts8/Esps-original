package com.example.esps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SellerActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText companyNameEditText, addressEditText, powerEditText, productivityEditText,
            licensePeriodEditText, powerPurchaseTariffEditText, technicalSpecificationEditText, insuranceEditText;
    private ImageView companyImageView;
    private Uri imageUri;
    private DatabaseReference databaseCompanies;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        companyNameEditText = findViewById(R.id.companyNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        powerEditText = findViewById(R.id.powerEditText);
        productivityEditText = findViewById(R.id.productivityEditText);
        licensePeriodEditText = findViewById(R.id.licensePeriodEditText);
        powerPurchaseTariffEditText = findViewById(R.id.powerPurchaseTariffEditText);
        technicalSpecificationEditText = findViewById(R.id.technicalSpecificationEditText);
        insuranceEditText = findViewById(R.id.insuranceEditText);
        companyImageView = findViewById(R.id.companyImageView);
        Button addCompanyButton = findViewById(R.id.addCompanyButton);
        Button chooseImageButton = findViewById(R.id.chooseImageButton);

        databaseCompanies = FirebaseDatabase.getInstance().getReference("companies");
        storageReference = FirebaseStorage.getInstance().getReference("companyImages");

        addCompanyButton.setOnClickListener(view -> {
            if (imageUri != null) {
                uploadImageAndAddCompany(imageUri);
            } else {
                Toast.makeText(SellerActivity.this, "Please select an image.", Toast.LENGTH_SHORT).show();
            }
        });

        chooseImageButton.setOnClickListener(v -> openFileChooser());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            companyImageView.setImageURI(imageUri);
        }
    }

    private void uploadImageAndAddCompany(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(SellerActivity.this, "Image is null, cannot upload", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fileReference = storageReference.child("companyImages/" + System.currentTimeMillis() + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Log success
                    Log.d("Upload", "Upload successful");
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Log.d("URL Retrieval", "File URL: " + uri.toString());
                        addCompany(uri.toString());
                    }).addOnFailureListener(e -> {
                        Log.e("URL Retrieval", "Failed to get download URL", e);
                        Toast.makeText(SellerActivity.this, "Failed to retrieve file URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Upload", "Upload failed", e);
                    Toast.makeText(SellerActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }


    private void addCompany(String imageUrl) {
        String name = companyNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        double power = parseDouble(powerEditText.getText().toString().trim());
        double productivity = parseDouble(productivityEditText.getText().toString().trim());
        int licensePeriod = parseInt(licensePeriodEditText.getText().toString().trim());
        double powerPurchaseTariff = parseDouble(powerPurchaseTariffEditText.getText().toString().trim());
        String technicalSpecification = technicalSpecificationEditText.getText().toString().trim();
        String insurance = insuranceEditText.getText().toString().trim();

        Intent intent = new Intent(this, Adminka.class);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        intent.putExtra("power", power);
        intent.putExtra("productivity", productivity);
        intent.putExtra("licensePeriod", licensePeriod);
        intent.putExtra("powerPurchaseTariff", powerPurchaseTariff);
        intent.putExtra("technicalSpecification", technicalSpecification);
        intent.putExtra("insurance", insurance);
        startActivity(intent);
//        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(imageUrl)) {
//            String id = databaseCompanies.push().getKey();
//            Company company = new Company(id, name, address, power, productivity, licensePeriod,
//                    powerPurchaseTariff, technicalSpecification, insurance, imageUrl);
//
//            databaseCompanies.child(id).setValue(company)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(SellerActivity.this, "Company added", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(SellerActivity.this, "Failed to add company", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(this, "You must enter all the details and select an image", Toast.LENGTH_SHORT).show();
//        }
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number entered", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number entered", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
}

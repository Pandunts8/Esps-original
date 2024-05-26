    package com.example.esps;

    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.database.DatabaseReference;

    public class Calculator extends AppCompatActivity {
        private TextView editTextPartialInvestment, editTextPartialInvestmentShare, editTextAnnualReturn,
                editText20YearsExpectedIncome, editTextPaybackPeriod, editTextAnnualProduction,
                editTextTotalInvestment, editText20YearsNetCashFlow, editText20YearsDepreciation, editTextAverageAnnualReturn;
        private Button calculateButton;
        private DatabaseReference databaseReference;

        private double storedNum1, storedNum2, storedNum3, storedNum4, storedNum5;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calculator);

            initializeViews();
            fetchDataFromFirebase();
        }

        private void initializeViews() {
            calculateButton = findViewById(R.id.calculateButton);
            editTextPartialInvestment = findViewById(R.id.editTextPartialInvestment);

            editTextPartialInvestmentShare = findViewById(R.id.editTextPartialInvestmentShare1);
            editTextAnnualReturn = findViewById(R.id.editTextAnnualReturn1);
            editText20YearsExpectedIncome = findViewById(R.id.editText20YearsExpectedIncome1);
            editTextPaybackPeriod = findViewById(R.id.editTextPaybackPeriod1);
            editTextAnnualProduction = findViewById(R.id.editTextAnnualProduction1);
            editTextTotalInvestment = findViewById(R.id.editTextTotalInvestment1);
            editText20YearsNetCashFlow = findViewById(R.id.editText20YearsNetCashFlow1);
            editText20YearsDepreciation = findViewById(R.id.editText20YearsDepreciation1);
            editTextAverageAnnualReturn = findViewById(R.id.editTextAverageAnnualReturn1);

            calculateButton.setOnClickListener(v -> calculateValues());
        }

        private void fetchDataFromFirebase() {
            Company company = getIntent().getParcelableExtra("COMPANY");
            if (company != null) {
                storedNum1 = company.getEnergyPrice();
                storedNum2 = company.getPower();
                storedNum3 = company.getProduction();
                storedNum4 = company.getUnitCost();
                storedNum5 = company.getMaintenanceCost();
            }

        }



        private void calculateValues() {
            try {
                double num1 = Double.parseDouble(editTextPartialInvestment.getText().toString().trim());

                double num6 = storedNum2 * storedNum3;
                double num7 = storedNum2 * storedNum4;
                double num9 = num7;
                double num8 = num6 * storedNum1 * 20;
                double num2 = num1 / num7;
                double num4 =  (num8 - num9 - storedNum5) * num2;
                double num10 = num4/20;
                double num3 = (num10 / num1) * 100;
                double num5 = num1 / num10;


                editTextPartialInvestmentShare.setText(String.valueOf(num2));
                editTextAnnualReturn.setText(String.valueOf(num3));
                editText20YearsExpectedIncome.setText(String.valueOf(num4));
                editTextPaybackPeriod.setText(String.valueOf(num5));
                editTextAnnualProduction.setText(String.valueOf(num6));
                editTextTotalInvestment.setText(String.valueOf(num7));
                editText20YearsNetCashFlow.setText(String.valueOf(num8));
                editText20YearsDepreciation.setText(String.valueOf(num9));
                editTextAverageAnnualReturn.setText(String.valueOf(num10));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        }
    }

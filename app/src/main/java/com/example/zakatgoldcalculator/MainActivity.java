package com.example.zakatgoldcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Toolbar my_toolbar;
    EditText etWeight, etValue;
    Spinner goldType;
    TextView tvTotalValue, tvZakatPayable, tvTotalZakat;
    Button btnCalculate, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // The Toolbar defined in the layout has the id "my_toolbar".
        my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etWeight = findViewById(R.id.etWeight);
        etValue = findViewById(R.id.etValue);
        goldType = findViewById(R.id.goldType);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

    }

    public void resetForm(){
        etWeight.setText("");
        etValue.setText("");
        goldType.setSelection(0);

        tvTotalValue.setText("Total Value:");
        tvZakatPayable.setText("Zakat Payable Value:");
        tvTotalZakat.setText("Total Zakat (2.5%):");

        etWeight.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my Zakat Gold Calculator app: https://github.com/zahiraysrna-dotcom");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        } else if (item.getItemId() == R.id.item_about) {
            Intent aboutIntent = new Intent(this, aboutActivity.class);
            startActivity(aboutIntent);
        }
        return false;
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Input Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void calculateZakat() {
        String weightStr = etWeight.getText().toString();
        String valueStr = etValue.getText().toString();

        if (weightStr.isEmpty() || valueStr.isEmpty()) {
            showError("Please enter weight and value per gram.");
            return;
        }

        double weight, valuePerGram;
        try {
            weight = Double.parseDouble(weightStr);
            valuePerGram = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers only.");
            return;
        }

        if (weight < 0) {
            showError("Weight cannot be negative.");
            etWeight.requestFocus();
            return;
        }
        if (valuePerGram <= 0) {
            showError("Value per gram must be greater than 0.");
            etValue.requestFocus();
            return;
        }

            String type = goldType.getSelectedItem().toString();
            int uruf = type.equals("Keep") ? 85 : 200;

            double totalValue = weight * valuePerGram;
            double payableWeight = Math.max(0, weight - uruf);
            double zakatPayable = payableWeight * valuePerGram;
            double totalZakat = zakatPayable * 0.025;

            tvTotalValue.setText("Total Value: RM " + String.format("%.2f", totalValue));
            tvZakatPayable.setText("Zakat Payable: RM " + String.format("%.2f", zakatPayable));
            tvTotalZakat.setText("Total Zakat: RM " + String.format("%.2f", totalZakat));
    }
}

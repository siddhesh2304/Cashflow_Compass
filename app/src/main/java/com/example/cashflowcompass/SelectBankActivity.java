package com.example.cashflowcompass;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SelectBankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);

        RadioGroup radioGroup = findViewById(R.id.bankRadioGroup);

        // Get the list of banks from Bankselect
        List<String> banklist = Bankselect.getBankList();

        // Dynamically create RadioButtons and add them to the RadioGroup
        for (String bank : banklist) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(bank);
            radioGroup.addView(radioButton);
        }

        // Set a listener to handle RadioButton selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Handle the selected bank here
            RadioButton selectedRadioButton = findViewById(checkedId);
            String selectedBank = selectedRadioButton.getText().toString();
            // You can perform actions based on the selected bank
        });
    }

    public void onSubmitClick(View view) {
        // Handle the "Submit" button click event
        // This method will be called when the "Submit" button is clicked
        // You can add code to process the selected bank or perform other actions
    }
}

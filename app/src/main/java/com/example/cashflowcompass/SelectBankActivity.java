package com.example.cashflowcompass;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

public class SelectBankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);

        RadioGroup radioGroup = findViewById(R.id.bankRadioGroup);

        // Get the map of banks from Bankselect
        Map<String, String> bankMap = Bankselect.getBankMap();

        // Dynamically create RadioButtons and add them to the RadioGroup
        for (Map.Entry<String, String> entry : bankMap.entrySet()) {
            int index = 1;
            String bank = entry.getValue();
            String accountno = entry.getKey();

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(bank + " "+accountno);
            radioButton.setId(index); // Set the RadioButton's ID to the index
            radioGroup.addView(radioButton);
        }

        // Set a listener to handle RadioButton selection
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Handle the selected bank here
            String selectedBank = bankMap.get(checkedId);
            // You can perform actions based on the selected bank
        });
    }

    public void onSubmitClick(View view) {
        // Handle the "Submit" button click event
        // This method will be called when the "Submit" button is clicked
        // You can add code to process the selected bank or perform other actions
    }
}

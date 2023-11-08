package com.example.cashflowcompass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstStartup", false);
        editor.apply();

        // Get the map of banks from Bankselect
        Map<String, String> bankMap = Bankselect.getBankMap();

        // Dynamically create RadioButtons and add them to the RadioGroup
        for (Map.Entry<String, String> entry : bankMap.entrySet()) {
            int index = 1;
            String bank = entry.getValue();
            String accountno = entry.getKey();

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(bank + " " + accountno);
            radioButton.setId(View.generateViewId()); // Set a unique ID for each RadioButton
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
        // Get the selected RadioButton ID from the RadioGroup
        RadioGroup radioGroup = findViewById(R.id.bankRadioGroup);
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId != -1) {
            // Find the RadioButton associated with the selected ID
            RadioButton selectedRadioButton = findViewById(checkedRadioButtonId);

            // Get the text of the selected RadioButton, which contains the account number
            String selectedRadioButtonText = selectedRadioButton.getText().toString();

            // Extract the account number from the text
            String[] parts = selectedRadioButtonText.split(" ");
            if (parts.length >= 2) {
                String accountno = parts[1]; // Assuming the account number is the second part

                // Store the selected account number in SharedPreferences
                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("selectedAccountNumber", accountno);
                editor.apply();

                // Retrieve the list of SMS messages using your SMS handler
                List<String> smsList = SmsHandler.viewSMSLogs(this);

                // Continue with processing the SMS messages
                // Create an instance of TransactionDataManager and pass the smsList
                TransactionDataManager transactionDataManager = new TransactionDataManager(this);
                List<TransactionDataManager.Transaction> extractedTransactions = transactionDataManager.extractTransactionsFromSMS(smsList);

                // Now you have the extracted transactions that you can use
                // You can also add them to your TransactionDataManager using addTransaction
                for (TransactionDataManager.Transaction transaction : extractedTransactions) {
                    transactionDataManager.addTransaction(transaction);
                }

            }
        } else {
            // No RadioButton is selected, handle this case if necessary
            // Log an error or display a message to the user
        }
        todashboard();



    }

    public void todashboard(){
        Intent intent = new Intent(this, Dashboard_activity.class);
        startActivity(intent);
    }
}

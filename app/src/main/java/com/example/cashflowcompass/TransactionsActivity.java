package com.example.cashflowcompass;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class TransactionsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions);

        // Create an instance of TransactionDataManager
        TransactionDataManager transactionDataManager = new TransactionDataManager(this);
        List<String> smsList = SmsHandler.viewSMSLogs(this);
        transactionDataManager.extractTransactionsFromSMS(smsList);

        List<String> filteredAmount = transactionDataManager.getFilteredAmount();
        List<String> filteredDate = transactionDataManager.getFilteredDate();
        List<String> filteredTransactionType = transactionDataManager.getFilteredTransactionType();

        // Create an array to store transaction details
        int transactionCount = filteredAmount.size();
        String[] transactionDetails = new String[transactionCount];

        for (int i = 0; i < transactionCount; i++) {
            transactionDetails[i] = "Amount: ₹ " + filteredAmount.get(i) + "\nDate: " + filteredDate.get(i) + "\nType: " + filteredTransactionType.get(i);
        }

        // Set up the ListView to display the transactions
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.transactionAmount, transactionDetails);
        listView.setAdapter(adapter);
    }
}

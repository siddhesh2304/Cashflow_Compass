package com.example.cashflowcompass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cashflowcompass.SmsHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionDataManager {
    private List<Transaction> transactions;
    private String selectedAccountNumber;
    private DatabaseReference databaseReference;
    List<String> filteredamount = new ArrayList<>();
    List<String> filtereddate = new ArrayList<>();
    List<String> filteredtranstype = new ArrayList();

    public TransactionDataManager(Context context) {
        this.transactions = new ArrayList<>();
        // Retrieve the selected account number from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        this.selectedAccountNumber = preferences.getString("selectedAccountNumber", "default_value");

        // Initialize the DatabaseReference with the reference to your Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("transactions");
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> extractTransactionsFromSMS(List<String> smsList) {
        List<Transaction> extractedTransactions = new ArrayList<>();
        List<String> dat = SmsHandler.dat(smsList);

        for (int i = 0; i < smsList.size(); i++) {
            String sms = smsList.get(i);
            if (sms.contains(selectedAccountNumber)) {
                Pattern pattern = Pattern.compile("(received|sent|credited|debited|Received|Sent|Credited|Debited) Rs\\.(\\d+\\.\\d+)");
                Matcher matcher = pattern.matcher(sms);
                while (matcher.find()) {
                    String variation = matcher.group(1); // Get the captured variation
                    if (variation.equalsIgnoreCase("sent") || variation.equalsIgnoreCase("Sent")) {
                        variation = "Debited";
                    } else if (variation.equalsIgnoreCase("Received") || variation.equalsIgnoreCase("received")) {
                        variation = "Credited";
                    }
                    String amountText = matcher.group(2); // Get the captured amount
                    double amount = Double.parseDouble(amountText); // Convert the captured amount to a double

                    filtereddate.add(dat.get(i));
                    filteredamount.add(amountText);
                    filteredtranstype.add(variation);
                }
            }
        }

        // Push filtered data to Firebase
        String Bankname = Bankselect.getBankNameForAccountNumber(selectedAccountNumber);
        for (int i = 0; i < filtereddate.size(); i++) {
            String date = filtereddate.get(i);
            String amount = filteredamount.get(i);
            String transtype = filteredtranstype.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date parsedDate = null;
            try {
                parsedDate = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Transaction transaction = new Transaction(selectedAccountNumber, Bankname, TransactionType.valueOf(transtype.toUpperCase()), Double.parseDouble(amount), parsedDate);
            addTransaction(transaction);
            pushDataToFirebase(transaction);
        }

        return extractedTransactions;
    }

    private void pushDataToFirebase(Transaction transaction) {
        Log.d("ANSER", "Pushing data to Firebase");
        String accountNumber = transaction.getAccountNumber();
        DatabaseReference accountReference = databaseReference.child(accountNumber);
        String transactionKey = accountReference.push().getKey();
        accountReference.child(transactionKey).setValue(transaction);
    }

    public void retrieveDataFromFirebase(final FirebaseCallback callback) {
        Log.d("ANSER", "Retrieving data from Firebase");
        DatabaseReference transactionsReference = databaseReference.child("transactions");
        transactionsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = childSnapshot.getValue(Transaction.class);
                    if (transaction != null) {
                        callback.onCallback(transaction);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseData", "Failed to read data from Firebase.", error.toException());
            }
        });
    }

    // Define a callback interface
    public interface FirebaseCallback {
        void onCallback(Transaction transaction);
    }

    public class Transaction {
        private String accountNumber;
        private String bankName;
        private TransactionType transactionType;
        private double amount;
        private Date date;

        public Transaction() {
            // Default constructor required for Firebase
        }

        public Transaction(String accountNumber, String bankName, TransactionType transactionType, double amount, Date date) {
            this.accountNumber = accountNumber;
            this.bankName = bankName;
            this.transactionType = transactionType;
            this.amount = amount;
            this.date = date;
        }

        // Getters and setters for each field

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public TransactionType getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }
    }

    public enum TransactionType {
        CREDITED,
        DEBITED
    }

    // Methods to access the filtered lists
    public List<String> getFilteredAmount() {
        return filteredamount;
    }

    public List<String> getFilteredDate() {
        return filtereddate;
    }

    public List<String> getFilteredTransactionType() {
        return filteredtranstype;
    }
}

package com.example.cashflowcompass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
    public TransactionDataManager(Context context) {
        this.transactions = new ArrayList<>();
        // Retrieve the selected account number from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        this.selectedAccountNumber = preferences.getString("selectedAccountNumber", "default_value");
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
        List<String> filteredamount = new ArrayList<>();
        List<String> filtereddate = new ArrayList<>();
        List<String> filteredtranstype = new ArrayList<>();
        for(int i=0;i<smsList.size();i++) {
            String sms = smsList.get(i);
            if (sms.contains(selectedAccountNumber)) {
                Pattern pattern = Pattern.compile("(received|sent|credited|debited|Received|Sent|Credited|Debited) Rs\\.(\\d+\\.\\d+)");
                Matcher matcher = pattern.matcher(sms);
                while (matcher.find()) {
                    String variation = matcher.group(1); // Get the captured variation
                    if(variation=="sent"||variation=="Sent"){
                        variation ="Debited";
                    }
                    else if(variation=="Received"||variation=="received"){
                        variation = "Credited";
                    }
                    String amountText = matcher.group(2);  // Get the captured amount
                    double amount = Double.parseDouble(amountText); // Convert the captured amount to a double

                    filtereddate.add(dat.get(i));
                    filteredamount.add(amountText);
                    filteredtranstype.add(variation);
                }
            }
        }

        Log.d("leng", String.valueOf(filteredamount.size()+filteredtranstype.size()+filtereddate.size()));

        return extractedTransactions;
    }

    private Transaction extractTransactionFromSMS(String sms, String selectedAccountNumber) {
        // Implement SMS parsing logic here based on your SMS format
        // You should extract account number, bank name, transaction type, amount, and time.

        // For demonstration, let's assume you have parsed the data into the following variables:
        String accountNumber = selectedAccountNumber; // Replace with your actual parsing logic
        String bankName = Bankselect.getBankNameForAccountNumber(accountNumber); // Get bank name from Bankselect class
        TransactionType transactionType = TransactionType.CREDITED; // Default to CREDITED
        double amount = 100.0; // Replace with your actual parsing logic
        // No need for date and time here as it's extracted in extractTransactionsFromSMS

        // Check if the SMS contains "Credited" or "Debited" and set the transaction type accordingly
        if (sms.contains("Credited")) {
            transactionType = TransactionType.CREDITED;
        } else if (sms.contains("Debited")) {
            transactionType = TransactionType.DEBITED;
        }

        // Log the values for debugging
        Log.d("Transaction", "Account Number: " + accountNumber);
        Log.d("Transaction", "Bank Name: " + bankName);
        Log.d("Transaction", "Transaction Type: " + transactionType.toString());

        // Check if the extracted account number matches the selected account number
        if (accountNumber.equals(selectedAccountNumber)) {
            // If it matches, create a new Transaction object and return it
            return new Transaction(accountNumber, bankName, transactionType, amount);
        }

        return null; // Return null if the selected account number doesn't match
    }

    private int getMonthNumber(String monthAbbreviation) {
        switch (monthAbbreviation) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
            default:
                return 0; // Unknown or error
        }
    }

    public class Transaction {
        private String accountNumber;
        private String bankName;
        private TransactionType transactionType;
        private double amount;
        private Date date;

        public Transaction(String accountNumber, String bankName, TransactionType transactionType, double amount) {
            this.accountNumber = accountNumber;
            this.bankName = bankName;
            this.transactionType = transactionType;
            this.amount = amount;
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

        public void setAmount(double amount) {
            this.amount = amount;
        }


    }

    enum TransactionType {
        CREDITED,
        DEBITED
    }
}

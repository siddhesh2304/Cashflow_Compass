package com.example.cashflowcompass;

import android.util.Log;

import java.util.ArrayList; // Import ArrayList
import java.util.HashMap; // Import HashMap
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bankselect {
    // Declare a Map to store the matched substrings and their corresponding index
    private static Map<String, String> bankMap = new HashMap<>();

    public static void performTaskOnSMSList(List<String> smsList) {
        // Define a regular expression pattern to match 'x' or 'X' followed by a 4-digit number
        String regex = "[xX**]\\d{4}";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);
        Log.e("Regex Pattern", regex);

        for (int i = 0; i < smsList.size(); i++) {
            String sms = smsList.get(i);

            // Check if the message contains the pattern
            Matcher matcher = pattern.matcher(sms);

            while (matcher.find()) {
                // Process the matched substring
                String matchedSubstring = matcher.group();
                if (sms.contains("received") || sms.contains("Received") || sms.contains("Sent") || sms.contains("sent")||sms.contains("Sender")||sms.contains("sender")||sms.contains("Credited")||sms.contains("credited")||sms.contains("debited")||sms.contains("Debited")) {
                    String bankName ="";
                    if (sms.contains("KOTAK")) {
                         bankName = "KOTAK";
                    }
                    else if(sms.contains("HDFC")) {
                         bankName =  "HDFC";
                    } else if (sms.contains("ICICI")) {
                        bankName="ICICI";
                    }
                    if (!bankMap.containsKey(matchedSubstring)&&bankName!="") {
                            bankMap.put(matchedSubstring, bankName);
                            Log.e("Banklist Item", "Bank Name: " + bankName + ", Matched Substring: " + matchedSubstring);
                        }
                    }
                }
            }
        }

    public static String getBankNameForAccountNumber(String selectedAccountNumber) {
        for (Map.Entry<String, String> entry : bankMap.entrySet()) {
            String accountNumber = entry.getKey();
            String bankName = entry.getValue();
            if (accountNumber.equals(selectedAccountNumber)) {
                return bankName;
            }
        }
        return null; // Account number not found in the bankMap
    }






    public static Map<String, String> getBankMap() {
        return bankMap;
    }
}

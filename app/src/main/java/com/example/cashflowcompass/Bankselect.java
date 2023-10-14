package com.example.cashflowcompass;

import android.util.Log;

import java.util.ArrayList; // Import ArrayList
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bankselect {
    // Declare a list to store matched substrings and initialize it
    private static List<String> banklist = new ArrayList<>();

    public static boolean check(List<String> che, String cha) {
        for (String item : che) {
            if (item.equals(cha)) {
                return true; // Match found in the list
            }
        }
        return false; // No match found in the list
    }

    public static void performTaskOnSMSList(List<String> smsList) {
        // Define a regular expression pattern to match 'x' or 'X' followed by a 4-digit number
        String regex = "[xX]\\d{4}";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);
        Log.e("Regex Pattern", regex);

        for (String sms : smsList) {
            // Check if the message contains the pattern
            Matcher matcher = pattern.matcher(sms);

            while (matcher.find()) {
                // Process the matched substring
                String matchedSubstring = matcher.group();
                if (sms.contains("received") || sms.contains("Received") || sms.contains("Sent") || sms.contains("sent")) {
                    if (!check(banklist, matchedSubstring)) {
                        // Add the matched substring to the banklist if not already present
                        banklist.add(matchedSubstring);
                    }
                }
            }
        }

        // Print the full banklist after all iterations
        for (String item : banklist) {
            Log.e("Banklist Item", item);
        }
    }
}

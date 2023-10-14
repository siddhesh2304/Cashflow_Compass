package com.example.cashflowcompass;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SmsHandler {

    public static List<String> viewSMSLogs(Context context) {
        List<String> smsList = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String stringMessage = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                String sender = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                if (stringMessage != null) {
                    if (sender != null) {
                        String s = "Sender-" + sender; // Concatenate sender and message
                        s = s + stringMessage+"  "; // Concatenate the message
                        smsList.add(s); // Add the combined string to the list
                    } else {
                        smsList.add(stringMessage); // Add the message without the sender
                    }
                }
                cursor.moveToNext();
            }
            cursor.close(); // Close the cursor when done
        }

        return smsList;
    }



    }




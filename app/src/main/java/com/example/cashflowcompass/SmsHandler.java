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
                if (stringMessage != null) {
                    smsList.add(stringMessage); // Add the message to the list
                }
                cursor.moveToNext();
            }
            cursor.close(); // Close the cursor when done
        }

        return smsList;
    }


}

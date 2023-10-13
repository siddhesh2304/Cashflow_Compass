package com.example.cashflowcompass;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.content.ContextCompat;

public class SmsHandler {

    public static void viewSMSLogs(Context context) {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String stringMessage = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                if (stringMessage != null) {
                    Log.e("SMS", stringMessage);
                }
                cursor.moveToNext();
            }
            cursor.close(); // Close the cursor when done
        }
    }
}


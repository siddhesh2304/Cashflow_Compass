package com.example.cashflowcompass;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
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
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));
                if (stringMessage != null && stringMessage.contains("received")||stringMessage.contains("Received")||stringMessage.contains("Sent")||stringMessage.contains("sent")||stringMessage.contains("Debited")||stringMessage.contains("debited")||stringMessage.contains("Credited")||stringMessage.contains("credited")) {
                    if (sender != null) {
                        String s = "Sender-" + sender; // Concatenate sender and message to same
                        s = s + stringMessage+"  "; // Concatenate the message
                        s += "Date & Time: " + new Date(timestamp) + "\n";
                        smsList.add(s); // Add the combined string to the list
                        Log.d("SMSHandler", "Added to SMS List: " + s);
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

    public static List<String> dat(List<String>sms){
        List<String>date=new ArrayList<>();

        for (int j=0;j<sms.size();j++){
            String smsm = sms.get(j);
            String dae =   String.valueOf(smsm.charAt(smsm.length()-27))+String.valueOf(smsm.charAt(smsm.length()-26))+":"+String.valueOf(getMonthNumber(String.valueOf(smsm.charAt(smsm.length()-31))+String.valueOf(smsm.charAt(smsm.length()-30))+String.valueOf(smsm.charAt(smsm.length()-29))))+":"+(String.valueOf(smsm.charAt(smsm.length()-5))+String.valueOf(smsm.charAt(smsm.length()-4))+String.valueOf(smsm.charAt(smsm.length()-3))+String.valueOf(smsm.charAt(smsm.length()-2))+String.valueOf(smsm.charAt(smsm.length()-1))) ;
            date.add(dae);
        }

        return date;
    }

    private static int getMonthNumber(String monthAbbreviation) {
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




    }




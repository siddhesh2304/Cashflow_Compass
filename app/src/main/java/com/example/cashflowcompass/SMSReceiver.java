package com.example.cashflowcompass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.content.SharedPreferences;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SMSReceiver", "SMS Received!");

        // Retrieve the stored account number from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String selectedAccountNumber = preferences.getString("selectedAccountNumber", "");

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (SmsMessage message : smsMessages) {
                String senderNo = message.getDisplayOriginatingAddress();
                String messageBody = message.getDisplayMessageBody();
                Log.d("SMSReceiver", "Received SMS from: " + senderNo + " Message: " + messageBody);

                // Check if the received message contains the selected account number
                if (messageBody.contains(selectedAccountNumber)) {
                    // This message contains the selected account number
                    // You can process or handle it here.
                }
            }
        }
    }
}

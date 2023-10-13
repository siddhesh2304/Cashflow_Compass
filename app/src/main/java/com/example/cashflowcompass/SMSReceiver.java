package com.example.cashflowcompass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/*public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            // Handle incoming SMS here
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                String messageBody = smsMessage.getMessageBody();
                String sender = smsMessage.getOriginatingAddress();

                // You can process the SMS message as needed
                // Log newly received SMS messages with a custom prefix
                Log.e("NewSMS", "Received SMS from " + sender + ": " + messageBody);
            }
        }
    }
}
*/

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SMSReceiver", "SMS Received!");
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (SmsMessage message : smsMessages) {
                String senderNo = message.getDisplayOriginatingAddress();
                String messageBody = message.getDisplayMessageBody();
                Log.d("SMSReceiver", "Received SMS from: " + senderNo + " Message: " + messageBody);

                // You can also perform additional actions with the received SMS here.
            }
        }
    }
}

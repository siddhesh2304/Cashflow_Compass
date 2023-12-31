package com.example.cashflowcompass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.net.Uri;
import android.provider.Settings;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE = 2; // New permission request code
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.RECEIVE_SMS
    };

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isFirstRun = settings.getBoolean(FIRST_RUN, true);

        if (isFirstRun) {
            requestLocationAndSmsPermissions(); // Request location and SMS permissions
            settings.edit().putBoolean(FIRST_RUN, false).apply();
        } else {
            // Check if SYSTEM_ALERT_WINDOW permission is granted
            if (Settings.canDrawOverlays(this)) {
                checkAndShowHelloWorld();
            } else {
                requestSystemAlertWindowPermission(); // Request SYSTEM_ALERT_WINDOW permission
            }
        }
    }

    private void requestLocationAndSmsPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS
        }, PERMISSIONS_REQUEST_CODE);
    }

    private void requestSystemAlertWindowPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                requestSystemAlertWindowPermission(); // Request SYSTEM_ALERT_WINDOW permission after location and SMS permissions are granted
            } else {
                showToast("Some permissions are necessary for CashFlowCompass to function properly.");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                checkAndShowHelloWorld();
            } else {
                showToast("Display over other apps permission is necessary for CashFlowCompass to function properly.");
            }
        }
    }

    private void checkAndShowHelloWorld() {
        // Placeholder method to handle what should happen when permissions are granted
        TextView helloWorldTextView = findViewById(R.id.messageTextView);
        helloWorldTextView.setText("Hello, CashFlowCompass!");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void MessageLogview(View view) {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null, null);
        cursor.moveToFirst();
        while(cursor!=null) {

            String stringMessage = cursor.getString(12);
            if (stringMessage.contains("sent")) {
                displayMessageOnScreen(stringMessage);
                break;
            }
            cursor.moveToNext();
        }

    }

    private void displayMessageOnScreen(String message)
    {
        TextView messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setText(message);
    }

}

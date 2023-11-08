package com.example.cashflowcompass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.content.BroadcastReceiver;
import android.net.Uri;
import android.provider.Settings;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE = 2; // New permission request code
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
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
            // Request location and SMS permissions
            requestLocationAndSmsPermissions();
            if (Settings.canDrawOverlays(this)) {
                settings.edit().putBoolean(FIRST_RUN, false).apply();
                navigateToAnotherActivity();
                onBackPressed();
            }


        } else {
            // Check if SYSTEM_ALERT_WINDOW permission is granted
            if (Settings.canDrawOverlays(this)) {
                navigateToAnotherActivity();
            } else {
                //requestSystemAlertWindowPermission();
            }
        }
    }

    public void navigateToAnotherActivity() {
        // Navigate to another activity (replace AnotherActivity.class with the actual class name of the activity)
        Intent intent = new Intent(this, Dashboard_activity.class);
        startActivity(intent);
        finish();
    }
    public void navigateToMainActivity() {
    // Navigate to another activity (replace AnotherActivity.class with the actual class name of the activity)
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish(); // Finish the current activity to prevent returning to it
}
    private void requestLocationAndSmsPermissions() {
        Log.d("PermissionDebug", "Requesting location and SMS permissions");
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS
        }, PERMISSIONS_REQUEST_CODE);
    }

    private void requestSystemAlertWindowPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
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
                requestSystemAlertWindowPermission();
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
            } else {
                showToast("Display over other apps permission is necessary for CashFlowCompass to function properly.");
            }
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void displayMessageOnScreen(String message) {
        Log.e("SMS", message);
    }

    // this is the change
    public void ViewSMSLogs(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
          List<String> ans = SmsHandler.viewSMSLogs(this);
          Bankselect.performTaskOnSMSList(ans);
          openSelectBankActivity();

        } else {
            showToast("Read SMS permission is required to view SMS logs.");
        }
    }

    public void openSelectBankActivity() {
        // Open the SelectBankActivity
        Intent intent = new Intent(this, SelectBankActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Do nothing (disable back button navigation)
        // If you don't call super.onBackPressed(), the back button won't have any effect.
    }

}

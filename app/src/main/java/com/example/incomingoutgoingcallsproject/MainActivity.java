package com.example.incomingoutgoingcallsproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.example.incomingoutgoingcallsproject.utils.Constants;
import com.example.incomingoutgoingcallsproject.utils.comparators.TimeComparator;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPhoneCallReceiver.setCallBack_update(callBack_update);
        getPermissions(MainActivity.this);
    }

    private final CallBack_Update callBack_update = new CallBack_Update() {
        @Override
        public void updateList() {
            MyPhoneCallReceiver.phoneToDuration = (HashMap<String, Duration>) TimeComparator.sort(MyPhoneCallReceiver.phoneToDuration, Constants.DESC);
            for (Map.Entry<String, Duration> entry : MyPhoneCallReceiver.phoneToDuration.entrySet()) {
                Log.d("pttt2", "" + entry.getKey() + " : " + formatDuration(entry.getValue()));
            }
            // Update the list whenever theres a change coming from the broadcast receiver.
        }
    };


    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        @SuppressLint("DefaultLocale") String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    private void getPermissions(Context context) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, CallsService.class);
                startForegroundService(intent);
            } else {
                boolean firstRequest = true;
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected. In this UI,
                    // include a "cancel" or "no thanks" button that allows the user to
                    // continue using your app without granting the permission.
                    requestWithExplainDialog(Manifest.permission.READ_PHONE_STATE);
                    firstRequest = false;
                }
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG)) {
                    requestWithExplainDialog(Manifest.permission.READ_CALL_LOG);
                    firstRequest = false;
                }
                if (firstRequest) {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    firstRequestPermissionLauncher.launch(Constants.PERMISSIONS);
                }
            }
    }

    private void getPermission(String permission) {
        if (Constants.FORCE  &&  shouldShowRequestPermissionRationale(permission)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            requestWithExplainDialog(permission);
        } else if (Constants.CAN_GRANT_MANUALLY  &&  !shouldShowRequestPermissionRationale(permission)) {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            manuallyDialog(permission);
        } else {
        }
    }

    private void requestWithExplainDialog(String permission) {
        String message = "We need permission for...";
        AlertDialog alertDialog =
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissionLauncher.launch(Constants.PERMISSIONS);
                                    }
                                }).show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void manuallyDialog(String permission) {
        if (shouldShowRequestPermissionRationale(permission)) {
            return;
        }

        String message = "Setting screen if user have permanently disable the permission by clicking Don't ask again checkbox.";
        AlertDialog alertDialog =
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        manuallyActivityResultLauncher.launch(intent);
                                        dialog.cancel();
                                    }
                                }).show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), returnedPermissions -> {
                for (String permission : Constants.PERMISSIONS)
                {
                    if (!returnedPermissions.get(permission)) {
                        getPermission(permission);
                    }
                }
            });

    private ActivityResultLauncher<String[]> firstRequestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), returnedPermissions -> {
                for (String permission : Constants.PERMISSIONS)
                {
                    if (!returnedPermissions.get(permission)) {
                        requestWithExplainDialog(permission);
                    }
                }
            });

    ActivityResultLauncher<Intent> manuallyActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        for (String permission : Constants.PERMISSIONS) {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED
                                    && Constants.FORCE) {
                                getPermissions(MainActivity.this);
                            }
                        }
                    }
                }
            });

}
package com.example.incomingoutgoingcallsproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();
        Intent intent = new Intent(this, CallsService.class);
        MyPhonecallReceiver.setCallBack_update(callBack_update);
        startForegroundService(intent);

    }

    private final CallBack_Update callBack_update = new CallBack_Update() {
        @Override
        public void updateList() {
            MyPhonecallReceiver.phoneToDuration = (HashMap<String, Duration>) Utils.sortByComparator(MyPhonecallReceiver.phoneToDuration, Utils.DESC);
            for (Map.Entry<String, Duration> entry : MyPhonecallReceiver.phoneToDuration.entrySet()) {
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

}
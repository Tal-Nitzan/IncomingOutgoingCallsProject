package com.example.incomingoutgoingcallsproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private Context context;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getBaseContext();
        Intent intent = new Intent(this, CallsService.class);
        MyPhonecallReceiver.setCallBack_update(callBack_update);
        startService(intent);
    }

    private CallBack_Update callBack_update = new CallBack_Update() {
        @Override
        public void updateList() {
            Log.d("pttt", "hereeeeeeeeeeeeeeeeeeeeee updatelist()");
            Toast.makeText(context ,"blabla",Toast.LENGTH_LONG).show();
            // Update the list whenever theres a change coming from the broadcast receiver.
        }
    };

}
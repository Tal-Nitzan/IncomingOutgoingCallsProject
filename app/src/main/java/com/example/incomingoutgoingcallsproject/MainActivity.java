package com.example.incomingoutgoingcallsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private CallBack_Update callBack_update = new CallBack_Update() {
        @Override
        public void updateList() {
            // Update the list whenever theres a change coming from the broadcast receiver.
        }
    };

}
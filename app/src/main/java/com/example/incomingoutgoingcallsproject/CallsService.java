package com.example.incomingoutgoingcallsproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class CallsService extends Service {
    private Context context;

    @Override
    public void onCreate() {
        context = getBaseContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        Intent intent = new Intent(this, MyPhonecallReceiver.class);
        sendBroadcast(intent);
        Log.d("pttt", "onCreate Thread: " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("pttt", "onStartCommand Thread: " + Thread.currentThread().getName());
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler h = new Handler(context.getMainLooper());
                // Although you need to pass an appropriate context
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"blabla2",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

}
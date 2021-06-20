package com.example.incomingoutgoingcallsproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CallsService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

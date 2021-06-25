package com.example.incomingoutgoingcallsproject;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyPhonecallReceiver extends PhonecallReceiver {

    private static CallBack_Update callBack_update;
    public static HashMap<String, Duration> phoneToDuration = new HashMap<String, Duration>();

    public static void setCallBack_update(CallBack_Update _callBack_update) {
        MyPhonecallReceiver.callBack_update = _callBack_update;
    }

    @Override
    protected void onIncomingCallStarted(String number, Date start) {
        callBack_update.updateList();

    }

    @Override
    protected void onOutgoingCallStarted(String number, Date start) {
        callBack_update.updateList();

    }

    @Override
    protected void onIncomingCallEnded(String number, Date start, Date end) {
        callBack_update.updateList();
        addDurationToPhoneNumber(number, start, end);
    }

    @Override
    protected void onOutgoingCallEnded(String number, Date start, Date end) {
        callBack_update.updateList();
        addDurationToPhoneNumber(number, start, end);
    }

    private void addDurationToPhoneNumber(String number, Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        if (phoneToDuration.containsKey(number)) {
            Duration currentDuration = phoneToDuration.get(number).plusMillis(diff);
            phoneToDuration.put(number, currentDuration);
            Log.d("pttt", "Phonecall!");
        } else {
            phoneToDuration.put(number, Duration.ofMillis(diff));
            Log.d("pttt", "Phonecall!");
        }
        callBack_update.updateList();
        if (callBack_update != null) {
            Log.d("pttt", "set callback " + callBack_update);
            callBack_update.updateList();
            Log.d("pttt", "Phonecall!");
        }
    }

    @Override
    protected void onMissedCall(String number, Date start) {

    }
}

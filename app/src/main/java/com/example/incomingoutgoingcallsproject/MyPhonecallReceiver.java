package com.example.incomingoutgoingcallsproject;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyPhonecallReceiver extends PhonecallReceiver {

    private CallBack_Update callBack_update;
    public static HashMap<String, Duration> phoneToDuration = new HashMap<String, Duration>();

    public void setCallBack_update(CallBack_Update _callBack_update) {
        this.callBack_update = _callBack_update;
    }

    @Override
    protected void onIncomingCallStarted(String number, Date start) {

    }

    @Override
    protected void onOutgoingCallStarted(String number, Date start) {

    }

    @Override
    protected void onIncomingCallEnded(String number, Date start, Date end) {
        addDurationToPhoneNumber(number, start, end);
    }

    @Override
    protected void onOutgoingCallEnded(String number, Date start, Date end) {
        addDurationToPhoneNumber(number, start, end);
    }

    private void addDurationToPhoneNumber(String number, Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        if (phoneToDuration.containsKey(number)) {
            Duration currentDuration = phoneToDuration.get(number).plusMillis(diff);
            phoneToDuration.put(number, currentDuration);
        } else {
            phoneToDuration.put(number, Duration.ofMillis(diff));
        }
        if (callBack_update != null) {
            callBack_update.updateList();
        }
    }

    @Override
    protected void onMissedCall(String number, Date start) {

    }
}

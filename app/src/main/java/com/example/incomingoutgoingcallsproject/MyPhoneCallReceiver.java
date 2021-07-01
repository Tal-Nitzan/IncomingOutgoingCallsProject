package com.example.incomingoutgoingcallsproject;

import android.util.Log;


import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyPhoneCallReceiver extends PhoneCallReceiver {

    private static CallBack_Update callBack_update;
    public static Map<String, Duration> phoneToDuration = new HashMap<String, Duration>();

    public static void setCallBack_update(CallBack_Update _callBack_update) {
        MyPhoneCallReceiver.callBack_update = _callBack_update;
    }

    @Override
    protected void onIncomingCallStarted(String number, Date start) {
        Log.d("pttt", "in incoming call started");
    }

    @Override
    protected void onOutgoingCallStarted(String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(String number, Date start, Date end) {
        addDurationToPhoneNumber(number, start, end);
        Log.d("pttt", "in incoming call ended");
//        callBack_update.updateList();
    }

    @Override
    protected void onOutgoingCallEnded(String number, Date start, Date end) {
        addDurationToPhoneNumber(number, start, end);
//        callBack_update.updateList();
    }

    private void addDurationToPhoneNumber(String number, Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        if (phoneToDuration.containsKey(number)) {
            Duration currentDuration = phoneToDuration.get(number).plusMillis(diff);
//            Log.d("pttt2", "here : " + number);
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

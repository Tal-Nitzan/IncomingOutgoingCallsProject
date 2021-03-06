package com.example.incomingoutgoingcallsproject;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public abstract class PhoneCallReceiver extends BroadcastReceiver {

    //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
    static PhonecallStartEndDetector listener;
    String outgoingSavedNumber;
    protected Context savedContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        savedContext = context;
        Intent background = new Intent(context, CallsService.class);
        context.startService(background);
        if(listener == null){
            listener = new PhonecallStartEndDetector();
        }

        //The other intent tells us the phone state changed.  Here we set a listener to deal with it
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    //Derived classes should override these to respond to specific events of interest
    protected abstract void onIncomingCallStarted(String number, Date start);
    protected abstract void onOutgoingCallStarted(String number, Date start);
    protected abstract void onIncomingCallEnded(String number, Date start, Date end);
    protected abstract void onOutgoingCallEnded(String number, Date start, Date end);
    protected abstract void onMissedCall(String number, Date start);

    //Deals with actual events
    public class PhonecallStartEndDetector extends PhoneStateListener {
        int lastState = TelephonyManager.CALL_STATE_IDLE;
        Date callStartTime;
        boolean isIncoming;
        String savedNumber;  //because the passed incoming is only valid in ringing

        public PhonecallStartEndDetector() {}

        //The outgoing number is only sent via a separate intent, so we need to store it out of band
        public void setOutgoingNumber(String number){
            savedNumber = number;
        }

        //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
        //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if(lastState == state){
                //No change, debounce extras
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;
                    savedNumber = incomingNumber;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //Transition of ringing->offhook are pickups of incoming calls.  Nothing donw on them
                    if(lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncoming = false;
                        callStartTime = new Date();
                        onOutgoingCallStarted(incomingNumber, callStartTime);
                    } else {
                        callStartTime = new Date();
                        onIncomingCallStarted(incomingNumber, callStartTime);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                    if(lastState == TelephonyManager.CALL_STATE_RINGING){
                        //Ring but no pickup-  a miss
                        onMissedCall(incomingNumber, callStartTime);
                    }
                    else if(isIncoming){
                        onIncomingCallEnded(incomingNumber, callStartTime, new Date());
                    }
                    else{
                        onOutgoingCallEnded(incomingNumber, callStartTime, new Date());
                    }
                    break;
            }
            lastState = state;
        }

    }



}
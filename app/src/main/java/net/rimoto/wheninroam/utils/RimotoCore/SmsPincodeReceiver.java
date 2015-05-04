package net.rimoto.wheninroam.utils.RimotoCore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SmsPincodeReceiver extends BroadcastReceiver {
    private static final String RIMOTO_PIN_TEXT = "PIN code:";
    private static final String SMS_BUNDLE = "pdus";

    public interface Callback {
        void done(String pin);
    }

    private Callback mCallback;
    public SmsPincodeReceiver(Callback cb) {
        mCallback = cb;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return;

        Bundle intentExtras = intent.getExtras();
        if(intentExtras == null) return;

        Object[] messages = (Object[]) intentExtras.get(SMS_BUNDLE);
        for(Object message : messages) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) message);

            String smsBody = smsMessage.getMessageBody();
            if(smsBody.contains(RIMOTO_PIN_TEXT)) {
                String pin = smsBody.split(RIMOTO_PIN_TEXT,2)[1].trim();
                mCallback.done(pin);
            }
        }
    }
}
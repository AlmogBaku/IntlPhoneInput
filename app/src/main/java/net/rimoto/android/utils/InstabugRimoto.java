package net.rimoto.android.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.instabug.library.Instabug;

import net.rimoto.core.API;
import net.rimoto.core.Session;
import net.rimoto.core.models.Subscriber;

import java.util.concurrent.Executors;

public class InstabugRimoto {

    public static void attachUser(Context context) {
        Executors.newSingleThreadExecutor().submit(() -> {
            Subscriber subscriber = Session.getCurrentSubscriber();

            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String home_operator = API.rimotoOperatorFormat(tel.getSimOperator());
            String roaming_operator = API.rimotoOperatorFormat(tel.getNetworkOperator());

            String userData = ""
                                +"subscriber: " + subscriber.toString() + "\r\n"
                                +"home_operator: " + home_operator + "\r\n"
                                +"visited_operator: " + roaming_operator + "\r\n"
                    ;
            Instabug.getInstance().setUserData(userData);
        });
    }
}

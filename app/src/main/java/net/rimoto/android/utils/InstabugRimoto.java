package net.rimoto.android.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.instabug.library.Instabug;

import net.rimoto.core.API;
import net.rimoto.core.Session;
import net.rimoto.core.models.Subscriber;
import net.rimoto.core.utils.VpnUtils;
import net.rimoto.vpnlib.VpnManager;

import java.util.concurrent.Executors;

import de.blinkt.openvpn.core.VpnStatus;

public class InstabugRimoto {

    public static void attachUser(Context context) {
        Executors.newSingleThreadExecutor().submit(() -> {
            Subscriber subscriber = Session.getCurrentSubscriber();

            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String home_operator = API.rimotoOperatorFormat(tel.getSimOperator());
            String roaming_operator = API.rimotoOperatorFormat(tel.getNetworkOperator());
            Boolean rimotoIsEnabled = !(VpnUtils.getCurrentProfileUUID(context) == null);

            String userData = ""
                                +"subscriber: " + subscriber.toString() + "\r\n"
                                +"home_operator: " + home_operator + "\r\n"
                                +"visited_operator: " + roaming_operator + "\r\n"
                                +"rimoto_enabled: " + rimotoIsEnabled + "\r\n"
                                +"vpn_connected: " + VpnManager.isConnected() + "\r\n"
                                +"vpn_state: " + VpnStatus.getLaststate() + "\r\n"
                    ;
            Log.d("testfairy-correlation-id", String.valueOf(subscriber.getId()));
            Instabug.getInstance().setUserData(userData);
        });
    }
}

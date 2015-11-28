package net.rimoto.android.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.instabug.library.Instabug;

import net.rimoto.core.API;
import net.rimoto.core.Session;
import net.rimoto.core.models.Subscriber;
import net.rimoto.core.utils.VpnUtils;
import net.rimoto.vpnlib.VpnManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.Connection;
import de.blinkt.openvpn.core.ProfileManager;
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

            VpnProfile profile = ProfileManager.get(context,VpnUtils.getCurrentProfileUUID(context));
            if(profile != null) {
                List<String> connections = new ArrayList<String>();
                for(Connection connection : profile.mConnections) {
                    connections.add(connection.mServerName+":"+connection.mServerPort);
                }
                userData += "vpn_profile.servers: [ " + TextUtils.join(", ", connections) + " ]\r\n";
                userData += "vpn_profile.wifi_policy: " + VpnProfile.WifiPolicy.values()[profile.mWifiPolicy.ordinal()] + " ]\r\n";
                userData += "vpn_profile.roaming_policy: " + VpnProfile.RoamingPolicy.values()[profile.mRoamingPolicy.ordinal()] + " ]\r\n";
            }

            Log.d("testfairy-correlation-id", String.valueOf(subscriber.getId()));
            Instabug.getInstance().setUserData(userData);
        });
    }
}

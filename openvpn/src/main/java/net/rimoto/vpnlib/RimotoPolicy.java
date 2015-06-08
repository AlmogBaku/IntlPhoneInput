package net.rimoto.vpnlib;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import de.blinkt.openvpn.VpnProfile;

/**
 * Rimoto policy voter
 */
public class RimotoPolicy {
    private static final String IS_DISCONNECTED_BY_POLICY = "net.rimoto.vpnlib.vpnmanager.isDisconnectedByPolicy";
    private static Boolean isDisconnectedByPolicy;

    public static void setDisconnectedByPolicy(Context context, Boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putBoolean(IS_DISCONNECTED_BY_POLICY, status);
        prefsEditor.apply();
        isDisconnectedByPolicy = status;
    }

    public static boolean getDisconnectedByPolicy(Context context) {
        if(isDisconnectedByPolicy != null)
            return isDisconnectedByPolicy;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isDisconnectedByPolicy = prefs.getBoolean(IS_DISCONNECTED_BY_POLICY, false);
        return isDisconnectedByPolicy;
    }

    /**
     * Chain check for rimoto policy
     * @param networkInfo NetworkInfo
     * @param profile VpnProfile
     * @return Boolean
     */
    public static Boolean shouldConnect(NetworkInfo networkInfo, VpnProfile profile) {
        Boolean shouldConnect;
        shouldConnect = wifiPolicyMatch(networkInfo, profile);
//        shouldConnect = shouldConnect && roamingPolicyMatch(networkInfo, profile);

        return shouldConnect;
    }

    /**
     * Check if should allow connection to the new network - WifiPolicy parameter
     * @param networkInfo new NetworkInfo
     * @return boolean
     */
    public static boolean wifiPolicyMatch(NetworkInfo networkInfo, VpnProfile profile) {
        VpnProfile.WifiPolicy profileWifiPolicy = profile.mWifiPolicy;

        if(profileWifiPolicy.equals(VpnProfile.WifiPolicy.BOTH))
            return true;

        if(networkInfo == null)
            return false;
        else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI && profileWifiPolicy.equals(VpnProfile.WifiPolicy.WIFI))
            return true;
        else if(networkInfo.getType() != ConnectivityManager.TYPE_WIFI && profileWifiPolicy.equals(VpnProfile.WifiPolicy.MOBILE))
            return true;

        return false;
    }

    /**
     * Check if should allow connection to the new network - RoamingPolicy parameter
     * @param networkInfo new NetworkInfo
     * @return boolean
     */
    public static boolean roamingPolicyMatch(NetworkInfo networkInfo, VpnProfile profile) {
        if(profile.mRoamingPolicy == VpnProfile.RoamingPolicy.BOTH) {
            return true;
        } else {
            if(networkInfo == null)
                return false;
            else if(profile.mRoamingPolicy.equals(VpnProfile.RoamingPolicy.ONLY) && networkInfo.isRoaming())
                return true;
            else if(profile.mRoamingPolicy.equals(VpnProfile.RoamingPolicy.FALSE) && !networkInfo.isRoaming())
                return true;
        }

        return false;
    }
}

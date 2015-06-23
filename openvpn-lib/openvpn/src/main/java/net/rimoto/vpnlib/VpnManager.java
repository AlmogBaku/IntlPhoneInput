package net.rimoto.vpnlib;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.Reader;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VpnStatus;

/**
 * VPN Manager
 */
public class VpnManager extends IntentService {
    public static final String EXTRA_PROFILE_UUID       = "net.rimoto.vpnlib.vpnmanager.extra.profileUuid";
    public static final String EXTRA_BY_POLICY          = "net.rimoto.vpnlib.extra.byPolicy";
    public static final String EXTRA_BY_BOOT            = "net.rimoto.vpnlib.extra.byBoot";

    public static final String ACTION_CONNECT           = Intent.ACTION_MAIN;
    public static final String ACTION_DISCONNECT        = "net.rimoto.vpnlib.vpnmanager.action.stop";

    public static final String TAG = "VpnManager | ";

    private static final String ACTIVE_PROFILE = "net.rimoto.vpnlib.vpnmanager.activeProfile";
    private static VpnProfile mActiveProfile;

    private String mProfileUuid;
    private boolean mByPolicy;
    private boolean mByBoot;

    private OpenVPNService mVpnService;
    private ServiceConnection mVpnConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            OpenVPNService.LocalBinder binder = (OpenVPNService.LocalBinder) service;
            mVpnService = binder.getService();

            disconnect();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mVpnService = null;
        }
    };

    public VpnManager() {
        super("VpnManager");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        mByPolicy = intent.getBooleanExtra(EXTRA_BY_POLICY, false);

        if(ACTION_CONNECT.equals(action)) {
            mByBoot = intent.getBooleanExtra(EXTRA_BY_BOOT, false);
            mProfileUuid = intent.getStringExtra(EXTRA_PROFILE_UUID);
            this.startVPN();
        } else if(ACTION_DISCONNECT.equals(action)) {
            this.stopVPN();
        }
    }

    /**
     * Starting the VPN
     */
    private void startVPN() {
        if(isConnected()) {
            VpnStatus.logDebug(TAG + "Already started");
            return;
        }
        VpnStatus.logDebug(TAG + "Starting..");

        Intent intent = new Intent(this.getApplicationContext(), ConnectVPN.class);
        intent.setAction(Intent.ACTION_MAIN);

        intent.putExtra(EXTRA_PROFILE_UUID, this.mProfileUuid);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        this.startActivity(intent);

        if(mByPolicy)
            VpnStatus.logDebug(TAG + "VPN started by policy..");
        if(mByBoot)
            VpnStatus.logDebug(TAG + "VPN started by boot..");
    }

    /**
     * Stopping the VPN
     */
    private void stopVPN() {
        if(!isConnected()) {
            if(!mByPolicy) {
                ProfileManager.setConntectedVpnProfileDisconnected(this);
                VpnManager.setActiveProfileDisconnected(this);
                VpnStatus.logDebug(TAG + "Rimoto Policy stopped");
            }
            VpnStatus.logDebug(TAG + "Already stopped");
            return;
        }

        Intent intent = new Intent(this, OpenVPNService.class);
        intent.setAction(OpenVPNService.START_SERVICE);
        bindService(intent, mVpnConnection, Context.BIND_AUTO_CREATE);
        VpnStatus.logDebug(TAG + "Waiting for OpenVPN service..");
    }

    /**
     * Actual disconnecting
     */
    private void disconnect() {
        if(!VpnManager.isConnected()) {
            VpnStatus.logDebug(TAG + "Already stopped");
        } else if (mVpnService != null && mVpnService.getManagement() != null) {
            VpnStatus.logDebug(TAG + "Stopping..");
            mVpnService.getManagement().stopVPN();
        } else {
            VpnStatus.logInfo(TAG + "Waiting for VPN Management service binding..");
            return;
        }

        if(mByPolicy) {
            RimotoPolicy.setDisconnectedByPolicy(this, true);
            VpnStatus.logDebug(TAG + "VPN stopped by policy..");
        } else {
            VpnStatus.logDebug(TAG + "Rimoto Policy stopped");
            ProfileManager.setConntectedVpnProfileDisconnected(this);
            VpnManager.setActiveProfileDisconnected(this);
        }

        unbindService(mVpnConnection);
        mVpnConnection = null;
    }

    /*
     * Profile Management
     */

    /**
     * Import VPN profile from an `.ovpn` file
     * @param context Context
     * @param reader Reader of the file
     * @param name String of the profile name [optional]
     * @return VpnProfile
     */
    public static VpnProfile importConfig(Context context, Reader reader, String name) {
        ConfigParser configParser = new ConfigParser();
        try {
            //convert
            configParser.parseConfig(reader);
            VpnProfile profile = configParser.convertProfile();

            if(name != null) {
                profile.mName = name;
            }

            //save the final profile on the profile manager
            VpnManager.saveProfile(context, profile);

            return profile;
        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
        }

        return null;
    }
    public static VpnProfile importConfig(Context context, Reader reader) {
        return importConfig(context, reader, null);
    }

    /**
     * Save VpnProfile to the profile manager
     * @param profile VpnProfile object
     */
    private static void saveProfile(Context context, VpnProfile profile) {
        ProfileManager profileManager = ProfileManager.getInstance(context);

        profileManager.addProfile(profile);
        profileManager.saveProfile(context, profile);
        profileManager.saveProfileList(context);
    }

    /*
     * Active connection management
     */

    /**
     * Set an active VPN profile
     * @param context Context
     * @param vpnProfile VpnProfile
     */
    public static void setActiveProfile(Context context, VpnProfile vpnProfile) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        String UUID = vpnProfile.getUUIDString();
        prefsEditor.putString(ACTIVE_PROFILE, UUID);
        prefsEditor.apply();

        mActiveProfile = vpnProfile;
    }
    /**
     * Set the "active VPN profile" as disconnected
     * @param context Context
     */
    public static void setActiveProfileDisconnected(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString(ACTIVE_PROFILE, null);
        prefsEditor.apply();

        mActiveProfile = null;
    }

    /**
     * Get the active VPN profile
     * @param context Context
     * @return VpnProfile
     */
    public static VpnProfile getActiveProfile(Context context) {
        if(mActiveProfile != null)
            return mActiveProfile;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        mActiveProfile = ProfileManager.get(context, prefs.getString(ACTIVE_PROFILE, null));
        return mActiveProfile;
    }

    /**
     * Is VpnManager have active profile
     *
     * **Active profile** means that the VpnManager will connect automatically when the profile's
     * policy allows it.
     * @param context Context
     * @return Boolean
     */
    public static Boolean isActive(Context context) {
        return getActiveProfile(context) != null;
    }

    /**
     * Helper: get current NetworkInfo
     * @param context Context
     * @return NetworkInfo
     */
    public static NetworkInfo getCurrentNetworkInfo(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conn.getActiveNetworkInfo();
    }

    /**
     * Is there an active connection to the VPN
     * @return Boolean
     */
    public static Boolean isConnected() {
        String state = VpnStatus.getLaststate();
        return (state != "NOPROCESS");
//        return (state != "NOPROCESS") && (state != "USER_VPN_PERMISSION");
    }
}

package net.rimoto.vpnlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.blinkt.openvpn.VpnProfile;

/**
 * State receiver listener
 */
public class StateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();

		if(Intent.ACTION_BOOT_COMPLETED.equals(action) || Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) {
            this.bootChange(context);
        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            this.networkChange(context);
        }
    }

    /**
     * Network change
     * @param context context
     */
    private void networkChange(Context context) {
        VpnProfile profile = VpnManager.getActiveProfile(context);
        if(profile == null)
            return;

        NetworkInfo networkInfo = VpnManager.getCurrentNetworkInfo(context);
        Boolean shouldConnect = RimotoPolicy.shouldConnect(networkInfo, profile);
        if(!shouldConnect) {
            this.stopVpnByPolicy(context);
        } else if(RimotoPolicy.getDisconnectedByPolicy(context)) {
            this.startVPN(context, profile, true);
        }
    }

    /**
     * Boot/update completed
     * @param context
     *
     * 	Debug: am broadcast -a android.intent.action.BOOT_COMPLETED
     */
    private void bootChange(Context context) {
        VpnProfile profile = VpnManager.getActiveProfile(context);
        if(profile != null) {
            startVPN(context, profile, false);
        }
    }

    /**
     * Starting the VPN Service
     */
    private void startVPN(Context context, VpnProfile profile, Boolean byPolicy) {
        Intent intent = new Intent(context.getApplicationContext(), VpnManager.class);
        intent.setAction(VpnManager.ACTION_CONNECT);

        intent.putExtra(VpnManager.EXTRA_PROFILE_UUID, profile.getUUIDString());
        intent.putExtra(VpnManager.EXTRA_BY_POLICY, byPolicy);
        intent.putExtra(VpnManager.EXTRA_BY_BOOT, !byPolicy);

        context.getApplicationContext().startService(intent);
    }

    /**
     * Starting the VPN Service
     */
    private void stopVpnByPolicy(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), VpnManager.class);
        intent.setAction(VpnManager.ACTION_DISCONNECT);

        intent.putExtra(VpnManager.EXTRA_BY_POLICY, true);

        context.startService(intent);
    }
}

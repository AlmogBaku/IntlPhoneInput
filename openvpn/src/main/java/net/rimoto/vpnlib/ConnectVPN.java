package net.rimoto.vpnlib;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.VpnService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import java.io.IOException;

import de.blinkt.openvpn.R;
import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VPNLaunchHelper;
import de.blinkt.openvpn.core.VpnStatus;
import de.blinkt.openvpn.core.VpnStatus.ConnectionStatus;

/**
 * Connect VPN activity
 * (must be an activity in order to connect to vpn)
 */
public class ConnectVPN extends Activity {
    public static final String EXTRA_PROFILE_UUID   = VpnManager.EXTRA_PROFILE_UUID;
	private static final int START_VPN_PROFILE      = 156;

	private VpnProfile mProfile;

	private boolean mCmFixed =false;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	}	

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
        String UUID = intent.getStringExtra(EXTRA_PROFILE_UUID);

        VpnProfile profile = ProfileManager.get(this, UUID);
        if(profile == null) {
            VpnStatus.logError(VpnManager.TAG + R.string.shortcut_profile_notfound);
            finish();
            return;
        }

        //Save the active profile anyway
        VpnManager.setActiveProfile(this, profile);

        //Policy check
        NetworkInfo networkInfo = VpnManager.getCurrentNetworkInfo(this);
        if(!RimotoPolicy.shouldConnect(networkInfo, profile)) {
            VpnStatus.logDebug(VpnManager.TAG + "Stopping started VPN by policy.");
            RimotoPolicy.setDisconnectedByPolicy(this, true);
            finish();
            return;
        }

        //Reset disconnected by policy
        RimotoPolicy.setDisconnectedByPolicy(this, false);

        mProfile = profile;
        startVPN();
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == START_VPN_PROFILE) {
			if(resultCode == Activity.RESULT_OK) {
				int needPw = mProfile.needUserPWInput(false);
				if(needPw !=0) {
					VpnStatus.updateStateString("USER_VPN_PASSWORD", "", R.string.state_user_vpn_password,
                            ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);

                    VpnStatus.logDebug(VpnManager.TAG + getResources().getString(R.string.state_user_vpn_password));
                    finish();
				} else {
					new startOpenVpnThread().start();
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// User does not want us to start, so we just vanish
				VpnStatus.updateStateString("USER_VPN_PERMISSION_CANCELLED", "", R.string.state_user_vpn_permission_cancelled,
                        ConnectionStatus.LEVEL_NOTCONNECTED);

                VpnStatus.logDebug(VpnManager.TAG + getResources().getString(R.string.state_user_vpn_permission_cancelled));
                finish();
			}
		}
	}

    /**
     * Connect the VPN
     */
	void startVPN() {
		int isValidProfile = mProfile.checkProfile(this);
		if(isValidProfile != R.string.no_error_found) {
            VpnStatus.logDebug(VpnManager.TAG + getResources().getString(isValidProfile));
            finish();
            return;
		}

		Intent intent = VpnService.prepare(this);

		// Check if we want to fix /dev/tun
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);        
		boolean useCM9Fix = prefs.getBoolean("useCM9Fix", false);
		boolean loadTunModule = prefs.getBoolean("loadTunModule", false);

		if(loadTunModule)
			executeSuCmd("insmod /system/lib/modules/tun.ko");
		if(useCM9Fix && !mCmFixed)
			executeSuCmd("chown system /dev/tun");


		if (intent != null) {
			VpnStatus.updateStateString("USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                    ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);

			// Start the query
			try {
				startActivityForResult(intent, START_VPN_PROFILE);
			} catch (ActivityNotFoundException e) {
				// Shame on you Sony! At least one user reported that 
				// an official Sony Xperia Arc S image triggers this exception
				VpnStatus.logError(VpnManager.TAG + R.string.no_vpn_support_image);
			}
		} else {
			onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);
		}

	}

    /**
     * Execute su command
     * @param command String
     */
	private void executeSuCmd(String command) {
		ProcessBuilder pb = new ProcessBuilder("su","-c",command);
		try {
			Process p = pb.start();
			int ret = p.waitFor();
			if(ret ==0)
				mCmFixed =true;
		} catch (InterruptedException | IOException e) {
            VpnStatus.logException("SU command", e);
		}
    }

    /**
     * Start OpenVPN Thread
     */
	private class startOpenVpnThread extends Thread {
		@Override
		public void run() {
			VPNLaunchHelper.startOpenVpn(mProfile, getBaseContext());
			finish();
		}
	}
}

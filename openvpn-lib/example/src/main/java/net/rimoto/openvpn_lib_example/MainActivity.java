package net.rimoto.openvpn_lib_example;

import android.content.Intent;
import android.content.res.Resources;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import net.rimoto.vpnlib.VpnLog;
import net.rimoto.vpnlib.VpnManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.VpnStatus;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = "tVPN";
    private boolean vpnStatus = false;
    private Switch vpnSwitcher;
    private TextView vpnStatusTextView, roamingStatusTextView;
    private VpnProfile mProfile;
    private static final int STOP_REQUEST = 156;
    private Boolean switchManuallyChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.vpnStatusTextView = (TextView) super.findViewById(R.id.vpnStatusTextView);
        this.roamingStatusTextView = (TextView) super.findViewById(R.id.roamingStatusTextView);
        this.vpnSwitcher = (Switch) super.findViewById(R.id.vpnSwitcher);
        this.vpnSwitcher.setChecked(VpnManager.isActive(this));
        this.vpnSwitcher.setOnCheckedChangeListener((compoundButton, status) -> this.setVpnStatus(status));

        Button roamingBtn = (Button) super.findViewById(R.id.roamingButton);
        roamingBtn.setOnClickListener((v) -> this.updateRoamingView());

        //Import the vpn profile
        this.import_ovpn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logcat:
                Resources res = this.getResources();
                Boolean start = (item.getTitle() == res.getString(R.string.action_logcat_start));

                if(start) {
                    VpnLog.getInstance().registerLogcat();
                    item.setTitle(R.string.action_logcat_stop);
                } else {
                    VpnLog.getInstance().unregisterLogcat();
                    item.setTitle(R.string.action_logcat_start);
                }
                return true;
            case R.id.action_logcat_all:
                try {
                    Log.d(TAG, VpnLog.getRecentLogs());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateRoamingView() {
        NetworkInfo netInfo = VpnManager.getCurrentNetworkInfo(this);
        Resources res = this.getResources();
        if(netInfo != null) {
            String statusText = res.getString(netInfo.isRoaming() ? R.string.on : R.string.off);
            this.roamingStatusTextView.setText(String.format(res.getString(R.string.roamingStatus), statusText));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.updateStatusView();
        this.updateRoamingView();
    }


    /**
     * Importing client.ovpn file
     */
    private void import_ovpn() {
        InputStream inputStream = getResources().openRawResource(R.raw.node1);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        mProfile = VpnManager.importConfig(this, inputStreamReader, "Node1 - almogbaku(17)");
    }

    /**
     * Set vpnStatus
     * @param vpnStatus boolean
     */
    private void setVpnStatus(boolean vpnStatus) {
        this.vpnStatus = vpnStatus;
        if(!this.switchManuallyChanged) {
            if (vpnStatus) {
                this.startVPN();
            } else {
                this.stopVPN();
            }
        }
        this.switchManuallyChanged = false;
        this.updateStatusView();
    }
    /**
     * Updating the view to show the vpnStatus
     */
    private void updateStatusView() {
        Resources res = this.getResources();
        String statusText = res.getString(this.vpnStatus ? R.string.on : R.string.off);

        this.vpnStatusTextView.setText(String.format(res.getString(R.string.vpnStatus), statusText));
    }
    /**
     * Starting the VPN Service
     */
    private void startVPN() {
        if(mProfile != null) {
            Intent intent = new Intent(this, VpnManager.class);
            intent.setAction(VpnManager.ACTION_CONNECT);

            intent.putExtra(VpnManager.EXTRA_PROFILE_UUID, mProfile.getUUIDString());
            startService(intent);
        }
    }


    /**
     * Stopping the VPN Service
     */
    private void stopVPN() {
        Intent intent = new Intent(this, VpnManager.class);
        intent.setAction(VpnManager.ACTION_DISCONNECT);

        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == STOP_REQUEST) {
            if(resultCode == RESULT_CANCELED) {
                this.switchManuallyChanged = true;
                this.vpnSwitcher.setChecked(true);
            }
        }
    }
}

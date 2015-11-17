package net.rimoto.android.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import net.rimoto.android.R;
import net.rimoto.core.Session;
import net.rimoto.core.utils.VpnUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ProfileManager;

@EFragment(R.layout.fragment_debug)
public class DebugFragment extends Fragment {
    @ViewById(R.id.access_token)
    protected EditText accessTokenEditTextView;

    @ViewById(R.id.sid)
    protected EditText sidEditTextView;

    @ViewById(R.id.internalIp)
    protected EditText ipEditTextView;

    @ViewById(R.id.wifiPolicy)
    protected Spinner wifiPolicySpinner;

    private VpnProfile mVpnProfile;

    @AfterViews
    protected void afterViews() {
        accessTokenEditTextView.setText(Session.getCurrentAccessToken().getToken());
        sidEditTextView.setText("" + Session.getCurrentSubscriber().getId());
        ipEditTextView.setText(getIpAddress());

        mVpnProfile = ProfileManager
                .get(getActivity(), VpnUtils.getCurrentProfileUUID(getActivity()));

        ArrayAdapter<VpnProfile.WifiPolicy> adapter = new ArrayAdapter<VpnProfile.WifiPolicy>(getActivity(),
                android.R.layout.simple_spinner_item, VpnProfile.WifiPolicy.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        wifiPolicySpinner.setAdapter(adapter);
        wifiPolicySpinner.setSelection(mVpnProfile.mWifiPolicy.ordinal());
        wifiPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mVpnProfile.mWifiPolicy = VpnProfile.WifiPolicy.values()[position];
                ProfileManager.getInstance(getContext()).saveProfile(getContext(), mVpnProfile);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private String getIpAddress() {
        try {
            for(Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}

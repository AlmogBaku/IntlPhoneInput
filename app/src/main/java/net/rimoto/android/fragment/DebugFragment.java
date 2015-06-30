package net.rimoto.android.fragment;

import android.support.v4.app.Fragment;
import android.widget.EditText;

import net.rimoto.android.R;
import net.rimoto.core.Session;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@EFragment(R.layout.fragment_debug)
public class DebugFragment extends Fragment {
    @ViewById(R.id.access_token)
    protected EditText accessTokenEditTextView;

    @ViewById(R.id.sid)
    protected EditText sidEditTextView;

    @ViewById(R.id.internalIp)
    protected EditText ipEditTextView;

    @AfterViews
    protected void afterViews() {
        accessTokenEditTextView.setText(Session.getCurrentAccessToken().getToken());
        sidEditTextView.setText(""+Session.getCurrentSubscriber().getId());
        ipEditTextView.setText(getIpAddress());
    }

    public static String getIpAddress() {
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

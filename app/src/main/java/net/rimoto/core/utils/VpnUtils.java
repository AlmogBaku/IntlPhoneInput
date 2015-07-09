package net.rimoto.core.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import net.rimoto.core.API;
import net.rimoto.vpnlib.RimotoPolicy;
import net.rimoto.vpnlib.VpnLog;
import net.rimoto.vpnlib.VpnManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VpnStatus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class VpnUtils {
    public static final String VPN_PROFILE_UUID = "net.rimoto.android.vpn_profile_uuid";
    private static VpnStatus.StateListener mStateListener;

    /**
     * Import VPN Config from CORE
     * @param context Context
     */
    public static void importVPNConfig(Context context) {
        API.getInstance().getOvpn(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
//                String ovpn = new String(((TypedByteArray) response.getBody()).getBytes());
                InputStream inputStream = new ByteArrayInputStream(((TypedByteArray) response.getBody()).getBytes());
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                VpnProfile profile = VpnManager.importConfig(context, inputStreamReader, "rimoto");

                saveCurrentProfileUUID(context, profile);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * Save current profile UUID to prefs
     * @param context Context
     * @param profile VpnProfile
     */
    private static void saveCurrentProfileUUID(Context context, VpnProfile profile) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        String UUID = profile.getUUIDString();
        prefsEditor.putString(VPN_PROFILE_UUID, UUID);
        prefsEditor.apply();
    }

    /**
     * Get current profile UUID
     * @param context Context
     * @return String UUID or null if not exists
     */
    public static String getCurrentProfileUUID(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(VPN_PROFILE_UUID, null);
    }

    /**
     * Rimoto VPN State Callback
     */
    public interface RimotoConnectStateCallback {
        void connected();
        void exiting();
        void shouldntConnect();
    }

    /**
     * VPN State listener
     */
    private static class RimotoConnectStateListener implements VpnStatus.StateListener {
        private RimotoConnectStateCallback mCallback;

        public RimotoConnectStateListener(RimotoConnectStateCallback callback) {
            mCallback = callback;
        }

        @Override
        public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
            switch (state) {
                case "CONNECTED":
                    mCallback.connected();
                    this.finish();
                    break;
                case "USER_VPN_PERMISSION_CANCELLED":
                    VpnStatus.updateStateString("NOPROCESS","");
                case "EXITING":
                    mCallback.exiting();
                    this.finish();
                    break;

            }
        }

        private void finish() {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                VpnStatus.removeStateListener(this);
                mStateListener=null;
                mCallback=null;
            }, 400);
        }
    }

    /**
     * Start VPN with current profile
     * @param context Context
     */
    public static void startVPN(Context context) {
        String UUID = getCurrentProfileUUID(context);
        if(UUID != null) {
            Intent intent = new Intent(context, VpnManager.class);
            intent.setAction(VpnManager.ACTION_CONNECT);
            intent.putExtra(VpnManager.EXTRA_PROFILE_UUID, UUID);
            context.startService(intent);
        } else {
            Toast.makeText(context, "No VPN configuration found.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Vpn Connection Spinner
     * @param context Context
     * @param callback RimotoStateCallback
     */
    public static void startVPN(Context context, RimotoConnectStateCallback callback) {
        VpnProfile profile = ProfileManager.get(context, getCurrentProfileUUID(context));
        if(RimotoPolicy.shouldConnect(VpnManager.getCurrentNetworkInfo(context), profile)) {
            mStateListener = new RimotoConnectStateListener(callback);
            VpnStatus.addStateListener(mStateListener);
            startVPN(context);
        } else {
            callback.shouldntConnect();
        }
    }

    /**
     * Stop VPN shortcut
     * @param context Context
     */
    public static void stopVPN(Context context) {
        Intent intent = new Intent(context, VpnManager.class);
        intent.setAction(VpnManager.ACTION_DISCONNECT);
        context.startService(intent);
    }

    /**
     * Rimoto VPN State Callback
     */
    public interface SimpleCallback {
        void done();
    }

    /**
     * VPN State listener
     */
    private static class RimotoDisconnectStateListener implements VpnStatus.StateListener {
        private SimpleCallback mCallback;

        public RimotoDisconnectStateListener(SimpleCallback callback) {
            mCallback = callback;
        }

        @Override
        public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
            if(state.equals("NOPROCESS")) {
                mCallback.done();
                this.finish();
            }
        }

        private void finish() {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                VpnStatus.removeStateListener(this);
                mStateListener=null;
                mCallback=null;
            }, 400);
        }
    }

    /**
     * Stop VPN shortcut
     * @param context Context
     */
    public static void stopVPN(Context context, SimpleCallback callback) {
        mStateListener = new RimotoDisconnectStateListener(callback);
        VpnStatus.addStateListener(mStateListener);
        stopVPN(context);
        if(!VpnManager.isConnected()) {
            callback.done();
        }
    }

    /**
     * Send logs
     * @param context Context
     */
    public static void sendLogs(Context context) {
        try {
            String logs = VpnLog.getRecentLogs();
            String device = String.format("Brand: %s | Model: %s | Product: %s | Manufacturer: %s",
                    Build.BRAND, Build.MODEL, Build.PRODUCT, Build.MANUFACTURER);
            API.getInstance().sendLogs(logs, device, new Callback<Boolean>() {
                @Override
                public void success(Boolean response, Response response2) {
                    Toast.makeText(context, "Logs sent", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(context, "Error sending logs.", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
        } catch (IOException e) {
            Toast.makeText(context, "Error fetching logs.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

package net.rimoto.core.utils;

import net.rimoto.android.R;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;
import net.rimoto.core.models.SCEService;
import net.rimoto.vpnlib.RimotoPolicy;
import net.rimoto.vpnlib.VpnManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

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
    private static VpnStatus.LogListener mLogListener;

    /**
     * Import VPN Config from CORE [ASYNC]
     * @param context Context
     */
    public static void importVPNConfig(Context context, @Nullable Callback<VpnProfile> callback) {
        API.getInstance().getOvpn(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
//                String ovpn = new String(((TypedByteArray) response.getBody()).getBytes());
                InputStream inputStream = new ByteArrayInputStream(((TypedByteArray) response.getBody()).getBytes());
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                VpnProfile profile = VpnManager.importConfig(context, inputStreamReader, "Rimoto");
                if(profile != null) {
                    profile.mProfileCreator = "Rimoto CORE";
                    saveCurrentProfileUUID(context, profile);

                    if (callback != null) {
                        callback.success(profile, response2);
                    }
                } else {
                    if(callback!=null) {
                        callback.failure(RetrofitError.unexpectedError("Cant import config", new Exception()));
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if(callback!=null) {
                    callback.failure(error);
                }
            }
        });
    }

    /**
     * Save current profile UUID to prefs
     * @param context Context
     * @param profile VpnProfile
     */
    public static void saveCurrentProfileUUID(Context context, @Nullable VpnProfile profile) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        String UUID = (profile==null)?null:profile.getUUIDString();
        prefsEditor.putString(VPN_PROFILE_UUID, UUID);
        prefsEditor.apply();
    }

    /**
     * Remove current profile UUID to prefs
     * @param context Context
     */
    public static void removeCurrentProfileUUID(Context context) {
        saveCurrentProfileUUID(context, null);
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
        private VpnStatus.LogListener mLogListener;

        public RimotoConnectStateListener(RimotoConnectStateCallback callback,
                                          VpnStatus.LogListener logListener) {
            mCallback = callback;
            mLogListener = logListener;
        }

        @Override
        public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
            final Handler handler = new Handler(Looper.getMainLooper());
            switch (state) {
                case "CONNECTED":
                    API.clearPool();
                    API.clearCache();
                    handler.post(mCallback::connected);
                    this.finish();
                    break;
                case "USER_VPN_PERMISSION_CANCELLED":
                    VpnStatus.updateStateString("NOPROCESS", "");
                case "EXITING":
                    handler.post(mCallback::exiting);
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
                if(mLogListener!=null) {
                    VpnStatus.removeLogListener(mLogListener);
                }
            }, 400);
        }
    }

    private static class RimotoCatchFatalLogListener implements VpnStatus.LogListener {
        private static final String FATAL_ERROR = "Android establish() method returned null (Really broken network configuration?)";
        private static final String ERROR_TUN = "ERROR: Cannot open TUN";
        private Context mContext;

        public RimotoCatchFatalLogListener(Context context) {
            mContext = context;
        }
        private boolean fatalDetected = false;

        @Override
        public void newLog(VpnStatus.LogItem logItem) {
            if(!fatalDetected) {
                if(
                    (logItem.getLogLevel()==VpnStatus.LogLevel.ERROR && logItem.getString(null).equals(FATAL_ERROR))
                    || (logItem.getLogLevel()==VpnStatus.LogLevel.INFO && logItem.getString(null).equals(ERROR_TUN))
                ) {
                    fatalDetected=true;
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(this::fatalCatched);
                }
            }
        }

        private void fatalCatched() {
            if(mContext==null) {
                return;
            }

            VpnManager.setActiveProfileDisconnected(mContext);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.oops_title)
                    .setMessage(R.string.connectionFatalError_message)
                    .setCancelable(false);

            AlertDialog alert = builder.create();
            alert.show();
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
     * @param callback RimotoConnectStateCallback
     */
    public static void startVPN(Context context, RimotoConnectStateCallback callback) {
        VpnProfile profile = ProfileManager.get(context, getCurrentProfileUUID(context));
        if (RimotoPolicy.shouldConnect(VpnManager.getCurrentNetworkInfo(context), profile)) {
            mLogListener = new RimotoCatchFatalLogListener(context);
            VpnStatus.addLogListener(mLogListener);
            mStateListener = new RimotoConnectStateListener(callback, mLogListener);
            VpnStatus.addStateListener(mStateListener);
        } else {
            callback.shouldntConnect();
        }
        API.getInstance().getPolicies(new Callback<List<Policy>>() {
            public void success(List<Policy> policies, Response response) {
                saveAllowedAppsVpnAndStartVPN(context, policies, callback);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
                callback.exiting();
            }
        });
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
                this.finish();
            }
        }

        private void finish() {
            API.clearPool();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                mCallback.done();
                VpnStatus.removeStateListener(this);
                mStateListener = null;
                mCallback = null;
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
     * Obtain set of all android bundle ids (package names) from the list of all policies
     * @param policies user policies
     * @return HashSet<String> of all available android bundle ids (package names) | null for all open
     */
    @Nullable
    public static HashSet<String> getAndroidBundleIdSetFromPolicies(List<Policy> policies) {
        HashSet<String> allowedApps = new HashSet<>();
        for (int i = 0; i < policies.size(); i++) {
            List<SCEService> services = policies.get(i).getServices();
            for (int j = 0; j < services.size(); j++) {
                allowedApps.add(services.get(j).getAndroidBundleId());
                if(services.get(j).getSlug().equals("allopen")) return null;
            }
        }
        return allowedApps;
    }


    public static void saveAllowedAppsVpnAndStartVPN(Context context, List<Policy> policies,
            RimotoConnectStateCallback callback) {

        HashSet<String> allowedApps = getAllowedApps(context, policies);

        VpnProfile vpnProfile = ProfileManager
                .get(context, VpnUtils.getCurrentProfileUUID(context));
        if (vpnProfile == null) {
            VpnUtils.importVPNConfig(context, new Callback<VpnProfile>() {
                @Override
                public void success(VpnProfile vpnProfile, Response response) {
                    if(allowedApps!=null) {
                        vpnProfile.mAllowedAppsVpn = allowedApps;
                        vpnProfile.mAllowedAppsVpnAreDisallowed = false;
                    }
                    ProfileManager.getInstance(context).saveProfile(context, vpnProfile);
                    VpnUtils.saveCurrentProfileUUID(context, vpnProfile);
                    startVPN(context);
                }

                @Override
                public void failure(RetrofitError error) {
                    callback.exiting();
                }
            });
        } else {
            if(allowedApps!=null) {
                vpnProfile.mAllowedAppsVpn = allowedApps;
                vpnProfile.mAllowedAppsVpnAreDisallowed = false;
            }
            ProfileManager.getInstance(context).saveProfile(context, vpnProfile);
            VpnUtils.saveCurrentProfileUUID(context, vpnProfile);
            startVPN(context);
        }
    }

    private static HashSet<String> getAllowedApps(Context context, List<Policy> policies) {
        HashSet<String> allowedApps = VpnUtils.getAndroidBundleIdSetFromPolicies(policies);
        if(allowedApps!=null) {
            allowedApps.add(context.getPackageName());
            allowedApps.add("com.android.chrome");
            allowedApps.add("com.google.android.browser");
            allowedApps.add("com.android.captiveportallogin");
            allowedApps.add("com.android.settings");
            allowedApps.add("com.google.android.gms");
        }
        return allowedApps;
    }

}

package net.rimoto.wheninroam.utils.RimotoCore;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class RimotoCore {
    private static String CLIENT_ID     = null;
    private static String REDIRECT_URI  = "http://localhost";
    private static String RESPONSE_TYPE = "code";
    private static String AUTH_ENDPOINT = "http://core.rimoto.net/api/oauth/v2/auth";
    private static String OAUTH_SCOPE   = "";

    /**
     * Initialize
     * @param client Client_ID
     * @param redirect_uri Redirect_URI
     */
    public static void init(String client, String redirect_uri) {
        CLIENT_ID = client;
        REDIRECT_URI = redirect_uri;
    }
    public static void init(String client) {
        CLIENT_ID = client;
    }
    public static void setAuthType(String authType) {
        RESPONSE_TYPE = authType;
    }


    /**
     * Helper: Build Authentication Uri
     * @param context Context
     * @param redirectParams HashMap of extra parameters
     * @return Uri
     */
    private Uri buildAuthUri(Context context, HashMap<String,String> redirectParams) {
        Uri redirectUri = buildRedirectUri(redirectParams);

        Uri.Builder auth_uri = new Uri.Builder();
        auth_uri.encodedPath(AUTH_ENDPOINT);
        auth_uri.appendQueryParameter("client_id", CLIENT_ID);
        auth_uri.appendQueryParameter("response_type", RESPONSE_TYPE);
        auth_uri.appendQueryParameter("redirect_uri", redirectUri.toString());
        auth_uri.appendQueryParameter("scope", OAUTH_SCOPE);

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String msisdn = telephonyManager.getLine1Number();
            if (msisdn != null) {
                auth_uri.appendQueryParameter("msisdn", msisdn);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return auth_uri.build();
    }

    /**
     * Helper: Build Redirect Uri
     * @param redirectParams HashMap of extra parameters
     * @return Uri
     */
    private Uri buildRedirectUri(HashMap<String,String> redirectParams) {
        Uri.Builder redirect_uri = new Uri.Builder();
        redirect_uri.encodedPath(REDIRECT_URI);
        for(Object o : redirectParams.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            redirect_uri.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
        }
        return redirect_uri.build();
    }

    private LinearLayout mLinearLayout;
    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    private void createLinearWebView(Context context) {
        mLinearLayout = new LinearLayout(context);

        mWebView = new WebView(mLinearLayout.getContext());
        mLinearLayout.addView(mWebView);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mDialog.setContentView(mLinearLayout);
    }

    private Dialog mDialog;
    private void createDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
    }
    private void hideAuthDialog() {
        unregisterSMSReceiver();
        if(mDialog!=null) {
            mDialog.dismiss();
            mLinearLayout = null;
            mWebView = null;
            mDialog = null;
        }
    }
    private void showAuthDialog() {
        registerSMSReceiver();
        if(mDialog!=null) {
            mDialog.show();
        }
    }


    /**
     * Open authentication dialog
     * @param context Context
     * @param redirectParams  HashMap of extra parameters
     * @param callback RimotoCallback
     */
    public void auth(Context context, HashMap<String,String> redirectParams, final RimotoCallback callback) {
        showSpinner(context);

        createDialog(context);
        mDialog.setOnCancelListener(d -> {
            hideAuthDialog();
            callback.done(null, new RimotoException("CANCELED"));
        });
        createLinearWebView(context);

        RimotoCallback cb = (token, error) -> {
            hideAuthDialog();
            if(error!=null && token instanceof AccessToken) {
                Session.saveAccessToken(context, (AccessToken) token);
            }
            callback.done(token, error);
        };
        
        mWebView.setWebViewClient(new RimotoWebViewClient(cb) {
            private boolean shown = false;

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!redirect) {
                    loadingFinished = true;
                }
                if (loadingFinished && !shown) {
                    hideSpinner();
                    showAuthDialog();
                    shown = true;
                } else {
                    redirect = false;
                }
            }
        });

        String AuthUrl = buildAuthUri(context, redirectParams).toString();
        mWebView.loadUrl(AuthUrl);
    }
    public void auth(Context context, RimotoCallback cb) {
        auth(context, new HashMap<>(), cb);
    }

    private SmsPincodeReceiver mRimotoSMSPincodeReceiver;
    /**
     * Register SMS receiver for auto-detect the SMS
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void registerSMSReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        filter.setPriority(999);

        mRimotoSMSPincodeReceiver = new SmsPincodeReceiver((pin) -> {
            mWebView.loadUrl(
                    "javascript:(function() {" +
                            "$('#password').val('"+pin+"');" +
                            "$('button[type=submit]').click();" +
                            "})();");
            unregisterSMSReceiver();
        });

        mDialog.getContext().registerReceiver(mRimotoSMSPincodeReceiver, filter);
    }

    /**
     * Unregister the SMS receiver
     */
    private void unregisterSMSReceiver() {
        if(mRimotoSMSPincodeReceiver !=null) {
            mDialog.getContext().unregisterReceiver(mRimotoSMSPincodeReceiver);
            mRimotoSMSPincodeReceiver = null;
        }
    }

    private ProgressDialog dialog;
    private void showSpinner(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void hideSpinner() {
        dialog.hide();
        dialog=null;
    }

    private static RimotoCore mInstance = null;
    public static RimotoCore getInstance() throws RimotoException {
        if(CLIENT_ID==null) {
            throw new RimotoException("You must Initialize the API");
        }

        if(mInstance == null)
        {
            mInstance = new RimotoCore();
        }
        return mInstance;
    }
}

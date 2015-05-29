package net.rimoto.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;

import net.rimoto.core.models.AccessToken;
import net.rimoto.core.utils.UI;

import java.util.HashMap;
import java.util.Map;

public class Login {
    private static RimotoCore sRimotoCore = null;
    private static Login sInstance = null;
    public static Login getInstance() throws RimotoException {
        if(sInstance == null)
        {
            sRimotoCore = RimotoCore.getInstance();
            sInstance = new Login();
        }
        return sInstance;
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
        auth_uri.encodedPath(sRimotoCore.getAuthEndpoint());
        auth_uri.appendQueryParameter("client_id", sRimotoCore.getClientId());
        auth_uri.appendQueryParameter("response_type", sRimotoCore.getAuthType());
        auth_uri.appendQueryParameter("redirect_uri", redirectUri.toString());
        auth_uri.appendQueryParameter("scope", sRimotoCore.getScope());

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
        redirect_uri.encodedPath(sRimotoCore.getRedirectUri());
        for(Object o : redirectParams.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            redirect_uri.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
        }
        return redirect_uri.build();
    }

    private LinearLayout mLinearLayout;
    private WebView mWebView;
    private Dialog mDialog;

    /**
     * Create LoginView
     * @param context Context
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void createLoginView(Context context) {
        mLinearLayout = new LinearLayout(context);

        mWebView = new WebView(mLinearLayout.getContext());
        mLinearLayout.addView(mWebView);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }


    /**
     * Create LoginDialog
     * @param context Context
     * @param callback RimotoCallback
     */
    private void createLoginDialog(Context context, RimotoCallback callback) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
        mDialog.setContentView(mLinearLayout);
        mDialog.setOnCancelListener(d -> {
            dismissLoginDialog();
            callback.done(null, new RimotoException("CANCELED"));
        });
    }

    /**
     * Show the LoginView
     */
    private void showLoginDialog() {
        registerSMSReceiver();
        if(mDialog!=null) {
            mDialog.show();
        }
    }

    /**
     * Dismiss the LoginView
     */
    private void dismissLoginDialog() {
        unregisterSMSReceiver();
        if(mDialog!=null) {
            mDialog.dismiss();
            mLinearLayout = null;
            mWebView = null;
            mDialog = null;
        }
    }

    private SmsPincodeReceiver mRimotoSMSPincodeReceiver;
    /**
     * Register SMS receiver for auto-detect the SMS
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void registerSMSReceiver() {
        if(mDialog==null) return;

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
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void unregisterSMSReceiver() {
        if(mRimotoSMSPincodeReceiver !=null) {
            mDialog.getContext().unregisterReceiver(mRimotoSMSPincodeReceiver);
            mRimotoSMSPincodeReceiver = null;
        }
    }

    /**
     * Open authentication dialog
     * @param context Context
     * @param redirectParams  HashMap of extra parameters
     * @param callback RimotoCallback
     */
    public void auth(Context context, HashMap<String,String> redirectParams, final RimotoCallback callback) {
        createLoginView(context);
        createLoginDialog(context, callback);
        RimotoCallback cb = (token, error) -> {
            dismissLoginDialog();
            if(error==null && token instanceof AccessToken) {
                Session.saveAccessToken((AccessToken) token);
            }
            callback.done(token, error);
        };

        UI.showSpinner(context);

        mWebView.setWebViewClient(new RimotoWebViewClient(cb) {
            private boolean shown = false;

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!shown) {
                    UI.hideSpinner();
                    showLoginDialog();
                    shown = true;
                }
                super.onPageFinished(view, url);
            }
        });

        String AuthUrl = buildAuthUri(context, redirectParams).toString();
        mWebView.loadUrl(AuthUrl);
    }
    public void auth(Context context, RimotoCallback cb) {
        auth(context, new HashMap<>(), cb);
    }
}

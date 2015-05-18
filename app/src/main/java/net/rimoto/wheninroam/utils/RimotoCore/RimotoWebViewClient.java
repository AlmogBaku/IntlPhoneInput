package net.rimoto.wheninroam.utils.RimotoCore;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RimotoWebViewClient extends WebViewClient {
    private RimotoCallback mCallback;

    public RimotoWebViewClient(RimotoCallback cb) {
        mCallback = cb;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        mCallback.done(null, new RimotoException(errorCode, description, failingUrl));
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (url.contains("code=")) {
            Uri uri = Uri.parse(url);

            CodeToken codeToken = new CodeToken(uri.getQueryParameter("code"));
            mCallback.done(codeToken, null);
        } else if (url.contains("token=")) {
            Uri uri = Uri.parse(url.replace("#","?"));
            AccessToken accessToken = new AccessToken(
                    uri.getQueryParameter("access_token"),
                    uri.getQueryParameter("expires_in"),
                    uri.getQueryParameter("token_type")
            );
            mCallback.done(accessToken, null);
        } else if (url.contains("error=access_denied")) {
            Uri uri = Uri.parse(url);
            mCallback.done(null, new RimotoException(403, uri.getQueryParameter("error_description"), url));
        }
    }
}

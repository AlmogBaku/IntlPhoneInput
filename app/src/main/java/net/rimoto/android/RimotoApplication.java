package net.rimoto.android;

import android.app.Application;

import net.rimoto.core.RimotoCore;
import net.rimoto.core.RimotoException;

import org.androidannotations.annotations.EApplication;

@EApplication
public class RimotoApplication extends Application {
    private final String rimotoClientID = "3_4xs1w1ynms08w8sc0w0k8k40kksccwwwwo8scso44c08c4goo4";
    private final String rimoto_authURI = "http://wheninroam.parseapp.com/inbound_rimoto_auth";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            RimotoCore.init(this, rimotoClientID, rimoto_authURI);
            RimotoCore.setAuthType("token");
        } catch (RimotoException ignored) {}
    }
}

package net.rimoto.wheninroam;

import android.app.Application;

import net.rimoto.wheninroam.utils.RimotoCore.RimotoCore;

import org.androidannotations.annotations.EApplication;

@EApplication
public class WhenInRoamApplication extends Application {
    private final String rimotoClientID = "3_4xs1w1ynms08w8sc0w0k8k40kksccwwwwo8scso44c08c4goo4";

    @Override
    public void onCreate() {
        super.onCreate();
        RimotoCore.init(rimotoClientID, "http://wheninroam.parseapp.com/inbound_rimoto_auth");
        RimotoCore.setAuthType("token");
    }
}

package net.rimoto.android;

import android.app.Application;

import net.rimoto.core.RimotoCore;
import net.rimoto.core.RimotoException;

import org.androidannotations.annotations.EApplication;

@EApplication
public class RimotoApplication extends Application {
    private final String rimotoClientID = "1_56hxwnph188wkosoc8c8ccswskkwcwggc4k8o4k0gs4ksgsk08";
    private final String rimoto_authURI = "http://localhost/";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            RimotoCore.init(this, rimotoClientID, rimoto_authURI);
            RimotoCore.setAuthType("token");
        } catch (RimotoException ignored) {}
    }
}

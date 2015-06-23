package net.rimoto.android;

import android.app.Application;

import com.instabug.library.Instabug;

import net.rimoto.core.RimotoCore;

import org.androidannotations.annotations.EApplication;

@EApplication
public class RimotoApplication extends Application {
    private final String rimotoClientID = "1_56hxwnph188wkosoc8c8ccswskkwcwggc4k8o4k0gs4ksgsk08";
    private final String rimoto_authURI = "http://localhost/";

    @Override
    public void onCreate() {
        super.onCreate();
        Instabug.initialize(this, "cfad51965edc86000b61bd545c4d84d2");

        RimotoCore.init(this, rimotoClientID, rimoto_authURI);
        RimotoCore.setAuthType("token");
    }
}

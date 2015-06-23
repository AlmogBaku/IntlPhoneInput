package net.rimoto.android;

import android.app.Application;
import android.content.Context;

import com.instabug.library.Instabug;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import net.rimoto.android.utils.LeakSlackUploadService;
import net.rimoto.core.RimotoCore;

import org.androidannotations.annotations.EApplication;

@EApplication
public class RimotoApplication extends Application {
    private final String rimotoClientID = "1_56hxwnph188wkosoc8c8ccswskkwcwggc4k8o4k0gs4ksgsk08";
    private final String rimoto_authURI = "http://localhost/";

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = installLeakCanary();
        Instabug.initialize(this, "cfad51965edc86000b61bd545c4d84d2");

        RimotoCore.init(this, rimotoClientID, rimoto_authURI);
        RimotoCore.setAuthType("token");
    }


    public static RefWatcher getRefWatcher(Context context) {
        RimotoApplication application = (RimotoApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    protected RefWatcher installLeakCanary() {
        return LeakCanary.install(this, LeakSlackUploadService.class, AndroidExcludedRefs.createAppDefaults().build());
    }

    private RefWatcher refWatcher;
}

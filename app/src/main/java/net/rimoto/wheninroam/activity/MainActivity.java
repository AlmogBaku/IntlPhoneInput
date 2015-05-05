package net.rimoto.wheninroam.activity;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.rimoto.wheninroam.R;
import net.rimoto.wheninroam.utils.RimotoCore.Login;
import net.rimoto.wheninroam.utils.RimotoCore.RimotoCore;
import net.rimoto.wheninroam.utils.RimotoCore.RimotoException;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {
    @OptionsItem(R.id.action_settings)
    public void actionSettings() {
        Log.d("tst", "lol");
    }

    @Click(R.id.login_btn)
    public void loginClick() {
        Log.d("tst","wow!");
        try {
            Login.getInstance().auth(this, (token, error) -> {
                if(error==null) {
                    Log.d("tst", token.toString());
                }
            });
        } catch (RimotoException e) {
            e.printStackTrace();
        }
    }
}

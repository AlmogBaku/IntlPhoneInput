package net.rimoto.android.activity;

import android.util.Log;

import net.rimoto.android.R;
import net.rimoto.core.Login;
import net.rimoto.core.RimotoException;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends RimotoCompatActivity {
    @OptionsItem(R.id.action_settings)
    public void actionSettings() {
        Log.d("tst", "lol");
    }
}

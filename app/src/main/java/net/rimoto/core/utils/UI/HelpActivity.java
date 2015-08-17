package net.rimoto.core.utils.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import net.rimoto.android.R;
import net.rimoto.android.activity.RimotoCompatActivity;
import net.rimoto.core.utils.UI.fragment.HelpFaqFragment;

public class HelpActivity extends RimotoCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        Fragment fragment = new HelpFaqFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "dialog")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

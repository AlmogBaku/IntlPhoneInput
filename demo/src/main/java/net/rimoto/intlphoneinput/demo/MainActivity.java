package net.rimoto.intlphoneinput.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

//import it.sephiroth.android.library.tooltip.Tooltip;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        net.rimoto.intlphoneinput.IntlPhoneInput phoneInput = (net.rimoto.intlphoneinput.IntlPhoneInput) findViewById(R.id.phone_input);

        phoneInput.setDefaultByDialCode(39);
        phoneInput.getCountrySpinner().setEnabled(false);

        Spinner spinner = phoneInput.getCountrySpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //    private void showToolTip(Spinner spinner) {
//        Tooltip.make(this, new Tooltip.Builder()
//                .anchor(spinner, Tooltip.Gravity.BOTTOM)
//                .closePolicy(Tooltip.ClosePolicy.TOUCH_ANYWHERE_NO_CONSUME, 8000)
//                .fitToScreen(true)
//                .text("Change here the country!")
//                .withArrow(true)
//                .withOverlay(true)
//                .withStyleId(R.style.tool_tip_balloon_layout))
//                .show();
//    }
}

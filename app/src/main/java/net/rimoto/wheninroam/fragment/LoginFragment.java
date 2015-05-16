package net.rimoto.wheninroam.fragment;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.widget.LinearLayout;

import net.rimoto.wheninroam.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {
    private int mPosition;

    public void setPosition(int position) {
        mPosition=position;
    }

    @ViewById(R.id.login_linear)
    LinearLayout mLinearLayout;

    @AfterViews
    public void setOriginalWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mLinearLayout.getLayoutParams().width = size.x;
    }
}
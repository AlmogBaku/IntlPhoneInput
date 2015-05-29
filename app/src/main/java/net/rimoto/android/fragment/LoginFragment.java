package net.rimoto.android.fragment;

import android.support.v4.app.Fragment;

import net.rimoto.android.R;

import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {
    private int mPosition;

    public void setPosition(int position) {
        mPosition = position;
    }

}

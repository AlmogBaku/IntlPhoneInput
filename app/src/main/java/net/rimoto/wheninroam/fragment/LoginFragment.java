package net.rimoto.wheninroam.fragment;

import android.support.v4.app.Fragment;

import net.rimoto.wheninroam.R;

import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_login_1)
public class LoginFragment extends Fragment {
    private int mPosition;
    public void setPosition(int position) {
        mPosition=position;
    }
}
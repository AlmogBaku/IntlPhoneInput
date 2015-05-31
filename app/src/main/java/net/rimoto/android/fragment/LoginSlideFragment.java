package net.rimoto.android.fragment;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import net.rimoto.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_login)
public class LoginSlideFragment extends Fragment {
    private int mPosition = -1;

    @ViewById(R.id.login_slide)
    protected ImageView mLoginSlide;

    public void setPosition(int position) {
        mPosition = position;
    }

    @AfterViews
    protected void changeImage() {
        if(mPosition != -1) {
            if(mPosition%2==0) mPosition=0;
            else mPosition=4;
            int id = getResources().getIdentifier("login_slide_pic"+mPosition, "drawable", getActivity().getPackageName());
            mLoginSlide.setImageResource(id);
        }
    }
}

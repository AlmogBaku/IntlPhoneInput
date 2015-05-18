package net.rimoto.wheninroam.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.widget.FrameLayout;

import net.rimoto.wheninroam.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {
    private int mPosition;
    private boolean mAfterViewsAnimate = false;

    public void setPosition(int position) {
        mPosition = position;
    }

    @ViewById(R.id.background_view)
    FrameLayout mBackgroundView;

    @Click(R.id.button)
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void startAnimations() {
        resetAnimation();

        Transition transition = new AutoTransition();
        transition.setDuration(3000);
        TransitionManager.beginDelayedTransition(mBackgroundView, transition);

        FrameLayout.LayoutParams bgParams = (FrameLayout.LayoutParams) mBackgroundView.getLayoutParams();
        bgParams.setMargins(0, 0, 0, 0);
        mBackgroundView.setLayoutParams(bgParams);
    }

    @AfterViews
    protected void AfterViews() {
        // Initialize LayoutParams
        FrameLayout.LayoutParams bgParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT
        );
        mBackgroundView.setLayoutParams(bgParams);
        resetAnimation();
    }

    private void resetAnimation() {
        FrameLayout.LayoutParams bgParams = (FrameLayout.LayoutParams) mBackgroundView.getLayoutParams();
        bgParams.setMargins(-400, 0, 0, 0);
        mBackgroundView.setLayoutParams(bgParams);
        Log.d("tst","rest");
    }
}
package net.rimoto.android.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.rimoto.android.R;
import net.rimoto.android.activity.WizardActivity_;
import net.rimoto.android.adapter.SCEServicesRecycleAdapter;
import net.rimoto.android.adapter.TagsRecycleAdapter;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;
import net.rimoto.vpnlib.VpnManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {
    public static final String EXTRA_POLICIES = "EXTRA_POLICIES";

    @ViewById(R.id.tagsRecycler)
    protected RecyclerView mTagsRecycler;

    @ViewById(R.id.actionBtn)
    protected Button mActionButton;

    @ViewById(R.id.activateBtn)
    protected ViewGroup mActivateButton;

    @ViewById(R.id.main_boxa_text)
    protected TextView mMainBoxaText;

    private TagsRecycleAdapter mAdapter;

    @AfterViews
    protected void afterViews() {
        mTagsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        mTagsRecycler.setLayoutManager(linearLayoutManager);

        initiatePolicies();
        getActivity().setTitle(R.string.main_activity_title);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!VpnManager.isActive(getActivity())) {
            setAsPreview();
        } else {
            setAsConnected();
        }
    }

    /**
     * Fet policies from Intent if available,
     *  Otherwise fetch them
     */
    private void initiatePolicies() {
        Intent intent = getActivity().getIntent();
        if(intent.getExtras() != null) {
            Parcelable policiesParcel = (Parcelable) intent.getExtras().get(EXTRA_POLICIES);
            if(policiesParcel != null) {
                List<Policy> policies = Parcels.unwrap(policiesParcel);
                setPolicies(policies);
            } else {
                fetchPolicies();
            }
        } else {
            fetchPolicies();
        }
    }

    /**
     * Fetching policies from API
     */
    private void fetchPolicies() {
        API.getInstance().getPolicies(new Callback<List<Policy>>() {
            public void success(List<Policy> policies, Response response) {
                setPolicies(policies);
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * Set the policies
     * @param policies List<Policy>
     */
    public void setPolicies(List<Policy> policies) {
        if(mTagsRecycler !=null) {
            mAdapter = new TagsRecycleAdapter(policies, true, serviceCallback);
            mTagsRecycler.setAdapter(mAdapter);
        }
        if(mPageState==PageState.Preview) {
            mAdapter.setPreview(true);
        } else {
            mAdapter.setPreview(false);
        }

        //Set as premium if have a non "appPolicy" exists
        for (Policy policy : policies) {
            if (!policy.getName().equals(TagsRecycleAdapter.FREE_POLICY_NAME)) {
                setAsPremium();
                break;
            }
        }

        //Show BTN after data initiated
        if (mActionButton != null && mPageState!=PageState.Preview) {
            mActionButton.setVisibility(View.VISIBLE);
        }
    }

    private enum PageState {Preview, ConnectedFree, ConnectedPremium }
    private PageState mPageState = PageState.ConnectedFree;

    private void setAsPreview() {
        mPageState = PageState.Preview;
        if(mActionButton != null) {
            mActionButton.setVisibility(View.GONE);
        }
        if(mActivateButton != null) {
            mActivateButton.setVisibility(View.VISIBLE);
        }
        if(mMainBoxaText != null) {
            mMainBoxaText.setText(R.string.main_boxa_text_preview);
        }
        if(mAdapter!=null) {
            mAdapter.setPreview(true);
        }
        getActivity().setTitle(R.string.main_activity_title_preview);
    }
    private void setAsConnected() {
        if(mPageState != PageState.Preview) {
            return; //Change only if the state changed from preview
        }

        getActivity().setTitle(R.string.main_activity_title);
        mPageState = PageState.ConnectedFree;
        if(mMainBoxaText != null) {
            mMainBoxaText.setText(R.string.main_boxa_text);
        }

        if(mActivateButton != null) {
            mActivateButton.setVisibility(View.GONE);
        }
        if (mActionButton != null && mAdapter!=null) {
            mActionButton.setVisibility(View.VISIBLE);
        }
        if(mAdapter!=null) {
            mAdapter.setPreview(false);
        }

        if(havePremium) {
            setAsPremium();
        } else {
            if(mActionButton !=null) {
                mActionButton.setText(R.string.actionBtn_topUp);
            }
        }
    }
    private boolean havePremium = false;
    private void setAsPremium() {
        havePremium=true;

        if(mPageState == PageState.Preview) {
            return; // Don't change anything on preview
        }

        mPageState = PageState.ConnectedPremium;
        if(mActionButton !=null) {
            mActionButton.setText(R.string.actionBtn_seePremium);
        }
    }

    private SCEServicesRecycleAdapter.ServiceCallback serviceCallback = (view, service) -> {
        if(mPageState == PageState.Preview) {
            return;
        }

        String bundleID = service.getAndroidBundleId();
        if(bundleID != null && isPackageInstalled(bundleID)) {
            launchApp(bundleID);
        } else if(service.getWebApp()!=null) {
            launchUrl(service.getWebApp());
        }
    };

    @Click(R.id.activateBtn)
    protected void startWizard() {
        Intent intent = new Intent(getActivity(), WizardActivity_.class);
        getActivity().startActivity(intent);
    }

    private void launchUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        getActivity().startActivity(intent);
    }

    private void launchApp(String bundleID) {
        Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(bundleID);
        getActivity().startActivity(LaunchIntent);
    }

    private boolean isPackageInstalled(String bundleID) {
        try {
            getActivity().getPackageManager().getPackageInfo(bundleID, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Click(R.id.actionBtn)
    protected void actionBtn() {
        switch (mPageState) {
            case Preview:
                startWizard();
                break;
            case ConnectedFree:
                TopUpFragment topUpFragment = new TopUpFragment_();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, topUpFragment).addToBackStack(null)
                        .commit();
                break;
            case ConnectedPremium:
                PlansFragment plansFragment = new PlansFragment_();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, plansFragment).addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTagsRecycler = null;
    }
}

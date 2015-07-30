package net.rimoto.android.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import net.rimoto.android.R;
import net.rimoto.android.adapter.TagsRecycleAdapter;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {
    @ViewById
    protected RecyclerView tagsRecycler;

    @ViewById(R.id.actionBtn)
    protected Button actionButton;

    @AfterViews
    protected void afterViews() {
        API.getInstance().getPolicies(policiesCB);
        tagsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        tagsRecycler.setLayoutManager(linearLayoutManager);
    }

    private Boolean havePremiumPlan = false;
    private Callback<List<Policy>> policiesCB = new Callback<List<Policy>>() {
        public void success(List<Policy> policies, Response response) {
            if(tagsRecycler!=null) {
                TagsRecycleAdapter adapter = new TagsRecycleAdapter(policies, true);
                tagsRecycler.setAdapter(adapter);
            }
            for(Policy policy:policies) {
                if(!policy.getName().equals(TagsRecycleAdapter.FREE_POLICY_NAME)) {
                    setPermiumPlan();
                    break;
                }
            }
            actionButton.setVisibility(View.VISIBLE);
        }

        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };

    private void setPermiumPlan() {
        havePremiumPlan = true;
        if(actionButton!=null) {
            actionButton.setText(R.string.actionBtn_seePremium);
        }
    }


    @Click(R.id.actionBtn)
    protected void actionBtn() {
        if(!havePremiumPlan) {
            TopUpFragment topUpFragment = new TopUpFragment_();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, topUpFragment).addToBackStack(null)
                    .commit();
        } else {
            PlansFragment plansFragment = new PlansFragment_();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, plansFragment).addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tagsRecycler=null;
    }
}

package net.rimoto.android.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.rimoto.android.R;
import net.rimoto.android.adapter.TagsRecycleAdapter;
import net.rimoto.android.views.CircleProgressView;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_plans)
public class PlansFragment extends Fragment {
    @ViewById
    protected RecyclerView tagsRecycler;

    @AfterViews
    protected void afterViews() {
        API.getInstance().getPolicies(policiesCB);
        tagsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        tagsRecycler.setLayoutManager(linearLayoutManager);
    }

    private Callback<List<Policy>> policiesCB = new Callback<List<Policy>>() {
        public void success(List<Policy> policies, Response response) {
            for(Policy policy:policies) {
                if(!policy.getName().equals(TagsRecycleAdapter.FREE_POLICY_NAME)) {
                    setPermiumPlan(policy);
                }
            }
            if(tagsRecycler!=null) {
                TagsRecycleAdapter adapter = new TagsRecycleAdapter(policies, false);
                tagsRecycler.setAdapter(adapter);
            }
        }

        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };

    @ViewById(R.id.percentage)
    protected CircleProgressView circleProgressView;

    @ViewById(R.id.planDetails)
    protected ViewGroup mPlanDetails;

    public void setPermiumPlan(Policy policy) {
        if(policy.getStartTime()==null) return;

        long now = new Date().getTime();
        long endTime = policy.getEndTime().getTime();
        long diffTime = endTime-now;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        float percentage = (float) (100*diffTime)/(policy.getExpiresIn()*1000*60);

        circleProgressView.setPercentage(percentage);
        circleProgressView.setText(Long.toString(diffDays));
        mPlanDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tagsRecycler=null;
    }
}

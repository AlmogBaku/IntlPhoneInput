package net.rimoto.android.fragment;

import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.rimoto.android.R;
import net.rimoto.android.adapter.TagsRecycleAdapter;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;

import org.androidannotations.annotations.AfterViews;
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

    @AfterViews
    protected void afterViews() {
        API.getInstance().getPolicies(policiesCB);
        tagsRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        tagsRecycler.setLayoutManager(linearLayoutManager);
    }

    private Callback<List<Policy>> policiesCB = new Callback<List<Policy>>() {
        public void success(List<Policy> policies, Response response) {
            TagsRecycleAdapter adapter = new TagsRecycleAdapter(policies, getActivity());
            tagsRecycler.setAdapter(adapter);
        }

        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        tagsRecycler=null;
    }
}

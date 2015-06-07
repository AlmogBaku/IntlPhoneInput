package net.rimoto.android.fragment;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.rimoto.android.R;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;
import net.rimoto.core.models.SCEService;
import net.rimoto.core.models.ServiceTag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
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
    }

    private Callback<List<Policy>> policiesCB = new Callback<List<Policy>>() {
        public void success(List<Policy> policies, Response response) {
            ArrayList<ServiceTag> tags = new ArrayList<>();
            for(Policy policy:policies) {
                for(SCEService service:policy.getServices()) {
                    for(ServiceTag tag:service.getTags()) {
                        int contains = tags.indexOf(tag);
                        if(contains==-1) {
                            tags.add(tag);
                        } else {
                            tag = tags.get(contains);
                        }
                        tag.addService(service);
                    }
                }
            }
            setListData(tags);
        }

        public void failure(RetrofitError error) {
            Log.d("err", error.toString());
        }
    };

    public void setListData(ArrayList<ServiceTag> listData) {

    }
}

package net.rimoto.android.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import net.rimoto.android.R;
import net.rimoto.android.adapter.SCEServicesRecycleAdapter;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;
import net.rimoto.core.models.SCEService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_plans)
public class TopUpFragment extends Fragment {

    private ArrayList<SCEService> plan1 = new ArrayList<>();
    private ArrayList<SCEService> plan2 = new ArrayList<>();
    private ArrayList<SCEService> plan3 = new ArrayList<>();

    @ViewById
    protected RecyclerView plan1_recycler;
    @ViewById
    protected RecyclerView plan2_recycler;
    @ViewById
    protected RecyclerView plan3_recycler;

    @AfterViews
    protected void afterViews() {
        initPlan1();
        initPlan2();
        initPlan3();
    }

    private void initPlan1() {
//        SCEService gmail = new SCEService();
//        gmail.setName("office365");
//        gmail.setSlug("office365");
//        plan1.add(gmail);

        SCEService office = new SCEService();
        office.setName("office365");
        office.setSlug("office365");
        plan1.add(office);

        recyclerInit(plan1_recycler, plan1);
    }

    private void initPlan2() {
        SCEService facebook = new SCEService();
        facebook.setName("Facebook");
        facebook.setSlug("facebook");
        plan2.add(facebook);

        SCEService twitter = new SCEService();
        twitter.setName("twitter");
        twitter.setSlug("twitter");
        plan2.add(twitter);

        recyclerInit(plan2_recycler, plan2);
    }

    private void initPlan3() {
        SCEService whatsapp = new SCEService();
        whatsapp.setName("whatsapp");
        whatsapp.setSlug("whatsapp");
        plan3.add(whatsapp);

        recyclerInit(plan3_recycler, plan3);
    }

    private void recyclerInit(RecyclerView recycler, ArrayList<SCEService> plan) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(linearLayoutManager);

        SCEServicesRecycleAdapter adapter = new SCEServicesRecycleAdapter();
        adapter.setServices(plan);
        recycler.setAdapter(adapter);
    }

    @Click
    protected void plan1_btn() {
        purchase(1);
    }
    @Click
    protected void plan2_btn() {
        purchase(2);
    }
    @Click
    protected void plan3_btn() {
        purchase(3);
    }

    private void purchase(int planId) {
        TelephonyManager tel = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String home_operator = API.rimotoOperatorFormat(tel.getSimOperator());
        String visited_operator = API.rimotoOperatorFormat(tel.getNetworkOperator());

        if(home_operator.equals("N/A") || visited_operator.equals("N/A")) {
            Toast toast = Toast.makeText(getActivity(), "You need a sim card in order to use rimoto", Toast.LENGTH_LONG);
            toast.show();
        }

        API.getInstance().addAppPolicy(home_operator, visited_operator, planId, new Callback<Policy>() {
            @Override
            public void success(Policy policy, Response response) {
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}

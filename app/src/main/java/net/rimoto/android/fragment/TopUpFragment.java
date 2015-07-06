package net.rimoto.android.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.rimoto.android.R;
import net.rimoto.android.adapter.SCEServicesRecycleAdapter;
import net.rimoto.core.models.SCEService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

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
}

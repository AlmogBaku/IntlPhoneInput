package net.rimoto.android.fragment;

import android.app.AlertDialog;
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
import net.rimoto.core.utils.UI;
import net.rimoto.core.utils.VpnUtils;
import net.rimoto.vpnlib.VpnManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_topup)
public class TopUpFragment extends Fragment {

    private ArrayList<SCEService> plan1 = new ArrayList<>();
    private ArrayList<SCEService> plan2 = new ArrayList<>();
    private ArrayList<SCEService> plan3 = new ArrayList<>();

    @ViewById
    protected RecyclerView plan1_recycler;
    @ViewById
    protected RecyclerView plan2_recycler;
//    @ViewById
    protected RecyclerView plan3_recycler;

    @AfterViews
    protected void afterViews() {
        initPlan1();
        initPlan2();
//        initPlan3();
    }

    private void addServiceToPlan(ArrayList<SCEService> plan, String service, String slug) {
        SCEService srv = new SCEService();
        srv.setName(service);
        srv.setSlug(slug);
        plan.add(srv);
    }
    private void addServiceToPlan(ArrayList<SCEService> plan, String service) {
        addServiceToPlan(plan, service, service);
    }

    private void initPlan1() {
        addServiceToPlan(plan1, "gmail");
        addServiceToPlan(plan1, "office365");
        addServiceToPlan(plan1, "waze");
        addServiceToPlan(plan1, "Google Map", "gmap");

        recyclerInit(plan1_recycler, plan1);
    }

    private void initPlan2() {
        addServiceToPlan(plan2, "facebook");
        addServiceToPlan(plan2, "twitter");
        addServiceToPlan(plan2, "instagram");
        addServiceToPlan(plan2, "Google Map", "gmap");

        recyclerInit(plan2_recycler, plan2);
    }

    private void initPlan3() {
        addServiceToPlan(plan3, "whatsapp");

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
//    @Click
//    protected void plan3_btn() {
//        purchase(3);
//    }

    private boolean purchasing=false;
    private void purchase(int planId) {
        if(purchasing) return;
        purchasing = true;

        TelephonyManager tel = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String home_operator = API.rimotoOperatorFormat(tel.getSimOperator());
        String visited_operator = API.rimotoOperatorFormat(tel.getNetworkOperator());

        if(home_operator.equals("N/A") || visited_operator.equals("N/A")) {
            Toast toast = Toast.makeText(getActivity(), "You need a sim card in order to use Rimoto", Toast.LENGTH_LONG);
            toast.show();
        }

        UI.showSpinner(getActivity(), "Purchasing your new plan..");
        UI.setNonCanaclableSpinner();
        API.getInstance().addAppPolicy(home_operator, visited_operator, planId, new Callback<Policy>() {
            @Override
            public void success(Policy policy, Response response) {
                UI.hideSpinner();
                purchaseSucceed(planId);
            }
            @Override
            public void failure(RetrofitError error) {
                UI.hideSpinner();
                error.printStackTrace();
            }
        });
    }

    private void purchaseSucceed(int planId) {
        if(VpnManager.isConnected()) {
            UI.showSpinner(getActivity(), "Activating your new plan..");
            UI.setNonCanaclableSpinner();
            VpnUtils.stopVPN(getActivity(), this::connectToVpn);
        } else {
            API.clearCache();
            gotoMainFragment();
        }
    }

    private void connectToVpn() {
        VpnUtils.startVPN(getActivity(), new VpnUtils.RimotoConnectStateCallback() {
            @Override
            public void connected() {
                UI.hideSpinner();
                gotoMainFragment();
            }

            @Override
            public void exiting() {
                UI.hideSpinner();
                Toast toast = Toast.makeText(getActivity(), "There was some problem with connecting you :\\", Toast.LENGTH_LONG);
                toast.show();
                VpnUtils.stopVPN(getActivity());
            }

            @Override
            public void shouldntConnect() {
                UI.hideSpinner();
                Toast toast = Toast.makeText(getActivity(), "Rimoto will connect to the cloud as soon you'll be abroad on wifi.", Toast.LENGTH_LONG);
                toast.show();
                gotoMainFragment();
            }
        });
    }

    private void gotoMainFragment() {
        purchasing=false;
        if(getActivity()!=null) {
            getActivity().getIntent().removeExtra(MainFragment.EXTRA_POLICIES);
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}

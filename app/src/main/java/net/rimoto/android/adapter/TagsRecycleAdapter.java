package net.rimoto.android.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rimoto.android.R;
import net.rimoto.core.models.Policy;
import net.rimoto.core.models.SCEService;
import net.rimoto.core.models.ServiceTag;

import net.rimoto.android.adapter.SCEServicesRecycleAdapter.ServiceCallback;

import java.util.ArrayList;
import java.util.List;

public class TagsRecycleAdapter extends RecyclerView.Adapter<TagsRecycleAdapter.ViewHolder> {
    public static final String FREE_POLICY_NAME = "appPolicy";
    private ArrayList<ServiceTag> mTags;
    private Boolean mOnlyFree = null; // null: all | false: only paid | true: only free
    private ServiceCallback mCallback;
    private boolean mPreview;

    /**
     * Constructor
     * @param policies List<Policy>
     * @param onlyFree Boolean ( null: all | false: only paid | true: only free )
     * @param callback ServiceCallback (null = no callback )
     */
    public TagsRecycleAdapter(List<Policy> policies, Boolean onlyFree, ServiceCallback callback) {
        this.mOnlyFree  = onlyFree;
        this.mTags      = policiesToTagsList(policies);
        this.mCallback  = callback;
    }

    /**
     * Convert policy list to tags->services list
     * @param policies List<Policy>
     * @return ArrayList<ServiceTag>
     */
    private ArrayList<ServiceTag> policiesToTagsList(List<Policy> policies) {
        ArrayList<ServiceTag> tags = new ArrayList<>();
        for(Policy policy:policies) {
            if(mOnlyFree !=null) {
                if(mOnlyFree && !policy.getName().equals(FREE_POLICY_NAME)) {
                    continue;
                } else if(!mOnlyFree && policy.getName().equals(FREE_POLICY_NAME)) {
                    continue;
                }
            }
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
        return tags;
    }

    @Override
    public TagsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicetag, parent, false);
        ViewHolder holder = new ViewHolder(v);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.servicesRecycler.setLayoutManager(layoutManager);

        SCEServicesRecycleAdapter adapter = new SCEServicesRecycleAdapter(mCallback);
        holder.servicesRecycler.setAdapter(adapter);

        return holder;
    }

    @Override
    public void onBindViewHolder(TagsRecycleAdapter.ViewHolder holder, int position) {
        holder.setTag(mTags.get(position), mPreview);
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public void setmPreview(boolean mPreview) {
        this.mPreview = mPreview;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tagName;
        private RecyclerView servicesRecycler;

        public ViewHolder(View itemView) {
            super(itemView);

            tagName = (TextView) itemView.findViewById(R.id.tagName);
            servicesRecycler = (RecyclerView) itemView.findViewById(R.id.servicesRecycler);
        }
        public void setTag(ServiceTag tag, boolean preview) {
            tagName.setText(tag.getName() + ":");
            SCEServicesRecycleAdapter adapter = (SCEServicesRecycleAdapter) servicesRecycler.getAdapter();
            adapter.setServices(tag.getServices(), preview);
            adapter.notifyDataSetChanged();
        }
    }
}

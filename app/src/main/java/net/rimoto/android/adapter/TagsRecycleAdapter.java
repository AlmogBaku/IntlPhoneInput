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

import java.util.ArrayList;
import java.util.List;

public class TagsRecycleAdapter extends RecyclerView.Adapter<TagsRecycleAdapter.ViewHolder> {
    private ArrayList<ServiceTag> tags;
    public TagsRecycleAdapter(List<Policy> policies) {
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
        this.tags = tags;
    }

    @Override
    public TagsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicetag, parent, false);
        ViewHolder holder = new ViewHolder(v);

        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.servicesRecycler.setLayoutManager(layoutManager);

        SCEServicesRecycleAdapter adapter = new SCEServicesRecycleAdapter();
        holder.servicesRecycler.setAdapter(adapter);

        return holder;
    }

    @Override
    public void onBindViewHolder(TagsRecycleAdapter.ViewHolder holder, int position) {
        holder.setTag(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagName;
        public RecyclerView servicesRecycler;

        public ViewHolder(View itemView) {
            super(itemView);

            tagName = (TextView) itemView.findViewById(R.id.tagName);
            servicesRecycler = (RecyclerView) itemView.findViewById(R.id.servicesRecycler);
        }
        public void setTag(ServiceTag tag) {
            tagName.setText(tag.getName());
            SCEServicesRecycleAdapter adapter = (SCEServicesRecycleAdapter) servicesRecycler.getAdapter();
            adapter.setServices(tag.getServices());
            adapter.notifyDataSetChanged();
        }
    }
}

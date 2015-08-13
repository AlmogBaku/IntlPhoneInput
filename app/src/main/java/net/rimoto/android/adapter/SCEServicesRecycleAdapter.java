package net.rimoto.android.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.rimoto.android.R;
import net.rimoto.android.rs.ScriptC_grayscale;
import net.rimoto.core.models.SCEService;

import java.util.ArrayList;
import java.util.List;

public class SCEServicesRecycleAdapter extends RecyclerView.Adapter<SCEServicesRecycleAdapter.ViewHolder> {
    private boolean mPreview=false;
    private List<SCEService> services = new ArrayList<>();
    private ServiceCallback mCallback=null;
    public interface ServiceCallback {
        void done(View itemView, SCEService service);
    }

    public SCEServicesRecycleAdapter() {}

    public SCEServicesRecycleAdapter(@Nullable ServiceCallback callback) {
        mCallback = callback;
    }

    @Override
    public SCEServicesRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(v, mCallback);
    }

    @Override
    public void onBindViewHolder(SCEServicesRecycleAdapter.ViewHolder holder, int position) {
        holder.setService(services.get(position), mPreview);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void setServices(List<SCEService> services, boolean preview) {
        this.services = services;
        this.mPreview = preview;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private boolean preview;
        private TextView serviceName;
        private ImageView serviceIcon;
        private SCEService service;
        private static String packageName;
        private TextView serviceLocalTag;
        private ServiceCallback callack;

        public ViewHolder(View itemView) {
            super(itemView);

            serviceName = (TextView) itemView.findViewById(R.id.serviceName);
            serviceIcon = (ImageView) itemView.findViewById(R.id.serviceIcon);
            serviceLocalTag = (TextView) itemView.findViewById(R.id.local_tag);

            if(packageName==null) {
                packageName = itemView.getContext().getPackageName();
            }

            itemView.setOnClickListener(onClick);
        }
        public ViewHolder(View itemView, ServiceCallback callback) {
            this(itemView);
            this.callack = callback;
        }

        public void setService(SCEService service, boolean preview) {
            this.preview = preview;
            this.service = service;
            serviceName.setText(service.getName());

            setImage();
            if(service.isLocal()) {
                serviceLocalTag.setVisibility(View.VISIBLE);
            } else {
                serviceLocalTag.setVisibility(View.GONE);
            }
        }
        private void setImage() {
            int id = itemView.getContext().getResources().getIdentifier("service_"+service.getSlug(), "drawable", packageName);
            if(id!=0) {
                if(preview) {
                    Bitmap bitmap = BitmapFactory.decodeResource(itemView.getContext().getResources(), id);
                    RenderScript rs = RenderScript.create(itemView.getContext());
                    Allocation input = Allocation.createFromBitmap(rs, bitmap); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
                    Allocation output = Allocation.createTyped(rs, input.getType());

                    ScriptC_grayscale grayscale = new ScriptC_grayscale(rs);
                    grayscale.forEach_grayscale(input, output);
                    output.copyTo(bitmap);
                    serviceIcon.setImageBitmap(bitmap);
                } else {
                    serviceIcon.setImageResource(id);
                }
            }
        }
        private View.OnClickListener onClick = (v) -> {
            if(callack!=null) {
                callack.done(itemView, service);
            }
        };
    }
}

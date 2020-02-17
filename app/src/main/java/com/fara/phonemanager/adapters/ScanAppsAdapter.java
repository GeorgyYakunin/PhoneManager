package com.fara.phonemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fara.phonemanager.R;
import com.fara.phonemanager.info.PInfo;

import java.util.List;

public class ScanAppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    private List<PInfo> list;
    private PInfo model;

    public ScanAppsAdapter(List<PInfo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_apps_scan_, parent, false);
        return new OurServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.model = this.list.get(position);

        OurServicesHolder viewHolder = (OurServicesHolder) holder;

        Glide.with(this.context).load(this.model.getAppIcon()).centerCrop().placeholder(R.drawable.ic_no_image_found).into(viewHolder.ivAppIcon);
        viewHolder.tvAppName.setText(this.model.getAppName());

        if (position == 0)
            viewHolder.pbScanLoading.setVisibility(View.VISIBLE);
        else
            viewHolder.pbScanLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class OurServicesHolder extends RecyclerView.ViewHolder {
        private ImageView ivAppIcon;
        private TextView tvAppName;
        private ProgressBar pbScanLoading;

        public OurServicesHolder(@NonNull View itemView) {
            super(itemView);

            this.ivAppIcon = itemView.findViewById(R.id.rlScanApps_ivAppIcon);
            this.tvAppName = itemView.findViewById(R.id.rlScanApps_tvAppName);
            this.pbScanLoading = itemView.findViewById(R.id.rlScanApps_pbScanLoading);
        }
    }
}
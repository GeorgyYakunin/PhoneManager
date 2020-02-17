package com.fara.phonemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.an.deviceinfo.userapps.UserApps;
import com.fara.phonemanager.R;

import java.util.List;

public class UserAppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserApps> list;
    private UserApps model;

    public UserAppsAdapter(List<UserApps> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users_apps, parent, false);
        return new UserAppsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.model = this.list.get(position);

        UserAppsHolder viewHolder = (UserAppsHolder) holder;

        viewHolder.tvAppName.setText(this.model.getAppName());
        viewHolder.tvPackageName.setText(this.model.getPackageName());
        viewHolder.etVersionName.setText(this.model.getVersionName());
        viewHolder.etVersionCode.setText(String.valueOf(this.model.getVersionCode()));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class UserAppsHolder extends RecyclerView.ViewHolder {
        private TextView tvAppName;
        private TextView tvPackageName;
        private TextView etVersionName;
        private TextView etVersionCode;

        public UserAppsHolder(@NonNull View itemView) {
            super(itemView);

            this.tvAppName = itemView.findViewById(R.id.rlUserApps_tvAppName);
            this.tvPackageName = itemView.findViewById(R.id.rlUserApps_tvPackageName);
            this.etVersionName = itemView.findViewById(R.id.rlUserApps_etVersionName);
            this.etVersionCode = itemView.findViewById(R.id.rlUserApps_etVersionCode);
        }
    }
}
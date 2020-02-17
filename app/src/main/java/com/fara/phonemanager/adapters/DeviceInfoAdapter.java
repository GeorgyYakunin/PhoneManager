package com.fara.phonemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.phonemanager.R;
import com.fara.phonemanager.info.DeviceInfo;

import java.util.List;

public class DeviceInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DeviceInfo> list;
    private DeviceInfo model;

    public DeviceInfoAdapter(List<DeviceInfo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_about_device_info, parent, false);
        return new DeviceInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.model = this.list.get(position);

        DeviceInfoHolder viewHolder = (DeviceInfoHolder) holder;

        viewHolder.tvKey.setText(this.model.getKey());
        viewHolder.tvValue.setText(this.model.getValue());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class DeviceInfoHolder extends RecyclerView.ViewHolder {
        private TextView tvKey;
        private TextView tvValue;

        public DeviceInfoHolder(@NonNull View itemView) {
            super(itemView);

            this.tvKey = itemView.findViewById(R.id.rlDeviceInfo_tvKey);
            this.tvValue = itemView.findViewById(R.id.rlDeviceInfo_tvValue);
        }
    }
}
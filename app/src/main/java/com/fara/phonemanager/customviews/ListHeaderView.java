package com.fara.phonemanager.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fara.phonemanager.R;

public class ListHeaderView extends RelativeLayout {
    private Context mContext;
    public TextView mSize;
    public TextView mProgress;

    public ListHeaderView(Context context, ViewGroup listView) {
        super(context);
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.list_views_head, listView, false);
        addView(view);
        mSize = findViewById(R.id.lstHeaderView_tvTotalSize);
        mProgress = findViewById(R.id.lstHeaderView_tvProgressMessage);
    }
}
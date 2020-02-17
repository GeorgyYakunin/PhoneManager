package com.fara.phonemanager.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fara.phonemanager.BaseDialog;
import com.fara.phonemanager.R;
import com.fara.phonemanager.listeners.OnSingleClickListener;

public class MessageDialog extends BaseDialog {
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView btnConfirm;

    private String title;
    private String message;

    private OnClickListeners onClickListeners;
    public interface OnClickListeners {
        public abstract void onConfirm();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        View rootView = inflater.inflate(R.layout.dialog_messaging, container, false);

        getDataArguments();
        findViews(rootView);
        initComponents();
        setViewsListeners();

        return rootView;
    }

    @Override
    public void getDataArguments() {
        this.title = getArguments().getString("title");
        this.message = getArguments().getString("message");
    }

    @Override
    public void findViews(View rootView) {
        this.tvTitle = rootView.findViewById(R.id.dgMessage_tvTitle);
        this.tvMessage = rootView.findViewById(R.id.dgMessage_tvMessage);
        this.btnConfirm = rootView.findViewById(R.id.dgMessage_btnConfirm);
    }

    @Override
    public void initComponents() {
        this.tvTitle.setText(this.title);
        this.tvMessage.setText(this.message);
    }

    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    @Override
    public void setViewsListeners() {
        this.btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (onClickListeners != null)
                    onClickListeners.onConfirm();

                dismiss();
            }
        });
    }
}
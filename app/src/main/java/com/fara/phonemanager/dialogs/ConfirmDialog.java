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

public class ConfirmDialog extends BaseDialog {
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView btnConfirm;
    private TextView btnCancel;

    private String title;
    private String message;

    private OnClickListeners onClickListeners;
    public interface OnClickListeners {
        public abstract void onConfirm();
        public abstract void onCancel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        View rootView = inflater.inflate(R.layout.dialog_conf, container, false);

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
        this.tvTitle = rootView.findViewById(R.id.dgConfirm_tvTitle);
        this.tvMessage = rootView.findViewById(R.id.dgConfirm_tvMessage);
        this.btnConfirm = rootView.findViewById(R.id.dgConfirm_btnConfirm);
        this.btnCancel = rootView.findViewById(R.id.dgConfirm_btnCancel);
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

        this.btnCancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (onClickListeners != null)
                    onClickListeners.onCancel();

                dismiss();
            }
        });
    }
}
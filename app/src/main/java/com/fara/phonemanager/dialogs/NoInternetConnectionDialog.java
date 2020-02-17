package com.fara.phonemanager.dialogs;

import android.app.Dialog;
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

public class NoInternetConnectionDialog extends BaseDialog {
    private TextView btnClose;

    private OnClickListeners onClickListeners;
    public interface OnClickListeners {
        public abstract void onClose();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        View rootView = inflater.inflate(R.layout.dialog_noconnection, container, false);

        getDataArguments();
        findViews(rootView);
        initComponents();
        setViewsListeners();

        return rootView;
    }

    @Override
    public void getDataArguments() {

    }

    @Override
    public void findViews(View rootView) {
        this.btnClose = rootView.findViewById(R.id.dgNoInternetConnection_btnClose);
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setViewsListeners() {
        this.btnClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onClose();
                    dismiss();
                }
            }
        });
    }

    public void setOnClickListeners(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
package com.fara.phonemanager.customviews;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fara.phonemanager.R;

public class CustomSnackBar {
    private Activity activity;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View popupView;

    private int width;
    private int height;

    private RelativeLayout rlMainLayout;
    private TextView tvMessage;
    private TextView btnConfirm;

    public CustomSnackBar(Activity activity) {
        this.activity = activity;

        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.popupView = this.inflater.inflate(R.layout.pop_snack_bar, null);

        this.rlMainLayout = this.popupView.findViewById(R.id.popWindow_rlMainLayout);
        this.tvMessage = this.popupView.findViewById(R.id.popWindow_tvMessage);
        this.btnConfirm = this.popupView.findViewById(R.id.popWindow_btnConfirm);

        this.width = LinearLayout.LayoutParams.MATCH_PARENT;
        this.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        this.popupWindow = new PopupWindow(this.popupView, this.width, this.height, false);
        this.popupWindow.setAnimationStyle(R.style.popup_window_animation_slide_for_snack_bar);
    }

    public void showSnackBarInfo(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorInfo));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.GONE);

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 3000);
    }

    public void showSnackBarSuccess(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorSuccess));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.GONE);

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 3000);
    }

    public void showSnackBarWarning(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWarning));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.GONE);

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 3000);
    }

    public void showSnackBarError(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorError));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.GONE);

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
            }
        }, 3000);
    }

    public void showSnackBarActionInfo(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorInfo));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.VISIBLE);
        this.btnConfirm.setTextColor(ContextCompat.getColor(activity, R.color.cyan_100));
        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public void showSnackBarActionSuccess(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorSuccess));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.VISIBLE);
        this.btnConfirm.setTextColor(ContextCompat.getColor(activity, R.color.green_A100));
        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public void showSnackBarActionWarning(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWarning));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.VISIBLE);
        this.btnConfirm.setTextColor(ContextCompat.getColor(activity, R.color.amber_200));
        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public void showSnackBarActionError(View view, String message) {
        this.rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorError));
        this.tvMessage.setText(message);

        this.btnConfirm.setVisibility(View.VISIBLE);
        this.btnConfirm.setTextColor(ContextCompat.getColor(activity, R.color.red_100));
        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        this.popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
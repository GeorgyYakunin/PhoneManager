package com.fara.phonemanager.customviews;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fara.phonemanager.ActivityBasic;
import com.fara.phonemanager.R;

public class CustomAlert {
    private Activity activity;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View popupView;

    private int width;
    private int height;

    private RelativeLayout rlMainLayout;
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView btnConfirm;
    private TextView btnCancel;

    public interface OnClickListener {
        public abstract void onConfirm();
        public abstract void onCancel();
    }

    public CustomAlert(Activity activity) {
        this.activity = activity;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.pop_alert, null);

        rlMainLayout = popupView.findViewById(R.id.popWindowAlert_rlMainLayout);
        tvTitle = popupView.findViewById(R.id.popWindowAlert_tvTitle);
        tvMessage = popupView.findViewById(R.id.popWindowAlert_tvMessage);
        btnConfirm = popupView.findViewById(R.id.popWindowAlert_btnConfirm);
        btnCancel = popupView.findViewById(R.id.popWindowAlert_btnCancel);

        width = LinearLayout.LayoutParams.MATCH_PARENT;
        height = LinearLayout.LayoutParams.WRAP_CONTENT;

        popupWindow = new PopupWindow(popupView, width, height, false);
        popupWindow.setAnimationStyle(R.style.popup_window_animation_slide_for_alert);
    }

    public void showAlertMessageInfo(View view, String title, String message) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorInfo));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.GONE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorInfo));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    public void showAlertMessageSuccess(View view, String title, String message) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorSuccess));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.GONE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorSuccess));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    public void showAlertMessageWarning(View view, String title, String message) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWarning));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.GONE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorWarning));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    public void showAlertMessageError(View view, String title, String message) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorError));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.GONE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorError));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    public void showAlertConfirmInfo(View view, String title, String message, final OnClickListener onClickListener) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorInfo));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.VISIBLE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorInfo));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onConfirm();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onCancel();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    public void showAlertConfirmSuccess(View view, String title, String message, final OnClickListener onClickListener) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorSuccess));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.VISIBLE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorSuccess));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onConfirm();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onCancel();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    public void showAlertConfirmWarning(View view, String title, String message, final OnClickListener onClickListener) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWarning));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.VISIBLE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorWarning));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onConfirm();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onCancel();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    public void showAlertConfirmError(View view, String title, String message, final OnClickListener onClickListener) {
        rlMainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorError));

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnCancel.setVisibility(View.VISIBLE);

        setNewNavigationBarBackgroundColor(ContextCompat.getColor(activity, R.color.colorError));

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onConfirm();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onCancel();
                popupWindow.dismiss();

                backToPreviousNavigationBarBackgroundColor();
            }
        });
    }

    private void setNewNavigationBarBackgroundColor(final int color) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ActivityBasic) activity).setStatusBarColor(color);
            }
        }, 200);
    }

    private void backToPreviousNavigationBarBackgroundColor() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ActivityBasic) activity).setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            }
        }, 200);
    }
}
package com.fara.phonemanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fara.phonemanager.customviews.CustomAlert;
import com.fara.phonemanager.customviews.CustomSnackBar;
import com.fara.phonemanager.customviews.CustomToast;
import com.fara.phonemanager.dialogs.MessageDialog;

/*
* It is an abstract class that all activities in this project inherit from this class. We use base-activity
* to control routine and repetitive tasks so that the application does not need to be re-implemented
* in different parts of the application.
*
* Totally, in all activities we have 3 processes. First one is to find the views that has been
* designed in Layout, second one is view and component initialization and last one is setting view
* listeners. For these categories, we define 3 abstract methods that implement in all activities that
* inherit base activity.
* */

public abstract class ActivityBasic extends AppCompatActivity {
    public abstract void findViews();
    public abstract void initComponents();
    public abstract void setViewsListeners();

    private View view;
    private ProgressDialog pDialog;
    private CustomSnackBar customSnackbar;
    private CustomAlert customAlert;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customSnackbar = new CustomSnackBar(ActivityBasic.this);
        customAlert = new CustomAlert(ActivityBasic.this);

        initStrictModeThreadPolicy();
//        hideStatusBar();
//        hideActionBar();
        initNavigationBarColor();
        initStatusBarColor();
        initDialogProgress();
    }

    private void initStrictModeThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void hideActionBar() {
        getSupportActionBar().hide();
    }

    // if phone is running Android API version above Lollipop set navigation bar color
    private void initNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    // if phone is running Android API version above Lollipop set status bar color
    private void initStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    // if phone is running Android API version above Lollipop
    // set status bar color
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    public void setNavigationBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
        }
    }

    //----- Keyboard

    public void hideKeyboard() {
//        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showKeyboard() {
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void showKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    // ----- Progress dialog

    private void initDialogProgress() {
        view = getLayoutInflater().inflate(R.layout.dialog_progressing, null);
        TextView txt = (TextView) view.findViewById(R.id.txt);
        txt.setText(getResources().getString(R.string.activity_base_dialog_progress_content));
        pDialog = new ProgressDialog(ActivityBasic.this);
        pDialog.setCancelable(false);
    }

    public void showDialogProgress() {
        runOnUiThread(() -> {
            if (!isFinishing()) {
                pDialog.show();
                pDialog.setContentView(view);
            }
        });
    }

    public void showDialogProgress(final String message) {
        runOnUiThread(() -> {
            view = getLayoutInflater().inflate(R.layout.dialog_progressing, null);
            TextView txt = (TextView) view.findViewById(R.id.txt);
            txt.setText(message);
            showDialogProgress();
        });
    }

    public void dismissDialogProgress() {
        runOnUiThread(() -> {
            if (!isFinishing()) {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
            }
        });
    }

    // ----- Message dialog

    public void showMessageDialog(String message) {
        showMessageDialog(getString(R.string.app_name), message);
    }

    public void showMessageDialog(String title, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);

        final MessageDialog messageDialog = new MessageDialog();
        messageDialog.setArguments(bundle);
        messageDialog.show(getSupportFragmentManager(), "Message Dialog");
    }

    // ----- Toast

    public void showToastInfo(final String message) {
        runOnUiThread(() -> CustomToast.makeText(ActivityBasic.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_INFO));
    }

    public void showToastSuccess(final String message) {
        runOnUiThread(() -> CustomToast.makeText(ActivityBasic.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_SUCCESS));
    }

    public void showToastWarning(final String message) {
        runOnUiThread(() -> CustomToast.makeText(ActivityBasic.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_WARNING));
    }

    public void showToastError(final String message) {
        runOnUiThread(() -> CustomToast.makeText(ActivityBasic.this, message, CustomToast.LENGTH_LONG, CustomToast.TYPE_ERROR));
    }

    // ----- SnackBar

    public void showSnackBarInfo(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarInfo(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    public void showSnackBarSuccess(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarSuccess(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    public void showSnackBarWarning(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarWarning(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    public void showSnackBarError(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarError(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    public void showSnackBarActionInfo(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarActionInfo(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    public void showSnackBarActionSuccess(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarActionSuccess(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    public void showSnackBarActionWarning(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarActionWarning(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    public void showSnackBarActionError(final String message) {
        runOnUiThread(() -> customSnackbar.showSnackBarActionError(getWindow().getDecorView().findViewById(android.R.id.content), message));
    }

    // ----- Alert

    public void showAlertMessageInfo(final String message) {
        showAlertMessageInfo(getResources().getString(R.string.app_name), message);
    }

    public void showAlertMessageInfo(final String title, final String message) {
        runOnUiThread(() -> customAlert.showAlertMessageInfo(getWindow().getDecorView().findViewById(android.R.id.content), title, message));
    }

    public void showAlertMessageSuccess(final String message) {
        showAlertMessageSuccess(getResources().getString(R.string.app_name), message);
    }

    public void showAlertMessageSuccess(final String title, final String message) {
        runOnUiThread(() -> customAlert.showAlertMessageSuccess(getWindow().getDecorView().findViewById(android.R.id.content), title, message));
    }

    public void showAlertMessageWarning(final String message) {
        showAlertMessageWarning(getResources().getString(R.string.app_name), message);
    }

    public void showAlertMessageWarning(final String title, final String message) {
        runOnUiThread(() -> customAlert.showAlertMessageWarning(getWindow().getDecorView().findViewById(android.R.id.content), title, message));
    }

    public void showAlertMessageError(final String message) {
        showAlertMessageError(getResources().getString(R.string.app_name), message);
    }

    public void showAlertMessageError(final String title, final String message) {
        runOnUiThread(() -> customAlert.showAlertMessageError(getWindow().getDecorView().findViewById(android.R.id.content), title, message));
    }

    public void showAlertConfirmInfo(final String message, final CustomAlert.OnClickListener onClickListener) {
        showAlertConfirmInfo(getResources().getString(R.string.app_name), message, onClickListener);
    }

    public void showAlertConfirmInfo(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        runOnUiThread(() -> customAlert.showAlertConfirmInfo(getWindow().getDecorView().findViewById(android.R.id.content), title, message, onClickListener));
    }

    public void showAlertConfirmSuccess(final String message, final CustomAlert.OnClickListener onClickListener) {
        showAlertConfirmSuccess(getResources().getString(R.string.app_name), message, onClickListener);
    }

    public void showAlertConfirmSuccess(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        runOnUiThread(() -> customAlert.showAlertConfirmSuccess(getWindow().getDecorView().findViewById(android.R.id.content), title, message, onClickListener));
    }

    public void showAlertConfirmWarning(final String message, final CustomAlert.OnClickListener onClickListener) {
        showAlertConfirmWarning(getResources().getString(R.string.app_name), message, onClickListener);
    }

    public void showAlertConfirmWarning(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        runOnUiThread(() -> customAlert.showAlertConfirmWarning(getWindow().getDecorView().findViewById(android.R.id.content), title, message, onClickListener));
    }

    public void showAlertConfirmError(final String message, final CustomAlert.OnClickListener onClickListener) {
        showAlertConfirmError(getResources().getString(R.string.app_name), message, onClickListener);
    }

    public void showAlertConfirmError(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        runOnUiThread(() -> customAlert.showAlertConfirmError(getWindow().getDecorView().findViewById(android.R.id.content), title, message, onClickListener));
    }
}
package com.fara.phonemanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fara.phonemanager.customviews.CustomAlert;
import com.fara.phonemanager.customviews.CustomSnackBar;
import com.fara.phonemanager.customviews.CustomToast;
import com.fara.phonemanager.dialogs.MessageDialog;

/*
 * See ActivityBasic
 *
 * Totally, in all fragments we have 4 processes. First one is to getting data arguments that has been
 * pass it from activity or fragment second one is to find the views that has been
 * designed in Layout, third one is view and component initialization and last one is setting view
 * listeners. For these categories, we define 4 abstract methods that implement in all fragments that
 * inherit base fragment.
 * */

public abstract class BaseFragment extends Fragment {
    public abstract void getArgument();
    public abstract void findViews();
    public abstract void initComponents();
    public abstract void setViewsListeners();

    private View view;
    private ProgressDialog pDialog;
    private CustomSnackBar customSnackbar;
    private CustomAlert customAlert;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        customSnackbar = new CustomSnackBar(getActivity());
        customAlert = new CustomAlert(getActivity());
    }

    //----- Keyboard

    public void hideKeyboard() {
        ((ActivityBasic) getActivity()).hideKeyboard();
    }

    public void showKeyboard() {
        ((ActivityBasic) getActivity()).showKeyboard();
    }

    public void showKeyboard(Context context, EditText editText) {
        ((ActivityBasic) getActivity()).showKeyboard(context, editText);
    }

    public void hideKeyboard(Context context, EditText editText) {
        ((ActivityBasic) getActivity()).hideKeyboard(context, editText);
    }

    // ----- Progress dialog

    public void showDialogProgress() {
        ((ActivityBasic) getActivity()).showDialogProgress();
    }

    public void showDialogProgress(final String message) {
        ((ActivityBasic) getActivity()).showDialogProgress(message);
    }

    public void dismissDialogProgress() {
        ((ActivityBasic) getActivity()).dismissDialogProgress();
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
        messageDialog.show(getChildFragmentManager(), "Message Dialog");
    }

    // ----- Toast

    public void showToastInfo(final String message) {
        getActivity().runOnUiThread(() -> CustomToast.makeText(getActivity(), message, CustomToast.LENGTH_LONG, CustomToast.TYPE_INFO));
    }

    public void showToastSuccess(final String message) {
        getActivity().runOnUiThread(() -> CustomToast.makeText(getActivity(), message, CustomToast.LENGTH_LONG, CustomToast.TYPE_SUCCESS));
    }

    public void showToastWarning(final String message) {
        getActivity().runOnUiThread(() -> CustomToast.makeText(getActivity(), message, CustomToast.LENGTH_LONG, CustomToast.TYPE_WARNING));
    }

    public void showToastError(final String message) {
        getActivity().runOnUiThread(() -> CustomToast.makeText(getActivity(), message, CustomToast.LENGTH_LONG, CustomToast.TYPE_ERROR));
    }

    // ----- SnackBar

    public void showSnackbarInfo(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarInfo(message);
    }

    public void showSnackBarSuccess(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarSuccess(message);
    }

    public void showSnackBarWarning(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarWarning(message);
    }

    public void showSnackBarError(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarError(message);
    }

    public void showSnackBarActionInfo(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarActionInfo(message);
    }

    public void showSnackBarActionSuccess(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarActionSuccess(message);
    }

    public void showSnackBarActionWarning(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarActionWarning(message);
    }

    public void showSnackBarActionError(final String message) {
        ((ActivityBasic) getActivity()).showSnackBarActionError(message);
    }

    // ----- Alert

    public void showAlertMessageInfo(final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageInfo(message);
    }

    public void showAlertMessageInfo(final String title, final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageInfo(title, message);
    }

    public void showAlertMessageSuccess(final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageSuccess(message);
    }

    public void showAlertMessageSuccess(final String title, final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageSuccess(title, message);
    }

    public void showAlertMessageWarning(final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageWarning(message);
    }

    public void showAlertMessageWarning(final String title, final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageWarning(title, message);
    }

    public void showAlertMessageError(final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageError(message);
    }

    public void showAlertMessageError(final String title, final String message) {
        ((ActivityBasic) getActivity()).showAlertMessageError(title, message);
    }

    public void showAlertConfirmInfo(final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmInfo(message, onClickListener);
    }

    public void showAlertConfirmInfo(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmInfo(title, message, onClickListener);
    }

    public void showAlertConfirmSuccess(final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmSuccess(message, onClickListener);
    }

    public void showAlertConfirmSuccess(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmSuccess(title, message, onClickListener);
    }

    public void showAlertConfirmWarning(final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmWarning(message, onClickListener);
    }

    public void showAlertConfirmWarning(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmWarning(title, message, onClickListener);
    }

    public void showAlertConfirmError(final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmError(message, onClickListener);
    }

    public void showAlertConfirmError(final String title, final String message, final CustomAlert.OnClickListener onClickListener) {
        ((ActivityBasic) getActivity()).showAlertConfirmError(title, message, onClickListener);
    }
}

package com.fara.phonemanager;

import android.view.View;

import androidx.fragment.app.DialogFragment;

/*
 * See ActivityBasic
 *
 * Totally, in all dialogs we have 4 processes. First one is to getting data arguments that has been
 * pass it from activity or fragment second one is to find the views that has been
 * designed in Layout, third one is view and component initialization and last one is setting view
 * listeners. For these categories, we define 4 abstract methods that implement in all dialogs that
 * inherit base dialog.
 * */

public abstract class BaseDialog extends DialogFragment {
    public abstract void getDataArguments();
    public abstract void findViews(View rootView);
    public abstract void initComponents();
    public abstract void setViewsListeners();
}
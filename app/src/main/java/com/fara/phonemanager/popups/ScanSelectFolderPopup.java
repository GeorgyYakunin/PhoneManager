package com.fara.phonemanager.popups;

import android.app.Activity;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.phonemanager.AppController;
import com.fara.phonemanager.R;
import com.fara.phonemanager.adapters.ScanSelectFolderAdapter;
import com.fara.phonemanager.info.ScanSelectFolderInfo;
import com.fara.phonemanager.listeners.OnSingleClickListener;
import com.fara.phonemanager.utils.GlobalUtils;
import com.fara.phonemanager.utils.SdCardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * We'll send the cached root path to this pop-up, displaying subfolders and files, and displaying
 * sub folders when the folder is clicked. This process is repeated until the user clicks the confirmation
 * button and then sends the selected folder path to the ScanSelectActivity.
 * */

public class ScanSelectFolderPopup {
    private Activity activity;

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View popupView;

    private int width;
    private int height;

    private RecyclerView rvFolders;
    private TextView btnSelect;

    private ScanSelectFolderAdapter adapter;
    private List<ScanSelectFolderInfo> list = new ArrayList<>();

    private String[] folder;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public abstract void onClick(String selectedFolderPath);
    }

    public ScanSelectFolderPopup(Activity activity) {
        this.activity = activity;

        initPopup();
        findViews();
        initComponents();
        setViewsListeners();
    }

    private void initPopup() {
        this.inflater = (LayoutInflater) this.activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        this.popupView = this.inflater.inflate(R.layout.pop_scaning_selected_folder, null);

        this.width = LinearLayout.LayoutParams.MATCH_PARENT;
        this.height = GlobalUtils.getHeightPercent(activity, 50);

        this.popupWindow = new PopupWindow(this.popupView, this.width, this.height, false);
        this.popupWindow.setFocusable(true);
        this.popupWindow.update();

        // Closes the popup window when touch outside.
        this.popupWindow.setOutsideTouchable(false);
        this.popupWindow.setFocusable(false);
    }

    private void findViews() {
        this.rvFolders = this.popupView.findViewById(R.id.puScanSelectFolder_rvFolders);
        this.btnSelect = this.popupView.findViewById(R.id.puScanSelectFolder_btnSelect);
    }

    private void initComponents() {
        initRecycleViewServices();
        dataToPopulateRecyclerViewWithStorage();
    }

    private void setViewsListeners() {
        this.btnSelect.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onClick(adapter.getRootFolder());
            }
        });
    }

    private void initRecycleViewServices() {
        this.rvFolders.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        this.rvFolders.setLayoutManager(mLayoutManager);
        this.rvFolders.setItemAnimator(new DefaultItemAnimator());

        this.adapter = new ScanSelectFolderAdapter(this.list);
        this.rvFolders.setAdapter(adapter);
    }

    private void dataToPopulateRecyclerViewWithStorage() {
        if (new SdCardUtils().hasStorage(AppController.getContext())) {
            File[] storages = new SdCardUtils().getStorages();
            for (File storage : storages) {
                if (!storage.getAbsolutePath().contains("self")) {
                    if (storage.getAbsolutePath().contains("emulated")) {
                        this.list.add(new ScanSelectFolderInfo(true, "storage", Environment.getExternalStorageDirectory().getAbsolutePath(), false));
                    } else {
                        this.list.add(new ScanSelectFolderInfo(true, "storage", storage.getAbsolutePath(), false));
                    }
                }
            }

            this.adapter.setPrevFolder("storage");
            this.adapter.setRootFolder("storage");
            this.adapter.notifyDataSetChanged();
        } else {
            dataToPopulateRecyclerViewWithDirectories(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
    }

    private void dataToPopulateRecyclerViewWithDirectories(String path) {
        File[] listFolders = new File(path).listFiles();

        this.folder = new String[listFolders.length];
        for (int i = 0; i < listFolders.length; i++) {
            if (listFolders[i].isDirectory()) {
                this.folder[i] = listFolders[i].getAbsolutePath();
                this.list.add(new ScanSelectFolderInfo(false, path, this.folder[i], true));
            } else {
                this.folder[i] = listFolders[i].getAbsolutePath();
                this.list.add(new ScanSelectFolderInfo(false, path, this.folder[i], false));
            }
        }

        Collections.sort(this.list, (abc1, abc2) -> Boolean.compare(abc2.isDirectory(), abc1.isDirectory()));

        this.adapter.setPrevFolder(path);
        this.adapter.setRootFolder(path);
        this.adapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void show(final View anchor) {
        this.popupWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, -2);
    }

    public void dismiss() {
        this.popupWindow.dismiss();
    }

    public boolean isShowing() {
        return this.popupWindow.isShowing();
    }
}
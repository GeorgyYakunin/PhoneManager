package com.fara.phonemanager.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.fara.phonemanager.ActivityBasic;
import com.fara.phonemanager.R;
import com.fara.phonemanager.SessionManager;
import com.fara.phonemanager.adapters.holders.ChildViewHolder;
import com.fara.phonemanager.adapters.holders.GroupViewHolder;
import com.fara.phonemanager.callbacks.IScanCallback;
import com.fara.phonemanager.customviews.ListHeaderView;
import com.fara.phonemanager.info.JunkGroup;
import com.fara.phonemanager.info.JunkInfo;
import com.fara.phonemanager.tasks.OverallScanTask;
import com.fara.phonemanager.tasks.ProcessScanTask;
import com.fara.phonemanager.tasks.SysCacheScanTask;
import com.fara.phonemanager.utils.junkclean.CleanUtil;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class ActivityBasicGarbageClean extends ActivityBasic {
    public static final int MSG_SYS_CACHE_BEGIN = 0x1001;
    public static final int MSG_SYS_CACHE_POS = 0x1002;
    public static final int MSG_SYS_CACHE_FINISH = 0x1003;

    public static final int MSG_PROCESS_BEGIN = 0x1011;
    public static final int MSG_PROCESS_POS = 0x1012;
    public static final int MSG_PROCESS_FINISH = 0x1013;

    public static final int MSG_OVERALL_BEGIN = 0x1021;
    public static final int MSG_OVERALL_POS = 0x1022;
    public static final int MSG_OVERALL_FINISH = 0x1023;

    public static final int MSG_SYS_CACHE_CLEAN_FINISH = 0x1100;
    public static final int MSG_PROCESS_CLEAN_FINISH = 0x1101;
    public static final int MSG_OVERALL_CLEAN_FINISH = 0x1102;

    public static final String HANG_FLAG = "hanged";

    private Handler handler;

    private boolean mIsSysCacheScanFinish = false;
    private boolean mIsSysCacheCleanFinish = false;

    private boolean mIsProcessScanFinish = false;
    private boolean mIsProcessCleanFinish = false;

    private boolean mIsOverallScanFinish = false;
    private boolean mIsOverallCleanFinish = false;

    private boolean mIsScanning = false;

    private BaseExpandableListAdapter mAdapter;
    private HashMap<Integer, JunkGroup> mJunkGroups = null;

    private ListHeaderView mHeaderView;
    private ConstraintLayout clJunkCleanLayout;
    private ConstraintLayout clNoNeedScanLayout;
    private LinearLayout mCleanButton;
    private ExpandableListView listView;
    private ImageView ivNoNeedScan;

    /*
    * To frees up a lot of space and improve privacy by cleaning processes, memory, clear uninstalled applications
    * and log files and deleting temporary files.
    *
    * It is necessary to mention that this class has been copied from github
    * Link : https://github.com/mzlogin/CleanExpert
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_garbage_clean);

        if (SessionManager.getInstance(ActivityBasicGarbageClean.this).getLastJunkCleanTime() == null || SessionManager.getInstance(ActivityBasicGarbageClean.this).getLastJunkCleanTime().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            SessionManager.getInstance(ActivityBasicGarbageClean.this).setLastJunkCleanTime(dateFormat.format(cal.getTime()));
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date previousDate = formatter.parse(SessionManager.getInstance(ActivityBasicGarbageClean.this).getLastJunkCleanTime());
            Date currentDate = new Date();
            long difference = (currentDate.getTime() - previousDate.getTime()) / 1000;

            // There are two layout on junk clean activity. First layout is about cleaning up memory
            // and files and second layout is about when there is no cleaning.
            this.clJunkCleanLayout = findViewById(R.id.acJunkClean_clJunkCleanLayout);
            this.clNoNeedScanLayout = findViewById(R.id.acJunkClean_clNoNeedScanLayout);
            if (difference > 3600) {
                this.clJunkCleanLayout.setVisibility(View.VISIBLE);
                this.clNoNeedScanLayout.setVisibility(View.GONE);

                findViews();
                initComponents();
                setViewsListeners();
            } else {
                this.clJunkCleanLayout.setVisibility(View.GONE);
                this.clNoNeedScanLayout.setVisibility(View.VISIBLE);

                setStatusBarColor(Color.parseColor("#FF2e78e6"));
                setNavigationBarColor(Color.parseColor("#FF2e78e6"));

                // Show google admob's banner
                this.ivNoNeedScan = findViewById(R.id.acJunkClean_ivNoNeedScan);





                Glide.with(ActivityBasicGarbageClean.this).load(R.drawable.ic_junk_clean_animation).centerCrop().placeholder(R.drawable.ic_no_image_found).into(ivNoNeedScan);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void findViews() {
        this.mCleanButton = findViewById(R.id.acJunkClean_btnDoClean);
        this.listView = findViewById(R.id.acJunkClean_elvJunkList);
    }

    @Override
    public void initComponents() {
//        To change the color of status bar & navigation bar in accordance with slider background.
        setStatusBarColor(Color.parseColor("#FF2e78e6"));
        setNavigationBarColor(Color.parseColor("#FF2e78e6"));



        initMessageHandler();

        this.mCleanButton.setEnabled(false);

        resetState();

        initListView();

        if (!mIsScanning) {
            mIsScanning = true;
            startScan();
        }
    }

    @Override
    public void setViewsListeners() {
        this.mCleanButton.setOnClickListener(v -> {
            mCleanButton.setEnabled(false);
            clearAll();
        });

        this.listView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            JunkInfo info = (JunkInfo) mAdapter.getChild(groupPosition, childPosition);
            if (groupPosition == JunkGroup.GROUP_APK ||
                    info.mIsChild ||
                    (groupPosition == JunkGroup.GROUP_ADV && !info.mIsChild && info.mPath != null)) {
                if (info.mPath != null) {
                    Toast.makeText(ActivityBasicGarbageClean.this, info.mPath, Toast.LENGTH_SHORT).show();
                }
            } else {
                int childrenInThisGroup = mAdapter.getChildrenCount(groupPosition);
                for (int i = childPosition + 1; i < childrenInThisGroup; i++) {
                    JunkInfo child = (JunkInfo) mAdapter.getChild(groupPosition, i);
                    if (!child.mIsChild) {
                        break;
                    }

                    child.mIsVisible = !child.mIsVisible;
                }
                mAdapter.notifyDataSetChanged();
            }
            return false;
        });
    }





    // On Cleaning message handler.
    private void initMessageHandler() {
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_SYS_CACHE_BEGIN:
                        break;

                    case MSG_SYS_CACHE_POS:
                        mHeaderView.mProgress.setText(getResources().getString(R.string.junk_clean_list_header_view_scan) + " : " + ((JunkInfo) msg.obj).mPackageName);
                        mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(ActivityBasicGarbageClean.this, getTotalSize()));
                        break;

                    case MSG_SYS_CACHE_FINISH:
                        mIsSysCacheScanFinish = true;
                        checkScanFinish();
                        break;

                    case MSG_SYS_CACHE_CLEAN_FINISH:
                        mIsSysCacheCleanFinish = true;
                        checkCleanFinish();
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            boolean hanged = bundle.getBoolean(HANG_FLAG, false);
                            if (hanged) {
                                Toast.makeText(ActivityBasicGarbageClean.this, getResources().getString(R.string.junk_clean_system_cache_clean_finish), Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;

                    case MSG_PROCESS_BEGIN:
                        break;

                    case MSG_PROCESS_POS:
                        mHeaderView.mProgress.setText(getResources().getString(R.string.junk_clean_list_header_view_scan) + " :: " + ((JunkInfo) msg.obj).mPackageName);
                        mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(ActivityBasicGarbageClean.this, getTotalSize()));
                        break;

                    case MSG_PROCESS_FINISH:
                        mIsProcessScanFinish = true;
                        checkScanFinish();
                        break;

                    case MSG_PROCESS_CLEAN_FINISH:
                        mIsProcessCleanFinish = true;
                        checkCleanFinish();
                        break;

                    case MSG_OVERALL_BEGIN:
                        break;

                    case MSG_OVERALL_POS:
                        mHeaderView.mProgress.setText(getResources().getString(R.string.junk_clean_list_header_view_scan) + " :: " + ((JunkInfo) msg.obj).mPath);
                        mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(ActivityBasicGarbageClean.this, getTotalSize()));
                        break;

                    case MSG_OVERALL_FINISH:
                        mIsOverallScanFinish = true;
                        checkScanFinish();
                        break;

                    case MSG_OVERALL_CLEAN_FINISH:
                        mIsOverallCleanFinish = true;
                        checkCleanFinish();
                        break;
                }
            }
        };
    }

    // This is list view of tasks that app free up memory
    private void initListView() {
        this.mHeaderView = new ListHeaderView(this, this.listView);
        this.mHeaderView.mProgress.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        this.listView.addHeaderView(this.mHeaderView);
        this.listView.setGroupIndicator(null);
        this.listView.setChildIndicator(null);
        this.listView.setDividerHeight(0);

        this.mAdapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return mJunkGroups.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                if (mJunkGroups.get(groupPosition).mChildren != null) {
                    return mJunkGroups.get(groupPosition).mChildren.size();
                } else {
                    return 0;
                }
            }

            @Override
            public Object getGroup(int groupPosition) {
                return mJunkGroups.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return mJunkGroups.get(groupPosition).mChildren.get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return 0;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                GroupViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(ActivityBasicGarbageClean.this).inflate(R.layout.list_group_, null);
                    holder = new GroupViewHolder();
                    holder.mPackageNameTv = convertView.findViewById(R.id.lstGroup_tvPackageName);
                    holder.mPackageSizeTv = convertView.findViewById(R.id.lstGroup_tvPackageSize);
                    convertView.setTag(holder);
                } else {
                    holder = (GroupViewHolder) convertView.getTag();
                }

                JunkGroup group = mJunkGroups.get(groupPosition);
                holder.mPackageNameTv.setText(group.mName);
                holder.mPackageSizeTv.setText(CleanUtil.formatShortFileSize(ActivityBasicGarbageClean.this, group.mSize));

                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                JunkInfo info = mJunkGroups.get(groupPosition).mChildren.get(childPosition);

                if (info.mIsVisible) {
                    ChildViewHolder holder;

                    if (info.mIsChild) {
                        convertView = LayoutInflater.from(ActivityBasicGarbageClean.this).inflate(R.layout.items_list_level_2, null);
                    } else {
                        convertView = LayoutInflater.from(ActivityBasicGarbageClean.this).inflate(R.layout.items_list_level_1, null);
                    }

                    holder = new ChildViewHolder();
                    holder.mJunkTypeTv = convertView.findViewById(R.id.lstLevelItem_junkType);
                    holder.mJunkSizeTv = convertView.findViewById(R.id.lstLevelItem_junkSize);

                    holder.mJunkTypeTv.setText(info.name);
                    holder.mJunkSizeTv.setText(CleanUtil.formatShortFileSize(ActivityBasicGarbageClean.this, info.mSize));
                } else {
                    convertView = LayoutInflater.from(ActivityBasicGarbageClean.this).inflate(R.layout.nullable_item_, null);
                }

                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        };

        this.listView.setAdapter(this.mAdapter);
    }

    // Cleaning all tasks when user click on clear button
    private void clearAll() {
        Thread clearThread = new Thread(new Runnable() {
            @Override
            public void run() {
                JunkGroup processGroup = mJunkGroups.get(JunkGroup.GROUP_PROCESS);
                for (JunkInfo info : processGroup.mChildren) {
                    CleanUtil.killAppProcesses(info.mPackageName);
                }
                Message msg = handler.obtainMessage(ActivityBasicGarbageClean.MSG_PROCESS_CLEAN_FINISH);
                msg.sendToTarget();

                CleanUtil.freeAllAppsCache(handler);

                ArrayList<JunkInfo> junks = new ArrayList<>();
                JunkGroup group = mJunkGroups.get(JunkGroup.GROUP_APK);
                junks.addAll(group.mChildren);

                group = mJunkGroups.get(JunkGroup.GROUP_LOG);
                junks.addAll(group.mChildren);

                group = mJunkGroups.get(JunkGroup.GROUP_TMP);
                junks.addAll(group.mChildren);

                CleanUtil.freeJunkInfos(junks, handler);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 0);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                SessionManager.getInstance(ActivityBasicGarbageClean.this).setLastJunkCleanTime(dateFormat.format(cal.getTime()));


            }
        });
        clearThread.start();
    }

    // Back to default state
    private void resetState() {
        this.mIsScanning = false;

        this.mIsSysCacheScanFinish = false;
        this.mIsSysCacheCleanFinish = false;

        this.mIsProcessScanFinish = false;
        this.mIsProcessCleanFinish = false;

        this.mJunkGroups = new HashMap<>();

        this.mCleanButton.setEnabled(false);

        JunkGroup cacheGroup = new JunkGroup();
        cacheGroup.mName = getString(R.string.junk_clean_cache_clean);
        cacheGroup.mChildren = new ArrayList<>();
        this.mJunkGroups.put(JunkGroup.GROUP_CACHE, cacheGroup);

        JunkGroup processGroup = new JunkGroup();
        processGroup.mName = getString(R.string.junk_clean_process_clean);
        processGroup.mChildren = new ArrayList<>();
        this.mJunkGroups.put(JunkGroup.GROUP_PROCESS, processGroup);

        JunkGroup apkGroup = new JunkGroup();
        apkGroup.mName = getString(R.string.junk_clean_apk_clean);
        apkGroup.mChildren = new ArrayList<>();
        this.mJunkGroups.put(JunkGroup.GROUP_APK, apkGroup);

        JunkGroup tmpGroup = new JunkGroup();
        tmpGroup.mName = getString(R.string.junk_clean_tmp_clean);
        tmpGroup.mChildren = new ArrayList<>();
        this.mJunkGroups.put(JunkGroup.GROUP_TMP, tmpGroup);

        JunkGroup logGroup = new JunkGroup();
        logGroup.mName = getString(R.string.junk_clean_log_clean);
        logGroup.mChildren = new ArrayList<>();
        this.mJunkGroups.put(JunkGroup.GROUP_LOG, logGroup);
    }

    // Checking whether scan is finished or not
    private void checkScanFinish() {
        this.mAdapter.notifyDataSetChanged();

        if (this.mIsProcessScanFinish && this.mIsSysCacheScanFinish && this.mIsOverallScanFinish) {
            this.mIsScanning = false;

            JunkGroup cacheGroup = this.mJunkGroups.get(JunkGroup.GROUP_CACHE);
            ArrayList<JunkInfo> children = cacheGroup.mChildren;
            cacheGroup.mChildren = new ArrayList<>();
            for (JunkInfo info : children) {
                cacheGroup.mChildren.add(info);
                if (info.mChildren != null) {
                    cacheGroup.mChildren.addAll(info.mChildren);
                }
            }
            children = null;

            long size = getTotalSize();
            String totalSize = CleanUtil.formatShortFileSize(this, size);
            this.mHeaderView.mSize.setText(totalSize);
            this.mHeaderView.mProgress.setText(getResources().getString(R.string.junk_clean_total_size) + totalSize);
            this.mHeaderView.mProgress.setGravity(Gravity.CENTER);

            this.mCleanButton.setEnabled(true);
        }
    }

    private void checkCleanFinish() {
        if (this.mIsProcessCleanFinish && this.mIsSysCacheCleanFinish && this.mIsOverallCleanFinish) {
            this.mHeaderView.mProgress.setText(getResources().getString(R.string.junk_clean_progress_cleaned));
            this.mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(this, 0L));

            for (JunkGroup group : mJunkGroups.values()) {
                group.mSize = 0L;
                group.mChildren = null;
            }

            this.mAdapter.notifyDataSetChanged();
        }
    }

    // Start to scanning devices
    private void startScan() {
        ProcessScanTask processScanTask = new ProcessScanTask(new IScanCallback() {
            @Override
            public void onBegin() {
                Message msg = handler.obtainMessage(MSG_PROCESS_BEGIN);
                msg.sendToTarget();
            }

            @Override
            public void onProgress(JunkInfo info) {
                Message msg = handler.obtainMessage(MSG_PROCESS_POS);
                msg.obj = info;
                msg.sendToTarget();
            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                JunkGroup cacheGroup = mJunkGroups.get(JunkGroup.GROUP_PROCESS);
                cacheGroup.mChildren.addAll(children);
                for (JunkInfo info : children) {
                    cacheGroup.mSize += info.mSize;
                }
                Message msg = handler.obtainMessage(MSG_PROCESS_FINISH);
                msg.sendToTarget();
            }
        });
        processScanTask.execute();

        SysCacheScanTask sysCacheScanTask = new SysCacheScanTask(new IScanCallback() {
            @Override
            public void onBegin() {
                Message msg = handler.obtainMessage(MSG_SYS_CACHE_BEGIN);
                msg.sendToTarget();
            }

            @Override
            public void onProgress(JunkInfo info) {
                Message msg = handler.obtainMessage(MSG_SYS_CACHE_POS);
                msg.obj = info;
                msg.sendToTarget();
            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                JunkGroup cacheGroup = mJunkGroups.get(JunkGroup.GROUP_CACHE);
                cacheGroup.mChildren.addAll(children);
                Collections.sort(cacheGroup.mChildren);
                Collections.reverse(cacheGroup.mChildren);
                for (JunkInfo info : children) {
                    cacheGroup.mSize += info.mSize;
                }
                Message msg = handler.obtainMessage(MSG_SYS_CACHE_FINISH);
                msg.sendToTarget();
            }
        });
        sysCacheScanTask.execute();

        OverallScanTask overallScanTask = new OverallScanTask(new IScanCallback() {
            @Override
            public void onBegin() {
                Message msg = handler.obtainMessage(MSG_OVERALL_BEGIN);
                msg.sendToTarget();
            }

            @Override
            public void onProgress(JunkInfo info) {
                Message msg = handler.obtainMessage(MSG_OVERALL_POS);
                msg.obj = info;
                msg.sendToTarget();
            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                for (JunkInfo info : children) {
                    String path = info.mChildren.get(0).mPath;
                    int groupFlag = 0;
                    if (path.endsWith(".apk")) {
                        groupFlag = JunkGroup.GROUP_APK;
                    } else if (path.endsWith(".log")) {
                        groupFlag = JunkGroup.GROUP_LOG;
                    } else if (path.endsWith(".tmp") || path.endsWith(".temp")) {
                        groupFlag = JunkGroup.GROUP_TMP;
                    }

                    JunkGroup cacheGroup = mJunkGroups.get(groupFlag);
                    cacheGroup.mChildren.addAll(info.mChildren);
                    cacheGroup.mSize = info.mSize;
                }

                Message msg = handler.obtainMessage(MSG_OVERALL_FINISH);
                msg.sendToTarget();
            }
        });
        overallScanTask.execute();
    }

    // Getting total size of junks
    private long getTotalSize() {
        long size = 0L;
        for (JunkGroup group : mJunkGroups.values()) {
            size += group.mSize;
        }
        return size;
    }
}
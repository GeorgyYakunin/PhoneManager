package com.fara.phonemanager.adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fara.phonemanager.R;
import com.fara.phonemanager.info.ScanSelectFolderInfo;
import com.fara.phonemanager.listeners.OnSingleClickListener;
import com.fara.phonemanager.utils.SdCardUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class ScanSelectFolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    private List<ScanSelectFolderInfo> list;
    private ScanSelectFolderInfo model;

    private Vector<String> prevFolder = new Vector<>();
    private String rootFolder = "";

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        public abstract void onClick(String folderPath);
    }

    public ScanSelectFolderAdapter(List<ScanSelectFolderInfo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scan_selected_folders, parent, false);
        return new ScanSelectFolderHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.model = this.list.get(position);

        ScanSelectFolderHolder viewHolder = (ScanSelectFolderHolder) holder;

        if (this.model.isStorage()) {
            if (this.model.getFolderName().contains("emulated")) {
                viewHolder.tvFolderName.setText(context.getResources().getString(R.string.activity_scan_select_label_local_storage));
            } else {
                viewHolder.tvFolderName.setText(context.getResources().getString(R.string.activity_scan_select_label_external_storage));
            }
        } else {
            String[] folderNames = this.model.getFolderName().split("/");
            String folderName = folderNames[folderNames.length - 1];
            viewHolder.tvFolderName.setText(folderName);
        }

        if (this.model.isStorage()) {
            if (this.model.getFolderName().contains("emulated")) {
                Glide.with(this.context).load(R.drawable.ic_storage).centerCrop().placeholder(R.drawable.ic_no_image_found).into(viewHolder.ivFolderIcon);
            } else {
                Glide.with(this.context).load(R.drawable.ic_sd_card).centerCrop().placeholder(R.drawable.ic_no_image_found).into(viewHolder.ivFolderIcon);
            }
        } else {
            if (this.model.isDirectory())
                Glide.with(this.context).load(R.drawable.ic_folder).centerCrop().placeholder(R.drawable.ic_no_image_found).into(viewHolder.ivFolderIcon);
            else
                Glide.with(this.context).load(R.drawable.ic_file).centerCrop().placeholder(R.drawable.ic_no_image_found).into(viewHolder.ivFolderIcon);
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public String getPrevFolder() {
        return this.prevFolder.lastElement();
    }

    public void setPrevFolder(String prevFolder) {
        this.prevFolder.add(prevFolder);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ScanSelectFolderHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout clContainer;
        private ImageView ivFolderIcon;
        private TextView tvFolderName;

        public ScanSelectFolderHolder(@NonNull View itemView) {
            super(itemView);

            this.clContainer = itemView.findViewById(R.id.rlScanSelectFolder_clContainer);
            this.ivFolderIcon = itemView.findViewById(R.id.rlScanSelectFolder_ivFolderIcon);
            this.tvFolderName = itemView.findViewById(R.id.rlScanSelectFolder_tvFolderName);

            this.clContainer.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (list.get(getAdapterPosition()).getFolderName().equals("...")) {
                        addParentDirectoriesAndFilesToList();
                    } else {
                        if (list.get(getAdapterPosition()).isDirectory()) {
                            if (!getRootFolder().equals(getPrevFolder())) {
                                setPrevFolder(getRootFolder());
                            }

                            setRootFolder(list.get(getAdapterPosition()).getFolderName());
                            addChildDirectoriesAndFilesToList(getRootFolder(), getPrevFolder());
                        }
                    }
                }
            });
        }

        private void addParentDirectoriesAndFilesToList() {
            setRootFolder(getPrevFolder());

            if (getPrevFolder().equals("storage")) {
                prevFolder.removeAllElements();

                list.clear();

                File[] storages = new SdCardUtils().getStorages();
                for (File storage : storages) {
                    if (!storage.getAbsolutePath().contains("self")) {
                        if (storage.getAbsolutePath().contains("emulated")) {
                            list.add(new ScanSelectFolderInfo(true, "storage", Environment.getExternalStorageDirectory().getAbsolutePath(), false));
                        } else {
                            list.add(new ScanSelectFolderInfo(true, "storage", storage.getAbsolutePath(), false));
                        }
                    }
                }

                setPrevFolder("storage");
                setRootFolder("storage");
                notifyDataSetChanged();
            } else {
                prevFolder.remove(prevFolder.lastElement());

                File[] listFolders = new File(getRootFolder()).listFiles();

                list.clear();

                if (prevFolder.size() > 0)
                    list.add(new ScanSelectFolderInfo(false, getPrevFolder(), "...", true));
                else
                    setPrevFolder(getRootFolder());

                String[] folder = new String[listFolders.length];
                for (int i = 0; i < listFolders.length; i++) {
                    if (listFolders[i].isDirectory()) {
                        folder[i] = listFolders[i].getAbsolutePath();
                        list.add(new ScanSelectFolderInfo(false, null, folder[i], true));
                    } else {
                        folder[i] = listFolders[i].getAbsolutePath();
                        list.add(new ScanSelectFolderInfo(false, null, folder[i], false));
                    }
                }

                Collections.sort(list, (abc1, abc2) -> Boolean.compare(abc2.isDirectory(), abc1.isDirectory()));

                notifyDataSetChanged();
            }
        }

        private void addChildDirectoriesAndFilesToList(String nextPath, String prevPath) {
            File[] listFolders = new File(nextPath).listFiles();

            list.clear();

            list.add(new ScanSelectFolderInfo(false, prevPath, "...", true));

            String[] folder = new String[listFolders.length];
            for (int i = 0; i < listFolders.length; i++) {
                if (listFolders[i].isDirectory()) {
                    folder[i] = listFolders[i].getAbsolutePath();
                    list.add(new ScanSelectFolderInfo(false, prevPath, folder[i], true));
                } else {
                    folder[i] = listFolders[i].getAbsolutePath();
                    list.add(new ScanSelectFolderInfo(false, prevPath, folder[i], false));
                }
            }

            Collections.sort(list, (abc1, abc2) -> Boolean.compare(abc2.isDirectory(), abc1.isDirectory()));

//            setPrevFolder(prevPath);

            notifyDataSetChanged();
        }
    }
}
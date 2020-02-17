package com.fara.phonemanager.info;

public class ScanSelectFolderInfo {
    private boolean isStorage;
    private String rootFolder;
    private String folderName;
    private boolean isDirectory;

    public ScanSelectFolderInfo() {
    }

    public ScanSelectFolderInfo(boolean isStorage, String rootFolder, String folderName, boolean isDirectory) {
        this.isStorage = isStorage;
        this.rootFolder = rootFolder;
        this.folderName = folderName;
        this.isDirectory = isDirectory;
    }

    public boolean isStorage() {
        return isStorage;
    }

    public void setStorage(boolean storage) {
        isStorage = storage;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}
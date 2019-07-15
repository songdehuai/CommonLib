package com.songdehuai.commonlib.utils.imagepicker;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageItem implements Parcelable {

    private String filePath;
    private String fileName;
    private int countSize;
    private boolean isCheck;


    public ImageItem() {
    }

    public ImageItem(String filePath, String fileName, int countSize) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.countSize = countSize;
    }

    public ImageItem(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public ImageItem(String filePath) {
        this.filePath = filePath;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getCountSize() {
        return countSize;
    }

    public void setCountSize(int countSize) {
        this.countSize = countSize;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filePath);
        dest.writeString(this.fileName);
        dest.writeInt(this.countSize);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
    }

    protected ImageItem(Parcel in) {
        this.filePath = in.readString();
        this.fileName = in.readString();
        this.countSize = in.readInt();
        this.isCheck = in.readByte() != 0;
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}

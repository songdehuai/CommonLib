package com.songdehuai.commonlib.utils.imagepicker;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.songdehuai.commonlib.R;
import com.songdehuai.commonlib.base.BaseActivity;
import com.songdehuai.commonlib.task.AbsTask;
import com.songdehuai.commonlib.task.Task;
import com.songdehuai.commonlib.utils.grantor.PermissionListener;
import com.songdehuai.commonlib.utils.grantor.PermissionsUtil;
import com.songdehuai.commonlib.utils.imagepicker.adapter.MultImageReAdapter;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 多图选择
 */
public class MultImagePickerActivity extends BaseActivity {

    private CopyOnWriteArrayList<ImageItem> imageItems = new CopyOnWriteArrayList<>();
    private RecyclerView imageRv;
    private MultImageReAdapter multMapAdapter;
    private int max = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multmap, "选择图片", "确定(0)");
        initViews();
    }

    @Override
    public void onPublish() {
        super.onPublish();
        if (multMapAdapter.getSelectImages().size() > 0) {
            ImagePicker.INSTANCE.onImageSuccess(new ArrayList<>(multMapAdapter.getSelectImages()));
        }
        finish();
    }

    private void initViews() {
        if (getIntent().hasExtra("max")) {
            max = getIntent().getIntExtra("max", 0);
        }
        imageRv = findViewById(R.id.image_rv);
        multMapAdapter = new MultImageReAdapter(thisActivity);
        multMapAdapter.setMax(max);
        imageRv.setLayoutManager(new GridLayoutManager(this, 3));
        imageRv.setAdapter(multMapAdapter);
        multMapAdapter.setOnSelectImageListener((isChecked, selectImages) -> {
                    getTitleView().setPublishText("确定 (" + selectImages.size() + ")");
                }
        );
        PermissionsUtil.requestPermission(thisActivity, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                getLocalImages();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    /**
     * 获取本地图片
     */
    private void getLocalImages() {
        Task.task().start(new AbsTask<CopyOnWriteArrayList<ImageItem>>() {
            @Override
            protected CopyOnWriteArrayList<ImageItem> doBackground() {
                HashMap<String, List<ImageItem>> allPhotosTemp = new HashMap<>();//所有照片
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String[] projImage = {MediaStore.Images.Media._ID
                        , MediaStore.Images.Media.DATA
                        , MediaStore.Images.Media.SIZE
                        , MediaStore.Images.Media.DISPLAY_NAME};
                Cursor mCursor = getContentResolver().query(mImageUri,
                        projImage,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");

                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024;
                        String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        //用于展示相册初始化界面
                        imageItems.add(new ImageItem(path, displayName, size));
                        // 获取该图片的父路径名
                        String dirPath = new File(path).getParentFile().getAbsolutePath();
                        //存储对应关系
                        if (allPhotosTemp.containsKey(dirPath)) {
                            List<ImageItem> data = allPhotosTemp.get(dirPath);
                            data.add(new ImageItem(path, displayName, size));
                            continue;
                        } else {
                            List<ImageItem> data = new ArrayList<>();
                            data.add(new ImageItem(path, displayName, size));
                            allPhotosTemp.put(dirPath, data);
                        }
                    }
                    mCursor.close();
                }
                return imageItems;
            }

            @Override
            protected void onSuccess(CopyOnWriteArrayList<ImageItem> result) {
                multMapAdapter.setImageItemList(result);
            }

            @Override
            protected void onError(Throwable ex, boolean isCallbackError) {
                ex.printStackTrace();
            }
        });
    }


}

package com.commonlib.utils.imagepicker

import android.Manifest
import android.os.Bundle
import android.provider.MediaStore

import com.commonlib.R
import com.commonlib.base.BaseActivity
import com.commonlib.utils.imagepicker.adapter.MultImageReAdapter
import com.commonlib.utils.task.AbsTask
import com.commonlib.utils.task.Task

import java.io.File
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.CopyOnWriteArrayList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commonlib.ext.permission.request

/**
 * 多图选择
 */
class MultImagePickerActivity : BaseActivity() {

    private val imageItems = CopyOnWriteArrayList<ImageItem>()
    private var imageRv: RecyclerView? = null
    private var multMapAdapter: MultImageReAdapter? = null
    private var max = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multmap, "选择图片", "确定(0)")
        initViews()
    }

    override fun onPublish() {
        super.onPublish()
        if (multMapAdapter!!.selectImages.size > 0) {
            ImagePicker.onImageSuccess(ArrayList(multMapAdapter!!.selectImages))
        }
        finish()
    }

    private fun initViews() {
        if (intent.hasExtra("max")) {
            max = intent.getIntExtra("max", 0)
        }
        imageRv = findViewById(R.id.image_rv)
        multMapAdapter = MultImageReAdapter(thisActivity)
        multMapAdapter!!.setMax(max)
        imageRv!!.layoutManager = GridLayoutManager(this, 3)
        imageRv!!.adapter = multMapAdapter
        multMapAdapter!!.setOnSelectImageListener { isChecked, selectImages ->
            titleView!!.setPublishText(
                "确定 (" + selectImages.size + ")"
            )
        }
        request(Manifest.permission.READ_EXTERNAL_STORAGE) {
            onGranted {
                getLocalImages()
            }
            onShowRationale { it.retry() }
        }
    }

    /**
     * 获取本地图片
     */
    private fun getLocalImages() {
        Task.task().start(object : AbsTask<CopyOnWriteArrayList<ImageItem>>() {
            override fun doBackground(): CopyOnWriteArrayList<ImageItem> {
                val allPhotosTemp = HashMap<String, List<ImageItem>>()//所有照片
                val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val projImage = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DISPLAY_NAME
                )
                val mCursor = contentResolver.query(
                    mImageUri,
                    projImage,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    arrayOf("image/jpeg", "image/png"),
                    MediaStore.Images.Media.DATE_MODIFIED + " desc"
                )

                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        val path =
                            mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                        val size =
                            mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024
                        val displayName =
                            mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                        //用于展示相册初始化界面
                        imageItems.add(ImageItem(path, displayName, size))
                        // 获取该图片的父路径名
                        val dirPath = File(path).parentFile.absolutePath
                        //存储对应关系
                        if (allPhotosTemp.containsKey(dirPath)) {
                            val data = allPhotosTemp[dirPath]?.toMutableList()
                            data?.add(ImageItem(path, displayName, size))
                            continue
                        } else {
                            val data = ArrayList<ImageItem>()
                            data.add(ImageItem(path, displayName, size))
                            allPhotosTemp[dirPath] = data
                        }
                    }
                    mCursor.close()
                }
                return imageItems
            }

            override fun onSuccess(result: CopyOnWriteArrayList<ImageItem>) {
                multMapAdapter!!.setImageItemList(result)
            }

            override fun onError(ex: Throwable, isCallbackError: Boolean) {
                ex.printStackTrace()
            }
        })
    }


}

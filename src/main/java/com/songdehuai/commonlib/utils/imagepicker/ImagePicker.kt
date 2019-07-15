package com.songdehuai.commonlib.utils.imagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent

import androidx.appcompat.app.AlertDialog
import java.util.ArrayList
import java.util.LinkedHashSet

/**
 * 系统原生图片选择器
 */
object ImagePicker {

    private var imagePickerCallBack: ImagePickerCallBack? = null

    /**
     * 多种图片选择器
     *
     * @param activity
     * @param callBack
     */
    fun showImagePickerDialog(activity: Activity, callBack: ImagePickerCallBack) {
        val items = arrayOf("相册", "相机")
        AlertDialog.Builder(activity).setItems(items) { _, which ->
            when (which) {
                0 -> startImagePicker(activity, callBack)
                1 -> startCameraPicker(activity, callBack)
            }
        }.show()
    }

    /**
     * 启动相机
     *
     * @param context
     * @param imagePickerCallBack
     */
    fun startCameraPicker(context: Context, imagePickerCallBack: ImagePickerCallBack) {
        ImagePicker.imagePickerCallBack = imagePickerCallBack
        val intent = Intent(context, CameraPickerCalBackActivity::class.java)
        intent.putExtra("isCamera", true)
        context.startActivity(intent)
    }

    /**
     * 启动多图选择
     *
     * @param context
     * @param callBack
     */
    fun startMultiImagePicker(context: Context, max: Int, callBack: ImagePickerCallBack) {
        ImagePicker.imagePickerCallBack = callBack
        val intent = Intent(context, MultImagePickerActivity::class.java)
        intent.putExtra("max", max)
        context.startActivity(intent)
    }


    /**
     * 启动相册
     *
     * @param context
     * @param imagePickerCallBack
     */
    fun startImagePicker(context: Context, imagePickerCallBack: ImagePickerCallBack) {
        ImagePicker.imagePickerCallBack = imagePickerCallBack
        val intent = Intent(context, CameraPickerCalBackActivity::class.java)
        intent.putExtra("isCamera", false)
        context.startActivity(intent)
    }

    fun onImageSuccess(imageItemList: ArrayList<ImageItem>) {
        if (imagePickerCallBack != null) {
            imagePickerCallBack!!.onGetImage(imageItemList)
        }
    }


}

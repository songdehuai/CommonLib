package com.commonlib.utils.imagepicker


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.commonlib.ext.permission.request

import com.commonlib.utils.AppUtils
import java.io.File
import java.util.*

/**
 * 拍照选择器回调
 */
class CameraPickerCalBackActivity : AppCompatActivity() {

    private val IMAGECODE = 909
    private val CAMERACODE = 908
    private var imageName: String? = null
    private var isCamera = false
    private var imageItemSet: ArrayList<ImageItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start()
    }

    private fun start() {
        imageItemSet = ArrayList()
        isCamera = intent.getBooleanExtra("isCamera", false)
        if (isCamera) {
            request(Manifest.permission.CAMERA) {
                onGranted {
                    imageName = UUID.randomUUID().toString() + ".jpg"
                    val file = File(externalCacheDir, imageName)
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.putExtra(
                            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                                this@CameraPickerCalBackActivity,
                                "$packageName.fileProvider", file
                            )
                        )
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
                    }
                    startActivityForResult(intent, CAMERACODE)
                }

                onDenied {
                    Toast.makeText(
                        this@CameraPickerCalBackActivity,
                        "没有权限",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                onShowRationale { it.retry() }
            }
        } else {
            request(Manifest.permission.READ_EXTERNAL_STORAGE) {
                onGranted {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, IMAGECODE)
                }
                onDenied {
                    Toast.makeText(
                        this@CameraPickerCalBackActivity,
                        "没有权限",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                onShowRationale { it.retry() }
            }
        }

    }

    override fun onPause() {
        //去掉动画
        overridePendingTransition(0, 0)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == CAMERACODE) {
                val imageItem = ImageItem("$externalCacheDir/$imageName", imageName)
                imageItemSet!!.add(imageItem)
                ImagePicker.onImageSuccess(imageItemSet!!)
            }
        }
        if (requestCode == IMAGECODE) {
            if (null != data && null != data.data) {
                val imageItem = ImageItem(AppUtils.getRealPathFromURI(this, data.data))
                imageItemSet!!.add(imageItem)
                ImagePicker.onImageSuccess(imageItemSet!!)
            }
        }
        //无动画关闭
        finish()
        overridePendingTransition(0, 0)
    }
}

package com.songdehuai.commonlib.utils

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.songdehuai.commonlib.R
import com.tencent.mmkv.MMKV

/**
 * DialogUtils
 *
 * @author songdehuai
 */
class DialogUtils(activity: Activity) {

    private lateinit var loadDialog: AlertDialog
    private lateinit var loadDialogBuilder: AlertDialog.Builder
    private lateinit var loadDialogView: View
    private lateinit var loadTextView: TextView
    private var mActivity: Activity = activity
    private var loadInit = false

    private fun initLoadDialog() {
        loadDialogView = View.inflate(mActivity, R.layout.dialog_load_uitls, null)
        loadTextView = loadDialogView.findViewById(R.id.dialog_tv)
        loadDialogBuilder = AlertDialog.Builder(mActivity)
        loadDialogBuilder.setView(loadDialogView)
        loadDialog = loadDialogBuilder.create()
        loadInit = true
    }

    fun showLaodDialog(text: String) {
        if (!loadInit) {
            initLoadDialog()
        }
        loadTextView.text = text
        loadDialog.show()
    }

    fun dismissLoadDialog() {
        if (loadDialog.isShowing) {
            loadDialog.dismiss()
        }
    }


    private lateinit var dialog: AlertDialog
    private lateinit var dialogBuilder: AlertDialog.Builder
    private var init = false

    private fun initDialog() {
        dialogBuilder = AlertDialog.Builder(mActivity)
        dialogBuilder.setPositiveButton(
            "确定"
        ) { _, _ -> }
        dialog = dialogBuilder.create()
        init = true
    }

    fun showDialog(text: String) {
        if (!init) {
            initDialog()
        }
        dialog.setMessage(text)
        dialog.show()
    }

    fun dismissDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }


}


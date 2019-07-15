package com.songdehuai.commonlib.expansion

import android.view.View
import androidx.annotation.Nullable
import com.songdehuai.commonlib.utils.LogUtil


private var mLastClickTime: Long = 0

private const val TIME_INTERVAL = 300L

fun View.setSingleClickListener(@Nullable listener: View.OnClickListener) {
    this.setOnClickListener {
        val nowTime = System.currentTimeMillis()
        if (nowTime - mLastClickTime > TIME_INTERVAL) {
            // 正常点击
            listener.onClick(this)
            mLastClickTime = nowTime
        } else {
            //连续点击，不处理

        }
    }
}
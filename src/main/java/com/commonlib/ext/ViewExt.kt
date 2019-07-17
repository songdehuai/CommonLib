package com.commonlib.ext

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.Nullable
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat


fun View.visible() = run { visibility = View.VISIBLE }

fun View.invisible() = run { visibility = View.INVISIBLE }
fun View.gone() = run { visibility = View.GONE }

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) = if (value) visible() else gone()

var View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) = if (value) invisible() else visible()

var View.isGone: Boolean
    get() = visibility == View.GONE
    set(value) = if (value) gone() else visible()

fun View.setPadding(@Px size: Int) {
    setPadding(size, size, size, size)
}

inline fun View.postDelayed(delayInMillis: Long, crossinline action: () -> Unit): Runnable {
    val runnable = Runnable { action() }
    postDelayed(runnable, delayInMillis)
    return runnable
}

fun View.toBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling toBitmap()")
    }
    return Bitmap.createBitmap(width, height, config).applyCanvas {
        translate(-scrollX.toFloat(), -scrollY.toFloat())
        draw(this)
    }
}
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
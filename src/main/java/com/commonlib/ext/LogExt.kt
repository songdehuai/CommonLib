package com.commonlib.ext

import com.commonlib.utils.LogUtil


fun String.logD() {
    LogUtil.d(this)
}

fun String.logE() {
    LogUtil.e(this)
}

fun String.logI() {
    LogUtil.i(this)
}

fun String.logV() {
    LogUtil.v(this)
}

fun String.logW() {
    LogUtil.w(this)
}

fun String.logWtf() {
    LogUtil.wtf(this)
}

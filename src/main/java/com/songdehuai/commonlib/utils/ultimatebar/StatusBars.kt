@file:Suppress("NOTHING_TO_INLINE")

package com.songdehuai.commonlib.utils.ultimatebar

import android.app.Activity

/**
 * 状态栏工具
 * @author songdehuai
 */
inline fun Activity.ultimateBarBuilder(): StatusBar.Builder = StatusBar.with(this)

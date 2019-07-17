package com.commonlib.ext

import com.google.gson.Gson

fun Any.toJsonStr(): String {
    return Gson().toJson(this)
}
package com.songdehuai.commonlib.net


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Result<T>(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    var result: T
) {
    fun isSuccess(): Boolean {
        return "200" == code
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
package com.commonlib.net

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType

import okgo.callback.AbsCallback
import okhttp3.Response

open class ResultCallBack<T> : AbsCallback<T>() {

    override fun convertResponse(response: Response): T? {
        var data: T? = null
        val rootType = javaClass.genericSuperclass
        val type = (rootType as ParameterizedType).actualTypeArguments[0]
        response.body?.run {
            data = Gson().fromJson(string(), type)
        }
        return data
    }
}

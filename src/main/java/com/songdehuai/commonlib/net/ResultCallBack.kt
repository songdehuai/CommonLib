package com.songdehuai.commonlib.net

import com.google.gson.Gson
import com.songdehuai.commonlib.expansion.logE
import java.lang.reflect.ParameterizedType

import okgo.callback.AbsCallback
import okgo.request.base.Request
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

    internal var onStart: ((Request<T, out Request<*, *>>) -> Unit)? = null
    internal var onSuccess: ((T) -> Unit)? = null
    internal var onCacheSuccess: ((okgo.model.Response<T>) -> Unit)? = null
    internal var onError: ((String) -> Unit)? = null
    internal var onFinish: (() -> Unit)? = null

    override fun onSuccess(response: okgo.model.Response<T>?) {
        super.onSuccess(response)
        response?.let { onSuccess?.invoke(it.body()) }
    }

    override fun onError(response: okgo.model.Response<T>?) {
        super.onError(response)
        response?.let {
            it.exception.message?.let { ex ->
                onError?.invoke(ex)
                ex.logE()
            }
        }
    }

    fun success(result: (T) -> Unit) {
        onSuccess = result
    }

    fun error(message: (String) -> Unit) {
        onError = message
    }
}

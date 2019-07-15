package com.songdehuai.commonlib.net

import com.songdehuai.commonlib.expansion.logE
import okgo.OkGo
import okgo.callback.AbsCallback
import okgo.model.Response
import okgo.request.base.Request
import java.lang.reflect.ParameterizedType


object HTTP {

    fun <T> post(url: String, resultCallBack: ResultCallBack<T>.() -> Unit) {
        OkGo.post<T>(url).execute(ResultCallBack<T>().also(resultCallBack))
    }

    fun <T> test(url: String, resultCallBack: ResultCallBack<T>) {
        OkGo.post<T>(url).execute(resultCallBack)
    }

    open class ResultCallBack<T> : AbsCallback<T>() {

        override fun convertResponse(response: okhttp3.Response): T? {
            val genType = javaClass.genericSuperclass
            val type = (genType as ParameterizedType).actualTypeArguments[0]
            val convert = JsonConvert<T>(type)
            return convert.convertResponse(response)
        }

        internal var onStart: ((Request<T, out Request<*, *>>) -> Unit)? = null
        internal var onSuccess: ((T) -> Unit)? = null
        internal var onCacheSuccess: ((Response<T>) -> Unit)? = null
        internal var onError: ((String) -> Unit)? = null
        internal var onFinish: (() -> Unit)? = null

        override fun onSuccess(response: Response<T>?) {
            super.onSuccess(response)
            response?.let { onSuccess?.invoke(it.body()) }
        }

        override fun onError(response: Response<T>?) {
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
}
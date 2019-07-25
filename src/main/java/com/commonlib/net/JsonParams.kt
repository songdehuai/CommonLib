package com.commonlib.net

import okgo.OkGo
import okgo.model.HttpParams
import org.json.JSONObject


/**
 * Json形式参数构建
 */
class JsonParams : HttpParams() {

    private val mJsonParams by lazy { JSONObject() }

    /**
     * 添加请求参数
     */
    fun addJsonParam(key: String, value: String) {
        mJsonParams.put(key, value)
    }

    /**
     * 添加请求参数
     */
    fun addJsonParam(key: String, value: Int) {
        mJsonParams.put(key, value)
    }

    /**
     * 添加请求参数
     */
    fun addJsonParam(key: String, value: Double) {
        mJsonParams.put(key, value)
    }

    /**
     * 添加请求参数
     */
    fun addJsonParam(key: String, value: Long) {
        mJsonParams.put(key, value)
    }

    /**
     * 添加请求参数
     */
    fun addJsonParam(key: String, value: Any) {
        mJsonParams.put(key, value)
    }

    /**
     * 发送Json形式的Post请求
     * @param uri       请求地址
     * @param callBack  请求回调
     */
    fun <T> postJson(uri: String, callBack: ResultCallBack<T>) {
        OkGo.post<T>(uri).upJson(mJsonParams.toString()).execute(callBack)
    }

}
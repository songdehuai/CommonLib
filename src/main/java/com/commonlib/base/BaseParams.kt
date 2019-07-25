package com.commonlib.base

import com.google.gson.Gson
import com.commonlib.net.ResultCallBack
import java.util.LinkedHashMap
import okgo.OkGo
import okgo.model.HttpParams


/**
 * 网络请求参数
 *
 * @author songdehuai
 */
open class BaseParams : HttpParams() {

    /**
     * 普通的键值对参数
     * 不可更改，不可删除，否则将会将多余参数带入请求中
     */
    @Transient
    var urlParamsMap: LinkedHashMap<String, List<String>>? = null

    /**
     * 文件的键值对参数
     * 不可更改，不可删除，否则将会将多余参数带入请求中
     */
    @Transient
    var fileParamsMap: LinkedHashMap<String, List<HttpParams.FileWrapper>>? = null


    /**
     * 将对象转换成jsonString
     *
     * @return jsonString
     */
    fun toJson(): String {
        put(this)
        return Gson().toJson(this)
    }

    /**
     * 发送Post请求，表单形式
     *
     * @param url            请求地址
     * @param resultCallBack 返回数据监听
     * @param <T>            要解析的数据类型
    </T> */
    fun <T> post(url: String, resultCallBack: ResultCallBack<T>) {
        OkGo.post<T>(url).params(this).execute(resultCallBack)
    }

    /**
     * 发送Post请求，json形式
     *
     * @param url            请求地址
     * @param resultCallBack 返回数据监听
     * @param <T>            要解析的数据类型
    </T> */
    open fun <T> postJson(url: String, resultCallBack: ResultCallBack<T>) {
        OkGo.post<T>(url).upJson(this.toJson()).execute(resultCallBack)
    }

    /**
     * 发送Post请求，json形式
     *
     * @param url            请求地址
     * @param resultCallBack 返回数据监听
     * @param <T>            要解析的数据类型
    </T> */
    open fun <T> postJson(url: String, jsonParams: String, resultCallBack: ResultCallBack<T>) {
        OkGo.post<T>(url).upJson(jsonParams).execute(resultCallBack)
    }

    /**
     * 发送Get请求
     *
     * @param url            请求地址
     * @param resultCallBack 返回数据监听
     * @param <T>            要解析的数据类型
    </T> */
    operator fun <T> get(url: String, resultCallBack: ResultCallBack<T>) {
        OkGo.get<T>(url).params(this).execute(resultCallBack)
    }

    /**
     * 构建请求参数
     *
     * @param paramsBuilder 请求参数体
     * @param <T>           请求参数类型
     * @return 构建的参数
    </T> */
    fun <T> builder(paramsBuilder: ParamsBuilder<T>): BaseParams {
        paramsBuilder.addParams(this as T)
        return this
    }

}


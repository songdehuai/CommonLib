package okgo

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper

import java.util.concurrent.TimeUnit
import java.util.logging.Level

import okgo.cache.CacheEntity
import okgo.cache.CacheMode
import okgo.cookie.CookieJarImpl
import okgo.https.HttpsUtils
import okgo.interceptor.HttpLoggingInterceptor
import okgo.model.HttpHeaders
import okgo.model.HttpParams
import okgo.request.DeleteRequest
import okgo.request.GetRequest
import okgo.request.HeadRequest
import okgo.request.OptionsRequest
import okgo.request.PatchRequest
import okgo.request.PostRequest
import okgo.request.PutRequest
import okgo.request.TraceRequest
import okgo.utils.HttpUtils
import okhttp3.OkHttpClient

/**
 * 作    者：songdehuai
 * 版    本：1.0
 * 创建日期：2019/7/1
 */
class OkGo private constructor() {

    private var context: Application? = null            //全局上下文
    val delivery: Handler              //用于在主线程执行的调度器
    private var okHttpClient: OkHttpClient? = null      //ok请求的客户端
    /** 获取全局公共请求参数  */
    var commonParams: HttpParams? = null
        private set       //全局公共请求参数
    /** 获取全局公共请求头  */
    var commonHeaders: HttpHeaders? = null
        private set     //全局公共请求头
    private var mRetryCount: Int = 0                //全局超时重试次数
    private var mCacheMode: CacheMode? = null           //全局缓存模式
    private var mCacheTime: Long = 0                //全局缓存过期时间,默认永不过期

    /** 获取全局的cookie实例  */
    val cookieJar: CookieJarImpl
        get() = okHttpClient!!.cookieJar as CookieJarImpl

    init {
        delivery = Handler(Looper.getMainLooper())
        mRetryCount = 3
        mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE
        mCacheMode = CacheMode.NO_CACHE

        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor("OkGo")
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        loggingInterceptor.setColorLevel(Level.INFO)
        builder.addInterceptor(loggingInterceptor)

        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)

        val sslParams = HttpsUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)
        okHttpClient = builder.build()
    }

    private object OkGoHolder {
        val holder = OkGo()
    }

    /** 必须在全局Application先调用，获取context上下文，否则缓存无法使用  */
    fun init(app: Application): OkGo {
        context = app
        return this
    }

    /** 获取全局上下文  */
    fun getContext(): Application? {
        HttpUtils.checkNotNull(
            context,
            "please call OkGo.getInstance().init() first in application!"
        )
        return context
    }

    fun getOkHttpClient(): OkHttpClient? {
        HttpUtils.checkNotNull(
            okHttpClient,
            "please call OkGo.getInstance().setOkHttpClient() first in application!"
        )
        return okHttpClient
    }

    /** 必须设置  */
    fun setOkHttpClient(okHttpClient: OkHttpClient): OkGo {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null")
        this.okHttpClient = okHttpClient
        return this
    }

    /** 超时重试次数  */
    fun setRetryCount(retryCount: Int): OkGo {
        if (retryCount < 0) throw IllegalArgumentException("retryCount must > 0")
        mRetryCount = retryCount
        return this
    }

    /** 超时重试次数  */
    fun getRetryCount(): Int {
        return mRetryCount
    }

    /** 全局的缓存模式  */
    fun setCacheMode(cacheMode: CacheMode): OkGo {
        mCacheMode = cacheMode
        return this
    }

    /** 获取全局的缓存模式  */
    fun getCacheMode(): CacheMode? {
        return mCacheMode
    }

    /** 全局的缓存过期时间  */
    fun setCacheTime(cacheTime: Long): OkGo {
        var cacheTime = cacheTime
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE
        mCacheTime = cacheTime
        return this
    }

    /** 获取全局的缓存过期时间  */
    fun getCacheTime(): Long {
        return mCacheTime
    }

    /** 添加全局公共请求参数  */
    fun addCommonParams(commonParams: HttpParams): OkGo {
        if (this.commonParams == null) this.commonParams = HttpParams()
        this.commonParams!!.put(commonParams)
        return this
    }

    /** 添加全局公共请求参数  */
    fun addCommonHeaders(commonHeaders: HttpHeaders): OkGo {
        if (this.commonHeaders == null) this.commonHeaders = HttpHeaders()
        this.commonHeaders!!.put(commonHeaders)
        return this
    }

    /** 根据Tag取消请求  */
    fun cancelTag(tag: Any?) {
        if (tag == null) return
        getOkHttpClient()?.run {
            dispatcher.queuedCalls().forEach {
                if (tag == it.request().tag()) {
                    it.cancel()
                }
            }
            dispatcher.runningCalls().forEach {
                if (tag == it.request().tag()) {
                    it.cancel()
                }
            }
        }
    }

    /** 取消所有请求请求  */
    fun cancelAll() {
        getOkHttpClient()?.run {
            dispatcher.queuedCalls().forEach {
                it.cancel()
            }
            dispatcher.runningCalls().forEach {
                it.cancel()
            }
        }
    }

    companion object {
        val DEFAULT_MILLISECONDS: Long = 60000      //默认的超时时间
        var REFRESH_TIME: Long = 300                      //回调刷新时间（单位ms）

        val instance: OkGo
            get() = OkGoHolder.holder

        /** get请求  */
        operator fun <T> get(url: String): GetRequest<T> {
            return GetRequest(url)
        }

        /** post请求  */
        fun <T> post(url: String): PostRequest<T> {
            return PostRequest(url)
        }

        /** put请求  */
        fun <T> put(url: String): PutRequest<T> {
            return PutRequest(url)
        }

        /** head请求  */
        fun <T> head(url: String): HeadRequest<T> {
            return HeadRequest(url)
        }

        /** delete请求  */
        fun <T> delete(url: String): DeleteRequest<T> {
            return DeleteRequest(url)
        }

        /** options请求  */
        fun <T> options(url: String): OptionsRequest<T> {
            return OptionsRequest(url)
        }

        /** patch请求  */
        fun <T> patch(url: String): PatchRequest<T> {
            return PatchRequest(url)
        }

        /** trace请求  */
        fun <T> trace(url: String): TraceRequest<T> {
            return TraceRequest(url)
        }

        /** 根据Tag取消请求  */
        fun cancelTag(client: OkHttpClient?, tag: Any?) {
            if (client == null || tag == null) return
            client.run {
                dispatcher.queuedCalls().forEach { call ->
                    if (tag == call.request().tag()) {
                        call.cancel()
                    }
                }

                dispatcher.runningCalls().forEach { call ->
                    if (tag == call.request().tag()) {
                        call.cancel()
                    }
                }
            }
        }

        /** 取消所有请求请求  */
        fun cancelAll(client: OkHttpClient?) {
            if (client == null) return
            client.run {
                dispatcher.queuedCalls().forEach {
                    it.cancel()
                }
                dispatcher.runningCalls().forEach {
                    it.cancel()
                }
            }
        }
    }
}

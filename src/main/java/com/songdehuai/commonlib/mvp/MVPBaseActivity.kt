package com.songdehuai.commonlib.mvp

import android.os.Bundle
import com.songdehuai.commonlib.base.BaseActivity
import java.lang.reflect.ParameterizedType


/**
 * MVPBaseActivity
 * @author songdehuai
 */
open class MVPBaseActivity<V : BaseView, T : BasePresenterImpl<V>> : BaseActivity() {

    var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getInstance<T>(this, 1)
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }


    fun <T> getInstance(o: Any, i: Int): T? {
        try {
            return ((o.javaClass
                .genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<T>)
                .newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }


}
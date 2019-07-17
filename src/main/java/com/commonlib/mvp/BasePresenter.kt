package com.commonlib.mvp


/**
 * BasePresenter
 */
interface BasePresenter<V : BaseView> {

    fun attachView(view: V)

    fun detachView()

    fun cancelAllTask()
}

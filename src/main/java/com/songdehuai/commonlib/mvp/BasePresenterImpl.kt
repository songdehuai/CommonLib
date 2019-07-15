package com.songdehuai.commonlib.mvp

import okgo.OkGo


open class BasePresenterImpl<V : BaseView> : BasePresenter<V> {

    override fun cancelAllTask() {
        //取消所有请求
        OkGo.instance.cancelAll()
    }

    protected var mView: V? = null

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

}

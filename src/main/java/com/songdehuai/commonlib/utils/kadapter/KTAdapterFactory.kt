package com.songdehuai.commonlib.utils.kadapter

object KTAdapterFactory {

    fun <T> KAdapter(body: BaseKTAdapter<T>.() -> Unit): BaseKTAdapter<T> {
        val adapter = object : BaseKTAdapter<T>() {}
        val resultAdapter = adapter as BaseKTAdapter<T>
        resultAdapter.body()
        return resultAdapter
    }

    inline fun <reified T> KAdapter(
        layoutId: Int, body: BaseKTAdapter<T>.() -> Unit
    ): BaseKTAdapter<T> {
        val adapter = object : BaseKTAdapter<T>() {}
        val resultAdapter = adapter as BaseKTAdapter<T>
        resultAdapter.layout { layoutId }
        resultAdapter.body()
        return resultAdapter
    }

}
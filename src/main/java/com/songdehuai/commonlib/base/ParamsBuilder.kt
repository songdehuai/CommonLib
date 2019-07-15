package com.songdehuai.commonlib.base

/**
 * 参数构建
 * @param <T>
</T> */
interface ParamsBuilder<T> {
    fun addParams(params: T)
}

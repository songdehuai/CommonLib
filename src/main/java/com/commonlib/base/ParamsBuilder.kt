package com.commonlib.base

/**
 * 参数构建
 * @param <T>
</T> */
interface ParamsBuilder<T> {
    fun addParams(params: T)
}

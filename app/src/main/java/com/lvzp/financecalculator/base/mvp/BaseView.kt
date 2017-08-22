package com.lvzp.financecalculator.base.mvp

import android.content.Context

/**
 * 作者：吕振鹏
 * 创建时间：06月11日
 * 时间：16:02
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
interface BaseView {
    /**
     * 获取当前View的环境变量
     */
    fun getContent(): Context?

    /**
     * 显示Toast提示
     */
    fun showToast(message: String)

    /**
     * 显示Snackbar提示
     */
    fun showSnackbar(message: String)
}
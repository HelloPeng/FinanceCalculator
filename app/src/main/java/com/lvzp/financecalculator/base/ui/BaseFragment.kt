package com.lvzp.financecalculator.base.ui

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.design.snackbar
import www.juyun.net.beedriver.base.mvp.BasePresenter
import com.lvzp.financecalculator.base.mvp.BaseView
import www.juyun.net.beedriver.base.ui.BaseActivity


/**
 * 作者：吕振鹏
 * 创建时间：2017/6/14
 * 时间：15:37
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
@SuppressLint("ShowToast")
abstract class BaseFragment<D : ViewDataBinding, V : BaseView, P : BasePresenter<V>> : Fragment(), BaseView {

    protected var mDataBinding: D? = null
    protected var mPresenter: P? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        mPresenter = createPresenter()
        mPresenter?.attach(this as V)
        initViews()
        return mDataBinding?.root
    }

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun initViews()

    protected abstract fun createPresenter(): P

    override fun getContent(): Context? {
        return context
    }

    override fun showToast(message: String) {
        (activity as BaseActivity<*, *, *>).showToast(message)
    }

    override fun showSnackbar(message: String) {
        snackbar(mDataBinding!!.root, message)
    }

}
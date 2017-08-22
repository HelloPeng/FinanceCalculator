package com.lvzp.financecalculator.base.adapter

import android.databinding.ViewDataBinding


/**
 * 作者：吕振鹏
 * 创建时间：2017/6/15
 * 时间：10:29
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
open class SimpleBindingAdapter<T>(resLayoutId: Int, val mVariable: Int) : BindingBaseRecycleAdapter<T, ViewDataBinding>(resLayoutId) {

    override fun bindingViews(holder: BindingViewHolder<ViewDataBinding>, position: Int, t: T?) {
        val binding = holder.getBinding()
        binding?.setVariable(mVariable, t)
        binding?.executePendingBindings()
    }


}
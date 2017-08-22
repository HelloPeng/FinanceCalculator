package com.lvzp.financecalculator.base.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView


/**
 * 作者：吕振鹏
 * 创建时间：2017/6/15
 * 时间：9:29
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
class BindingViewHolder<out D : ViewDataBinding>(binding: D) : RecyclerView.ViewHolder(binding.root) {

    private var mOnItemClickListener: BindingBaseRecycleAdapter.OnItemClickListener? = null
    private var mOnItemLongClickListener: BindingBaseRecycleAdapter.OnItemLongClickListener? = null
    private var mBinding: D? = binding

    fun getBinding(): D? {
        return mBinding
    }

    fun setOnItemClickListener(listener: BindingBaseRecycleAdapter.OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: BindingBaseRecycleAdapter.OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }

    init {
        binding.root.setOnClickListener {
            mOnItemClickListener?.onItemClick(this, binding.root, layoutPosition)
        }
        binding.root.setOnLongClickListener {
            mOnItemLongClickListener?.onItemLongClick(this, binding.root, layoutPosition)
            mOnItemClickListener != null
        }
    }
}

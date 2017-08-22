package com.lvzp.financecalculator.base.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * 作者：吕振鹏
 * 创建时间：2017/6/15
 * 时间：9:26
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
abstract class BindingBaseRecycleAdapter<T, D : ViewDataBinding>(resLayoutId: Int) : RecyclerView.Adapter<BindingViewHolder<D>>() {
    protected var mDataBinding: D? = null

    protected var mListData: MutableList<T>? = null
    private var mResLayoutId: Int = resLayoutId
    private var mLayoutInflater: LayoutInflater? = null

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    fun setupData(listData: MutableList<T>?) {
        mListData = listData
        notifyDataSetChanged()
    }

    fun addItem(item: T?) {
        if (item == null)
            return
        mListData?.add(item)
        notifyItemInserted(0)
    }

    fun cleanData() {
        if (mListData != null)
            mutableListOf(mListData).clear()
        mListData = null
    }

    fun getItem(i: Int): T? {
        if (mListData == null)
            return null
        else
            return mListData!![i]
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingViewHolder<D> {
        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(parent?.context)
        val binding: D = DataBindingUtil.bind(mLayoutInflater?.inflate(mResLayoutId, parent, false))
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<D>, position: Int) {
        holder.setOnItemClickListener(mOnItemClickListener)
        holder.setOnItemLongClickListener(mOnItemLongClickListener)
        bindingViews(holder, position, getItem(position))
    }

    protected abstract fun bindingViews(holder: BindingViewHolder<D>, position: Int, t: T?)

    override fun getItemCount(): Int {
        return if (mListData == null) 0 else mListData!!.size
    }


    interface OnItemClickListener {
        /**
         * [RecyclerView]的条目点击事件
         */
        fun onItemClick(viewHolder: RecyclerView.ViewHolder?, view: View?, position: Int?)
    }

    interface OnItemLongClickListener {
        /**
         * [RecyclerView]的条目点击事件
         */
        fun onItemLongClick(viewHolder: RecyclerView.ViewHolder?, view: View?, position: Int?)
    }


}
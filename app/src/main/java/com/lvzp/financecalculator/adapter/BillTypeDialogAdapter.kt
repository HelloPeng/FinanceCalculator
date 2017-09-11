package com.lvzp.financecalculator.adapter

import android.annotation.SuppressLint
import android.support.v7.view.menu.MenuItemImpl
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lvzp.financecalculator.R
import org.jetbrains.anko.image

/**
 * 作者：吕振鹏
 * 创建时间：08月27日
 * 时间：22:37
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
class BillTypeDialogAdapter : RecyclerView.Adapter<BillTypeDialogAdapter.MyHolder>() {

    private var mListData: List<MenuItemImpl>? = null
    private var mItemClickListener: OnItemClickListener? = null

    fun setupDate(listData: List<MenuItemImpl>?) {
        mListData = listData
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (layoutItem: View, position: Int) -> Unit) {
        mItemClickListener = object : OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {
                listener.invoke(itemView, position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_bill_type, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mListData?.size ?: 0
    }


    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        val menuItem = mListData?.get(position)
        holder?.tvBillName?.text = menuItem?.title.toString()
        holder?.ivBillIcon?.image = menuItem?.icon
    }

    inner class MyHolder(layoutView: View) : RecyclerView.ViewHolder(layoutView) {
        var tvBillName: TextView? = null
        var ivBillIcon: ImageView? = null

        init {
            tvBillName = layoutView.findViewById(R.id.tv_bill_type_name) as TextView
            ivBillIcon = layoutView.findViewById(R.id.iv_bill_type_icon) as ImageView
            layoutView.setOnClickListener {
                mItemClickListener?.onItemClick(layoutView,adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(itemView: View, position: Int)
    }
}
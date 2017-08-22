package com.lvzp.financecalculator.ui

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lvzp.financecalculator.BR
import com.lvzp.financecalculator.R
import com.lvzp.financecalculator.base.adapter.BindingViewHolder
import com.lvzp.financecalculator.base.adapter.SimpleBindingAdapter
import com.lvzp.financecalculator.bean.Items
import com.lvzp.financecalculator.bean.OrderBean
import com.lvzp.financecalculator.databinding.ActivityMainBinding
import com.lvzp.financecalculator.databinding.ItemLayoutBinding
import com.lvzp.financecalculator.databinding.ItemMainBinding
import com.lvzp.financecalculator.mvp.contract.MainContract
import com.lvzp.financecalculator.mvp.presenter.MainPresenter
import org.jetbrains.anko.imageResource
import www.juyun.net.beedriver.base.ui.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainContract.View, MainPresenter>(), MainContract.View {

    var mList: ArrayList<OrderBean>? = ArrayList()
    var mItems: Array<Int> = arrayOf(
            R.drawable.ic_vector_pingan,
            R.drawable.ic_vector_baidu,
            R.drawable.ic_vector_jd,
            R.drawable.ic_vector_jiebai,
            R.drawable.ic_vector_jt,
            R.drawable.ic_vector_xiaomi,
            R.drawable.ic_vector_zhaoshang,
            R.drawable.ic_vector_huabai,
            R.drawable.ic_vector_zhongxin)
    var mNames: Array<String> = arrayOf(
            "平安银行",
            "百度钱包",
            "京东金融",
            "蚂蚁借呗",
            "交通银行",
            "小米金融",
            "招商银行",
            "蚂蚁借呗",
            "中信银行")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * 设置标题栏的属性
     */
    override fun setupTitle() {
        setTitleText("还款记录")
    }

    /**
     * 初始化基本控件
     */
    override fun initViews() {
        for (i: Int in 0..10) {
            val outItem: OrderBean = OrderBean(price = "13", month = "3", total = "8")
            val items = ArrayList<Items>()
            (0..8).mapTo(items) { Items(mItems[it], mNames[it], "9月30日", "33.113") }
            outItem.items = items
            mList?.add(outItem)
        }
        mDataBinding?.recyclerView?.layoutManager = LinearLayoutManager(this)
        val value: SimpleBindingAdapter<OrderBean> = object : SimpleBindingAdapter<OrderBean>(R.layout.item_main, BR.item) {
            override fun bindingViews(holder: BindingViewHolder<ViewDataBinding>, position: Int, t: OrderBean?) {
                super.bindingViews(holder, position, t)
                val binding = holder.getBinding() as ItemMainBinding
                binding.itemRecyclerView.layoutManager = object : LinearLayoutManager(this@MainActivity) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                val adapter: SimpleBindingAdapter<Items> = object : SimpleBindingAdapter<Items>(R.layout.item_layout, BR.orderItem) {
                    override fun bindingViews(itemHolder: BindingViewHolder<ViewDataBinding>, itemPosition: Int, item: Items?) {
                        super.bindingViews(itemHolder, position, item)
                        val dataBinding = itemHolder.getBinding() as ItemLayoutBinding
                        dataBinding.ivIcon.imageResource = item?.iconRes ?: 0
                    }
                }
                adapter.setupData(t?.items?.toMutableList())
                binding.itemRecyclerView.adapter = adapter
            }
        }
        value.setupData(mList)
        mDataBinding?.recyclerView?.adapter = value

    }

    /**
     * 获取布局文件
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    /**
     * 创建View的控制器
     */
    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }
}

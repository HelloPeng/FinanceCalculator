package com.lvzp.financecalculator.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.lvzp.financecalculator.BR
import com.lvzp.financecalculator.R
import com.lvzp.financecalculator.base.adapter.BindingViewHolder
import com.lvzp.financecalculator.base.adapter.SimpleBindingAdapter
import com.lvzp.financecalculator.bean.DBOrderInfoBean
import com.lvzp.financecalculator.bean.DBOrderTotal
import com.lvzp.financecalculator.bean.Items
import com.lvzp.financecalculator.bean.OrderBean
import com.lvzp.financecalculator.databinding.ActivityMainBinding
import com.lvzp.financecalculator.databinding.ItemLayoutBinding
import com.lvzp.financecalculator.databinding.ItemMainBinding
import com.lvzp.financecalculator.db.DBManager
import com.lvzp.financecalculator.db.SQLHelper
import com.lvzp.financecalculator.db.iml.DBManagerIml
import com.lvzp.financecalculator.mvp.contract.MainContract
import com.lvzp.financecalculator.mvp.presenter.MainPresenter
import com.lvzp.financecalculator.utils.MenuBuildUtils
import com.lvzp.statusbarlib.StatusBarCompat
import org.jetbrains.anko.imageResource
import www.juyun.net.beedriver.base.ui.BaseActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("RestrictedApi")
class MainActivity : BaseActivity<ActivityMainBinding, MainContract.View, MainPresenter>(), MainContract.View {

    companion object {
        private val REQUEST_CODE_ADD_BILL = 0x0000331
    }

    var mList: ArrayList<OrderBean>? = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarCompat.with(this).statusBarDarkFont(false).init()
    }

    /**
     * 设置标题栏的属性
     */
    override fun setupTitle() {
        setTitleText("还款记录")
        val rightView = ImageView(this)
        rightView.imageResource = R.drawable.ic_vector_add
        setTitleRightLayout(rightView)
        rightView.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, AddBillActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_BILL)
        }
    }

    /**
     * 初始化基本控件
     */
    override fun initViews() {
        val menuItemList = MenuBuildUtils.buildMenuItemList(this, R.menu.billmenu)
        val dbManager = DBManagerIml.getInstance(this)
        val dataList = dbManager.query(SQLHelper.DB_BILL_RECORD_TABLE_NAME, DBOrderInfoBean::class.java)
        DBManager.orderBy = "${SQLHelper.BILL_TOTAL_VALUE_KEY_YEAR}, ${SQLHelper.BILL_TOTAL_VALUE_KEY_MONEY}"
        val dataTotalList = dbManager.query(SQLHelper.DB_BILL_TOTAL_TABLE_NAME, DBOrderTotal::class.java)
        dataTotalList.map {
            val outItem = OrderBean(price = it?.money, month = it?.month, total = it?.num)
            val items = ArrayList<Items>()
            dataList.map { o ->
                menuItemList.filter {
                    it.titleCondensed == o?.account
                }.map {
                    val dates = o?.repay_time?.split("-")
                    items.add(Items(it.icon, it.title.toString(), "${dates?.get(1)}月${dates?.get(2)}日", o?.repay_money))
                }
            }
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
                        dataBinding.ivIcon.setImageDrawable(item?.iconRes)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

        }
    }

    private fun getDate(date: String?): Array<String> {
        val sf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            calendar.time = sf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return arrayOf(calendar.get(Calendar.YEAR).toString(), (calendar.get(Calendar.MONTH) + 1).toString(), calendar.get(Calendar.DAY_OF_MONTH).toString())
    }


    private fun updateListDataItem(dbBean: DBOrderInfoBean) {
        var date = getDate(dbBean.repay_time)

    }

}

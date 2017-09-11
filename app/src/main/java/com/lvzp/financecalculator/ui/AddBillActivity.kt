package com.lvzp.financecalculator.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.view.menu.MenuItemImpl
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.DatePicker
import com.lvzp.financecalculator.R
import com.lvzp.financecalculator.adapter.BillTypeDialogAdapter
import com.lvzp.financecalculator.bean.DBOrderInfoBean
import com.lvzp.financecalculator.bean.DBOrderTotal
import com.lvzp.financecalculator.databinding.ActivityAddBillBinding
import com.lvzp.financecalculator.db.DBManager
import com.lvzp.financecalculator.db.SQLHelper
import com.lvzp.financecalculator.db.iml.DBManagerIml
import com.lvzp.financecalculator.mvp.contract.AddBillContract
import com.lvzp.financecalculator.mvp.presenter.AddBillPresenter
import com.lvzp.financecalculator.utils.MenuBuildUtils
import com.lvzp.statusbarlib.StatusBarCompat
import org.jetbrains.anko.toast
import www.juyun.net.beedriver.base.ui.BaseActivity
import java.util.*

/**
 * 添加账单的Activity
 */
@SuppressLint("RestrictedApi")
class AddBillActivity : BaseActivity<ActivityAddBillBinding, AddBillContract.View, AddBillPresenter>(),
        AddBillContract.View,
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private var mDateDialog: DatePickerDialog? = null
    private var mBillTypeDialog: AlertDialog? = null
    private var mBillTypeSelectPosition = -1
    private var mDBManager: DBManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * 设置标题栏的属性
     */
    override fun setupTitle() {
        StatusBarCompat.with(this).statusBarDarkFont(false).init()
        openTitleLeftView(true)
        setTitleText("添加新内容")
    }

    /**
     * 初始化基本控件
     */
    override fun initViews() {
        mDBManager = DBManagerIml.getInstance(this)
        mDataBinding?.btnSaves?.setOnClickListener(this)
        mDataBinding?.llSelectBillRepayTime?.setOnClickListener(this)
        mDataBinding?.llSelectBillType?.setOnClickListener(this)
        setupDateDialog()
        setupBillTypeDialog()
    }

    private var mMenuItemList: MutableList<MenuItemImpl>? = null

    private fun setupBillTypeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout_bill_type, null)
        val dialogRecyclerView = dialogView.findViewById(R.id.dialog_recycler_view) as RecyclerView
        dialogRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter = BillTypeDialogAdapter()
        dialogRecyclerView.adapter = adapter
        mMenuItemList = MenuBuildUtils.buildMenuItemList(this, R.menu.billmenu)
        adapter.setupDate(mMenuItemList)
        adapter.setOnItemClickListener { _, position ->
            mBillTypeSelectPosition = position
            val menuItem = mMenuItemList?.get(position)
            mDataBinding?.tvBillType?.text = menuItem?.title
            mBillTypeDialog?.dismiss()
        }
        mBillTypeDialog = AlertDialog.
                Builder(this).
                setTitle("请选择账户类型").
                setView(dialogView).
                create()
    }

    private fun setupDateDialog() {
        val instance = Calendar.getInstance()
        mDateDialog = DatePickerDialog(this, this,
                instance.get(Calendar.YEAR),
                instance.get(Calendar.MONTH),
                instance.get(Calendar.DAY_OF_MONTH))
    }

    /**
     * 获取布局文件
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_add_bill
    }

    /**
     * 创建View的控制器
     */
    override fun createPresenter(): AddBillPresenter {
        return AddBillPresenter()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_select_bill_repay_time -> mDateDialog?.show()
            R.id.ll_select_bill_type -> mBillTypeDialog?.show()
            R.id.btn_saves -> {
                if (mBillTypeSelectPosition == -1 || TextUtils.isEmpty(mDataBinding?.tvSelectBillRepayTime?.text)
                        || TextUtils.isEmpty(mDataBinding?.editRepayMoney?.text)) {
                    showSnackbar("请将数据补全后处理")
                    return
                }
                val itemMenu = mMenuItemList?.get(mBillTypeSelectPosition)
                val dbBean = DBOrderInfoBean()
                dbBean.account = itemMenu?.titleCondensed?.toString()
                dbBean.repay_time = mDataBinding?.tvSelectBillRepayTime?.text?.toString()
                dbBean.create_time = System.currentTimeMillis().toString()
                dbBean.repay_money = mDataBinding?.editRepayMoney?.text?.toString()
                dbBean.remarks = mDataBinding?.editRemarks?.text?.toString()
                val insert = mDBManager?.insert(SQLHelper.DB_BILL_RECORD_TABLE_NAME, dbBean)
                mDataBinding?.editRepayMoney?.setText("")
                val dates = dbBean.repay_time?.split("-")
                if (insert == true) {
                    DBManager.selection = "${SQLHelper.BILL_TOTAL_VALUE_KEY_YEAR} = ? and ${SQLHelper.BILL_TOTAL_VALUE_KEY_MONTH} = ?"
                    DBManager.selectionArgs = arrayOf(dates?.get(0), dates?.get(1))
                    val arrayList = mDBManager?.query(SQLHelper.DB_BILL_TOTAL_TABLE_NAME, DBOrderTotal::class.java)
                    if (arrayList?.size ?: 0 > 0) {
                        val item = arrayList?.get(0)
                        val num = (item?.num?.toInt() ?: 0 + 1).toString()
                        val money = (item?.money?.toFloat() ?: 0.toFloat() + (dbBean.repay_money?.toFloat() ?: 0.toFloat())).toString()
                        val bean = DBOrderTotal(num = num, money = money)
                        DBManager.selection = "_id = ?"
                        DBManager.selectionArgs = arrayOf(item?._id)
                        val update = mDBManager?.update(SQLHelper.DB_BILL_TOTAL_TABLE_NAME, bean)
                        toast("--总表--是否更新成功 = $update")
                    } else {
                        val bean = DBOrderTotal(num = "1", money = dbBean.repay_money, year = dates?.get(0), month = dates?.get(1), create_time = System.currentTimeMillis().toString())
                        val insertTotal = mDBManager?.insert(SQLHelper.DB_BILL_TOTAL_TABLE_NAME, bean)
                        toast("--总表--是否插入成功 = $insertTotal")
                    }
                } else {
                    showSnackbar("存储失败，数据有异常")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mDataBinding?.tvSelectBillRepayTime?.text = "$year-${month + 1}-$dayOfMonth"
    }
}

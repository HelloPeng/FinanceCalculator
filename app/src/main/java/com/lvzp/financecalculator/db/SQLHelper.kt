package com.lvzp.financecalculator.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 作者：吕振鹏
 * 创建时间：08月23日
 * 时间：22:32
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
class SQLHelper(context: Context?,
                name: String? = DB_NAME,
                factory: SQLiteDatabase.CursorFactory? = null,
                version: Int = DB_VERSION) :
        SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val DB_NAME = "data.db" //数据库名称

        val DB_VERSION = 1 //数据库版本

        val DB_BILL_RECORD_TABLE_NAME = "bill_record"
        val DB_BILL_TOTAL_TABLE_NAME = "bill_total"

        val BILL_RECORD_VALUE_KEY_ID = "_id"
        val BILL_RECORD_VALUE_KEY_ACCOUNT = "account"//账户名称
        val BILL_RECORD_VALUE_KEY_REPAY_TIME = "repay_time"//还款时间
        val BILL_RECORD_VALUE_KEY_REPAY_MONEY = "repay_money"//还款金额
        val BILL_RECORD_VALUE_KEY_REMARKS = "remarks"//备注
        val BILL_RECORD_VALUE_KEY_CREATE_TIME = "create_time"//创建时间

        val BILL_TOTAL_VALUE_KEY_ID = "_id"
        val BILL_TOTAL_VALUE_KEY_YEAR = "year"
        val BILL_TOTAL_VALUE_KEY_MONTH = "month"
        val BILL_TOTAL_VALUE_KEY_NUM = "num"
        val BILL_TOTAL_VALUE_KEY_MONEY = "money"
        val BILL_TOTAL_VALUE_KEY_CREATE_TIME = "create_time"//创建时间
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $DB_BILL_RECORD_TABLE_NAME (" +
                "$BILL_RECORD_VALUE_KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$BILL_RECORD_VALUE_KEY_ACCOUNT TEXT, " +
                "$BILL_RECORD_VALUE_KEY_REPAY_MONEY TEXT, " +
                "$BILL_RECORD_VALUE_KEY_REPAY_TIME TEXT, " +
                "$BILL_RECORD_VALUE_KEY_REMARKS TEXT, " +
                "$BILL_RECORD_VALUE_KEY_CREATE_TIME TEXT)")
        db?.execSQL("CREATE TABLE $DB_BILL_TOTAL_TABLE_NAME (" +
                "$BILL_TOTAL_VALUE_KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$BILL_TOTAL_VALUE_KEY_YEAR TEXT, " +
                "$BILL_TOTAL_VALUE_KEY_MONTH TEXT, " +
                "$BILL_TOTAL_VALUE_KEY_NUM TEXT, " +
                "$BILL_TOTAL_VALUE_KEY_MONEY TEXT, " +
                "$BILL_TOTAL_VALUE_KEY_CREATE_TIME TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
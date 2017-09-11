package com.lvzp.financecalculator.db.iml

import android.content.ContentValues
import android.content.Context
import com.lvzp.financecalculator.db.DBManager
import com.lvzp.financecalculator.db.SQLHelper


/**
 * 作者：吕振鹏
 * 创建时间：08月23日
 * 时间：23:11
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
class DBManagerIml(context: Context) : DBManager {


    private var mSQLHelper: SQLHelper? = null

    init {
        mSQLHelper = SQLHelper(context)
    }

    companion object {

        private var sSQLDBManager: DBManagerIml? = null

        fun getInstance(context: Context): DBManager {
            if (sSQLDBManager == null) {
                sSQLDBManager = DBManagerIml(context)
            }
            return sSQLDBManager!!
        }
    }


    override fun insert(tableName: String, values: Any?): Boolean {
        var isInsertSul = false
        if (mSQLHelper != null) {
            val cv = ContentValues()
            val aClass = values?.javaClass
            val fields = aClass?.declaredFields
            if (fields != null) {
                for (field in fields) {
                    field.isAccessible = true
                    val name = field.name
                    var value: Any? = null
                    if (name == "serialVersionUID") continue
                    try {
                        value = field.get(values)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (value != null) {
                        cv.put(name, value.toString())
                    }
                }
            }
            val database = mSQLHelper!!.writableDatabase
            database.beginTransaction()
            try {
                isInsertSul = database.insertOrThrow(tableName, null, cv) > -1
                database.setTransactionSuccessful()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                database.endTransaction()
            }

        }
        return isInsertSul
    }

    override fun update(tableName: String, values: Any?): Boolean {
        var isInsertSul = false
        val cv = ContentValues()
        val aClass = values?.javaClass
        val fields = aClass?.declaredFields
        if (fields != null) {
            for (field in fields) {
                field.isAccessible = true
                val name = field.name
                var value: Any? = null
                if (name == "serialVersionUID") continue
                try {
                    value = field.get(values)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (value != null) {
                    cv.put(name, value.toString())
                }
            }
        }
        if (mSQLHelper != null) {
            val db = mSQLHelper!!.writableDatabase
            isInsertSul = db.update(tableName, cv, DBManager.selection, DBManager.selectionArgs) > 0
            reset()
        }
        return isInsertSul
    }

    override fun delete(tableName: String): Boolean {
        return false
    }

    override fun <T> query(tableName: String, cls: Class<T>): ArrayList<T?> {
        val list = ArrayList<T?>()
        if (mSQLHelper != null) {
            val fields = cls.declaredFields
            val db = mSQLHelper!!.readableDatabase
            val query = db.query(tableName, DBManager.columns, DBManager.selection, DBManager.selectionArgs, null, null, DBManager.orderBy)
            reset()
            if (query != null && fields != null) {
                while (query.moveToNext()) {
                    var instance: T? = null
                    try {
                        instance = cls.newInstance()
                        for (field in fields) {
                            field.isAccessible = true
                            val fieldName = field.name
                            val value = query.getString(query.getColumnIndex(fieldName))
                            field.set(instance, value)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    list.add(instance)
                }
                query.close()
            }

        }
        return list
    }

    private fun reset() {
        DBManager.columns = arrayOf()
        DBManager.selection = ""
        DBManager.orderBy = ""
        DBManager.selectionArgs = arrayOf()
    }
}
package com.lvzp.financecalculator.db

/**
 * 作者：吕振鹏
 * 创建时间：08月23日
 * 时间：23:11
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
interface DBManager {

    companion object {
        var columns: Array<String?> = arrayOf()

        var selection: String = ""

        var selectionArgs: Array<String?> = arrayOf()

        var orderBy: String = ""
    }

    fun insert(tableName: String, values: Any?): Boolean

    fun update(tableName: String, values: Any?): Boolean

    fun delete(tableName: String): Boolean

    fun <T> query(tableName: String, cls: Class<T>): ArrayList<T?>
}
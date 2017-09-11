package com.lvzp.financecalculator.bean

/**
 * 作者：吕振鹏
 * 创建时间：08月27日
 * 时间：23:41
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
data class DBOrderInfoBean(var _id: String? = null,
                           var account: String? = null,
                           var repay_time: String? = null,
                           var repay_money: String? = null,
                           var remarks: String? = null,
                           var create_time: String? = null)
package com.lvzp.financecalculator.bean

/**
 * 作者：吕振鹏
 * 创建时间：08月22日
 * 时间：22:54
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
data class OrderBean(
        var price: String,
        var total: String,
        var month: String,
        var items: List<Items>? = null)

data class Items(var iconRes: Int,
                 var name: String,
                 var repayTime: String,
                 var money: String)
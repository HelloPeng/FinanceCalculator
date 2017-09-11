package com.lvzp.financecalculator.bean

import android.graphics.drawable.Drawable

/**
 * 作者：吕振鹏
 * 创建时间：08月22日
 * 时间：22:54
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
data class OrderBean(
        var price: String? = null,
        var total: String? = null,
        var month: String? = null,
        var items: List<Items>? = null)

data class Items(var iconRes: Drawable? = null,
                 var name: String? = null,
                 var repayTime: String? = null,
                 var money: String? = null)
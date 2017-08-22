package com.lvzp.financecalculator

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * 作者：吕振鹏
 * 创建时间：2017/6/14
 * 时间：9:57
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
class NestRecyclerView : RecyclerView {

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, expandSpec)
    }
}
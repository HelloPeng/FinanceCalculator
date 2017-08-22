package www.juyun.net.beedriver.base.mvp

import com.lvzp.financecalculator.base.mvp.BaseView
import java.lang.ref.WeakReference


/**
 * 作者：吕振鹏
 * 创建时间：06月11日
 * 时间：16:03
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
open class BasePresenter<V : BaseView> {

    private var mWeakReferenceView: WeakReference<V>? = null

    open fun attach(view: V) {
        mWeakReferenceView = WeakReference(view)
    }

    fun deAttach() {
        if (mWeakReferenceView != null) {
            mWeakReferenceView!!.clear()
            mWeakReferenceView = null
        }
    }

    fun getView(): V? {
        return if (mWeakReferenceView != null) mWeakReferenceView!!.get() else null
    }
}
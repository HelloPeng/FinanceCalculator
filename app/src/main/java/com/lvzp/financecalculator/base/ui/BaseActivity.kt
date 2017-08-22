package www.juyun.net.beedriver.base.ui

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.lvzp.financecalculator.R
import com.lvzp.statusbarlib.StatusBarCompat
import com.lvzp.statusbarlib.utils.SystemUtils
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.find
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColor
import www.juyun.net.beedriver.base.mvp.BasePresenter
import com.lvzp.financecalculator.base.mvp.BaseView


/**
 * 作者：吕振鹏
 * 创建时间：06月11日
 * 时间：13:03
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */

@SuppressLint("ShowToast")
abstract class BaseActivity<D : ViewDataBinding, V : BaseView, P : BasePresenter<V>> : AppCompatActivity(), BaseView {

    companion object {
        val DEFAULT_TITLE_TEXT_COLOR: Int = -1
    }

    //标题栏中的成员变量
    private var mRlAppTitleParent: RelativeLayout? = null
    private var mLlAppTitleLeft: LinearLayout? = null
    private var mIvAppTitleBack: ImageView? = null
    private var mTvAppTitleHint: TextView? = null
    private var mRlAppTitleContent: RelativeLayout? = null
    private var mTvAppTitleText: TextView? = null
    private var mLlAppTitleRight: LinearLayout? = null

    protected var mDataBinding: D? = null
    protected var mPresenter: P? = null
    protected var mContext: Context? = null
    private var mToast: Toast? = null

    /**
     * 设置标题栏的属性
     */
    protected abstract fun setupTitle()

    /**
     * 初始化基本控件
     */
    protected abstract fun initViews()

    /**
     * 获取布局文件
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * 创建View的控制器
     */
    protected abstract fun createPresenter(): P


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mDataBinding = setContentView()
        mPresenter = createPresenter()
        mPresenter?.attach(this as V)
        //在调用所有的方法之前，用来初始化一些成员变量
        initFieldBeforeMethods()
        initTitle()
        StatusBarCompat.with(this).setupTitleBar(mRlAppTitleParent).init()
        setupTitle()
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.deAttach()
        StatusBarCompat.with(this).destroy()
    }

    private fun initTitle() {
        mRlAppTitleParent = find(R.id.rl_app_title_parent)
        mLlAppTitleLeft = find(R.id.ll_app_title_left)
        mIvAppTitleBack = find(R.id.iv_app_title_back)
        mTvAppTitleHint = find(R.id.tv_app_title_hint)
        mRlAppTitleContent = find(R.id.rl_app_title_content)
        mTvAppTitleText = find(R.id.tv_app_title_text)
        mLlAppTitleRight = find(R.id.ll_app_title_right)
    }


    /**
     * 用来初始化一些成员变量，这个方法调用的时间在所有方法调用之前
     * 因为不一定所有的子类都需要设置这个方法，所以不写成为抽象的方法了
     */
    open protected fun initFieldBeforeMethods() {}

    /**
     * 通过DataBindingUtils填充页面布局并返回数据绑定对象

     * @return 当前页面数据绑定对象
     */
    protected fun setContentView(): D? {
        if (mDataBinding == null)
            return DataBindingUtil.setContentView(this, getLayoutId())
        return mDataBinding
    }


    /**
     * 关闭当前页面
     */
    protected fun openTitleLeftView(isDefault: Boolean): LinearLayout? {
        if (mLlAppTitleLeft == null) return null
        if (isDefault) {
            mLlAppTitleLeft?.visibility = View.VISIBLE
        }
        mLlAppTitleLeft?.setOnClickListener({
            onBackPressed()
        })

        return mLlAppTitleLeft
    }

    /**
     * 设置左侧图标

     * @param leftDrawable
     */
    protected fun setTitleLeftIcon(leftDrawable: Drawable) {
        ViewCompat.setBackground(mIvAppTitleBack, leftDrawable)
    }

    /**
     * 设置左侧图标

     * @param leftIconRes
     */
    protected fun setTitleLeftIconRes(@DrawableRes leftIconRes: Int) {
        mIvAppTitleBack?.imageResource = leftIconRes
    }

    /**
     * 设置左侧提示文字

     * @param text
     */
    protected fun setTitleLeftHintText(text: String) {
        mTvAppTitleHint?.text = text
    }


    /**
     * 设置标题的文字，如果想定制自己的颜色，可以设置[textColor]
     */
    protected fun setTitleText(text: String?, textColor: Int? = DEFAULT_TITLE_TEXT_COLOR) {
        if (mTvAppTitleText != null) {
            mTvAppTitleText?.text = text ?: ""
            if (textColor != DEFAULT_TITLE_TEXT_COLOR)
                mTvAppTitleText?.textColor = textColor!!
            else
                mTvAppTitleText?.textColor = ContextCompat.getColor(mContext, R.color.colorAppTitleColor)
        }
    }

    /**
     * 设置中间的控件
     * @param layoutRes  布局文件
     */
    protected fun setTitleCenterViewRes(@LayoutRes layoutRes: Int): View {
        val titleCenterView = LayoutInflater.from(mContext).inflate(layoutRes, null)
        setTitleCenterView(titleCenterView)
        return titleCenterView
    }

    /**
     * 设置中间的控件
     * @param view              具体填充的控件
     */
    protected fun setTitleCenterView(view: View) {
        if (mRlAppTitleContent == null) return
        if (view.layoutParams == null) {
            val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.rules[0] = RelativeLayout.CENTER_IN_PARENT
            view.layoutParams = lp
        }
        mRlAppTitleContent?.removeAllViews()
        mRlAppTitleContent?.addView(view)
    }

    /**
     * 显示Snackbar提示
     */
    override fun showSnackbar(message: String) {
        snackbar(mDataBinding!!.root, message)
    }

    /**
     * 显示Toast提示
     */
    override fun showToast(message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        } else {
            mToast?.setText(message)
            mToast?.duration = Toast.LENGTH_SHORT
        }
        mToast?.show()
    }

    fun cancelToast() {
        mToast?.cancel()
    }

    override fun getContent(): Context? {
        return mContext
    }

    override fun onBackPressed() {
        cancelToast()
        super.onBackPressed()
    }

    protected fun setTitleRightLayout(view: View) {
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        view.layoutParams = lp
        val padding = SystemUtils.getDip(mContext, 10.toFloat()).toInt()
        view.setPadding(padding, 0, padding, 0)
        mLlAppTitleRight?.addView(view)
    }

    protected fun setTitleRightLayout(a: () -> View) {
        setTitleRightLayout(a())
    }

    protected fun getTitleRightLayout(): LinearLayout? {
        return mLlAppTitleRight
    }

    /**
     * 屏幕变亮
     */
    fun lightOn() {
        val lp = window.attributes
        lp.alpha = 1.0f
        window.attributes = lp
    }

    /**
     * 屏幕变暗
     */
    fun lightOff() {
        val lp = window.attributes
        lp.alpha = 0.3f
        window.attributes = lp
    }
}
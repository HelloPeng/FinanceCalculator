package com.lvzp.statusbarlib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lvzp.statusbarlib.utils.BarConfig;
import com.lvzp.statusbarlib.utils.OSUtils;
import com.lvzp.statusbarlib.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：吕振鹏
 * 创建时间：06月10日
 * 时间：16:41
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */

public class StatusBarCompat {

    private static final String TAG = StatusBarCompat.class.toString();
    private static Map<String, View> mMap = new HashMap<>();
    private static List<String> mFragmentList = new ArrayList<>();
    private String mActivityName;
    private String mFragmentName;
    private Activity mActivity;
    private Window mWindow;
    private ViewGroup mViewGroup;
    private BarConfig mConfig;
    private View mStatusView;
    private boolean isDarkFont;
    private View mTitleBarView;
    private int mStatusBarColor;
    private float mStatusBarAlpha;


    private StatusBarCompat(Activity activity) {
        mActivityName = activity.getClass().getName();
        initDefaultValue(activity, mActivityName);
    }


    private StatusBarCompat(Fragment fragment) {
        mActivityName = fragment.getActivity().getClass().getName();
        mFragmentName = mActivityName + "_and_" + fragment.getClass().getName();
        if (!mFragmentList.contains(mFragmentName))
            mFragmentList.add(mFragmentName);
        initDefaultValue(fragment.getActivity(), mFragmentName);
    }

    private void initDefaultValue(Activity activity, String name) {
        mActivity = activity;
        mWindow = mActivity.getWindow();
        mViewGroup = (ViewGroup) mWindow.getDecorView();
        mConfig = new BarConfig(activity);
        if (!mMap.isEmpty() && !name.isEmpty()) {
            if (mMap.get(name) == null) {
                if (mFragmentName != null) { //保证一个activity页面有同一个状态栏view和导航栏view
                    mStatusView = mMap.get(mActivityName);
                }
                mMap.put(name, mStatusView);
            } else {
                mStatusView = mMap.get(name);
            }
        } else {
            mStatusView = new View(activity);
            if (mFragmentName != null) {  //保证一个activity页面有同一个状态栏view和导航栏view
                mStatusView = mMap.get(mActivityName);
            }
            mMap.put(name, mStatusView);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int initBarAboveLOLLIPOP(int uiFlags, int color, boolean isLightColor) {
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;  //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
        if (isLightColor && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            color = ColorUtils.blendARGB(color, Color.BLACK, 0.4f);
        }
        mWindow.setStatusBarColor(color);  //设置状态栏颜色
        return uiFlags;
    }

    /**
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initBarBelowLOLLIPOP(int color, boolean isLightColor) {
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        //设置一个假的状态栏
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        if (!mConfig.isNavigationAtBottom()) {
            params.rightMargin = mConfig.getNavigationBarWidth();
        }
        mStatusView.setLayoutParams(params);
        if (isLightColor) {
            color = ColorUtils.blendARGB(color, Color.BLACK, 0.4f);
        }
        mStatusView.setBackgroundColor(color);
        mStatusView.setVisibility(View.VISIBLE);
        ViewGroup viewGroup = (ViewGroup) mStatusView.getParent();
        if (viewGroup != null)
            viewGroup.removeView(mStatusView);
        mViewGroup.addView(mStatusView);
    }

    /**
     * 初始化状态栏
     *
     * @param isLightColor 状态栏的颜色是否为高亮色，如果是高亮色就应该将字体设置为黑色
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initBar(int color, boolean isLightColor) {
        if (isDarkFont) {
            isLightColor = true;
        }
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;  //防止系统栏隐藏时内容区域大小发生变化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
                uiFlags = initBarAboveLOLLIPOP(uiFlags, color, isLightColor); //初始化5.0以上，包含5.0
                //fitsSystemWindows();  //android 5.0以上解决状态栏和布局重叠问题
            } else {
                initBarBelowLOLLIPOP(color, isLightColor); //初始化5.0以下，4.4以上沉浸式
            }
            uiFlags = SystemUtils.setStatusBarDarkFont(uiFlags, isLightColor); //android 6.0以上设置状态栏字体为暗色
        }
        mWindow.getDecorView().setSystemUiVisibility(uiFlags);
        if (OSUtils.isMIUI6More())
            SystemUtils.setMiuiStatusBarDarkMode(mActivity, isLightColor);         //修改miui状态栏字体颜色
        else if (OSUtils.isFlymeOS4More()) {          // 修改Flyme OS状态栏字体颜色
            SystemUtils.setMeizuStatusBarDarkIcon(mActivity, isLightColor);
        }
    }

    public static StatusBarCompat with(Activity activity) {
        return new StatusBarCompat(activity);
    }

    public static StatusBarCompat with(Fragment fragment) {
        return new StatusBarCompat(fragment);
    }

    public StatusBarCompat setupTitleBar(View title) {
        if (title == null) return this;
        mTitleBarView = title;
        ColorDrawable drawable = (ColorDrawable) mTitleBarView.getBackground();
        if (drawable == null) {
            drawable = new ColorDrawable();
        }
        mStatusBarColor = drawable.getColor();
        mStatusBarAlpha = title.getAlpha();
        setTitleBar();
        return this;
    }

    public void init() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
        //根据传入的透明度，重新计算颜色值
        mStatusBarColor = Color.argb((int) mStatusBarAlpha, Color.red(mStatusBarColor), Color.green(mStatusBarColor), Color.blue(mStatusBarColor));
        boolean isLightColor = isStatusBarLight(mStatusBarColor);
        initBar(mStatusBarColor, isLightColor);
    }

    /**
     * 重新绘制标题栏高度，解决状态栏与顶部重叠问题
     * Sets title bar.
     */
    private void setTitleBar() {
        ViewGroup.LayoutParams layoutParams = mTitleBarView.getLayoutParams();
        //确定是否为状态栏留有空隙了
        if (layoutParams.height != layoutParams.height + mConfig.getStatusBarHeight())
            layoutParams.height = layoutParams.height + mConfig.getStatusBarHeight();
        mTitleBarView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
        mTitleBarView.setLayoutParams(layoutParams);

    }

    /**
     * 会根据设置进来的颜色自动变色（并且如果状态栏的颜色为高亮时，会在MiUi或者是魅族手机或者6.0以上的手机设置白底黑字）
     *
     * @param color 要设置的颜色
     * @param alpha 颜色的透明度
     */
    public StatusBarCompat setStatusBarColor(int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        mStatusBarColor = color;
        mStatusBarAlpha = alpha * 255;
        return this;
    }

    public StatusBarCompat statusBarDarkFont(boolean isDarkFont) {
        this.isDarkFont = isDarkFont;
        return this;
    }

    public boolean isStatusBarLight(int color) {
        float blue = Color.blue(color);
        float red = Color.red(color);
        float green = Color.green(color);

        double grayLevel = red * 0.299 + green * 0.587 + blue * 0.114;
        //通过RGB的颜色计算，判断是否属于浅色，当状态栏为浅色时设置字体颜色为深色
        //通过把 RGB 模式转换成 YUV 模式，而 Y 是明亮度（灰阶），因此只需要获得 Y 的值
        return grayLevel >= 132;
    }

    /**
     * 当Activity关闭的时候，在onDestroy方法中调用
     */
    public void destroy() {
        if (mActivityName != null) {
            if (mFragmentList.size() > 0) {
                for (String name : mFragmentList) {
                    if (name.contains(mActivityName))
                        mMap.remove(name);
                }
            }
            mMap.remove(mActivityName);
        }
    }

}

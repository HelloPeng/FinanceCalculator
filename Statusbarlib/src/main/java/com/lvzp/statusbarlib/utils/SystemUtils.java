package com.lvzp.statusbarlib.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者：吕振鹏
 * 创建时间：06月10日
 * 时间：16:51
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */

public class SystemUtils {


    public static int getStatusBarHeight(Context context) {
        BarConfig config = new BarConfig(context);
        return config.getStatusBarHeight();
    }

    public static float getDip(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

    public static int[] getScreenSize(Context context) {
        int[] screenSize = new int[2];
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenSize[0] = displayMetrics.widthPixels;//宽
        screenSize[1] = displayMetrics.heightPixels;//高
        return screenSize;
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    public static int setStatusBarDarkFont(int uiFlags, boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && darkMode) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * 修改小米状态栏的白底黑字状态
     *
     * @param activity
     * @param darkMode
     * @return
     */
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkMode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkMode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改魅族状态栏的白底黑字状态
     *
     * @param activity
     * @param darkMode
     * @return
     */
    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean darkMode) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkMode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }


}

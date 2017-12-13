package com.tylz.wechat.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tylz.wechat.R;

/**
 * @author cxw
 * @date 2017/12/6
 * @des 自定义圆角的dialog
 */

public class CustomDialog extends Dialog {

    /**
     * 宽高由布局文件中指定（但是最底层的宽度无效，可以多嵌套一层解决）
     */
    public CustomDialog(@NonNull Context context, View layout, int themeResId) {
        super(context, themeResId);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    /**
     * 宽高由该方法的参数设置
     */
    public CustomDialog(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        // 设置窗口属性
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置宽度、高度、密度、对齐方式
        float density = getDensity(context);
        params.width = (int) (width * density);
        params.height = (int) (height * density);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
    /**
     * 获取显示密度
     *
     * @param context
     * @return
     */
    public float getDensity(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        return dm.density;
    }
}

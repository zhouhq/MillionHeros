package android.masterfzu.millionheros.util;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/1/19.
 */

public class ScreenUtils {
    public static DisplayMetrics getMetrics(Context ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        if(ctx == null) {
            Log.e("ScreenUtil.getMetrics", "ApplicationContext is null!");
            return metrics;
        } else {
            WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            display.getMetrics(metrics);
            boolean isPortrait = display.getWidth() < display.getHeight();
            int width = isPortrait?display.getWidth():display.getHeight();
            int height = isPortrait?display.getHeight():display.getWidth();
            metrics.widthPixels = width;
            metrics.heightPixels = height;
            return metrics;
        }
    }

    public static float getDensity(Context context) {
        DisplayMetrics metrics = getMetrics(context);
        return metrics.density;
    }
    public static float getScreenWidth(Context context) {
        DisplayMetrics metrics = getMetrics(context);
        return metrics.widthPixels;
    }
    public static float getScreenHeight(Context context) {
        DisplayMetrics metrics = getMetrics(context);
        return metrics.heightPixels;
    }
}

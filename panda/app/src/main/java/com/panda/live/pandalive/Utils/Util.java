package com.panda.live.pandalive.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;

/**
 * Created by Android Studio on 3/11/2018.
 */

public class Util {
    private static final String PREFERENCES_FILE = "materialsample_settings";


//    public static int getToolbarHeight(Context context) {
//        int height = (int) context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
//        return height;
//    }
//
//    public static int getStatusBarHeight(Context context) {
//        int height = (int) context.getResources().getDimension(R.dimen.statusbar_size);
//        return height;
//    }
//
//
//    public static Drawable tintMyDrawable(Drawable drawable, int color) {
//        drawable = DrawableCompat.wrap(drawable);
//        DrawableCompat.setTint(drawable, color);
//        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
//        return drawable;
//    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}

package com.panda.live.pandalive.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Android Studio on 3/11/2018.
 */

public class SetTypeUtil {
    public static void setTypeFaceTextView(Context context, TextView textView, String type) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), type);
        textView.setTypeface(typeface);
    }
}

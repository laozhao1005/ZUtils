package com.kit.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by Zhao on 16/7/17.
 */
public class ResWrapper {
    private Context context;

    private static ResWrapper resWrapper;

    public static ResWrapper getInstance() {

        if (resWrapper == null)
            resWrapper = new ResWrapper();

        return resWrapper;
    }


    public ResWrapper setContext(Context context) {
        this.context = context;
        return resWrapper;
    }

    public Context getContext() {
        return context.getApplicationContext();
    }

    public void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }


    public int getColor(int colorId) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            return context.getResources().getColor(colorId, null);
        else
            return ContextCompat.getColor(context, colorId);
    }


    public String getString(int stringId) {
        return context.getResources().getString(stringId);
    }


    public String[] getStringArray(int stringId) {
        return context.getResources().getStringArray(stringId);
    }


    public String getText4ResStringArray(int intR) {
        String[] hibernate_copy = context.getResources().getStringArray(intR);
        String textStr = (String) ArrayUtils.getOne(hibernate_copy);
        return textStr;

    }


}

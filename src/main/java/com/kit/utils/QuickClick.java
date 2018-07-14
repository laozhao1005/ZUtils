package com.kit.utils;

import android.support.annotation.Nullable;

import com.kit.app.core.task.DoSomeThing;

public class QuickClick {


    public static boolean isQuick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    public static void check(@Nullable DoSomeThing doSomeThing) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;

        if (flag && doSomeThing != null) {
            doSomeThing.execute();
        }
    }


    public void inspect(@Nullable DoSomeThing doSomeThing) {
        check(doSomeThing);
    }

    public QuickClick limit(int time) {
        MIN_CLICK_DELAY_TIME = time;
        return get();
    }


    public QuickClick get() {
        if (quickClick == null) {
            quickClick = new QuickClick();
        }
        return quickClick;
    }

    /**
     * 两次点击按钮之间的点击间隔不能少于1000毫秒
     */
    private static QuickClick quickClick;


    private static int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    private QuickClick() {
    }

}
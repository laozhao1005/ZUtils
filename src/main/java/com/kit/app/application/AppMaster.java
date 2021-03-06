/*
 * Copyright (c) 2018.
 * Author：Zhao
 * Email：joeyzhao1005@gmail.com
 */

package com.kit.app.application;

import android.content.Context;
import android.content.pm.ActivityInfo;

/**
 * Created by joeyzhao on 2018/3/16.
 */

public class AppMaster implements IApp {


    @Override
    public void restartApp() {

        if (app == null) {
            return;
        }
        app.restartApp();
    }

    @Override
    public boolean isDebug() {
        if (app == null) {
            return false;
        }
        return app.isDebug();
    }

    @Override
    public String getAppName() {
        if (app == null) {
            return null;
        }
        return app.getAppName();
    }

    @Override
    public Context getAppContext() {
        if (app == null) {
            return null;
        }
        return app.getAppContext();
    }

    @Override
    public String getApplicationId() {
        if (app == null) {
            return null;
        }
        return app.getApplicationId();
    }

    @Override
    public String getFlavor() {
        if (app == null) {
            return null;
        }
        return app.getFlavor();
    }

    @Override
    public String getVersionName() {
        if (app == null) {
            return null;
        }
        return app.getVersionName();
    }

    @Override
    public long getVersionCode() {
        if (app == null) {
            return 0;
        }
        return app.getVersionCode();
    }

    public IApp getApp() {
        return app;
    }

    public void setApp(IApp app) {
        this.app = app;
    }

    private IApp app;


    private static AppMaster mInstance;

    private AppMaster() {
    }

    public static AppMaster getInstance() {
        if (mInstance == null) {
            mInstance = new AppMaster();
        }
        return mInstance;
    }
}

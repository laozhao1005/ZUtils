package com.kit.app.application;

import android.content.Context;

public interface IApp {
    String getAppName();
    Context getAppContext();
    String getApplicationId();
    String getVersionName();
    long getVersionCode();

    String getFlavor();
    boolean isDebug();
    void restartApp();
}

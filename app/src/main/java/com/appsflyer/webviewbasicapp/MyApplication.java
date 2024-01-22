package com.appsflyer.webviewbasicapp;
import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().init(getString(R.string.af_dev_key), null, this);
        AppsFlyerLib.getInstance().start(this);
    }
}

package com.appsflyer.webviewbasicapp;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.appsflyer.AppsFlyerLib;


public class MainJsInterface {
    Context mContext;

    MainJsInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void recordEvent(String name, String json){
        Map<String, Object> params = null;
        if(json!=null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                params = new HashMap<>();
                Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Object value = jsonObject.opt(key);
                    params.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        AppsFlyerLib.getInstance().logEvent(this.mContext, name, params);
    }
}

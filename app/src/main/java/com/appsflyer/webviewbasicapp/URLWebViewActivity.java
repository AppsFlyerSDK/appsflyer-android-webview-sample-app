package com.appsflyer.webviewbasicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import com.appsflyer.AppsFlyerLib;

public class URLWebViewActivity extends AppCompatActivity {
    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlweb_view);

        // WebView setting
        webView = (WebView) findViewById(R.id.url_webview);
        webView.setWebViewClient(new myWebClient());

        // check if PBA Web SDK is started
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);

        // add custom value to userAgent to identify Android WebView environment
        String userAgent = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent+"/Android_WebView");

        webView.loadUrl("https://appsflyersdk.github.io/webview-http-sample-page/urlloading.html");
    }

    public class myWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.startsWith("af-event://")) {
                String[] urlParts = url.split("\\?");
                if (urlParts.length > 1) {
                    String query = urlParts[1];
                    String eventName = null;
                    HashMap<String, Object> eventValue = new HashMap<>();

                    for (String param : query.split("&")) {
                        String[] pair = param.split("=");
                        String key = pair[0];
                        if (pair.length > 1) {
                            if ("eventName".equals(key)){
                                eventName = pair[1];
                            } else if ("eventValue".equals(key)){
                                JSONObject event;
                                JSONArray keys;
                                try {
                                    event = new JSONObject(pair[1]);
                                    keys = event.names();
                                    for (int i = 0; i < keys.length(); i++){
                                        eventValue.put(keys.getString(i), event.getString(keys.getString(i)));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    AppsFlyerLib.getInstance().logEvent(getApplicationContext(),eventName,eventValue);
                }
                return true;
            }
            view.loadUrl(url);
            return true;
        }
    }
}
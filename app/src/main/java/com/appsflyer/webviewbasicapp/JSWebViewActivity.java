package com.appsflyer.webviewbasicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JSWebViewActivity extends AppCompatActivity {

    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsweb_view);

        // WebView setting
        webView = (WebView) findViewById(R.id.js_webview);
        webView.setWebViewClient(new WebViewClient());

        // check if PBA Web SDK is started
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MainJsInterface(getApplicationContext()), "app");

        webView.loadUrl("https://appsflyersdk.github.io/webview-http-sample-page/jsinterface.html");
    }
}



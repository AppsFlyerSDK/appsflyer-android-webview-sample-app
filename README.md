
# 🤖 AppsFlyer Android WebView Sample App

This repository demonstrates how to **send in-app events from a WebView inside an Android hybrid app** using the [AppsFlyer Android SDK](https://support.appsflyer.com/hc/en-us/articles/207032066-Basic-SDK-integration-guide) and prevent duplicate reporting when used together with the [PBA Web SDK](https://support.appsflyer.com/hc/en-us/articles/360001610038-PBA-Web-SDK-integration-guide).

It supports two methods of integrating WebView in-app event tracking:

- **JavaScript Interface injection**
- **URL loading interception**

---

## 🔧 Prerequisites

- Read: [In-app events for hybrid apps — AppsFlyer Support](https://support.appsflyer.com/hc/en-us/articles/207031976-In-app-events-for-hybrid-apps)
- Basic integration of the [AppsFlyer Android SDK](https://support.appsflyer.com/hc/en-us/articles/207032066-Basic-SDK-integration-guide#starting-the-sdk-in-android)
- Hybrid mobile app architecture using WebView
- Sample WebView HTML pages:  
  https://github.com/AppsFlyerSDK/webview-http-sample-page

---

## 📁 File Overview

| File / Class | Description |
|--------------|-------------|
| `MainActivity.java` | Entry point for the app; allows selection between JS interface and URL loading WebView samples |
| `MainJsInterface.java` | Handles JavaScript interface calls (via `window.app`) and passes events to AppsFlyer SDK |
| `URLWebViewActivity.java` | Configures WebView to intercept `af-event://` URLs and adds custom `/Android_WebView` string to the user agent |
---

## ⚙️ Runtime Behavior

### JS Interface Method (`jsinterface.html`)

- `window.app.recordEvent()` is triggered from the WebView
- Android SDK receives the event through `MainJsInterface`
- Web SDK is **not initialized** if the JS interface (`window.app`) is present

```java
webView.addJavascriptInterface(new MainJsInterface(getApplicationContext()), "app");
```

---

### URL Loading Method (`urlloading.html`)

- WebView creates a custom URL:

```js
iframe.setAttribute("src", "af-event://inappevent?eventName=...&eventValue=...");
```

- `URLWebViewActivity` sets a custom WebViewClient to intercept this scheme
- It also appends `/Android_WebView` to the user agent for filtering Web SDK execution

```java
String userAgent = webView.getSettings().getUserAgentString();
webView.getSettings().setUserAgentString(userAgent + "/Android_WebView");
```

- Web SDK reads the UA and skips initialization if it detects `Android_WebView`

---

## ✅ Duplicate Event Prevention

This project ensures that:

- In-app events from WebView are sent **only via the native Android SDK**
- Web SDK is **not initialized** in WebView environments:
  - JS Interface detected (`window.app`)
  - User Agent includes `/Android_WebView`

Client-side event duplication is effectively avoided by separating SDK responsibilities.

---

## 🧭 In-App Event Propagation Flow

```
┌────────────────────┐
│   In-app event     │
└─────────┬──────────┘
          │
          ▼
┌──────────────────────────────┐
│ WebView with native SDK?     │───────────┐
└──────────┬───────────────────┘           │
           │ Yes                           │ No
           ▼                               ▼
┌──────────────────────┐     ┌──────────────────────────┐
│ Invoke via JS        │     │ Initialize PBA Web SDK   │
│ interface or         │     └─────────────┬────────────┘
│ URL loading          │                   ▼
└──────────┬───────────┘     ┌──────────────────────────┐
           ▼                 │ Track event with         │
┌──────────────────────┐     │ Web SDK                  │
│ Android SDK receives │     └─────────────┬────────────┘
└──────────┬───────────┘                   ▼
           ▼                 ┌──────────────────────────┐
┌──────────────────────┐     │ Send in-app event to     │
│ Send in-app event to │     │ AppsFlyer                │
│ AppsFlyer            │     └──────────────────────────┘
└──────────────────────┘
```

---

## 📎 Related Docs

- [Android SDK Integration Guide](https://dev.appsflyer.com/hc/docs/android-sdk)  
- [In-App Events in Hybrid Apps](https://support.appsflyer.com/hc/en-us/articles/207031976-In-app-events-for-hybrid-apps)  
- [PBA Web SDK Integration Guide](https://support.appsflyer.com/hc/en-us/articles/360001610038-PBA-Web-SDK-integration-guide)

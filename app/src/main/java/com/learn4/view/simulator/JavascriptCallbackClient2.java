package com.learn4.view.simulator;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class JavascriptCallbackClient2 {

    private Context mContext;
    private WebView webView;
    private Activity activity;

    private String testString;

    public JavascriptCallbackClient2(Activity activity, Context context, WebView webView, String stringText) {
        this.mContext = context;
        this.webView = webView;

        this.testString = stringText;

        this.activity = activity;
    }

    private String publishEvent(String functionName, String data) {
        StringBuffer buffer = new StringBuffer()
                .append("window.dispatchEvent(\n")
                .append("   new CustomEvent(\"").append(functionName).append("\", {\n")
                .append("           detail: {\n")
                .append("               data: ").append(data).append("\n")
                .append("           }\n")
                .append("       }\n")
                .append("   )\n")
                .append(");");
        return buffer.toString();
    }

    @JavascriptInterface
    public void showToastMessage(final String message) {
        Log.e("testtest", message);

        ((TeachableActivity)activity).testSetting(message);
//        try {
//            Log.e("testtestt", "message");
//            //testListener = new TeachableActivity();
//            //teachableActivity.tsetSetting(message);
//            testListener.setting(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @JavascriptInterface
    public void callJavaScriptFunction() {
        webView.postDelayed(() -> {
            webView.evaluateJavascript(publishEvent("javascriptFunction", "\"" + testString + "\""),
                    (result) -> {}
            );
        }, 0);
    }
}

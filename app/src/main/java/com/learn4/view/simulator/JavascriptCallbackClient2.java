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

    private String testString;

    public JavascriptCallbackClient2(Context activity, WebView webView, String stringText) {
        this.mContext = activity;
        this.webView = webView;

        this.testString = stringText;
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
        //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Log.e("testtestt", message);
    }

    @JavascriptInterface
    public void callJavaScriptFunction() {
        webView.postDelayed(() -> {
            webView.evaluateJavascript(publishEvent("javascriptFunction", "\"" + testString + "\""),
                    (result) -> {
                        //Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                    }
            );
        }, 0);
    }
}

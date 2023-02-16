package com.learn4.view.simulator;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class JavascriptCallbackClient {

    private Context mContext;
    private WebView webView;
    private Button button;
    private String code;

    public JavascriptCallbackClient(Context context, WebView webView, String code) {
        this.mContext = context;
        this.webView = webView;
        this.code = code;
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
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void callBtnScriptFunction() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("hi","callBtnScriptFunction()");
                webView.postDelayed(() -> {
                webView.evaluateJavascript(publishEvent("javascriptFunction", "No more color"),
                        (result) -> {
                            Toast.makeText(mContext, "button click!" + result, Toast.LENGTH_SHORT).show();
                        }
                );

                }, 5000);
            }
        });
    }


    @JavascriptInterface
    public void callJavaScriptFunction() {
        Log.e("hi","callJavaScriptFunction()");

//        code = " void setup() {\n" +
//                "   pinMode(7, OUTPUT); \n" +
//                " }\n" +
//                "\n" +
//                " void loop() {\n" +
//                "   digitalWrite(7, HIGH);\n" +
//                "   delay(5000);\n" +
//                "   digitalWrite(7, LOW);\n" +
//                "   delay(5000);\n" +
//                " }";

        webView.postDelayed(() -> {
            webView.evaluateJavascript(publishEvent("javascriptFunction", "\""+code.replace("\n","")+"\""),
                    (result) -> {
                        Toast.makeText(mContext, "in "+result, Toast.LENGTH_SHORT).show();
                    }
            );
        }, 5000);
    }
}
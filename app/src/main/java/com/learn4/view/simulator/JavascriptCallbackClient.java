package com.learn4.view.simulator;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class JavascriptCallbackClient {

    private Context mContext;
    private WebView webView;
    private Button button;
    private String code;
    private String chapter_id;
    private String hex2str = "";
    private TextView serial_monitor;
    private StringBuffer serial_text = new StringBuffer("");
    int count = 0;
    String str="";


    public JavascriptCallbackClient(Context context, WebView webView,TextView serial_monitor,String chapter_id, String code) {
        this.mContext = context;
        this.webView = webView;
        this.chapter_id = chapter_id;
        this.code = code;
        this.serial_monitor = serial_monitor;
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

        Log.e("buffer",buffer.toString());
        return buffer.toString();
    }

    @JavascriptInterface
    public void showToastMessage(final String message) {
        Log.e("message",message);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }


    @JavascriptInterface
    public void showToastLog(final String message)
    {
        str += message+"\n";
        if (count > 5){
            serial_monitor.setText(str.substring(str.indexOf("\n"),str.length()));
        }else{
            serial_monitor.setText(str);
            count++;
        }


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
        Log.e("code", code);
        int delay = 0;

        if (!code.contains("contents_id:")){

            hex2str = convertString2Hex(chapter_id+"@@"+code);
        }else{
            hex2str = code;
        }
        //  "\""+code.replace("\n","").getBytes()+"\""
        webView.postDelayed(() -> {
            webView.evaluateJavascript(publishEvent("javascriptFunction", "\""+hex2str+"\""),
                    (result) -> {
                        Toast.makeText(mContext, "in "+result, Toast.LENGTH_SHORT).show();
                    }
            );
        }, 0);
    }

    private String convertString2Hex(String str){
        StringBuilder stringBuilder = new StringBuilder();

        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            stringBuilder.append(charToHex);
        }
        return  stringBuilder.toString();
    }
}
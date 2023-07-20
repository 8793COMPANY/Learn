package com.learn4.view.simulator;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.learn4.util.Application;

public class JavascriptCallbackClient2 {

    private Context mContext;
    private WebView webView;
    private Activity activity;
    private String testString;

    String [] send_data = {"a","b","c","d","e","f","g"};

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
    public void showToastMessage(final String message, final String link) {
        Log.e("testtestt", message);

        String [] lists = message.split("\n");
        int max_index =0;
        double max =0;
        for (int i=0; i<lists.length; i++){
            double current_per = Double.parseDouble(lists[i].split(":")[1].replace("%","").trim());
            if (max < current_per){
                max_index = i;
                max = current_per;
            }
        }

        Log.e("list "+max_index,max+"");

        serial_write(send_data[max_index]+"");

        ((TeachableActivity)activity).testSetting(message, link);
    }

    @JavascriptInterface
    public void callJavaScriptFunction() {
        webView.postDelayed(() -> {
            webView.evaluateJavascript(publishEvent("javascriptFunction", "\"" + testString + "\""),
                    (result) -> {}
            );
        }, 0);
    }

    //serial.write 기능
    public void serial_write(String str){
        Application.mPhysicaloid.open();
        Log.e("open! check",Application.mPhysicaloid.isOpened()+"");
        if (Application.mPhysicaloid.isOpened()) {
            Log.e("open!","isOpen()");
//            if(Application.mPhysicaloid.open()) {
            Log.e("open!","physicaloid");
            byte[] buf = str.trim().getBytes();
            Application.mPhysicaloid.write(buf, buf.length);
            Application.mPhysicaloid.close();
//            }
        }
    }
}
package com.learn4.view.simulator;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.learn4.R;
import com.learn4.view.simulator.JavascriptCallbackClient;

public class SimulatorDialog extends Dialog {

    Button upload_btn;

    private View.OnClickListener Confirm_Btn;
    private View.OnClickListener Cancel_Btn;
    TextView textView;
    private String title;
    Context context;


    public SimulatorDialog(@NonNull Context context, String text) {
        super(context);
        this.context = context;
        title = text;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_simulator);
        WebView webView = findViewById(R.id.webview);
        Button upload_btn = findViewById(R.id.send_button);


       upload_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.e("title",title);
               title="hi";
               webView.addJavascriptInterface(new JavascriptCallbackClient(context, webView,title.replace("\n","")),"android");
               webView.loadUrl("http://192.168.0.5:3000/");
           }
       });
//
//        arduino_code.setText(" void setup() {\n" +
//                "   pinMode(7, OUTPUT); \n" +
//                " }\n" +
//                "\n" +
//                " void loop() {\n" +
//                "   digitalWrite(7, HIGH);\n" +
//                "   delay(5000);\n" +
//                "   digitalWrite(7, LOW);\n" +
//                "   delay(5000);\n" +
//                " }");


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://192.168.0.5:3000/");

//        confirm_btn.setOnClickListener(Confirm_Btn);
//        if (Cancel_Btn != null) {
//            cancel_btn.setOnClickListener(Cancel_Btn);
//        } else {
//            cancel_btn.setVisibility(View.GONE);
//        }

    }


}

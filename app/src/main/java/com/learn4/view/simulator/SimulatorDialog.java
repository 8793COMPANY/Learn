package com.learn4.view.simulator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.learn4.R;
import com.learn4.util.MySharedPreferences;
import com.learn4.view.simulator.JavascriptCallbackClient;

import java.io.File;

public class SimulatorDialog extends Dialog {

    Button upload_btn;

    private View.OnClickListener Confirm_Btn;
    private View.OnClickListener Cancel_Btn;
    TextView textView;
    private String title;
    private String chapter_id, contents_name;
    Context context;
    Button component_close_btn;
    WebView webView;


    public ValueCallback<Uri> filePathCallbackNormal;
    public ValueCallback<Uri[]> filePathCallbackLollipop;
    public final static int FILECHOOSER_NORMAL_REQ_CODE = 2001;
    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2002;
    private Uri cameraImageUri = null;



    public SimulatorDialog(@NonNull Context context, String chapter_id, String contents_name, String text) {
        super(context);
        this.context = context;
        this.chapter_id = chapter_id;
        title = text;
        this.contents_name = contents_name;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_simulator);
        webView = findViewById(R.id.simulator_web_view);
        Button upload_btn = findViewById(R.id.upload_btn);
        TextView code_view  = findViewById(R.id.code_view);
        TextView hint_text  = findViewById(R.id.hint_text);
        TextView loading_text = findViewById(R.id.loading_text);
        LinearLayout code_upload_progress = findViewById(R.id.code_upload_progress);
        component_close_btn = findViewById(R.id.component_close_btn);

        webView.getSettings().setDefaultTextEncodingName("UTF-8");

        code_view.setMovementMethod(new ScrollingMovementMethod());

        upload_btn.setSelected(true);

        if (chapter_id.equals("3-2")){
            hint_text.setText("LED = 8번, 시간 = 1초");
        }else if (chapter_id.equals("3-3")){
            hint_text.setText("LED = 8번, 시간 = 0.5초");
        }else if (chapter_id.equals("3-4")){
            hint_text.setText("LED = 13번, 시간 = 1초");
        }else if (chapter_id.equals("5-2")){
            hint_text.setText("스위치 = 7번");
        }else if (chapter_id.equals("5-3")){
            hint_text.setText("스위치 = 5번");
        }else if (chapter_id.equals("7-2")){
            hint_text.setText("3색 LED");
        }else if (chapter_id.equals("25-2")) {
            hint_text.setText("서보모터");
        }else if (chapter_id.equals("27-2")) {
            hint_text.setText("불꽃감지센서");
        }else{
            hint_text.setText("스위치 = 5번, LED = 10번");
        }

        component_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {
//            // For Android < 3.0
//            public void openFileChooser( ValueCallback<Uri> uploadMsg) {
//                Log.d("MainActivity", "3.0 <");
//                openFileChooser(uploadMsg, "");
//            }
//
//            // For Android 3.0+
//            public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType) {
//                Log.d("MainActivity", "3.0+");
//                m_oInstance.filePathCallbackNormal = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                m_oInstance.startActivityForResult(Intent.createChooser(i, "File Chooser"), m_oInstance.FILECHOOSER_NORMAL_REQ_CODE);
//            }
//
//            // For Android 4.1+
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                Log.d("MainActivity", "4.1+");
//                openFileChooser(uploadMsg, acceptType);
//            }

            // For Android 5.0+
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                Log.d("MainActivity", "5.0+");

                // Callback 초기화 (중요!)
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop.onReceiveValue(null);
                    filePathCallbackLollipop = null;
                }
                filePathCallbackLollipop = filePathCallback;

                boolean isCapture = fileChooserParams.isCaptureEnabled();
                runCamera(isCapture);
                return true;
            }
        });


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                int errorCode = error.getErrorCode();
                Log.e("errorCode",errorCode+"");


                loading_text.setVisibility(View.GONE);
                webView.loadData("현재 사용이 불가능합니다.","text/html; charset=utf-8","UTF-8");


//                switch (errorCode) {
//                    case ERROR_AUTHENTICATION:
//                        break;               // 서버에서 사용자 인증 실패
//                    case ERROR_BAD_URL:
//                        break;                           // 잘못된 URL
//                    case ERROR_CONNECT:
//                        break;                          // 서버로 연결 실패
//                    case ERROR_FAILED_SSL_HANDSHAKE:
//                        break;    // SSL handshake 수행 실패
//                    case ERROR_FILE:
//                        break;                                  // 일반 파일 오류
//                    case ERROR_FILE_NOT_FOUND:
//                        break;               // 파일을 찾을 수 없습니다
//                    case ERROR_HOST_LOOKUP:
//                        break;           // 서버 또는 프록시 호스트 이름 조회 실패
//                    case ERROR_IO:
//                        break;                              // 서버에서 읽거나 서버로 쓰기 실패
//                    case ERROR_PROXY_AUTHENTICATION:
//                        break;   // 프록시에서 사용자 인증 실패
//                    case ERROR_REDIRECT_LOOP:
//                        break;               // 너무 많은 리디렉션
//                    case ERROR_TIMEOUT:
//                        break;                          // 연결 시간 초과
//                    case ERROR_TOO_MANY_REQUESTS:
//                        break;     // 페이지 로드중 너무 많은 요청 발생
//                    case ERROR_UNKNOWN:
//                        break;                        // 일반 오류
//                    case ERROR_UNSUPPORTED_AUTH_SCHEME:
//                        break; // 지원되지 않는 인증 체계
//                    case ERROR_UNSUPPORTED_SCHEME:
//                        break;          // URI가 지원되지 않는 방식
//                }
//                Log.d(TAG, "(WEBVIEW)onReceivedError : " + errorCode );
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        });


       upload_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.e("title",title.replace("\n",""));
//               title="hi";
               code_upload_progress.setVisibility(View.VISIBLE);
               upload_btn.setSelected(false);
               upload_btn.setEnabled(false);
               webView.addJavascriptInterface(new JavascriptCallbackClient(context, webView, code_view, loading_text, upload_btn, code_upload_progress,
                       chapter_id,title.replace("\n","")),"android");

               webView.removeJavascriptInterface("searchBoxJavaBridge_");
               webView.removeJavascriptInterface("accessibility");
               webView.removeJavascriptInterface("accessibilityTraversal");
               webView.loadUrl("https://master.d3u1psek9w7brx.amplifyapp.com/");
               //webView.loadUrl("http://192.168.0.5:8080/");
               if (!contents_name.equals("none")) {
                   if (MySharedPreferences.getInt(context, contents_name + " MAX") < 5) {
                       MySharedPreferences.setInt(context, contents_name + " MAX", 5);
                   }
               }
               MySharedPreferences.setInt(context,contents_name,5);


               //webView.loadUrl("https://master.d3u1psek9w7brx.amplifyapp.com/");
//               webView.loadUrl("http://192.168.0.8:8080/");
               //webView.loadUrl("http://192.168.0.8:3000/");

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
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);

        webView.addJavascriptInterface(new JavascriptCallbackClient(context, webView, code_view, loading_text, upload_btn, code_upload_progress,
                chapter_id,"contents_id:"+chapter_id),"android");


        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.loadUrl("https://master.d3u1psek9w7brx.amplifyapp.com/");

        //webView.loadUrl("http://192.168.0.5:8080/");
        //webView.loadUrl("http://192.168.0.8:3000/");

        //webView.loadUrl("https://master.d3u1psek9w7brx.amplifyapp.com/");
//        webView.loadUrl("http://192.168.0.8:8080/");


        //webView.loadUrl("http://192.168.0.8:3000/");

//        confirm_btn.setOnClickListener(Confirm_Btn);
//        if (Cancel_Btn != null) {
//            cancel_btn.setOnClickListener(Cancel_Btn);
//        } else {
//            cancel_btn.setVisibility(View.GONE);
//        }

    }


    private void runCamera(boolean _isCapture)
    {
        if (!_isCapture)
        {// 갤러리 띄운다.
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            pickIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            String pickTitle = "사진 가져올 방법을 선택하세요.";
            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);

            ((Activity)context).startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE);
            return;
        }

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        File path = ((Activity)context).getFilesDir();
        File file = new File(path, "fokCamera.png");
        // File 객체의 URI 를 얻는다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            String strpa = ((Activity)context).getApplicationContext().getPackageName();
            cameraImageUri = FileProvider.getUriForFile(((Activity)context), ((Activity)context).getApplicationContext().getPackageName() + ".fileprovider", file);
        }
        else
        {
            cameraImageUri = Uri.fromFile(file);
        }
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);

        if (!_isCapture)
        { // 선택팝업 카메라, 갤러리 둘다 띄우고 싶을 때..
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            pickIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            String pickTitle = "사진 가져올 방법을 선택하세요.";
            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);

            // 카메라 intent 포함시키기..
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{intentCamera});
            ((Activity)context).startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE);
        }
        else
        {// 바로 카메라 실행..
            ((Activity)context).startActivityForResult(intentCamera, FILECHOOSER_LOLLIPOP_REQ_CODE);
        }
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode)
//        {
//            case FILECHOOSER_NORMAL_REQ_CODE:
//                if (resultCode == RESULT_OK)
//                {
//                    if (filePathCallbackNormal == null) return;
//                    Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
//                    filePathCallbackNormal.onReceiveValue(result);
//                    filePathCallbackNormal = null;
//                }
//                break;
//            case FILECHOOSER_LOLLIPOP_REQ_CODE:
//                if (resultCode == RESULT_OK)
//                {
//                    if (filePathCallbackLollipop == null) return;
//                    if (data == null)
//                        data = new Intent();
//                    if (data.getData() == null)
//                        data.setData(cameraImageUri);
//
//                    filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//                    filePathCallbackLollipop = null;
//                }
//                else
//                {
//                    if (filePathCallbackLollipop != null)
//                    {
//                        filePathCallbackLollipop.onReceiveValue(null);
//                        filePathCallbackLollipop = null;
//                    }
//
//                    if (filePathCallbackNormal != null)
//                    {
//                        filePathCallbackNormal.onReceiveValue(null);
//                        filePathCallbackNormal = null;
//                    }
//                }
//                break;
//            default:
//
//                break;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    protected void onStop() {
        super.onStop();
        webView.removeJavascriptInterface("android");
    }
}

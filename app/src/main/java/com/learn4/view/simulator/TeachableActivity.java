package com.learn4.view.simulator;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn4.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TeachableActivity extends AppCompatActivity {

    WebView webView;
    //public ValueCallback<Uri> filePathCallbackNormal;
    public ValueCallback<Uri[]> filePathCallbackLollipop;
    //public final static int FILECHOOSER_NORMAL_REQ_CODE = 2001;
    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2002;
    //public ValueCallback<Uri[]> filePathCallback;
    //public final static int REQ_SELECT_IMAGE  = 2001;

    private Uri cameraImageUri = null;
    Uri imageUri;

    TextView code_view, loading_text;
    Button upload_btn;
    LinearLayout code_upload_progress;

    String resultString = "";
    boolean btn_check = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachable);

        code_view = findViewById(R.id.code_view);
        loading_text = findViewById(R.id.loading_text);
        upload_btn = findViewById(R.id.upload_btn);
        code_upload_progress = findViewById(R.id.code_upload_progress);

        webView = findViewById(R.id.ai_web_view);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JavascriptCallbackClient2(getBaseContext(), webView, "no"), "android");
        webView.loadUrl("http://192.168.0.8:3000/");

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
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                //Log.d("MainActivity", "5.0+");
                // Callback 초기화 (중요!)
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop.onReceiveValue(null);
                    filePathCallbackLollipop = null;
                }
                filePathCallbackLollipop = filePathCallback;

                boolean isCapture = fileChooserParams.isCaptureEnabled();

                runCamera(isCapture);
                btn_check = false;
                //selectImage(filePathCallback);
                return true;
            }
        });
//
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//
//                int errorCode = error.getErrorCode();
//                Log.e("errorCode",errorCode+"");
//                Log.e("errorCode",error+"");
//
//                webView.loadData("현재 사용이 불가능합니다.","text/html; charset=utf-8","UTF-8");
//            }
//
//            @Nullable
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                return super.shouldInterceptRequest(view, request);
//            }
//        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //openGallery();
                    runCamera(false);
                    btn_check = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                webView.addJavascriptInterface(new JavascriptCallbackClient2(getBaseContext(), webView, "yes"), "android");
                webView.loadUrl("http://192.168.0.8:3000/");
//                webView.addJavascriptInterface(new JavascriptCallbackClient(getBaseContext(), webView, code_view, loading_text, upload_btn, code_upload_progress,
//                        "chapter_id","code"),"android");
//                webView.addJavascriptInterface(new JavascriptCallbackClient(getBaseContext(), webView, code_view, loading_text,
//                        upload_btn, code_upload_progress, "contents_id:"+"teachable", "code_id:"+resultString),"android");
//
//                webView.removeJavascriptInterface("searchBoxJavaBridge_");
//                webView.removeJavascriptInterface("accessibility");
//                webView.removeJavascriptInterface("accessibilityTraversal");
//                webView.loadUrl("http://192.168.0.8:3000/");
            }
        });
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
//
//        webView.getSettings().setDomStorageEnabled(true);
//
//        webView.addJavascriptInterface(new JavascriptCallbackClient(getBaseContext(), webView, code_view, loading_text,
//                upload_btn, code_upload_progress, "contents_id:"+"teachable", resultString),"android");
//
//        webView.removeJavascriptInterface("searchBoxJavaBridge_");
//        webView.removeJavascriptInterface("accessibility");
//        webView.removeJavascriptInterface("accessibilityTraversal");
//
//        webView.loadUrl("http://192.168.0.8:3000/");
    }

//    private void openGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        imageResult.launch(intent);
//    }
//
//    @SuppressLint({"QueryPermissionsNeeded", "IntentReset"})
//    private void selectImage(ValueCallback<Uri[]> filePathCallback2) {
//        if (filePathCallback != null) {
//            filePathCallback.onReceiveValue(null);
//            filePathCallback = null;
//        }
//        filePathCallback = filePathCallback2;
//        //this.filePathCallback.onReceiveValue(null);
//        //this.filePathCallback = filePathCallback;
//
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File path = getFilesDir();
//        File file = new File(path, "fokCamera.png");
//
//        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
//            cameraImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
//        }
//
//        Intent pickIntent = new Intent(Intent.ACTION_PICK);
//        pickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        pickIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        String pickTitle = "사진 가져올 방법을 선택하세요.";
//        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
//
//        // 카메라 intent 포함시키기
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{cameraIntent});
//        startActivityForResult(chooserIntent, REQ_SELECT_IMAGE);
//    }
//
//    private ActivityResultLauncher<Intent> imageResult = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK) {
//                    assert result.getData() != null;
//                    imageUri = result.getData().getData();
//                    //Log.e("gallery", imageUri+"");
//
//                    String testUri = getPath(imageUri);
//                    //Log.e("gallery", testUri);
//
//                    //ImageDecoder.Source Dsource = null;
//                    Bitmap Dsource = null, Bsource = null;
//                    boolean check = false;
//                    resultString = "";
//
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//                        try {
//                            //Dsource = ImageDecoder.createSource(getApplication().getContentResolver(), imageUri);
//                            Dsource = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
//                            Log.e("gallery",  "D : " + Dsource+"");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        check = true;
//                    } else {
//                        try {
//                            Bsource = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
//                            Log.e("gallery",  "B : " +Bsource+"");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (check) {
//                        if (Dsource != null) {
//                            resultString = bitmapToString(Dsource);
//                            //Log.e("gallery", "D : " + resultString);
//                            Log.e("gallery", "D : ");
//                        }
//                    } else {
//                        if (Bsource != null) {
//                            resultString = bitmapToString(Bsource);
//                            //Log.e("gallery", "B : " + resultString);
//                            Log.e("gallery", "B : ");
//                        }
//                    }
//                    //Bitmap testBitmap = stringToBitmap(testUri);
//                    //Log.e("gallery", testBitmap+"");
//
//                    webView.addJavascriptInterface(new JavascriptCallbackClient(getBaseContext(), webView, code_view, loading_text,
//                            upload_btn, code_upload_progress, "contents_id:"+"teachable", resultString),"android");
//
//                    webView.removeJavascriptInterface("searchBoxJavaBridge_");
//                    webView.removeJavascriptInterface("accessibility");
//                    webView.removeJavascriptInterface("accessibilityTraversal");
//                    webView.loadUrl("http://192.168.0.8:3000/");
//
//                } else if (result.getResultCode() == RESULT_CANCELED) {
//                    Log.e("gallery", "Cancelled");
//                }
//            }
//    );
//
//    private String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//
//        return cursor.getString(column_index);
//    }
//
//    private Bitmap stringToBitmap(String base64) {
//        byte[] encodeByte = Base64.decode(base64, Base64.DEFAULT);
//
//        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        bitmap.recycle();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void runCamera(boolean _isCapture)
    {
//        if (!_isCapture)
//        {// 갤러리 띄운다.
//            Intent pickIntent = new Intent(Intent.ACTION_PICK);
//            pickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//            pickIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//            String pickTitle = "사진 가져올 방법을 선택하세요.";
//            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
//
//            startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE);
//            return;
//        }
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        @SuppressLint("SdCardPath")
        String[] checkPath = {"/data/user/0/com.learn4/files/fokCamera.jpg", "/data/user/0/com.learn4/files/fokCamera2.jpg"};

        File[] file1 = {new File(checkPath[0]), new File(checkPath[1])};
        int mode = 0;

        File path = getFilesDir();
        File file;

        if (file1[0].exists()) {
            //Log.e("testtest", "isFileExistsCheck : 파일 존재함");
            mode= 1;
            file = new File(path, "fokCamera2.jpg");
        } else if (file1[1].exists()) {
            //Log.e("testtest", "isFileExistsCheck2 : 파일 존재함");
            mode= 2;
            file = new File(path, "fokCamera.jpg");
        } else {
            //Log.e("testtest", "isFileExistsCheck : 파일 없음");
            mode= 2;
            file = new File(path, "fokCamera.jpg");
        }
        //Log.e("testtest","test : " + file);
        //Log.e("testtest", file+"");
        // File 객체의 URI 를 얻는다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            String strpa = getApplicationContext().getPackageName();
            cameraImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
            //Log.e("testtest","test : " + cameraImageUri);
            switch (mode) {
                case 1:
                    if (file1[0].exists()) {
                        boolean success = file1[0].delete();

                        if (success) {
                            Log.e("testtest", "삭제 성공0");
                        } else {
                            Log.e("testtest", "삭제 실패0");
                        }
//                        File[] files = file1[0].listFiles();
//
//                        if (files != null) {
//                            for (File value : files) {
//                                if (value.delete()) {
//                                    Log.e("testtest", value.getName() + "삭제 성공");
//                                } else {
//                                    Log.e("testtest", value.getName() + "삭제 실패");
//                                }
//                            }
//
//                            if (file1[0].delete()) {
//                                Log.e("testtest", "성공");
//                            } else {
//                                Log.e("testtest", "실패");
//                            }
//                        } else {
//                            Log.e("testtest", "없음2");
//                        }
                    }
                    break;
                case 2:
                    if (file1[1].exists()) {
                        boolean success = file1[1].delete();

                        if (success) {
                            Log.e("testtest", "삭제 성공11");
                        } else {
                            Log.e("testtest", "삭제 실패1");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        else
        {
            cameraImageUri = Uri.fromFile(file);
            //Log.e("testtest","test2 : " + cameraImageUri);
        }
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);

        if (!_isCapture)
        { // 선택팝업 카메라, 갤러리 둘다 띄우고 싶을 때
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            pickIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            String pickTitle = "사진을 가져올 방법을 선택하세요";
            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);

            // 카메라 intent 포함시키기
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{intentCamera});
            startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE);
        }
        else
        {// 바로 카메라 실행..
            startActivityForResult(intentCamera, FILECHOOSER_LOLLIPOP_REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
//            case FILECHOOSER_NORMAL_REQ_CODE:   //2001
//                if (resultCode == RESULT_OK)
//                {
//                    if (filePathCallbackNormal == null) return;
//                    Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
//                    filePathCallbackNormal.onReceiveValue(result);
//                    filePathCallbackNormal = null;
//                }
//                if (resultCode == RESULT_OK)
//                {
//                    if (filePathCallback == null) return;
//                    if (data == null)
//                        data = new Intent();
//                    if (data.getData() == null)
//                        data.setData(cameraImageUri);
//
//                    filePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//                    filePathCallback = null;
//                }
//                else
//                {
//                    if (filePathCallback != null) {
//                        filePathCallback.onReceiveValue(null);
//                        filePathCallback = null;
//                    }
//                }
//                break;
            case FILECHOOSER_LOLLIPOP_REQ_CODE: {   //2002
                //Log.e("testtest","start");
                //boolean mode_check = false;

                if (resultCode == RESULT_OK)
                {
                    //Log.e("testtest", btn_check+"");
                    if (btn_check) {
                        //Log.e("testtest","start camera");
//                        if (data == null) {
//                            Log.e("testtest", "change null");
//                            data = new Intent();
//
//                            if (data.getData() == null)
//                                data.setData(cameraImageUri);
//
//                            //mode_check = true;
//                        }
                        //Log.e("testtest", "not null");
                        //assert data != null;
                        //Log.e("testtest", data.getData()+"");

                        Bitmap Dsource = null;
                        //Bitmap bitmap2 = null;

                        resultString = "";
                        //imageUri = data.getData();

                        if (data == null) {
                            imageUri = cameraImageUri;
                        } else {
                            imageUri = data.getData();
                        }
//                        if (!mode_check) {
//                            bitmap2  = BitmapFactory.decodeFile(getPath(imageUri));
//                        } else {
//                            bitmap2  = BitmapFactory.decodeFile(String.valueOf(cameraImageUri));
//                            Log.e("testtest", "change null camera");
//                        }

                        //Bitmap bitmap2  = BitmapFactory.decodeFile(getPath(imageUri));
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] imageBytes = baos.toByteArray();

                        //String testString = "data:image/jpeg:base64,"+ Base64.encodeToString(imageBytes, Base64.DEFAULT) + "';";
                        //Log.e("test", Base64.encodeToString(imageBytes, Base64.DEFAULT));

//                        Bitmap bitmap = null;
//                        File testImage = new File(getPath(imageUri));
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                        try {
//                            bitmap = BitmapFactory.decodeStream(new FileInputStream(testImage), null, options);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        //Bitmap bitmap = BitmapFactory.decodeFile(testImage);
                        try {
                            //Dsource = ImageDecoder.createSource(getApplication().getContentResolver(), imageUri);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                Dsource = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                            } else {
                                Dsource = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
                            }
                            //Log.e("gallery",  "D : " + Dsource+"");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (Dsource != null) {
                            resultString = bitmapToString(Dsource);
                            //Log.e("testtest", "D : " + resultString);

//                            webView.evaluateJavascript(publishEvent("testImage", "\""+resultString+"\""),
//                                    (result) -> {
//                                        Log.e("message","완료");
//                                    }
//                            );
//                            try {
//                                webView.addJavascriptInterface(new JavascriptCallbackClient2(getBaseContext(), webView,
//                                        "'data:image/png;base64," + URLEncoder.encode(resultString, "UTF-8") + "';"), "android");
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
                            //Log.e("testtest","end camera1");
                            try {
                                webView.addJavascriptInterface(new JavascriptCallbackClient2(getApplicationContext(), webView,
                                        URLEncoder.encode(resultString, "UTF-8")), "android");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //webView.addJavascriptInterface(new JavascriptCallbackClient2(getBaseContext(), webView, testString), "android");

//                            try {
//                                webView.addJavascriptInterface(new JavascriptCallbackClient2(getBaseContext(), webView,
//                                        "'data:image/png;base64," + URLEncoder.encode(resultString, "UTF-8").replaceAll("\\+", "%20")
//                                                .replaceAll("\\%21", "!").replaceAll("\\%27", "'").replaceAll("\\%28", "(")
//                                                .replaceAll("\\%29", ")").replaceAll("\\%7E", "~") + "';"), "android");
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
                            webView.loadUrl("http://192.168.0.8:3000/");
                            //webView.evaluateJavascript("alert('Hello, World!');", null);
                            //"window.dispatchEvent(new CustomEvent(\"" + "testImage" + "\", {detail: " + ${Gson().toJson(detail)} + "}));";

                            //webView.evaluateJavascript("window.dispatchEvent(testImage)", null);
                            //webView.evaluateJavascript(publishEvent("testImage", "\""+resultString+"\""), null);
//                            webView.evaluateJavascript("window.dispatchEvent(\n" + "   new CustomEvent(\"" +
//                                    "testImage" + "\", {\n" + "           detail: {\n" + "               data: "
//                                    + "\""+resultString+"\"" + "\n" + "           }\n" + "       }\n" + "   )\n" + ");", null);
//                            Handler handler = new Handler();
//                            handler.postDelayed(() -> {
//                                webView.evaluateJavascript("window.dispatchEvent(testImage)", null);
//                            }, 0);

                            //evaluateJavascript("window.dispatchEvent(new CustomEvent('NativeEvent', {detail: '" + message + "'}));", null);
                        }
                        //Log.e("testtest","end camera2");
                    } else {
                        //Log.e("testtest","start gallery");
                        if (filePathCallbackLollipop == null) {
                            cameraImageUri = null;
                            //Log.e("testtest","filePathCallbackLollipop null");
                            return;
                        }
                        if (data == null) {
                            //Log.e("testtest","data null");
                            data = new Intent();
                        }
                        if (data.getData() == null) {
                            //Log.e("testtest","data.getData() null");
                            data.setData(cameraImageUri);
                        }
                        //Log.e("testtest",cameraImageUri +"");
                        //Log.e("testtest",data +"");
                        //Log.e("testtest", "camera");
                        filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                        filePathCallbackLollipop = null;
                        //Log.e("testtest","end gallery");
                    }
                }
                else
                {
                    if (filePathCallbackLollipop != null)
                    {
                        filePathCallbackLollipop.onReceiveValue(null);
                        filePathCallbackLollipop = null;
                    }
//                    if (filePathCallbackNormal != null)
//                    {
//                        filePathCallbackNormal.onReceiveValue(null);
//                        filePathCallbackNormal = null;
//                    }
                }
                //내가 추가한 부분
//                assert data.getData() != null;
//                imageUri = data.getData();
//                //Log.e("gallery", imageUri+"");
//
//                String testUri = getPath(imageUri);
//                //Log.e("gallery", testUri);
//
//                //ImageDecoder.Source Dsource = null;
//                Bitmap Dsource = null, Bsource = null;
//                boolean check = false;
//                resultString = "";
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//                    try {
//                        //Dsource = ImageDecoder.createSource(getApplication().getContentResolver(), imageUri);
//                        Dsource = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
//                        Log.e("gallery",  "D : " + Dsource+"");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    check = true;
//                } else {
//                    try {
//                        Bsource = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
//                        Log.e("gallery",  "B : " +Bsource+"");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if (check) {
//                    if (Dsource != null) {
//                        resultString = bitmapToString(Dsource);
//                        //Log.e("gallery", "D : " + resultString);
//                        Log.e("gallery", "D : ");
//                    }
//                } else {
//                    if (Bsource != null) {
//                        resultString = bitmapToString(Bsource);
//                        //Log.e("gallery", "B : " + resultString);
//                        Log.e("gallery", "B : ");
//                    }
//                }
//                //Bitmap testBitmap = stringToBitmap(testUri);
//                //Log.e("gallery", testBitmap+"");
//
//                webView.addJavascriptInterface(new JavascriptCallbackClient(getBaseContext(), webView, code_view, loading_text,
//                        upload_btn, code_upload_progress, "contents_id:"+"teachable", resultString),"android");
//
//                webView.removeJavascriptInterface("searchBoxJavaBridge_");
//                webView.removeJavascriptInterface("accessibility");
//                webView.removeJavascriptInterface("accessibilityTraversal");
//                webView.loadUrl("http://192.168.0.8:3000/");
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.removeJavascriptInterface("android");
    }
}
package com.learn4.view.simulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.learn4.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

public class TeachableActivity extends AppCompatActivity{

    WebView webView;

    public ValueCallback<Uri[]> filePathCallbackLollipop;
    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2002;

    private Uri cameraImageUri = null;
    Uri imageUri;

    TextView code_view, loading_text, guide_text;
    Button upload_btn;
    LinearLayout code_upload_progress;

    String resultString = "";
    boolean btn_check = false;

    HorizontalBarChart predict_view;
    //JavascriptCallbackClient2 javascriptCallbackClient2;

    String predict_result = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachable);

        code_view = findViewById(R.id.code_view);
        loading_text = findViewById(R.id.loading_text);
        guide_text = findViewById(R.id.guide_text);
        upload_btn = findViewById(R.id.upload_btn);
        code_upload_progress = findViewById(R.id.code_upload_progress);
        webView = findViewById(R.id.ai_web_view);
        predict_view = findViewById(R.id.predict_view);

        webView.setBackgroundColor(0);

//        javascriptCallbackClient2 = new JavascriptCallbackClient2(getBaseContext());
//        //javascriptCallbackClient2 = new JavascriptCallbackClient2(getBaseContext(), webView, "yes");
//        javascriptCallbackClient2.setTestListener(this);

        //testtest(predict_result);
//        configureChartAppearance(predict_result);
//        prepareChartData(createChartData(predict_result));

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

        webView.addJavascriptInterface(new JavascriptCallbackClient2(this, getBaseContext(), webView, "no"), "android");
        webView.loadUrl("https://main.d3kfr80s8mk4j7.amplifyapp.com/");

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

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    runCamera(false);
                    btn_check = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                webView.addJavascriptInterface(new JavascriptCallbackClient2(getParent(), getBaseContext(), webView, "yes"), "android");
                webView.loadUrl("https://main.d3kfr80s8mk4j7.amplifyapp.com/");
            }
        });
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        bitmap.recycle();
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void runCamera(boolean _isCapture)
    {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        @SuppressLint("SdCardPath")
        String[] checkPath = {"/data/user/0/com.learn4/files/fokCamera.jpg", "/data/user/0/com.learn4/files/fokCamera2.jpg"};

        File[] file1 = {new File(checkPath[0]), new File(checkPath[1])};
        int mode = 0;

        File path = getFilesDir();
        File file;

        if (file1[0].exists()) {
            mode= 1;
            file = new File(path, "fokCamera2.jpg");
        } else if (file1[1].exists()) {
            mode= 2;
            file = new File(path, "fokCamera.jpg");
        } else {
            mode= 2;
            file = new File(path, "fokCamera.jpg");
        }

        // File 객체의 URI 얻기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            cameraImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);

            switch (mode) {
                case 1:
                    if (file1[0].exists()) {
                        boolean success = file1[0].delete();

                        if (success) {
                            Log.e("testtest", "삭제 성공0");
                        } else {
                            Log.e("testtest", "삭제 실패0");
                        }
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
        {// 바로 카메라 실행
            startActivityForResult(intentCamera, FILECHOOSER_LOLLIPOP_REQ_CODE);
        }
    }

    public void testSetting(String s) {
        Log.e("testtestt", "실행됨2");
        Log.e("testtestt", s);
        predict_result = s;
        configureChartAppearance(predict_result);
        prepareChartData(createChartData(predict_result));
    }

//    @Override
//    public void setting(String s) {
//        Log.e("testtestt", "실행됨");
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILECHOOSER_LOLLIPOP_REQ_CODE) {//2002
            if (resultCode == RESULT_OK) {
                if (btn_check) {
                    Bitmap Dsource = null;

                    resultString = "";

                    if (data == null) {
                        imageUri = cameraImageUri;
                    } else {
                        imageUri = data.getData();
                    }

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            Dsource = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                        } else {
                            Dsource = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (Dsource != null) {
                        resultString = bitmapToString(Dsource);

                        try {
                            webView.addJavascriptInterface(new JavascriptCallbackClient2(getParent(), getBaseContext(), webView,
                                    URLEncoder.encode(resultString, "UTF-8")), "android");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        webView.loadUrl("https://main.d3kfr80s8mk4j7.amplifyapp.com/");

                    }
                } else {
                    if (filePathCallbackLollipop == null) {
                        cameraImageUri = null;
                        return;
                    }
                    if (data == null) {
                        data = new Intent();
                    }
                    if (data.getData() == null) {
                        data.setData(cameraImageUri);
                    }

                    filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    filePathCallbackLollipop = null;
                }
            } else {
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop.onReceiveValue(null);
                    filePathCallbackLollipop = null;
                }
            }
        }
    }

    public void configureChartAppearance(String predict) {
        //Log.e("testtest", "?");
        String[] splitString = predict.split("\n");
        String[] APPS = new String[splitString.length];
        for (int i = 0; i < splitString.length ; i++) {
            APPS[i] = splitString[i].split(":")[0];
        }

        predict_view.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        predict_view.setTouchEnabled(false); // 터치 유무
        predict_view.getLegend().setEnabled(false); // Legend는 차트의 범례
        predict_view.setExtraOffsets(10f, 0f, 40f, 0f);

        // XAxis (수평 막대 기준 왼쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = predict_view.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15f);
        xAxis.setTextColor(Color.parseColor("#5a5a5a"));
        xAxis.setGridLineWidth(0f);
        xAxis.setGridColor(Color.parseColor("#fdf3dc"));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X 축 데이터 표시 위치

        // YAxis(Left) (수평 막대 기준 아래쪽) - 선 유무, 데이터 최솟값/최댓값, label 유무
        YAxis axisLeft = predict_view.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0f); // 최솟값
        axisLeft.setAxisMaximum(100f); // 최댓값
        axisLeft.setGranularity(1f); // 값만큼 라인선 설정
        axisLeft.setDrawLabels(false); // label 삭제

        // YAxis(Right) (수평 막대 기준 위쪽) - 사이즈, 선 유무
        YAxis axisRight = predict_view.getAxisRight();
        axisRight.setTextSize(15f);
        axisRight.setDrawLabels(false); // label 삭제
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);

        // XAxis에 원하는 String 설정하기 (앱 이름)
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return APPS[(int) value];
            }
        });
    }

    public BarData createChartData(String predict) {
        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();
//        for (int i = 0; i < MAX_X_VALUE; i++) {
//            float x = i;
//            float y = new Random().nextFloat() * (MAX_Y_VALUE - MIN_Y_VALUE) + MIN_Y_VALUE;
//            values.add(new BarEntry(x, y));
//        }
        String[] splitString = predict.split("\n");

        for (int i = 0; i < splitString.length ; i++) {
            float x = i;
            String splitString2 = splitString[i].split(":")[1];
            String splitString3 = splitString2.replace("%","");
            float y = Float.parseFloat(splitString3);
            values.add(new BarEntry(x, y));
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        BarDataSet set2 = new BarDataSet(values, "SET_LABEL");
        //Log.e("testtest", values+"??");
        set2.setDrawIcons(false);
        set2.setDrawValues(true);
        set2.setColor(Color.parseColor("#ffab2e")); // 색상 설정
        //set2.setGradientColor(Color.parseColor("#ffc462"), Color.parseColor("#ff872e"));

        // 데이터 값 원하는 String 포맷으로 설정하기 (ex. ~회)
        set2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                //Log.e("testtest", value+"???");
                return (String.valueOf((int) value)) + "%";
            }
        });

        // 3. [BarData] 보여질 데이터 구성
        BarData data = new BarData(set2);
        data.setBarWidth(0.25f);
        data.setValueTextSize(15);
        data.setValueTextColor(Color.parseColor("#b45611"));

        return data;
    }

    public void prepareChartData(BarData data) {
        predict_view.setData(data); // BarData 전달
        predict_view.invalidate(); // BarChart 갱신해 데이터 표시
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.removeJavascriptInterface("android");
    }
}
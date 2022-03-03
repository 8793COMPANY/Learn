/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.corporation8793.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Guideline;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.TimeoutError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.corporation8793.MySharedPreferences;
import com.corporation8793.R;
import com.corporation8793.dialog.FinishDialog;
import com.corporation8793.dialog.ProgressDialog;
import com.corporation8793.dialog.UploadDialog;
import com.corporation8793.learn.xml.ParentXml;
import com.google.blockly.android.AbstractBlocklyActivity;
import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.FlyoutFragment;
import com.google.blockly.android.OnCloseCheckListener;
import com.google.blockly.android.TabItemClick;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.ui.BusProvider;
import com.google.blockly.android.ui.CategoryData;
import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.android.ui.PushEvent;
import com.google.blockly.model.DefaultBlocks;
import com.google.blockly.utils.BlockLoadingException;
import com.physicaloid.lib.Boards;
import com.physicaloid.lib.Physicaloid;
import com.squareup.otto.Subscribe;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlunit.builder.Input;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Demo app with the Blockly Games turtle game in a webview.
 */
public class MainActivity extends BlocklySectionsActivity implements TabItemClick , OnCloseCheckListener {
    //2021.07.23 제일 최신 버전

    private static final String TAG = "TurtleActivity";

    private static final String SAVE_FILENAME = "turtle_workspace.xml";
    private static final String AUTOSAVE_FILENAME = "turtle_workspace_temp.xml";
    private static final String AUTOSAVE_FILENAME2 = "turtle_workspace_temp2.xml";
    private TextView mGeneratedTextView;
    private TextView mGeneratedErrorTextView;
    private FrameLayout mGeneratedFrameLayout;
    private String mNoCodeText;
    private String mNoErrorText;
    private CategoryView mCategoryView;
    FlyoutFragment flyoutFragment;
    View [] tempTab = {null, null, null};
    Boolean [] tempTabCheck = {false, false, false};

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS");

    private Boolean initial=true, wifi_check = false, usb_check = false;
    Boolean compileCheck = false, copy_check = false;
    private EditText editURL;
    View setup_view, loop_view, method_view, etc_view, code_view, serial_view, upload_view;
    EditText serial_input_box;

    Button serial_send_btn, init_btn, translate_btn, code_btn, serial_btn;
    public Button block_copy_btn, reset_btn;

    ImageView block_bot_btn;
    MediaPlayer mediaPlayer;

    LinearLayout trashcan_btn;
    LinearLayout blockly_monitor, input_space;
    String code = "",current_tag ="", serial_code="", serial_input="";
    String submittedXml = "";
    TextView monitor_text;
    CategoryData categoryData;
    String TARGET_BASE_PATH;
    UsbDevice bigBoard;
    BroadcastReceiver mUsbReceiver;
    ProgressDialog customProgressDialog;
    String str ="";
    ScrollView scrollview;
    int num = 0;
    String contents_name ="none";

    Guideline guideline4;
    Button upload_btn;

    ArrayList<Integer> arrayList;
    ArrayAdapter<Integer> arrayAdapter;
    Spinner baud_rate;


    private UploadDialog uploadListener, error_Listener;
    private FinishDialog finishListener;

    int current_pos =0;


    //시니얼 모니터 slow 방지 문자열
    StringBuilder stringBuilder = new StringBuilder();

    BlocklyController controller;


    byte[] buffer = new byte[1024];  // buffer store for the stream
    int bytes; // bytes returned from read()
    // TODO : 블루투스 모드까지 ON
    //Physicaloid mPhysicaloid = new Physicaloid(this, true, "IOTMASS");
    // TODO : ONLY USB
    Physicaloid mPhysicaloid = new Physicaloid(this);

    Boolean [] view_check = {true,true,true,true,true,true,true};

    String [] turtle_files_kor = {"default/logic_blocks_kor.json","default/loop_blocks_kor.json","default/math_blocks_kor.json","default/variable_blocks_kor.json", "turtle/turtle_blocks_kor.json"};
    String [] turtle_files_eng = {"default/logic_blocks.json","default/loop_blocks.json","default/math_blocks.json","default/variable_blocks.json", "turtle/turtle_blocks.json"};

    static final List<String> TURTLE_BLOCK_DEFINITIONS = Arrays.asList(
            DefaultBlocks.COLOR_BLOCKS_PATH,
            DefaultBlocks.LOGIC_BLOCKS_PATH,
            DefaultBlocks.LOOP_BLOCKS_PATH,
            DefaultBlocks.MATH_BLOCKS_PATH,
            DefaultBlocks.TEXT_BLOCKS_PATH,
            DefaultBlocks.VARIABLE_BLOCKS_PATH,
            "turtle/turtle_blocks.json"
    );
    static final List<String> TURTLE_BLOCK_GENERATORS = Arrays.asList(
            "turtle/generators.js"
    );
    private static final int MAX_LEVELS = 2;
    private static final String[] LEVEL_TOOLBOX = new String[MAX_LEVELS];

    static {
        LEVEL_TOOLBOX[0] = "arduino_basic.xml";
        LEVEL_TOOLBOX[1] = "arduino_advanced.xml";
    }

    Handler mHandler = new Handler();




    public void read_code() {
        Log.e("generated", "리드");
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        if(deviceList.isEmpty()){
            Log.e("read_code generated : ", "isempty 디바이스 리스트");
        }
        else {
            Set<String> keys = deviceList.keySet();
            for (String key : keys) {
                bigBoard = deviceList.get(key);
            }
        }

        try {
            mPhysicaloid.open();

            byte[] buf = new byte[256];
            mPhysicaloid.read(buf, buf.length);
//            monitor_text.append(new String(buf));
            Log.e("buf",new String(buf).trim());

//            monitor_text.append(num+"\n");
//            str += new String(buf);

            if (new String(buf).trim().length() != 0) {
                Log.e("num:",num+"");
                Log.e("length",stringBuilder.toString().length()+"");
                if (num < 60) {
                    stringBuilder.append(new String(buf));
                } else if (num >= 60) {
                    stringBuilder.append(new String(buf));
                    num = 50;
                    stringBuilder.delete(0,2560);
                    Log.e("length delete","OK");
                }

                if (Integer.parseInt(String.valueOf(stringBuilder.toString().length())) > 5120) {
                    stringBuilder.delete(0,2560);
                    Log.e("length delete MAX","OK");
                }

                monitor_text.setText(stringBuilder);
                num++;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"보드를 연결해주세요.",Toast.LENGTH_SHORT).show();
            mMonitorHandler.sendEmptyMessage(1);
        }

    }

    private final CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback =
            new CodeGenerationRequest.CodeGeneratorCallback() {
                @Override
                public void onFinishCodeGeneration(final String generatedCode, String xml) {
                    Log.e("start!","onFinishCodeGeneration");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            code = generatedCode;
                            submittedXml = xml;
//                            updateTextMinWidth();
                        }
                    });

                    if(generatedCode.contains("alert")) {
                        /*
                         js 에서   '!!alert!! ~~'
                         관련 코드를 return하면 여기로 들어옴
                        */
                        String[] alert = generatedCode.split("!!");
                        Log.e("in",alert[2]);
                        Toast.makeText(getApplicationContext(), alert[2], Toast.LENGTH_LONG).show();
                        code = generatedCode;
                        customProgressDialog.dismiss();
//                        Log.e("generated",generatedCode);
                    }
                    else {
                        Log.e("start!","not generatedCode.contains");

                        code = generatedCode;
                        submittedXml = xml;
                        create_file(generatedCode,"code.ino");
                        Log.e("compileCheck",compileCheck+"");
                        //Log.e("!@","nono");
                        if (compileCheck) {
                            //Log.e("generated", "컴파컴파");
                            remotecompile("code.ino", getCompiler());
                            Log.e("in!","ok");
                            compileCheck = false;
                            customProgressDialog.dismiss();
                        }
                    }
                }
            };

    public void serial_write(String str){
        if (mPhysicaloid.isOpened()) {
            if(mPhysicaloid.open()) {
                byte[] buf = str.getBytes();
                mPhysicaloid.write(buf, buf.length);
                mPhysicaloid.close();
            }
        }
    }

    public void upload_code(String file){
        Log.e("generated", "업로드드");
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        if(deviceList.isEmpty()){
            Log.e("uploadcode generated : ", "isempty 디바이스 리스트");
        }

        if (mPhysicaloid.isOpened()) {
            OpenUSB();
            loadXmlFromWorkspace();
            Log.e("in! upload","before");
            mHandler.sendEmptyMessageDelayed(1,3000);
            mPhysicaloid.upload(Boards.ARDUINO_UNO, file);
            Log.e("in! upload","finish");
            mHandler.removeMessages(1);
            uploadListener.show();

        } else {
            Boolean value = OpenUSB();
            loadXmlFromWorkspace();
            if (value) {
                // TODO : 업로드 팝업 디자인 수정
                mPhysicaloid.upload(Boards.ARDUINO_UNO, file);
                uploadListener.show();
            }
            else {
                Toast.makeText(getApplicationContext(),"한번 더 업로드 버튼을 눌러주세요",Toast.LENGTH_SHORT).show();
            }
            getFileSize();
        }


    }

    public long getFileSize(){
        File directory = new File("/data/data/com.learn4/out.hex");
        Log.e("file size",directory.length()+"");
        return directory.length();
    }



    public Boolean OpenUSB() {
        Log.e("usb initial",initial+"");
        if(initial) {
            Log.e("openUsb true","!!");
            initial = false;
            mPhysicaloid.upload(Boards.ARDUINO_UNO, "/data/data/com.learn4/code.ino");
            return initial;
        }
        else {
            Log.e("openUsb false","!!");
            return true;
        }
    }

    public void remotecompile(String filename, String url) {
        Log.e("filename",filename);

        Log.e("generated", "리모트 전");


        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        if(response.contains("code.ino")) {
                            //     mGeneratedErrorTextView.setVisibility(View.VISIBLE);
                            //   mGeneratedErrorTextView.setText(response);
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        }
                        else {
                            //     mGeneratedErrorTextView.setVisibility(View.GONE);
                            //       mGeneratedErrorTextView.setText("");
//                            Log.e("2여기다 이놈아",response);
                            create_file(response, "out.hex");
                            Log.e("generated", "리모트 안");
                            upload_code("/data/data/com.learn4/out.hex");
                            Log.e("upload finish","in!");
                        }
                        //System.out.println(response);
                    }
                }, error -> {
                    Log.e("error","in!");
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    if (error.getMessage() != null) {
                        Log.e("서버 에러", "remotecompile: " + error.getMessage());
                        if(error.toString().equals("com.android.volley.error.ServerError")) {
                            Log.e("server error log",error.getMessage());
                            //       mGeneratedErrorTextView.setVisibility(View.VISIBLE);
                            //       mGeneratedErrorTextView.setText("Error:\n\t Problem Connecting Remote Compiler: null reply from compiler");
                        }
                        else if(error.getMessage().contains("java.net.ConnectException")) {
                            Log.e("Remote error log",error.getMessage());
                            Toast.makeText(getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            //    mGeneratedErrorTextView.setVisibility(View.VISIBLE);
                            //    mGeneratedErrorTextView.setText("Error:\n\t Problem Connecting Remote Compiler: ConnectException");
    //                    Toast.makeText(getApplicationContext(), "Error Connecting Remote Compiler", Toast.LENGTH_LONG).show();
                        }
                        else {
                            // mGeneratedErrorTextView.setVisibility(View.VISIBLE);
                            //  mGeneratedErrorTextView.setText(error.getMessage());

                            Log.e("error log",error.getMessage());
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });

//        Map<String,String> Headers = new HashMap<>();
//        Headers.put("board", "uno");
//        Headers.put("file", "file:///android_asset/blink.ino");
        smr.addMultipartParam("board", "Text", "uno");
        smr.addFile("file", TARGET_BASE_PATH+filename);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(smr);
    }

  /*  public void get_ports() {
        PeripheralManager manager = PeripheralManager.getInstance();
        List<String> deviceList = manager.getUartDeviceList();
        if (deviceList.isEmpty()) {
            Log.i(TAG, "No UART port available on this device.");
        } else {
            Log.i(TAG, "List of available devices: " + deviceList);
            System.out.println(deviceList);
        }
    }*/

    public void create_file(String fileContents, String filename){
        FileOutputStream outputStream;
        File f = new File(TARGET_BASE_PATH+filename);
        try {

//            InputStream is = new FileInputStream(f);
//            int size = is.available();
//            Toast.makeText(getApplicationContext(),"size:"+size, Toast.LENGTH_LONG).show();
            FileOutputStream fooStream = new FileOutputStream(f, false);
            byte[] myBytes = fileContents.getBytes();
            fooStream.write(myBytes);
            fooStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void execute_shell(String cmd) throws IOException {
        String s = null;
        String[] path = {"PATH=/sbin:/vendor/bin:/system/sbin:/system/bin:/system/xbin:/data/data/"+this.getPackageName()+"/local/bin"};
        Process p = Runtime.getRuntime().exec(cmd,path,new File("/data/data/"+this.getPackageName()));//+"/hardware/tools/avr/bin/"));
        //Toast.makeText(getApplicationContext(), getApplicationInfo().dataDir,Toast.LENGTH_LONG).show();
        StringBuilder str = new StringBuilder();
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null) {
            str.append(s+"\n");
            //System.out.println(s);

        }

        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            str.append(s);

        }
        String ori_str = str.toString();
        if(!ori_str.equals(null) && !ori_str.equals("") && !ori_str.contains("warning")){
            System.out.println(str.toString());
        }


    }

    @Override
    public void onLoadWorkspace() {
        Log.e("in!","onLoadWorkspace");
        mBlocklyActivityHelper.loadWorkspaceFromAppDirSafely(SAVE_FILENAME);
    }

    @Override
    public void onCompilerChoose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert, null);

        builder.setView(view)
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(loadJSONFromFile());
                            obj.put("url",editURL.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        create_file(obj.toString(),"app.json");
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        editURL = view.findViewById(R.id.edit_url);
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Compiler:"+getCompiler());
        alert.show();
    }

    public String loadJSONFromFile() {
        String json = null;
        try {
            File file = new File(TARGET_BASE_PATH+"app.json");
            InputStream is = new FileInputStream(file);
            int size = is.available();
//            Toast.makeText(getApplicationContext(),size+"",Toast.LENGTH_SHORT).show();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onCodeWorkspace() {
//        if(mGeneratedTextView.getVisibility()!= View.VISIBLE){
//            mGeneratedTextView.setVisibility(View.VISIBLE);
//            if(!mGeneratedErrorTextView.getText().toString().equals(""))
//                mGeneratedErrorTextView.setVisibility(View.VISIBLE);
////            updateTextMinWidth();
//        }
//        else {
//            mGeneratedTextView.setVisibility(View.GONE);
//            mGeneratedErrorTextView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onSaveWorkspace() {
        Log.e("in","onSaveWorkspace");
        mBlocklyActivityHelper.saveWorkspaceToAppDirSafely(SAVE_FILENAME);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return onDemoItemSelected(item, this) || super.onOptionsItemSelected(item);
    }

    static boolean onDemoItemSelected(MenuItem item, AbstractBlocklyActivity activity) {
        BlocklyController controller = activity.getController();
        int id = item.getItemId();
        boolean loadWorkspace = false;
        String filename = "";
        if (id == R.id.action_demo_android) {
            loadWorkspace = true;
            filename = "android.xml";
        }
//        else if (id == R.id.action_demo_lacey_curves) {
//            loadWorkspace = true;
//            filename = "lacey_curves.xml";
//        } else if (id == R.id.action_demo_paint_strokes) {
//            loadWorkspace = true;
//            filename = "paint_strokes.xml";
//        }

        if (loadWorkspace) {
            String assetFilename = "turtle/demo_workspaces/" + filename;
            try {
                controller.loadWorkspaceContents(activity.getAssets().open(assetFilename));
            } catch (IOException | BlockLoadingException e) {
                throw new IllegalStateException(
                        "Couldn't load demo workspace from assets: " + assetFilename, e);
            }
            addDefaultVariables(controller);
            return true;
        }

        return false;
    }

    /**
     * Estimate the pixel size of the longest line of text, and set that to the TextView's minimum
     * width.
     */
//    private void updateTextMinWidth() {
//        String text = mGeneratedTextView.getText().toString();
//        int maxline = 0;
//        int start = 0;
//        int index = text.indexOf('\n', start);
//        while (index > 0) {
//            maxline = Math.max(maxline, index - start);
//            start = index + 1;
//            index = text.indexOf('\n', start);
//        }
//        int remainder = text.length() - start;
//        if (remainder > 0) {
//            maxline = Math.max(maxline, remainder);
//        }
//
//        float density = getResources().getDisplayMetrics().density;
//        mGeneratedTextView.setMinWidth((int) (maxline * 15 * density));
//    }

    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        // Use the same blocks for all the levels. This lets the user's block code carry over from
        // level to level. The set of blocks shown in the toolbox for each level is defined by the
        // toolbox path below.
        return TURTLE_BLOCK_DEFINITIONS;
    }

//    @Override
//    protected int getActionBarMenuResId() {
//        return R.menu.turtle_actionbar;
//    }

    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        return TURTLE_BLOCK_GENERATORS;
    }

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        // Expose a different set of blocks to the user at each level.
        return "turtle/" + LEVEL_TOOLBOX[getCurrentSectionIndex()];
    }

    @Override
    protected void onInitBlankWorkspace() {
        addDefaultVariables(getController());
    }

    @NonNull
    @Override
    protected ListAdapter onCreateSectionsListAdapter() {
        // Create the game levels with the labels "Level 1", "Level 2", etc., displaying
        // them as simple text items in the sections drawer.
        String[] levelNames = new String[MAX_LEVELS];
        TARGET_BASE_PATH = "/data/data/"+this.getPackageName()+"/";
        levelNames[0] = "ArduBasic";
        levelNames[1] = "ArduAdvanced";

        return new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                levelNames);
    }

    @Override
    protected boolean onSectionChanged(int oldSection, int newSection) {
        Log.e("+oldSection",oldSection+"");
        Log.e("+newSection",newSection+"");
        reloadToolbox();
        return true;
    }




    //wifi , usb 연결 상시 확인
    BroadcastReceiver uploadEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("action",action);
//            Log.e("openUsb",OpenUSB()+"");
            loadXmlFromWorkspace();

            if(action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                // USB was connected
                Log.e("USB 감지 : ", "연결연결");
                mPhysicaloid.open();
                usb_check = true;
//                initial = true;
//                mPhysicaloid.upload(Boards.ARDUINO_UNO, "/data/data/com.learn4/code.ino");
//                OpenUSB();
            }

            if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                // USB was disconnected
                Log.e("USB 감지 : ", "실패실패");
                mPhysicaloid.close();
                usb_check = false;

            }

            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()){
                    wifi_check = true;
                    Log.e("!!wifi","connect");
                    Toast.makeText(getApplicationContext(),"wifi 연결",Toast.LENGTH_SHORT).show();

//                    checkUploadBtn();
                }else{
                    wifi_check = false;
                    Log.e("!!wifi","not connect");
                    Toast.makeText(getApplicationContext(),"wifi 안 연결",Toast.LENGTH_SHORT).show();
//                    checkUploadBtn();
                }


            }


            checkUploadBtn();
        }
    };




    void checkUploadBtn(){
        Log.e("checkUploadBtn","in");
        if (wifi_check&& usb_check){
            Log.e("checkUploadBtn","true");
            categoryData.getUpload_btn().setBackgroundResource(R.drawable.upload_btn_on);
            categoryData.getUpload_btn().setEnabled(true);
        }else{
            Log.e("checkUploadBtn","false");
            categoryData.getUpload_btn().setBackgroundResource(R.drawable.upload_btn_false);
            categoryData.getUpload_btn().setEnabled(false);
        }
    }


    private View.OnClickListener upload_confirm = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show();
            if (!contents_name.equals("none")) {
                if (MySharedPreferences.getInt(getApplicationContext(), contents_name + " MAX") < 5) {
                    MySharedPreferences.setInt(getApplicationContext(), contents_name + " MAX", 5);
                }
            }
                MySharedPreferences.setInt(getApplicationContext(),contents_name,5);
            uploadListener.dismiss();

        }
    };

    private View.OnClickListener submit_confirm = v -> {
        // TODO : LMS 서버 통신
        Toast.makeText(getApplicationContext(), "LMS 에 제출되었습니다", Toast.LENGTH_SHORT).show();
    };

    private View.OnClickListener error_confirm = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show();
            uploadListener.dismiss();
        }
    };

    private View.OnClickListener finish_confirm = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show()
            finishListener.dismiss();
            finish();

        }
    };

    private View.OnClickListener finish_cancel = v -> {
        // TODO : LMS 서버 통신
        finishListener.dismiss();
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideSystemUI();
//        test();



        Log.e("????",contents_name);
        Log.e("onCreate","in");
//        mPhysicaloid.open();
        OpenUSB();
        loadXmlFromWorkspace();

        if (!contents_name.equals("none")){

            if (MySharedPreferences.getInt(getApplicationContext(),contents_name+" MAX") < 4) {
                MySharedPreferences.setInt(getApplicationContext(), contents_name+" MAX", 4);
            }
            MySharedPreferences.setInt(getApplicationContext(),contents_name,4);

        }else{

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        controller = getController();
        controller.recenterWorkspace();
        controller.zoomOut();
        controller.setCopyCheck(this);



        flyoutFragment = new FlyoutFragment();
        flyoutFragment.setCloseCheck(this);


        this.mCategoryView=mBlocklyActivityHelper.getmCategoryView();
        mCategoryView.setItemClick(this);
        this.registerReceiver(uploadEventReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        this.registerReceiver(uploadEventReceiver, new IntentFilter("android.hardware.usb.action.USB_DEVICE_ATTACHED"));
        this.registerReceiver(uploadEventReceiver, new IntentFilter("android.hardware.usb.action.USB_DEVICE_DETACHED"));

        uploadListener = new UploadDialog(this, upload_confirm, submit_confirm, "업로드 성공!","확인을 눌러주세요");
        finishListener = new FinishDialog(this,finish_confirm,finish_cancel);
        error_Listener = new UploadDialog(this, upload_confirm, null, "인터넷 연결 불안정","WIFI를 확인을 해주세요");





        // TODO : 팝업 테스트
        // upload_Listener.show();
//        Log.e("turtle_block",TURTLE_BLOCK_DEFINITIONS.get(6));
    }

    @Subscribe
    public void FinishLoad(PushEvent mPushEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        Log.e("??","finishLoad " + mPushEvent.getPos());
        setLineForOtherCategoryTabs(mPushEvent.getPos());

        Log.e("FinishLoad - case", tempTabCheck[0] + ", " + tempTabCheck[1] + ", " + tempTabCheck[2] + ", ");

        int count = 0;
        for (boolean ttc : tempTabCheck) {
            if (ttc) {
                count += 1;
            }
        }
        if (count >= 2) {
            initTabCheck();
            Log.e("FinishLoad - case", "Double Check Init !");
        }



        switch (mPushEvent.getPos()) {
            // 어택땅
            case 8:
                loadXmlFromWorkspace();
                initTabColor();
                initTabCheck();
                trashcan_btn.setVisibility(View.VISIBLE);
                blockly_monitor.setVisibility(View.GONE);
                view_check[current_pos] = true;
                break;
            default:
                Log.e("code_btn in","어택땅");
                if(mPushEvent.getPos() < 4) {
                    loadXmlFromWorkspace();
                    initTabColor();
                    initTabCheck();
                    break;
                } else if (mPushEvent.getPos() >= 4 && mPushEvent.getPos() != 8){
                    loadXmlFromWorkspace();
                    initTabColor();

                    break;
                } else {
                    loadXmlFromWorkspace();
                }
        }



        if (categoryData.isClosed()) {
            if (current_pos>3) {
//                findViewById(current_tab[categoryData.getPosition()]).setBackgroundColor(getResources().getColor(R.color.white));
                Log.e("닫힘", "ㅎㅎ");
//                translate_btn.setVisibility(View.VISIBLE);
                trashcan_btn.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    protected View onCreateContentView(int parentId) {
        View root = getLayoutInflater().inflate(R.layout.split_content, null);
        mGeneratedFrameLayout = root.findViewById(R.id.generated_workspace);
        Log.e("oncreate","contentview");


        View blockly_workspace = root.findViewById(R.id.blockly_workspace);


        block_copy_btn = blockly_workspace.findViewById(R.id.block_copy_btn);

        block_bot_btn = blockly_workspace.findViewById(R.id.block_bot_btn);
        // 봇 메시지 초기화
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_true_answer);

        blockly_monitor = blockly_workspace.findViewById(R.id.blockly_monitor);
        input_space = blockly_workspace.findViewById(R.id.input_space);
        monitor_text = blockly_workspace.findViewById(R.id.monitor_text);
//        translate_btn = blockly_workspace.findViewById(R.id.translate_btn);
        monitor_text.setMovementMethod(new ScrollingMovementMethod());

        serial_input_box = blockly_workspace.findViewById(R.id.serial_input_box);
        serial_send_btn = blockly_workspace.findViewById(R.id.serial_send_btn);
//        init_btn = blockly_workspace.findViewById(R.id.init_btn);
        scrollview = blockly_workspace.findViewById(R.id.scrollview);
        trashcan_btn = blockly_workspace.findViewById(R.id.blockly_overlay_buttons);

        reset_btn = blockly_workspace.findViewById(R.id.reset_btn);


        guideline4 = blockly_workspace.findViewById(R.id.guideline4);

        baud_rate = blockly_workspace.findViewById(R.id.baud_rate);


        arrayList = new ArrayList<>();
        arrayList.add(9600);
        arrayList.add(19200);
        arrayList.add(38400);
        arrayList.add(115200);

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);

        baud_rate.setAdapter(arrayAdapter);
        baud_rate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPhysicaloid.setBaudrate(arrayList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        block_copy_btn.setOnClickListener(v->{
            if (copy_check){
                copy_check = false;
                block_copy_btn.setBackgroundResource(R.drawable.block_copy_btn_off);
            }else{
                copy_check = true;
                block_copy_btn.setBackgroundResource(R.drawable.block_copy_btn_on);
            }
            controller.setCopyEnabled(copy_check);
        });

        // 테스트 메시지 재생
        block_bot_btn.setOnClickListener(v -> {
            // 봇 메시지 초기화
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            // 답안지 갱신
            loadXmlFromWorkspace();


            // TODO : block compare test
            ParentXml parentXml = new ParentXml(getApplicationContext(),
                    "turtle/demo_workspaces/lv1_blink.xml",
                                                submittedXml);

            Source solution_src = parentXml.getSolutionSource();
            Source submitted_src = parentXml.getSubmittedSource();

            String solution_str = parentXml.getSolutionString();
            String submitted_str = parentXml.getSubmittedString();

            // 채점
            Log.d("Build Bot", "Is that the right answer? : " + solution_str.equals(submitted_str));

//            if (solution_str.equals(submitted_str)) {
//                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_true_answer);
//                mediaPlayer.start();
//
//                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_ok));
//
//                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                alertDialog.setTitle("블록 코딩 튜터");
//                alertDialog.setMessage("정답입니다. 참 잘했어요~!");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        (dialog, which) -> {
//                            block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
//                            dialog.dismiss();
//                        });
//                alertDialog.show();
//            } else {
//                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_false_answer);
//                mediaPlayer.start();
//
//                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_no));
//
//                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                alertDialog.setTitle("블록 코딩 튜터");
//                alertDialog.setMessage("오답입니다. 코드를 다시 확인해주세요.");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        (dialog, which) -> {
//                            block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
//                            dialog.dismiss();
//                        });
//                alertDialog.show();
//            }
            Log.d("Build Bot", "===============================");

            // 정답지, 답안지 IS 초기화
            InputSource solution_is = parentXml.getSolutionInputSource();
            InputSource submitted_is = parentXml.getSubmittedInputSource();

            // 정답지, 답안지 DOM 생성
            parentXml.initDocument();

            Document solution_doc = parentXml.getSolutionDocument();
            Document submitted_doc = parentXml.getSubmittedDocument();

            // 답안지 파싱 작업 시작
            NodeList submitted_statement_nl = submitted_doc.getElementsByTagName("statement");
            NodeList solution_statement_nl = submitted_doc.getElementsByTagName("statement");
            Node submitted_setup_node = null;
            NodeList submitted_setup_nl = null;
            Node submitted_loop_node = null;
            NodeList submitted_loop_nl = null;

            // 1. Setup 이랑 Loop 노드 분리
            for (int i = 0; i < submitted_statement_nl.getLength(); i++) {
                Node n = submitted_statement_nl.item(i);

                // turtle_setup_loop - statement node details (for debug log)
                Log.d("Build Bot", i + " - n0 name : " + n.getNodeName());
                Log.d("Build Bot", i + " - n0 attr name : " + n.getAttributes().getNamedItem("name").getNodeValue());

                // attr name : DO - Setup node
                // attr name : DO1 - Loop node
                switch (n.getAttributes().getNamedItem("name").getNodeValue()) {
                    case "DO":
                        submitted_setup_node = n;
                    case "DO1":
                        submitted_loop_node = n;
                }
            }

            // TODO : 2. Setup 노드 테스트 케이스 작성
            Element e = (Element) submitted_statement_nl.item(0);

            // Setup 의 pinMode " 핀 번호가 13이고 핀 IO가 OUTPUT " 인지, 아닌지 검증
            // 또는, " 답안지가 정답지와 일치 " 했을때 정답처리
            if (e.getElementsByTagName("field").item(0).getTextContent().equals("13") &&
                    e.getElementsByTagName("field").item(1).getTextContent().equals("OUTPUT")
                    ||
                    solution_str.equals(submitted_str)
            ) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_true_answer);
                mediaPlayer.start();

                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_ok));

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("블록 코딩 튜터");
                alertDialog.setMessage("정답입니다. 참 잘했어요~!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> {
                            block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
                            dialog.dismiss();
                        });
                alertDialog.show();
            }
            // 핀 번호만 틀린 경우
            else if (!e.getElementsByTagName("field").item(0).getTextContent().equals("13") &&
                    e.getElementsByTagName("field").item(1).getTextContent().equals("OUTPUT")
            ) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_false_pin);
                mediaPlayer.start();

                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_no));

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("블록 코딩 튜터");
                alertDialog.setMessage("오답입니다. Pin 을 확인해주세요!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> {
                            block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
                            dialog.dismiss();
                        });
                alertDialog.show();
            }
            // 핀 IO만 틀린 경우
            else if (e.getElementsByTagName("field").item(0).getTextContent().equals("13") &&
                    !e.getElementsByTagName("field").item(1).getTextContent().equals("OUTPUT")
            ) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_false_pin_io);
                mediaPlayer.start();

                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_no));

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("블록 코딩 튜터");
                alertDialog.setMessage("오답입니다. Pin IO 를 확인해주세요!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> {
                            block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
                            dialog.dismiss();
                        });
                alertDialog.show();
            }
            // 둘 다 틀린 경우
            else {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_false_answer);
                mediaPlayer.start();

                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_no));

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("블록 코딩 튜터");
                alertDialog.setMessage("오답입니다. 코드를 다시 확인해주세요.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> {
                            block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
                            dialog.dismiss();
                        });
                alertDialog.show();
            }

            Log.d("Build Bot pin number", e.getElementsByTagName("field").item(0).getTextContent());
            Log.d("Build Bot pin IO", e.getElementsByTagName("field").item(1).getTextContent());
            Log.d("Build Bot first line", parentXml.getPreprocessedString(submitted_setup_node.getTextContent()));
        });


        // 테스트 메시지 재생 완료
        // TODO : 테스트 메시지 재생이 끝나면 원상태로 복귀
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);

        reset_btn.setOnClickListener(v->{
            getController().resetWorkspace();
            loadSetupBlock();
        });



        InputMethodManager imm = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow ( serial_input_box.getWindowToken (), 0 );

        categoryData = CategoryData.getInstance();
        categoryData.setClosed(false);
        BusProvider.getInstance().register(this);

//      setupView(blockly_workspace);


        //지울 거
        code_btn =  blockly_workspace.findViewById(R.id.code_btn);
        serial_btn = blockly_workspace.findViewById(R.id.serial_btn);
        upload_btn =  blockly_workspace.findViewById(R.id.upload_btn);
//


//        view_linear = blockly_workspace.findViewById(R.id.view_linear);

//        mUsbReceiver = new mUsbReceiver();

        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.setContentView(R.layout.dialog_progress);

        Display display;  // in Activity
        display = getWindowManager().getDefaultDisplay();
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;

        ViewGroup.LayoutParams layoutParams = guideline4.getLayoutParams();
        layoutParams.width = (int)(width / 1280.0) * 614 ;
        //guideline4.setLayoutParams(layoutParams);
        guideline4.setGuidelineEnd((int) ((width / 1280.0) * 614));

//        if (turtle_pos == 0) {
//            translate_btn.setBackgroundResource(R.drawable.kor_btn_img);
//        }else {
//            translate_btn.setBackgroundResource(R.drawable.eng_btn_img);
//        }

        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        serial_input_box.setOnClickListener(v -> {
            // 에디트 텍스트
        });

        serial_send_btn.setOnClickListener(v -> {
            serial_input = serial_input_box.getText().toString();
            serial_input_box.setText("");

            monitor_text.append(serial_input + "\n");

            serial_write(serial_input);
            imm.hideSoftInputFromWindow ( serial_input_box.getWindowToken (), 0 );
            Toast.makeText(this, "전송", Toast.LENGTH_SHORT).show();
        });

//        init_btn.setOnClickListener(v -> {
//            str = "";
//            monitor_text.setText("");
//            Toast.makeText(this, "초기화", Toast.LENGTH_SHORT).show();
//        });

//


//        translate_btn.setOnClickListener(v ->{
//
//            if (turtle_pos == 0) {
//                turtle_pos = 1;
//                TURTLE_BLOCK_DEFINITIONS.set(1,turtle_files_kor[0]);
//                TURTLE_BLOCK_DEFINITIONS.set(2,turtle_files_kor[1]);
//                TURTLE_BLOCK_DEFINITIONS.set(3,turtle_files_kor[2]);
//                TURTLE_BLOCK_DEFINITIONS.set(5,turtle_files_kor[3]);
//                TURTLE_BLOCK_DEFINITIONS.set(6,turtle_files_kor[4]);
//            }else {
//                turtle_pos = 0;
//                TURTLE_BLOCK_DEFINITIONS.set(1,turtle_files_eng[0]);
//                TURTLE_BLOCK_DEFINITIONS.set(2,turtle_files_eng[1]);
//                TURTLE_BLOCK_DEFINITIONS.set(3,turtle_files_eng[2]);
//                TURTLE_BLOCK_DEFINITIONS.set(5,turtle_files_eng[3]);
//                TURTLE_BLOCK_DEFINITIONS.set(6,turtle_files_eng[4]);
//            }
////
////
////
//            Intent intent = getIntent();
//            overridePendingTransition(0, 0);
//            // 플래그 설정
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent); //현재 액티비티 재실행 실시
//            overridePendingTransition(0, 0);
//            finish(); //현재 액티비티 종료 실시
//        });


//        BlocklyController controller = getController();
//        boolean loadWorkspace = false;
//        String filename = "";
//        loadWorkspace = true;
//        filename = "android.xml";
//
//        if (loadWorkspace) {
//            String assetFilename = "turtle/demo_workspaces/" + filename;
//            try {
//                controller.loadWorkspaceContents(getAssets().open(assetFilename));
//            } catch (IOException | BlockLoadingException e) {
//                throw new IllegalStateException(
//                        "Couldn't load demo workspace from assets: " + assetFilename, e);
//            }
//        }

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            BlocklyController controller = getController();

            // 키보드가 열렸을때
            if (isOpen) {
                // 포커스 된 블록이 있다면
                if (categoryData.getNtp() != null) {
                    // 해당 블록을 기준으로 화면을 이동하고
                    controller.zoomToFocusedBlock(categoryData.getNtp(), categoryData.getPtp(), categoryData.getRtp());
                    // 포커스 된 블록의 좌표값은 초기화
                    categoryData.setRtp(null);
                    categoryData.setPtp(null);
                    categoryData.setNtp(null);
                }
            }
            else {
                //Toast.makeText(getApplicationContext(), "keyboard hidden", Toast.LENGTH_SHORT).show();
            }
        });





        return root;
    }

    //setup loop 블록 workspace에 다시 생성
    void loadSetupBlock(){
        String str = "<xml xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <block type=\"turtle_setup_loop\" x=\"-20.0\" y=\"108.0\" />\n" +
                "</xml>";
        InputStream is = new ByteArrayInputStream(str.getBytes());

        try {
            mBlocklyActivityHelper.loadWorkspaceFromInputStream(is);
        } catch (BlockLoadingException e1) {
            Log.e(TAG, "Failed to load default arduino workspace", e1);
        }
    }

    void loadXmlFromWorkspace() {
        if (getController().getWorkspace().hasBlocks()) {
            mBlocklyActivityHelper.requestCodeGeneration(
                    getBlockGeneratorLanguage(),
                    getBlockDefinitionsJsonPaths(),
                    getGeneratorsJsPaths(),
                    getCodeGenerationCallback());
        }
    }

    // 정답지 로드
    void loadWorkspace(AbstractBlocklyActivity activity) {
        BlocklyController controller = activity.getController();

        String filename = "lv1_blink.xml";
        String assetFilename = "turtle/demo_workspaces/" + filename;

        try {
            controller.loadWorkspaceContents(activity.getAssets().open(assetFilename));
        } catch (IOException | BlockLoadingException e) {
            throw new IllegalStateException(
                    "Couldn't load demo workspace from assets: " + assetFilename, e);
        }
        addDefaultVariables(controller);
    }

    private final Handler mMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO : process device message.
            Log.e("getHandleMessage","" + msg.what);
            switch (msg.what){
                case 0:
                    read_code();
                    Log.e("serial isopen : ",mPhysicaloid.isOpened() + "");
                    scrollview.fullScroll(View.FOCUS_DOWN);
                    sendEmptyMessage(0);
                    break;

                case 1:
                    //Log.e("bye","zz");
                    //upload_code(str);
                    removeMessages(0);
                    str ="";
                    monitor_text.setText("");
                    break;
            }
        }
    } ;


    void setMonitor(int position){

        if (current_pos == position) {
            if (view_check[position]){
                Log.e("setMonitor","in!");
                blockly_monitor.setVisibility(View.VISIBLE);
                trashcan_btn.setVisibility(View.INVISIBLE);
                view_check[position] = false;
                categoryData.setClosed(false);
            }else {
                Log.e("setMonitor","in!2");
                blockly_monitor.setVisibility(View.GONE);
                trashcan_btn.setVisibility(View.VISIBLE);
                view_check[position] = true;
                mMonitorHandler.sendEmptyMessage(1);
            }

        }else{
            Log.e("setMonitor","in!3");
            view_check[position] = false;
            blockly_monitor.setVisibility(View.VISIBLE);
            trashcan_btn.setVisibility(View.INVISIBLE);
            categoryData.setClosed(false);
        }

        current_pos = position;

    }

    void setInitLine(){
        code_btn.setSelected(false);
        serial_btn.setSelected(false);
    }

    public void setLineForOtherCategoryTabs(int position) {
        //mMonitorHandler.sendEmptyMessage(1);
        Log.e("??","setLineForOtherCategoryTabs");
        categoryData.setPosition(position);
        blockly_monitor.setVisibility(View.GONE);
        switch (position) {
            case -1:
                setInitLine();
                break;
            case 0:
                setInitLine();
                setCategoryTabsColor(position);
                break;
            case 1:
                setInitLine();
                setCategoryTabsColor(position);
                break;
            case 2:
                setInitLine();
                setCategoryTabsColor(position);
                break;
            case 3:
                setInitLine();
                setCategoryTabsColor(position);
                break;

            default:
                break;
        }
    }

    public void setCategoryTabsColor(int position){
        if (current_pos == position) {
            if (view_check[position]){
                trashcan_btn.setVisibility(View.INVISIBLE);
                view_check[position] = false;
                categoryData.setClosed(false);
            }else {
                trashcan_btn.setVisibility(View.VISIBLE);
                view_check[position] = true;
            }

        }else{
            view_check[position] = false;
            trashcan_btn.setVisibility(View.INVISIBLE);
            categoryData.setClosed(false);
        }

        current_pos = position;
    }



    @Override
    public void onBackPressed() {
        finishListener.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMonitorHandler.sendEmptyMessage(1);
        // 봇 메시지 재생 종료
        mediaPlayer.release();
//        if (wifiEventReceiver != null) {
//            unregisterReceiver(wifiEventReceiver);
//            wifiEventReceiver = null;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 봇 메시지 재생 종료
        mediaPlayer.release();
    }

    @Override
    public void onClearWorkspace() {
        super.onClearWorkspace();
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        Log.e("in!","getCodeGenerationCallback");
        return mCodeGeneratorCallback;
    }

    static void addDefaultVariables(BlocklyController controller) {
        // TODO: (#22) Remove this override when variables are supported properly
        controller.addVariable("item");
        Log.e("addDefaultVariables","in");
    }

    /**
     * Optional override of the save path, since this demo Activity has multiple Blockly
     * configurations.
     * @return Workspace save path used by this Activity.
     */
    @Override
    @NonNull
    protected String getWorkspaceSavePath() {
        return SAVE_FILENAME;
    }

    /**
     * Optional override of the auto-save path, since this demo Activity has multiple Blockly
     * configurations.
     * @return Workspace auto-save path used by this Activity.
     */
    @Override
    @NonNull
    protected String getWorkspaceAutosavePath() {
        Log.e("in!","getWorkspaceAutosavePath");
        contents_name = getIntent().getStringExtra("contents_name");
        if (contents_name.equals("none")){
            Log.e("none","in");
            return AUTOSAVE_FILENAME;
        }
        Log.e("none","not in");
        return AUTOSAVE_FILENAME2;
    }


    public void onClick(View view) {
        mGeneratedErrorTextView.setVisibility(View.GONE);
    }

    public String getCompiler() {
        Log.e("??","ddddd");
        JSONObject obj = null;
        String url = null;
//        try {
//            obj = new JSONObject(loadJSONFromFile());
//            url = obj.get("url").toString();
//            Log.e("dg","zzz");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("hi","hi");
//            Log.e("getCompiler Error",e.toString());
//        }
        // TODO : 컴파일러 세팅
        return "http://52.79.240.76:5000";

        // 테스트용 구라 주소
        //return "http://87.93.87.93:5000";
    }


    @Override
    public void onClickTest(View v, int pos) {
        Log.e("??","onClickTest");
        Log.e("main create",pos+"");
        Log.e("isEnabled onClickTest",v.isSelected()+"");
        if (pos >= 4) {
            tempTab[pos-4] = v;

            Log.e("before case", tempTabCheck[pos-4] + "");
            //Log.e("in case", tempTabCheck[pos-4] + "");
        } else {
            initTabColor();
            initTabCheck();
        }

        // TODO : 상단 버튼 대응
        switch (pos) {
            // code
            case 4:
                code_btn(pos);
                break;

            // serial monitor
            case 5:
                serial_btn(pos);
                break;

            // upload
            case 6:
                Log.e("6 in","come");
                upload_btn(pos);
                break;
        }
    }

    public void initTabColor() {
        for (View v : tempTab) {
            if (v != null) {
                v.setSelected(false);
            }
        }
        Log.e("initTabColor - case", tempTab[0] + ", " + tempTab[1] + ", " + tempTab[2] + ", ");
    }

    public void initTabCheck() {
        tempTabCheck = new Boolean[] {false, false, false};
        Log.e("initTabCheck - case", tempTabCheck[0] + ", " + tempTabCheck[1] + ", " + tempTabCheck[2] + ", ");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUI();
        }
    }


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    @Override
    public void onCloseCheck(String msg) {
        //Log.e("들어온다!!",msg);


        try {
            if (categoryData.getPosition() < 4) {
                Log.e("hi","zz");
//                findViewById(current_tab[categoryData.getPosition()]).setBackgroundColor(getResources().getColor(R.color.white));
                view_check[current_pos] = true;
//                translate_btn.setVisibility(View.VISIBLE);
                trashcan_btn.setVisibility(View.VISIBLE);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCopyCheck(boolean check) {
        copy_check = check;
        Log.e("in!",copy_check+"");
        controller.setCopyEnabled(copy_check);
        block_copy_btn.setBackgroundResource(R.drawable.block_copy_btn_off);
    }

    public void code_btn(int pos) {
        setInitLine();
        code_btn.setSelected(true);
        mMonitorHandler.sendEmptyMessage(1);
        monitor_text.setText("");
        Log.e("hi code_btn","click");
        categoryData.setPosition(4);
        categoryData.setSelection(true);
        categoryData.setClosed(true);
        setMonitor(4);
        mBlocklyActivityHelper.getFlyoutController();


        input_space.setVisibility(View.GONE);
//                init_btn.setVisibility(View.GONE);z

        if (getController().getWorkspace().hasBlocks()) {
            mBlocklyActivityHelper.requestCodeGeneration(
                    getBlockGeneratorLanguage(),
                    getBlockDefinitionsJsonPaths(),
                    getGeneratorsJsPaths(),
                    getCodeGenerationCallback());
        }



        Handler handler = new Handler();
        handler.postDelayed(() -> {

            String size1 = "file size : "+getFileSize()+"bytes\n\n\n";
//                    monitor_text.setText(size+mFormat.format(first_time)+"\n"+mFormat.format(last_time)+"\n"+code);
            monitor_text.setText(code);
        }, 100);


        if (tempTabCheck[pos-4]) {
            blockly_monitor.setVisibility(View.GONE);
            initTabColor();
            initTabCheck();
        } else {
            tempTabCheck[pos-4] = !tempTabCheck[pos-4];
            tempTab[pos-4].setSelected(tempTabCheck[pos-4]);
            Log.e("after case", tempTabCheck[pos-4] + "");
        }
    }

    public void serial_btn(int pos) {
        setInitLine();
        serial_btn.setSelected(true);
        monitor_text.setText("");

        mPhysicaloid.open();
        Log.e("isOpened Serial","" + mPhysicaloid.isOpened());

        categoryData.setPosition(5);
        categoryData.setClosed(true);
        setMonitor(5);
        mBlocklyActivityHelper.getFlyoutController();

        input_space.setVisibility(View.VISIBLE);
        Log.e("serial","btn");

        stringBuilder.setLength(0);
        num = 0;

        if (tempTabCheck[pos-4]) {
            blockly_monitor.setVisibility(View.GONE);
            initTabColor();
            initTabCheck();
        } else {
            tempTabCheck[pos-4] = !tempTabCheck[pos-4];
            tempTab[pos-4].setSelected(tempTabCheck[pos-4]);
            Log.e("after case", tempTabCheck[pos-4] + "");
            mMonitorHandler.sendEmptyMessage(0);
        }
    }

    public void upload_btn(int pos) {
        hideSystemUI();
        if (wifi_check) {
//                    first_time = System.currentTimeMillis();
//                    Toast.makeText(getApplicationContext(), "first_time : "+mFormat.format(first_time), Toast.LENGTH_SHORT).show();
            mMonitorHandler.sendEmptyMessage(1);
            customProgressDialog.show();
//                    last_time = System.currentTimeMillis();
//                    Toast.makeText(getApplicationContext(), "last_time : "+mFormat.format(last_time), Toast.LENGTH_SHORT).show();
            blockly_monitor.setVisibility(View.GONE);
            mBlocklyActivityHelper.getFlyoutController();
            categoryData.setPosition(6);
            current_pos = 6;
            setInitLine();
            compileCheck = true;
            if (getController().getWorkspace().hasBlocks()) {
                Log.e("??", "들어옴");
                mBlocklyActivityHelper.requestCodeGeneration(
                        getBlockGeneratorLanguage(),
                        getBlockDefinitionsJsonPaths(),
                        getGeneratorsJsPaths(),
                        getCodeGenerationCallback());
            }

        } else {
            Toast.makeText(getApplicationContext(),"WIFI를 연결해주세요!",Toast.LENGTH_SHORT).show();
        }


        current_pos = 6;
    }
}
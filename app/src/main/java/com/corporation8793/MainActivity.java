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

package com.corporation8793;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

import com.google.blockly.android.FlyoutFragment;
import com.google.blockly.android.OnCloseCheckListener;
import com.google.blockly.android.ui.BusProvider;
import com.google.blockly.android.ui.CategoryData;

import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.blockly.android.AbstractBlocklyActivity;
import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.TabItemClick;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;

import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.android.ui.PushEvent;
import com.google.blockly.model.DefaultBlocks;
import com.google.blockly.utils.BlockLoadingException;
import com.physicaloid.lib.Boards;
import com.physicaloid.lib.Physicaloid;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Demo app with the Blockly Games turtle game in a webview.
 */
public class MainActivity extends BlocklySectionsActivity implements TabItemClick , OnCloseCheckListener {


    private static final String TAG = "TurtleActivity";

    private static final String SAVE_FILENAME = "turtle_workspace.xml";
    private static final String AUTOSAVE_FILENAME = "turtle_workspace_temp.xml";
    private TextView mGeneratedTextView;
    private TextView mGeneratedErrorTextView;
    private FrameLayout mGeneratedFrameLayout;
    private String mNoCodeText;
    private String mNoErrorText;
    private CategoryView mCategoryView;
    FlyoutFragment flyoutFragment;

    private Boolean initial=true, wifi_check = false, usb_check = false;
    Boolean compileCheck = false;
    private EditText editURL;
    View setup_view, loop_view, method_view, etc_view, code_view, serial_view, upload_view;
    int [] current_tab = {R.id.setup_view,R.id.loop_view,R.id.method_view,R.id.etc_view,R.id.code_view,R.id.serial_view,R.id.upload_view};
    EditText serial_input_box;
    Button serial_send_btn, init_btn, translate_btn, code_btn, serial_btn;
    LinearLayout blockly_monitor, input_space;
    String code = "",current_tag ="", serial_code="", serial_input="";
    TextView monitor_text;
    CategoryData categoryData;
    String TARGET_BASE_PATH;
    UsbDevice bigBoard;
    BroadcastReceiver mUsbReceiver;
    ProgressDialog customProgressDialog;
    String str ="";
    ScrollView scrollview;
    int num = 0;

    Guideline guideline4;
    Button upload_btn;

    private UploadDialog upload_Listener, error_Listener;

    LinearLayout view_linear;
    int current_pos =0, pre_pos=0;
    static int turtle_pos=0;
//    String current_tag;


    //시니얼 모니터 slow 방지 문자열
    StringBuilder stringBuilder = new StringBuilder();


    byte[] buffer = new byte[1024];  // buffer store for the stream
    int bytes; // bytes returned from read()
    Physicaloid mPhysicaloid = new Physicaloid(this);

    Boolean [] view_check = {true,true,true,true};

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

    private final Handler mHandler = new Handler();
    public void read_code() {
        Log.e("generated", "리드");
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        if(deviceList.isEmpty()){
            Log.e("generated : ", "isempty 디바이스 리스트");
        }
        else {
            Set<String> keys = deviceList.keySet();
            for (String key : keys) {
                bigBoard = deviceList.get(key);
            }
        }

        try {
            mPhysicaloid.open(9600, bigBoard);
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

    //private WebView mTurtleWebview;
    private final CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback =
            new CodeGenerationRequest.CodeGeneratorCallback() {
                @Override
                public void onFinishCodeGeneration(final String generatedCode) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            code = generatedCode;
//                            updateTextMinWidth();
                        }
                    });

                    if(generatedCode.contains("alert")) {
                        String[] alert = generatedCode.split("!!");
                        Toast.makeText(getApplicationContext(), alert[2], Toast.LENGTH_LONG).show();
                        code = generatedCode;
//                        Log.e("generated",generatedCode);
                    }
                    else {

                        // Sample callback.
                        //Log.i(TAG, "generatedCode:\n" + generatedCode);
                        // System.out.println( "generatedCode:\n" + generatedCode);
                        //Toast.makeText(getApplicationContext(), generatedCode,Toast.LENGTH_LONG).show();
                    /*ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", generatedCode);
                    clipboard.setPrimaryClip(clip);*/
                        // Intent launchIntent = getPackageManager().getLaunchIntentForPackage("name.antonsmirnov.android.arduinodroid2");
                        //   if (launchIntent != null) {
                        // mPhysicaloid.upload(Boards.ARDUINO_UNO, "/storage/emulated/0/code/Blink.hex");
                        // try {
                        //  get_ports();
//                        System.out.println(generatedCode);
//                        Log.e("generated",generatedCode);
                        code = generatedCode;
                        create_file(generatedCode,"code.ino");
                        Log.e("!@","nono");
                        if (compileCheck) {
                            Log.e("generated", "컴파컴파");
                            remotecompile("code.ino", getCompiler());
                            compileCheck = false;
                            customProgressDialog.dismiss();
                        }


                        //  execute_shell("ls");


//                            execute_shell("touch Blink.cpp");
//                            execute_shell("cp hardware/arduino/cores/arduino/main.cpp Blink.cpp");
//
//                            execute_shell("sed -i wBlink1.cpp Blink.cpp files/Blink.ino");
//                            execute_shell("avr-g++")


                        //--execute_shell("cat Blink1.cpp");
                        // --execute_shell("/storage/emulated/0/code/hardware/tools/avr/bin/avr-g++");
                        // --execute_shell("sh -c avr-g++");
                        // -- execute_shell_2(new String[]{"sh -c", "/data/data/com.google.blockly.demo/hardware/tools/avr/bin/avr-g++"});
                        //--execute_shell_2(new String[]{"sh", "/storage/emulated/0/code/hardware/tools/avr/bin/avr-g++"});
                        //  --execute_shell(new String[] {"avr-g++","-x", "c++", "-MMD", "-c", "-mmcu=atmega328p", "-Wall", "-DF_CPU=16000000L", "-DARDUINO=160", "-DARDUINO_ARCH_AVR", "-D__PROG_TYPES_COMPAT__", "-I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino", "-I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard", "-Wall", "-Os", "Blink1.cpp"});



                        //--execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os Blink1.cpp");
                        /*  execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/wiring_digital.c");
                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/wiring.c");
                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/wiring_analog.c");

                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/wiring_pulse.c");
                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/wiring_shift.c");

                            //execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/CDC.cpp");
                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/HardwareSerial.cpp");

                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/WString.cpp");
                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/WMath.cpp");

                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/WInterrupts.c");
                           // execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/USBCore.cpp");

                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/Tone.cpp");
                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/Stream.cpp");

                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/Print.cpp");
                            execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/IPAddress.cpp");
*/
                        //  execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/HID.cpp");
                        // --execute_shell("avr-g++ -x c++ -MMD -c -mmcu=atmega328p -Wall -DF_CPU=16000000L -DARDUINO=160 -DARDUINO_ARCH_AVR -D__PROG_TYPES_COMPAT__ -I/data/data/com.google.blockly.demo/hardware/arduino/cores/arduino -I/data/data/com.google.blockly.demo/hardware/arduino/variants/standard -Wall -Os /data/data/com.google.blockly.demo/hardware/arduino/cores/arduino/LiquidCrystal.cpp");

                        //   execute_shell("avr-ar rcs libcore.a wiring.o wiring_digital.o wiring_analog.o wiring_shift.o wiring_pulse.o WMath.o WString.o WInterrupts.o Tone.o Stream.o Print.o IPAddress.o HardwareSerial.o");
                        //--execute_shell("avr-ar rcs core.a CDC.cpp.o LiquidCrystal.o HardwareSerial.cpp.o HID.cpp.o IPAddress.cpp.o malloc.c.o new.cpp.o main.cpp.o new.cpp.o Print.cpp.o realloc.c.o Stream.cpp.o Tone.cpp.o Tone.cpp.o USBCore.cpp.o WInterrupts.c.o wiring.c.o wiring_analog.c.o wiring_digital.c.o wiring_pulse.c.o wiring_shift.c.o WMath.cpp.o WString.cpp.o");
                        //--execute_shell("avr-gcc -mmcu=atmega328p -Wl,--gc-sections -Os -o Blink1.elf Blink1.o core.a -lc -lm");
                        //--execute_shell("avr-objcopy -O ihex -R .eeprom Blink1.elf Blink1.hex");
                        //--Toast.makeText(getApplicationContext(), "Compilation Success, trying to upload code!!",Toast.LENGTH_LONG).show();


                        // execute_shell("chmod -R 700 hardware");
                        //execute_shell("echo hi");
                        // execute_shell("rm -rf Blink.cpp");
                        //} catch (IOException e) {
                        //   Toast.makeText(getApplicationContext(), "Error Compiling", Toast.LENGTH_LONG).show();
                        //}





                        //  startActivity(launchIntent);//null pointer check in case package name was not found
                        // }
                    /*mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String encoded = "Turtle.execute("
                                    + JavascriptUtil.makeJsString(generatedCode) + ")";
                            mTurtleWebview.loadUrl("javascript:" + encoded);
                        }
                    });*/
                    }

                }
            };

    public void serial_write(String str){
        if (mPhysicaloid.isOpened()) {
            if(mPhysicaloid.open(9600, bigBoard)) {
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
            Log.e("generated : ", "isempty 디바이스 리스트");
        }
        else {
            Boolean value = OpenUSB();
            if (value) {
//                Toast.makeText(getApplicationContext(), "Compilation Success, Uploading", Toast.LENGTH_LONG).show();
                mPhysicaloid.upload(Boards.ARDUINO_UNO, file);
//                Log.e("generated",code);
//                Toast.makeText(getApplicationContext(), "Uploading Completed", Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(),"업로드 성공!",Toast.LENGTH_SHORT).show();
                upload_Listener.show();

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                Window window = upload_Listener.getWindow();

                int x = (int)(size.x * 0.5f);
                int y = (int)(size.y * 0.45f);

                window.setLayout(x,y);
            }
            else {
                Toast.makeText(getApplicationContext(),"한번 더 업로드 버튼을 눌러주세요",Toast.LENGTH_SHORT).show();
//                // mGeneratedErrorTextView.setVisibility(View.VISIBLE);
//                //  mGeneratedErrorTextView.setText("ERROR: Connect USB and try again");
            }
        }

//        Toast.makeText(getApplicationContext(), "Check if Program uploaded ", Toast.LENGTH_LONG).show();

    }

    public Boolean OpenUSB() {
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
                        }
                        //System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.toString().equals("com.android.volley.error.ServerError")) {
                    Log.e("server error log",error.getMessage());
                    //       mGeneratedErrorTextView.setVisibility(View.VISIBLE);
                    //       mGeneratedErrorTextView.setText("Error:\n\t Problem Connecting Remote Compiler: null reply from compiler");
                }
                else if(error.getMessage().contains("java.net.ConnectException")) {
                    Log.e("Remote error log",error.getMessage());
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
    BroadcastReceiver usbEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("action",action);

            if(action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                // USB was connected
                Log.e("USB 감지 : ", "연결연결");
                mPhysicaloid.open();
                usb_check = true;
            }

            if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                // USB was disconnected
                Log.e("USB 감지 : ", "실패실패");
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
        if (wifi_check && usb_check){
            Log.e("checkUploadBtn","true");
            upload_btn.setBackgroundResource(R.drawable.upload_btn);
            upload_btn.setEnabled(true);
        }else{
            Log.e("checkUploadBtn","false");
            upload_btn.setBackgroundResource(R.drawable.upload_btn_false);
            upload_btn.setEnabled(false);
        }
    }


    private View.OnClickListener upload_confirm = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show();
            upload_Listener.dismiss();
        }
    };

    private View.OnClickListener error_confirm = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show();
            upload_Listener.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
//        test();
        BlocklyController controller = getController();
        controller.recenterWorkspace();
        controller.zoomOut();

        flyoutFragment = new FlyoutFragment();
        flyoutFragment.setCloseCheck(this);

        this.mCategoryView=mBlocklyActivityHelper.getmCategoryView();
        mCategoryView.setItemClick(this);
        this.registerReceiver(usbEventReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        this.registerReceiver(usbEventReceiver, new IntentFilter("android.hardware.usb.action.USB_DEVICE_ATTACHED"));
        this.registerReceiver(usbEventReceiver, new IntentFilter("android.hardware.usb.action.USB_DEVICE_DETACHED"));

        upload_Listener = new UploadDialog(this, upload_confirm,"업로드 성공!","확인을 눌러주세요");
        error_Listener = new UploadDialog(this, upload_confirm,"인터넷 연결 불안정","WIFI를 확인을 해주세요");


//        Log.e("turtle_block",TURTLE_BLOCK_DEFINITIONS.get(6));
    }

    @Subscribe
    public void FinishLoad(PushEvent mPushEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        Log.e("??","finishLoad");
        setLineForOtherCategoryTabs(mPushEvent.getPos());
        blockly_monitor.setVisibility(View.GONE);

        if (categoryData.isClosed()) {
            if (current_pos>3) {
                findViewById(current_tab[categoryData.getPosition()]).setBackgroundColor(getResources().getColor(R.color.white));
                Log.e("닫힘", "ㅎㅎ");
//                translate_btn.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    protected View onCreateContentView(int parentId) {
        View root = getLayoutInflater().inflate(R.layout.split_content, null);
        mGeneratedFrameLayout = root.findViewById(R.id.generated_workspace);
        Log.e("oncreate","contentview");


        View blockly_workspace = root.findViewById(R.id.blockly_workspace);


        blockly_monitor = blockly_workspace.findViewById(R.id.blockly_monitor);
        input_space = blockly_workspace.findViewById(R.id.input_space);
        monitor_text = blockly_workspace.findViewById(R.id.monitor_text);
//        translate_btn = blockly_workspace.findViewById(R.id.translate_btn);
        monitor_text.setMovementMethod(new ScrollingMovementMethod());

        serial_input_box = blockly_workspace.findViewById(R.id.serial_input_box);
        serial_send_btn = blockly_workspace.findViewById(R.id.serial_send_btn);
//        init_btn = blockly_workspace.findViewById(R.id.init_btn);
        scrollview = blockly_workspace.findViewById(R.id.scrollview);

        guideline4 = blockly_workspace.findViewById(R.id.guideline4);


        InputMethodManager imm = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow ( serial_input_box.getWindowToken (), 0 );

        categoryData = CategoryData.getInstance();
        categoryData.setClosed(false);
        BusProvider.getInstance().register(this);

        setupView(blockly_workspace);

        code_btn =  blockly_workspace.findViewById(R.id.code_btn);
        serial_btn = blockly_workspace.findViewById(R.id.serial_btn);
        upload_btn =  blockly_workspace.findViewById(R.id.upload_btn);

        view_linear = blockly_workspace.findViewById(R.id.view_linear);

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
//
//
//
//            Intent intent = getIntent();
//            overridePendingTransition(0, 0);
//            // 플래그 설정
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent); //현재 액티비티 재실행 실시
//            overridePendingTransition(0, 0);
//            finish(); //현재 액티비티 종료 실시
//        });


        code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMonitorHandler.sendEmptyMessage(1);
                monitor_text.setText("");
                Log.e("hi code_btn","click");
                categoryData.setPosition(4);
                categoryData.setClosed(true);
                current_pos = 4;
                setMonitor(code_btn.getTag().toString(),code_view);
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
                    monitor_text.setText(code);
                }, 100);
                current_pos = 4;
            }
        });

        serial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monitor_text.setText("");
                mMonitorHandler.sendEmptyMessage(0);
                categoryData.setPosition(5);
                categoryData.setClosed(true);
                current_pos = 5;
                setMonitor(serial_btn.getTag().toString(),serial_view);
                mBlocklyActivityHelper.getFlyoutController();

//                setInitLine();
                input_space.setVisibility(View.VISIBLE);
                Log.e("serial","btn");

                stringBuilder.setLength(0);
                num = 0;
                current_pos = 5;
            }
        });


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
                if (wifi_check) {
                    mMonitorHandler.sendEmptyMessage(1);
                    customProgressDialog.show();
                    blockly_monitor.setVisibility(View.GONE);
                    mBlocklyActivityHelper.getFlyoutController();
                    categoryData.setPosition(6);
                    current_pos = 6;
                    setInitLine();
                    upload_view.setBackgroundColor(Color.parseColor("#f78f43"));
                    compileCheck = true;
                    if (getController().getWorkspace().hasBlocks()) {
                        Log.e("??", "들어옴");
                        mBlocklyActivityHelper.requestCodeGeneration(
                                getBlockGeneratorLanguage(),
                                getBlockDefinitionsJsonPaths(),
                                getGeneratorsJsPaths(),
                                getCodeGenerationCallback());
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"WIFI를 연결해주세요!",Toast.LENGTH_SHORT).show();
                }


                current_pos = 6;
            }
        });



        return root;
    }



    private final Handler mMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO : process device message.
            switch (msg.what){
                case 0:
                    read_code();
                    Log.e("serial isopen : ",mPhysicaloid.isOpened() + "");
                    scrollview.fullScroll(View.FOCUS_DOWN);
                    sendEmptyMessage(0);
                    break;

                case 1:
                    Log.e("bye","zz");
                    removeMessages(0);
                    str ="";
                    monitor_text.setText("");
                    break;
            }
        }
    } ;


    void setMonitor(String tag,View view){

        setInitLine();
        if (current_tag.equals(tag)){
            if (blockly_monitor.getVisibility()==View.GONE) {
                blockly_monitor.setVisibility(View.VISIBLE);
//                translate_btn.setVisibility(View.INVISIBLE);
                view.setBackgroundColor(Color.parseColor("#f78f43"));
                Log.e("??","들어오는데");
            }else {
                setInitLine();
                blockly_monitor.setVisibility(View.GONE);
//                translate_btn.setVisibility(View.VISIBLE);
                Log.e("??2","들어오는데");
                mMonitorHandler.sendEmptyMessage(1);

            }
            }else{
            view.setBackgroundColor(Color.parseColor("#f78f43"));
            blockly_monitor.setVisibility(View.VISIBLE);
//            translate_btn.setVisibility(View.INVISIBLE);
            Log.e("??3","들어오는데");
        }

        current_tag = tag;
    }

    void setupView(View view){
        setup_view = view.findViewById(R.id.setup_view);
        loop_view = view.findViewById(R.id.loop_view);
        method_view = view.findViewById(R.id.method_view);
        etc_view = view.findViewById(R.id.etc_view);
        code_view = view.findViewById(R.id.code_view);
        serial_view = view.findViewById(R.id.serial_view);
        upload_view = view.findViewById(R.id.upload_view);
    }

    void setInitLine(){
        setup_view.setBackgroundColor(getResources().getColor(R.color.white));
        loop_view.setBackgroundColor(getResources().getColor(R.color.white));
        method_view.setBackgroundColor(getResources().getColor(R.color.white));
        etc_view.setBackgroundColor(getResources().getColor(R.color.white));
        code_view.setBackgroundColor(getResources().getColor(R.color.white));
        serial_view.setBackgroundColor(getResources().getColor(R.color.white));
        upload_view.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void setLineForOtherCategoryTabs(int position) {
        mMonitorHandler.sendEmptyMessage(1);
        Log.e("??","gg");
        switch (position) {
            case -1:
                categoryData.setPosition(position);
                setInitLine();
                break;
            case 0:
                categoryData.setPosition(position);
                setInitLine();
                setCategoryTabsColor(position,setup_view);
                break;
            case 1:
                categoryData.setPosition(position);
                setInitLine();
                setCategoryTabsColor(position,loop_view);
                break;
            case 2:
                categoryData.setPosition(position);
                setInitLine();
                setCategoryTabsColor(position,method_view);
                break;
            case 3:
                categoryData.setPosition(position);
                setInitLine();
                setCategoryTabsColor(position,etc_view);
                break;
            default:
                break;
        }
    }

    public void setCategoryTabsColor(int position, View view){
        Log.e("setCategoryTabs",categoryData.isClosed()+"");
        if (current_pos == position) {
            if (view_check[position]){
                view.setBackgroundColor(Color.parseColor("#f78f43"));
//                translate_btn.setVisibility(View.INVISIBLE);
                view_check[position] = false;
                categoryData.setClosed(false);
            }else {
//                translate_btn.setVisibility(View.VISIBLE);
                view_check[position] = true;
            }

        }else{
            view_check[position] = false;
            view.setBackgroundColor(Color.parseColor("#f78f43"));
//            translate_btn.setVisibility(View.INVISIBLE);
            categoryData.setClosed(false);
        }

        current_pos = position;
    }



    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("배울래를 종료하시겠습니까?");
        builder.setPositiveButton("아니오",(dialog, which) -> {
            dialog.cancel();});
        builder.setNegativeButton("예",(dialog, which) -> {finish();});
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMonitorHandler.sendEmptyMessage(1);
//        if (wifiEventReceiver != null) {
//            unregisterReceiver(wifiEventReceiver);
//            wifiEventReceiver = null;
//        }
    }

    @Override
    public void onClearWorkspace() {
        super.onClearWorkspace();
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
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
        return AUTOSAVE_FILENAME;
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
        return "http://13.124.237.59:5000 ";
    }


    @Override
    public void onClickTest(int pos) {
        Log.e("main create",pos+"");
//        blockly_monitor.setVisibility(View.GONE);
        //여기서 버튼 underline 바뀌는 코드 작성하면 됨
        if (pos ==0){

        }else if (pos ==1){

        }else if (pos == 2){

        }else{

        }
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
        Log.e("들어온다!!",msg);

        try {
            if (categoryData.getPosition() < 4) {
                Log.e("hi","zz");
                findViewById(current_tab[categoryData.getPosition()]).setBackgroundColor(getResources().getColor(R.color.white));
                view_check[current_pos] = true;
//                translate_btn.setVisibility(View.VISIBLE);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }


    }
}

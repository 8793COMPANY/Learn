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

package com.learn4.view;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.error.TimeoutError;
import com.google.blockly.android.BlockClickDialog;
import com.google.blockly.android.UploadBtnCheck;
import com.google.blockly.android.BlockDropdownClick;
import com.google.blockly.android.ui.PendingDrag;
import com.google.blockly.android.ui.fieldview.BasicFieldDropdownView;

import com.google.blockly.model.FieldDropdown;

import com.learn4.data.dto.SimulatorComponent;
import com.learn4.data.room.AppDatabase2;
import com.learn4.data.room.dao.BlockDictionaryDao;
import com.learn4.data.room.entity.BlockDictionary;
import com.learn4.tutor.TutorCheck;
import com.learn4.util.FileSharedPreferences;
import com.learn4.util.NetworkConnection;
import com.learn4.util.Application;
import com.learn4.util.MySharedPreferences;
import com.learn4.R;
import com.learn4.view.custom.dialog.BuildBotDialog;
import com.learn4.view.custom.dialog.NameInputDialog;
import com.learn4.view.custom.dialog.UploadFalseDialog;
import com.learn4.view.problem.basic.ProblemActivity;
import com.learn4.view.simulator.SimulatorAdapter;
import com.learn4.view.simulator.SimulatorDialog;
import com.learn4.view.dictionary.CodeDictionaryAdapter;
import com.learn4.view.custom.dialog.FinishDialog;
import com.learn4.view.custom.dialog.ProgressDialog;
import com.learn4.view.custom.dialog.UploadDialog;
import com.learn4.data.dto.CodeBlock;
import com.google.blockly.android.FlyoutFragment;
import com.google.blockly.android.OnCloseCheckListener;
import com.google.blockly.android.ui.BusProvider;
import com.google.blockly.android.ui.CategoryData;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.blockly.android.AbstractBlocklyActivity;
import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.TabItemClick;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;

import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.android.ui.PushEvent;
import com.google.blockly.model.Block;
import com.google.blockly.model.BlocklyCategory;
import com.google.blockly.model.DefaultBlocks;
import com.google.blockly.utils.BlockLoadingException;
import com.learn.wp_rest.data.acf.UploadReportJson;
import com.learn.wp_rest.data.wp.media.Media;
import com.learn.wp_rest.data.wp.posts.UploadReport;
import com.learn4.util.Constants;
import com.learn4.view.simulator.TeachableActivity;
import com.physicaloid.lib.Boards;
import com.squareup.otto.Subscribe;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import kotlin.Pair;


/**
 * Demo app with the Blockly Games turtle game in a webview.
 */
public class MainActivity extends BlocklySectionsActivity implements TabItemClick , OnCloseCheckListener , UploadBtnCheck, BlockDropdownClick {
    //2021.07.23 제일 최신 버전


    private static final String TAG = "TurtleActivity";

    private static String SAVE_FILENAME = "turtle_workspace.xml";
    private static String AUTOSAVE_FILENAME = "turtle_workspace_temp.xml";
    private static final String AUTOSAVE_FILENAME2 = "turtle_workspace_temp2.xml";
    private TextView mGeneratedErrorTextView;
    private FrameLayout mGeneratedFrameLayout;

    BasicFieldDropdownView basicFieldDropdownView;
    FieldDropdown fieldDropdown;
    private CategoryView mCategoryView;
    FlyoutFragment flyoutFragment;
    View [] block_tempTab = {null, null, null,null};
    View [] tempTab = {null, null, null,null,null,null,null};
    Boolean [] tempTabCheck = {false, false, false,false,false,false,false};

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS");

    private Boolean initial=true,  server_upload_check = true;
    Boolean compileCheck = false, copy_check = false;
    private EditText editURL;
    EditText serial_input_box;

    Button serial_send_btn, translate_btn, ai_test_btn;
    Button code_save_btn, code_load_btn;
    public Button  close_btn;

    Button block_setup_btn, block_loop_btn, block_method_btn, block_etc_btn;

    public ImageView block_bot_btn;
    Button simulator_btn, component_close_btn, move_btn;
    TextView code_view;
    MediaPlayer mediaPlayer;
    Bitmap bitmapWorkspace;

    LinearLayout trashcan_btn;
    LinearLayout blockly_monitor, input_space;
    String code = "", serial_input="";
    TextView monitor_text;
    CategoryData categoryData;
    String TARGET_BASE_PATH;
    UsbDevice bigBoard;
    ProgressDialog customProgressDialog;
    String str ="", submittedXml = "";
    ScrollView scrollview;
    int num = 0;
    String contents_name ="none", chapter_id = "none";
    String [] chapter_id_split;
    String id ="none";

    Guideline guideline4;

    ConstraintLayout block_dictionary;
    RecyclerView block_list, component_list;
    ConstraintLayout block_simulator;

    WebView simulator_web_view;

    ArrayList<String> file_list;
    ArrayList<Integer> arrayList;
    ArrayList<CodeBlock> dictionary_block_list = new ArrayList<>();
    ArrayList<SimulatorComponent> simulator_component_list = new ArrayList<>();
    ArrayAdapter<Integer> arrayAdapter;
    Spinner baud_rate;


    private UploadDialog uploadListener, error_Listener;
    private FinishDialog finishListener, resetListener;
    private SimulatorDialog simulatorDialog;
    private BuildBotDialog buildbotDialog;
    private NameInputDialog nameInputDialog;

    int current_pos =0, check_num = 1;
    static int turtle_pos = 0;
    boolean simulator_check = false;

    AppDatabase2 db2 = null;
    public BlockDictionaryDao blockDictionaryDao;
    List<BlockDictionary> blockDictionaryList = new ArrayList<>();

    //시니얼 모니터 slow 방지 문자열
    StringBuilder stringBuilder = new StringBuilder();

    BlocklyController controller;

    CodeDictionaryAdapter dictionaryAdapter;
    SimulatorAdapter simulatorAdapter;


    byte[] buffer = new byte[1024];  // buffer store for the stream

    int bytes; // bytes returned from read()
    // TODO : 블루투스 모드까지 ON
    //Physicaloid mPhysicaloid = new Physicaloid(this, true, "IOTMASS");
    // TODO : ONLY USB
    //Physicaloid mPhysicaloid = new Physicaloid(this);

    Boolean [] view_check = {true,true,true,true,true,true,true,true,true,true,true,false};


    // 배울래 블록 한글로 번역할 때 필요한 것
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
    String solutionXmlAssetFilePath ="";
    Application application;


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
            Application.mPhysicaloid.open();
            Log.e("Application physicaloid",Application.mPhysicaloid.isOpened()+"");

            byte[] buf = new byte[256];
            Application.mPhysicaloid.read(buf, buf.length);
//            monitor_text.append(new String(buf));
            Log.e("buf",new String(buf).contains("\n")+"");

//            monitor_text.append(num+"\n");
//            str += new String(buf);

            if (new String(buf).trim().length() != 0) {
                Log.e("num:",num+"");
                Log.e("length",stringBuilder.toString().length()+"");
                if (num < 60) {
                    stringBuilder.append(new String(buf).trim());
                } else if (num >= 60) {
                    stringBuilder.append(new String(buf).trim());
                    num = 50;
                    stringBuilder.delete(0,2560);
                    Log.e("length delete","OK");
                }

                if (Integer.parseInt(String.valueOf(stringBuilder.toString().length())) > 5120) {
                    stringBuilder.delete(0,2560);
                    Log.e("length delete MAX","OK");
                }

                Log.e("text",stringBuilder.toString());
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
                public void onFinishCodeGeneration(final String generatedCode, String xml) {
                    Log.e("start!","onFinishCodeGeneration");

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("code check codegeneration",generatedCode);
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

//                        customProgressDialog.dismiss();
//                        Log.e("generated",generatedCode);
                    }
                    else {
                        Log.e("start!","not generatedCode.contains");

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
                        submittedXml = xml;
                        Log.e("code",code);

                        create_file(generatedCode, "code.ino");
                        Log.e("compileCheck",compileCheck+"");
                        //Log.e("!@","nono");
                        if (compileCheck) {
                            //Log.e("generated", "컴파컴파");
                            remotecompile("code.ino", getCompiler());
                            Log.e("in!","ok");
                            compileCheck = false;
//                            customProgressDialog.dismiss();
                        }


//                        try {
//                            execute_shell("ls");
//                                                        execute_shell("touch Blink.cpp");
//                            execute_shell("cp hardware/arduino/cores/arduino/main.cpp Blink.cpp");
//
//                            execute_shell("sed -i wBlink1.cpp Blink.cpp files/Blink.ino");
//                            execute_shell("avr-g++");
//                        }catch (IOException e){
//                            e.printStackTrace();
//                        }



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

    public void upload_code(String file){
        Log.e("generated", "업로드드");
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        if(deviceList.isEmpty()){
            Log.e("uploadcode generated : ", "isempty 디바이스 리스트");
        }

        if (Application.mPhysicaloid.isOpened()) {
            OpenUSB();
            Log.e("in! upload","before");
            mHandler.sendEmptyMessageDelayed(1,3000);
            Application.mPhysicaloid.upload(Boards.ARDUINO_UNO, file);
            Log.e("in! upload","finish");
            mHandler.removeMessages(1);
            customProgressDialog.dismiss();
            uploadListener.show();

        } else {
            Boolean value = OpenUSB();
            if (value) {
                Application.mPhysicaloid.upload(Boards.ARDUINO_UNO, file);
                customProgressDialog.dismiss();
                uploadListener.show();
            }
            else {
                Toast.makeText(getApplicationContext(),"한번 더 업로드 버튼을 눌러주세요",Toast.LENGTH_SHORT).show();
            }
            getFileSize();
        }

    }

    public long getFileSize(){
        File directory = new File(Constants.upload_package_path+"out.hex");
        Log.e("file size",directory.length()+"");
        return directory.length();
    }


    public Boolean OpenUSB() {
        Log.e("usb initial",initial+"");
        if(initial) {
            Log.e("openUsb true","!!");
            initial = false;
            Application.mPhysicaloid.upload(Boards.ARDUINO_UNO, Constants.upload_package_path+ "code.ino");
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
                            Log.e("simpleMultiPartRequest ??","in");
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            customProgressDialog.dismiss();

//                            uploadListener.dismiss();
                        }
                        else {
                            //     mGeneratedErrorTextView.setVisibility(View.GONE);
                            //       mGeneratedErrorTextView.setText("");
//                            Log.e("2여기다 이놈아",response);
                            create_file(response, "out.hex");
                            Log.e("generated", "리모트 안");
                            upload_code(Constants.upload_package_path+"out.hex");
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
        Log.e("??","in execute");
        String s = null;
        String[] path = {"PATH=/sbin:/vendor/bin:/system/sbin:/system/bin:/system/xbin:/data/data/"+this.getPackageName()+"/local/bin"};
        Log.e("path check","/data/data/"+this.getPackageName()+"/");
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
        Log.e("?? ori str",ori_str);

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
                        create_file(obj.toString(), "app.json");
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
            File file = new File(TARGET_BASE_PATH+ "app.json");
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
        Log.e("path check","/data/data/"+this.getPackageName()+"/");
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


    private View.OnClickListener upload_confirm = new View.OnClickListener() {
        public void onClick(View v) {
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
        uploadListener.dismiss();
    };

    private View.OnClickListener error_confirm = new View.OnClickListener() {
        public void onClick(View v) {
            uploadListener.dismiss();
        }
    };

    private View.OnClickListener finish_confirm = new View.OnClickListener() {
        public void onClick(View v) {
            finishListener.dismiss();
            finish();

        }
    };

    private View.OnClickListener reset_confirm = new View.OnClickListener() {
        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(), "확인버튼",Toast.LENGTH_SHORT).show()
            resetListener.dismiss();
            getController().resetWorkspace();
            loadSetupBlock();
        }
    };

    private View.OnClickListener reset_cancel = v -> {
        // TODO : LMS 서버 통신
        resetListener.dismiss();
    };

    private View.OnClickListener finish_cancel = v -> {
        // TODO : LMS 서버 통신
        finishListener.dismiss();
    };




    //빌드봇 다이얼로그 관련 onclicklistener

    private View.OnClickListener cancel_listener = new View.OnClickListener() {
        public void onClick(View v) {
            buildbotDialog.dismiss();

        }
    };

    private View.OnClickListener ok_listener = new View.OnClickListener() {
        public void onClick(View v) {
            buildbotDialog.dismiss();
            //



            switch (check_num){
                case 1:
                    application = (Application) getApplication();

                    if (simulator_check) {
                        application.showLoadingScreen(MainActivity.this);
                    }

                    // 답안지 갱신
                    loadXmlFromWorkspace();

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        application.hideLoadingScreen();

                        TutorCheck tutorCheck = new TutorCheck(MainActivity.this, simulator_check, chapter_id, submittedXml);
                    }, 1500);
                    break;
                case 2:
                    boolean loadWorkspace = false;
                    String filename = "";
                    loadWorkspace = true;
                    filename = "android.xml";

                    String assetFilename = "turtle/demo_workspaces/" + filename;
                    try {
                        controller.loadWorkspaceContents(getAssets().open(assetFilename));
                    } catch (IOException | BlockLoadingException e) {
                        throw new IllegalStateException(
                                "Couldn't load demo workspace from assets: " + assetFilename, e);
                    }
                    addDefaultVariables(controller);
                    break;
                case 3:
                    nameInputDialog = new NameInputDialog(MainActivity.this,file_name_ok_listener);
                    nameInputDialog.show();
                    break;
                case 4:
                    mBlocklyActivityHelper.loadWorkspaceFromAppDirSafely("안녕.xml");
                    break;
            }
            Toast.makeText(getApplicationContext(), check_num+"", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener learning_goal_listener = new View.OnClickListener() {
        public void onClick(View v) {
            check_num = 1;
            buildbotDialog.exercise_btn.setChecked(false);
            buildbotDialog.code_save_btn.setChecked(false);
            buildbotDialog.code_load_btn.setChecked(false);
        }
    };

    private View.OnClickListener exercise_listener = new View.OnClickListener() {
        public void onClick(View v) {
          check_num = 2;
            buildbotDialog.learning_goal_btn.setChecked(false);
            buildbotDialog.code_save_btn.setChecked(false);
            buildbotDialog.code_load_btn.setChecked(false);
        }
    };

    private View.OnClickListener code_save_listener = new View.OnClickListener() {
        public void onClick(View v) {
           check_num = 3;
            buildbotDialog.learning_goal_btn.setChecked(false);
            buildbotDialog.exercise_btn.setChecked(false);
            buildbotDialog.code_load_btn.setChecked(false);
        }
    };

    private View.OnClickListener code_load_listener = new View.OnClickListener() {
        public void onClick(View v) {
            check_num = 4;
            buildbotDialog.learning_goal_btn.setChecked(false);
            buildbotDialog.exercise_btn.setChecked(false);
            buildbotDialog.code_save_btn.setChecked(false);
        }
    };


    // 파일 이름 저장 리스너

    private View.OnClickListener file_name_ok_listener = new View.OnClickListener() {
        public void onClick(View v) {
            if (nameInputDialog.name_input.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }else{
                file_list.add(nameInputDialog.name_input.getText().toString().trim());
                FileSharedPreferences.setStringArrayList(getApplicationContext(), "files",file_list);
                for (int i= 0; i< file_list.size(); i++){
                    Log.e("file", file_list.get(i));
                }
                mBlocklyActivityHelper.saveWorkspaceToAppDirSafely(nameInputDialog.name_input.getText().toString().trim()+".xml");
                nameInputDialog.dismiss();
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkConnection networkConnection = new NetworkConnection(getApplicationContext());
        networkConnection.observe(this, aBoolean -> {

            //Toast.makeText(getApplicationContext(), aBoolean+"", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), Build.VERSION.SDK_INT+"", Toast.LENGTH_SHORT).show();
            if (aBoolean != null) {
                if (aBoolean){
                    Application.wifi_check = true;
                    //Log.e("checkkk", "network avail2222");
                }else{
                    Application.wifi_check = false;
                    //Log.e("checkkk", "network unavail2222");
                }
            } else {
                Application.wifi_check = false;
            }

            Application.checkUploadBtn();
        });



//        basicFieldDropdownView.setOnBlockDropdownClickListener(new BlockDropdownClick() {
//            @Override
//            public void onBlockDropdownClick(int position) {
//                Log.e("test", "hihihi");
//            }
//        });

        //Log.e("test", getController().mPendingEvents+"");

        /*
        fieldDropdown = new FieldDropdown("test");
        fieldDropdown.setOnBlockDropdownClickListener(this);
        fieldDropdown.setOnBlockDropdownClickListener(new BlockDropdownClick() {
            @Override
            public void onBlockDropdownClick() {
                Log.e("test", "hihiho!!!");
            }
        });
         */

        /*basicFieldDropdownView = new BasicFieldDropdownView(this);

        //asicFieldDropdownView.mDropdownField.

        basicFieldDropdownView.mFieldObserver = new Field.Observer() {
            @Override
            public void onValueChanged(Field field, String oldValue, String newValue) {
                Log.e("test", "hihiho!!!");
            }
        };

        basicFieldDropdownView.setOnBlockDropdownClickListener(this);
        basicFieldDropdownView.setOnBlockDropdownClickListener(new BlockDropdownClick() {
            @Override
            public void onBlockDropdownClick(int position) {
                Log.e("test", "hihihi");
            }
        });

        basicFieldDropdownView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("testtest", "hihihi yes");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("testtest", "hihihi no");
            }
        });*/


        /*ConnectivityManager connectivityManager = getBaseContext().getSystemService(ConnectivityManager.class);
        Network currentNetwork = connectivityManager.getActiveNetwork();

        if (currentNetwork != null) {
            Log.e("checkkk", "network avail2");
        } else {
            Log.e("checkkk", "network unavail2");
        }*/
        db2 = AppDatabase2.getInstance(getBaseContext());

        blockDictionaryDao = db2.blockDictionaryDao();
        blockDictionaryList = blockDictionaryDao.findAll();
        Log.e("blockDictionaryList", "blockDictionaryList : " + blockDictionaryList.size()+"");

        contents_name = getIntent().getStringExtra("contents_name");
        chapter_id = getIntent().getStringExtra("id");

        chapter_id_split = chapter_id.split("-");

        Log.e("chapter_id",chapter_id);


        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);

        Log.e(TAG, ">>> block_width size.x : " + size.x + ", size.y : " + size.y);

        if ((chapter_id.split("-")[0].equals("3") || chapter_id.split("-")[0].equals("5")
                || chapter_id.split("-")[0].equals("7") || chapter_id.split("-")[0].equals("9")
                || chapter_id.split("-")[0].equals("11") || chapter_id.split("-")[0].equals("13")
                || chapter_id.split("-")[0].equals("15") || chapter_id.split("-")[0].equals("17")
                || chapter_id.split("-")[0].equals("19") || chapter_id.split("-")[0].equals("21")
                || chapter_id.split("-")[0].equals("23") || chapter_id.split("-")[0].equals("25")
                || chapter_id.split("-")[0].equals("27") || chapter_id.split("-")[0].equals("43"))){
            simulator_check = true;
            simulator_btn.setVisibility(View.VISIBLE);
            block_bot_btn.setVisibility(View.VISIBLE);
        }else{
            simulator_check = false;
            simulator_btn.setVisibility(View.GONE);
            block_bot_btn.setVisibility(View.GONE);
        }

        if (chapter_id.split("-")[0].equals("25") || chapter_id.split("-")[0].equals("27")) {
            simulator_btn.setSelected(true);
            simulator_btn.setEnabled(true);
        }



        Log.e("????",contents_name);
        Log.e("onCreate","in");
//        mPhysicaloid.open();
        OpenUSB();
        if (!contents_name.equals("none")){

            if (MySharedPreferences.getInt(getApplicationContext(),contents_name+" MAX") < 4) {
                MySharedPreferences.setInt(getApplicationContext(), contents_name+" MAX", 4);
            }
            MySharedPreferences.setInt(getApplicationContext(),contents_name,4);

        }else{
            server_upload_check = false;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        controller = getController();
        controller.recenterWorkspace();
        controller.zoomOut();
        controller.setCopyCheck(this);

        flyoutFragment = new FlyoutFragment();
        flyoutFragment.setCloseCheck(this);


        // 코드 사전 설명 보여지는 부분
        this.mCategoryView=mBlocklyActivityHelper.getmCategoryView();
        mCategoryView.mCategoryTabs.setEnableCheck(this);
//        Log.e("hi",mCategoryView.mRootCategory.getSubcategories().get(0).getCategoryName());\'

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.block_setup_btn:
                        clickEvent(0);
                        break;
                    case R.id.block_loop_btn:
                        clickEvent(1);
                        break;
                    case R.id.block_method_btn:
                        clickEvent(2);
                        break;
                    case R.id.block_etc_btn:
                        clickEvent(3);
                        break;
                }
            }
        };

        block_setup_btn = findViewById(R.id.block_setup_btn);
        block_loop_btn = findViewById(R.id.block_loop_btn);
        block_method_btn = findViewById(R.id.block_method_btn);
        block_etc_btn = findViewById(R.id.block_etc_btn);

        block_setup_btn.setOnClickListener(onClickListener);
        block_loop_btn.setOnClickListener(onClickListener);
        block_method_btn.setOnClickListener(onClickListener);
        block_etc_btn.setOnClickListener(onClickListener);

        clickEvent(0);

        block_list.setLayoutManager(new LinearLayoutManager(this));
        block_list.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }
        });


        simulator_component_list.add(new SimulatorComponent(0, "13"));
        component_list.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        simulatorAdapter = new SimulatorAdapter(simulator_component_list);
        component_list.setAdapter(simulatorAdapter);

//        (BlocklyCategory.BlockItem) blocks.get(0)).getBlock()
//        Log.e("blocks",blocks.size()+"");

        mCategoryView.setItemClick(this);
//        basicFieldDropdownView = new BasicFieldDropdownView(this,true);
//        basicFieldDropdownView.setOnBlockDropdownClickListener(this);
//
//        basicFieldDropdownView.setMainCheck(true);
//        Log.e("test in main",basicFieldDropdownView.getMainCheck()+"");
//        Log.e("test in main",basicFieldDropdownView.mListener+"");



        uploadListener = new UploadDialog(this, upload_confirm, submit_confirm, "업로드 성공!","확인을 눌러주세요");
        uploadListener.setCancelable(false);
        finishListener = new FinishDialog(this,"단원을 종료하시겠습니까?",finish_confirm,finish_cancel);
        resetListener = new FinishDialog(this,"정말 초기화하시겠습니까?",reset_confirm,reset_cancel);
        error_Listener = new UploadDialog(this, upload_confirm, null, "인터넷 연결 불안정","WIFI를 확인을 해주세요");

        // 시뮬레이터 초기화


        Display display2;  // in Activity
        display2 = getWindowManager().getDefaultDisplay();
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size2 = new Point();
        display2.getSize(size2); // or getSize(size)
        int width = size2.x;
        int height = size2.y;
        controller.setCenterXPosition(width);
        controller.setCenterYPosition(height);



        // TODO : 팝업 테스트
        // upload_Listener.show();
//        Log.e("turtle_block",TURTLE_BLOCK_DEFINITIONS.get(6));
    }



    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int userAction = event.getAction();
        Log.e("test", "test test : "+getController().mPendingEvents+"");

        // 블럭 드래그 체크
        if (getController().mDragger.mPendingDrag != null) {
            boolean test = getController().mDragger.mPendingDrag.isDragging();
            Log.e("test", "event test: " + userAction + "");

            loadXmlFromWorkspace();

            // userAction 여기서 찍어보기(밑에는 참고하기)
            *//*public static final int ACTION_DOWN             = 0;
            public static final int ACTION_UP               = 1;
            public static final int ACTION_MOVE             = 2;
            public static final int ACTION_CANCEL           = 3;*//*
            if (userAction != 2) {
                // 2만 뜨다가 3이 마지막에 뜸
                Log.e("test", "drag test: " + test + "");

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Log.e("test", code);
                }, 2000);
            }
        }

        // 블럭 드롭다운 & 입력 체크
        getController().setOnBlockClickListener(new BlocklyController.OnBlockClickListener() {
            @Override
            public void onBlockClick(PendingDrag pendingDrag) {
                Log.e("test", "block click test : ok");
            }
        });

        Log.e("test", "boolean check : " + getController().dropdownBooleanCheck);

        // test2
        //getController()


        //Log.e("test", "drag: " + userAction+"");
        //if (getController().getWorkspace().hasBlocks()) {
        //BlockFactory blockFactory = getController().getWorkspace().getBlockFactory();

        //Log.e(TAG, "blockFactory : " + blockFactory.getAllBlockDefinitions());
        //Log.e(TAG, "mTouchHandler : " + getController().mWorkspaceView.setDragger(getController().mDragger)+"");
        //getController().mWorkspaceView.setDragger(getController().mDragger.mPendingDrag.isDragging());
            *//*if (getController().mDragger.mPendingDrag != null) {
                boolean test = getController().mDragger.mPendingDrag.isDragging();
                Log.e("test", "drag test: " + test+"");
            }*//*
        //Log.e(TAG, "drag check : " + getController().mDragger.mPendingDrag.isDragging() + "");
        //}
        *//*switch (userAction) {

            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "화면 누름");
                break;

            case MotionEvent.ACTION_UP:
                Log.e(TAG, "화면에서 손 뗌");
                break;

            default:
                break;
        }*//*
        return super.dispatchTouchEvent(event);
    }*/

    public void clickEvent(int num) {
        Button [] button = {block_setup_btn, block_loop_btn, block_method_btn, block_etc_btn};

        for (int i = 0; i <button.length; i++) {
            if (num == i) {
                button[i].setSelected(true);
                button[i].setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                button[i].setSelected(false);
                button[i].setTextColor(Color.parseColor("#b45611"));
            }

        }


        dictionary_block_list.clear();
        List<BlocklyCategory.CategoryItem> blocks = mCategoryView.mRootCategory.getSubcategories().get(num).getItems();

        for (BlocklyCategory.CategoryItem item : blocks) {
            String title = "";
            String info = "";
            boolean check = false;

            if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BLOCK) {
                // Clean up the old views
                BlocklyCategory.BlockItem blockItem = (BlocklyCategory.BlockItem) item;
                Block block = blockItem.getBlock();


//                block.getController().getBlockViewFactory().

                /*if(block.getType() == Field.TYPE_VARIABLE) {

                }*/

                Log.e("test", "test : " + block.getNextBlock());

                Log.e("test", "block type : " + block.getType());



                if (block !=null) {
                    Log.e("block item","not null");
                }
                Log.e("block",blockItem.getBlock().toString());

                String [] splitBlock = blockItem.getBlock().toString().split("\"");
                Log.e("block", splitBlock[1]);

                // 엑셀 파일 내용과 어떻게 맞춰서 넣을지
                for (int i = 0; i < blockDictionaryList.size(); i++) {
                    if (blockDictionaryList.get(i).getBlock_type().equals(splitBlock[1])) {
                        title = blockDictionaryList.get(i).getBlock_name();
                        info = blockDictionaryList.get(i).getBlock_explanation();
                        check = true;
                    }
                }

                if (check) {
                    dictionary_block_list.add(new CodeBlock("0", title, info, R.drawable.problem_block2, block));
                }
                //dictionary_block_list.add(new CodeBlock("0","Setup","아두이노에서 무슨 PIN을 어떻게 사용할지 정하는 곳",R.drawable.problem_block2,block));
            }
        }
        dictionaryAdapter = new CodeDictionaryAdapter(getApplicationContext(), controller, dictionary_block_list);
        block_list.setAdapter(dictionaryAdapter);
    }


    public void setDictionaryData(int position){
        dictionary_block_list.clear();
        List<BlocklyCategory.CategoryItem> blocks = mCategoryView.mRootCategory.getSubcategories().get(position).getItems();
        for (BlocklyCategory.CategoryItem item : blocks) {
            if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BLOCK) {
                // Clean up the old views
                BlocklyCategory.BlockItem blockItem = (BlocklyCategory.BlockItem) item;
//                controller.getBlockViewFactory().getView()
                Block block = blockItem.getBlock();
                if (block !=null)
                    Log.e("block item","not null");
                Log.e("block",blockItem.getBlock().toString());
                dictionary_block_list.add(new CodeBlock("0","Setup","아두이노에서 무슨 PIN을 어떻게 사용할지 정하는 곳",R.drawable.problem_block2,block));
//                dictionary_block_list.add(new CodeBlock("1","digitalWrite","정해진 PIN 번호를 HIGH 또는 LOW로 설정하는 블록",R.drawable.problem_block4));
//                dictionary_block_list.add(new CodeBlock("2","Setup","아두이노에서 무슨 PIN을 어떻게 사용할지 정하는 곳",R.drawable.problem_block5));
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 봇 메시지 재생 종료
        mediaPlayer.release();
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
            case 11:
                mMonitorHandler.sendEmptyMessage(1);
                initTabColor();
                initTabCheck();
                if (simulator_check) {
                    simulator_btn.setVisibility(View.VISIBLE);
                    block_bot_btn.setVisibility(View.VISIBLE);
                }
                trashcan_btn.setVisibility(View.VISIBLE);
                translate_btn.setVisibility(View.VISIBLE);

                blockly_monitor.setVisibility(View.GONE);
                view_check[current_pos] = true;
                break;
            default:
                Log.e("code_btn in","어택땅");
                if(mPushEvent.getPos() < 4) {
                    initTabColor();
                    initTabCheck();
                    break;
                } else if (mPushEvent.getPos() >= 4 ){
                    initTabColor();
                    break;
                }
        }

    }

    @Override
    protected View onCreateContentView(int parentId) {
        getLayoutInflater();
        View root = getLayoutInflater().inflate(R.layout.split_content, null);
        mGeneratedFrameLayout = root.findViewById(R.id.generated_workspace);
        Log.e("oncreate","contentview");

        file_list = new ArrayList<>();

        file_list.addAll(FileSharedPreferences.getStringArrayList(getApplicationContext(),"files"));

        for (int i= 0; i< file_list.size(); i++){
            Log.e("file", file_list.get(i));
        }

//        mBlocklyActivityHelper.getmCategoryView().getCurrentCategory().getCategoryName();

        View blockly_workspace = root.findViewById(R.id.blockly_workspace);

//        block_copy_btn = blockly_workspace.findViewById(R.id.block_copy_btn);

        block_bot_btn = blockly_workspace.findViewById(R.id.block_bot_btn);
        simulator_btn = blockly_workspace.findViewById(R.id.go_simulator_btn);
        move_btn = blockly_workspace.findViewById(R.id.move_btn);
        code_view = blockly_workspace.findViewById(R.id.code_view);




        code_view.setMovementMethod(new ScrollingMovementMethod());

        Log.e("chapter_id",chapter_id);
        Log.e("chapter id 3",chapter_id.split("-")[0].equals("3")+"");
        Log.e("chapter id 5",chapter_id.split("-")[0].equals("5")+"");



        simulator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getController().getWorkspace().hasBlocks()) {
                    mBlocklyActivityHelper.requestCodeGeneration(
                            getBlockGeneratorLanguage(),
                            getBlockDefinitionsJsonPaths(),
                            getGeneratorsJsPaths(),
                            getCodeGenerationCallback());
                }

                Log.e("code",code);
                Log.e("chapter_id",chapter_id);

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    simulatorDialog = new SimulatorDialog(MainActivity.this,chapter_id,contents_name,code);
                    simulatorDialog.show();
                }, 500);

//                block_simulator.setVisibility(View.VISIBLE);
//                simulator_btn.setVisibility(View.GONE);
//                trashcan_btn.setVisibility(View.GONE);
//                block_bot_btn.setVisibility(View.GONE);
//
//                simulator_web_view.getSettings().setJavaScriptEnabled(true);
//                simulator_web_view.loadUrl("http://192.168.0.5:3000/");
            }
        });

        move_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //simulatorDialog = new SimulatorDialog(MainActivity.this,code);
                //simulatorDialog.show();
            }
        });

        component_close_btn = blockly_workspace.findViewById(R.id.component_close_btn);
        component_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block_simulator.setVisibility(View.GONE);
                if (simulator_check) {
                    simulator_btn.setVisibility(View.VISIBLE);
                    block_bot_btn.setVisibility(View.VISIBLE);
                }
                trashcan_btn.setVisibility(View.VISIBLE);
                translate_btn.setVisibility(View.VISIBLE);

            }
        });

        // 봇 메시지 초기화
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_true_answer);

        blockly_monitor = blockly_workspace.findViewById(R.id.blockly_monitor);
        input_space = blockly_workspace.findViewById(R.id.input_space);
        monitor_text = blockly_workspace.findViewById(R.id.monitor_text);
        translate_btn = blockly_workspace.findViewById(R.id.translate_btn);
        ai_test_btn = blockly_workspace.findViewById(R.id.ai_test_btn);

        code_load_btn = blockly_workspace.findViewById(R.id.code_load_btn);
        code_save_btn = blockly_workspace.findViewById(R.id.code_save_btn);

        monitor_text.setMovementMethod(new ScrollingMovementMethod());

        serial_input_box = blockly_workspace.findViewById(R.id.serial_input_box);
        serial_send_btn = blockly_workspace.findViewById(R.id.serial_send_btn);

        scrollview = blockly_workspace.findViewById(R.id.scrollview);
        trashcan_btn = blockly_workspace.findViewById(R.id.blockly_overlay_buttons);


        guideline4 = blockly_workspace.findViewById(R.id.guideline4);

        baud_rate = blockly_workspace.findViewById(R.id.baud_rate);

        block_dictionary = blockly_workspace.findViewById(R.id.block_dictionary);

        block_simulator = blockly_workspace.findViewById(R.id.block_simulator);

        block_list = blockly_workspace.findViewById(R.id.block_list);

        component_list = blockly_workspace.findViewById(R.id.component_list);

        close_btn = blockly_workspace.findViewById(R.id.close_btn);

        simulator_web_view = blockly_workspace.findViewById(R.id.simulator_web_view);


//        block_setup_btn.setSelected(true);
//
        if (turtle_pos == 0){
            translate_btn.setBackgroundResource(R.drawable.translate_kor_btn);

        }else{
            translate_btn.setBackgroundResource(R.drawable.translate_eng_btn);
        }

        arrayList = new ArrayList<>();
        arrayList.add(9600);
        arrayList.add(19200);
        arrayList.add(38400);
        arrayList.add(115200);

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);


        close_btn.setOnClickListener(v->{
            block_dictionary.setVisibility(View.GONE);
            view_check[9] = true;
            tempTab[5].setSelected(false);
            if (simulator_check) {
                simulator_btn.setVisibility(View.VISIBLE);
                block_bot_btn.setVisibility(View.VISIBLE);
            }
            trashcan_btn.setVisibility(View.VISIBLE);
            translate_btn.setVisibility(View.VISIBLE);

        });



        baud_rate.setAdapter(arrayAdapter);
        baud_rate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Application.mPhysicaloid.setBaudrate(arrayList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        block_copy_btn.setOnClickListener(v->{
//            if (copy_check){
//                copy_check = false;
//                block_copy_btn.setBackgroundResource(R.drawable.block_copy_btn_off);
//            }else{
//                copy_check = true;
//                block_copy_btn.setBackgroundResource(R.drawable.block_copy_btn_on);
//            }
//            controller.setCopyEnabled(copy_check);
//        });

        // 테스트 메시지 재생
//        block_bot_btn.setOnClickListener(v -> {
//            if (mediaPlayer != null) {
//                mediaPlayer.release();
//            }
//            // 봇 메시지 초기화
////            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bot_test_sound);
//            mediaPlayer.start();
//            Toast.makeText(this, "디지털라이트의 블록의 핀은 십삼번핀에 연결해주세요", Toast.LENGTH_SHORT).show();
//        });

        block_bot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_num = 1;
                buildbotDialog = new BuildBotDialog(MainActivity.this,ok_listener,cancel_listener
                        ,learning_goal_listener, exercise_listener, code_save_listener, code_load_listener);
                buildbotDialog.show();

//                application = (Application) getApplication();
//
//                if (simulator_check) {
//                    application.showLoadingScreen(MainActivity.this);
//                }
//
//                // 답안지 갱신
//                loadXmlFromWorkspace();
//
//                Handler handler = new Handler();
//                handler.postDelayed(() -> {
//                    application.hideLoadingScreen();
//
//                    TutorCheck tutorCheck = new TutorCheck(MainActivity.this, simulator_check, chapter_id, submittedXml);
//                }, 1500);
            }
        });

        /*block_bot_btn.setOnClickListener(v -> {
            if (getController().getWorkspace().hasBlocks()) {
                mBlocklyActivityHelper.requestCodeGeneration(
                        getBlockGeneratorLanguage(),
                        getBlockDefinitionsJsonPaths(),
                        getGeneratorsJsPaths(),
                        getCodeGenerationCallback());
            }

            Log.e("code",code);
            Log.e("chapter_id",chapter_id);

            // 봇 메시지 초기화
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            // 답안지 갱신
            loadXmlFromWorkspace();

            String solutionXmlAssetFilePath ="";
            if(chapter_id.equals("3-2")){
                solutionXmlAssetFilePath = "lv3_2.xml";
            }else if (chapter_id.equals("3-3")){
                solutionXmlAssetFilePath = "lv3_3.xml";
            }else if (chapter_id.equals("3-4")){
                solutionXmlAssetFilePath = "lv3_4.xml";
            }else if (chapter_id.equals("5-2")) {
                solutionXmlAssetFilePath = "lv5_2.xml";
            }else if (chapter_id.equals("5-3")) {
                solutionXmlAssetFilePath = "lv5_3.xml";
            }else if (chapter_id.equals("5-4")) {
                solutionXmlAssetFilePath = "lv5_4.xml";
            }else{
                solutionXmlAssetFilePath = "lv3_2.xml";
            }

            //TODO: 소영님 파일변경은 solutionXmlAssetFilePath 값 변경만 하시면 됩니다.
            //solutionXmlAssetFilePath = "lv5_4.xml";

            Log.e("solutionXmlAssetFilePath",solutionXmlAssetFilePath);


            // TODO : block compare test
            ParentXml parentXml = new ParentXml(getApplicationContext(),
                    "turtle/demo_workspaces/"+solutionXmlAssetFilePath,
                                                submittedXml);

            Source solution_src = parentXml.getSolutionSource();
            Source submitted_src = parentXml.getSubmittedSource();

            String solution_str = parentXml.getSolutionString();
            String submitted_str = parentXml.getSubmittedString();

            Log.e("solution_str",solution_str);
            Log.e("submitted_str",submitted_str);

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


            //블록 이름 가져오기
//            Log.e("e check",e.getElementsByTagName("block").item(3).getAttributes().getNamedItem("type").getTextContent()+"");
//            Log.e("e check",e.getElementsByTagName("field").item(0).getTextContent()+"");

            if (e != null) {

                // 또는, " 답안지가 정답지와 일치 " 했을때 정답처리

                if (solution_str.equals(submitted_str)){
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
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("블록 코딩 튜터");
                    alertDialog.setMessage("틀렸습니다. 다시 한번 해보세요~!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> {
                                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
                                dialog.dismiss();
                            });
                    alertDialog.show();
                }

                // Setup 의 pinMode " 핀 번호가 13이고 핀 IO가 OUTPUT " 인지, 아닌지 검증
                *//*
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
                *//*


                Log.d("Build Bot pin number", e.getElementsByTagName("field").item(0).getTextContent());
                Log.d("Build Bot pin IO", e.getElementsByTagName("field").item(1).getTextContent());
                Log.d("Build Bot first line", parentXml.getPreprocessedString(submitted_setup_node.getTextContent()));
            }

<<<<<<< HEAD
        });
=======

        });*/


        // 테스트 메시지 재생 완료
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);


        InputMethodManager imm = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow ( serial_input_box.getWindowToken (), 0 );

        categoryData = CategoryData.getInstance();

        categoryData.setSimulator_btn(simulator_btn);
        Application.setSimulatorEnabled(false);

//        Application.setSimulatorEnabled(false);
//        categoryData.setClosed(false);
        BusProvider.getInstance().register(this);

//        setupView(blockly_workspace);


        //지울 거
//        code_btn =  blockly_workspace.findViewById(R.id.code_btn);
//        serial_btn = blockly_workspace.findViewById(R.id.serial_btn);
//        upload_btn =  blockly_workspace.findViewById(R.id.upload_btn);


        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.setContentView(R.layout.dialog_progress);

        customProgressDialog.setCancelable(false);

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


        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        serial_input_box.setOnClickListener(v -> {
            // 에디트 텍스트

        });

        serial_send_btn.setOnClickListener(v -> {
            serial_input = serial_input_box.getText().toString();
            serial_input_box.setText("");

//            monitor_text.append(serial_input + "\n");
            stringBuilder.append(serial_input + "\n");
            monitor_text.setText(stringBuilder);

            serial_write(serial_input);
            imm.hideSoftInputFromWindow ( serial_input_box.getWindowToken (), 0 );
            Toast.makeText(this, "전송", Toast.LENGTH_SHORT).show();
        });


        translate_btn.setOnClickListener(v ->{

            if (turtle_pos == 0) {
                turtle_pos = 1;
                TURTLE_BLOCK_DEFINITIONS.set(1,turtle_files_kor[0]);
                TURTLE_BLOCK_DEFINITIONS.set(2,turtle_files_kor[1]);
                TURTLE_BLOCK_DEFINITIONS.set(3,turtle_files_kor[2]);
                TURTLE_BLOCK_DEFINITIONS.set(5,turtle_files_kor[3]);
                TURTLE_BLOCK_DEFINITIONS.set(6,turtle_files_kor[4]);
//                translate_btn.setBackgroundResource(R.drawable.translate_kor_btn);
            }else {
                turtle_pos = 0;
                TURTLE_BLOCK_DEFINITIONS.set(1,turtle_files_eng[0]);
                TURTLE_BLOCK_DEFINITIONS.set(2,turtle_files_eng[1]);
                TURTLE_BLOCK_DEFINITIONS.set(3,turtle_files_eng[2]);
                TURTLE_BLOCK_DEFINITIONS.set(5,turtle_files_eng[3]);
                TURTLE_BLOCK_DEFINITIONS.set(6,turtle_files_eng[4]);
//                translate_btn.setBackgroundResource(R.drawable.translate__btn);
            }
//
//
//
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            // 플래그 설정
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent); //현재 액티비티 재실행 실시
            overridePendingTransition(0, 0);
            finish(); //현재 액티비티 종료 실시
        });

        // teachable machine test
        ai_test_btn.setOnClickListener(v -> {
//            mBlocklyActivityHelper.saveWorkspaceToAppDirSafely("test1.xml");
            mBlocklyActivityHelper.loadWorkspaceFromAppDirSafely("test1.xml");

            boolean loadWorkspace = false;
            String filename = "";
                loadWorkspace = true;
                filename = "android.xml";

//        else if (id == R.id.action_demo_lacey_curves) {
//            loadWorkspace = true;
//            filename = "lacey_curves.xml";
//        } else if (id == R.id.action_demo_paint_strokes) {
//            loadWorkspace = true;
//            filename = "paint_strokes.xml";
//        }

                String assetFilename = "turtle/demo_workspaces/" + filename;
                try {
                    controller.loadWorkspaceContents(getAssets().open(assetFilename));
                } catch (IOException | BlockLoadingException e) {
                    throw new IllegalStateException(
                            "Couldn't load demo workspace from assets: " + assetFilename, e);
                }
                addDefaultVariables(controller);
        });

        code_save_btn.setOnClickListener(v -> {
            mBlocklyActivityHelper.saveWorkspaceToAppDirSafely("test1.xml");
            mBlocklyActivityHelper.loadWorkspaceFromAppDirSafely("test1.xml");

        });

        code_load_btn.setOnClickListener(v -> {
            mBlocklyActivityHelper.loadWorkspaceFromAppDirSafely("test1.xml");

        });


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

    void loadXmlFromWorkspace() {
        if (getController().getWorkspace().hasBlocks()) {
            mBlocklyActivityHelper.requestCodeGeneration(
                    getBlockGeneratorLanguage(),
                    getBlockDefinitionsJsonPaths(),
                    getGeneratorsJsPaths(),
                    getCodeGenerationCallback());
        }
    }

    void dictionary_btn_selected(Button btn, boolean check){
        btn.setSelected(check);
        if (check)
            btn.setTextColor(getResources().getColor(R.color.white));
        else
            btn.setTextColor(Color.parseColor("#b45611"));
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

    private final Handler mMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO : process device message.
            Log.e("getHandleMessage","" + msg.what);
            switch (msg.what){
                case 0:
                    read_code();
                    Log.e("serial isopen : ",Application.mPhysicaloid.isOpened() + "");
                    scrollview.fullScroll(View.FOCUS_DOWN);
                    sendEmptyMessage(0);
                    break;

                case 1:
                    removeMessages(0);
                    str ="";
                    monitor_text.setText("");
                    break;
            }
        }
    } ;

    public void setLineForOtherCategoryTabs(int position) {
        //mMonitorHandler.sendEmptyMessage(1);
        Log.e("??","setLineForOtherCategoryTabs");
        blockly_monitor.setVisibility(View.GONE);
        block_dictionary.setVisibility(View.GONE);

        if (position <4) {
            setCloseWindow(position,"block");
        }
    }


    public void setCloseWindow(int position, String type){
        Log.e("current_pos",current_pos+"");
        Log.e("current_pos position",position+"");

        if (current_pos == position) {
            if (view_check[position]){

                if (type.equals("monitor")){
                    Log.e("monitor", "in!");
                    blockly_monitor.setVisibility(View.VISIBLE);
                    tempTab[position-4].setSelected(true);
                }else if(type.equals("dictionary")){
                    Log.e("dictionary", "in!");
                    block_dictionary.setVisibility(View.VISIBLE);
                    dictionaryAdapter.notifyDataSetChanged();
                    tempTab[position - 4].setSelected(true);
                    if (current_pos <4){
                        if(block_tempTab[current_pos] != null)
                            block_tempTab[current_pos].setSelected(false);
                    }
                }

                if (simulator_check) {
                    simulator_btn.setVisibility(View.INVISIBLE);
                    block_bot_btn.setVisibility(View.INVISIBLE);
                }
                trashcan_btn.setVisibility(View.INVISIBLE);
                translate_btn.setVisibility(View.INVISIBLE);

                view_check[position] = false;

            }else {
                if (type.equals("monitor")){
                    Log.e("monitor", "in!");
                    blockly_monitor.setVisibility(View.GONE);
                    tempTab[position-4].setSelected(false);
                    mMonitorHandler.sendEmptyMessage(1);
                }else if(type.equals("dictionary")){
                    Log.e("dictionary", "in!");
                    block_dictionary.setVisibility(View.GONE);
                    view_check[position] = true;
                    tempTab[position - 4].setSelected(false);
                }

                if (simulator_check) {
                    simulator_btn.setVisibility(View.VISIBLE);
                    block_bot_btn.setVisibility(View.VISIBLE);
                }
                trashcan_btn.setVisibility(View.VISIBLE);
                translate_btn.setVisibility(View.VISIBLE);

                view_check[position] = true;
            }

        }else{
            if (type.equals("monitor")){
                Log.e("monitor", "in!");
                blockly_monitor.setVisibility(View.VISIBLE);
                tempTab[position-4].setSelected(true);
            }else if(type.equals("dictionary")){
                Log.e("dictionary", "in!");
                block_dictionary.setVisibility(View.VISIBLE);
                dictionaryAdapter.notifyDataSetChanged();
                tempTab[position - 4].setSelected(true);
                if (current_pos <4){
                    if(block_tempTab[current_pos] != null)
                        block_tempTab[current_pos].setSelected(false);
                }
            }
            view_check[position] = false;
            trashcan_btn.setVisibility(View.INVISIBLE);
            translate_btn.setVisibility(View.INVISIBLE);

            if (simulator_check) {
                simulator_btn.setVisibility(View.INVISIBLE);
                block_bot_btn.setVisibility(View.INVISIBLE);
            }
        }



        current_pos = position;
        Log.e("current_pos",current_pos+"");
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

        id = getIntent().getStringExtra("id");
        Log.e("mainactivity id",id);
        AUTOSAVE_FILENAME =  "turtle_workspace_temp"+id+".xml";
        SAVE_FILENAME = "turtle_workspace"+id+".xml";
//        if (contents_name.equals("none")){
//            Log.e("none","in");
//            return AUTOSAVE_FILENAME;
//        }
//        Log.e("none","not in");
//        return AUTOSAVE_FILENAME2;
        return  AUTOSAVE_FILENAME;
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
        return "http://learnserver24-LB-1900786351.ap-northeast-2.elb.amazonaws.com:5000";
    }


    @Override
    public void onClickTest(View v, int pos) {
        Log.e("??","onClickTest");
        Log.e("main create",pos+"");
        Log.e("isEnabled onClickTest",v.isSelected()+"");
        if (pos >= 4) {
            Log.e("pos",pos+"");
            tempTab[pos-4] = v;
            mBlocklyActivityHelper.getFlyoutController(); // setup() ~ 기타 탭의 블록창이 닫힘
            Log.e("tempTabCheck length",tempTabCheck.length+"");
            Log.e("before case", tempTabCheck[pos-4] + "");
            //Log.e("in case", tempTabCheck[pos-4] + "");
        } else {
            block_tempTab[pos] = v;
            initTabColor();
            initTabCheck();
        }

        // TODO : 상단 버튼 대응
        switch (pos) {

            case 4: // 실제코드
                Log.e("4 in","come");
                code_btn(pos);
                break;
            case 5: // 시리얼 모니터
                Log.e("5 in","come");
                serial_btn(pos);
                break;
            case 6: // 업로드
                Log.e("6 in","come");
                upload_btn(pos);
                break;

            case 7: // 리셋
                current_pos = 7;
                Log.e("close check",mBlocklyActivityHelper.getFlyoutController()+"");
                resetListener.show();
                break;
            case 8: // 홈
                Log.e("close check",mBlocklyActivityHelper.getFlyoutController()+"");
                current_pos = 8;
                finishListener.show();
                break;
            case 9: // 코드사전
                Log.e("9 in","come");
                CategoryData.getInstance().getSetUp_btn().setSelected(false);
                CategoryData.getInstance().getLoop_btn().setSelected(false);
                CategoryData.getInstance().getMethod_btn().setSelected(false);
                blockly_monitor.setVisibility(View.GONE);
                current_pos = 9;
                setCloseWindow(pos,"dictionary");
                break;
            case 10: // 티처블머신
                Log.e("10 in","come");
                current_pos = 10;
                CategoryData.getInstance().getSetUp_btn().setSelected(false);
                CategoryData.getInstance().getLoop_btn().setSelected(false);
                CategoryData.getInstance().getMethod_btn().setSelected(false);


                Intent intent = new Intent(MainActivity.this, TeachableActivity.class);
                startActivity(intent);
//                current_pos = 9;



                break;
        }
    }

    public void initTabColor() {
        for (View v : tempTab) {
            if (v != null) {
                v.setSelected(false);
            }
        }
        Log.e("initTabColor - case", tempTab[0] + ", " + tempTab[1] + ", " + tempTab[2] + ", "+ tempTab[3] + ", "+ tempTab[4] + ", "+ tempTab[5] );
    }

    public void initTabCheck() {
        tempTabCheck = new Boolean[] {false, false, false,false,false,false,false};
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
        // 탭이 닫힐 때 실행됨 (setup() ~ 코드사전 까지)
        try {
            if (current_pos < 4) {
                Log.e("hi","zz");
                view_check[current_pos] = true;
                trashcan_btn.setVisibility(View.VISIBLE);
                translate_btn.setVisibility(View.VISIBLE);

                if (simulator_check) {
                    simulator_btn.setVisibility(View.VISIBLE);
                    block_bot_btn.setVisibility(View.VISIBLE);
                }
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
//        block_copy_btn.setBackgroundResource(R.drawable.block_copy_btn_off);
    }

    public static String uri2path(Context context, Uri contentUri) {
        Log.e("in","uripath");
        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
//        Uri uri = Uri.fromFile(new File(path));

        cursor.close();
        Log.e("out","uripath");
        return path;
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        OutputStream fos;
        Log.e("hey","in");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.e("여기가 안도네 ?????","?????????");
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));

            try {
                new Thread(()->{
                    Log.e("thread","in");
                    Log.e("uri",uri2path(this,imageUri));
                    Pair<String, Media> response_ci = Application.mediaRepository.uploadMedia(new File(uri2path(this,imageUri)));
                    MySharedPreferences.setString(this,"block_img"+chapter_id,response_ci.getSecond().getGuid().getRendered());

                    Pair<String, UploadReport> response =Application.postsRepository.createUploadReport(
                            chapter_id+". "+contents_name,
                            MySharedPreferences.getString(this,"circuit_img"+chapter_id),
                            response_ci.getSecond().getGuid().getRendered()
                    );
                    Log.e("response",response.getFirst());
                    Log.e("response",response.getSecond().toString());

                    Pair<String, UploadReportJson> upload_result = Application.acfRepository.updateUploadReportAcf(
                            response.getSecond().getId(),
                            Integer.parseInt(chapter_id_split[0]),
                            Integer.parseInt(chapter_id_split[1]),
                            MySharedPreferences.getString(this,"circuit_img"+chapter_id),
                            response_ci.getSecond().getGuid().getRendered()
                    );

                    Log.e("upload_result",upload_result.getFirst());
                    Log.e("upload_result",upload_result.getSecond().toString());
                }).start();
            }catch (Exception e){
                Log.e("main error",e.toString());
                e.printStackTrace();
            }


        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, name + ".jpg");
            fos = new FileOutputStream(image);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).close();
    }

    public void code_btn(int pos) {
        mMonitorHandler.sendEmptyMessage(1);
        monitor_text.setText("");
        Log.e("hi code_btn","click");

        block_dictionary.setVisibility(View.GONE);
        input_space.setVisibility(View.GONE);

        setCloseWindow(pos,"monitor");


        if (getController().getWorkspace().hasBlocks()) {
            mBlocklyActivityHelper.requestCodeGeneration(
                    getBlockGeneratorLanguage(),
                    getBlockDefinitionsJsonPaths(),
                    getGeneratorsJsPaths(),
                    getCodeGenerationCallback());
        }

        Log.e("code_btn",code);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            String size1 = "file size : "+getFileSize()+"bytes\n\n\n";
//                    monitor_text.setText(size+mFormat.format(first_time)+"\n"+mFormat.format(last_time)+"\n"+code);
            monitor_text.setText(code);
            Log.e("code",code);
        }, 500);


        Log.e("code_btn", "status boolean : " + tempTabCheck[pos-4]);
    }

    public void serial_btn(int pos) {
        monitor_text.setText("");
        Application.mPhysicaloid.open();
        Log.e("isOpened Serial","" + Application.mPhysicaloid.isOpened());
        block_dictionary.setVisibility(View.GONE);
        setCloseWindow(pos,"monitor");
        input_space.setVisibility(View.VISIBLE);
        Log.e("serial","btn");

        stringBuilder.setLength(0);
        num = 0;
        mMonitorHandler.sendEmptyMessage(0);
    }

    public void upload_btn(int pos) {
        hideSystemUI();
        Log.e("upload_btn","in");
        Log.e("upload_btn",Application.all_check+"");

        if (Application.all_check) {
            mMonitorHandler.sendEmptyMessage(1);
            customProgressDialog.show();

            blockly_monitor.setVisibility(View.GONE);
            mBlocklyActivityHelper.getFlyoutController();
            categoryData.setPosition(6);
            current_pos = 6;

            compileCheck = true;

            if (getController().getWorkspace().hasBlocks()) {
                Log.e("??", "들어옴");
                mBlocklyActivityHelper.requestCodeGeneration(
                        getBlockGeneratorLanguage(),
                        getBlockDefinitionsJsonPaths(),
                        getGeneratorsJsPaths(),
                        getCodeGenerationCallback());
            }

            Log.e("MainActivity chapter_id",chapter_id+"");
            // TODO : 화면 캡쳐 트리거
//            if(!chapter_id.equals("0")) {
//                Log.e("MainActivity", "captureWorkspace: start");
//                bitmapWorkspace = controller.captureWorkspace();
//
//                try {
//                    saveImage(bitmapWorkspace, "captureWorkspace");
//                    Log.e("MainActivity", "captureWorkspace: save ok");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

        } else {
            //Toast.makeText(getApplicationContext(), "WIFI 및 USB를 연결해주세요!", Toast.LENGTH_SHORT).show();
            UploadFalseDialog uploadFalseDialog = new UploadFalseDialog(this);
            uploadFalseDialog.show();
        }

        current_pos = 6;
    }

    @Override
    public void onCheckEnabled() {
        Log.e("onCheckEnabled","in!");
        if (Application.wifi_check&& Application.usb_check){
            Log.e("checkUploadBtn","true");
            categoryData.getUpload_btn().setBackgroundResource(R.drawable.upload_btn_on);
            categoryData.getUpload_btn().setEnabled(true);
        }else{
            Log.e("checkUploadBtn","false");
            categoryData.getUpload_btn().setBackgroundResource(R.drawable.upload_btn_false);
            //categoryData.getUpload_btn().setEnabled(false);
        }
    }

    private void showCustomDialog(int num) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);

        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.dialog_tutor, (ConstraintLayout)findViewById(R.id.tutor_dialog));

        builder.setView(view);

        if (num == 1) {
            block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_ok));
            ((TextView)view.findViewById(R.id.tutor_text)).setText("정답입니다. 참 잘했어요~!");
        } else if (num == 2) {
            ((TextView)view.findViewById(R.id.tutor_text)).setText("틀렸습니다. 다시 한번 해보세요~!");
        } else {
            ((TextView)view.findViewById(R.id.tutor_text)).setText("빈 블록입니다. 블록 코딩을 해주세요~!");
        }

        AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.bottom_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block_bot_btn.setImageDrawable(getResources().getDrawable(R.drawable.bot_test_2_normal));
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = size.x * 500/1280;
        params.height = size.y * 300/720;
        alertDialog.getWindow().setAttributes(params);*/

        ViewGroup.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = (int) (335*2.6);
        alertDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

        //alertDialog.getWindow().setLayout(335*3, 170*3);

        /*WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT < 30) {
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            Window window = alertDialog.getWindow();

            int x = (int) (size.x * 0.5f);
            int y = (int) (size.y * 0.5f);

            window.setLayout(x, y);
        } else {
            Rect rect = windowManager.getCurrentWindowMetrics().getBounds();

            Window window = alertDialog.getWindow();

            int x = (int) (rect.width() * 0.5f);
            int y = (int) (rect.height() * 0.5f);

            window.setLayout(x, y);
        }*/
    }

    @Override
    public void onBlockDropdownClick() {
        Log.e("test","inin");
    }

//    @Override
//    public void onDropdownClick() {
//        Log.e("test","in");
//    }
}
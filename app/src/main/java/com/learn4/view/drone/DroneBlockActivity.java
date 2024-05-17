package com.learn4.view.drone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.blockly.Setting;
import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.FlyoutFragment;
import com.google.blockly.android.TabItemClick;
import com.google.blockly.android.UploadBtnCheck;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.control.ConnectionManager;
import com.google.blockly.android.control.FlyoutController;
import com.google.blockly.android.ui.BlockGroup;
import com.google.blockly.android.ui.BlockTouchHandler;
import com.google.blockly.android.ui.BlockView;
import com.google.blockly.android.ui.BusProvider;
import com.google.blockly.android.ui.CategoryData;
import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.android.ui.Dragger;
import com.google.blockly.android.ui.FlyoutCallback;
import com.google.blockly.android.ui.PendingDrag;
import com.google.blockly.android.ui.PushEvent;
import com.google.blockly.android.ui.ViewPoint;
import com.google.blockly.android.ui.WorkspaceHelper;
import com.google.blockly.model.Block;
import com.google.blockly.model.BlocklyCategory;
import com.google.blockly.model.BlocklySerializerException;
import com.google.blockly.model.DefaultBlocks;
import com.google.blockly.model.VariableCustomCategory;
import com.google.blockly.model.WorkspacePoint;
import com.google.blockly.utils.BlockLoadingException;
import com.learn4.R;
import com.squareup.otto.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DroneBlockActivity extends BlocklySectionsActivity implements TabItemClick ,UploadBtnCheck {
    private static final String TAG = "DroneBlockActivity";
    MyHandler myHandler;

    public static final int ARM = 1;
    public static final int MSG = 2;
    public static final int BAT = 3;
    public static final int RECEIVED = 4;
    public static final int DELAY = 5;
    public static final int DEBUG = 254;

    public static final int UPLOAD_STOP = 100;

    int x125 = 1;


    public byte seek_r_val = (byte) 0;
    public byte seek_g_val = (byte) 0;
    public byte seek_b_val = (byte) 0;
    public byte rgb_id_val = 2;

    public static final String ip = "192.168.4.1";
    public static final int port = 5000;

    public boolean SW = true;
    public boolean STOP = false;
    public boolean SAFE_LANDING = false;

    public boolean DRONE_STOP = false;

    String battery_value = "0";

    public byte pitch = 0x7d;
    public byte roll = 0x7d;
    public byte yaw = 0x7d;
    public byte throttle = 0x7d;

    public byte checkSum = (byte) 0;

    public boolean arm = false;
    public boolean first_arm = true;
    public boolean disarm = false;
    public boolean cal = false;
    public boolean setting = false;
    public boolean trim_forward = false;
    public boolean trim_backward = false;
    public boolean trim_left = false;
    public boolean trim_right = false;
    public boolean rgb = false;
    public boolean optic = true;

    public boolean connected = false;
    public boolean rgbNcal = false; // 너무 빨라서 안되나? 아니면 cali 동안 rgb가 씹힘?
    public boolean debug = false;



    public boolean start_stop_check= false;


    UDPClient client;
    DatagramSocket socket;

    public int batCount = 0;
    public int debugCount = 0;

    CategoryData categoryData;

    String TARGET_BASE_PATH;

    LinearLayout trashcan_btn;
    private FrameLayout mGeneratedFrameLayout;
    RecyclerView block_list_view;
    BlocklyController controller;
    FlyoutFragment flyoutFragment;

    CategoryView mCategoryView;

    private static String SAVE_FILENAME = "drone_workspace.xml";

    private static String AUTOSAVE_FILENAME = "drone_workspace_temp.xml";

    String code="", xml = "";

    TextView battery;

    ExampleThread thread;

    WorkspaceHelper mHelper;

    private ConnectionManager mConnectionManager;
//    private FlyoutCallback mCallback;
    private BlocklyCategory mCurrentCategory;
    private BlockTouchHandler mTouchHandler;
    FlyoutController mFlyoutController;

    private final Handler mHandler = new Handler();

    private LayoutInflater mHelium;

    private final WorkspacePoint mTempWorkspacePoint = new WorkspacePoint();



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
    private static final int MAX_LEVELS = 1;
    private static final String[] LEVEL_TOOLBOX = new String[MAX_LEVELS];

    static {
        LEVEL_TOOLBOX[0] = "drone_basic.xml";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("hello in","oncreate");

        controller = getController();
        mHelper = controller.getWorkspaceHelper();
        mConnectionManager = controller.getWorkspace().getConnectionManager();
        mTouchHandler = controller.getDragger()
                .buildImmediateDragBlockTouchHandler(new DragHandler());

        mHelium = LayoutInflater.from(this);
        mCategoryView = mBlocklyActivityHelper.getmCategoryView();
//        mHelper = controller.getWorkspaceHelper();
//
        mCategoryView.mCategoryTabs.setEnableCheck(this);
        mCategoryView.setItemClick(this);

//        block_list_view.setAdapter(new Adapter());





//        controller = getController();
//        controller.recenterWorkspace();
//        controller.zoomOut();
//        controller.setCopyCheck(this);
//
//        flyoutFragment = new FlyoutFragment();
//


        //        block_list_view.setAdapter(new Adapter());


//        setContentView(R.layout.blockly_drone_workspace);
//        mGeneratedFrameLayout = findViewById(R.id.generated_workspace);



        categoryData = CategoryData.getInstance();


//        Application.setSimulatorEnabled(false);
//        categoryData.setClosed(false);
        BusProvider.getInstance().register(this);
//
//
////        List<BlocklyCategory.CategoryItem> blocks = mCategoryView.mRootCategory.getSubcategories().get(0).getItems();
//

//        mFlyoutController = new FlyoutController(controller);

//        Application.checkUploadBtn();

        mBlocklyActivityHelper.getFlyoutController();



    }
//
    @Override
    protected View onCreateContentView(int containerId) {
        Log.e("hello in","onCreateContentView");
        getLayoutInflater();
        View root = getLayoutInflater().inflate(R.layout.activity_drone_block, null);
        mGeneratedFrameLayout = root.findViewById(R.id.generated_workspace);




        myHandler = new MyHandler();

        Button cal_btn = root.findViewById(R.id.cal_btn);
        Button button = root.findViewById(R.id.load_btn);
        Button wifi_button = root.findViewById(R.id.wifi_btn);
        Button arm_btn = root.findViewById(R.id.arm_btn);
        Button disarm_btn = root.findViewById(R.id.disarm_btn);
//        Button go_drone_btn = root.findViewById(R.id.go_drone_btn);

//        go_drone_btn.setOnClickListener(v->{
//            Intent intent = new Intent(DroneBlockActivity.this, DroneActivity.class);
//            overridePendingTransition(0, 0);
//            // 플래그 설정
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent); //현재 액티비티 재실행 실시
//            overridePendingTransition(0, 0);
//            finish(); //현재 액티비티 종료 실시
//        });

        trashcan_btn = root.findViewById(R.id.blockly_overlay_buttons);

//        View blockly_toolbox_ui = root.findViewById(blockly_toolbox_ui);



        block_list_view = root.findViewById(R.id.block_list_view);

        block_list_view.setLayoutManager(new LinearLayoutManager(this));





        battery = root.findViewById(R.id.battery);

        cal_btn.setOnClickListener(v->{
            cal = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rgbNcal = true; // 됨
                }
            }, 500);
            Log.e("touch", "calibration");
            delay(4000);
        });




        button.setOnClickListener(v->{
//            loadPythonMainBlock();

            String functionName = "funcA";
//            code = "funcA();\nfuncB(\"hello\");";

//            thread = new ExampleThread(code, obj);
//            thread.start();
//            Log.e("thread","start");

        });

        wifi_button.setOnClickListener(v->{
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            Log.e("touch", "wifi");
        });

        arm_btn.setOnClickListener(v->{
            if (getController().getWorkspace().hasBlocks()) {
                mBlocklyActivityHelper.requestCodeGeneration(
                        getBlockGeneratorLanguage(),
                        getBlockDefinitionsJsonPaths(),
                        getGeneratorsJsPaths(),
                        getCodeGenerationCallback());
            }

            Handler handler = new Handler();
            handler.postDelayed(() -> {

                Log.e(TAG+" code", code);
            }, 500);

        });

        disarm_btn.setOnClickListener(v->{
            disarm = true;
        });






        return root;
    }

    public Class<?> hello(String type){
        Log.e("type check",type.getClass()+"");
        switch (type){
            case "Int":
                return int.class;
            case "Boolean":
                return boolean.class;
            case "String":
                return String.class;

        }
        return int.class;
    }

    public void setLineForOtherCategoryTabs(int position) {
        //mMonitorHandler.sendEmptyMessage(1);
        Log.e("??","setLineForOtherCategoryTabs");

    }

    @Subscribe
    public void FinishLoad(PushEvent mPushEvent) {
        // 이벤트가 발생한뒤 수행할 작업
        Log.e("??","finishLoad " + mPushEvent.getPos());
        setLineForOtherCategoryTabs(mPushEvent.getPos());




        switch (mPushEvent.getPos()) {
            // 어택땅
            case 7:

                trashcan_btn.setVisibility(View.VISIBLE);
                break;
            default:
                Log.e("code_btn in","어택땅");
                if(mPushEvent.getPos() < 1) {

                    break;
                } else if (mPushEvent.getPos() >= 1 ){
                    trashcan_btn.setVisibility(View.VISIBLE);
                    break;
                }
        }

    }

    protected void onAutosave() {
        try {
            mBlocklyActivityHelper.saveWorkspaceToAppDir(getWorkspaceAutosavePath());
        } catch (FileNotFoundException | BlocklySerializerException e) {
            Log.e(TAG, "Failed to autosaving workspace.", e);
        }
    }


    @Override
    protected void onStop() {
        Log.e("my", "onStop");

        SW = false;
        Setting.drone_upload_btn_check = false;
        onAutosave();

        //소켓 함부로 닫지 마라
//        if (!socket.isClosed()) {
//            socket.close();
//        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("my", "onDestroy");
        if (!socket.isClosed()) {
            Log.e("my", "!isClosed()");
            socket.close();
            Log.e("my", Boolean.toString(socket.isConnected()));
            Log.e("my", Boolean.toString(socket.isBound()));
            Log.e("my", Boolean.toString(socket.isClosed()));
        }

//        unregisterReceiver(rssiReceiver);
        super.onDestroy();
    }




    @Override
    protected void onResume() {
        super.onResume();
//        loadPythonMainBlock();
        onAutoload();
        battery_value="not received";
    }

    @Override
    protected void onStart() {
        Log.e(TAG,"onstart");
        SW = true;
        throttle = 0x7d;
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client = new UDPClient();
        client.start();

//        mBlocklyActivityHelper.loadWorkspaceFromAppDirSafely(SAVE_FILENAME);

        super.onStart();
    }

    void loadSetupBlock(){
        String str = "<xml xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "            <block type=\"pinMode\">\n" +
                "                <value name=\"PIN\">\n" +
                "                    <block type=\"base_pins_list\">\n" +
                "                        <field name=\"PIN\">8</field>\n" +
                "                    </block>\n" +
                "                </value>\n" +
                "                <value name=\"VALUE1\">\n" +
                "                    <block type=\"base_output_list\">\n" +
                "                        <field name=\"LOGIC\">OUTPUT</field>\n" +
                "                    </block>\n" +
                "                </value>\n" +
                "            </block>\n" +
                "</xml>";
        InputStream is = new ByteArrayInputStream(str.getBytes());

        try {
            mBlocklyActivityHelper.loadWorkspaceFromInputStream(is);
        } catch (BlockLoadingException e1) {
            Log.e("mainactivity error", "Failed to load default arduino workspace", e1);
        }
    }

    public void calibration(){
        Log.e("droneTest hello", "calibration");
        cal = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rgbNcal = true; // 됨
            }
        }, 500);

    }

    public void arm(){
        Log.e("droneTest hello ", "arm");
        arm = true;
    }

    public void thread_stop(){
        Log.e("droneTest hello ", "arm");
        throttle = (byte) 125;
        yaw = (byte) 125;
        roll = (byte) 125;
        pitch = (byte) 125;
        disarm();
    }



    public void disarm(){
        Log.e("droneTest hello ", "disarm");
        disarm = true;
    }

    public void delay(long sec){
        Log.e("droneTest hello ", "delay" + sec);
        int counter = 0;
        try
        {
            ExampleThread.sleep(sec);
//            while (counter <= sec){
//                delay(20);
//                throttle = (byte) 125;
//                counter = counter + 30;
//            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Log.e("thread","delay");
    }

    public void take_off(){
        Log.e("droneTest hello ", "take_off");
        int speed = 175;
        int counter = 0;

        throttle = (byte) 175;

        while (counter <= 1500){
            delay(20);
            throttle = (byte) speed;
            counter = counter + 30;
        }

        throttle = (byte) 125;
        delay(300);
    }

    public void land(){
        Log.e("droneTest hello ", "land");
        int counter = 0;
        throttle = (byte) 80;
        while (counter <=300){
            delay(20);
            throttle = (byte) 80;
            counter = counter +1;
        }
        throttle = 0x7D;
        delay(300);
    }

    public void go(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        pitch = (byte)170;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        pitch = (byte) 125;
        delay(200);
    }

    public void back(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        pitch = (byte)80;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        pitch = (byte) 125;
        delay(200);
    }

    public void left(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        roll = (byte)80;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        roll = (byte) 125;
        delay(200);
    }

    public void right(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        roll = (byte)170;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        roll = (byte) 125;
        delay(300);
    }

    public void up(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        throttle = (byte)170;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        roll = (byte) 125;
        delay(300);
    }

    public void down(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        throttle = (byte)80;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        roll = (byte) 125;
        delay(300);
    }

    public void ccw(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        yaw = (byte)80;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        yaw = (byte) 125;
        delay(300);
    }

    public void cw(long cmd_param){
        Log.e("droneTest hello ", "go");
        int counter = 0;

        yaw = (byte)170;

        while (counter <= cmd_param){
            delay(20);
            counter = counter + 1;
        }

        yaw = (byte) 125;
        delay(300);
    }

    public void loadfor(){

        String str =
                "<block type=\"turtle_drone_main\" id=\"8d32874e-b4b2-4f58-bf8f-d5fa928da0ff\" x=\"8.0\" y=\"128.0\">\n" +
                        "<statement name=\"DO\">\n"+
                "<block type=\"drone_for\">\n" +
                "                                <value name=\"FROM\">\n" +
                "                                    <block type=\"math_number\">\n" +
                "                                        <field name=\"NUM\">1</field>\n" +
                "                                    </block>\n" +
                "                                </value>\n" +
                "                                <statement name=\"DO\">\n" +
                "  <block type=\"arm_start\" x=\"-20.0\" y=\"108.0\">\n" +
                "<next>\n"+
                "  <block type=\"drone_delay\">\n" +
                "         <field name=\"NUM\">5</field>\n" +
                "<next>\n"+
                "  <block type=\"take_off\">\n" +
                "<next>\n"+
                "  <block type=\"drone_delay\">\n" +
                "         <field name=\"NUM\">5</field>\n" +
                "<next>\n"+
                "  <block type=\"land\" />\n" +
                "</next>\n"+
                "  </block>\n" +
                "</next>\n"+
                "  </block>\n" +
                "</next>\n"+
                "  </block>\n" +
                "</next>\n"+
                "  </block>\n" +
                "                                </statement>\n" +
                "                            </block>\n"+
        "        </statement>\n" +
                "    </block>\n";

        InputStream is = new ByteArrayInputStream((str).getBytes());

        try {
            mBlocklyActivityHelper.loadWorkspaceFromInputStream(is);
        } catch (BlockLoadingException e1) {
            Log.e("mainactivity error", "Failed to load default arduino workspace", e1);
        }
    }

    void loadPythonMainBlock(){
//        "  <block type=\"drone_delay\" >\n" +
//                "                                <field name=\"NUM\">5</field>\n" +
//                "</block>\n" +
        String str = "<xml xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <block type=\"arm_start\" x=\"-20.0\" y=\"108.0\"/>\n" +
                "  <block type=\"drone_delay\">\n" +
                "         <field name=\"NUM\">5</field>\n" +
                "  </block>\n" +
                "  <block type=\"take_off\" />\n" +
                "  <block type=\"drone_delay\">\n" +
                "         <field name=\"NUM\">5</field>\n" +
                "  </block>\n" +
                "  <block type=\"land\" />\n" +
                "</xml>";
        InputStream is = new ByteArrayInputStream(str.getBytes());

        try {
            mBlocklyActivityHelper.loadWorkspaceFromInputStream(is);
        } catch (BlockLoadingException e1) {
            Log.e("mainactivity error", "Failed to load default arduino workspace", e1);
        }
    }


    private final CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback =
            new CodeGenerationRequest.CodeGeneratorCallback() {
                @Override
                public void onFinishCodeGeneration(final String generatedCode, String xml) {
                    Log.e("start!","onFinishCodeGeneration");
                    code = generatedCode.replace("void setup() {\n\n}", "");
                    xml = xml;





                }
            };

    @NonNull
    @Override
    protected ListAdapter onCreateSectionsListAdapter() {
        String[] levelNames = new String[MAX_LEVELS];
        Log.e("path check","/data/data/"+this.getPackageName()+"/");
        TARGET_BASE_PATH = "/data/data/"+this.getPackageName()+"/";
        levelNames[0] = "DroneBasic";


        return new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                levelNames);
    }

    @Override
    protected boolean onSectionChanged(int oldSection, int newSection) {
        return false;
    }

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        return "turtle/" + LEVEL_TOOLBOX[getCurrentSectionIndex()];
    }

    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        return TURTLE_BLOCK_DEFINITIONS;
    }

    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        return TURTLE_BLOCK_GENERATORS;
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        return mCodeGeneratorCallback;
    }

    @Override
    protected void onInitBlankWorkspace() {
        addDefaultVariables(getController());
    }

    static void addDefaultVariables(BlocklyController controller) {
        // TODO: (#22) Remove this override when variables are supported properly
        controller.addVariable("item");
        Log.e("addDefaultVariables","in");
    }




    @Override
    public void onLoadWorkspace() {
        Log.e("in!","onLoadWorkspace");
        mBlocklyActivityHelper.loadWorkspaceFromAppDirSafely(SAVE_FILENAME);
    }

    @Override
    public void onSaveWorkspace() {
        Log.e("in","onSaveWorkspace");
        mBlocklyActivityHelper.saveWorkspaceToAppDirSafely(SAVE_FILENAME);
    }


    protected boolean onAutoload() {
        String filePath = getWorkspaceAutosavePath();
        try {
            mBlocklyActivityHelper.loadWorkspaceFromAppDir(filePath);
            return true;
        } catch (FileNotFoundException e) {
            loadfor();
        } catch (BlockLoadingException | IOException e) {
            Log.e(TAG, "Failed to load workspace", e);
            mBlocklyActivityHelper.getController().resetWorkspace();

            File file = getFileStreamPath(filePath);
            if (!file.delete()) {
                Log.e(TAG, "Failed to delete corrupted autoload workspace: " + filePath);
            }
        }
        return false;
    }

    @Override
    @NonNull
    protected String getWorkspaceAutosavePath() {
        Log.e("in!","getWorkspaceAutosavePath");

        return  AUTOSAVE_FILENAME;
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

    @Override
    public void onCheckEnabled() {

    }

    @Override
    public void onClickTest(View v, int pos) {
//        mBlocklyActivityHelper.getFlyoutController();
        if (pos != 1 ){
            mBlocklyActivityHelper.getFlyoutController();
            trashcan_btn.setVisibility(View.VISIBLE);
        }
        switch (pos){
            case 0:
                finish();
                break;
            case 1:
                Log.e("onclicktest", "코딩블록");
                if (v.isSelected()){
                    Log.e("onclicktest 코딩블록", "selected");
                    trashcan_btn.setVisibility(View.INVISIBLE);
                }else{
                    Log.e("onclicktest 코딩블록", "not selected");
                    trashcan_btn.setVisibility(View.VISIBLE);
                }

                break;

            case 2:
                if (getController().getWorkspace().hasBlocks()) {
                    mBlocklyActivityHelper.requestCodeGeneration(
                            getBlockGeneratorLanguage(),
                            getBlockDefinitionsJsonPaths(),
                            getGeneratorsJsPaths(),
                            getCodeGenerationCallback());
                }
                cal = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rgbNcal = true; // 됨
//                        categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn_on);
                        Log.e("check code", code);
                    }
                }, 500);
                delay(100);
                break;


            case 3:

                if (connected) {
                    Log.e("drone upload btn ", "in");
                    try {
                        if (thread != null) {
                            if (thread.isAlive()) {
                                Log.e("onclicktest 업로드", "1");
                                DRONE_STOP = true;
                                try {
                                    thread.setRunningState(false);
                                    thread_stop();
                                    categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn_on);
                                    categoryData.getDrone_start_btn().setBackgroundResource(R.drawable.drone_start_off);
                                    categoryData.getDrone_upload_btn().setEnabled(false);
                                    ExampleThread.sleep(1000);
                                    thread.interrupt();
                                    ExampleThread.currentThread().interrupt();



                                    myHandler.sendEmptyMessageDelayed(UPLOAD_STOP, 1000);

                                }catch (InterruptedException e){
                                    categoryData.getDrone_start_btn().setBackgroundResource(R.drawable.drone_start_off);
                                    categoryData.getDrone_upload_btn().setEnabled(true);
                                    ExampleThread.currentThread().interrupt();

//                                    throw new RuntimeException(e);
                                }


                                break;
                            }
                        }
                    } catch (NullPointerException e) {
                        Log.e("thread null", "not initialize");
                    }


                    if (getController().getWorkspace().hasBlocks()) {
                        mBlocklyActivityHelper.requestCodeGeneration(
                                getBlockGeneratorLanguage(),
                                getBlockDefinitionsJsonPaths(),
                                getGeneratorsJsPaths(),
                                getCodeGenerationCallback());
                    }

                    Object obj = this;

                    categoryData.getDrone_start_btn().setBackgroundResource(R.drawable.drone_start_on);
                    thread = new ExampleThread(code, obj);
                    thread.setRunningState(true);
                    thread.start();

                    Log.e("onclicktest", "업로드");
                    break;
                }
                break;

            case 4:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                Log.e("touch", "wifi");
           break;
            case 5:
                Toast.makeText(getApplicationContext(), battery_value, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    static class droneTest{
        DroneBlockActivity droneBlockActivity;
        public void funcA(){
            Log.e("droneTest hello ", "funA");
        }

        public void funcB(){
            Log.e("droneTest bye ", "funB");

        }

        public void delay(){
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Log.e("droneTest hello ", "sleep");
        }

    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ARM:
                    Log.e("myhandler in","arm");
//                    Log.e(TAG+"ARM", (String)msg.obj);
//                    armPosition();
                    break;
                case MSG:
                    Log.e("myhandler in","msg");
//                    Log.e(TAG + "MSG", (String) msg.obj);
//                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case BAT:
                    Log.e("myhandler in","bat");
//                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, (String) msg.obj);
//                    if (msg.arg1 == 2) drone_menu_tabs.getTabAt(4).setText()
//                    else txt_bat.setTextColor(Color.WHITE);
//                    txt_bat.setText((String) msg.obj);

//                    battery.setText((String) msg.obj);

                    String bat_msg = (String) msg.obj;
                    Log.e("bat_msg", bat_msg);
                    if (!battery_value.equals(bat_msg) && !bat_msg.equals("not received")){
                        Log.e("battery text",(String) msg.obj);
                        battery_value = bat_msg;
                        battery_image_setting(battery_value);
                        categoryData.getPercentage().setText(battery_value+"%");
                    }

                    break;
                case DEBUG:
                    Log.e("myhandler in","debug");
                    int[] a = (int[]) msg.obj;
                    int[] b = new int[4];
                    for (int i = 0; i < 4; i++) {
                        int c = 0;
                        if(a[2 * i] < 0) c = 256;
                        b[i] = a[2 * i] + c + (a[2 * i + 1] << 8);
//                        Log.e("a0", Integer.toString(a[2*i]));
//                        Log.e("a1", Integer.toString((a[2 * i + 1] << 8)));
                    }


                    break;
                case RECEIVED:
//                    if (msg.arg1 == 2) txt_bat.setTextColor(Color.RED);
//                    else txt_bat.setTextColor(Color.WHITE);
//                    txt_bat.setText((String) msg.obj);
                    Log.e("myhandler in","receive");
//                    IntentFilter rssiFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//                    registerReceiver(rssiReceiver, rssiFilter);
//                    WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                    wifiMan.startScan();
                    battery.setText("Battery:"+(String) msg.obj);



                    break;


                case DELAY:
                    throttle = 125;
                    break;

                case UPLOAD_STOP:
                    categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn_on);
                    start_stop_check = true;
//                    DRONE_STOP = false;
                    categoryData.getDrone_upload_btn().setEnabled(true);
                    break;
            }
        }
    }

    void battery_image_setting(String per){
        int grade = Integer.parseInt(per);
        Log.e("grade check",grade+"");

        if (grade < 16){
            categoryData.getDrone_battery_btn().setBackgroundResource(R.drawable.battery_1);
        }else  if (grade >= 16 &&grade < 30){
            categoryData.getDrone_battery_btn().setBackgroundResource(R.drawable.battery_2);
        }else  if (grade >= 30 &&grade < 45){
            categoryData.getDrone_battery_btn().setBackgroundResource(R.drawable.battery_3);
        }else  if (grade >= 45 &&grade < 65){
            categoryData.getDrone_battery_btn().setBackgroundResource(R.drawable.battery_4);
        }else  if (grade >= 65 &&grade < 85){
            categoryData.getDrone_battery_btn().setBackgroundResource(R.drawable.battery_5);
        }else  if (grade >= 85 ){
            categoryData.getDrone_battery_btn().setBackgroundResource(R.drawable.battery_6);
        }

    }

    private BroadcastReceiver rssiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiMan.startScan();
            int linkspeed = wifiMan.getConnectionInfo().getLinkSpeed();
            int newRssi = wifiMan.getConnectionInfo().getRssi();
            int level = wifiMan.calculateSignalLevel(newRssi, 10);
            int percentage = (int) ((level/10.0)*100);
            String macAdd = wifiMan.getConnectionInfo().getBSSID();
            Log.e("linkspeed",linkspeed+"");
//            drone_menu_tabs.getTabAt(5).setText(percentage+"");
            //debugtext.setText("링크 스피드 : " + linkspeed + " / 신호 감도 : " + percentage + " / 맥어드레스 : " + macAdd );
        }
    };


    public class UDPClient extends Thread {

        InetAddress serverAddr;

        public UDPClient() {
            try {
                // Retrieve the servername
                serverAddr = InetAddress.getByName(ip);
                Log.e("UDP", "C: Connecting...");

                GetUDPMSG getUDPMSG = new GetUDPMSG(socket);
                getUDPMSG.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
//            categoryData.getDrone_upload_btn().setBackgroundResource();

            try {
                Log.e("UDPClient", "run");
                while (true) {
                    while (STOP) {
//                        Log.e("my", "STOP");
                        if (!SW) {
                            break;
                        }
                    }
                    if (!SW) {
                        break;
                    }
                    /* Prepare some data to be sent. */
//                    byte[] buf = ("Hello from Client").getBytes();

                    /*                    ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
                    buff.order(ByteOrder.LITTLE_ENDIAN);
                    buff.putInt(150);
                    temp[4] = buff.get();*/


//                    byte[] buf = ("$M<" + "5" + "150" + Integer.toString(pitch) + Integer.toString(roll) + Integer.toString(yaw) + Integer.toString(throttle) + "5" + Integer.toString(checkSum)).getBytes();
//                    byte[] buf = ("$M<" + "150" + Integer.toString(pitch) + Integer.toString(roll) + Integer.toString(yaw) + Integer.toString(throttle) + "5" + Integer.toString(checkSum)).getBytes();


                    if (arm) {
                        Log.e("handler arm","in");
                        Message msg = myHandler.obtainMessage();
                        msg.what = ARM;
                        msg.arg1 = 4;
                        msg.obj = "hi";
                        myHandler.sendMessage(msg);

                        sendFunction(socket, serverAddr, (byte) 0x97);
//                        sendSignal(socket, serverAddr, (byte)1500, (byte)1500, (byte)2000, (byte)1000); // fail for 2.4
                    } else if (disarm) {
                        Log.e("handler disarm","in");
                        sendFunction(socket, serverAddr, (byte) 0x98);
                    } else if (cal) {
                        sendFunction(socket, serverAddr, (byte) 0xcd);
                    } else if (trim_forward) {
                        sendFunction(socket, serverAddr, (byte) 0x99);
                    } else if (trim_backward) {
                        sendFunction(socket, serverAddr, (byte) 0x9A);
                    } else if (trim_left) {
                        sendFunction(socket, serverAddr, (byte) 0x9B);
                    } else if (trim_right) {
                        sendFunction(socket, serverAddr, (byte) 0x9C);
                    } else if (rgb) {
                        Log.e("send","rgb in");
                        sendRGB(socket, serverAddr, (byte) 0x8D);
                    } else if (!STOP) {
//                        Log.e("send",roll+", "+pitch+", "+yaw+", "+throttle);
//                        Log.e("send value", roll+","+pitch+","+yaw+","+throttle);
                        sendSignal(socket, serverAddr, roll, pitch, yaw, throttle);
//                        Log.e(TAG+"sendSignal", Integer.toString(cont_version));
                    }

                    // bat
                    if (!connected) {
//                        Log.e("hello",categoryData.getUpload_btn().getBackground()+"");
                        Message msg = myHandler.obtainMessage();
                        msg.what = BAT;
                        msg.arg1 = 2;
                        msg.obj = "not received";
                        myHandler.sendMessage(msg);
//                        if (categoryData.getDrone_upload_btn().isEnabled()){
//                            categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn);
//                            categoryData.getDrone_upload_btn().setEnabled(false);
//                        }
//
                    }
                    batCount++;
                    if (batCount % 20 == 0) {
                        if (batCount > 100) {
                            if (connected){
                                Log.e("hello in??","connected in");
                                categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn);
                                categoryData.getDrone_upload_btn().setEnabled(false);
                                categoryData.getDrone_battery_btn().setBackgroundResource(R.drawable.drone_battery_btn_on);
                                categoryData.getPercentage().setText("");

                            }
                            batCount = 0;
                            connected = false;
                        }
                        sendFunction(socket, serverAddr, (byte) 0xA3); // bat
//                        sendFunction(socket, serverAddr, (byte) 0xfe);// 0xA3); // debug0
//                        Log.e(TAG, "send bat");
                    }

                    debugCount++;
                    if (debugCount % 5 == 0) {
                        debugCount = 0;
//                        sendFunction(socket, serverAddr, (byte) 0xfe); // debug
                    }

//                    else if (SAFE_LANDING) {
////                    } else if (SAFE_LANDING && (throttle & 0xff) > 150) {
//                        for (int i = 5; i > 0; i--) {
//                            for (int j = 0; j < 30; j++) {
//                                sendSignal(socket, serverAddr, (byte) 0x7d, (byte) 0x7d, (byte) 0x7d, (byte) (i * 30));
////                                sendSignal(socket, serverAddr, (byte) 0x7d, (byte) 0x7d, (byte) 0x7d, (byte) (100));
//                                Thread.sleep(20);
////                                Log.e("my", Integer.toString(j));
//                            }
//                        }
//                        SAFE_LANDING = false;
//                    }

                    arm = false;
                    disarm = false;
                    cal = false;
                    trim_forward = false;
                    trim_backward = false;
                    trim_left = false;
                    trim_right = false;
                    rgb = false;
                    checkSum = 0;
                    Thread.sleep(20);
                    if (rgbNcal) {
                        rgbNcal = false;
                        rgb = true;
                    }

                    /*            *//* Create UDP-packet with
                     * data & destination(url+port) *//*
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, port);
                    Log.d("UDP", "C: Sending: '" + new String(buf) + "'");
                    Log.d("UDP", "C: Sending:temp '" + new String(temp) + "'");

            *//* Send out the packet *//*
                    socket.send(packet);
                    Log.d("UDP", "C: Sent.");
                    Log.d("UDP", "C: Done.");*/


//                socket.receive(packet);
//                Log.d("UDP", "C: Received: '" + new String(packet.getData()) + "'");


                }

            } catch (Exception e) {
                Log.e("UDP", "C: Error", e);
            }
        }
    }







    void sendFunction(DatagramSocket socket, InetAddress serverAddr, byte signal) {
        try {
            if ((trim_forward || trim_backward || trim_left || trim_right) && (x125 == 10))
                signal += (byte) 4;
            byte[] armTemp = new byte[6];
            armTemp[0] = (byte) '$';
            armTemp[1] = (byte) 'M';
            armTemp[2] = (byte) '<';
            armTemp[3] = (byte) 0;
            armTemp[4] = signal;
            armTemp[5] = signal;

            DatagramPacket packet = new DatagramPacket(armTemp, armTemp.length, serverAddr, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendSignal(DatagramSocket socket, InetAddress serverAddr, byte r, byte p, byte y, byte t) {
        try {
            byte[] temp = new byte[11];
            temp[0] = (byte) '$';
            temp[1] = (byte) 'M';
            temp[2] = (byte) '<';
            temp[3] = (byte) 0x05;
            checkSum ^= (byte) 0x05;
            temp[4] = (byte) 0x95;
            checkSum ^= (byte) 0x95;
            temp[5] = r;
            checkSum ^= r;
            temp[6] = p;
            checkSum ^= p;
            temp[7] = y;
            checkSum ^= y;
            temp[8] = t;
            checkSum ^= t;
            if (optic) {
                temp[9] = 0x00;
                checkSum ^= 0x00; //AUX
            } else {
                temp[9] = 0x01;
                checkSum ^= 0x01;
            }
            temp[10] = checkSum;
            DatagramPacket packet = new DatagramPacket(temp, temp.length, serverAddr, port);
            socket.send(packet);
            Log.e("send ok","finish");
        } catch (Exception e) {
           Log.e("send error",e.toString());
        }
    }



    void sendRGB(DatagramSocket socket, InetAddress serverAddr, byte sign) {
        try {
            byte[] temp = new byte[11];
            temp[0] = (byte) '$';
            temp[1] = (byte) 'M';
            temp[2] = (byte) '<';
            temp[3] = (byte) 0x05;
            checkSum ^= (byte) 0x05;
            temp[4] = sign;
            checkSum ^= sign;
            temp[5] = seek_r_val;
            checkSum ^= seek_r_val;
            temp[6] = seek_g_val;
            checkSum ^= seek_g_val;
            temp[7] = seek_b_val;
            checkSum ^= seek_b_val;
            temp[8] = rgb_id_val;
            checkSum ^= rgb_id_val;
            temp[9] = 0x00;
            checkSum ^= 0x00; //AUX
            temp[10] = checkSum;
            DatagramPacket packet = new DatagramPacket(temp, temp.length, serverAddr, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GetUDPMSG extends Thread {

        String text;

        byte[] message = new byte[20];
        DatagramPacket p = new DatagramPacket(message, message.length);

        DatagramSocket mDataGramSocket;

        public GetUDPMSG(DatagramSocket socket) {
            try {
                mDataGramSocket = socket;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (!SW) {
                        break;
                    }
                    try {
//                        Log.e(TAG + "bat", Integer.toString(p.getData()[4]));
                        mDataGramSocket.receive(p);
//                        text = new String(message, 0, p.getLength()); // x
                        Message msg = myHandler.obtainMessage();
                        if (p.getData()[4] == (byte) 0xA3) {
                            Log.e(TAG, "BAT GOT IT");
                            Log.e(TAG, connected+"");
                            batCount = 1;
                            if (categoryData.getDrone_upload_btn() != null && !connected && !battery_value.equals("not received")){
                                categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn_on);
                                Setting.drone_upload_btn_check = true;
                                categoryData.getDrone_upload_btn().setEnabled(true);
                                start_stop_check = true;
                            }
                            connected = true;
////                            Log.e(TAG+"getData", "hi");
                            int a = p.getData()[5];
////                            Log.e(TAG + "getData", Integer.toString(a));
                            msg.what = BAT;
                            msg.arg1 = 1;
                            msg.obj = Integer.toString(a);
                            myHandler.sendMessage(msg);

                        } else if (p.getData()[4] == (byte) 0xfe) {
                            msg.what = DEBUG;
                            int[] a = new int[8];
                            for (int i = 0; i < 8; i++) {
                                a[i] = p.getData()[i + 5];
                            }
                            msg.obj = a;

                            myHandler.sendMessage(msg);
//                            Log.e(TAG + "getData5", Integer.toString(a));
                        } else {
                            batCount = 1;
                            if (categoryData.getDrone_upload_btn() != null){
                                categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn_on);
                                start_stop_check = true;
                            }
                            connected = true;
                            msg.what = RECEIVED;
                            msg.arg1 = 1;
                            msg.obj = (char) p.getData()[0] + " received";
                            myHandler.sendMessage(msg);
                            Log.e(TAG, "GOT IT");
                        }
//                        String bat = Byte.toString(a); // 굳이 스트링 갈 필요 없다.
                        // If you're not using an infinite loop:
                        //mDataGramSocket.close();
                    } catch (NullPointerException | IOException e) {
                        // no response received after 1 second. continue sending

                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    private class ExampleThread extends Thread {
        private static final String TAG = "ExampleThread";
        String code="";
        Object obj;
        private boolean isRunning;


        public ExampleThread(String code, Object obj) {
            // 초기화 작업
            this.code = code;
            this.obj = obj;
        }

        public void run() {

            while (isRunning) {
                try {
                    Log.e("hello class", obj.getClass().getName());
                    Class<?> cls = Class.forName(obj.getClass().getName());

                    String[] methods = code.split("\n");
                    List<String> list = new ArrayList<>();
                    start_stop_check = false;
                    categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_stop_btn_on);
                    categoryData.getDrone_start_btn().setBackgroundResource(R.drawable.drone_start_on);


                    for (int i = 0; i < methods.length; i++) {
                        Log.e("drone hello methods not trim", methods[i]);
                        methods[i] = methods[i].trim();
                        list.add(methods[i]);
                        Log.e("drone hello methods", methods[i]);
                        Log.e("drone hello methods", list.get(i));
                    }

                    Log.e("drone hello method size", methods.length + "");
                    Log.e("drone hello method list size", list.size() + "");

                    if (list.contains("for(end);")) {
                        Log.e("check for문", "in");
                        List<Integer> values = new ArrayList<>();
                        int current = 0;
                        for (int p = 0; p < methods.length; p++) {
                            if (methods[p].equals("for(end);")) {
                                values.add(p);
                            }
                        }

                        Log.e("drone hello ??", "in");

                        int current_end_pos = 0;
                        Log.e("dronetest list size", list.size() + "");
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).trim().equals("")) {
                                continue;
                            }
                            methods[i] = methods[i].replace("(", " ").replace(");", "").trim();
                            String[] params_check3 = methods[i].split(" ");
                            Log.e("methods i ", methods[i]);
                            if (methods[i].contains("for") && !methods[i].contains("end")) {
                                Log.e("dronetest hello in ", "first if");
                                for (int loop = 0; loop < Integer.parseInt(params_check3[1]); loop++) {

                                        int count = (i + 1);
                                        while (count < values.get(current_end_pos) - i) {
                                            Log.e("methods[count]", methods[count]);
                                            methods[count] = methods[count].trim().replace("(", " ").replace(");", "");
                                            String[] params_check2 = methods[count].split(" ");
                                            Log.e("method size", params_check2.length + "");
                                            if (params_check2.length > 1) {
                                                Log.e("params check", params_check2[0]);
                                                Log.e("params check", params_check2[1]);
                                                Method m = cls.getDeclaredMethod(params_check2[0], long.class);
                                                if (params_check2[0].equals("delay")) {
                                                    params_check2[1] = params_check2[1] + "000";
                                                }
                                                m.invoke(obj, Long.parseLong(params_check2[1]));

                                            } else {
                                                Method m = cls.getDeclaredMethod(params_check2[0]);
                                                m.invoke(obj);
                                            }
                                            count++;
                                        }

                                }
                                i = values.get(current_end_pos) + 1;
                                current_end_pos++;


                            } else if (!methods[i].contains("for")) {
                                Log.e("dronetest hello in ", "second if");
                                for (String method : methods) {

                                        Log.e("thread method", method);
                                        method = method.replace("(", " ").replace(");", "");
                                        Log.e("method", method + "");
                                        String[] params_check = method.split(" ");
                                        Log.e("method size", params_check.length + "");
                                        if (params_check.length > 1) {
                                            Log.e("params check", params_check[0]);
                                            Log.e("params check", params_check[1]);
                                            Method m = cls.getDeclaredMethod(params_check[0], long.class);
                                            if (params_check[0].equals("delay")) {
                                                params_check[1] = params_check[1] + "000";
                                            }
                                            m.invoke(obj, Long.parseLong(params_check[1]));
                                        } else {
                                            Method m = cls.getDeclaredMethod(params_check[0]);
                                            m.invoke(obj);
                                        }

                                }

                            }
//                        if (params_check.length > 1) {
//                            //파라미터 있을 때
//                            Log.e("params check", params_check[0]);
//                            Log.e("params check", params_check[1]);
//
//                        } else {
//                            //파라미터 없을 때
//                            for (String method : methods) {
//                                Log.e("thread method", method);
//                                method = method.replace("(", " ").replace(");", "");
//                                Log.e("method", method + "");
//                                String[] params_check = method.split(" ");
//                                Log.e("method size", params_check.length + "");
//                                if (params_check.length > 1) {
//                                    Log.e("params check", params_check[0]);
//                                    Log.e("params check", params_check[1]);
//                                    Method m = cls.getDeclaredMethod(params_check[0], long.class);
//                                    m.invoke(obj, Long.parseLong(params_check[1]));
//                                } else {
//                                    Method m = cls.getDeclaredMethod(params_check[0]);
//                                    m.invoke(obj);
//                                }
//
//
//                            }
//                        }


                            Log.e("droneTest", "end");
                            Log.e("thread", "end");
                            categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn_on);
                            categoryData.getDrone_start_btn().setBackgroundResource(R.drawable.drone_start_off);
                            start_stop_check = true;
                        }
                    } else {
                        Log.e("dronetest hello in ", "second if");
                        for (String method : methods) {
                                Log.e("thread method", method);
                                method = method.replace("(", " ").replace(");", "");
                                Log.e("method", method + "");
                                String[] params_check = method.split(" ");
                                Log.e("method size", params_check.length + "");
                                if (params_check.length > 1) {
                                    Log.e("params check", params_check[0]);
                                    Log.e("params check", params_check[1]);
                                    Method m = cls.getDeclaredMethod(params_check[0], long.class);
                                    if (params_check[0].equals("delay")) {
                                        params_check[1] = params_check[1] + "000";
                                    }
                                    m.invoke(obj, Long.parseLong(params_check[1]));
                                } else {
                                    Method m = cls.getDeclaredMethod(params_check[0]);
                                    m.invoke(obj);
                                }
                        }
                        Log.e("droneTest", "end");
                        Log.e("thread", "end");
                        categoryData.getDrone_upload_btn().setBackgroundResource(R.drawable.drone_start_btn_on);
                        categoryData.getDrone_start_btn().setBackgroundResource(R.drawable.drone_start_off);
                        start_stop_check = true;

                    }

                    try {
                        isRunning = false;
                        ExampleThread.sleep(1000);
                        ExampleThread.currentThread().interrupt();
                    } catch (InterruptedException e) {
                        ExampleThread.currentThread().interrupt();
                    }

                }catch (ClassNotFoundException e) {
                    Log.e("ExampleThread class not found error", e.getMessage());
                } catch (NoSuchMethodException e) {
                    Log.e("ExampleThread no such method error", e.toString());
                } catch (InvocationTargetException e) {
                    Log.e("ExampleThread invocation target error", e.getMessage());
                } catch (IllegalAccessException e) {
                    Log.e("ExampleThread illegal access error", e.getMessage());
                }
            }
        }

        public void setRunningState(boolean state) {

            isRunning = state;
        }
    }

    @NonNull
    private Pair<BlockGroup, ViewPoint> getWorkspaceBlockGroupForTouch(PendingDrag pendingDrag) {
        Log.e("getworkspace","blockgroupfortouch");
        BlockView touchedBlockView = pendingDrag.getTouchedBlockView();
        Block rootBlock = touchedBlockView.getBlock().getRootBlock();
        Log.e("rootBlock",rootBlock.getType());
        BlockView rootTouchedBlockView = mHelper.getView(rootBlock);
        BlockGroup rootTouchedGroup = rootTouchedBlockView.getParentBlockGroup();

        // Calculate the offset from rootTouchedGroup to touchedBlockView in view
        // pixels. We are assuming the only transforms between BlockViews are the
        // child offsets.
        View view = (View) touchedBlockView;
//        view.setBackgroundColor(Color.parseColor("#FF007F"));


        float offsetX = view.getX() + pendingDrag.getTouchDownViewOffsetX();
        float offsetY = view.getY() + pendingDrag.getTouchDownViewOffsetY();
        ViewGroup parent = (ViewGroup) view.getParent();
        while (parent != rootTouchedGroup) {
            view = parent;
            offsetX += view.getX();
            offsetY += view.getY();
            parent = (ViewGroup) view.getParent();
        }
        ViewPoint touchOffset = new ViewPoint((int) Math.ceil(offsetX),
                (int) Math.ceil(offsetY));

        // Adjust for RTL, where the block workspace coordinate will be in the top right
        if (mHelper.useRtl()) {
            offsetX = rootTouchedGroup.getWidth() - offsetX;
        }

        // Scale into workspace coordinates.
        int wsOffsetX = mHelper.virtualViewToWorkspaceUnits(offsetX);
        int wsOffsetY = mHelper.virtualViewToWorkspaceUnits(offsetY);

        // Offset the workspace coord by the BlockGroup's touch offset.
        mTempWorkspacePoint.setFrom(
                pendingDrag.getTouchDownWorkspaceCoordinates());
        mTempWorkspacePoint.offset(-wsOffsetX, -wsOffsetY);


        ;
//        int itemIndex = getCurrentCategory().indexOf(rootBlock); // Should never be -1
        int itemIndex =0;

        BlockGroup dragGroup = mCallback.getDraggableBlockGroup(itemIndex, rootBlock,
                mTempWorkspacePoint);
        return Pair.create(dragGroup, touchOffset);
    }

    protected FlyoutCallback mCallback = new FlyoutCallback() {
        @Override
        public void onButtonClicked(View v, String action, BlocklyCategory category) {
            if (action == VariableCustomCategory.ACTION_CREATE_VARIABLE && controller != null) {

                controller.requestAddVariable("item");
            }
        }

        @Override
        public BlockGroup getDraggableBlockGroup(int index, Block blockInList,
                                                 WorkspacePoint initialBlockPosition) {
            Log.e("in block","drag");
            Block copy = blockInList.deepCopy();
            copy.setPosition(initialBlockPosition.x, initialBlockPosition.y);
            BlockGroup copyView = controller.addRootBlock(copy);
//            mTrashCategory.removeItem(index);
//            closeTrash();
            return copyView;
        }
    };


    public class Adapter extends RecyclerView.Adapter<BlockViewHolder> {

        @Override
        public int getItemCount() {

            return mCategoryView.mRootCategory.getSubcategories().get(0).getItems().size();
        }

        @Override
        public BlockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new BlockViewHolder(getApplicationContext());
        }

        @Override
        public int getItemViewType(int position) {
//            if (mCurrentCategory == null) {
//                return -1;
//            }
//            Log.e("position",position+"");

//
//            mRecyclerView.setLayoutParams(marginLayoutParams);
            return mCategoryView.mRootCategory.getSubcategories().get(0).getItems().get(position).getType();
        }

        @Override
        public void onBindViewHolder(BlockViewHolder holder, int position) {
//            Log.e("mcurrentcategory",mCurrentCategory.getCategoryName());
            List<BlocklyCategory.CategoryItem> blocks = mCategoryView.mRootCategory.getSubcategories().get(0).getItems();

//            holder.mContainer.setBackgroundColor(Color.parseColor("#B5B2FF"));

            BlocklyCategory.CategoryItem item = blocks.get(position);
            if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BLOCK) {
                Block block = ((BlocklyCategory.BlockItem) item).getBlock();
                block.setEditable(true);
                BlockGroup bg = mHelper.getParentBlockGroup(block);

                if (bg == null) {
                    bg = mHelper.getBlockViewFactory().buildBlockGroupTree(
                            block, mConnectionManager, mTouchHandler);
                } else {
                    bg.setTouchHandler(mTouchHandler);
                }






                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        (int) (ViewGroup.LayoutParams.WRAP_CONTENT ),
                        (int) (ViewGroup.LayoutParams.WRAP_CONTENT ));

                layoutParams.rightMargin = 0;
//                if (block.getType().equals("logic_compare") || block.getType().equals("logic_operation")){
//                    Log.e("here!!!!!","ok");
//                    bg.setScaleX(1f);
//                    bg.setScaleY(1f);
//                    bg.setPivotX(0);
//                    bg.setPivotY(0);
//                    layoutParams.bottomMargin = 40;
//
//                }else{
//                    bg.setScaleX(0.8f);
//                    bg.setScaleY(0.8f);
//                    bg.setPivotX(0);
//                    bg.setPivotY(0);
//                }
//
//
//                if (block.getType().equals("get_weather")){
//                    bg.setScaleX(0.8f);
//                    bg.setScaleY(0.8f);
//                    bg.setPivotX(0);
//                    bg.setPivotY(0);
//                }
//
//
////                bg.setBackgroundColor(Color.parseColor("#B2CCFF"));
//                //이부분 수정
//                if(bg.getParent() != null) {
//                    ((ViewGroup)bg.getParent()).removeView(bg); // <- fix
//                }


                if (!block.getType().equals("turtle_setup_loop")) {
                    holder.mContainer.addView(bg, layoutParams);
                    holder.mContainer.setForegroundGravity(0);
                }



//                    ViewGroup.LayoutParams params = holder.mContainer.getLayoutParams();
//                    holder.mContainer.setTouchDelegate(new TouchDelegate(new Rect(holder.mContainer.getWidth(),0,holder.mContainer.getWidth(),holder.mContainer.getHeight()), bg));
//                holder.mContainer.setScaleX(0.8f);
//                holder.mContainer.setScaleY(0.8f);


//                holder.mContainer.setPivotX(0);
//                holder.mContainer.setPivotY(0);


                holder.bg = bg;



            } else {
                Log.e(TAG, "Tried to bind unknown item type " + item.getType());
            }
        }


        @Override
        public void onViewRecycled(BlockViewHolder holder) {
            // If this was a block item BlockGroup may be reused under a new parent.
            // Only clear if it is still a child of mContainer.
            Log.e("recycled","holder");
            if (holder.bg != null && holder.bg.getParent() == holder.mContainer) {
                holder.bg.unlinkModel();
                holder.bg = null;
                holder.mContainer.removeAllViews();
            }

            super.onViewRecycled(holder);
        }
    }

    private class BlockViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout mContainer;
        BlockGroup bg = null;  // Root of the currently attach block views.
        ImageView imageView;
        BlockViewHolder(Context context) {
            super(new FrameLayout(context));
            mContainer = (FrameLayout) itemView;
        }
    }

    /** {@link Dragger.DragHandler} implementation for BlockListViews. */
    private class DragHandler implements Dragger.DragHandler {

        @Override
        public Runnable maybeGetDragGroupCreator(final PendingDrag pendingDrag) {
            return new Runnable() {
                @Override
                public void run() {
                    // Acquire the draggable BlockGroup on the Workspace from the
                    // {@link OnDragListBlock}.
                    Pair<BlockGroup, ViewPoint> dragGroupAndTouchOffset =
                            getWorkspaceBlockGroupForTouch(pendingDrag);
                    if (dragGroupAndTouchOffset != null) {
                        pendingDrag.startDrag(
                                block_list_view,
                                dragGroupAndTouchOffset.first,
                                dragGroupAndTouchOffset.second);
                        //Log.e("hi~","maybegetdraggroupcreator");
                    }

                }
            };
        }

        @Override
        public boolean onBlockClicked(final PendingDrag pendingDrag) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Identify and process the clicked BlockGroup.
                    getWorkspaceBlockGroupForTouch(pendingDrag);
                    Log.e("onBlock","clicked");
                }
            });
            return true;
        }
    }



}
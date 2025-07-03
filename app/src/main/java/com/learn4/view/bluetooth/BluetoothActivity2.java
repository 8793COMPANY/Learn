package com.learn4.view.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.learn4.R;
import com.learn4.util.Application;
import com.learn4.util.DisplaySize;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity2 extends AppCompatActivity {

    // 방향키 선언
    Button[] key_btn = new Button[8];
    Integer[] key_btn_id = {R.id.left_top, R.id.left_bottom, R.id.left_left, R.id.left_right,
            R.id.right_top, R.id.right_bottom, R.id.right_left, R.id.right_right};

    Button home_btn, keyboard_btn, bluetooth_btn, serial_btn;
    TextView ok_btn, read_text_display;
    ConstraintLayout left_edit_layout, right_edit_layout;

    // 편집키 선언
    EditText[] key_edit = new EditText[8];
    Integer[] key_edit_id = {R.id.ONE, R.id.FOUR, R.id.TWO, R.id.THREE,
            R.id.FIVE, R.id.EIGHT, R.id.SIX, R.id.SEVEN};

    // 시리얼 데이터 출력
    StringBuilder stringBuilder = new StringBuilder();
    String serial_text = "";
    UsbDevice bigBoard;
    int number;

    BluetoothAdapter btAdapter;
    BluetoothSocket btSocket;

    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> btArrayAdapter;
    ArrayList<String> deviceAddressArray;

    private final static int REQUEST_ENABLE_BT = 1;
    private ConnectedBluetoothThread2 connectedBluetoothThread2;

    AlertDialog.Builder builder;
    ListView pair_lv;

    boolean bluetoothCheck = false;

    String connectedDeviceName = "";

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth2);

        // 변수 선언 및 크기 설정
        init_variable();

        // 변수 선언 - 키버튼 따로 설정
        init_variable_key_btn();

        // 하단 소프트키 숨기기
        hide_soft_key();

        // 블루투스 권한 요청
        getPermission();

        // 블루투스 브로드캐스트 리시버(실시간 연결 확인용)
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(broadcastReceiver, filter);

        mContext = this;

        home_btn.setOnClickListener(v->{
            finish();
        });

        keyboard_btn.setOnClickListener(v->{
            ok_btn.setVisibility(View.VISIBLE);

            left_edit_layout.setVisibility(View.VISIBLE);
            right_edit_layout.setVisibility(View.VISIBLE);

            read_text_display.setVisibility(View.GONE); // 숨기기
        });

        ok_btn.setOnClickListener(v->{
            ok_btn.setVisibility(View.GONE);

            left_edit_layout.setVisibility(View.GONE);
            right_edit_layout.setVisibility(View.GONE);
        });

        serial_btn.setOnClickListener(v->{
            if (read_text_display.getVisibility() == View.VISIBLE) {
                read_text_display.setVisibility(View.GONE); // 숨기기
            } else {
                read_text_display.setVisibility(View.VISIBLE); // 보이기

                ok_btn.setVisibility(View.GONE);

                left_edit_layout.setVisibility(View.GONE);
                right_edit_layout.setVisibility(View.GONE);
            }
        });

        bluetooth_btn.setOnClickListener(v->{
            Log.e("bluetooth text check", bluetooth_btn.getText().toString());

            //블루투스 연결 코드
            onBT();

            //블루투스 페어링 연결 확인
            PairingBluetoothListState();

            Log.e("action 확인~~!", "bluetoothCheck : " + bluetoothCheck);

            //블루투스 연결 확인부터
            if (!bluetoothCheck) {
                pairing();
            } else {
                Toast.makeText(getApplicationContext(), "이미 연결된 기기가 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 이건 데이터 읽어올 때 사용하는 부분 (지금 사용되지 않음)
    public void read_data(String data) {
        Log.e("action 확인~~!", "read : " + data);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                read_text_display.setText(data);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler mMonitorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("getHandleMessage", "" + msg.what);

            if (msg.what == 0) {
                // 시리얼 데이터 출력
                open_serial_monitor();
                Log.e("serial isopen : ", Application.mPhysicaloid.isOpened() + "");
                // 반복 실행
                sendEmptyMessage(0);
            } else {
                removeMessages(0);
            }
        }
    };

    // 시리얼 데이터 출력하기
    private void open_serial_monitor() {
        Application.mPhysicaloid.open();
        Log.e("isOpened Serial", "" + Application.mPhysicaloid.isOpened());

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        if (deviceList.isEmpty()) {
            Log.e("read_code generated : ", "isempty 디바이스 리스트");
        } else {
            Set<String> keys = deviceList.keySet();
            for (String key : keys) {
                bigBoard = deviceList.get(key);
            }
        }

        try {
            Application.mPhysicaloid.open();
            Log.e("Application physicaloid", Application.mPhysicaloid.isOpened() + "");

            byte[] buf = new byte[256];
            Application.mPhysicaloid.read(buf, buf.length);

            serial_text += new String(buf, "UTF-8");
            boolean enter_check = false;

            for (int i = 0; i < buf.length; i++) {
                if (buf[i] == 0x0d) {
                    enter_check = true;
                    Log.e("buf enter", "in");
                }
                if (buf[i] == 0x0a) {
                    enter_check = true;
                    Log.e("buf enter2", "in");
                }
            }

            if (serial_text.trim().length() != 0) {
                Log.e("number:", number + "");
                Log.e("length", stringBuilder.toString().length() + "");
                Log.e("text check", serial_text.trim());

                if (serial_text.trim().equals("b1@")) {
                    Log.e("hello serial", "baeWe");
                } else {
                    read_text_display.setText(serial_text.trim());
                    serial_text = "";
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "보드를 연결해주세요.", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    void pairing() {
        builder = new AlertDialog.Builder(this);
        // show paired devices
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.pairing, null);
        builder.setView(dialogView);

        pair_lv = dialogView.findViewById(R.id.pair_lv);
        btArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceAddressArray = new ArrayList<>();

        btArrayAdapter.clear();
        if (deviceAddressArray != null && !deviceAddressArray.isEmpty()) {
            deviceAddressArray.clear();
        }
        Log.e("check devicename next", "first");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.e("check devicename next", "");
        pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                Log.e("check devicename", deviceName);
                String deviceHardwareAddress = device.getAddress(); // MAC address
                btArrayAdapter.add(deviceName);
                deviceAddressArray.add(deviceHardwareAddress);
            }
        }

        pair_lv.setAdapter(btArrayAdapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        pair_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 화면 터치 막기
                                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Toast.makeText(getApplicationContext(), "연결중입니다...", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        try {
                            //Toast.makeText(getApplicationContext(), btArrayAdapter.getItem(position) + " try...", Toast.LENGTH_SHORT).show();

                            final String name = btArrayAdapter.getItem(position); // get name
                            final String address = deviceAddressArray.get(position); // get address
                            boolean flag = true;

                            BluetoothDevice device = btAdapter.getRemoteDevice(address);
                            btSocket = null;

                            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

                            // create & connect socket
                            try {
                                btSocket = device.createRfcommSocketToServiceRecord(uuid);
                                btSocket.connect();
                            } catch (IOException | SecurityException e) {
                                flag = false;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 화면 터치 풀기
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        Toast.makeText(getApplicationContext(), "connection failed!", Toast.LENGTH_SHORT).show();
                                        bluetoothCheck = false;

                                        Log.e("action 확인~~!", e.getMessage());
                                    }
                                });

                                e.printStackTrace();
                            }

                            if (flag) {
                                connectedBluetoothThread2 = new ConnectedBluetoothThread2(btSocket);
                                connectedBluetoothThread2.start();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Handler().postDelayed(() -> {
                                            // 화면 터치 풀기
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                            // 딜레이 후 시작할 코드 작성
                                            Toast.makeText(getApplicationContext(), "connected to " + name, Toast.LENGTH_SHORT).show();
                                            bluetoothCheck = true;
                                            connectedDeviceName = name;

                                            Log.e("action 확인~~!", "onon");
                                            Log.e("action 확인~~!", "onon : " + bluetoothCheck);

                                            // 연결 완료시 시리얼 모니터 출력하기
                                            //mMonitorHandler.sendEmptyMessage(0);
                                        }, 1500);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.e("action 확인~~!", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();
            }
        });
    }

    void onBT() {
        // Enable bluetooth
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("action 확인~~!", "broadcastReceiver");

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Log.e("action 확인~~!", "연결됨");

                bluetooth_btn.setBackgroundResource(R.drawable.bluetooth_bluetooth_on);

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.e("action 확인~~!", "연결 끊김");

                bluetooth_btn.setBackgroundResource(R.drawable.bluetooth_bluetooth_off);
            }
        }
    };

    // 블루투스 연결 장치 확인 여부
    public boolean isConnected(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("isConnected", (Class[]) null);
            boolean connected = (boolean) m.invoke(device, (Object[]) null);
            return connected;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void PairingBluetoothListState() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Set<BluetoothDevice> bluetoothDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            for (BluetoothDevice bluetoothDevice : bluetoothDevices) {
                Log.e("action 확인~~!", "확인! " + bluetoothDevice.getName());

                if (bluetoothDevice.getName().equals(connectedDeviceName)) {
                    if (isConnected(bluetoothDevice)) {
                        //TODO : 연결중인상태
                        bluetoothCheck = true;
                        Log.e("action 확인~~!", "연결됨!");
                    } else {
                        //TODO : 연결중이 아닌상태
                        bluetoothCheck = false;
                        Log.e("action 확인~~!", "연결안됨!");
                    }
                }
            }
        } catch (NullPointerException e) {
            //블루투스 서비스 사용불가인 경우
        }
    }

    public void getPermission() {
        // Get permission
        String[] permission_list = {
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions(BluetoothActivity2.this, permission_list,  1);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private void hide_soft_key() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off.");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private void init_variable() {
        home_btn = findViewById(R.id.home_btn);
        keyboard_btn = findViewById(R.id.keyboard_btn);
        ok_btn = findViewById(R.id.ok_btn);
        ok_btn.setTextSize(DisplaySize.font_size_y_45);
        bluetooth_btn = findViewById(R.id.bluetooth_btn);
        serial_btn = findViewById(R.id.serial_btn);
        read_text_display = findViewById(R.id.read_text_display);
        read_text_display.setMovementMethod(ScrollingMovementMethod.getInstance());
        read_text_display.setTextSize(DisplaySize.font_size_y_45);

        left_edit_layout = findViewById(R.id.left_edit_layout);
        right_edit_layout = findViewById(R.id.right_edit_layout);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init_variable_key_btn() {
        // EditText 리스너
        for (int i = 0; i < key_edit_id.length; i++) {
            key_edit[i] = (EditText) findViewById(key_edit_id[i]);
            key_edit[i].setTextSize(DisplaySize.font_size_y_50);

            int finalI = i;

            // EditText 변경 이벤트
            key_edit[i].addTextChangedListener(new TextWatcher() {
                private String previousText = "";

                @Override
                public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                    previousText = s.toString(); // 이전 텍스트 저장
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String input = s.toString();

                    // 1. 빈 입력 (공백만 있는 경우 포함)
                    if (input.trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "공백 없이 한 글자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 2. 한 글자 초과한 경우
                    if (input.length() > 1) {
                        key_edit[finalI].removeTextChangedListener(this);
                        key_edit[finalI].setText(previousText);
                        key_edit[finalI].setSelection(previousText.length());
                        key_edit[finalI].addTextChangedListener(this);

                        Toast.makeText(getApplicationContext(), "한 글자만 입력할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    }

                    // 3. (선택) 특수문자만 입력했는지 체크 (예: 영어/숫자/한글 제외)
                    // Pattern.matches("[\\p{Punct}]+", input) → 특수문자만
//                    else if (input.matches("[\\p{Punct}\\s]+")) {
//                        Toast.makeText(getApplicationContext(), "특수문자는 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                        key_edit[finalI].removeTextChangedListener(this);
//                        key_edit[finalI].setText("");
//                        key_edit[finalI].addTextChangedListener(this);
//                    }
                }
            });
        }

        for (int i = 0; i < key_btn_id.length; i++) {
            key_btn[i] = (Button) findViewById(key_btn_id[i]);

            int finalI = i;

            // 키 버튼 터치 이벤트
            key_btn[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    // 블루투스 연결 확인 후 전송
                    PairingBluetoothListState();
                    if (!bluetoothCheck) {
                        Toast.makeText(getApplicationContext(), "블루투스 연결을 해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                Log.e("testestttt", "ACTION_DOWN" + finalI);

                                // 버튼마다 다른 on 이미지 설정
                                switch (finalI) {
                                    case 0:
                                    case 4:
                                        v.setBackgroundResource(R.drawable.bluetooth_top_on);
                                        break;
                                    case 1:
                                    case 5:
                                        v.setBackgroundResource(R.drawable.bluetooth_bottom_on);
                                        break;
                                    case 2:
                                    case 6:
                                        v.setBackgroundResource(R.drawable.bluetooth_left_on);
                                        break;
                                    case 3:
                                    case 7:
                                        v.setBackgroundResource(R.drawable.bluetooth_right_on);
                                        break;
                                    default:
                                        break;
                                }

                                connectedBluetoothThread2.write(key_edit[finalI].getText().toString().trim());

                                return true;

                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // 버튼마다 다른 off 이미지 설정
                                switch (finalI) {
                                    case 0:
                                    case 4:
                                        v.setBackgroundResource(R.drawable.bluetooth_top_off);
                                        break;
                                    case 1:
                                    case 5:
                                        v.setBackgroundResource(R.drawable.bluetooth_bottom_off);
                                        break;
                                    case 2:
                                    case 6:
                                        v.setBackgroundResource(R.drawable.bluetooth_left_off);
                                        break;
                                    case 3:
                                    case 7:
                                        v.setBackgroundResource(R.drawable.bluetooth_right_off);
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("action 확인~~!", "onStop()");

        try {
            if (btSocket != null) {
                btSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("action 확인~~!", "onDestroy()");

        unregisterReceiver(broadcastReceiver);
    }
}
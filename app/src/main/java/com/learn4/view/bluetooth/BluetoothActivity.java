package com.learn4.view.bluetooth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.learn4.R;
import com.learn4.util.Application;
import com.learn4.util.DisplaySize;
import com.learn4.view.custom.dialog.WordChangeDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity implements View.OnLongClickListener {

    TextView read_text, write_text, read_text_display, time_text_display;
    Button read_reset_btn, start_btn, stop_btn, reset_btn, send_btn, bluetooth_btn, back_btn;
    EditText write_edit_text;

    Button[] key_btn = new Button[9];
    Integer[] key_btn_id = {R.id.ONE, R.id.TWO, R.id.THREE, R.id.FOUR, R.id.FIVE,
            R.id.SIX, R.id.SEVEN, R.id.EIGHT, R.id.NINE};

    WordChangeDialog wordChangeDialog;
    int num;

    BluetoothAdapter btAdapter;

    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> btArrayAdapter;
    ArrayList<String> deviceAddressArray;

    private final static int REQUEST_ENABLE_BT = 1;
    private ConnectedBluetoothThread connectedThread;
    private Thread timeThread = null;
    private Boolean isRunning = true, isLongPress = false;

    AlertDialog.Builder builder;
    ListView pair_lv;

    public static Context mContext;

    boolean bluetoothCheck = false;

    StringBuilder stringBuilder = new StringBuilder();

    String serial_text = "";
    UsbDevice bigBoard;
    int number;

    BluetoothSocket btSocket;

    String connectedDeviceName = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // 변수 선언
        init_variable();

        // 변수 선언 - 키버튼 따로 설정
        init_variable_key_btn();

        // 변수 글씨 크기 및 패딩값 설정
        init_size();

        // 하단 소프트키 숨기기
        hide_soft_key();

        // 권한 요청
        getPermission();

        // 블루투스 브로드캐스트 리시버(실시간 연결 확인용)
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(broadcastReceiver, filter);

        mContext = this;

        Handler handler = new Handler();

        // 클릭, 롱클릭 둘 다 사용해야 돼서 터치리스터 사용
        bluetooth_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(longPressRunnable, 1500);
                        break;

                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(longPressRunnable);
                        if (!isLongPress) {
                            // user short pressed
                            //Toast.makeText(BluetoothActivity.this, "Short Press", Toast.LENGTH_SHORT).show();
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
                        }
                        break;
                }
                return false;
            }
        });

        read_reset_btn.setOnClickListener(v -> {
            //리드 화면 리셋
            read_text_display.setText("");
        });

        start_btn.setOnClickListener(v -> {
            //타이머 시작
            stop_watch_start();
        });

        start_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openWordChangeDialog(2);
                return true;
            }
        });

        stop_btn.setOnClickListener(v -> {
            //일시정지
            stop_watch_stop();
        });

        stop_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openWordChangeDialog(3);
                return true;
            }
        });

        reset_btn.setOnClickListener(v -> {
            //타이머 리셋(초기화)
            stop_watch_reset();
        });

        reset_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openWordChangeDialog(4);
                return true;
            }
        });

        send_btn.setOnClickListener(v -> {
            Log.e("action 확인~~!", "send_btn");
            Log.e("action 확인~~!", write_edit_text.getText().toString());

            //블루투스 페어링 연결 확인
            PairingBluetoothListState();

            Log.e("action 확인~~!", "onon!! : " + bluetoothCheck);

            //블루투스 연결 확인부터
            if (!bluetoothCheck) {
                Toast.makeText(getApplicationContext(), "블루투스 연결을 해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                if (write_edit_text.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "전송할 단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (write_edit_text.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "전송할 단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    connectedThread.write(write_edit_text.getText().toString().trim());
                }
            }
        });

        back_btn.setOnClickListener(v->{
            finish();
        });
    }

    // 데이터 읽어오기
    public void read_data(String data) {
        Log.e("action 확인~~!", "read : " + data);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                read_text_display.setText(data);

                if (data.equals(start_btn.getText().toString())) {
                    //타이머 시작
                    stop_watch_start();
                } else if (data.equals(stop_btn.getText().toString())) {
                    //일시 정지
                    stop_watch_stop();
                } else if (data.equals(reset_btn.getText().toString())) {
                    //타이머 종료(리셋)
                    stop_watch_reset();
                } else {
                    Log.e("action 확인~~!", "nope");
                }

//                switch (data) {
//                    case "start":
//                        //타이머 시작
//                        stop_watch_start();
//                        break;
//                    case "stop":
//                        //일시 정지
//                        stop_watch_stop();
//                        break;
//                    case "reset":
//                        //타이머 종료
//                        stop_watch_reset();
//                        break;
//                    default:
//                        Log.e("action 확인~~!", "nope");
//                        break;
//                }
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

//    @SuppressLint("HandlerLeak")
//    public Handler mBluetoothHandler  = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Log.e("getHandleMessage", "" + msg.what);
//
//            if (msg.what == 0) {
//                String readMessage = null;
//                try {
//                    readMessage = new String((byte[]) msg.obj, "UTF-8");
//
//                    Log.e("action 확인~~!", "readMessage : " + readMessage);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

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

//                    if (number < 15) {
//                        stringBuilder.setLength(0);
//                        stringBuilder.append(new String(buf).trim());
//                        if (enter_check){
//                            Log.e("buf text",new String(buf).trim());
//                            stringBuilder.append("\n");
//                            enter_check = false;
//                            serial_text = "";
//                            number++;
//                        }
//
//                        Log.e("buf text",new String(buf).trim());
//
//                    } else if (number >= 15) {
//                        stringBuilder.delete(0,stringBuilder.indexOf("\n",1));
//                        Log.e("buf stringBuilder len check",stringBuilder.length()+"");
//                        stringBuilder.append(new String(buf).trim());
//
//                        Log.e("buf stringBuilder check",stringBuilder.toString());
//                        if (enter_check) {
//                            Log.e("buf text",new String(buf).trim());
//                            stringBuilder.append("\n");
//                            enter_check = false;
//                            number++;
//                            serial_text = "";
//                        }
//                        number = 14;
//
//                        Log.e("length delete","OK");
//                    }
//
//                    if (Integer.parseInt(String.valueOf(stringBuilder.toString().length())) > 5120) {
//                        stringBuilder.delete(0,2560);
//                        Log.e("length delete MAX","OK");
//                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "보드를 연결해주세요.", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void stop_watch_start() {
        if (start_btn.isEnabled()) {
            Log.e("action 확인~~!", "start_btn");

            //일시 정지 초기화
            if (!isRunning) {
                isRunning = true;
            }

            //타이머 시작
            timeThread = new Thread(new timeThread());
            timeThread.start();

            //버튼 설정
            start_btn.setEnabled(false);
            stop_btn.setEnabled(true);
            reset_btn.setEnabled(true);

            start_btn.setVisibility(View.GONE);
            stop_btn.setVisibility(View.VISIBLE);
            reset_btn.setVisibility(View.VISIBLE);

            //데이터 보냄
            //connectedThread.write("A");
            //connectedThread.write("start");
        }
    }

    private void stop_watch_stop() {
        if (stop_btn.isEnabled()) {
            if (!start_btn.isEnabled()) {
                Log.e("action 확인~~!", "stop_btn");

                //일시정지
                isRunning = !isRunning;

                //버튼 설정
                start_btn.setEnabled(false);
                stop_btn.setEnabled(true);
                reset_btn.setEnabled(true);

                start_btn.setVisibility(View.GONE);
                stop_btn.setVisibility(View.VISIBLE);
                reset_btn.setVisibility(View.VISIBLE);
            }

//            if (stop_btn.getText().toString().equals("중지")) {
//                stop_btn.setText("재시작");

//            } else {
//                stop_btn.setText("중지");
//            }

            //데이터 보냄
            //connectedThread.write("B");
            //connectedThread.write("stop");
        }
    }

    private void stop_watch_reset() {
        if (reset_btn.isEnabled()) {
            Log.e("action 확인~~!", "reset_btn");

            //타이머 리셋(초기화)
            timeThread.interrupt();

            time_text_display.setText("");
            time_text_display.setText("00:00:00:00");

            //버튼 설정
//            start_btn.setEnabled(true);
//            stop_btn.setEnabled(false);
//            reset_btn.setEnabled(false);
            start_btn.setEnabled(true);
            stop_btn.setEnabled(true);
            reset_btn.setEnabled(true);

            start_btn.setVisibility(View.VISIBLE);
            stop_btn.setVisibility(View.VISIBLE);
            reset_btn.setVisibility(View.VISIBLE);

            //stop_btn.setText("중지");

            //데이터 보냄
            //connectedThread.write("C");
            //connectedThread.write("reset");
        }
    }

    // 롱 클릭 기능 구현
    Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isLongPress = true; // user long pressed
            //Toast.makeText(BluetoothActivity.this, "Long Press", Toast.LENGTH_SHORT).show();
        }
    };

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                                connectedThread = new ConnectedBluetoothThread(btSocket);
                                connectedThread.start();

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

//                try {
//                    Toast.makeText(getApplicationContext(),"연결중입니다...",Toast.LENGTH_SHORT).show();
//                    dialog.cancel();
//                    //Toast.makeText(getApplicationContext(), btArrayAdapter.getItem(position) + " try...", Toast.LENGTH_SHORT).show();
//
//                    final String name = btArrayAdapter.getItem(position); // get name
//                    final String address = deviceAddressArray.get(position); // get address
//                    boolean flag = true;
//
//                    BluetoothDevice device = btAdapter.getRemoteDevice(address);
//                    BluetoothSocket btSocket = null;
//
//                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//
//                    // create & connect socket
//                    try {
//                        btSocket = device.createRfcommSocketToServiceRecord(uuid);
//                        btSocket.connect();
//                    } catch (IOException | SecurityException e) {
//                        flag = false;
//                        Toast.makeText(getApplicationContext(), "connection failed!", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//
//                    if(flag){
//                        connectedThread = new ConnectedBluetoothThread(btSocket);
//                        connectedThread.start();
//
//                        new Handler().postDelayed(() -> {
//                            //딜레이 후 시작할 코드 작성
//                            Toast.makeText(getApplicationContext(), "connected to " + name, Toast.LENGTH_SHORT).show();
//                            connectedThread.run();
//                        }, 2000);
//                    }
//                } catch (Exception e) {
//                    Log.e("action 확인~~!", e.getMessage());
//                    e.printStackTrace();
//                }
            }
        });
    }

    void onBT() {
        // Enable bluetooth
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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

                bluetooth_btn.setBackgroundResource(R.drawable.bluetooth_on);

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.e("action 확인~~!", "연결 끊김");

                bluetooth_btn.setBackgroundResource(R.drawable.bluetooth_off);
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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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

        ActivityCompat.requestPermissions(BluetoothActivity.this, permission_list,  1);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);

            time_text_display.setText(result);
        }
    };

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                time_text_display.setText("");
                                time_text_display.setText("00:00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }

    private void init_variable() {
        read_text = findViewById(R.id.read_text);
        write_text = findViewById(R.id.write_text);
        read_text_display = findViewById(R.id.read_text_display);
        read_text_display.setMovementMethod(ScrollingMovementMethod.getInstance());
        time_text_display = findViewById(R.id.time_text_display);

        read_reset_btn = findViewById(R.id.read_reset_btn);
        start_btn = findViewById(R.id.start_btn);
        stop_btn = findViewById(R.id.stop_btn);
        reset_btn = findViewById(R.id.reset_btn);

        write_edit_text = findViewById(R.id.write_edit_text);
        send_btn = findViewById(R.id.send_btn);

        bluetooth_btn = findViewById(R.id.bluetooth_btn);
        back_btn = findViewById(R.id.back_btn);
    }

    private void init_variable_key_btn() {
        for (int i = 0; i < key_btn_id.length; i++) {
            key_btn[i] = (Button) findViewById(key_btn_id[i]);
            key_btn[i].setTextSize(DisplaySize.font_size_y_30);

            int finalI = i;

            // 키 버튼 클릭 이벤트
            key_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("action 확인~~!", key_btn[finalI].getText()+"");

                    // edit text 입력되는 부분
                    write_edit_text.setText(key_btn[finalI].getText());
                    write_edit_text.setSelection(write_edit_text.getText().length());

                    //블루투스 페어링 연결 확인
                    PairingBluetoothListState();
                    Log.e("action 확인~~!", "onon~~ : " + bluetoothCheck);

                    if (!bluetoothCheck) {
                        Toast.makeText(getApplicationContext(), "블루투스 연결을 해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        // 데이터 전송
                        connectedThread.write(key_btn[finalI].getText().toString().trim());
                    }
                }
            });

            // 키 버튼 롱 클릭 이벤트
            key_btn[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.e("action 확인~~!", key_btn[finalI].getText()+" : Long~!");

                    num = finalI;

                    // 다이얼로그 실행
                    openWordChangeDialog(1);

                    return true;
                }
            });
        }
    }

    private final View.OnClickListener word_change_ok_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("action 확인~~!", "word_change_ok_listener");
            Log.e("action 확인~~!", wordChangeDialog.word_input_box.getText() + "");

            if (wordChangeDialog.word_input_box.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                switch (wordChangeDialog.num) {
                    case 1: // 키패드의 단어를 변경하는 경우
                        key_btn[num].setText(wordChangeDialog.word_input_box.getText().toString().trim());
                        break;
                    case 2: // start 단어를 변경하는 경우
                        start_btn.setText(wordChangeDialog.word_input_box.getText().toString().trim());
                        break;
                    case 3: // stop 단어를 변경하는 경우
                        stop_btn.setText(wordChangeDialog.word_input_box.getText().toString().trim());
                        break;
                    case 4: // reset 단어를 변경하는 경우
                        reset_btn.setText(wordChangeDialog.word_input_box.getText().toString().trim());
                        break;
                }
            }

            wordChangeDialog.dismiss();
        }
    };

    private void openWordChangeDialog(int num) {
        // 다이얼로그 실행
        wordChangeDialog = new WordChangeDialog(BluetoothActivity.this, word_change_ok_listener, num);
        wordChangeDialog.show();

        // 다이얼로그 화면 크기 조절
        Window window = wordChangeDialog.getWindow();

        int x = (int) (Application.displaySize_X * 0.43f);
        int y = (int) (Application.displaySize_Y * 0.278f);
        window.setLayout(x, y);
    }

    private void init_size() {
        read_text.setTextSize(DisplaySize.font_size_y_36);
        write_text.setTextSize(DisplaySize.font_size_y_36);
        read_text_display.setTextSize(DisplaySize.font_size_y_45);
        read_text_display.setPadding((int) DisplaySize.font_size_y_20,0,(int) DisplaySize.font_size_y_20,(int) DisplaySize.font_size_y_20);
        time_text_display.setTextSize(DisplaySize.font_size_y_50);

        read_reset_btn.setTextSize(DisplaySize.font_size_y_28);
        start_btn.setTextSize(DisplaySize.font_size_y_28);
        stop_btn.setTextSize(DisplaySize.font_size_y_28);
        reset_btn.setTextSize(DisplaySize.font_size_y_28);

        write_edit_text.setTextSize(DisplaySize.font_size_y_29);
        write_edit_text.setPadding((int) DisplaySize.font_size_x_20,0,(int) DisplaySize.font_size_x_20,0);
        send_btn.setTextSize(DisplaySize.font_size_y_28);
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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        mMonitorHandler.sendEmptyMessage(1);
//    }

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
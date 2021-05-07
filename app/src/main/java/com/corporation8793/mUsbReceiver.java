package com.corporation8793;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.physicaloid.lib.Boards;
import com.physicaloid.lib.Physicaloid;

import java.util.HashMap;
import java.util.Set;

public class mUsbReceiver extends BroadcastReceiver {
    Context ct;
    Physicaloid physicaloid;
    UsbDevice bigBoard;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ct = context;
        physicaloid = new Physicaloid(context);

        UsbManager manager = (UsbManager) context.getSystemService(context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        if(deviceList.isEmpty()){
            Log.e("USB 감지 : ", "isempty 디바이스 리스트");
        }
        else {
            Set<String> keys = deviceList.keySet();
            for (String key : keys) {
                bigBoard = deviceList.get(key);
            }
        }

        if(action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
            // USB was connected
            Log.d("USB 감지 : ", "연결연결");

            physicaloid.open();
        } else if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
            // USB was disconnected
            Log.d("USB 감지 : ", "실패실패");
        }
    }
}
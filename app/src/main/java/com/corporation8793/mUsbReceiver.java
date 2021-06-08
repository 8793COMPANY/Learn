package com.corporation8793;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.physicaloid.lib.Boards;
import com.physicaloid.lib.Physicaloid;

import java.util.HashMap;

public class mUsbReceiver extends BroadcastReceiver {
    Context ct;
    Physicaloid physicaloid;
    boolean usbCheck = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ct = context;
        physicaloid = new Physicaloid(context);

        if(action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
            // USB was connected
            Log.d("USB 감지 : ", "연결연결");
            physicaloid.open();

        } else if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
            // USB was disconnected
            Log.d("USB 감지 : ", "실패실패");

        }
    }


    public Boolean getCheck(){
        return usbCheck;
    }
}

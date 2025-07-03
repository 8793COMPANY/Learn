package com.learn4.view.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedBluetoothThread2 extends Thread {

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    byte[] buffer;
    int bytes;

    public ConnectedBluetoothThread2(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    @Override
    public void run() {
        buffer = new byte[1024];

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Read from the InputStream
                        bytes = mmInStream.available();
                        if (bytes != 0) {
                            buffer = new byte[1024];
                            SystemClock.sleep(50);
                            bytes = mmInStream.available();
                            bytes = mmInStream.read(buffer, 0, bytes);

                            //데이터 받아옴
                            final String incomingMessage = new String(buffer, 0, bytes);
                            Log.e("From Machine P / bluetoothActivity2 : ", incomingMessage);

                            ((BluetoothActivity2)BluetoothActivity2.mContext).read_data(incomingMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };

        thread.start();
    }

    public void write(String input) {
        Log.d("BluetoothWrite", "input=[" + input + "] len=" + input.length());

        if (input.trim().isEmpty()) {
            Log.w("BluetoothWrite", "전송할 데이터가 비어있습니다. 전송 생략");
            return;
        }

        byte[] bytes = input.getBytes();

        try {
            mmOutStream.write(bytes);
            Log.i("To Machine P", "전송된 데이터: [" + new String(bytes) + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}

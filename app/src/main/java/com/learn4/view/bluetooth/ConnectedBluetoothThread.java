package com.learn4.view.bluetooth;
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ConnectedBluetoothThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    byte[] buffer;
    int bytes;

    public ConnectedBluetoothThread(BluetoothSocket socket) {
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

//                            for (int i = 0; i < buffer.length; i++) {
//                                if (buffer[i] != 0) {
//                                    Log.e("From Machine P3 : ", "buffer : " + buffer[i]);
//                                }
//                            }

                            //데이터 받아옴
                            final String incomingMessage = new String(buffer, 0, bytes);
                            Log.e("From Machine P : ", incomingMessage);

                            ((BluetoothActivity)BluetoothActivity.mContext).read_data(incomingMessage);
                            //((BluetoothActivity)BluetoothActivity.mContext).mBluetoothHandler.obtainMessage(0, bytes, -1, buffer).sendToTarget();

//                            InputStreamReader inputStreamReader = new InputStreamReader(mmInStream);
//                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                            Log.e("From Machine P2 : ", "bufferedReader : " + bufferedReader);
//
//                            byte[] byteArr = new byte[100];
//                            int readByte = mmInStream.read(byteArr);
//                            String data = new String(byteArr, 0, readByte, "UTF-8");
//                            Log.e("From Machine P3 : ", "data : " + data);
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

    /* Call this from the main activity to send data to the remote device */
    public void write(String input) {
        byte[] bytes = input.getBytes();
        try {
            mmOutStream.write(bytes);
            Log.i("To Machine P : ", new String(bytes));
        } catch (IOException e) {
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}
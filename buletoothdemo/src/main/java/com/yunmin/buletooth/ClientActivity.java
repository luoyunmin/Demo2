package com.yunmin.buletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by luoyunmin on 2017/5/30.
 */

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {
    TextView sendMsgInfoTV;
    EditText sendMsgEdt;
    Button sendMsgBtn;
    Button connectionBtn, bondConnectionBtn;
    BluetoothAdapter bluetoothAdapter;
    private static final UUID SERVER_UUID = UUID.fromString("1eebc742-7bae-4797-93bb-747822c67b88");
    MyClientSocket myClientSocket;
    InputStream in;
    OutputStream out;
    private static final int CLIENT_READ_MSG_WHAT = 0x003;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CLIENT_READ_MSG_WHAT:
                    sendMsgInfoTV.setText((String) msg.obj);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        sendMsgInfoTV = (TextView) findViewById(R.id.tv_msg_info);
        sendMsgEdt = (EditText) findViewById(R.id.ed_send_msg);
        sendMsgBtn = (Button) findViewById(R.id.btn_send);
        bondConnectionBtn = (Button) findViewById(R.id.btn_bond_connection);
        connectionBtn = (Button) findViewById(R.id.btn_connection);

        connectionBtn.setOnClickListener(this);
        bondConnectionBtn.setOnClickListener(this);
        sendMsgBtn.setOnClickListener(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bond_connection:
                if (bluetoothAdapter != null) {
                    bluetoothAdapter.cancelDiscovery();
                    Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
                    if (deviceSet.size() > 0) {
                        BluetoothDevice device = deviceSet.iterator().next();
                        myClientSocket = new MyClientSocket(device);
                        myClientSocket.start();
                    }
                }
                break;
            case R.id.btn_connection:
                Toast.makeText(this, "暂时还没做", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_send:
                String msg = sendMsgEdt.getText().toString();
                if (myClientSocket.connecting()) {
                    Toast.makeText(this, "未连接蓝牙", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!"".equals(msg)) {
                    try {
                        if (out != null) {
                            out.write(msg.getBytes());
                        }
                    } catch (IOException e) {
                        Log.e("lym", "client send msg fail");
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private class MyClientSocket extends Thread {
        private BluetoothSocket mClientSocket;

        public MyClientSocket(BluetoothDevice device) {
            try {
                mClientSocket = device.createRfcommSocketToServiceRecord(SERVER_UUID);
                mClientSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    out = mClientSocket.getOutputStream();
                    in = mClientSocket.getInputStream();
                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len = in.read(b)) != -1) {
                        Message msg = handler.obtainMessage();
                        msg.what = CLIENT_READ_MSG_WHAT;
                        msg.obj = new String(b, 0, len);
                        handler.sendMessage(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean connecting() {
            if (mClientSocket == null || mClientSocket.isConnected()) {
                return false;
            }
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("lym", "client ondestory");
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

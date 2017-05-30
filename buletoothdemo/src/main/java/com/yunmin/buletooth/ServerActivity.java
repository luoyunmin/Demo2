package com.yunmin.buletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
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
import java.util.UUID;

/**
 * Created by luoyunmin on 2017/5/30.
 */

public class ServerActivity extends AppCompatActivity implements View.OnClickListener {
    TextView sendMsgInfoTV;
    EditText sendMsgEdt;
    Button sendMsgBtn;
    Button connectionBtn, bondConnectionBtn;
    BluetoothAdapter bluetoothAdapter;
    private static final UUID SERVER_UUID = UUID.fromString("1eebc742-7bae-4797-93bb-747822c67b88");
    int bluetoothConnectionState;
    MyServerSocket myServerSocket;
    InputStream in;
    OutputStream out;
    private static final int SERVER_READ_MSG_WHAT = 0x001;
    private static final int SERVER_WRITE_MSG_WHAT = 0x002;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SERVER_READ_MSG_WHAT:
                    sendMsgInfoTV.setText((String) msg.obj);
                    break;
                case SERVER_WRITE_MSG_WHAT:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
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
                    myServerSocket = new MyServerSocket();
                    myServerSocket.start();
                }
                break;
            case R.id.btn_connection:
                Toast.makeText(this, "暂时还没做", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_send:
                String msg = sendMsgEdt.getText().toString();
                if (!"".equals(msg)) {
                    try {
                        if (out != null) {
                            out.write(msg.getBytes());
                        }
                    } catch (IOException e) {
                        Log.e("lym", "Server send msg fail");
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private class MyServerSocket extends Thread {
        private BluetoothServerSocket mServerSocket;

        public MyServerSocket() {
            try {
                mServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("luoyunmin", SERVER_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            if (mServerSocket != null) {
                BluetoothSocket mClientSocket = null;
                try {
                    //和java的Socket一样，这是一个会阻塞的方法
                    mClientSocket = mServerSocket.accept();
                    Log.e("lym", mClientSocket.getRemoteDevice().getName());
                    in = mClientSocket.getInputStream();
                    out = mClientSocket.getOutputStream();
                    while (true) {
                        byte[] b = new byte[1024];
                        int len = 0;
                        while ((len = in.read(b)) != -1) {
                            Message msg = handler.obtainMessage();
                            msg.what = SERVER_READ_MSG_WHAT;
                            msg.obj = new String(b, 0, len);
                            handler.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("lym", "server ondestory");
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

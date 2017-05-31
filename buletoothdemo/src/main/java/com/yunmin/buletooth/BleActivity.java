package com.yunmin.buletooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by luoyunmin on 2017/5/31.
 */


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BleActivity extends AppCompatActivity implements View.OnClickListener {

    Button bleBtn;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        bleBtn = (Button) findViewById(R.id.btn_ble);
        bleBtn.setOnClickListener(this);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2)
            mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e("lym", "设备不支持BLE");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ble:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2
                        && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    Log.e("lym", "if");
                    mBluetoothAdapter.startLeScan(leScanCallback);
                } else if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Log.e("lym", "else if");
                    BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                    bluetoothLeScanner.startScan(new ScanCallback() {
                        @Override
                        public void onScanResult(int callbackType, ScanResult result) {
                            super.onScanResult(callbackType, result);
                            Log.e("lym", "onScanResult: " + result.getDevice().getName());
                        }

                        @Override
                        public void onScanFailed(int errorCode) {
                            super.onScanFailed(errorCode);
                            Log.e("lym", "onScanFailed");
                        }
                    });
                }
                break;
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Log.e("lym", "onLeScan:" + bluetoothDevice.getName());
        }
    };
}

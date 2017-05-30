package com.yunmin.buletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static android.util.Log.e;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView bluetoothInfo;
    Button openBluetooth, closeBluetooth, systemApiOpenBluetooth;
    Button searchBluetooth;
    Button myDevice, bondedDevice;
    Button bondBluetooth, cancelBonded;
    Button bluetoothCommunication;

    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_OPEN_CODE = 0x001;
    Set<BluetoothDevice> searchDevice = new HashSet<>();
    BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                e("lym", "find device: name: " + device.getName() + "address:" + device.getAddress());
                searchDevice.add(device);
            }
            //搜索完成
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                e("lym", "搜索完成");
            }
            //蓝牙扫描开始
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                e("lym", "蓝牙扫描开始");
            }
            //蓝牙设备绑定过程
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                int blueBondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (blueBondState) {
                    case BluetoothDevice.BOND_NONE:
                        Toast.makeText(context, "设备: " + device.getName() + " 取消绑定", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Toast.makeText(context, "设备: " + device.getName() + " 正在绑定中...", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Toast.makeText(context, "设备: " + device.getName() + " 已成功绑定", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            //蓝牙开启到关闭再到开启的一系列状态变化
            else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.e("lym", "STATE_OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.e("lym", "STATE_TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.e("lym", "STATE_ON");
                        try {
                            Method setScanMode = bluetoothAdapter.getClass().getMethod("setScanMode", int.class, int.class);
                            setScanMode.invoke(bluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 60);
                            //Method setDiscoverableTimeout = bluetoothAdapter.getClass().getMethod("setDiscoverableTimeout", int.class);
//                            Method setScanMode = bluetoothAdapter.getClass().getMethod("setScanMode", int.class);
                            //setDiscoverableTimeout.invoke(bluetoothAdapter,BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 60);
//                            setScanMode.invoke(bluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.e("lym", "STATE_TURNING_OFF");
                        break;
                }
            }
            //监听BluetoothAdapter的scanMode变化
            else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                switch (scanMode) {
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.e("lym", "SCAN_MODE_NONE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.e("lym", "SCAN_MODE_CONNECTABLE");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.e("lym", "SCAN_MODE_CONNECTABLE_DISCOVERABLE");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openBluetooth = (Button) findViewById(R.id.open_bluetooth);
        closeBluetooth = (Button) findViewById(R.id.close_bluetooth);
        systemApiOpenBluetooth = (Button) findViewById(R.id.api_open_bluetooth);
        searchBluetooth = (Button) findViewById(R.id.search_bluetooth);
        myDevice = (Button) findViewById(R.id.my_bluetooth_info);
        bondedDevice = (Button) findViewById(R.id.bonded_bluetooth_info);

        bluetoothCommunication = (Button) findViewById(R.id.bluetooth_communication);
        //配对和取消配对
        bondBluetooth = (Button) findViewById(R.id.bonded_bluetooth);
        cancelBonded = (Button) findViewById(R.id.cancel_bonded);

        bluetoothInfo = (TextView) findViewById(R.id.bluetooth_info);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        openBluetooth.setOnClickListener(this);
        closeBluetooth.setOnClickListener(this);
        systemApiOpenBluetooth.setOnClickListener(this);
        searchBluetooth.setOnClickListener(this);
        myDevice.setOnClickListener(this);
        bondedDevice.setOnClickListener(this);
        bondBluetooth.setOnClickListener(this);
        cancelBonded.setOnClickListener(this);
        bluetoothCommunication.setOnClickListener(this);

        registerBluetoothRrceiver();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_bluetooth:
                adapterOpenBluetooth();
                break;
            case R.id.close_bluetooth:
                if (bluetoothAdapter.isEnabled())
                    bluetoothAdapter.disable();
                break;
            case R.id.api_open_bluetooth:
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(intent, REQUEST_OPEN_CODE);
                //打开的同时设置蓝牙的可见性,这种方法可以设置蓝牙的可见时间
                Log.e("lym", "api");
                Intent timeIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                timeIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(timeIntent, REQUEST_OPEN_CODE);
                break;
            case R.id.search_bluetooth:
                if (bluetoothAdapter != null) {
                    if (!bluetoothAdapter.startDiscovery()) {
                        Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_bluetooth_info:
                printMyDevice();
                break;
            case R.id.bonded_bluetooth_info:
                printBondedDeviceInfo();
                break;
            case R.id.bonded_bluetooth:
                if (searchDevice != null && searchDevice.size() > 0) {
                    BluetoothDevice device = searchDevice.iterator().next();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (device.getBondState() == BluetoothDevice.BOND_NONE)
                            device.createBond();
                    }
                } else {
                    Toast.makeText(this, "请先搜索蓝牙设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_bonded:
                Set<BluetoothDevice> bondedDevice = bluetoothAdapter.getBondedDevices();
                if (bondedDevice.size() > 0) {
                    BluetoothDevice device = bondedDevice.iterator().next();
//                Method[] methodArray = device.getClass().getDeclaredMethods();
//                for (Method m : methodArray) {
//                    //Log.e("lym", "method name: " + m.getName());
//                    String methodName = m.getName();
//                    if ("removeBond".equals(methodName)) {
//                        Log.e("lym", "method name: " + methodName);
//                        Log.e("lym", "method return type: " + m.getGenericReturnType());
//                        Type[] types = m.getGenericParameterTypes();
//                        for (Type t : types) {
//                            Log.e("lym", t.getClass().getName());
//                        }
//                    }
//                }
                    try {
                        Method method = device.getClass().getMethod("removeBond", (Class) null);
                        method.invoke(device, (Object) null);
                    } catch (NoSuchMethodException e) {
                        e("lym", "noSuch");
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e("lym", "invocation");
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e("lym", "Ill");
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "没有已经配对的设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bluetooth_communication:
                Intent intent = new Intent(this, CommunicationActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "别瞎点", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_OPEN_CODE == requestCode) {
            e("lym", "resultCode: " + resultCode);
        }
    }

    private void registerBluetoothRrceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(broadCastReceiver, intentFilter);
    }

    private void adapterOpenBluetooth() {

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();

            }
        }
    }

    private void printMyDevice() {
        if (bluetoothAdapter != null) {
            StringBuilder sb = new StringBuilder("本设备蓝牙信息\n");
            sb.append("address:");
            sb.append(bluetoothAdapter.getAddress());
            sb.append("\n");
            sb.append("name:");
            sb.append(bluetoothAdapter.getName());
            sb.append("\n");
            sb.append("isDecovering:");
            sb.append(bluetoothAdapter.isDiscovering());
            bluetoothInfo.setText(sb.toString());
        }
    }

    private void printBondedDeviceInfo() {
        if (bluetoothAdapter != null) {
            StringBuilder sb = new StringBuilder("已配对蓝牙信息\n");
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

            }
            for (BluetoothDevice b : bondedDevices) {
                sb.append("\n");
                sb.append("address:");
                sb.append(b.getAddress());
                sb.append("\nname:");
                sb.append(b.getName());
            }
            bluetoothInfo.setText(sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }
}

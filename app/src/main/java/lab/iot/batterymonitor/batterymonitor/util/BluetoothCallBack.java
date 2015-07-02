package lab.iot.batterymonitor.batterymonitor.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * Created by zhujianjie on 1/7/2015.
 */
public class BluetoothCallBack implements BluetoothAdapter.LeScanCallback {
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e("BluetoothCallBack","address = "+device.getAddress() +" Rssi = "+rssi);
    }
}

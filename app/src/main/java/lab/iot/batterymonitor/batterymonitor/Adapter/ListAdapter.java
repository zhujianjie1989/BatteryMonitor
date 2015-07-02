package lab.iot.batterymonitor.batterymonitor.Adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lab.iot.batterymonitor.batterymonitor.R;
import lab.iot.batterymonitor.batterymonitor.activity.MainActivity;
import lab.iot.batterymonitor.batterymonitor.util.BluetoothCallBack;
import lab.iot.batterymonitor.batterymonitor.view.ViewHolder;

/**
 * Created by zhujianjie on 26/6/2015.
 */
public class ListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<Map<String, String>> mData;
        private MainActivity mainActivity;
        private  BluetoothAdapter  mBluetoothAdapter;

        private List<Map<String, String>> getData() {

            List<Map<String, String>> list = new ArrayList<Map<String, String>>();

            Map<String, String> map = new HashMap<String, String>();
          /*  map.put("title", "Bluetooth");
            map.put("info", "Bluetooth");
            list.add(map);*/

            map = new HashMap<String, String>();
            map.put("title", "Magnetic");
            map.put("info", "Magnetic");

            list.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Light");
            map.put("info", "Light");
            list.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Proximity");
            map.put("info", "Proximity");
            list.add(map);


            map = new HashMap<String, String>();
            map.put("title", "Accelerometer");
            map.put("info", "Accelerometer");
            list.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Gyroscope");
            map.put("info", "Gyroscope");
            list.add(map);

            map = new HashMap<String, String>();
            map.put("title", "Bluetooth");
            map.put("info", "Bluetooth");
            list.add(map);

            return list;
        }

        public ListAdapter(MainActivity context){
            this.mInflater = LayoutInflater.from(context);
            this.mData = getData();
            this.mainActivity = context;
            initBluetooth();
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

             ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.sensor_layout, null);

                holder.title = (TextView)convertView.findViewById(R.id.TV_Title);
                holder.info = (TextView)convertView.findViewById(R.id.textView4);
                holder.spinner = (Spinner) convertView.findViewById(R.id.SP_frequency);
                holder.toggleButton = (ToggleButton)convertView.findViewById(R.id.TB_Switch);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            holder.title.setText((String) mData.get(position).get("title"));
            holder.info.setText((String) mData.get(position).get("info"));

            String[] m={"Fast","Game","UI","Normal"};

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(convertView.getContext(),android.R.layout.simple_spinner_item,m);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            holder.spinner.setAdapter(adapter2);

            final ViewHolder finalHolder = holder;
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("spinner onItemClick", "" + position);
                    finalHolder.sp_selectIndex = position;
                    ListAdapter.this.mainActivity.changeSensorSampleRate(finalHolder.TB_index,position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });


            holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    String key = mData.get(position).get("title");
                    Log.e("toggleButton", "toggleButton onclick  " + position + " " + isChecked + " " + key);
                    if (position == 5) {
                        final BluetoothCallBack callBack =new BluetoothCallBack();
                        if (isChecked) {
                            timer = new Timer();
                            setOff_flag(false);

                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (isOff_flag()){
                                        mBluetoothAdapter.stopLeScan(callBack);
                                        timer.cancel();
                                        return;
                                    }
                                    if (getFlag()){
                                        mBluetoothAdapter.startLeScan(callBack);
                                    }
                                    else{
                                        mBluetoothAdapter.stopLeScan(callBack);
                                    }
                                    setFlag(!getFlag());
                                }
                            },1000,(finalHolder.sp_selectIndex+1)*1000);

                        } else {
                            setOff_flag(true);

                            Log.e("timer.cancel", "timer.canceltimer.canceltimer.canceltimer.canceltimer.cancel");


                        }
                        return;
                    }
                    MainActivity.flags.put(key, isChecked);
                    if (isChecked) {
                        ListAdapter.this.mainActivity.registerSensor(position, finalHolder.sp_selectIndex);
                        finalHolder.TB_index=position;
                    } else {
                        ListAdapter.this.mainActivity.unRegister(position);
                        finalHolder.TB_index = -1;
                    }
                }
            });
            return convertView;
        }

    private boolean flag =true;

    public boolean isOff_flag() {
        return off_flag;
    }

    public void setOff_flag(boolean off_flag) {
        this.off_flag = off_flag;
    }

    private boolean off_flag = false;
    private Timer timer = new Timer();
    private void setFlag(boolean flag){
        this.flag = flag;
    }

    private boolean getFlag(){
        return flag;
    }
    private void initBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            (new Toast(this.mainActivity)).makeText(this.mainActivity,"mBluetoothAdapter == null",Toast.LENGTH_LONG);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.mainActivity.startActivityForResult(enableBtIntent, 0);
            (new Toast(this.mainActivity)).makeText(this.mainActivity, "mBluetoothAdapter.isEnabled() ==  no", Toast.LENGTH_LONG);
        }

    }



}
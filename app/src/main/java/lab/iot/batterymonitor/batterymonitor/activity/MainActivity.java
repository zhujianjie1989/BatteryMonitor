package lab.iot.batterymonitor.batterymonitor.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import lab.iot.batterymonitor.batterymonitor.Adapter.ListAdapter;
import lab.iot.batterymonitor.batterymonitor.R;
import lab.iot.batterymonitor.batterymonitor.service.BatteryService;
import lab.iot.batterymonitor.batterymonitor.util.BluetoothCallBack;
import lab.iot.batterymonitor.batterymonitor.view.ViewHolder;


public class MainActivity extends Activity {
    private final int SENSOR_DELAY_FASTEST = 0;
    private final int SENSOR_DELAY_GAME = 1;
    private final int SENSOR_DELAY_UI = 2;
    private final int SENSOR_DELAY_NORMAL = 3;
    public    int curr_power = -1;
    private BatteryService batteryService;
    public static Map<String,ViewHolder>viewHolderMap = new HashMap<String,ViewHolder>();
    public SensorManager  sensorManager ;
    public int[] sensorType = {Sensor.TYPE_MAGNETIC_FIELD,Sensor.TYPE_LIGHT,Sensor.TYPE_PROXIMITY,Sensor.TYPE_ACCELEROMETER,Sensor.TYPE_GYROSCOPE};
    public int[] sensorSampleType={SENSOR_DELAY_FASTEST,SENSOR_DELAY_GAME ,SENSOR_DELAY_UI,SENSOR_DELAY_NORMAL};
    public String[] sensorName = {"Magnetic","Light","Proximity","Accelerometer","Gyroscope"};
    public Map<String,Sensor> sensorMap = new HashMap<String,Sensor>();
    public boolean button_flag = false;
    private Timer timer;
    private BluetoothAdapter mBluetoothAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TextView capcity = (TextView)findViewById(R.id.TV_Curr_capacity);
            TextView curr = (TextView)findViewById(R.id.TV_Curr_level);
            TextView usage = (TextView)findViewById(R.id.TV_usage);
            if(curr_power == -1)
            {
                curr_power = msg.arg2;
            }
            capcity.setText(curr_power+"");
            curr.setText((msg.arg2)+"");
            usage.setText((curr_power-msg.arg2)+"");
            if (!button_flag) {
                Button startButton = (Button)findViewById(R.id.BT_Start);
                startButton.setEnabled(true);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    //    batteryService.stopTimer();
    }

    public  void updata(String capcity,String level){

        Message message  =  new Message();
        message.arg1 = Integer.parseInt(capcity)/1000;
        message.arg2 = Integer.parseInt(level)/1000;
        handler.sendMessage(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView)findViewById(R.id.listView);
        ListAdapter adapter = new ListAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("setOnItemClickListener", "" + position);
            }
        });

        batteryService= new BatteryService(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initBluetooth();
        final Button startButton = (Button)findViewById(R.id.BT_Start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> keys = viewHolderMap.keySet();
                Iterator<String> ite = keys.iterator();

                if (!button_flag) {
                    EditText time = (EditText) findViewById(R.id.EB_Timer);
                    batteryService.startTimer(Integer.parseInt(time.getText().toString()));
                    curr_power=-1;
                    startButton.setEnabled(false);

                    while (ite.hasNext()) {
                        String key = ite.next();
                        if (viewHolderMap.get(key).flag) {
                            if (viewHolderMap.get(key).SensorIndex == 5) {
                                Log.e("start bluetooth", "start bluetooth");
                                final BluetoothCallBack callBack = new BluetoothCallBack();
                                timer = new Timer();
                                setOff_flag(false);
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (isOff_flag()) {
                                            mBluetoothAdapter.stopLeScan(callBack);
                                            timer.cancel();
                                            return;
                                        }
                                        if (getFlag()) {
                                            mBluetoothAdapter.startLeScan(callBack);
                                        } else {
                                            mBluetoothAdapter.stopLeScan(callBack);
                                        }
                                        setFlag(!getFlag());
                                    }
                                }, 1000, (viewHolderMap.get(key).sp_selectIndex + 1) * 1000);

                            } else {
                                registerSensor(viewHolderMap.get(key).SensorIndex, viewHolderMap.get(key).sp_selectIndex);
                            }

                        }
                    }

                    button_flag = !button_flag;
                }


            }
        });



    }

    public void stopRun(){
        Log.e("unRegister", " unRegister ");
        Set<String> keys = viewHolderMap.keySet();
        Iterator<String> ite = keys.iterator();
        if (button_flag){

            while (ite.hasNext()) {
                String key = ite.next();
                Log.e("unRegister","  "+key);
                if (viewHolderMap.get(key).flag) {
                    if (viewHolderMap.get(key).SensorIndex == 5) {
                        setOff_flag(true);
                        continue;
                    }
                    unRegister(viewHolderMap.get(key).SensorIndex);

                }
            }
            button_flag = !button_flag;
        }

    }
    private boolean off_flag = false;
    public boolean isOff_flag() { return off_flag;}
    public void setOff_flag(boolean off_flag) {this.off_flag = off_flag;}

    private boolean flag =true;
    private void setFlag(boolean flag){this.flag = flag;}
    private boolean getFlag(){  return flag;    }

    private void initBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            (new Toast(this)).makeText(this,"mBluetoothAdapter == null",Toast.LENGTH_LONG);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.startActivityForResult(enableBtIntent, 0);
            (new Toast(this)).makeText(this, "mBluetoothAdapter.isEnabled() ==  no", Toast.LENGTH_LONG);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void registerSensor(int SensorIndex,int  samplingPeriodUsIndex){

        Sensor sensor = sensorManager.getDefaultSensor(sensorType[SensorIndex]);
        if (sensor != null) {
            Log.e("registerSensor", sensorName[SensorIndex]);
            sensorManager.registerListener(myListener, sensor, sensorSampleType[samplingPeriodUsIndex]);
            sensorMap.put(sensorName[SensorIndex],sensor);
        }

    }

    public void changeSensorSampleRate(int SensorIndex,int  samplingPeriodUsIndex){
        if (SensorIndex==-1)
            return;

        Log.e("changeSensorSampleRate", sensorName[SensorIndex]);
        this.unRegister(SensorIndex);
        this.registerSensor(SensorIndex, samplingPeriodUsIndex);
    }

    public void unRegister(int SensorIndex){
        Log.e("unRegisterSensor", sensorName[SensorIndex]);
        sensorManager.unregisterListener(myListener,sensorMap.get(sensorName[SensorIndex]));
    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {


                DealWithSensorEvent event = new DealWithSensorEvent(sensorEvent);
                Thread thread = new Thread(event);
                thread.start();

        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    class  DealWithSensorEvent implements  Runnable
    {
        private SensorEvent sensorEvent = null;
        private float[] value = new float[3];
        public DealWithSensorEvent( SensorEvent sensorEvent)
        {
            this.sensorEvent= sensorEvent;
        }

        @Override
        public void run() {



            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {

                if( viewHolderMap.get("Magnetic").flag)
                {
                   // Log.e("Magnetic","Magnetic");
                    value = sensorEvent.values;


                }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT  )
            {

                if( viewHolderMap.get("Light").flag) {
                  //  Log.e("Light","Light");
                    value = sensorEvent.values;

                }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY  )
            {

                if( viewHolderMap.get("Proximity").flag) {
                  //  Log.e("Proximity","Proximity");
                    value = sensorEvent.values;
                }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
            {

                if( viewHolderMap.get("Accelerometer").flag) {
                 //   Log.e("Accelerometer","Accelerometer");
                    value = sensorEvent.values;

                }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE  )
            {

                if( viewHolderMap.get("Gyroscope").flag) {
                  //  Log.e("Gyroscope","Gyroscope");
                    value = sensorEvent.values;
                }
            }


        }
    }

}

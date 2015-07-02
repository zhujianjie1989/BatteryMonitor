package lab.iot.batterymonitor.batterymonitor.activity;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import lab.iot.batterymonitor.batterymonitor.Adapter.ListAdapter;
import lab.iot.batterymonitor.batterymonitor.R;
import lab.iot.batterymonitor.batterymonitor.service.BatteryService;


public class MainActivity extends Activity {
    private final int SENSOR_DELAY_FASTEST = 0;
    private final int SENSOR_DELAY_GAME = 1;
    private final int SENSOR_DELAY_UI = 2;
    private final int SENSOR_DELAY_NORMAL = 3;
    public    int curr_power = -1;
    private BatteryService batteryService;
    public static Map<String,Boolean>flags = new HashMap<String,Boolean>();
    public SensorManager  sensorManager ;
    public int[] sensorType = {Sensor.TYPE_MAGNETIC_FIELD,Sensor.TYPE_LIGHT,Sensor.TYPE_PROXIMITY,Sensor.TYPE_ACCELEROMETER,Sensor.TYPE_GYROSCOPE};
    public int[] sensorSampleType={SENSOR_DELAY_FASTEST,SENSOR_DELAY_GAME ,SENSOR_DELAY_UI,SENSOR_DELAY_NORMAL};
    public String[] sensorName = {"Magnetic","Light","Proximity","Accelerometer","Gyroscope"};
    public Map<String,Sensor> sensorMap = new HashMap<String,Sensor>();


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
                Log.e("listView.setOnItemClickListener", "" + position);
            }
        });

        batteryService= new BatteryService(this);
        batteryService.startTimer();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


            value = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {

                if( flags.get("Magnetic"))
                {
                  //  Log.e("Magnetic","Magnetic");
                   float curr = value[0]/10;

                }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT  )
            {

                if( flags.get("Light")) {
                 //   Log.e("Light","Light");

                }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY  )
            {

                if( flags.get("Proximity")) {

                 //   Log.e("Proximity","Proximity");
                }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
            {

                if( flags.get("Accelerometer")) {
                  //  Log.e("Accelerometer","Accelerometer");

                }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE  )
            {

                if( flags.get("Gyroscope")) {

                 //   Log.e("Gyroscope","Gyroscope");
                }
            }


        }
    }

}

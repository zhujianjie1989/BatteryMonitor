package lab.iot.batterymonitor.batterymonitor.util;

import android.hardware.SensorEvent;

/**
 * Created by zhujianjie on 1/7/2015.
 */
public class  DealWithSensorEvent implements  Runnable{
        private SensorEvent sensorEvent = null;
        private float[] value = new float[3];
        public DealWithSensorEvent( SensorEvent sensorEvent)
                {
                this.sensorEvent= sensorEvent;
                }

    @Override
    public void run() {
         /*   value = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE)
            {

            curr = value[0]/10;
            if( PRESSURE.isChecked())
            {

            if (wireless.isChecked())
            {
            UDPSend("pre("+curr+",0,0)");
            }
            else  if (database.isChecked())
            {
            sqlite.intsertBarometer(description.getText().toString(),"",df.format(curr));
            }

            }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER  )
            {
            //Log.e("zhujianjie","CB_Accelerometer tttt");

            if( ACCELEROMETER.isChecked())
            {
            if (wireless.isChecked())
            {
            UDPSend("acc("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
            }
            else  if (database.isChecked())
            {
            sqlite.intsertAccelerator(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
            }

            }
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE  )
            {
            //Log.e("zhujianjie","CB_Gyroscope tttt");

            if( GYROSCOPE.isChecked())
            {
            if (wireless.isChecked())
            {
            UDPSend("gyr("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
            }
            else  if (database.isChecked())
            {
            sqlite.intsertGyroscope(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
            }

            }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION )
            {
            //Log.e("zhujianjie","CB_Orientation tttt");

            if( ORIENTATION.isChecked())
            {
            if (wireless.isChecked())
            {
            UDPSend("ori("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
            }
            else  if (database.isChecked())
            {
            sqlite.intserOrientation(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
            }

            }
            }else if ( sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD  )
            {
            //Log.e("zhujianjie","CB_Megnetic_field tttt");

            if( Megnetic_field.isChecked())
            {
            if (wireless.isChecked())
            {
            UDPSend("mag("+df.format(value[0])+","+df.format(value[1])+","+df.format(value[2])+")");
            }
            else  if (database.isChecked())
            {
            sqlite.intsertMagnetic_field(description.getText().toString(),df.format(value[0]),df.format(value[1]),df.format(value[2]));
            }

            }
            }

*/
            }
        }
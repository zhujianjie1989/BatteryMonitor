
package lab.iot.batterymonitor.batterymonitor.service;

import android.util.Log;

import java.util.Date;

import lab.iot.batterymonitor.batterymonitor.activity.MainActivity;
import lab.iot.batterymonitor.batterymonitor.util.BatteryTimer;

/**
 * Created by zhujianjie on 30/6/2015.
 */

public class BatteryService {
    private BatteryTimer timer;
    private MainActivity activity;
    private Date startTime;
    private int lastTime;
    private boolean flag=false;

    public  BatteryService(MainActivity activity){

        this.activity = activity;
    }

    public void startTimer(int lasttime){
        this.lastTime=lasttime*60*1000;
        if (!flag) {
            timer= new BatteryTimer(this,1000,500);
            timer.startTimer();
            startTime = new Date();
            flag = !flag;
        }


    }

    public void stopTimer(){
        if (flag) {
            timer.stopTimer();
            flag = !flag;
        }

    }

    public void callback(String capcity,String level){

        Date now = new Date();
        Log.e("time diff",(now.getTime() - startTime.getTime() )+"  "+this.lastTime);
        if ((now.getTime() - startTime.getTime() ) > this.lastTime)
        {
            activity.stopRun();
            stopTimer();

        }
        activity.updata(capcity,level);
      //  Log.e("BatteryService updata", capcity);


    }




}

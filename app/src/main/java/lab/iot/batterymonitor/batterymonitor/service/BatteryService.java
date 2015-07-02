
package lab.iot.batterymonitor.batterymonitor.service;

import lab.iot.batterymonitor.batterymonitor.activity.MainActivity;
import lab.iot.batterymonitor.batterymonitor.util.BatteryTimer;

/**
 * Created by zhujianjie on 30/6/2015.
 */

public class BatteryService {
    private BatteryTimer timer;
    private MainActivity activity;

    public  BatteryService(MainActivity activity){
        timer= new BatteryTimer(this,1000,500);
        this.activity = activity;
    }

    public void startTimer(){
        timer.startTimer();
    }

    public void stopTimer(){
        timer.stopTimer();
    }

    public void callback(String capcity,String level){
      //  Log.e("BatteryService updata", capcity);
        activity.updata(capcity,level);

    }




}

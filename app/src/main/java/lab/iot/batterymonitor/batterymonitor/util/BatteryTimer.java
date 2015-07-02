
package lab.iot.batterymonitor.batterymonitor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Timer;
import java.util.TimerTask;

import lab.iot.batterymonitor.batterymonitor.service.BatteryService;

/**
 * Created by zhujianjie on 30/6/2015.
 */

public class BatteryTimer {
    private int delay;
    private int period;
    private BatteryService service;
    private Timer timer = new Timer();
    private File file  = new File("/sys/class/power_supply/battery/uevent");
    public BatteryTimer(BatteryService service ,int delay,int period){
        this.delay = delay;
        this.period = period;
        this.service = service;
    }

    public void startTimer(){
        this.timer.schedule(task,delay,period);
    }

    public void stopTimer(){
        this.stopTimer();
    }


private TimerTask task = new TimerTask() {
        @Override
        public void run() {
           // Log.e("file ", file.getPath() + " " + file.exists());
            BufferedReader reader = null ;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = null;
                String result1=null;
                String result2 = null;
                while ((line = reader.readLine()) != null) {
                   // Log.e("line " , line );

                    if (line.contains("POWER_SUPPLY_CHARGE_FULL")){
                        result1 = line.substring(line.indexOf('=')+1);
                    }

                    if (line.contains("POWER_SUPPLY_CHARGE_COUNTER=")){
                     //   Log.e("line " , line );
                        result2 = line.substring(line.indexOf('=')+1);
                        BatteryTimer.this.service.callback(result1,result2);

                        break;
                    }

                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}


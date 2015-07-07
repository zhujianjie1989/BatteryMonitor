package lab.iot.batterymonitor.batterymonitor.view;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by zhujianjie on 26/6/2015.
 */
public class ViewHolder {
    public TextView title;
    public TextView info;
    public Spinner spinner;
    public ToggleButton toggleButton;
    public int sp_selectIndex=0;
    public int SensorIndex=-1;
    public boolean flag=false;
}

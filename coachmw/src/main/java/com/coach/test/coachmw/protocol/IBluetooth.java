package com.coach.test.coachmw.protocol;

import com.coach.test.coachmw.datastructure.DeviceInformation;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-02-17.
 */

public interface IBluetooth{
    public void connectionState(int state);
    public void changedScanList(ArrayList<DeviceInformation> list);
}
package com.coach.test.myapplication.adapter;

import java.util.ArrayList;

import com.coach.test.myapplication.R;
import com.coach.test.coachmw.datastructure.DeviceInformation;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-02-17.
 */

/**
 * Device ListView Adapter.
 * @author user
 *
 */
public class DeviceInformAdapter extends ArrayAdapter<DeviceInformation> {
    private static final String tag = "DeviceInformAdapter";
    private ArrayList<DeviceInformation> items;
    private LayoutInflater inflater;
    public DeviceInformAdapter(Context context, int resource,
                               ArrayList<DeviceInformation> objects) {
        super(context, resource, objects);
        this.items = objects;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null) {
            v = inflater.inflate(R.layout.listview_device, null);
        }
        DeviceInformation dev = items.get(position);
        if(dev != null) {
            TextView name = (TextView) v.findViewById(R.id.listDevName);
            TextView mac = (TextView) v.findViewById(R.id.listDevMac);

            if(dev.getName() != null && dev.getMac() != null) {
                name.setText(dev.getName());
                mac.setText(dev.getMac());
            }

            // bug fix
			/*if(dev.getName() == null && dev.getMac() == null) {
				String notFound = v.getResources().getString(R.string.bt_not_found);
				name.setText(notFound);
			} else {
				name.setText(dev.getName());
				mac.setText(dev.getMac());
			}*/
        }
        return v;
    }
}
package com.coach.test.coachmw.datastructure;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Administrator on 2017-02-17.
 */

// 어레이 정보 클래스
public final class DeviceInformation {
    private String name;
    private String mac;
    private BluetoothDevice device;
    //private ArrayList<Services> service;
    public DeviceInformation() {
        this.name = null;
        this.mac = null;
        this.device = null;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public BluetoothDevice getDevice() {
        return device;
    }
    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
	/*public ArrayList<Services> getService() {
		return service;
	}
	public void setService(ArrayList<Services> service) {
		this.service = service;
	}*/
}
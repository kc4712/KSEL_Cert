package com.coach.test.coachmw.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import com.coach.test.coachmw.datastructure.Attribute;
import com.coach.test.coachmw.datastructure.DeviceInformation;
import com.coach.test.coachmw.protocol.IBluetooth;
import com.coach.test.coachmw.protocol.INordicFormat;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.util.Log;


/**
 * Created by Administrator on 2017-02-17.
 */


public final class BluetoothManager {
    private static final String tag = "BluetoothManager";

    /** 변수 **/
    protected static final int arrayLength = 8;
    private static int countGattClient = 0;
    private final static int REMOTE_COUNT_NOT = 0;
    public final static int REMOTE_COUNT_1 = 1;
    public final static int REMOTE_COUNT_2 = 2;

    private final static byte CMD_VIBRATE = 0x07;
    private final static byte LEN_VIBRATE = 2; // 헤더 없이 2
    private final static byte TYPE_START_VIBRATE = 1;
    private final static byte TYPE_STOP_VIBRATE = 2;

    public final static int CONNECT = 1;
    public final static int DISCONNECT = 2;
    public final static int CONNECTION_COMPLETE = 3;

    private static int STATE_CONNECTION = DISCONNECT;

    private final static String COACH_NAME = "CO";
    private final static String COACH_REPLACE_NAME = "iBODY24 Coach ";

    /** 데이터 세팅 flag **/
    private static byte COUNT_ANT_DATA_1 = 0;
    private static byte PRECOUNT_ANT_DATA_1 = 0;
    private static byte COUNT_ANT_DATA_2 = 0;
    private static byte PRECOUNT_ANT_DATA_2 = 0;

    /**
     * 블루투스 통신 CMD
     */
    protected final static byte CMD_REQ_USER_DATA = 1;
    protected final static byte CMD_REQ_RTC_DATA = 2;
    protected final static byte CMD_DATA_FEATURE = 3;
    protected final static byte CMD_REQ_BATTERY = 4;
    protected final static byte CMD_REQ_FIRMWARE_DOWNLOAD_READY = 5;
    protected final static byte CMD_REQ_IBODY24_RESET = 6;
    protected final static byte CMD_REQ_RESERVED = 7;
    protected final static byte CMD_REQ_RESERVED_2 = 8;
    protected final static byte CMD_REQ_GET_CONNECTION_STATE = 9;
    protected final static byte CMD_REQ_RESERVED_3 = 10;
    protected final static byte CMD_REQ_FEATURE_INFORM = 11;
    protected final static byte CMD_REQ_CONFIRM = 12;

    protected final static byte MALE = 0;
    protected final static byte FEMALE = 1;

    protected final static byte SEQ_1 = 1;
    protected final static byte SEQ_2 = 2;
    protected final static byte SEQ_3 = 3;
    protected final static byte SEQ_4 = 4;
    protected final static byte SEQ_5 = 5;

    protected final static byte DIR_REQUEST = 1;
    protected final static byte DIR_REPLY = 2;
    protected final static byte DIR_ERR = 3; // crc 는 payload 만..
    protected final static byte DIR_SUCCES = 4;

    private static short SIZE_PAST_FEATURE = 0;

    protected final static byte LEN_SEQ_DATA = 3;
    protected final static byte LEN_TIME_DATA = 4;
    protected final static byte LEN_CRC_ERR_FRAME = 5;
    protected final static byte LEN_SUCCESS_FRAME = 6;
    protected final static byte LEN_STATE_CONNECTION_FRAME = 5;
    protected final static byte LEN_USER_DATA = 4;
    protected final static byte LEN_RTC_DATA = 8;
    protected final static byte LEN_FIRMWARE_DOWNLOAD_READY = 5;
    protected final static byte LEN_IBODY24_RESET = 5;

    //new
    protected final static byte CMD_PLAY = 0x10;
    protected final static byte CMD_SERIAL = 0x05;
    protected final static byte REQ_SERIAL = 0x01;
    protected final static byte LEN_SERIAL = 0xf;
    protected final static byte LEN_HD = 0x3;
    protected final static byte REQ_PLAY = 0x01;
    protected final static byte REQ_STOP = 0x02;
    protected final static byte CMD_PACKET = 'P';
    protected final static byte LEN_PACKET_TOTAL = 20;

    private Handler connectionHandler;
    private Runnable runnableConnection = new Runnable() {
        @Override
        public void run() {
            if (mCb != null)
                mCb.connectionState(CONNECTION_COMPLETE);
            STATE_CONNECTION = CONNECTION_COMPLETE;
        }
    };

    private Handler packetHandler1, packetHandler2;
    private Runnable runnablePacket1 = new Runnable() {
        @Override
        public void run() {
            if(mNordicCb != null)
                mNordicCb.onSensor(Packet1.packet2());
        }
    };
    private Runnable runnablePacket2 = new Runnable() {
        @Override
        public void run() {
            if(mNordicCb != null)
                mNordicCb.onSensorD(Packet2.packet2());
        }
    };
    private static class Packet1 {
        static byte cmd;
        static short seq; // 번호를 양수로 표시하기 위해..

        static short acc_x0;
        static short acc_y0;
        static short acc_z0;
        static short press0;

        static short acc_x1;
        static short acc_y1;
        static short acc_z1;
        static short press1;

        static byte hr0;
        static byte hr1;

        /** 배열의 0,1,2 = 가속도 x,y,z **/
        /** 배열의 3,4,5 = 자이로 x,y,z **/
        /** 배열의 6 = 기압 **/
        /** 배열의 7 = 심박 **/
        static float[] packet1() {
            float[] packet = new float[8];
            packet[0] = ((float)acc_x0) / 100;
            packet[1] = ((float)acc_y0) / 100;
            packet[2] = ((float)acc_z0) / 100;
            packet[3] = 0;
            packet[4] = 0;
            packet[5] = 0;
            packet[6] = ((float) press0) / 100;
            packet[7] = ((float) (hr0&0xff));

            return packet;
        }
        static float[] packet2() {
            float[] packet = new float[8];
            packet[0] = ((float)acc_x1) / 100;
            packet[1] = ((float)acc_y1) / 100;
            packet[2] = ((float)acc_z1) / 100;
            packet[3] = 0;
            packet[4] = 0;
            packet[5] = 0;
            packet[6] = ((float) press1) / 100;
            packet[7] = ((float) (hr1&0xff));

            return packet;
        }
        static void init() {
            acc_x0 = acc_y0 = acc_z0 = press0 = hr0 = 0;
            acc_x1 = acc_y1 = acc_z1 = press1 = hr1 = 0;
        }
    }
    private static class Packet2 {
        static byte cmd;
        static short seq; // 번호를 양수로 표시하기 위해..

        static short acc_x0;
        static short acc_y0;
        static short acc_z0;
        static short press0;

        static short acc_x1;
        static short acc_y1;
        static short acc_z1;
        static short press1;

        static byte hr0;
        static byte hr1;

        /** 배열의 0,1,2 = 가속도 x,y,z **/
        /** 배열의 3,4,5 = 자이로 x,y,z **/
        /** 배열의 6 = 기압 **/
        /** 배열의 7 = 심박 **/
        static float[] packet1() {
            float[] packet = new float[8];
            packet[0] = ((float)acc_x0) / 100;
            packet[1] = ((float)acc_y0) / 100;
            packet[2] = ((float)acc_z0) / 100;
            packet[3] = 0;
            packet[4] = 0;
            packet[5] = 0;
            packet[6] = ((float) press0) / 100;
            packet[7] = ((float) (hr0&0xff));

            return packet;
        }
        static float[] packet2() {
            float[] packet = new float[8];
            packet[0] = ((float)acc_x1) / 100;
            packet[1] = ((float)acc_y1) / 100;
            packet[2] = ((float)acc_z1) / 100;
            packet[3] = 0;
            packet[4] = 0;
            packet[5] = 0;
            packet[6] = ((float) press1) / 100;
            packet[7] = ((float) (hr1&0xff));

            return packet;
        }
        static void init() {
            acc_x0 = acc_y0 = acc_z0 = press0 = hr0 = 0;
            acc_x1 = acc_y1 = acc_z1 = press1 = hr1 = 0;
        }
    }


    private String mBluetoothDeviceAddress, mBluetoothDeviceName;

    // BLE 관련
    private boolean mScanning = false;
    private static final long SCAN_PERIOD = 10000;

    private BluetoothGatt mBluetoothGatt1 = null;
    private BluetoothGatt mBluetoothGatt2 = null;

    private static final BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();

    private static Context mContext;
    private ArrayList<DeviceInformation> arList;

    private static BluetoothManager mInstance = null;
    private static IBluetooth mCb;

    private static INordicFormat mNordicCb;

    public static void registCallback(IBluetooth cb) {
        mCb = cb;
    }

    public static void unregistCallback() {
        mCb = null;
    }

    protected static void registDataCallback(INordicFormat cb) {
        Log.d("jeyang","Coach Bluetooth registDataCallback : "+cb);
        mNordicCb = cb;
    }

    protected static void unregistDataCallback() {
        Log.d("jeyang","Coach Bluetooth unregistDataCallback");
        mNordicCb = null;
    }

    protected static void initInstance() {
        if (mInstance == null)
            mInstance = new BluetoothManager();
    }

    public static BluetoothManager getInstance(Context context) {
        //initInstance();
        setContext(context);
        return mInstance;
    }

    private static void setContext(Context context) {
        mContext = context;
    }

    private BluetoothManager() {
        arList = new ArrayList<DeviceInformation>();
        packetHandler1 = new Handler();
        packetHandler2 = new Handler();
        connectionHandler = new Handler();
    }

    private byte[] requestPlay(byte play) {
        byte[] frame = new byte[LEN_HD];
        frame[0] = CMD_PLAY;
        frame[1] = play;
        frame[2] = 0x00;
        // test
        if(play == REQ_PLAY)
            count = 0;

        return frame;
    }

    private byte[] sendSerial(String serial) {
        byte[] frame = new byte[LEN_HD+LEN_SERIAL];
        frame[0] = CMD_SERIAL;
        frame[1] = REQ_SERIAL;
        frame[2] = LEN_SERIAL;

        System.arraycopy(serial.getBytes(), 0, frame, 3, LEN_SERIAL);

        return frame;
    }

    public void play() {
        if(mBluetoothGatt1 == null)
            return;

        byte[] frame = requestPlay(REQ_PLAY);

        BluetoothGattCharacteristic mChar = mBluetoothGatt1.getService(Attribute.RX_SERVICE_UUID).getCharacteristic(Attribute.RX_CHAR_UUID);
        mChar.setValue(frame);
        mBluetoothGatt1.writeCharacteristic(mChar);

        if(mBluetoothGatt2 == null)
            return;

        BluetoothGattCharacteristic mChar2 = mBluetoothGatt2.getService(Attribute.RX_SERVICE_UUID).getCharacteristic(Attribute.RX_CHAR_UUID);
        mChar2.setValue(frame);
        mBluetoothGatt2.writeCharacteristic(mChar2);
    }

    public void stop() {
        packetHandler1.removeCallbacks(runnablePacket1);
        if(mBluetoothGatt1 == null)
            return;

        byte[] frame = requestPlay(REQ_STOP);

        BluetoothGattCharacteristic mChar = mBluetoothGatt1.getService(Attribute.RX_SERVICE_UUID).getCharacteristic(Attribute.RX_CHAR_UUID);
        mChar.setValue(frame);
        mBluetoothGatt1.writeCharacteristic(mChar);

        if(mBluetoothGatt2 == null)
            return;

        BluetoothGattCharacteristic mChar2 = mBluetoothGatt2.getService(Attribute.RX_SERVICE_UUID).getCharacteristic(Attribute.RX_CHAR_UUID);
        mChar2.setValue(frame);
        mBluetoothGatt2.writeCharacteristic(mChar2);
    }

    /**
     * 이건 어찌할지...2인용은???
     * @param serial
     */
    private void serial(String serial) {
        if(mBluetoothGatt1 == null)
            return;

        byte[] frame = sendSerial(serial);

        BluetoothGattCharacteristic mChar = mBluetoothGatt1.getService(Attribute.RX_SERVICE_UUID).getCharacteristic(Attribute.RX_CHAR_UUID);
        mChar.setValue(frame);
        mBluetoothGatt1.writeCharacteristic(mChar);
    }


    private float[] acc_gyro(byte[] data) {
        short[] tmpShort = new short[6];

        short accX = (short) (((data[0]& 0xff) << 8)  | (data[1]& 0xff));
        short accY = (short) (((data[2]& 0xff) << 8)  | (data[3]& 0xff));
        short accZ = (short) (((data[4]& 0xff) << 8)  | (data[5]& 0xff));
        short gyroX = (short) (((data[6]& 0xff) << 8)  | (data[7]& 0xff));
        short gyroY = (short) (((data[8]& 0xff) << 8)  | (data[9]& 0xff));
        short gyroZ = (short) (((data[10]& 0xff) << 8)  | (data[11]& 0xff));


        tmpShort[0] = accX;
        tmpShort[1] = accY;
        tmpShort[2] = accZ;
        tmpShort[3] = gyroX;
        tmpShort[4] = gyroY;
        tmpShort[5] = gyroZ;

        float[] tmpFloat = new float[6];
        tmpFloat[0] = ((float)tmpShort[0])/128;
        tmpFloat[1] = ((float)tmpShort[1])/128;
        tmpFloat[2] = ((float)tmpShort[2])/128;
        tmpFloat[3] = ((float)tmpShort[3])/10;
        tmpFloat[4] = ((float)tmpShort[4])/10;
        tmpFloat[5] = ((float)tmpShort[5])/10;

        return tmpFloat;
    }

    private float[] acc_gyro_press_pulse(byte[] data) {
        short[] tmpShort = new short[9];
        short accX = (short) (((data[0] & 0xff) << 8) | (data[1] & 0xff));
        short accY = (short) (((data[2] & 0xff) << 8) | (data[3] & 0xff));
        short accZ = (short) (((data[4] & 0xff) << 8) | (data[5] & 0xff));
        short gyroX = (short) (((data[6] & 0xff) << 8) | (data[7] & 0xff));
        short gyroY = (short) (((data[8] & 0xff) << 8) | (data[9] & 0xff));
        short gyroZ = (short) (((data[10] & 0xff) << 8) | (data[11] & 0xff));
        short pressureMSB = (short) (((data[12] & 0xff) << 8) | (data[13] & 0xff));
        short pressureLSB = (short) (((data[14] & 0xff) << 8) | (data[15] & 0xff));
        short PulseRate = (short) (data[16] & 0xff);

        tmpShort[0] = accX;
        tmpShort[1] = accY;
        tmpShort[2] = accZ;
        tmpShort[3] = gyroX;
        tmpShort[4] = gyroY;
        tmpShort[5] = gyroZ;
        tmpShort[6] = pressureMSB;
        tmpShort[7] = pressureLSB;
        tmpShort[8] = PulseRate;

        int pressure = ((tmpShort[6] & 0xffff) << 16) | (tmpShort[7] & 0xffff);

        float[] tmpFloat = new float[8];
        tmpFloat[0] = ((float) tmpShort[0]) / 100;
        tmpFloat[1] = ((float) tmpShort[1]) / 100;
        tmpFloat[2] = ((float) tmpShort[2]) / 100;
        tmpFloat[3] = ((float) tmpShort[3]) / 1000;
        tmpFloat[4] = ((float) tmpShort[4]) / 1000;
        tmpFloat[5] = ((float) tmpShort[5]) / 1000;
        tmpFloat[6] = ((float) pressure) / 100;
        tmpFloat[7] = ((float) tmpShort[8]);

        return tmpFloat;
    }

    public String getRemoteMac() {
        if(mBluetoothGatt1 == null)
            return null;
        return mBluetoothGatt1.getDevice().getAddress();
    }
    public String getRemoteMac(int remoteCount) {
        if(remoteCount < REMOTE_COUNT_1 && remoteCount > REMOTE_COUNT_2)
            return null;
        if(remoteCount == REMOTE_COUNT_1 && mBluetoothGatt1 == null)
            return null;
        if(remoteCount == REMOTE_COUNT_2 && mBluetoothGatt2 == null)
            return null;
        return remoteCount == REMOTE_COUNT_2 ? mBluetoothGatt2.getDevice().getAddress() : mBluetoothGatt1.getDevice().getAddress();
    }

    // boradcom
    protected final static byte LEN_BLUETOOTHLE_FRAME = 20;
    protected final static byte STX = (byte) 0xc8;
    protected final static byte ETX = (byte) 0xc9;
    protected static boolean setSTX = false;
    protected static boolean setETX = false;

    private byte[] frameBuffer = new byte[LEN_BLUETOOTHLE_FRAME * 10];
    private int LEN_CURRENT_FRAME = 0;
    private int OFFSET_RAW_BROADCOM_LEN = 1;
    private int OFFSET_FEATURE_BROADCOM_LEN = 3;
    private Timer mTimeout;

    //

    /**
     * GATT 객체에서 Service, characteristic UUID 정보를 리스트에 추가.
     *
     * @param gatt
     *            gatt 객체.
     */
    private void getServiceFromGATT(BluetoothGatt gatt) {
        List<BluetoothGattService> gattServices = gatt.getServices();

        Log.i(tag, "getServiceFromGATT!!!");

        // 서비스 밑에 캐릭터가 달려 있으므로, 리스트뷰로 작성하는게 맞음. -> 걍 어레이로...
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                final int charaProp = gattCharacteristic.getProperties();

                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    gatt.setCharacteristicNotification(gattCharacteristic, true);
                } else {
                    gatt.setCharacteristicNotification(gattCharacteristic, false);
                }
                Log.i(tag, gattCharacteristic.getUuid().toString());
            }
        }


        BluetoothGattService service = gatt.getService(Attribute.RX_SERVICE_UUID);
        if (service != null) {
            BluetoothGattCharacteristic mChar = service.getCharacteristic(Attribute.TX_CHAR_UUID);
            BluetoothGattDescriptor descriptor = mChar.getDescriptor(Attribute.CCCD);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
        }
		/*
		BluetoothGattService service = gatt.getService(Attribute.UUID_UART_SERVICE);
		if(service != null) {
			BluetoothGattCharacteristic mChar = service.getCharacteristic(Attribute.UUID_UART_REGISTER);
			gatt.readCharacteristic(mChar);
		}
		*/
    }


    public int count = 0; //test
    /**
     * GATT 콜백. 연결에 대한 이벤트를 받을수 있다. (연결 상태, 읽기, 쓰기, 상태 변화)
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(tag, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(tag, "Attempting to start service discovery:" + gatt.discoverServices());

                countGattClient++;
                // progress_dismiss();
                if (mCb != null)
                    mCb.connectionState(CONNECT);
                STATE_CONNECTION = CONNECT;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(tag, "Disconnected from GATT server.");
                packetHandler1.removeCallbacks(runnablePacket1);
                packetHandler2.removeCallbacks(runnablePacket2);
                connectionHandler.removeCallbacks(runnableConnection);

                gatt.close();
                gatt = null;

                if(mBluetoothGatt1 == null)
                    Log.d(tag,"mBluetoothGatt1 null");
                if(mBluetoothGatt2 == null)
                    Log.d(tag,"mBluetoothGatt2 null");

                countGattClient--;

                Packet1.init();
                Packet2.init();

                if (mCb != null)
                    mCb.connectionState(DISCONNECT);
                STATE_CONNECTION = DISCONNECT;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                getServiceFromGATT(gatt);

                connectionHandler.postDelayed(runnableConnection, 2*1000);
            } else {
                Log.w(tag, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(tag, "GATT_SUCCESS READ status: " + status);
            } else {
                Log.i(tag, "GATT_FAIL READ status: " + status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i(tag, "onCharacteristicWrite : status->" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            UUID tmp = characteristic.getUuid();
            if (tmp.equals(Attribute.TX_CHAR_UUID)) {
                byte[] getFrameBuffer = characteristic.getValue();

                byte len = getFrameBuffer[1];


                // 100ms 주기 데이터 적산.
                if(getFrameBuffer[0] == CMD_PACKET) {
                    count++; // test
                    //Log.d("jeyang","***   Count : "+count+"   ***"); // test
                    int remoteCount = 0;
                    if(gatt.equals(mBluetoothGatt1)) {
                        COUNT_ANT_DATA_1++;
                        remoteCount = REMOTE_COUNT_1;
                    } else {
                        COUNT_ANT_DATA_2++;
                        remoteCount = REMOTE_COUNT_2;
                    }

                    if(remoteCount == REMOTE_COUNT_1) {
                        Packet1.cmd = getFrameBuffer[0];
                        Packet1.seq = (short) (getFrameBuffer[1]&0xff);
                        Packet1.acc_x0 = (short) ((getFrameBuffer[2]&0xff) | ((getFrameBuffer[3] &0xff) <<8));
                        Packet1.acc_y0 = (short) ((getFrameBuffer[4]&0xff) | ((getFrameBuffer[5] &0xff)<<8));
                        Packet1.acc_z0 = (short) ((getFrameBuffer[6]&0xff) | ((getFrameBuffer[7] &0xff)<< 8));
                        Packet1.press0 = (short) ((getFrameBuffer[8]&0xff) | ((getFrameBuffer[9] &0xff)<< 8));
                        Packet1.acc_x1 = (short) ((getFrameBuffer[10]&0xff) | ((getFrameBuffer[11] &0xff)<< 8));
                        Packet1.acc_y1 = (short) ((getFrameBuffer[12]&0xff) | ((getFrameBuffer[13] &0xff)<< 8));
                        Packet1.acc_z1 = (short) ((getFrameBuffer[14]&0xff) | ((getFrameBuffer[15] &0xff)<< 8));
                        Packet1.press1 = (short) ((getFrameBuffer[16]&0xff) | ((getFrameBuffer[17] &0xff)<< 8));
                        Packet1.hr0 = getFrameBuffer[18];
                        Packet1.hr1 = getFrameBuffer[19];
                    } else {
                        Packet2.cmd = getFrameBuffer[0];
                        Packet2.seq = (short) (getFrameBuffer[1]&0xff);
                        Packet2.acc_x0 = (short) ((getFrameBuffer[2]&0xff) | ((getFrameBuffer[3] &0xff) <<8));
                        Packet2.acc_y0 = (short) ((getFrameBuffer[4]&0xff) | ((getFrameBuffer[5] &0xff)<<8));
                        Packet2.acc_z0 = (short) ((getFrameBuffer[6]&0xff) | ((getFrameBuffer[7] &0xff)<< 8));
                        Packet2.press0 = (short) ((getFrameBuffer[8]&0xff) | ((getFrameBuffer[9] &0xff)<< 8));
                        Packet2.acc_x1 = (short) ((getFrameBuffer[10]&0xff) | ((getFrameBuffer[11] &0xff)<< 8));
                        Packet2.acc_y1 = (short) ((getFrameBuffer[12]&0xff) | ((getFrameBuffer[13] &0xff)<< 8));
                        Packet2.acc_z1 = (short) ((getFrameBuffer[14]&0xff) | ((getFrameBuffer[15] &0xff)<< 8));
                        Packet2.press1 = (short) ((getFrameBuffer[16]&0xff) | ((getFrameBuffer[17] &0xff)<< 8));
                        Packet2.hr0 = getFrameBuffer[18];
                        Packet2.hr1 = getFrameBuffer[19];
                    }



                    /** get data!!! **/
                    if(mNordicCb != null) {
                        if(remoteCount == REMOTE_COUNT_1) {
                            mNordicCb.onSensor(Packet1.packet1());
                            //Log.d("jeyang","1p packet1->seq:"+Packet1.seq);
                            packetHandler1.postDelayed(runnablePacket1, 50);
                        } else {
                            //mNordicCb.onSensorD(Packet2.packet1());
                            //Log.d("jeyang","2p packet2->seq:"+Packet2.seq);
                            //packetHandler2.postDelayed(runnablePacket2, 50);
                        }
                    }
                }


				/*
				// 50ms
				if (len == 17) {
					count++; // test
					Log.d("jeyang","***   Count : "+count+"   ***"); // test
					int remoteCount = 0;
					if(gatt.equals(mBluetoothGatt1)) {
						COUNT_ANT_DATA_1++;
						remoteCount = REMOTE_COUNT_1;
					} else {
						COUNT_ANT_DATA_2++;
						remoteCount = REMOTE_COUNT_2;
					}
					// 정상 주기로 오는 데이터인 경우
					byte[] tmpval = new byte[getFrameBuffer[1]];
					System.arraycopy(getFrameBuffer, 2, tmpval, 0, getFrameBuffer[1]);

					*//** get data!!! **//*
					if(mNordicCb != null) {
						if(remoteCount == REMOTE_COUNT_1) {
							mNordicCb.onSensor(acc_gyro_press_pulse(tmpval));
						} else {
							mNordicCb.onSensorD(acc_gyro_press_pulse(tmpval));
						}
					}
				}
				*/
            }

			/*if (tmp.equals(Attribute.UUID_UART_NOTIFY)) {
				byte[] getFrameBuffer = characteristic.getValue();
				byte[] getFrame=null;


				for(int i=0; i<getFrameBuffer.length; i++) {
					byte buf = getFrameBuffer[i];
					if(setSTX != true) {
						if(buf == STX) {
							//Log.d(tag, "setSTX");
							setSTX = true;
							frameBuffer[LEN_CURRENT_FRAME] = buf;
							LEN_CURRENT_FRAME++;
							mTimeout = new Timer("Timeout");
							mTimeout.schedule(new TimerTask() {
								@Override
								public void run() {
									// 300ms 지나도 etx가 안오고 stx만 set 되어있으면, 리셋.
									if(setSTX == true && setETX == false) {
										for(int j=0; j<LEN_CURRENT_FRAME; j++) {
											frameBuffer[j] = 0;
										}
										LEN_CURRENT_FRAME=0;
										setSTX = false;
										setETX = false;
										Log.e(tag, "Timeout... 300ms");
									}
								}
							}, 100*3);
						}
					} else {
						//Log.d(tag,String.format("setSTX->getData : 0x%02x", buf));
						int offsetData = LEN_CURRENT_FRAME;
						frameBuffer[LEN_CURRENT_FRAME] = buf;
						LEN_CURRENT_FRAME++;
						if (frameBuffer[offsetData] == ETX) {
							if (frameBuffer[1] == 12) {
								int len = frameBuffer[OFFSET_RAW_BROADCOM_LEN];
								//Log.d(tag, "setSTX->getData->getETX ::: framelen:"+len+" LEN_CURR:"+LEN_CURRENT_FRAME);
								if (len + 3 == LEN_CURRENT_FRAME) {
									// ETX 확정
									setETX = true;
									// setSTX = false;
									getFrame = new byte[LEN_CURRENT_FRAME - 2];
									System.arraycopy(frameBuffer, 1, getFrame, 0, LEN_CURRENT_FRAME - 2);

									for (int j = 0; j < LEN_CURRENT_FRAME; j++) {
										frameBuffer[j] = 0;
									}
									LEN_CURRENT_FRAME = 0;
									// break;
								}
							} else {
								int len = frameBuffer[OFFSET_FEATURE_BROADCOM_LEN];
								//Log.d(tag, "setSTX->getData->getETX ::: framelen:"+len+" LEN_CURR:"+LEN_CURRENT_FRAME);
								if( len+6 ==LEN_CURRENT_FRAME ) {
									// ETX 확정
									setETX = true;
									//setSTX = false;
									getFrame = new byte[LEN_CURRENT_FRAME-2];
									System.arraycopy(frameBuffer, 1, getFrame, 0, LEN_CURRENT_FRAME-2);

									for(int j=0; j<LEN_CURRENT_FRAME; j++) {
										frameBuffer[j] = 0;
									}
									LEN_CURRENT_FRAME = 0;
									//break;
								}
							}
						}
					}
				}

				if(setSTX != true)
					return;

				if(setETX != true)
					return;

				if(mTimeout != null) {
					mTimeout.cancel();
					mTimeout = null;
				}

				setSTX = false;
				setETX = false;



				byte cmd = getFrame[0];

				BluetoothGattCharacteristic mChar =
						gatt.getService(Attribute.UUID_UART_SERVICE).getCharacteristic(Attribute.UUID_UART_WRITE);

				if (cmd == 12) {

					// byte[] tmpval = characteristic.getValue();
					//byte[] tmpval = new byte[10];
					byte[] tmpval = new byte[12];
					System.arraycopy(getFrame, 1, tmpval, 0, getFrame[0]);
					//System.arraycopy(getFrame, 1, tmpval, 0, 12);

					if(mNordicCb != null) {
						mNordicCb.onSensor(acc_gyro(tmpval));
					}

				} else if(cmd == CMD_REQ_USER_DATA) {
					byte[] user = requestUserData();
					mChar.setValue(user);
					gatt.writeCharacteristic(mChar);
					Log.i(tag, "write USER DATA");
				} else if(cmd == CMD_REQ_RTC_DATA) {
					byte[] rtc = requestRTC();
					mChar.setValue(rtc);
					gatt.writeCharacteristic(mChar);
					Log.i(tag, "write RTC DATA");
				} else if(cmd == CMD_REQ_BATTERY) {
					Log.i(tag, "set Battery!!!");
					//EngineConfiguration.setBattery(getBattery(getFrame));
				} else if(cmd == CMD_REQ_GET_CONNECTION_STATE) {
					byte[] state = requestConnectionState();
					mChar.setValue(state);
					gatt.writeCharacteristic(mChar);
					Log.i(tag, "write Connection DATA");
				}
			}*/
        }
    };

	/*
	public static boolean incomingAnyData(int remoteCount) {
		Log.d(tag,"PRECOUNT_ANT_DATA_1:"+PRECOUNT_ANT_DATA_1+" COUNT_ANT_DATA_1:"+COUNT_ANT_DATA_1+
				" PRECOUNT_ANT_DATA_2:"+PRECOUNT_ANT_DATA_2+" COUNT_ANT_DATA_2:"+COUNT_ANT_DATA_2);
		if(remoteCount == REMOTE_COUNT_1) {
			if (PRECOUNT_ANT_DATA_1 != COUNT_ANT_DATA_1) {
				PRECOUNT_ANT_DATA_1 = COUNT_ANT_DATA_1;
				//Log.d(tag,"COUNT 111");
				return true;
			} else
				return false;
		} else {
			if (PRECOUNT_ANT_DATA_2 != COUNT_ANT_DATA_2) {
				PRECOUNT_ANT_DATA_2 = COUNT_ANT_DATA_2;
				//Log.d(tag,"COUNT 222");
				return true;
			} else
				return false;
		}
	}
	*/

    public String[] getDeviceInformation() {
        if(mBluetoothGatt1 == null)
            return null;

        String[] ret = new String[]{mBluetoothDeviceName, mBluetoothDeviceAddress};

        return ret;
    }

    /**
     * BLE device 스캔 시작, 중지.
     *
     * @param enable
     *            true: 시작, false: 중지.
     */
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBTAdapter.stopLeScan(mLeScanCallback);
                    // setProgressBarIndeterminateVisibility(false);
                    // mBtnScan.setEnabled(true);
                }
            }, SCAN_PERIOD);

            arList.clear();
            mScanning = true;
            mBTAdapter.startLeScan(mLeScanCallback);
            // adapterList.notifyDataSetChanged();
        } else {
            mScanning = false;
            mBTAdapter.stopLeScan(mLeScanCallback);
            // setProgressBarIndeterminateVisibility(false);
            // mBtnScan.setEnabled(true);
        }
    }

    /**
     * BLE scan 콜백. 스캔된 device 정보를 리스트에 추가한다.
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            String name = device.getName();
            //아직 블루투스 이름이 바뀌지 않았으므로 넣지 않는다. 주석처리.
            if(name == null || name.length() < COACH_NAME.length())
                return;

            if(!name.substring(0, COACH_NAME.length()).equals(COACH_NAME))
                return;
            name = name.replace(COACH_NAME, COACH_REPLACE_NAME);

            DeviceInformation tmpInfo = new DeviceInformation();
            tmpInfo.setName(name);
            tmpInfo.setMac(device.getAddress());
            tmpInfo.setDevice(device);

            boolean newDevice = false;
            for (DeviceInformation s : arList) {
                if (s.getMac().equals(tmpInfo.getMac())) {
                    newDevice = false;
                    break;
                }
                newDevice = true;
            }
            if(arList.size() == 0)
                newDevice = true;

            if (newDevice) {
                arList.add(tmpInfo);
                if(mCb != null)
                    mCb.changedScanList(arList);
            }

        }
    };

    /**
     * BLE connect. connect 정보를 멤버 변수로 저장한다. GATT 객체, MAC 어드레스.
     *
     * @param address
     *            연결하려는 MAC 주소.
     * @return true: 성공, false: 실패.
     */
    public boolean connect(final String address) {
        if (mBTAdapter == null || address == null) {
            Log.w(tag, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

		/*
		if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
			Log.d(tag, "Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				return true;
			} else {
				return false;
			}
		}
		*/

        final BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(tag, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
        if(countGattClient == REMOTE_COUNT_NOT)
            mBluetoothGatt1 = device.connectGatt(mContext, true, mGattCallback);
        else if(countGattClient == REMOTE_COUNT_1)
            mBluetoothGatt2 = device.connectGatt(mContext, true, mGattCallback);
        Log.d(tag, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mBluetoothDeviceName = device.getName();

        return true;
    }

    public void disconnect() {
        Log.d(tag,"disconnect Manager!!!");
        VideoManager.log(tag,"disconnect Manager!!!");
        if (mBluetoothGatt1 == null) {
            return;
        }
        packetHandler1.removeCallbacks(runnablePacket1);
        packetHandler2.removeCallbacks(runnablePacket2);
        mBluetoothGatt1.disconnect();
        if(mBluetoothGatt2 != null)
            mBluetoothGatt2.disconnect();

        mBluetoothGatt1 = null;
        mBluetoothDeviceName = mBluetoothDeviceAddress = null;
		/*
		 * if(mBluetoothGatt != null) mBluetoothGatt.close();
		 */
        //mBluetoothGatt = null;
        // isConnection = false;
    }

    /**
     * 스마트폰에서 블루투스가 이용가능한지 확인한다.
     * @return true:이용가능, false:이용불가.
     */
    public static boolean isEnabled() {
        if ( !isSupported() && !mBTAdapter.isEnabled() ) {
            return false;
        } else {
            return true;
        }
    }
    private static boolean isSupported() {
        if (mBTAdapter == null) {
            return false;
        } else
            return true;
    }

    public static int getCountGattClient() {
        return countGattClient;
    }

    public static boolean isConnectionCompleted() {
        return STATE_CONNECTION == CONNECTION_COMPLETE ? true : false;
    }

    protected void startVibrate() {
        byte[] frame = new byte[LEN_VIBRATE];
        frame[0] = CMD_VIBRATE;
        frame[1] = TYPE_START_VIBRATE;

        sendMessage(frame);
    }
    protected void stopVibrate() {
        byte[] frame = new byte[LEN_VIBRATE];
        frame[0] = CMD_VIBRATE;
        frame[1] = TYPE_STOP_VIBRATE;

        sendMessage(frame);
    }

    private boolean sendMessage(byte[] message) {
        if(mBluetoothGatt1 == null)
            return false;

        BluetoothGattCharacteristic mChar =
                mBluetoothGatt1.getService(Attribute.RX_SERVICE_UUID).getCharacteristic(Attribute.RX_CHAR_UUID);

        mChar.setValue(message);
        mBluetoothGatt1.writeCharacteristic(mChar);
        return true;
    }
}
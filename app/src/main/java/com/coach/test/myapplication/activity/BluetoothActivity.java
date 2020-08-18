package com.coach.test.myapplication.activity;

import java.util.ArrayList;

import com.coach.test.myapplication.R;
import com.coach.test.myapplication.adapter.DeviceInformAdapter;
import com.coach.test.myapplication.manager.SoundPoolManager;
import com.coach.test.myapplication.util.MwApiWrapper;
import com.coach.test.coachmw.datastructure.DeviceInformation;
import com.coach.test.coachmw.manager.BluetoothManager;
import com.coach.test.coachmw.protocol.IBluetooth;
//import com.coach.test.coachmw.manager.vmRAWtest;

//import com.coach.test.myapplication.activity.vmRAWtest;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-02-17.
 */


/**
 * 블루투스 접속 Activity. 검색된 List를 표시하고 접속을 관리한다.
 * @author user
 *
 */
public class BluetoothActivity extends WActivity {
    private static final String tag = "BluetoothActivity";

    /** Variable **/
    private static final int REQ_ENABLE = 1;
    private boolean isConnection = false;
    private boolean isScan = false;

    /** View **/
    private Button mBtnScan;
    private Button mBtnTest;
    private TextView mTextLogingState;
    private ListView mDeviceList;
    private CheckBox mCheckBoxDualMode;

    /** Adapter **/
    private DeviceInformAdapter adapterList;

    /** Array **/
    private ArrayList<DeviceInformation> arList;

    /** Interface **/
    // 미들웨어로 변환.
    //private BluetoothManager mBManager;
    private IBluetooth mIBluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** 타이틀 바에 progress 표시 **/
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_bluetooth);

        /** 블루투스 활성화 Intent **/
        Intent enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQ_ENABLE);
        if(!BluetoothManager.isEnabled())
            finish();

        /** View 초기화 **/
        mBtnScan = (Button) findViewById(R.id.btnScan);
        mBtnTest = (Button) findViewById(R.id.btnTest);
        ListView mDevList = (ListView) findViewById(R.id.btListView);
        mCheckBoxDualMode = (CheckBox) findViewById(R.id.checkBoxDualMode);

        /** Array 초기화 **/
        arList = new ArrayList<DeviceInformation>();

        /** Adapter 초기화 **/
        adapterList = new DeviceInformAdapter(this, R.layout.listview_device, arList);
        mDevList.setAdapter(adapterList);

        /** Listener 등록 **/
        mBtnScan.setOnClickListener(mBtnClick);
        mBtnTest.setOnClickListener(mBtnClick2);
        mDevList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (MwApiWrapper.WrapperBluetoothManager.connect(arList.get(position).getMac())) { // 연결시도...
                    for(int i=0; i<parent.getChildCount() ; i++) {
                        parent.getChildAt(i).setBackgroundResource(R.drawable.text_border_gradient_green1);;
                    }
                    if(!isCheckDualMode())
                        view.setBackgroundResource(R.drawable.text_border_gradient_blue1);
                    else if(BluetoothManager.getCountGattClient() < 1){
                        view.setBackgroundResource(R.drawable.text_border_gradient_gray1);
                        view.setEnabled(false);
                    } else
                        view.setBackgroundResource(R.drawable.text_border_gradient_blue1);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            MwApiWrapper.WrapperBluetoothManager.scanLeDevice(false);
                            setProgressBarIndeterminateVisibility(false);
                            mBtnScan.setEnabled(true);
                            progress(R.string.dialog_wait_for_min);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(!isConnection) { // 모종의 이유로 10초동안 연결이 되질 않음. ListView clr->alert
                                        progress_dismiss();
                                        alert(R.string.alert_failed_connect);
                                        arList.clear();
                                        adapterList.notifyDataSetChanged();
                                        MwApiWrapper.WrapperBluetoothManager.disconnect();
                                        vib();
                                    }
                                }
                            }, 10*1000);
                        }
                    }); // runOnUiThread End
                }
            }
        }); // setOnItemClickListener End

        /** Bluetooth 객체 등록 **/
        // 미들웨어로 변환.
        //mBManager = BluetoothManager.getInstance(this);
        /** Interface 등록 **/
        mIBluetooth = new IBluetooth() {
            @Override
            public void connectionState(int state) {
                if(state == BluetoothManager.CONNECT) { // 연결됨.
                    isConnection = true;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progress_dismiss();
                            // 다음 화면으로...아직 읍엉
                            Intent intent = new Intent(BluetoothActivity.this, VideoListActivity.class);
                            if(!isCheckDualMode()) {
                                startActivity(intent);
                                finish();
                            } else {
                                if(BluetoothManager.getCountGattClient() == 2 ) {
                                    intent.putExtra(VideoPlayActivity.DUAL_MODE, true);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });
                } else {
                    isConnection = false;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            SoundPoolManager.playBeep(BluetoothActivity.this);
                            alert(R.string.alert_disconnect);
                            MwApiWrapper.WrapperBluetoothManager.disconnect();
                            vib();
                            arList.clear();
                            adapterList.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void changedScanList(ArrayList<DeviceInformation> list) {
                Log.d(tag,"changedScanList");
                if(isScan)
                    return;
                isScan = true;

                arList.clear();
                for(DeviceInformation dev : list) {
                    arList.add(dev);
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        adapterList.notifyDataSetChanged();
                    }
                }); // runOnUiThread End

                /** UI 변경에 여유를 주기 위한 Handler. UI Thread 내부가 아니므로 Handler를 생성하기 위해 Looper를 넘겨주었다. **/
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isScan = false;
                    }
                }, 100);
            }
        }; // mIBluetooth End
    } // onCreate End


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.main_actionbar, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, UserInformActivity.class)
                        .putExtra(UserInformActivity.EXTRA_NAME, UserInformActivity.SET_ACTION_BAR));
                break;
        }
        return true;
    }

    private boolean isCheckDualMode() {
        return mCheckBoxDualMode.isChecked();
    }

    /**
     * 버튼의 클릭리스너.
     */
    OnClickListener mBtnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnScan: // 검색 시작.
                    setProgressBarIndeterminateVisibility(true);
                    // 검색중이라면, 누르지 못하게 비활성화 시킴. 검색이 끝날때까지 못누름.
                    v.setEnabled(false);
                    // 검색 전, Array clr.
                    arList.clear();
                    // 검색 수행.
                    MwApiWrapper.WrapperBluetoothManager.scanLeDevice(true);
                    // Stops scanning after a pre-defined scan period.
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 검색이 끝나면 다시 버튼 활성화
                            setProgressBarIndeterminateVisibility(false);
                            mBtnScan.setEnabled(true);
                        }
                    }, 10*1000);
                    break;
            }
        }
    };

    /**
     * 버튼의 클릭리스너.
     */
    OnClickListener mBtnClick2 = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnTest: // 검색 시작.
				/*setProgressBarIndeterminateVisibility(true);
				// 검색중이라면, 누르지 못하게 비활성화 시킴. 검색이 끝날때까지 못누름.
				v.setEnabled(false);
				// 검색 전, Array clr.
				//arList.clear();
				// 검색 수행.
				//MwApiWrapper.WrapperBluetoothManager.scanLeDevice(true);
				//MwApiWrapper.WrappervmRAWtest.play();
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					@Override
					public void run() {
						vmRAWtest.play();
						setProgressBarIndeterminateVisibility(false);
						mBtnTest.setEnabled(true);
					}
				}, 100);*/
                    // Stops scanning after a pre-defined scan period.
				/*new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// 검색이 끝나면 다시 버튼 활성화

						setProgressBarIndeterminateVisibility(false);
						mBtnTest.setEnabled(true);
					}
				}, 5*1000);*/


                    isConnection = true;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progress_dismiss();
                            // 다음 화면으로...아직 읍엉
                            startActivity(new Intent(BluetoothActivity.this, VideoListActivity2.class));

                            finish();
                        }
                    });

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQ_ENABLE:
                if(resultCode != RESULT_OK)
                    finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MwApiWrapper.WrapperBluetoothManager.registCallback(mIBluetooth);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MwApiWrapper.WrapperBluetoothManager.registCallback(mIBluetooth);
    }
}

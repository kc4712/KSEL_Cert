package com.coach.test.myapplication.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.coach.test.myapplication.R;
import com.coach.test.myapplication.manager.SoundPoolManager;
import com.coach.test.myapplication.util.MwApiWrapper;
import com.coach.test.coachmw.datastructure.DeviceInformation;
//import com.coach.test.coachmw.manager.BluetoothManager;
import com.coach.test.coachmw.manager.VideoManager;
//import com.coach.test.coachmw.protocol.IBluetooth;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-02-17.
 */

/**
 * 동영상 리스트 출력 Activity. 선택된 동영상 정보를 다음 Activity로 전달.
 * @author user
 *
 */
public class VideoListActivity2 extends WActivity {
    private static final String tag = "VideoListActivity2";

    /** Variable **/
    private static boolean setSave=false;
    private static boolean setDebug=false;
    private static boolean setDebugOK=false;
    private static boolean setDebugPlay=false;
    private static final int REQUEST_FILE = 10;
    private File FILE_RAW_DATA;
    private int debugVideoID;
    private boolean isDualMode = false;


    /** Array **/
    private float[] listQ;

    /** View **/
    private EditText edtFileName, edtStartSec;
    private TextView txtRemoteMac1, txtRemoteMac2, txtRootSdPath;
    private Button play1, play2, play3, play1_hong, play2_hong, play1_lee, play2_lee;
    private Button btnSetVideo;
    private CheckBox chkBoxSave, chkBoxDebug, chkBoxDebugPlay;

    private Button mBtnSelectAccFile, mBtnSelectHrFile;
    private TextView mAccTxtPath, mHrTxtPath;

    private String accPath = null;
    private String hrPath = null;
    //test count view
    //private TextView txtTestCount;

    /** Interface **/
    //private IBluetooth mIBluetooth;
    //미들웨어화
    //private BluetoothManager mBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist2);

        /** View 초기화 **/
        play1 = (Button) findViewById(R.id.play1);
        play2 = (Button) findViewById(R.id.play2);
        play3 = (Button) findViewById(R.id.play3);
        play1_hong = (Button) findViewById(R.id.play1_hong);
        play2_hong = (Button) findViewById(R.id.play2_hong);
        play1_lee = (Button) findViewById(R.id.play1_lee);
        play2_lee = (Button) findViewById(R.id.play2_lee);
        btnSetVideo = (Button) findViewById(R.id.btnsetVideo);

        edtFileName = (EditText) findViewById(R.id.edtFileName);
        edtStartSec = (EditText) findViewById(R.id.edtStartSec);

        txtRemoteMac1 = (TextView) findViewById(R.id.remoteMac1);
        txtRemoteMac2 = (TextView) findViewById(R.id.remoteMac2);
        txtRootSdPath = (TextView) findViewById(R.id.rootSdPath);
        //test view
        //txtTestCount = (TextView) findViewById(R.id.testCount);

        chkBoxSave = (CheckBox) findViewById(R.id.chkBoxSave);
        chkBoxDebug = (CheckBox) findViewById(R.id.chkBoxDebug);
        chkBoxDebugPlay = (CheckBox) findViewById(R.id.chkBoxDebugPlay);

        mBtnSelectAccFile = (Button) findViewById(R.id.selectAccFile);
        mBtnSelectHrFile = (Button) findViewById(R.id.selectHrFile);
        mAccTxtPath = (TextView) findViewById(R.id.AccTxtPath);
        mHrTxtPath = (TextView) findViewById(R.id.HrTxtPath);


        /** Listener 등록 **/
        mBtnSelectAccFile.setOnClickListener(mRawClick);
        mBtnSelectHrFile.setOnClickListener(mRawClick);


        play1.setOnClickListener(mPlayClick);
        play2.setOnClickListener(mPlayClick);
        play3.setOnClickListener(mPlayClick);
        play1_hong.setOnClickListener(mPlayClick);
        play2_hong.setOnClickListener(mPlayClick);
        play1_lee.setOnClickListener(mPlayClick);
        play2_lee.setOnClickListener(mPlayClick);
        btnSetVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu mPopup = new PopupMenu(VideoListActivity2.this, v);
                getMenuInflater().inflate(R.menu.popup_setvideo, mPopup.getMenu());
                mPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final String setString;
                        switch(item.getItemId()) {
                            case R.id.video1_1:
                                debugVideoID = R.id.video1_1;
                                setString = "V1_1";
                                break;
                            case R.id.video1_8:
                                debugVideoID = R.id.video1_8;
                                setString = "V1_8";
                                break;
                            case R.id.video1_3:
                                debugVideoID = R.id.video1_3;
                                setString = "V1_3";
                                break;
                            case R.id.video1_6:
                                debugVideoID = R.id.video1_6;
                                setString = "V1_6";
                                break;
                            case R.id.video1_16:
                                debugVideoID = R.id.video1_16;
                                setString = "V1_16";
                                break;
                            case R.id.video2_1:
                                debugVideoID = R.id.video2_1;
                                setString = "V2_1";
                                break;
                            case R.id.video2_2:
                                debugVideoID = R.id.video2_2;
                                setString = "V2_2";
                                break;
                            case R.id.video2_3:
                                debugVideoID = R.id.video2_3;
                                setString = "V2_3";
                                break;
                            case R.id.video2_4:
                                debugVideoID = R.id.video2_4;
                                setString = "V2_4";
                                break;
                            case R.id.video2_6:
                                debugVideoID = R.id.video2_6;
                                setString = "V2_6";
                                break;
                            default:
                                setString = "V1_1";
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                btnSetVideo.setText(setString);
                            }
                        });

                        return true;
                    }
                });
                mPopup.show();
            }
        });
        chkBoxSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setSave = true;
                } else {
                    setSave = false;
                }
                //Log.d(tag,"onCheckedChanged:"+setSave+" isChecked:"+isChecked);
            }
        });
        chkBoxDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setDebug = true;
                    selectFile();
                } else {
                    setDebug = false;
                    setDebugOK = false;
                    if(listQ == null)
                        return;
                    for(int i=0; i<listQ.length; i++){
                        listQ[i] = 0;
                    }
                }
            }
        });
        chkBoxDebugPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setDebugPlay = true;
                } else {
                    setDebugPlay = false;
                }
            }
        });

        /** Bluetooth 객체 등록 **/
        //미들웨어화
        //mBManager = BluetoothManager.getInstance(this);
        /** Interface 등록 **/
		/*mIBluetooth = new IBluetooth() {
			@Override
			public void connectionState(int state) {
				if(state == BluetoothManager.DISCONNECT) {
					runOnUiThread(new Runnable() {
						public void run() {
							SoundPoolManager.playBeep(VideoListActivity2.this);
							alert(R.string.alert_disconnect, BluetoothActivity.class);
							MwApiWrapper.WrapperBluetoothManager.disconnect();
							vib();
						}
					}); // runOnUiThread End
				}
			}

			@Override
			public void changedScanList(ArrayList<DeviceInformation> list) {
			}
		}; // mIBluetooth End
		*/

        /** 변수의 초기 값 설정 **/
        debugVideoID = R.id.video1_1;
        isDualMode = getIntent().getBooleanExtra(VideoPlayActivity2.DUAL_MODE, false);

        /** View의 초기 값 설정 **/
        //txtRemoteMac1.setText(MwApiWrapper.WrapperBluetoothManager.getRemoteMac(1));
        txtRootSdPath.setText(MwApiWrapper.WrapperVideoManager.getSDPath() + VideoManager.mFolder);
        //if(isDualMode) {
        //	txtRemoteMac2.setText(MwApiWrapper.WrapperBluetoothManager.getRemoteMac(2));
        //} else {
        //	txtRemoteMac2.setVisibility(View.GONE);
        //	}
    }

    OnClickListener mRawClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.selectAccFile:
                    selectFile(11);
                    break;
                case R.id.selectHrFile:
                    selectFile(12);
                    break;
            }
        }
    };

    private void selectFile(int code){
        Intent selectIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectIntent.setType("file/*");
        startActivityForResult(selectIntent, code);
    }


    /**
     * clickListener 이며, 클릭한 영상의 정보를 다음 Activity로 전달하는 역할을 수행.
     * AlertDialog를 출력하여 진행을 담당.
     */
    OnClickListener mPlayClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // PLAY!!!
            //MwApiWrapper.WrapperRawManager.play();

            final int id;
            switch(v.getId()) {
                case R.id.play1:
                    id = R.raw.play1;
                    break;
                case R.id.play2:
                    id = R.raw.play2;
                    break;
                case R.id.play3:
                    id = R.raw.play3;
                    break;
                case R.id.play1_hong:
                    id = R.raw.play1_hong;
                    break;
                case R.id.play2_hong:
                    id = R.raw.play2_hong;
                    break;
                case R.id.play1_lee:
                    id = R.raw.play1_lee;
                    break;
                case R.id.play2_lee:
                    id = R.raw.play2_lee;
                    break;
                default:
                    return;
            }

            String fileName;
            int startSec;
            if(edtFileName.getText().toString() == null)
                fileName = "default";
            else
                fileName = edtFileName.getText().toString();


            if(edtStartSec.getText().toString() == null)
                startSec = 0;
            else {
                try {
                    startSec = Integer.parseInt(edtStartSec.getText().toString()) * 1000;
                } catch (NumberFormatException e) {
                    startSec = 0;
                }
            }
            startSec = startSec < 0 ? 0 : startSec;

            if(setDebug && setDebugOK) {
                // 현재 필요없어보여서 뺏음
				/*
				Intent intent = new Intent(VideoListActivity.this, DebugPlayActivity.class);
				intent.putExtra(DebugManager.DEBUG_RAW_DATA, listQ);
				intent.putExtra(DebugManager.DEBUG_VIDEO_NAME, debugVideoID);
				intent.putExtra(DebugManager.DEBUG_FILE_NAME, fileName);
				intent.putExtra(DebugManager.DEBUG_CHK_SAVE, setSave);

				startActivity(intent);*/
            } else {
                Intent intent = new Intent(VideoListActivity2.this, ActivityVideo2.class);
                intent.putExtra(VideoManager.VIDEO_NAME, id);
                intent.putExtra(ActivityVideo2.ACCPATH, accPath);
                intent.putExtra(ActivityVideo2.HRPATH, hrPath);
                //intent.putExtra(ActivityVideo.RecordName, fileName);
                //intent.putExtra(VideoManager.CHK_SAVE, setSave);
                //intent.putExtra(VideoManager.CHK_DEBUG_PLAY, setDebugPlay);
                intent.putExtra(ActivityVideo2.V_START_SEC, startSec);
                //intent.putExtra(ActivityVideo2.BLE_MAC, MwApiWrapper.WrapperBluetoothManager.getRemoteMac(1));
                //intent.putExtra(VideoPlayActivity.DUAL_MODE, isDualMode);
                startActivity(intent);
            }
        }
    }; // mPlayClick End


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


    private void selectFile() {
        Intent selectIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectIntent.setType("file/*");
        startActivityForResult(selectIntent, REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_FILE:
                if(resultCode == RESULT_OK) {
                    FILE_RAW_DATA = new File(data.getData().getPath());
                    Log.d(tag,"file exist : "+FILE_RAW_DATA.exists());
                    Log.d(tag,"file len : "+FILE_RAW_DATA.length());

                    listQ = getRawDataFiletoStream(readFile(FILE_RAW_DATA));
				/*for(int i=0;i <listQ.length; i++) {
					Log.d(tag, "listQ["+i+"]:"+this.listQ[i]);
				}*/
                    setDebugOK = true;
				/*Intent intent = new Intent(VideoListActivity.this, DebugPlayActivity.class);
				intent.putExtra(DebugManager.DEBUG_RAW_DATA, getRawDataFiletoStream(readFile(FILE_RAW_DATA)));
				startActivity(intent);*/
                } else {
                    chkBoxDebug.setChecked(false);
                    setDebug = false;
                    setDebugOK = false;
                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    Log.d(tag, data.getData().getPath());
                    accPath = data.getData().getPath();
                    mAccTxtPath.setText(accPath);
                }
                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    Log.d(tag, data.getData().getPath());
                    hrPath = data.getData().getPath();
                    mHrTxtPath.setText(hrPath);
                }
                break;
        }
    }
    public ArrayList<String> readFile(File file)  {
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> arrayString = new ArrayList<String>();
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String stringBuffer = null;
            while((stringBuffer = br.readLine()) != null) {
                //Log.d(tag, "read file:::'"+stringBuffer+"'");
                arrayString.add(stringBuffer);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null)
                    br.close();
                if(fr != null)
                    fr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arrayString;
    }
    public float[] getRawDataFiletoStream(ArrayList<String> arrayString) {
        if(arrayString == null)
            return null;

        float[] retList = new float[arrayString.size()*3];

        //Log.d(tag,"size:"+arrayString.size());


        for(int idxArray=0; idxArray < arrayString.size(); idxArray++) {
            String s = arrayString.get(idxArray);
            //Log.d(tag,"s:"+s);

            String[] splitFirst = s.split(" ");

            //Log.d(tag,"f:"+splitFirst[0]+":"+splitFirst[1]);


            retList[(idxArray*3)] = Float.parseFloat(splitFirst[0]);
            retList[(idxArray*3)+1] = Float.parseFloat(splitFirst[1]);
            retList[(idxArray*3)+2] = Float.parseFloat(splitFirst[2]);

			/*Log.d(tag,"["+(idxArray*3)+"]"+Float.parseFloat(splitFirst[0]));
			Log.d(tag,"["+((idxArray*3)+1)+"]"+Float.parseFloat(splitFirst[1]));
			Log.d(tag,"["+((idxArray*3)+2)+"]"+Float.parseFloat(splitFirst[2]));*/
        }

        return retList;
    }
	/*
	@Override
	protected void onResume() {
		super.onResume();
		//test view
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				BluetoothManager mb = BluetoothManager.getInstance(getApplicationContext());
				txtTestCount.setText(""+mb.count);
			}
		}, 1000);
		MwApiWrapper.WrapperBluetoothManager.registCallback(mIBluetooth);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MwApiWrapper.WrapperBluetoothManager.unregistCallback();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MwApiWrapper.WrapperBluetoothManager.disconnect();
	}*/
}


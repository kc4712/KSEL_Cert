package com.coach.test.myapplication.activity;

import java.util.ArrayList;

import com.coach.test.myapplication.R;
import com.coach.test.myapplication.manager.SoundPoolManager;
import com.coach.test.myapplication.util.MwApiWrapper;
import com.coach.test.coachmw.datastructure.DeviceInformation;
import com.coach.test.coachmw.manager.BluetoothManager;
import com.coach.test.coachmw.manager.ConfigManager;
import com.coach.test.coachmw.protocol.IBluetooth;
import com.coach.test.coachmw.util.DataBaseUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

/**
 * Created by Administrator on 2017-02-17.
 */


/**
 * 유저 데이터를 저장하는 activity. 저장하면 블루투스 접속 화면으로 진행.
 * @author user
 *
 */
public class UserInformActivity extends WActivity {
    private static final String tag = "UserInformActivity";

    /** Variable **/
    public static final String EXTRA_NAME = "com.coach.test.myapplication.extra";
    public static final String SET_ACTION_BAR = "com.coach.test.myapplication.actionbar";

    private int mAge;
    private int mWeight;
    private int mHeight;
    private int mSex;
    private int mGoalWeight;
    private int mDietPeriod;
    private int mLanguage;

    private int reference;

    private boolean setOptionMenu = false;

    /** Manager **/
    //private PreferencesManager mPre;
    //private ConfigManager mConfig;

    /** View **/
    private Button btnSave; //btnSetRef;
    private EditText edtAge, edtWeight, edtHeight, edtGoalWeight, edtDietPeriod;
    private ToggleButton tglSex;

    /** Interface **/
    // 미들웨어화
    //private BluetoothManager mBManager;
    private IBluetooth mIBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        /** Manager 초기화 **/
        //mPre = new PreferencesManager(this);
        //mConfig = ConfigManager.getInstance();

        /** View 초기화 **/
        btnSave = (Button) findViewById(R.id.btnSave);
        //btnSetRef = (Button) findViewById(R.id.btnSetReference);

        edtAge = (EditText) findViewById(R.id.editAge);
        edtWeight = (EditText) findViewById(R.id.editWeight);
        edtHeight = (EditText) findViewById(R.id.editHeight);
        edtGoalWeight = (EditText) findViewById(R.id.editGoalWeight);
        edtDietPeriod = (EditText) findViewById(R.id.editDietPeriod);

        tglSex = (ToggleButton) findViewById(R.id.btnSex);

        /** Listener 등록 **/
        btnSave.setOnClickListener(mClick);
        //btnSetRef.setOnClickListener(mClick);
        tglSex.setOnCheckedChangeListener(mChange);

        /** action bar의 진입 여부 확인 **/
        if(getIntent().getCharSequenceExtra(EXTRA_NAME) != null
                && getIntent().getCharSequenceExtra(EXTRA_NAME).equals(SET_ACTION_BAR))
            setOptionMenu = true;

        /** Bluetooth 객체 등록 **/
        // 미들웨어화
        //mBManager = BluetoothManager.getInstance(this);
        /** Interface 등록 **/
        mIBluetooth = new IBluetooth() {
            @Override
            public void connectionState(int state) {
                if(state == BluetoothManager.DISCONNECT) { // 연결됨.
                    runOnUiThread(new Runnable() {
                        public void run() {
                            SoundPoolManager.playBeep(UserInformActivity.this);
                            alert(R.string.alert_disconnect, BluetoothActivity.class);
                            MwApiWrapper.WrapperBluetoothManager.disconnect();
                            vib();
                        }
                    });
                }
            }

            @Override
            public void changedScanList(ArrayList<DeviceInformation> list) {
            }
        };

        /** Activity 변수 초기화 **/
        initVal();
    }

    /** Function **/
    private void initVal() {
        mSex = DataBaseUtil.toSexInteger("남자");
        mAge = Integer.parseInt(edtAge.getText().toString());
        mWeight = Integer.parseInt(edtWeight.getText().toString());
        mHeight = Integer.parseInt(edtHeight.getText().toString());
        mGoalWeight = Integer.parseInt(edtGoalWeight.getText().toString());
        mDietPeriod = Integer.parseInt(edtDietPeriod.getText().toString());
        mLanguage = ConfigManager.COUNTRY_KOREA;
        //reference = mConfig.getLevel();
        //btnSetRef.setText(getReferenceString(reference));
    }
    /*
    private String getReferenceString(int mode) {
        if(mode == PreferencesManager.ADVANCED_REFERENCE)
            return getString(R.string.ref_adv);
        else if(mode == PreferencesManager.MID_REFERENCE)
            return getString(R.string.ref_mid);
        else
            return getString(R.string.ref_nov);
    }
    */
    private boolean isVaild() {
        if(mAge == 0)
            return false;
        if(mWeight == 0)
            return false;
        if(mHeight == 0)
            return false;
        if(mGoalWeight == 0)
            return false;
        if(mDietPeriod == 0)
            return false;
        if(mLanguage == 0)
            return false;

        return true;
    }

    /** Listener **/
    OnClickListener mClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btnSave:
                    // 예외처리해야된다.
                    if(!isVaild()) {
                        toast(R.string.err_non_vaild_user_profile);
                        return;
                    }
                    // 미들웨어 화
				/*DBContactHelper mDBHelper = DBContactHelper.getInstance();
				DataBase.UserProfile profile = DataBase.getUserProfileInstance();
				profile.setUserProfile(mSex, mAge, mWeight, mHeight);
				*/

                    //DataBase.UserProfile profile = MwApiWrapper.WrapperUserProfile.setUserProfile(mSex, mAge, mHeight, mWeight);
                    MwApiWrapper.WrapperDBHelper.setUserProfile(mSex, mAge, mHeight, mLanguage);
                    MwApiWrapper.WrapperDBHelper.setUserWeightProfile(mWeight, mGoalWeight, mDietPeriod);
                    //mConfig.setLevel(reference);

                    /** go to Bluetooth **/
                    if(!setOptionMenu)
                        startActivity(new Intent(UserInformActivity.this, BluetoothActivity.class));

                    finish();
                    break;
			/*
			case R.id.btnSetReference:
				PopupMenu mPopup = new PopupMenu(UserInformActivity.this, v);
				getMenuInflater().inflate(R.menu.popup_reference, mPopup.getMenu());
				mPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch(item.getItemId()) {
						case R.id.adv:
							btnSetRef.setText(getString(R.string.ref_adv));
							reference = PreferencesManager.ADVANCED_REFERENCE;
							break;
						case R.id.mid:
							btnSetRef.setText(getString(R.string.ref_mid));
							reference = PreferencesManager.MID_REFERENCE;
							break;
						case R.id.nov:
							btnSetRef.setText(getString(R.string.ref_nov));
							reference = PreferencesManager.NOVICE_REFERENCE;
							break;
						}
						return true;
					}
				});
				mPopup.show();
				break;
				*/
            }
        }
    };

    OnCheckedChangeListener mChange = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()) {
                case R.id.btnSex:
                    if(isChecked) {
                        mSex = DataBaseUtil.toSexInteger("여자");
                    } else {
                        mSex = DataBaseUtil.toSexInteger("남자");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        MwApiWrapper.WrapperBluetoothManager.unregistCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!setOptionMenu)
            MwApiWrapper.WrapperBluetoothManager.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MwApiWrapper.WrapperBluetoothManager.registCallback(mIBluetooth);
    }
}

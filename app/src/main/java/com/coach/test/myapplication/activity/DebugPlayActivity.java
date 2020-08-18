package com.coach.test.myapplication.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.coach.test.myapplication.R;
import com.coach.test.myapplication.manager.SoundPoolManager;
import com.coach.test.coachmw.datastructure.DeviceInformation;
import com.coach.test.coachmw.manager.BluetoothManager;
import com.coach.test.coachmw.protocol.IBluetooth;
import com.coach.test.coachmw.protocol.IViewComment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-02-17.
 */


/**
 * 동영상 재생 Activity. 실제 동영상이 재생되는 Activity로 재생을 위한 Surface제어와 기타 comment의 출력을 담당.
 * @author user
 *
 */
public class DebugPlayActivity extends WActivity {
    private static final String tag = "DebugPlayActivity";

    /** Variable **/
    private boolean toggle_Instruction = false;
    private boolean show = false;

    private int animDuration = 1000;

    /** Animation **/
    private Animation animScaleFadeIn, animScaleFadeOut;

    /** View **/
    private TextView mTxtVCalorie, mTxtVCount;
    private TextView mTxtVAccuracy1, mTxtVAccuracy2, mTxtVAccuracy3, mTxtVAccuracy4, mTxtVAccuracy5;
    private TextView mTxtInstruction;

    /** comment 관련 **/
    private Timer mMainTimer = new Timer("D-MainTimer");

    /** Interface **/
    private IBluetooth mIBluetooth;
    private IViewComment mIView;

    /** Bluetooth Manager 객체 등록 **/
    private BluetoothManager mBManager;
    /** VideoManager 객체 등록 **/
    //private VideoManager mVManager = new VideoManager();
    /** DebugManager 객체 등록 **/
    // 이제 필요 없어보임. close
    //private DebugManager mDManager= new DebugManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        /** DEBUG 화면 처리 **/
        TextView DEBUG = (TextView)findViewById(R.id.DEBUG_MODE);
        DEBUG.setVisibility(View.VISIBLE);

        /** 동영상 정보 등록 **/
        Intent intent = getIntent();

        // 이제 필요 없어보임. close
        //mDManager.setVideoID(intent.getIntExtra(DebugManager.DEBUG_VIDEO_NAME, 0));
        //mDManager.setRawData(intent.getFloatArrayExtra(DebugManager.DEBUG_RAW_DATA));
        //mDManager.play();

        /** View 초기화 **/
        mTxtVCalorie = (TextView) findViewById(R.id.detailsView1); 		/** 소비 칼로리 **/
        mTxtVCount = (TextView) findViewById(R.id.detailsView2); 		/** 행동 횟수 **/
        mTxtVAccuracy1 = (TextView) findViewById(R.id.accuracyView1); 	/** 행동 강도 1단계 **/
        mTxtVAccuracy2 = (TextView) findViewById(R.id.accuracyView2); 	/** 행동 강도 2단계 **/
        mTxtVAccuracy3 = (TextView) findViewById(R.id.accuracyView3); 	/** 행동 강도 3단계 **/
        mTxtVAccuracy4 = (TextView) findViewById(R.id.accuracyView4); 	/** 행동 강도 4단계 **/
        mTxtVAccuracy5 = (TextView) findViewById(R.id.accuracyView5); 	/** 행동 강도 5단계 **/
        mTxtInstruction = (TextView) findViewById(R.id.txt_instruction);/** 추가 설명 **/

        /** Animation 초기화 **/
        animScaleFadeIn = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeIn.setDuration(animDuration);
        animScaleFadeOut = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeOut.setDuration(animDuration);


        /** comment Timer **/
        if (mMainTimer != null) {
            mMainTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(toggle_Instruction) {
                                mTxtInstruction.setBackgroundResource(R.drawable.text_border_white_non_transparent);
                                toggle_Instruction = false;
                            } else {
                                mTxtInstruction.setBackgroundResource(R.drawable.text_border_blue_non_transparent);
                                toggle_Instruction = true;
                            }
                            // 이제 필요 없어보임. close
							/*if(!mDManager.getPlaying()) {
								finish();
							}*/
                        }
                    });
                }
            }, 0, 1000);
        }

        /** Bluetooth 객체 등록 **/
        mBManager = BluetoothManager.getInstance(this);
        /** Bluetooth Interface 등록 **/
        mIBluetooth = new IBluetooth() {
            @Override
            public void connectionState(int state) {
                if(state == BluetoothManager.DISCONNECT) { // 연결됨.
                    runOnUiThread(new Runnable() {
                        public void run() {
                            SoundPoolManager.playBeep(DebugPlayActivity.this);
                            alert(R.string.alert_disconnect, BluetoothActivity.class);
                            mBManager.disconnect();
                            vib();
                        }
                    }); // runOnUiThread End
                }
            }

            @Override
            public void changedScanList(ArrayList<DeviceInformation> list) {
            }
        }; // mIBluetooth End
        BluetoothManager.registCallback(mIBluetooth);

        /** KIST 엔진의 결과물을 표시하기 위한 interface callback **/
        mIView = new IViewComment() {
            @Override
            public void onKISTOutput_Calorie(final float calorie, final int videoID) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /** Calorie **/
                        mTxtVCalorie.setText(calorie+"kcal");
                    }
                });
            }
            @Override
            public void onKISTOutput_Accuracy(final int accuracy_Percent, final int videoID) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /** Phase **/
                        toViewPhase(toPhase(accuracy_Percent));
                    }
                });
            }

            @Override
            public void onInstruction(final String txt) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(show) {
                            mTxtInstruction.setText(txt);
                            return;
                        }

                        show = true;
                        mTxtInstruction.setVisibility(View.VISIBLE);
                        mTxtInstruction.setText(txt);
                        mTxtInstruction.startAnimation(animScaleFadeIn);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (show) {
                                    mTxtInstruction.startAnimation(animScaleFadeOut);
                                    mTxtInstruction.setVisibility(View.INVISIBLE);
                                }
                            }
                        }, animDuration * 3);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show = false;
                            }
                        }, animDuration*4);
                    }
                });
            }


            @Override
            public void onDescription(int videoIndex, String txt) {
            }

            @Override
            public void onKISTOutput_Count(final int count, final int videoID) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /** Count **/
                        mTxtVCount.setText(count+"/15"); /** 전체 횟수는 어떻게???? **/
                    }
                });
            }

            @Override
            public void onBriefDescription(int videoIndex, String txt) {
            }
            @Override
            public void onKISTOutput_AccuracyD(int arg0, int arg1) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onKISTOutput_CalorieD(float arg0, int arg1) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onKISTOutput_CountD(int arg0, int arg1) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onWarnning(String arg0) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onWarnningD(String arg0) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onKISTOutput_Point(int arg0, int arg1) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onKISTOutput_PointD(int arg0, int arg1) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onLaptime(String arg0) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onHeartRateCompared(int arg0) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onCommentR(String arg0) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onCommentSection(final long displayTime, final int point, final int count_percent, final int accuracy_percent, final String txt) {
                // TODO Auto-generated method stub

            }
        }; // mIView End
        // 이제 필요 없어보임. close
        //DebugManager.registCallback(mIView);
    }

    /**
     * 입력된 강도(%)를 단계의 형태로 반환. 총 5단계.
     * @param percent 행동 강도(%)
     * @return 행동 강도의 단계 형태
     */
    private int toPhase(int percent) {
        if(percent == 0)
            return 0;
        else if(0<percent && percent < 20)
            return 1;
        else if(20<=percent && percent < 40)
            return 2;
        else if(40<=percent && percent < 60)
            return 3;
        else if(60<=percent && percent < 80)
            return 4;
        else
            return 5;
    }

    /**
     * 입력된 강도를 View형태로 전환. 강도에 따라 표시되는 View가 달라짐.
     * @param phase 행동 강도
     * @return true:성공, false:실패.(View 초기화가 이루어지지 않음. 1~5단계의 범위를 벗어남)
     */
    private boolean toViewPhase(int phase) {
        if(mTxtVAccuracy1 == null || mTxtVAccuracy2 == null || mTxtVAccuracy3 == null
                || mTxtVAccuracy4 == null || mTxtVAccuracy5 == null)
            return false;
        switch(phase) {
            case 0:
                mTxtVAccuracy1.setVisibility(View.INVISIBLE);
                mTxtVAccuracy2.setVisibility(View.INVISIBLE);
                mTxtVAccuracy3.setVisibility(View.INVISIBLE);
                mTxtVAccuracy4.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mTxtVAccuracy1.setVisibility(View.VISIBLE);
                mTxtVAccuracy2.setVisibility(View.INVISIBLE);
                mTxtVAccuracy3.setVisibility(View.INVISIBLE);
                mTxtVAccuracy4.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mTxtVAccuracy1.setVisibility(View.VISIBLE);
                mTxtVAccuracy2.setVisibility(View.VISIBLE);
                mTxtVAccuracy3.setVisibility(View.INVISIBLE);
                mTxtVAccuracy4.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mTxtVAccuracy1.setVisibility(View.VISIBLE);
                mTxtVAccuracy2.setVisibility(View.VISIBLE);
                mTxtVAccuracy3.setVisibility(View.VISIBLE);
                mTxtVAccuracy4.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5.setVisibility(View.INVISIBLE);
                break;
            case 4:
                mTxtVAccuracy1.setVisibility(View.VISIBLE);
                mTxtVAccuracy2.setVisibility(View.VISIBLE);
                mTxtVAccuracy3.setVisibility(View.VISIBLE);
                mTxtVAccuracy4.setVisibility(View.VISIBLE);
                mTxtVAccuracy5.setVisibility(View.INVISIBLE);
                break;
            case 5:
                mTxtVAccuracy1.setVisibility(View.VISIBLE);
                mTxtVAccuracy2.setVisibility(View.VISIBLE);
                mTxtVAccuracy3.setVisibility(View.VISIBLE);
                mTxtVAccuracy4.setVisibility(View.VISIBLE);
                mTxtVAccuracy5.setVisibility(View.VISIBLE);
                break;
            default:
                return false;
        }

        return true;
    }

    /** 종료될 시, 영상 객체들 release **/
    private void exitPlayer() {
        if (mMainTimer != null) {
            mMainTimer.cancel();
            mMainTimer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundPoolManager.playEnd(DebugPlayActivity.this);
        BluetoothManager.unregistCallback();
        // 이제 필요 없어보임. close
        //DebugManager.unregistCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 이제 필요 없어보임. close
        //mDManager.end();
        exitPlayer();
    }
}
package com.coach.test.myapplication.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.coach.test.myapplication.R;
import com.coach.test.myapplication.manager.SoundPoolManager;
import com.coach.test.myapplication.util.MwApiWrapper;
import com.coach.test.myapplication.view.StrokeTextView;
import com.coach.test.coachmw.datastructure.DeviceInformation;
import com.coach.test.coachmw.manager.BluetoothManager;
import com.coach.test.coachmw.manager.VideoManager;
import com.coach.test.coachmw.manager.VideoManager.VideoParameter;
import com.coach.test.coachmw.protocol.IBluetooth;
import com.coach.test.coachmw.protocol.IViewComment;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
public class VideoPlayActivity extends WActivity implements SurfaceHolder.Callback {
    private static final String tag = "VideoPlayActivity";

    /** Variable **/
    public static final String V_START_SEC = "com.coach.test.myapplication.start_sec";
    public static final String DUAL_MODE = "com.coach.test.myapplication.dual_mode";

    private boolean isDualMode = false;

    private boolean toggle_Instruction = false;
    private boolean toggle_InstructionD = false;
    private boolean show_Inst = false, show_Laptime = false, show_Warnning = false;
    private boolean showD = false;

    private int animDuration = 200;
    private int animRatingDuration = 500;

    private int preRating = 0;
    private int thr_rating = 4;

    private int prePos = 0;

    public static final int PHASE_0 = 0;
    public static final int PHASE_1 = 10;
    public static final int PHASE_2 = 30;
    public static final int PHASE_3 = 50;
    public static final int PHASE_4 = 70;
    public static final int PHASE_5 = 90;

    private static boolean setChkDebugPlay=false;

    private boolean resume = false;
    private static int startSec = 0;

    /** View **/
    private SurfaceView mSView;
    private SurfaceHolder mSHolder;
    private MediaPlayer mPlayer;
    private TextView mTxtVCalorie, mTxtVCount, mTxtVCalorieD, mTxtVCountD, mTxtWarnning, mTxtLapTime;
    private TextView mTxtVAccuracy1, mTxtVAccuracy2, mTxtVAccuracy3, mTxtVAccuracy4, mTxtVAccuracy5
            ,mTxtVAccuracy1D, mTxtVAccuracy2D, mTxtVAccuracy3D, mTxtVAccuracy4D, mTxtVAccuracy5D;
    private TextView mTxtInstruction, mTxtRating, mTxtDescription, mTxtInstructionD;
    private StrokeTextView mTxtBriefDescription_v_1;

    /** Animation **/
    private Animation animScaleFadeIn_Inst, animScaleFadeOut_Inst, animScaleFadeInD, animScaleFadeOutD,
            animScaleFadeIn_Laptime,animScaleFadeOut_Laptime,animScaleFadeIn_Warnning,animScaleFadeOut_Warnning,
            animScaleFadeIn_Desc,animScaleFadeOut_Desc;
    //private ValueAnimator objAnimRating;

    /** comment 관련 **/
    private Timer mMainTimer = new Timer("MainTimer");
    private Timer mMainTimerD = new Timer("MainTimer-Dual");

    /** Interface **/
    private IBluetooth mIBluetooth;
    private IViewComment mIView;

    /** Bluetooth Manager 객체 등록 **/
    // 미들웨어화
    //private BluetoothManager mBManager;
    /** VideoManager 객체 등록 **/
    // 미들웨어화
    //private VideoManager mVManager = new VideoManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        /** isDual? **/
        isDualMode = getIntent().getBooleanExtra(VideoPlayActivity.DUAL_MODE, false);

        /** wrapper 초기화 **/
        //MwApiWrapper.WrapperVideoManager.setInstance(this);
        MwApiWrapper.WrapperVideoManager.setPlayMode(VideoManager.MODE_NEW_START);

        /** 동영상 재생 초기화 **/
        mPlayer = new MediaPlayer();

        mSView = (SurfaceView) findViewById(R.id.surfaceView1);
        mSHolder = mSView.getHolder();
        mSHolder.addCallback(this);

        mSHolder.setKeepScreenOn(true);

        /** View 초기화 **/
        mTxtVCalorie = (TextView) findViewById(R.id.detailsView1); 		/** 소비 칼로리 **/
        mTxtVCount = (TextView) findViewById(R.id.detailsView2); 		/** 행동 횟수 **/
        mTxtVAccuracy1 = (TextView) findViewById(R.id.accuracyView1); 	/** 행동 강도 1단계 **/
        mTxtVAccuracy2 = (TextView) findViewById(R.id.accuracyView2); 	/** 행동 강도 2단계 **/
        mTxtVAccuracy3 = (TextView) findViewById(R.id.accuracyView3); 	/** 행동 강도 3단계 **/
        mTxtVAccuracy4 = (TextView) findViewById(R.id.accuracyView4); 	/** 행동 강도 4단계 **/
        mTxtVAccuracy5 = (TextView) findViewById(R.id.accuracyView5); 	/** 행동 강도 5단계 **/

        mTxtVCalorieD = (TextView) findViewById(R.id.detailsView1D); 		/** 소비 칼로리 **/
        mTxtVCountD = (TextView) findViewById(R.id.detailsView2D); 		/** 행동 횟수 **/
        mTxtVAccuracy1D = (TextView) findViewById(R.id.accuracyView1D); 	/** 행동 강도 1단계 **/
        mTxtVAccuracy2D = (TextView) findViewById(R.id.accuracyView2D); 	/** 행동 강도 2단계 **/
        mTxtVAccuracy3D = (TextView) findViewById(R.id.accuracyView3D); 	/** 행동 강도 3단계 **/
        mTxtVAccuracy4D = (TextView) findViewById(R.id.accuracyView4D); 	/** 행동 강도 4단계 **/
        mTxtVAccuracy5D = (TextView) findViewById(R.id.accuracyView5D); 	/** 행동 강도 5단계 **/

        mTxtInstruction = (TextView) findViewById(R.id.txt_instruction);/** 추가 설명 **/
        mTxtInstructionD = (TextView) findViewById(R.id.txt_instructionD);/** 추가 설명 2인용 **/
        mTxtRating = (TextView) findViewById(R.id.txt_rating); 			/** 단계별 평가 **/
        mTxtDescription = (TextView) findViewById(R.id.txt_description);/** 동작의 설명 View **/
        mTxtBriefDescription_v_1 = (StrokeTextView) findViewById(R.id.txt_v_1_brief_description); /** (동1)동작의 간단한 설명 View **/
        mTxtWarnning = (TextView) findViewById(R.id.txt_warnning); /** 심박수 경고 **/
        mTxtLapTime = (TextView) findViewById(R.id.txt_laptime); /** laptime 메시지 **/


        /** Animation 초기화 **/
        animScaleFadeIn_Inst = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeIn_Inst.setDuration(animDuration);
        animScaleFadeOut_Inst = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeOut_Inst.setDuration(animDuration);
        animScaleFadeInD = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeInD.setDuration(animDuration);
        animScaleFadeOutD = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeOutD.setDuration(animDuration);
        animScaleFadeIn_Laptime = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeIn_Laptime.setDuration(animDuration);
        animScaleFadeOut_Laptime = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeOut_Laptime.setDuration(animDuration);
        animScaleFadeIn_Warnning = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeIn_Warnning.setDuration(animDuration);
        animScaleFadeOut_Warnning = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeOut_Warnning.setDuration(animDuration);
        animScaleFadeIn_Desc = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeIn_Desc.setDuration(animDuration);
        animScaleFadeOut_Desc = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScaleFadeOut_Desc.setDuration(animDuration);
		/*
		final FrameLayout.LayoutParams frameParam = (FrameLayout.LayoutParams) mTxtRating.getLayoutParams();
		objAnimRating = ValueAnimator.ofInt(frameParam.getMarginStart(), frameParam.getMarginStart()+getDpToPixel(100));
		objAnimRating.setDuration(animRatingDuration);
		objAnimRating.setInterpolator(new OvershootInterpolator());
		objAnimRating.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				frameParam.setMarginStart((Integer)animation.getAnimatedValue());
				mTxtRating.setLayoutParams(frameParam);
			}
		});
		*/


        /** comment Timer **/
		/*if (mMainTimer != null) {
			mMainTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							MwApiWrapper.WrapperVideoManager.setCurrentPosition(getCurrentPosition());
							if(toggle_Instruction) {
								mTxtInstruction.setBackgroundResource(R.drawable.text_border_white_non_transparent);
								toggle_Instruction = false;
							} else {
								mTxtInstruction.setBackgroundResource(R.drawable.text_border_blue_non_transparent);
								toggle_Instruction = true;
							}
						}
					});
				}
			}, 0, 1000);
		}*/
		/*
		if (mMainTimerD != null) {
			mMainTimerD.schedule(new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							//MwApiWrapper.WrapperVideoManager.setCurrentPosition(getCurrentPosition());
							if(toggle_InstructionD) {
								mTxtInstructionD.setBackgroundResource(R.drawable.text_border_white_non_transparent);
								toggle_InstructionD = false;
							} else {
								mTxtInstructionD.setBackgroundResource(R.drawable.text_border_blue_non_transparent);
								toggle_InstructionD = true;
							}
						}
					});
				}
			}, 0, 1000);
		}
		*/
        /** Bluetooth 객체 등록 **/
        // 미들웨어화
        //mBManager = BluetoothManager.getInstance(this);
        /** Bluetooth Interface 등록 **/
        mIBluetooth = new IBluetooth() {
            @Override
            public void connectionState(int state) {
                if(state == BluetoothManager.DISCONNECT) { // 연결됨.
                    runOnUiThread(new Runnable() {
                        public void run() {
                            SoundPoolManager.playBeep(VideoPlayActivity.this);
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
        BluetoothManager.registCallback(mIBluetooth);

        /** KIST 엔진의 결과물을 표시하기 위한 interface callback **/
        mIView = new IViewComment() {
            @Override
            public void onKISTOutput_Count(final int count, final int videoID) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /** Count **/
                        mTxtVCount.setText(count+toCountInformation(videoID)); /** 전체 횟수는 어떻게???? **/
                    }
                });
            }
            @Override
            public void onKISTOutput_CountD(final int count, final int videoID) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /** Count **/
                        mTxtVCountD.setText(count+toCountInformation(videoID)); /** 전체 횟수는 어떻게???? **/
                    }
                });
            }

            @Override
            public void onKISTOutput_Calorie(final float calorie, final int videoID) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /** Calorie **/
                        //float c = Float.parseFloat(String.format("%.2f",(calorie*8)+10));
                        mTxtVCalorie.setText(calorie+"kcal");
                    }
                });
            }
            @Override
            public void onKISTOutput_CalorieD(final float calorie, final int videoID) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /** Calorie **/
                        mTxtVCalorieD.setText(calorie+"kcal");
                    }
                });
            }

            @Override
            public void onKISTOutput_Accuracy(final int accuracy_Percent, final int videoID) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //int currentRating = toPhase(accuracy_Percent);

                        /** Phase **/
                        toViewPhase(toPhase(accuracy_Percent));
						/*
						*//** Rating **//*
						if(currentRating == 5 && !objAnimRating.isRunning() && !MwApiWrapper.WrapperVideoManager.isRVideo()) {
							if (objAnimRating.isStarted() && currentRating > thr_rating) {
								objAnimRating.cancel();
							}
							if (currentRating > thr_rating) {
								mTxtRating.setVisibility(View.VISIBLE);
								Log.d(tag,"rate start");
								setRatingTextView(currentRating);
								setGradientText(currentRating);
								Log.d(tag,"rate end");
								objAnimRating.start();
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										mTxtRating.setVisibility(View.INVISIBLE);
									}
								}, 800);
							} else
								mTxtRating.setVisibility(View.INVISIBLE);
						}
						*/
                    }
                });
            }
            @Override
            public void onKISTOutput_AccuracyD(final int accuracy_Percent, final int videoID) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //int currentRating = toPhase(accuracy_Percent);
                        Log.d(tag+"D","onKISTOutput_Accuracy1");

                        /** Phase **/
                        toViewPhaseD(toPhase(accuracy_Percent));
                        Log.d(tag+"D","onKISTOutput_Accuracy2 : phase->"+toPhase(accuracy_Percent));
						/*
						*//** Rating **//*
						if(currentRating == 5 && !objAnimRating.isRunning() && !MwApiWrapper.WrapperVideoManager.isRVideo()) {
							if (objAnimRating.isStarted() && currentRating > thr_rating) {
								objAnimRating.cancel();
							}
							if (currentRating > thr_rating) {
								mTxtRating.setVisibility(View.VISIBLE);
								Log.d(tag+"D","rate start");
								setRatingTextView(currentRating);
								setGradientText(currentRating);
								Log.d(tag+"D","rate end");
								objAnimRating.start();
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										mTxtRating.setVisibility(View.INVISIBLE);
									}
								}, 800);
							} else
								mTxtRating.setVisibility(View.INVISIBLE);
						}
						*/
                    }
                });
            }

            @Override
            public void onInstruction(final String txt) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(MwApiWrapper.WrapperVideoManager.isRVideo())
                            return;

                        if (show_Inst) {

                            //if(mTxtInstruction.getText().toString().equals(txt))
                            //	mTxtInstruction.setText(txt);
                            //else
                            //	mTxtInstruction.setText(mTxtInstruction.getText().toString()+"\n"+txt);

                            return;
                        }

                        show_Inst = true;
                        mTxtInstruction.setVisibility(View.VISIBLE);
                        mTxtInstruction.setText(txt);
                        //mTxtInstruction.startAnimation(animScaleFadeIn_Inst);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (show_Inst) {
                                    //mTxtInstruction.startAnimation(animScaleFadeOut_Inst);
                                    mTxtInstruction.setVisibility(View.INVISIBLE);
                                }
                            }
                        }, animDuration * 10);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show_Inst = false;
                            }
                        }, animDuration * 11);
                    }
                });
            }

            @Override
            public void onDescription(final int videoIndex, final String txt) {
                Log.d("jeyang","desc "+videoIndex);
                runOnUiThread(new Runnable() {
                    public void run() {
                        setDescriptionView(videoIndex, txt);
                    }
                });
            }

            @Override
            public void onBriefDescription(final int videoIndex, final String txt) {
                Log.d("jeyang","brief "+videoIndex);
                runOnUiThread(new Runnable() {
                    public void run() {
                        setBriefDescriptionView(videoIndex, txt);
                    }
                });
            }
            @Override
            public void onWarnning(final String txt) {
                runOnUiThread(new Runnable() {
                    public void run() {
						/*
						if(MwApiWrapper.WrapperVideoManager.isRVideo())
							return;
						*/
                        if (show_Warnning) {

                            //if(mTxtInstruction.getText().toString().equals(txt))
                            //	mTxtInstruction.setText(txt);
                            //else
                            //	mTxtInstruction.setText(mTxtInstruction.getText().toString()+"\n"+txt);

                            return;
                        }

                        show_Warnning = true;
                        mTxtWarnning.setVisibility(View.VISIBLE);
                        mTxtWarnning.setText(txt);
                        mTxtWarnning.startAnimation(animScaleFadeIn_Warnning);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (show_Warnning) {
                                    mTxtWarnning.startAnimation(animScaleFadeOut_Warnning);
                                    mTxtWarnning.setVisibility(View.INVISIBLE);
                                }
                            }
                        }, animDuration * 10);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show_Warnning = false;
                            }
                        }, animDuration * 11);
                    }
                });
            }

            @Override
            public void onWarnningD(String arg0) {
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
            public void onLaptime(final String txt) {
                runOnUiThread(new Runnable() {
                    public void run() {

                        if(MwApiWrapper.WrapperVideoManager.isRVideo())
                            return;

                        if (show_Laptime) {

                            //if(mTxtInstruction.getText().toString().equals(txt))
                            //	mTxtInstruction.setText(txt);
                            //else
                            //	mTxtInstruction.setText(mTxtInstruction.getText().toString()+"\n"+txt);

                            return;
                        }

                        show_Laptime = true;
                        mTxtLapTime.setVisibility(View.VISIBLE);
                        mTxtLapTime.setText(txt);
                        mTxtLapTime.startAnimation(animScaleFadeIn_Laptime);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (show_Laptime) {
                                    mTxtLapTime.startAnimation(animScaleFadeOut_Laptime);
                                    mTxtLapTime.setVisibility(View.INVISIBLE);
                                }
                            }
                        }, animDuration * 10);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                show_Laptime = false;
                            }
                        }, animDuration * 11);
                    }
                });
            }
            @Override
            public void onHeartRateCompared(final int hr) {
                //Log.d(tag,"hr:"+hr);
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
        VideoManager.registCallback(mIView);
        mSView.performClick();
        mSView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if(mPlayer != null) {
                            if(mPlayer.isPlaying()) {
                                mPlayer.pause();
                                MwApiWrapper.WrapperVideoManager.setPlaying(false);
                            } else {
                                mPlayer.start();
                                MwApiWrapper.WrapperVideoManager.setPlaying(true);
                            }
                        }

                        v.performClick();
                        break;
                }

                return true;
            }
        });
    }

    private void start() {
        if (mMainTimer != null) {
            mMainTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int pos = getCurrentPosition();
                            if(prePos != pos) {
                                //Log.d(tag,"ms position:"+mPlayer.getCurrentPosition()+" sec position:"+mPlayer.getCurrentPosition()/1000);
                                MwApiWrapper.WrapperVideoManager.setCurrentPosition(pos);
                                prePos = pos;
                            }
                            if(toggle_Instruction) {
                                mTxtInstruction.setBackgroundResource(R.drawable.text_border_white_non_transparent);
                                toggle_Instruction = false;
                            } else {
                                mTxtInstruction.setBackgroundResource(R.drawable.text_border_blue_non_transparent);
                                toggle_Instruction = true;
                            }
                        }
                    });
                }
            }, 0, 50);
        }
    }
    private void stop() {
        if(mMainTimer != null)
            mMainTimer.cancel();
        mMainTimer = null;
    }

    private String toCountInformation(int videoID) {
        switch(videoID) {
            case VideoManager.VideoParameter.Group1.Index.V_1_1:
            case VideoManager.VideoParameter.Group1.Index.V_1_1_R:
                return "/" + VideoManager.VideoParameter.Group1.Ref.Count.V_1_1;
            case VideoManager.VideoParameter.Group1.Index.V_1_8:
            case VideoManager.VideoParameter.Group1.Index.V_1_8_R:
                return "/" + VideoManager.VideoParameter.Group1.Ref.Count.V_1_8;
            case VideoManager.VideoParameter.Group1.Index.V_1_3:
            case VideoManager.VideoParameter.Group1.Index.V_1_3_R:
                return "/" + VideoManager.VideoParameter.Group1.Ref.Count.V_1_3;
            case VideoManager.VideoParameter.Group1.Index.V_1_6:
            case VideoManager.VideoParameter.Group1.Index.V_1_6_R:
                return "/" + VideoManager.VideoParameter.Group1.Ref.Count.V_1_6;
            case VideoManager.VideoParameter.Group1.Index.V_1_16:
            case VideoManager.VideoParameter.Group1.Index.V_1_16_R:
                return "/" + VideoManager.VideoParameter.Group1.Ref.Count.V_1_16;

            case VideoManager.VideoParameter.Group2.Index.V_2_1:
            case VideoManager.VideoParameter.Group2.Index.V_2_1_R:
                return "/" + VideoManager.VideoParameter.Group2.Ref.Count.V_2_1;
            case VideoManager.VideoParameter.Group2.Index.V_2_2:
            case VideoManager.VideoParameter.Group2.Index.V_2_2_R:
                return "/" + VideoManager.VideoParameter.Group2.Ref.Count.V_2_2;
            case VideoManager.VideoParameter.Group2.Index.V_2_3:
            case VideoManager.VideoParameter.Group2.Index.V_2_3_R:
                return "/" + VideoManager.VideoParameter.Group2.Ref.Count.V_2_3;
            case VideoManager.VideoParameter.Group2.Index.V_2_4:
            case VideoManager.VideoParameter.Group2.Index.V_2_4_R:
                return "/" + VideoManager.VideoParameter.Group2.Ref.Count.V_2_4;
            case VideoManager.VideoParameter.Group2.Index.V_2_6:
            case VideoManager.VideoParameter.Group2.Index.V_2_6_R:
                return "/" + VideoManager.VideoParameter.Group2.Ref.Count.V_2_6;

            case VideoManager.VideoParameter.Group4.Index.V_4_1:
            case VideoManager.VideoParameter.Group4.Index.V_4_1_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_1;
            case VideoManager.VideoParameter.Group4.Index.V_4_2:
            case VideoManager.VideoParameter.Group4.Index.V_4_2_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_2;
            case VideoManager.VideoParameter.Group4.Index.V_4_3:
            case VideoManager.VideoParameter.Group4.Index.V_4_3_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_3;
            case VideoManager.VideoParameter.Group4.Index.V_4_4:
            case VideoManager.VideoParameter.Group4.Index.V_4_4_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_4;
            case VideoManager.VideoParameter.Group4.Index.V_4_5:
            case VideoManager.VideoParameter.Group4.Index.V_4_5_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_5;
            case VideoManager.VideoParameter.Group4.Index.V_4_6:
            case VideoManager.VideoParameter.Group4.Index.V_4_6_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_6;
            case VideoManager.VideoParameter.Group4.Index.V_4_7:
            case VideoManager.VideoParameter.Group4.Index.V_4_7_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_7;
            case VideoManager.VideoParameter.Group4.Index.V_4_8:
            case VideoManager.VideoParameter.Group4.Index.V_4_8_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_8;
            case VideoManager.VideoParameter.Group4.Index.V_4_9:
            case VideoManager.VideoParameter.Group4.Index.V_4_9_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_9;
            case VideoManager.VideoParameter.Group4.Index.V_4_10:
            case VideoManager.VideoParameter.Group4.Index.V_4_10_R:
                return "/" + VideoManager.VideoParameter.Group4.Ref.Count.V_4_10;

            case VideoManager.VideoParameter.Group5.Index.V_5_1:
            case VideoManager.VideoParameter.Group5.Index.V_5_1_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_1;
            case VideoManager.VideoParameter.Group5.Index.V_5_2:
            case VideoManager.VideoParameter.Group5.Index.V_5_2_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_2;
            case VideoManager.VideoParameter.Group5.Index.V_5_3:
            case VideoManager.VideoParameter.Group5.Index.V_5_3_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_3;
            case VideoManager.VideoParameter.Group5.Index.V_5_4:
            case VideoManager.VideoParameter.Group5.Index.V_5_4_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_4;
            case VideoManager.VideoParameter.Group5.Index.V_5_5:
            case VideoManager.VideoParameter.Group5.Index.V_5_5_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_5;
            case VideoManager.VideoParameter.Group5.Index.V_5_6:
            case VideoManager.VideoParameter.Group5.Index.V_5_6_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_6;
            case VideoManager.VideoParameter.Group5.Index.V_5_7:
            case VideoManager.VideoParameter.Group5.Index.V_5_7_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_7;
            case VideoManager.VideoParameter.Group5.Index.V_5_8:
            case VideoManager.VideoParameter.Group5.Index.V_5_8_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_8;
            case VideoManager.VideoParameter.Group5.Index.V_5_9:
            case VideoManager.VideoParameter.Group5.Index.V_5_9_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_9;
            case VideoManager.VideoParameter.Group5.Index.V_5_10:
            case VideoManager.VideoParameter.Group5.Index.V_5_10_R:
                return "/" + VideoManager.VideoParameter.Group5.Ref.Count.V_5_10;

            case VideoManager.VideoParameter.Group6.Index.V_6_1:
            case VideoManager.VideoParameter.Group6.Index.V_6_1_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_1;
            case VideoManager.VideoParameter.Group6.Index.V_6_2:
            case VideoManager.VideoParameter.Group6.Index.V_6_2_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_2;
            case VideoManager.VideoParameter.Group6.Index.V_6_3:
            case VideoManager.VideoParameter.Group6.Index.V_6_3_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_3;
            case VideoManager.VideoParameter.Group6.Index.V_6_4:
            case VideoManager.VideoParameter.Group6.Index.V_6_4_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_4;
            case VideoManager.VideoParameter.Group6.Index.V_6_5:
            case VideoManager.VideoParameter.Group6.Index.V_6_5_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_5;
            case VideoManager.VideoParameter.Group6.Index.V_6_6:
            case VideoManager.VideoParameter.Group6.Index.V_6_6_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_6;
            case VideoManager.VideoParameter.Group6.Index.V_6_7:
            case VideoManager.VideoParameter.Group6.Index.V_6_7_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_7;
            case VideoManager.VideoParameter.Group6.Index.V_6_8:
            case VideoManager.VideoParameter.Group6.Index.V_6_8_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_8;
            case VideoManager.VideoParameter.Group6.Index.V_6_9:
            case VideoManager.VideoParameter.Group6.Index.V_6_9_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_9;
            case VideoManager.VideoParameter.Group6.Index.V_6_10:
            case VideoManager.VideoParameter.Group6.Index.V_6_10_R:
                return "/" + VideoManager.VideoParameter.Group6.Ref.Count.V_6_10;

            case VideoManager.VideoParameter.Group7.Index.V_7_1:
            case VideoManager.VideoParameter.Group7.Index.V_7_1_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_1;
            case VideoManager.VideoParameter.Group7.Index.V_7_2:
            case VideoManager.VideoParameter.Group7.Index.V_7_2_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_2;
            case VideoManager.VideoParameter.Group7.Index.V_7_3:
            case VideoManager.VideoParameter.Group7.Index.V_7_3_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_3;
            case VideoManager.VideoParameter.Group7.Index.V_7_4:
            case VideoManager.VideoParameter.Group7.Index.V_7_4_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_4;
            case VideoManager.VideoParameter.Group7.Index.V_7_5:
            case VideoManager.VideoParameter.Group7.Index.V_7_5_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_5;
            case VideoManager.VideoParameter.Group7.Index.V_7_6:
            case VideoManager.VideoParameter.Group7.Index.V_7_6_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_6;
            case VideoManager.VideoParameter.Group7.Index.V_7_7:
            case VideoManager.VideoParameter.Group7.Index.V_7_7_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_7;
            case VideoManager.VideoParameter.Group7.Index.V_7_8:
            case VideoManager.VideoParameter.Group7.Index.V_7_8_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_8;
            case VideoManager.VideoParameter.Group7.Index.V_7_9:
            case VideoManager.VideoParameter.Group7.Index.V_7_9_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_9;
            case VideoManager.VideoParameter.Group7.Index.V_7_10:
            case VideoManager.VideoParameter.Group7.Index.V_7_10_R:
                return "/" + VideoManager.VideoParameter.Group7.Ref.Count.V_7_10;
            default:
                return "/10";
        }
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
     * 동작의 간략 설명을 표시하기 위한 View setting
     * @param videoIndex 현재 재생 중인 동영상 idx
     * @param txt 해당 영상의 설명
     */
    private void setDescriptionView(int videoIndex, String txt) {
        if(txt == null) {
            if(mTxtDescription.getVisibility() == View.VISIBLE) {
                mTxtDescription.startAnimation(animScaleFadeOut_Desc);
                mTxtDescription.setVisibility(View.INVISIBLE);
            }
            return;
        }

        switch(videoIndex) {
            case VideoParameter.Group1.Index.V_1_1_R:
            case VideoParameter.Group1.Index.V_1_8_R:
            case VideoParameter.Group1.Index.V_1_3_R:
            case VideoParameter.Group1.Index.V_1_6_R:
            case VideoParameter.Group1.Index.V_1_16_R:
            case VideoParameter.Group2.Index.V_2_1_R:
            case VideoParameter.Group2.Index.V_2_2_R:
            case VideoParameter.Group2.Index.V_2_3_R:
            case VideoParameter.Group2.Index.V_2_4_R:
            case VideoParameter.Group2.Index.V_2_6_R:
            case VideoParameter.Group4.Index.V_4_1_R:
            case VideoParameter.Group4.Index.V_4_1:
            case VideoParameter.Group4.Index.V_4_2_R:
            case VideoParameter.Group4.Index.V_4_2:
            case VideoParameter.Group4.Index.V_4_3_R:
            case VideoParameter.Group4.Index.V_4_3:
            case VideoParameter.Group4.Index.V_4_4_R:
            case VideoParameter.Group4.Index.V_4_4:
            case VideoParameter.Group4.Index.V_4_5_R:
            case VideoParameter.Group4.Index.V_4_5:
            case VideoParameter.Group4.Index.V_4_6_R:
            case VideoParameter.Group4.Index.V_4_6:
            case VideoParameter.Group4.Index.V_4_7_R:
            case VideoParameter.Group4.Index.V_4_7:
            case VideoParameter.Group4.Index.V_4_8_R:
            case VideoParameter.Group4.Index.V_4_8:
            case VideoParameter.Group4.Index.V_4_9_R:
            case VideoParameter.Group4.Index.V_4_9:
            case VideoParameter.Group4.Index.V_4_10_R:
            case VideoParameter.Group4.Index.V_4_10:
            case VideoParameter.Group5.Index.V_5_1_R:
            case VideoParameter.Group5.Index.V_5_1:
            case VideoParameter.Group5.Index.V_5_2_R:
            case VideoParameter.Group5.Index.V_5_2:
            case VideoParameter.Group5.Index.V_5_3_R:
            case VideoParameter.Group5.Index.V_5_3:
            case VideoParameter.Group5.Index.V_5_4_R:
            case VideoParameter.Group5.Index.V_5_4:
            case VideoParameter.Group5.Index.V_5_5_R:
            case VideoParameter.Group5.Index.V_5_5:
            case VideoParameter.Group5.Index.V_5_6_R:
            case VideoParameter.Group5.Index.V_5_6:
            case VideoParameter.Group5.Index.V_5_7_R:
            case VideoParameter.Group5.Index.V_5_7:
            case VideoParameter.Group5.Index.V_5_8_R:
            case VideoParameter.Group5.Index.V_5_8:
            case VideoParameter.Group5.Index.V_5_9_R:
            case VideoParameter.Group5.Index.V_5_9:
            case VideoParameter.Group5.Index.V_5_10_R:
            case VideoParameter.Group5.Index.V_5_10:
            case VideoParameter.Group6.Index.V_6_1_R:
            case VideoParameter.Group6.Index.V_6_1:
            case VideoParameter.Group6.Index.V_6_2_R:
            case VideoParameter.Group6.Index.V_6_2:
            case VideoParameter.Group6.Index.V_6_3_R:
            case VideoParameter.Group6.Index.V_6_3:
            case VideoParameter.Group6.Index.V_6_4_R:
            case VideoParameter.Group6.Index.V_6_4:
            case VideoParameter.Group6.Index.V_6_5_R:
            case VideoParameter.Group6.Index.V_6_5:
            case VideoParameter.Group6.Index.V_6_6_R:
            case VideoParameter.Group6.Index.V_6_6:
            case VideoParameter.Group6.Index.V_6_7_R:
            case VideoParameter.Group6.Index.V_6_7:
            case VideoParameter.Group6.Index.V_6_8_R:
            case VideoParameter.Group6.Index.V_6_8:
            case VideoParameter.Group6.Index.V_6_9_R:
            case VideoParameter.Group6.Index.V_6_9:
            case VideoParameter.Group6.Index.V_6_10_R:
            case VideoParameter.Group6.Index.V_6_10:
            case VideoParameter.Group7.Index.V_7_1_R:
            case VideoParameter.Group7.Index.V_7_1:
            case VideoParameter.Group7.Index.V_7_2_R:
            case VideoParameter.Group7.Index.V_7_2:
            case VideoParameter.Group7.Index.V_7_3_R:
            case VideoParameter.Group7.Index.V_7_3:
            case VideoParameter.Group7.Index.V_7_4_R:
            case VideoParameter.Group7.Index.V_7_4:
            case VideoParameter.Group7.Index.V_7_5_R:
            case VideoParameter.Group7.Index.V_7_5:
            case VideoParameter.Group7.Index.V_7_6_R:
            case VideoParameter.Group7.Index.V_7_6:
            case VideoParameter.Group7.Index.V_7_7_R:
            case VideoParameter.Group7.Index.V_7_7:
            case VideoParameter.Group7.Index.V_7_8_R:
            case VideoParameter.Group7.Index.V_7_8:
            case VideoParameter.Group7.Index.V_7_9_R:
            case VideoParameter.Group7.Index.V_7_9:
            case VideoParameter.Group7.Index.V_7_10_R:
            case VideoParameter.Group7.Index.V_7_10:
                //mTxtDescription.startAnimation(animScaleFadeOut_Desc);
                mTxtDescription.setVisibility(View.INVISIBLE);
                break;
            default:
                mTxtDescription.startAnimation(animScaleFadeOut_Desc);
                mTxtDescription.setVisibility(View.INVISIBLE);
        }
    }

    private void setBriefDescriptionView(int videoIndex, String txt) {
        switch(videoIndex) {
            case VideoParameter.Group1.Index.V_1_1:
            case VideoParameter.Group1.Index.V_1_8:
            case VideoParameter.Group1.Index.V_1_3:
            case VideoParameter.Group1.Index.V_1_6:
            case VideoParameter.Group1.Index.V_1_16:
            case VideoParameter.Group2.Index.V_2_1:
            case VideoParameter.Group2.Index.V_2_2:
            case VideoParameter.Group2.Index.V_2_3:
            case VideoParameter.Group2.Index.V_2_4:
            case VideoParameter.Group2.Index.V_2_6:
            case VideoParameter.Group4.Index.V_4_1:
            case VideoParameter.Group4.Index.V_4_2:
            case VideoParameter.Group4.Index.V_4_3:
            case VideoParameter.Group4.Index.V_4_4:
            case VideoParameter.Group4.Index.V_4_5:
            case VideoParameter.Group4.Index.V_4_6:
            case VideoParameter.Group4.Index.V_4_7:
            case VideoParameter.Group4.Index.V_4_8:
            case VideoParameter.Group4.Index.V_4_9:
            case VideoParameter.Group4.Index.V_4_10:
            case VideoParameter.Group5.Index.V_5_1:
            case VideoParameter.Group5.Index.V_5_2:
            case VideoParameter.Group5.Index.V_5_3:
            case VideoParameter.Group5.Index.V_5_4:
            case VideoParameter.Group5.Index.V_5_5:
            case VideoParameter.Group5.Index.V_5_6:
            case VideoParameter.Group5.Index.V_5_7:
            case VideoParameter.Group5.Index.V_5_8:
            case VideoParameter.Group5.Index.V_5_9:
            case VideoParameter.Group5.Index.V_5_10:
            case VideoParameter.Group6.Index.V_6_1:
            case VideoParameter.Group6.Index.V_6_2:
            case VideoParameter.Group6.Index.V_6_3:
            case VideoParameter.Group6.Index.V_6_4:
            case VideoParameter.Group6.Index.V_6_5:
            case VideoParameter.Group6.Index.V_6_6:
            case VideoParameter.Group6.Index.V_6_7:
            case VideoParameter.Group6.Index.V_6_8:
            case VideoParameter.Group6.Index.V_6_9:
            case VideoParameter.Group6.Index.V_6_10:
            case VideoParameter.Group7.Index.V_7_1:
            case VideoParameter.Group7.Index.V_7_2:
            case VideoParameter.Group7.Index.V_7_3:
            case VideoParameter.Group7.Index.V_7_4:
            case VideoParameter.Group7.Index.V_7_5:
            case VideoParameter.Group7.Index.V_7_6:
            case VideoParameter.Group7.Index.V_7_7:
            case VideoParameter.Group7.Index.V_7_8:
            case VideoParameter.Group7.Index.V_7_9:
            case VideoParameter.Group7.Index.V_7_10:
                mTxtBriefDescription_v_1.setVisibility(View.VISIBLE);
                mTxtBriefDescription_v_1.setText(txt);
                mTxtBriefDescription_v_1.startAnimation(animScaleFadeIn_Desc);
                break;
            default:
                mTxtBriefDescription_v_1.startAnimation(animScaleFadeOut_Desc);
                mTxtBriefDescription_v_1.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 단계별 평가율 TextView의 setting.
     * @param rating 현재 단계의 평가.
     */
    private void setRatingTextView(int rating) {
        switch(rating) {
            case 0:
            case 1:
            case 2:
                return;
            case 3:
                mTxtRating.setTextAppearance(this, R.style.TextGood);
                mTxtRating.setText(getString(R.string.rating_good));
                break;
            case 4:
                mTxtRating.setTextAppearance(this, R.style.TextGreat);
                mTxtRating.setText(getString(R.string.rating_great));
                break;
            case 5:
                mTxtRating.setTextAppearance(this, R.style.TextPerfect);
                mTxtRating.setText(getString(R.string.rating_perfect));
                break;
        }
    }

    /**
     * 단계별 평가율 TextView의 Text Setting
     * @param rating 단계별 평가율
     */
    private void setGradientText(int rating) {
        switch(rating) {
            case 0:
            case 1:
            case 2:
                return;
            case 3:
                mTxtRating.getPaint().setShader(new LinearGradient(0, 0, getDpToPixel(100), 0, getResources().getColor(R.color.gradient_start_good), getResources().getColor(R.color.gradient_end_good), Shader.TileMode.CLAMP));
                break;
            case 4:
                mTxtRating.getPaint().setShader(new LinearGradient(0, 0, getDpToPixel(100), 0, getResources().getColor(R.color.gradient_start_great), getResources().getColor(R.color.gradient_end_great), Shader.TileMode.CLAMP));
                break;
            case 5:
                mTxtRating.getPaint().setShader(new LinearGradient(0, 0, getDpToPixel(100), 0, getResources().getColor(R.color.gradient_start_perfect), getResources().getColor(R.color.gradient_end_perfect), Shader.TileMode.CLAMP));
                break;
        }
    }

    /**
     * pixel을 dp단위로 변환
     * @param pixel 변환하고자 하는 pixel 값
     * @return 변환된 dp값
     */
    private int getPixelToDp(int pixel) {
        return (int)(pixel / (getResources().getDisplayMetrics().densityDpi / 160f));
    }

    /**
     * dp를 pixel단위로 변환
     * @param DP 변환하고자 하는 dp 값
     * @return 변환된 pixel값
     */
    private int getDpToPixel(int DP) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, getResources().getDisplayMetrics()));
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

    /**
     * 입력된 강도를 View형태로 전환. 강도에 따라 표시되는 View가 달라짐. Debug
     * @param phase 행동 강도
     * @return true:성공, false:실패.(View 초기화가 이루어지지 않음. 1~5단계의 범위를 벗어남)
     */
    private boolean toViewPhaseD(int phase) {
        if(mTxtVAccuracy1D == null || mTxtVAccuracy2D == null || mTxtVAccuracy3D == null
                || mTxtVAccuracy4D == null || mTxtVAccuracy5D == null)
            return false;
        switch(phase) {
            case 0:
                mTxtVAccuracy1D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy2D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy3D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy4D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5D.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mTxtVAccuracy1D.setVisibility(View.VISIBLE);
                mTxtVAccuracy2D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy3D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy4D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5D.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mTxtVAccuracy1D.setVisibility(View.VISIBLE);
                mTxtVAccuracy2D.setVisibility(View.VISIBLE);
                mTxtVAccuracy3D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy4D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5D.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mTxtVAccuracy1D.setVisibility(View.VISIBLE);
                mTxtVAccuracy2D.setVisibility(View.VISIBLE);
                mTxtVAccuracy3D.setVisibility(View.VISIBLE);
                mTxtVAccuracy4D.setVisibility(View.INVISIBLE);
                mTxtVAccuracy5D.setVisibility(View.INVISIBLE);
                break;
            case 4:
                mTxtVAccuracy1D.setVisibility(View.VISIBLE);
                mTxtVAccuracy2D.setVisibility(View.VISIBLE);
                mTxtVAccuracy3D.setVisibility(View.VISIBLE);
                mTxtVAccuracy4D.setVisibility(View.VISIBLE);
                mTxtVAccuracy5D.setVisibility(View.INVISIBLE);
                break;
            case 5:
                mTxtVAccuracy1D.setVisibility(View.VISIBLE);
                mTxtVAccuracy2D.setVisibility(View.VISIBLE);
                mTxtVAccuracy3D.setVisibility(View.VISIBLE);
                mTxtVAccuracy4D.setVisibility(View.VISIBLE);
                mTxtVAccuracy5D.setVisibility(View.VISIBLE);
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Intent intent = getIntent();
        int id = intent.getIntExtra(VideoManager.VIDEO_NAME, 0);
        int setID=0;
        if(id == R.raw.play1)
            setID = VideoManager.SET_VIDEO_ID_1;
        else if(id == R.raw.play2)
            setID = VideoManager.SET_VIDEO_ID_2;
        else if(id == R.raw.play3)
            setID = VideoManager.SET_VIDEO_ID_3;
        else if(id == R.raw.play1_hong)
            setID = VideoManager.SET_VIDEO_ID_4;
        else if(id == R.raw.play2_hong)
            setID = VideoManager.SET_VIDEO_ID_5;
        else if(id == R.raw.play1_lee)
            setID = VideoManager.SET_VIDEO_ID_6;
        else if(id == R.raw.play2_lee)
            setID = VideoManager.SET_VIDEO_ID_7;

        if (setID == 0)
            return;

        if (mPlayer == null)
            return;

        setChkDebugPlay = intent.getBooleanExtra(VideoManager.CHK_DEBUG_PLAY, true);
        //if(!resume)
        startSec = intent.getIntExtra(V_START_SEC, 0);

        /** 동영상 정보 등록 **/
        MwApiWrapper.WrapperVideoManager.setRecordName(intent.getStringExtra(VideoManager.FILE_NAME));
        MwApiWrapper.WrapperVideoManager.setVideoID(setID);
        MwApiWrapper.WrapperVideoManager.setChkSave(intent.getBooleanExtra(VideoManager.CHK_SAVE, true));
		/*
		if(isDualMode)
			setChkDebugPlay = false;
		MwApiWrapper.WrapperVideoManager.setChkDebugPlay(setChkDebugPlay);
		*/
        MwApiWrapper.WrapperVideoManager.setDualMode(isDualMode);
        if(isDualMode)
            findViewById(R.id.layout_debug_play).setVisibility(View.VISIBLE);

        /** 재생 준비 **/
        try {
            mPlayer.setDataSource(this, Uri.parse("android.resource://com.coach.test.myapplication/raw/" + id));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.setDisplay(mSHolder);
        try {
            mPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                /** 동영상이 끝나면 수행 **/
                SoundPoolManager.playEnd(VideoPlayActivity.this);
                stop();
                finish();
            }
        });
        //mPlayer.setVolume(0, 0);
        mPlayer.seekTo(startSec);
        Log.d(tag,"seekto:"+startSec);

        /** 재생 **/
        mPlayer.start();
        MwApiWrapper.WrapperVideoManager.play();
        //MwApiWrapper.WrapperVideoManager.play2();
        start();
    }

    /**
     * 영상의 재생중인 position을 얻어옴. 초단위.
     * @return 영상 position. 초단위.
     */
    private int getCurrentPosition() {
        if(mPlayer != null) {
            // test
            //if(( mPlayer.getCurrentPosition()/1000) >= 100)
            //	mPlayer.seekTo(0);
            return mPlayer.getCurrentPosition()/1000;
        } else
            return 0;
    }

    /** 종료될 시, 영상 객체들 release **/
    private void exitPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        if (mMainTimer != null) {
            mMainTimer.cancel();
            mMainTimer = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    */
    @Override
    protected void onPause() {
        super.onPause();
        //startSec = getCurrentPosition();
        MwApiWrapper.WrapperBluetoothManager.unregistCallback();
        VideoManager.unregistCallback();
        //Log.d(tag,"pause:"+startSec);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MwApiWrapper.WrapperVideoManager.end();
        MwApiWrapper.WrapperBluetoothManager.stop();
        exitPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MwApiWrapper.WrapperVideoManager.setPlayMode(VideoManager.MODE_RESUME);
        //resume = true;
        //Log.d(tag,"resume");
    }
}

package com.coach.test.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;


import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

//import kr.co.greencomm.base.CoachBaseActivity;
import com.coach.test.myapplication.R;
import com.coach.test.coachmw.datastructure.DeviceInformation;
import com.coach.test.coachmw.manager.BluetoothManager;
import com.coach.test.coachmw.manager.VideoManager;
import com.coach.test.coachmw.protocol.IBluetooth;
import com.coach.test.coachmw.protocol.IViewComment;
import com.coach.test.myapplication.manager.SoundPoolManager;
import com.coach.test.myapplication.util.MwApiWrapper;
import com.coach.test.myapplication.util.SetInfoView;
import com.coach.test.myapplication.util.TimerTextView;



/**
 * Created by Administrator on 2017-02-20.
 */

public class ActivityVideo extends WActivity
        implements IViewComment
{
    // 블루투스 장치의 연결을 대기하는 최대 시간 (msec)
    private static final int BLUETOOTH_CONNECT_WAIT_LIMIT = 6000;
    private static final int READY_VIDEO_MEDIA = 0x01;
    private static final int READY_BLUETOOTH_DEVICE = 0x02;
    private static final int READY_ALL = 0x03;

    // 상단 하단 좌측, 최하단에 표시되는 각종 텍스트 박스의 표시될때의 백그라운드 컬러
    private static int Information_Text_Back_Color = Color.rgb(0x33, 0x33, 0x33);

    // 미들웨어 블루투스, 비디오 관련 메니져
    private BluetoothManager m_bluetooth;
    //private VideoManager m_video;

    /** Interface **/
    private IBluetooth mIBluetooth;

    // 비디오 재생 뷰
    private VideoView videoView;
    // 비디오 파일 플레이 준비 또는 블루투스 장치 연결 대기
    private ProgressBar progressBar;

    // 하단에 표시되는 각종 정보 표시 영역이 모두 포함된 레이아웃 한꺼번에 안보이게 할때 사용
    private LinearLayout m_infoView;

    // 세트 종합 정보 표시 관련
    private SetInfoView m_setInfo;

    // 포인트, 칼로리, 심박수, 운동횟수 등 텍스트 표시 뷰
    private TextView m_txt_Point;
    //private TextView m_txt_Calorie;
    private TextView m_txt_HeartRate;
    private TextView m_txt_Count;
    private TextView m_txt_warning;
    // 정확도 관련 바 그래프 표시를 위한 이미지 뷰
    private ImageView m_bar_graph;

    // 상단 운동 제목, 하단 좌측의 정확도, 최하단의 각종 멘트 표시 텍스트 뷰
    private TimerTextView m_timer_title;
    private TimerTextView m_timer_info;
    private TimerTextView m_timer_Acc;

    // 플레이를 요청한 프로그램 번호
    private int m_progCode;
    // 현재 진행중인 재생 시간 (초)
    private int m_sec = 0;
    private static int startSec = 0;

    // 비디오 화면 터치 주기를 확보하기 위한 플래그
    private boolean m_videoBeginTouch = false;
    // 비디오 재생 쓰레드 지속 컨디션 플레그
    private boolean m_loopFlag = false;
    // 비디오 재생, 중지 상태 플래그
    private boolean m_isPlaying = false;
    // 블루투스 장치 연결 요청 시간 (msec)
    private long m_request_time = 0;
    // 각종 상태를 종합하여 비디오 재생 관련 상황 판다.
    private boolean m_status;

    public static final String V_START_SEC = "com.coach.test.myapplication.start_sec";
    public static final String BLE_MAC = "com.coach.test.myapplication.ble_mac";
    public static final String RecordName = "com.coach.test.myapplication.recordName";
    public static String FileName;

    private boolean setSave;

    View capView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);

        // 상위 액티비티가 전달한 코드를 읽는다.
        Intent intent = getIntent();
        String bleAddr = intent.getStringExtra(BLE_MAC);
        FileName = intent.getStringExtra(RecordName);
        setSave = intent.getBooleanExtra(VideoManager.CHK_SAVE, false);
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
        m_progCode = setID;
        /*m_progCode = intent.getExtras().getInt("ProgCode", 0);
        // 프로그램 속의 코스 번호 (준비 운동일때만 필요)
        int m_subCode = intent.getExtras().getInt("SubCode", 0);*/

        // 비디오 재생 중에 화면이 항상 켜져 있도록 한다.
        startSec = intent.getIntExtra(V_START_SEC, 0);
        //MwApiWrapper.WrapperVideoManager.setRecordName(intent.getStringExtra(VideoManager.FILE_NAME));
        //MwApiWrapper.WrapperVideoManager.setVideoID(setID);
        //MwApiWrapper.WrapperVideoManager.setChkSave(intent.getBooleanExtra(VideoManager.CHK_SAVE, true));
        //m_video.setChkSave(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 필요한 모든 위젯을 확보하고 값을 초기화 한다.
        getWidgets();

        // 비디오 파일 명칭
        /*String fileName = "prog_" + String.format("%03d", m_progCode) + (m_progCode == 1 ? "_" + String.format("%02d", m_subCode) : "") + ".mp4";
        Log.i("ActivityVideo","FileName: " + fileName);

        // 준비운동 이라면 비디오 뷰에 인터넷 주소를 제공하고
        if (m_progCode == 1) {
            Uri uri = Uri.parse("http://222.236.47.55/video/" + fileName);
            Log.i("ActivityVideo","URI: " + uri.toString());
            videoView.setVideoURI(uri);
        }
        // 준비운동이 아니면 비디오 뷰에 다운로드된 파일을 제공한다.
        else {*/
            //File path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            //File file = new File(path, fileName);
            //videoView.setVideoPath(file.getAbsolutePath());
            videoView.setVideoURI(Uri.parse("android.resource://com.coach.test.myapplication/raw/" + id));
        //}

        // 종합 상태값을 초기화 한다.
        m_status = false;
        // 준비하는 과정을 미리함
        videoView.requestFocus();

        // 동영상이 재생준비가 완료되엇을떄를 알수있는 리스너 (실제 웹에서 영상을 다운받아 출력할때 많이 사용됨)
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // 동영상 재생준비가 완료된후 호출되는 메서드
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 동영상 재생 준비가 완료 되었다면 블루투스 장치 연결 상태를 확인하여
                // 비디오 재생을 시작할수 있도록 시작 메서드를 호출한다.
                //m_status |= READY_VIDEO_MEDIA;
                m_status = true;
                checkStart();
            }
        });

        // 비디오 화면 클릭 이벤트 처리
        // 재생중 클릭하면 일시정지, 재생을 반복한다.
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (m_videoBeginTouch == false) {
                    m_videoBeginTouch = true;
                    if (m_isPlaying) {
                        m_isPlaying = false;
                        videoView.pause();
                        m_infoView.setVisibility(View.GONE);
                        // 비디오 메니져에게 일시 중지 상황을 알림
                        //if (m_video != null) {
                        MwApiWrapper.WrapperVideoManager.setPlaying(m_isPlaying);
                        //}
                    }
                    else {
                        m_isPlaying = true;
                        videoView.start();
                        // 비디오 메니져에게 재생 상황을 알림
                        //if (m_video != null) {
                            MwApiWrapper.WrapperVideoManager.setPlaying(m_isPlaying);
                        //}
                    }
                }
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        m_videoBeginTouch = false;
                    }
                }, 100);
                return true;
            }
        });

        // 동영상 재생이 완료된걸 알수있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            // 동영상 재생이 완료된후 호출되는 메서드
            public void onCompletion(MediaPlayer player) {
                // 동영상이 끝에 도달한 경우 정상으로 종료 처리
                SoundPoolManager.playEnd(ActivityVideo.this);
                closeActivity(RESULT_OK);
            }
        });

        if (m_progCode > 1) {
            Log.i("ActivityVideo","BluetoothManager.getCountGattClient()"+BluetoothManager.getCountGattClient());
            if (BluetoothManager.getCountGattClient() == 0) {
                BluetoothManager.registCallback(m_bluetooth_callback);
                // 블루투스 메니져의 인스턴스를 얻는다.
                m_bluetooth = BluetoothManager.getInstance(this);

                // 밴드 연결을 요청한다.
                //m_bluetooth.connect(DB_User.getDeviceAddress());
                m_bluetooth.connect(bleAddr);
                m_request_time = System.currentTimeMillis();
            }
        }

        if (m_progCode == 1) {
            startVideo();
        }
        else {
            loopThread.start();
        }
    }

    private IBluetooth m_bluetooth_callback = new IBluetooth() {
        @Override
        public void connectionState(int i) {
            //if (i == BluetoothManager.CONNECTION_COMPLETE) {
            //Log.i("connectionState","i="+i);
            if(i == BluetoothManager.CONNECTION_COMPLETE) { // 연결됨.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_status = true;
                        /*String devName = DB_User.getDeviceName();
                        if (devName == null || devName.isEmpty()) {
                            // 여기에서 정보 업데이트
                            m_bluetooth = BluetoothManager.getInstance(ActivityVideo.this);
                            String[] infos = m_bluetooth.getDeviceInformation();
                            if (infos.length > 1) {
                                DB_User.setDeviceAddress(infos[1]);
                            }
                            DB_User.setDeviceName(infos[0]);
                            DB_User.runUserQuery(QueryCode.SetDevice, null);
                        }*/

                        //SoundPoolManager.playBeep(ActivityVideo.this);
                        //alert(R.string.alert_disconnect, BluetoothActivity.class);
                        //MwApiWrapper.WrapperBluetoothManager.disconnect();
                        //vib();
                        checkStart();
                    }
                });
            }
        }
        @Override
        public void changedScanList(ArrayList<DeviceInformation> arrayList) {}
    };

    private void startVideo() {
        progressBar.setVisibility(View.GONE);
        videoView.seekTo(startSec);
        videoView.start();
        m_isPlaying = true;
    }

    private void checkStart() {
        //Log.i("checkStart","m_status="+m_status);
        if (m_status) {
            // 블루투스 운동 모드 시작
            m_bluetooth = BluetoothManager.getInstance(this);
            m_bluetooth.play();
            // 비디오 메니져의 인스턴스를 얻는다.
            //m_video = VideoManager.getInstance();
            VideoManager.registCallback(this);
            //언어 설정
            MwApiWrapper.WrapperVideoManager.setLocale(Locale.getDefault().getLanguage());
            //까지
            MwApiWrapper.WrapperVideoManager.setRecordName(FileName);
            //m_video.setChkSave(true);
            MwApiWrapper.WrapperVideoManager.setPlayMode(VideoManager.MODE_NEW_START);
            MwApiWrapper.WrapperVideoManager.setVideoID(m_progCode);

            MwApiWrapper.WrapperVideoManager.setChkSave(setSave);

            MwApiWrapper.WrapperVideoManager.play();

            startVideo();
        }
    }

    private void closeActivity(int resultCode) {
        try {
            m_loopFlag = false;
            loopThread.join();
        }
        catch (Exception e) {}

        if (m_progCode > 1) {
            VideoManager.unregistCallback();
            //if (m_video != null) {
                MwApiWrapper.WrapperVideoManager.end();
            //}
            //BluetoothManager.unregistCallback();
            if (m_bluetooth != null) {
                if (BluetoothManager.getCountGattClient() > 0) {
                    m_bluetooth.stop();
                    //m_bluetooth.disconnect();
                }
            }

        }

        Intent intent = getIntent();
        Activity parent = getParent();
        if (parent == null) {
            setResult(resultCode, intent);
        }
        else {
            parent.setResult(resultCode, intent);
        }
        m_isPlaying = false;
        finish();
    }

    /**
     * 화면 표시 등을 위해 각 위젯들을 가져오고
     * 내용을 초기화 한다.
     */
    private void getWidgets() {
        Resources res = getResources();

        videoView = (VideoView) findViewById(R.id.VideoView);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);

        m_infoView = (LinearLayout) findViewById(R.id.info_view);
        m_setInfo = new SetInfoView (findViewById(R.id.set_view));

        m_txt_Point = (TextView) findViewById(R.id.txt_pts);
        //m_txt_Calorie = (TextView) findViewById(R.id.txt_cal);
        m_txt_HeartRate = (TextView) findViewById(R.id.txt_heart);
        m_txt_Count = (TextView) findViewById(R.id.txt_cnt);

        m_txt_warning = (TextView) findViewById(R.id.txt_acc_warning);
        m_txt_warning.setText("");
        m_txt_warning.setBackgroundColor(0);

        m_timer_title = new TimerTextView((TextView) findViewById(R.id.txt_title), 2000, Information_Text_Back_Color);
        m_timer_info = new TimerTextView((TextView) findViewById(R.id.txt_info), 2000, Information_Text_Back_Color);
        m_timer_Acc = new TimerTextView((TextView) findViewById(R.id.txt_acc), 2000, Information_Text_Back_Color);

        m_bar_graph = (ImageView) findViewById(R.id.bar_graph);

        m_txt_Point.setText("0");
        //m_txt_Calorie.setText("0");
        m_txt_HeartRate.setText("0");
        m_txt_Count.setText("0");

        m_infoView.setVisibility(View.GONE);
        m_setInfo.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        closeActivity(RESULT_CANCELED);
    }

    private Handler myHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            // 비디오 시작이 되지 않고 일정 시간 이상 지나면 중단 처리
            /*if (m_loopFlag == true && m_status != 0x03) {
                if (m_request_time + BLUETOOTH_CONNECT_WAIT_LIMIT < System.currentTimeMillis()) {
                    // 종료시킨다.
                    m_loopFlag = false;
                    try {
                        loopThread.join();
                    }
                    catch (Exception e) {}
                    if (m_status == 0x01) {
                        displayMsg(R.string.not_connect_band, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                closeActivity(RESULT_CANCELED);
                            }
                        });
                    }
                    else {
                        displayMsg(R.string.not_prepare_video, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                closeActivity(RESULT_CANCELED);
                            }
                        });
                    }
                }
            }*/
            //else {
                // 비디오 재생 시간을 1초 간격으로 미들웨어에 제공
                if (m_isPlaying) {
                    int pos = videoView.getCurrentPosition() / 1000;
                    int posLast = videoView.getDuration()/1000;
                    if (m_sec != pos) {
                        m_sec = pos;
                        MwApiWrapper.WrapperVideoManager.setCurrentPosition(m_sec);
                        if (m_sec == 114) {
                            m_setInfo.TakeScreen();
                        }
                        //Log.i("ActivityVideo","SetPosition: " + m_sec + "/" + posLast);
                        // UI가 표시되지 않아도 되는 시간인지 확인하여 끈다.
                        if (m_progCode > 1) {
                            m_infoView.setVisibility(MwApiWrapper.WrapperVideoManager.isDisableUI() ? View.GONE : View.VISIBLE);
                        }
                    }
                }
            //}

            // 화면에 표시되는 텍스트 정보들이 일정 시간 이후 자동으로 꺼짐 처리
            m_timer_title.hideCheck();
            m_timer_info.hideCheck();
            m_timer_Acc.hideCheck();

            // 세트 종합 정보 표시 처리
            m_setInfo.check();

        }
    };



    Thread loopThread = new Thread(new Runnable()
    {
        public void run()
        {
            try {
                m_loopFlag = true;
                while(m_loopFlag) {
                    myHandler.sendMessage(myHandler.obtainMessage());
                    Thread.sleep(50);
                }
            } catch (Throwable t) {
                // Exit Thread
            }
        }
    });

    private float m_org_Point = 0;
    private float m_org_Calorie = 0;
    private int m_org_HeartRate = 0;
    private int m_org_Count = 0;
    private int m_org_Accuracy = 0;

    void displayPoint(int value) {
        if (m_org_Point == value) return;
        m_org_Point = value;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String arg = String.valueOf(m_org_Point);
                Log.i("ActivityVideo","전체 퍼센테이지: " + arg);
                m_txt_Point.setText(arg);
            }
        });
    }

    void displayCalorie(float value) {
        /*if (m_org_Calorie == value) return;
        m_org_Calorie = value;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String arg = String.valueOf(m_org_Calorie);
                Log.i("ActivityVideo","칼로리소비: " + arg);
                m_txt_Calorie.setText(arg);
            }
        });*/
    }

    void displayHeartRate(int value) {
        if (m_org_HeartRate == value) return;
        m_org_HeartRate = value;
        if (m_org_HeartRate>=100){
            m_org_HeartRate = 100;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String arg = String.valueOf(m_org_HeartRate);
                //Log.i("ActivityVideo","심박수: " + arg);
                m_txt_HeartRate.setText(arg);
                /*if (m_org_HeartRate >= 80) {
                    m_txt_warning.setText("Warning!!");
                    m_txt_warning.setBackgroundColor(Color.WHITE);
                }
                else {
                    m_txt_warning.setText("");
                    m_txt_warning.setBackgroundColor(0);
                }*/
            }
        });
    }

    void displayCount(int value) {
        if (m_org_Count == value) return;
        m_org_Count = value;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String arg = String.valueOf(m_org_Count);
                Log.i("ActivityVideo","수행횟수: " + arg);
                m_txt_Count.setText(arg);
            }
        });
    }

    void displayAccuracyBar(int value) {
        if (m_org_Accuracy == value) return;
        m_org_Accuracy = value;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String arg = String.valueOf(m_org_Accuracy);
                Log.i("ActivityVideo","정확도: " + arg);
                if (m_org_Accuracy <= 0) {
                    m_bar_graph.setImageResource(R.drawable.video_precision_00);
                }
                else if (m_org_Accuracy <= 20) {
                    m_bar_graph.setImageResource(R.drawable.video_precision_01);
                }
                else if (m_org_Accuracy <= 40) {
                    m_bar_graph.setImageResource(R.drawable.video_precision_02);
                }
                else if (m_org_Accuracy <= 60) {
                    m_bar_graph.setImageResource(R.drawable.video_precision_03);
                }
                else if (m_org_Accuracy <= 80) {
                    m_bar_graph.setImageResource(R.drawable.video_precision_04);
                }
                else {
                    m_bar_graph.setImageResource(R.drawable.video_precision_05);
                }
            }
        });
    }

    void displayDescription(final String msg) {
        Log.i("ActivityVideo","표시정보: " + msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_timer_info.show(msg);
            }
        });
    }

    void displayTitle(final String msg) {
        Log.i("ActivityVideo","제목표시: " + msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_timer_title.show(msg);
            }
        });
    }

    void displayAccuracy(final String msg) {
        Log.i("ActivityVideo","정확도텍스트표시: " + msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_timer_Acc.show(msg);
            }
        });
    }

    @Override
    public void onKISTOutput_Point(int v, int idx) {
        displayPoint(v);
    }

    @Override
    public void onKISTOutput_PointD(int v, int idx) {
    }

    @Override
    public void onKISTOutput_Calorie(float v, int idx) {
        displayCalorie(v);
    }

    @Override
    public void onKISTOutput_CalorieD(float v, int idx) {
    }

    @Override
    public void onHeartRateCompared(int v) {
        //displayHeartRate(v);
    }

    @Override
    public void onKISTOutput_Accuracy(int v, int idx) {
        displayAccuracyBar(v);
        displayHeartRate(v);
    }

    @Override
    public void onKISTOutput_AccuracyD(int v, int idx) {
    }

    @Override
    public void onKISTOutput_Count(int v, int idx) {
        displayCount(v);
    }

    @Override
    public void onKISTOutput_CountD(int i, int i1) {
    }

    @Override
    public void onInstruction(String s) {
        Log.i("ActivityVideo","Instruction: " + s);
        displayDescription(s);
    }

    @Override
    public void onWarnning(String s) {
        Log.i("ActivityVideo","Warnning: " + s);
        displayDescription(s);
    }

    @Override
    public void onWarnningD(String s) {
        Log.i("ActivityVideo","Warnning: " + s);
        displayDescription(s);
    }

    @Override
    public void onLaptime(String s) {
        Log.i("ActivityVideo","Laptime: " + s);
        displayDescription(s);
    }

    @Override
    public void onCommentSection(final long displayTime, final int point, final int count_percent, final int accuracy_percent, final String desc) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_setInfo.show(displayTime, point, count_percent, accuracy_percent, desc);
            }
        });
    }

    @Override
    public void onCommentR(String s) {
        Log.i("ActivityVideo","CommentR: " + s);
        displayAccuracy(s);
    }

    @Override
    public void onDescription(int idx, String s) {
        Log.i("ActivityVideo","Description: " + s);
        displayDescription(s);
    }

    @Override
    public void onBriefDescription(int idx, String s) {
        Log.i("ActivityVideo","BriefDescription: " + s);
        displayTitle(s);
    }
}

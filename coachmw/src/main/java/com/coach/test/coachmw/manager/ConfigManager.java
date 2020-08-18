package com.coach.test.coachmw.manager;

import com.coach.test.coachmw.database.DBContactHelper;
import com.coach.test.coachmw.datastructure.DataBase;
import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2017-02-17.
 */

/**
 * M/W에 필요한 기본적인 정보를 관리하며, 일부 계산을 담당합니다.
 */
public final class ConfigManager extends DBContactHelper {
    private static final String tag = "ConfigManager";
    // config 객체
    private static ConfigManager mConfig = null;
    //private PreferencesManager mPre;

    public static final int SEX_MALE = 1;
    public static final int SEX_FEMALE = 2;

    public static final int COUNTRY_NORTH_AMERICA = 1;
    public static final int COUNTRY_JAPAN = 2;
    public static final int COUNTRY_KOREA = 3;


    /**
     * 성공.
     */
    public static final int SUCCESS = 0;
    /**
     * 실패.
     */
    public static final int FAIL = 1;
    /**
     * null 혹은 0인 데이터가 들어왔습니다.
     */
    public static final int DO_NOT_USE_NULLDATA = 2;

    /**
     * EngineConfiguration 의 생성자
     * @param context Application의 context.
     */
    private ConfigManager(Context context) {
        super(context);
		/*
		DBContactHelper.initInstance(context);
		if (mDBHelper == null) {
			mDBHelper = DBContactHelper.getInstance();
		}
		*/
        DataBase.initInstance();
        BluetoothManager.initInstance();

        //mPre = new PreferencesManager(context);
        createAllTable();
    }
    public static void initInstance(Context context) {
        if(mConfig == null) {
            mConfig = new ConfigManager(context);
            VideoManager.initInstance(context);
        }
    }
    public static ConfigManager getInstance() {
        return mConfig;
    }

    public int setUserProfile(int sex, int age, int height, int language) {
        if(age < 1 || height < 1)
            return DO_NOT_USE_NULLDATA;
        if(language < 1 && language > 3 || sex < 1 && sex > 2)
            return DO_NOT_USE_NULLDATA;

        return addUserProfile(sex, age, height, language, 0/*reserved_1*/, 0/*reserved_2*/);
    }
    public DataBase.UserProfile getUserProfile() {
        return super.getUserProfile();
    }

    public int setUserWeightProfile(int weight, int goal_weight, int diet_period) {
        if(weight < 1 || goal_weight < 1 || diet_period < 1)
            return DO_NOT_USE_NULLDATA;

        return addUserWeightProfile(weight, goal_weight, diet_period);
    }
    public DataBase.UserWeightProfile getUserWeightProfile() {
        return super.getUserWeightProfile();
    }

	/*public int getLevel() {
		return mPre.getUserReference();
	}

	public void setLevel(int level) {
		mPre.setUserReference(level);
	}*/

    /**
     * 사용자의 운동의 데이터를 db에 추가.
     * @param time
     * @param video_idx
     * @param consume_calorie
     * @param count
     * @param accuracy
     * @param heartrate
     * @return
     */
    protected int addUserExerciseData(int video_idx, int video_full_count, int exer_idx, int exer_count, long start_time, long end_time,
                                      int consume_calorie, int count, int count_percent, int perfect_count, int min_accuracy, int max_accuracy,
                                      int avg_accuracy , int min_hr, int max_hr , int avg_hr, int cmp_hr,int point, int reserved_1, int reserved_2) {

        if (count_percent > 100) count_percent = 100;
        if (max_accuracy > 100) max_accuracy = 100;
        if (avg_accuracy > 100) avg_accuracy = 100;

        return super.addUserExerciseData(video_idx, video_full_count, exer_idx, exer_count, start_time, end_time, consume_calorie,
                count, count_percent, perfect_count, min_accuracy, max_accuracy, avg_accuracy, min_hr, max_hr,
                avg_hr, cmp_hr, point, reserved_1, reserved_2);
    }


    /**
     * 사용자의 운동 데이터를 db에서 전부 삭제한다.
     * @return
     */
    public int deleteUserExerciseData() {
        return super.deleteUserExerciseData();
    }

    /**
     * 해당 startTime의 운동 데이터를 db에서 삭제한다.
     * @return
     */
    protected int deleteUserExerciseData(Long startTime) {
        return super.deleteUserExerciseData(startTime);
    }

    /**
     * 사용자의 운동 데이터를 db에서 전부 받아온다.
     * @return
     */
    public DataBase.UserExerciseData[] getUserExerciseData() {
        return super.getUserExerciseData();
    }


    /**
     * 기본 사용자 정보 DB가 비어 있는지 확인합니다. 만약, 비어있다면 Profile, DietPlan, DietPeriod의 정보를 입력주어야 합니다.
     * @return true:DB is empty. false:DB is full.
     */
    public boolean isEmptyUserData() {
        if(getUserProfile() == null)
            return true;
        if(getUserWeightProfile() == null)
            return true;

        return false;
    }



    /**
     * 베터리 정보가 비었습니다.
     */
    public static final byte STATE_BATTERY_NULL = 0;
    /**
     * 정상 상태입니다. (방전 상태).
     */
    public static final byte STATE_BATTERY_NORMAL = 1;
    /**
     * 충전 중 상태입니다.
     */
    public static final byte STATE_BATTERY_CHARGED = 2;
    /**
     * 충전 완료 상태입니다.
     */
    public static final byte STATE_BATTERY_CHARGE_COMPLETE = 3;
    private static byte[] batterylevel=null;
    protected synchronized static void setBattery(byte[] battery) {
		/*if(batterylevel == null) {
			batterylevel = new byte[2];
		}*/
        //System.arraycopy(battery, 0, batterylevel, 0, battery.length);
        // test 용으로는 adc가 들어간 4바이트가 올라온다.
        batterylevel = battery;
        if(batterylevel[0] != STATE_BATTERY_NORMAL) {
            batterylevel[1] = 0;
        }
    }
    /**
     * 현재 베터리의 상태 정보를 byte array 형태로 반환한다. 한번이라도 베터리 정보를 받은적이 없으면, 첫 번째 배열에 0을 반환한다.
     * 충전중, 충전완료 상태는 남은 용량 정보가 0으로 표시된다.
     * @return 베터리의 상태 정보 배열. (byte[0] = 0:정보 null 1:미충전,2:충전중,3:충전완료 .byte[1] = 남은 용량(%).)
     */
    public static byte[] getBattery() {
        // 충전중, 충전완료, 남은 용량.
        if(batterylevel != null) {
            return batterylevel;
        } else {
			/*byte[] ret = new byte[2];
			ret[0] = STATE_BATTERY_NULL;
			ret[1] = 0;*/
            // test
            byte[] ret = new byte[5];
            ret[0] = STATE_BATTERY_NULL;
            ret[1] = 0;
            ret[2] = 0;
            ret[3] = 0;
            ret[4] = 0; // 심박수 떄문에 1바이트 추가함.
            return ret;
        }
    }
}


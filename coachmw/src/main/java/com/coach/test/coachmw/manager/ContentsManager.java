package com.coach.test.coachmw.manager;

import com.coach.test.coachmw.datastructure.DataBase;
import android.content.Context;

/**
 * Created by Administrator on 2017-02-17.
 */

/**
 * 앱 내부에 무엇인가 출력해야하는 내용을 담당하는 클래스.
 * @author user
 *
 */
public class ContentsManager {
    /** Variable **/
    //private PreferencesManager mPre;
    //private Context mContext;
    private ConfigManager mConfig;
    private float INLAB_RATE = 7.7f;

    public ContentsManager(Context context) {
        //mContext = context;
        mConfig = ConfigManager.getInstance();
        //mPre = new PreferencesManager(context);
    }

    /**
     * 일일 소비 칼로리를 float 형태로 반환한다. 이 메서드를 사용하기 전에 사용자의 Profile 정보, DietPeriod 정보가 입력되어 있어야 한다.
     * @return 일일 소비 칼로리 반환.
     */
    public int getCalorieConsumeDaily() {
        //DataBase.UserProfile profile = mConfig.getUserProfile();
        DataBase.UserWeightProfile wProfile = mConfig.getUserWeightProfile();

        return getCalorieConsumeDaily(wProfile.getWeight(), wProfile.getGoal_weight(), wProfile.getDiet_period());
        //return convertPoint(getCalorieConsumeDaily(profile.getWeight(), profile.getGoalWeight(), dPeriod.getDietPeriod()));
    }

    private int getCalorieConsumeDaily(float weight, float goalWeight,
                                       int dietPeriod) {
        if(weight - goalWeight == 0)
            return 0;
        if(dietPeriod == 0)
            return 0;
        return (int)(((weight - goalWeight)* 1000 * INLAB_RATE) / (dietPeriod*7));
    }

	/*
	public int getCalorieTotalConsumeDaily() {
		UserExerciseData[] uDB = mConfig.getUserExerciseData();
		if(uDB == null)
			return 0;

		Calendar mCal = Calendar.getInstance();
		int day = mCal.get(Calendar.DAY_OF_MONTH);
		int month = mCal.get(Calendar.MONTH);

		long dbTime = uDB[uDB.length-1].getTime();
		mCal.setTimeInMillis(dbTime);
		int dbDay = mCal.get(Calendar.DAY_OF_MONTH);
		int dbMonth = mCal.get(Calendar.MONTH);

		if(dbDay != day)
			return 0;
		if(dbMonth != month)
			return 0;

		int total_Cal=0;
		for(UserExerciseData u : uDB) {
			mCal.setTimeInMillis(u.getTime());
			int uDay = mCal.get(Calendar.DAY_OF_MONTH);
			int uMonth = mCal.get(Calendar.MONTH);
			if(uMonth != month || uDay != day)
				continue;

			total_Cal += u.getConsume_calorie();
		}

		return total_Cal;
	}

	public int getAccuracyDaily() {
		UserExerciseData[] uDB = mConfig.getUserExerciseData();
		if(uDB == null)
			return 0;

		Calendar mCal = Calendar.getInstance();
		int day = mCal.get(Calendar.DAY_OF_MONTH);
		int month = mCal.get(Calendar.MONTH);

		long dbTime = uDB[uDB.length-1].getTime();
		mCal.setTimeInMillis(dbTime);
		int dbDay = mCal.get(Calendar.DAY_OF_MONTH);
		int dbMonth = mCal.get(Calendar.MONTH);

		if(dbDay != day)
			return 0;
		if(dbMonth != month)
			return 0;

		int idx=0;
		int ave_accuracy=0;
		for(UserExerciseData u : uDB) {
			mCal.setTimeInMillis(u.getTime());
			int uDay = mCal.get(Calendar.DAY_OF_MONTH);
			int uMonth = mCal.get(Calendar.MONTH);
			if(uMonth != month || uDay != day)
				continue;

			idx++;
			ave_accuracy += u.getAccuracy();
		}

		return ave_accuracy/idx;
	}
	*/
}

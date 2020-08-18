package com.coach.test.coachmw.database;

/**
 * Created by Administrator on 2017-02-17.
 */

import com.coach.test.coachmw.datastructure.DataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 데이터 베이스(이하 DB)를 관리하는 클래스로 선언되어 있는 static 변수들을 제외하고는 접근할 수 없습니다.
 */

public class DBContactHelper extends SQLiteOpenHelper {
    private static final String tag = "DBContactHelper";

    private static DBContactHelper mDBHelper = null;
    /**
     * 성공
     */
    protected static final int SUCCESS = 0;
    /**
     * 실패
     */
    protected static final int FAILED = 1;

    /**
     * 기본 DB 테이블이 존재합니다.
     */
    protected static final int EXIST_BASIC_TABLE = 10;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "coachDB.db";

    // Contacts table name
    private static final String TABLE_PROFILE_INFORMAION = "profileInform";
    private static final String TABLE_WEIGHT_INFORMAION = "weight_inform";
    private static final String TABLE_DATA = "data";

    // Basic Information Columns names
    protected static final String KEY_SEX = "sex";
    protected static final String KEY_AGE = "age";
    protected static final String KEY_HEIGHT = "height";
    protected static final String KEY_LANGUAGE = "language";
    protected static final String KEY_PROFILE_RESERVED_1 = "reserved_1";
    protected static final String KEY_PROFILE_RESERVED_2 = "reserved_2";

    // 체중 정보
    protected static final String KEY_WEIGHT = "weight";
    protected static final String KEY_GOAL_WEIGHT = "goal_weight";
    protected static final String KEY_DIET_PERIOD = "diet_period";


    // Data DB
    protected static final String KEY_INDEX = "idx";
    protected static final String KEY_VIDEO_IDX = "video_idx";
    protected static final String KEY_VIDEO_FULL_COUNT = "video_full_count";
    protected static final String KEY_EXER_IDX = "exer_idx";
    protected static final String KEY_EXER_COUNT = "exer_count";
    protected static final String KEY_START_TIME = "start_time";
    protected static final String KEY_END_TIME = "end_time";
    protected static final String KEY_CONSUME_CALORIE = "consume_calorie";
    protected static final String KEY_COUNT = "count";
    protected static final String KEY_COUNT_PERCENT = "count_percent";
    protected static final String KEY_PERFECT_COUNT = "perfect_count";
    protected static final String KEY_MIN_ACCURACY = "min_accuracy";
    protected static final String KEY_MAX_ACCURACY = "max_accuracy";
    protected static final String KEY_AVG_ACCURACY = "avg_accuracy";
    protected static final String KEY_MIN_HEARTRATE = "min_heartrate";
    protected static final String KEY_MAX_HEARTRATE = "max_heartrate";
    protected static final String KEY_AVG_HEARTRATE = "avg_heartrate";
    protected static final String KEY_CMP_HEARTRATE = "compared_heartrate";
    protected static final String KEY_POINT = "point";
    protected static final String KEY_EXER_RESERVED_1 = "reserved_1";
    protected static final String KEY_EXER_RESERVED_2 = "reserved_2";

    /****/
    protected DBContactHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    protected static void initInstance(Context context) {
        if(mDBHelper == null) {
            mDBHelper = new DBContactHelper(context);
        }
    }

    protected static DBContactHelper getInstance() {
        return mDBHelper;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE_INFORMAION
                + "(" + KEY_SEX + " INTEGER PRIMARY KEY," + KEY_AGE + " INTEGER," // 1 , 2
                + KEY_HEIGHT + " INTEGER," + KEY_LANGUAGE + " INTEGER,"
                + KEY_PROFILE_RESERVED_1 + " INTEGER,"+ KEY_PROFILE_RESERVED_2 + " INTEGER"+ ")";
        db.execSQL(CREATE_PROFILE_TABLE);

        String CREATE_WEIGHT_TABLE = "CREATE TABLE " + TABLE_WEIGHT_INFORMAION
                + "(" + KEY_WEIGHT + " INTEGER PRIMARY KEY," + KEY_GOAL_WEIGHT + " INTEGER," // 1 , 2
                + KEY_DIET_PERIOD + " INTEGER" + ")";
        db.execSQL(CREATE_WEIGHT_TABLE);

        String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA
                + "(" + KEY_INDEX + " INTEGER PRIMARY KEY," + KEY_VIDEO_IDX + " INTEGER," // 1 , 2
                + KEY_VIDEO_FULL_COUNT+ " INTEGER," + KEY_EXER_IDX + " INTEGER,"
                + KEY_EXER_COUNT + " INTEGER,"+ KEY_START_TIME+ " INTEGER,"
                + KEY_END_TIME+ " INTEGER,"	+ KEY_CONSUME_CALORIE + " INTEGER,"
                + KEY_COUNT + " INTEGER,"+ KEY_COUNT_PERCENT + " INTEGER,"
                + KEY_PERFECT_COUNT + " INTEGER," + KEY_MIN_ACCURACY + " INTEGER,"
                + KEY_MAX_ACCURACY+ " INTEGER,"	+ KEY_AVG_ACCURACY + " INTEGER,"
                + KEY_MIN_HEARTRATE + " INTEGER,"+ KEY_MAX_HEARTRATE + " INTEGER,"
                + KEY_AVG_HEARTRATE + " INTEGER,"+ KEY_CMP_HEARTRATE + " INTEGER,"
                + KEY_POINT +" INTEGER,"+ KEY_EXER_RESERVED_1 +" INTEGER,"
                + KEY_EXER_RESERVED_2 +" INTEGER" + ")";
        db.execSQL(CREATE_DATA_TABLE);

        Log.i(tag, "onCreate!!!!!!!!!!!!!!!!!!!!!");
    }

    protected void createAllTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_PROFILE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROFILE_INFORMAION
                + "(" + KEY_SEX + " INTEGER PRIMARY KEY," + KEY_AGE + " INTEGER," // 1 , 2
                + KEY_HEIGHT + " INTEGER," + KEY_LANGUAGE + " INTEGER,"
                + KEY_PROFILE_RESERVED_1 + " INTEGER,"+ KEY_PROFILE_RESERVED_2 + " INTEGER"+ ")";
        db.execSQL(CREATE_PROFILE_TABLE);

        String CREATE_WEIGHT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WEIGHT_INFORMAION
                + "(" + KEY_WEIGHT + " INTEGER PRIMARY KEY," + KEY_GOAL_WEIGHT + " INTEGER," // 1 , 2
                + KEY_DIET_PERIOD + " INTEGER" + ")";
        db.execSQL(CREATE_WEIGHT_TABLE);

        String CREATE_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DATA
                + "(" + KEY_INDEX + " INTEGER PRIMARY KEY," + KEY_VIDEO_IDX + " INTEGER," // 1 , 2
                + KEY_VIDEO_FULL_COUNT+ " INTEGER," + KEY_EXER_IDX + " INTEGER,"
                + KEY_EXER_COUNT + " INTEGER,"+ KEY_START_TIME+ " INTEGER,"
                + KEY_END_TIME+ " INTEGER,"	+ KEY_CONSUME_CALORIE + " INTEGER,"
                + KEY_COUNT + " INTEGER,"+ KEY_COUNT_PERCENT + " INTEGER,"
                + KEY_PERFECT_COUNT + " INTEGER," + KEY_MIN_ACCURACY + " INTEGER,"
                + KEY_MAX_ACCURACY+ " INTEGER,"	+ KEY_AVG_ACCURACY + " INTEGER,"
                + KEY_MIN_HEARTRATE + " INTEGER,"+ KEY_MAX_HEARTRATE + " INTEGER,"
                + KEY_AVG_HEARTRATE + " INTEGER,"+ KEY_CMP_HEARTRATE + " INTEGER,"
                + KEY_POINT +" INTEGER,"+ KEY_EXER_RESERVED_1 +" INTEGER,"
                + KEY_EXER_RESERVED_2 +" INTEGER" + ")";
        db.execSQL(CREATE_DATA_TABLE);
    }

    protected void dropAllTable() {
        // Drop older table if existed
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE_INFORMAION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_INFORMAION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        dropAllTable();
        createAllTable();
        Log.i(tag,"onUpgrade!!!!!!!!!!!!!!!");
    }

    /**
     * CRUD 함수
     */

    // 프로필 추가
    private int addProfile(DataBase.UserProfile dbObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        String countQuery = "SELECT  * FROM " + TABLE_PROFILE_INFORMAION;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count > 0) {
            Log.w(tag, "we need only one basic information");
            return EXIST_BASIC_TABLE;
        }
        ContentValues values = new ContentValues();
        values.put(KEY_SEX, dbObj.getSex());
        values.put(KEY_AGE, dbObj.getAge());
        values.put(KEY_HEIGHT, dbObj.getHeight());
        values.put(KEY_LANGUAGE, dbObj.getLanguage());
        values.put(KEY_PROFILE_RESERVED_1, dbObj.getReserved_1());
        values.put(KEY_PROFILE_RESERVED_2, dbObj.getReserved_2());
        table = TABLE_PROFILE_INFORMAION;

        // Inserting Row
        db.insert(table, null, values);

        return SUCCESS;
    }

    private int updateProfile(DataBase.UserProfile dbObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        ContentValues values = new ContentValues();
        values.put(KEY_SEX, dbObj.getSex());
        values.put(KEY_AGE, dbObj.getAge());
        values.put(KEY_HEIGHT, dbObj.getHeight());
        values.put(KEY_LANGUAGE, dbObj.getLanguage());
        values.put(KEY_PROFILE_RESERVED_1, dbObj.getReserved_1());
        values.put(KEY_PROFILE_RESERVED_2, dbObj.getReserved_2());
        table = TABLE_PROFILE_INFORMAION;

        // Inserting Row
        db.update(table, values, null, null);

        return SUCCESS;
    }
    private int deleteProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        table = TABLE_PROFILE_INFORMAION;

        long ret =db.delete(table, null, null);

        if(ret > 0)
            return SUCCESS;
        else
            return FAILED;
    }

    private int addWeightProfile(DataBase.UserWeightProfile dbObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        String countQuery = "SELECT  * FROM " + TABLE_WEIGHT_INFORMAION;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count > 0) {
            Log.w(tag, "we need only one basic information");
            return EXIST_BASIC_TABLE;
        }
        ContentValues values = new ContentValues();
        values.put(KEY_WEIGHT, dbObj.getWeight());
        values.put(KEY_GOAL_WEIGHT, dbObj.getGoal_weight());
        values.put(KEY_DIET_PERIOD, dbObj.getDiet_period());
        table = TABLE_WEIGHT_INFORMAION;

        // Inserting Row
        db.insert(table, null, values);

        return SUCCESS;
    }

    private int updateWeightProfile(DataBase.UserWeightProfile dbObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        ContentValues values = new ContentValues();
        values.put(KEY_WEIGHT, dbObj.getWeight());
        values.put(KEY_GOAL_WEIGHT, dbObj.getGoal_weight());
        values.put(KEY_DIET_PERIOD, dbObj.getDiet_period());
        table = TABLE_WEIGHT_INFORMAION;

        // Inserting Row
        db.update(table, values, null, null);

        return SUCCESS;
    }
    private int deleteWeightProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        table = TABLE_WEIGHT_INFORMAION;

        long ret =db.delete(table, null, null);

        if(ret > 0)
            return SUCCESS;
        else
            return FAILED;
    }

    private int addData(DataBase.UserExerciseData dbObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_IDX, dbObj.getVideo_idx());
        values.put(KEY_VIDEO_FULL_COUNT, dbObj.getVideo_full_count());
        values.put(KEY_EXER_IDX, dbObj.getExer_idx());
        values.put(KEY_EXER_COUNT, dbObj.getExer_count());
        values.put(KEY_START_TIME, dbObj.getStart_time());
        values.put(KEY_END_TIME, dbObj.getEnd_time());
        values.put(KEY_CONSUME_CALORIE, dbObj.getConsume_calorie());
        values.put(KEY_COUNT, dbObj.getCount());
        values.put(KEY_COUNT_PERCENT, dbObj.getCount_percent());
        values.put(KEY_PERFECT_COUNT, dbObj.getPerfect_count());
        values.put(KEY_MIN_ACCURACY, dbObj.getMin_accuracy());
        values.put(KEY_MAX_ACCURACY, dbObj.getMax_accuracy());
        values.put(KEY_AVG_ACCURACY, dbObj.getAvg_accuracy());
        values.put(KEY_MIN_HEARTRATE, dbObj.getMin_heartrate());
        values.put(KEY_MAX_HEARTRATE, dbObj.getMax_heartrate());
        values.put(KEY_AVG_HEARTRATE, dbObj.getAvg_heartrate());
        values.put(KEY_CMP_HEARTRATE, dbObj.getCmp_accuracy());
        values.put(KEY_POINT, dbObj.getPoint());
        values.put(KEY_EXER_RESERVED_1, dbObj.getReserved_1());
        values.put(KEY_EXER_RESERVED_2, dbObj.getReserved_2());
        table = TABLE_DATA;


        // Inserting Row
        db.insert(table, null, values);

        return SUCCESS;
    }

    private int deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        table = TABLE_DATA;

        long ret =db.delete(table, null, null);

        if(ret > 0)
            return SUCCESS;
        else
            return FAILED;
    }

    private int deleteData(Long startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = null;

        table = TABLE_DATA;

        long ret = db.delete(table, KEY_START_TIME +"=?", new String[]{ startTime.toString() });

        if(ret > 0)
            return SUCCESS;
        else
            return FAILED;
    }

    private DataBase.UserExerciseData[] getDataBase() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DATA, null, null, null,
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        if( cursor.getCount() == 0) {
            Log.e(tag, "not exist table data:"+TABLE_DATA);
            cursor.close();
            return null;
        }


        DataBase.UserExerciseData[] databaseArray = new DataBase.UserExerciseData[cursor.getCount()];
        int i=0;
        do{
            DataBase.UserExerciseData database = DataBase.getUserExerciseData();
            database.setUserExerciseData(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                    cursor.getInt(4),cursor.getLong(5),cursor.getLong(6),cursor.getInt(7),cursor.getInt(8),
                    cursor.getInt(9),cursor.getInt(10),cursor.getInt(11),cursor.getInt(12),cursor.getInt(13),
                    cursor.getInt(14),cursor.getInt(15),cursor.getInt(16),cursor.getInt(17),cursor.getInt(18),
                    cursor.getInt(19),cursor.getInt(20));
            databaseArray[i] = database;
            i++;
        }
        while(cursor.moveToNext());

        cursor.close();
        return databaseArray;
    }

    private DataBase.UserProfile getProfile() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILE_INFORMAION, null, null, null,
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        if( cursor.getCount() == 0) {
            Log.e(tag, "not exist profile table data:"+TABLE_PROFILE_INFORMAION);
            cursor.close();
            return null;
        }

        DataBase.UserProfile profile = DataBase.getUserProfileInstance();
        profile.setUserProfile(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                cursor.getInt(4),cursor.getInt(5));

        cursor.close();
        return profile;
    }

    private DataBase.UserWeightProfile getWeightProfile() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WEIGHT_INFORMAION, null, null, null,
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        if( cursor.getCount() == 0) {
            Log.e(tag, "not exist profile table data:"+TABLE_WEIGHT_INFORMAION);
            cursor.close();
            return null;
        }

        DataBase.UserWeightProfile profile = DataBase.getUserWeightProfileInstance();
        profile.setUserWeightProfile(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));

        cursor.close();
        return profile;
    }

    protected int addUserProfile(int sex, int age, int height, int language, int reserved_1, int reserved_2) {
        DataBase.UserProfile profile = DataBase.getUserProfileInstance();
        profile.setUserProfile(sex, age, height, language, reserved_1, reserved_2);
        if(addProfile(profile) != DBContactHelper.SUCCESS) {
            return updateProfile(profile);
        }
        return DBContactHelper.SUCCESS;
    }

    protected DataBase.UserProfile getUserProfile() {
        return getProfile();
    }

    protected int addUserWeightProfile(int weight, int goal_weight, int diet_period) {
        DataBase.UserWeightProfile wProfile = DataBase.getUserWeightProfileInstance();
        wProfile.setUserWeightProfile(weight, goal_weight, diet_period);
        if(addWeightProfile(wProfile) != DBContactHelper.SUCCESS) {
            return updateWeightProfile(wProfile);
        }
        return DBContactHelper.SUCCESS;
    }

    protected DataBase.UserWeightProfile getUserWeightProfile() {
        return getWeightProfile();
    }

    protected int addUserExerciseData(int video_idx, int video_full_count, int exer_idx, int exer_count,long start_time, long end_time,
                                      int consume_calorie, int count, int count_percent, int perfect_count, int min_accuracy, int max_accuracy,
                                      int avg_accuracy , int min_hr, int max_hr , int avg_hr, int cmp_hr, int point, int reserved_1, int reserved_2) {
        DataBase.UserExerciseData database = DataBase.getUserExerciseData();
        database.setUserExerciseData(video_idx, video_full_count, exer_idx, exer_count, start_time, end_time, consume_calorie,
                count, count_percent, perfect_count, min_accuracy, max_accuracy, avg_accuracy, min_hr, max_hr, avg_hr,
                cmp_hr, point, reserved_1, reserved_2);

        return addData(database);
    }

    protected DataBase.UserExerciseData[] getUserExerciseData() {
        return getDataBase();
    }

    protected int deleteUserExerciseData() {
        return deleteData();
    }

    protected int deleteUserExerciseData(Long startTime) {
        return deleteData(startTime);
    }
}
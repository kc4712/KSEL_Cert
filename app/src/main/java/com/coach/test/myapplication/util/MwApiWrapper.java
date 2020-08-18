package com.coach.test.myapplication.util;

import com.coach.test.coachmw.datastructure.DataBase;
import com.coach.test.coachmw.manager.BluetoothManager;
//import com.coach.test.coachmw.RawManager;
import com.coach.test.coachmw.manager.ConfigManager;
import com.coach.test.coachmw.manager.VideoManager;
//import com.coach.test.coachmw.manager.VideoManager2;
//import kr.co.greencomm.ibody24coach.activity.vmRAWtest;
import com.coach.test.coachmw.protocol.IBluetooth;
import com.coach.test.coachmw.protocol.IViewComment;
import android.content.Context;

/**
 * Created by Administrator on 2017-02-17.
 */

import android.content.Context;

/**
 * 앱의 첫 실행 시, 초기화를 담당.
 *
 * @author user
 *
 */
public class MwApiWrapper {
    //private static DBContactHelper mDBHelper;
    //private static DataBase.UserProfile mUserProfile;
    private static BluetoothManager mBManager;
    private static VideoManager mVManager;
    //private static PreferencesManager mPreManager;
    private static ConfigManager mConfigManager;
    //private static vmRAWtest mvmRAWtest;

    public MwApiWrapper(Context context) {
        /** 객체들의 초기화 **/
        ConfigManager.initInstance(context);
        mConfigManager = ConfigManager.getInstance();
        mVManager = VideoManager.getInstance();
        //mVManager2 = VideoManager2.getInstance();
        //DBContactHelper.initInstance(context);
		/*DataBase.initInstance();
		BluetoothManager.initInstance();*/
        //mvmRAWtest = vmRAWtest.getInstance();
        //mPreManager = new PreferencesManager(context);
        //mDBHelper = DBContactHelper.getInstance();
        mBManager = BluetoothManager.getInstance(context);
        //mRManager = RawManager.getInstance(context);
        //mUserProfile = DataBase.getUserProfileInstance();
    }

    public static class WrapperBluetoothManager {
        public static boolean connect(String address) {
            return mBManager.connect(address);
        }

        public static void scanLeDevice(boolean enable) {
            mBManager.scanLeDevice(enable);
        }
        public static void disconnect() {
            mBManager.disconnect();
        }
        public static void registCallback(IBluetooth cb) {
            BluetoothManager.registCallback(cb);
        }
        public static void unregistCallback() {
            BluetoothManager.unregistCallback();
        }
        public static String getRemoteMac() {
            return mBManager.getRemoteMac();
        }
        public static String getRemoteMac(int remoteCount) {
            return mBManager.getRemoteMac(remoteCount);
        }
        public static void play() {
            mBManager.play();
        }
        public static void stop() {
            mBManager.stop();
        }
    }
	/*
	public static class WrapperRawManager {
		public static void play() {
			mBManager.play();
		}
	}*/

    public static class WrapperDBHelper {
        public static int setUserProfile(int sex, int age, int height, int language) {
            return mConfigManager.setUserProfile(sex, age, height, language);
        }
        public static DataBase.UserProfile getUserProfile() {
            return mConfigManager.getUserProfile();
        }
        public static int setUserWeightProfile(int weight, int goal_weight, int diet_period) {
            return mConfigManager.setUserWeightProfile(weight, goal_weight, diet_period);
        }
        public static DataBase.UserWeightProfile getUserWeightProfile() {
            return mConfigManager.getUserWeightProfile();
        }
        public static boolean isEmptyUserData() {
            return mConfigManager.isEmptyUserData();
        }
    }
/*
	public static class WrapperUserProfile {
		public static DataBase.UserProfile setUserProfile(int sex, int age, int height, int weight) {
			mUserProfile.setUserProfile(sex, age, height, weight);
			return mUserProfile;
		}
	}
	*/
	/*public static class WrappervmRAWtest {

		public static boolean setVideoID(int videoID) {
			return mvmRAWtest.setVideoID(videoID);
		}
		public static void setCurrentPosition(int currentPosition) {
			mvmRAWtest.setCurrentPosition(currentPosition);
		}
		public static boolean isRVideo() {
			return mvmRAWtest.isRVideo();
		}
		public static void setPlaying(boolean setPlay) {
			mvmRAWtest.setPlaying(setPlay);
		}
		public static void play() {
			mvmRAWtest.play();
		}
		public static void end() {
			mvmRAWtest.end();
		}
		public static void setChkSave(boolean setSave) {
			mvmRAWtest.setChkSave(setSave);
		}
		public static void setRecordName(String name) {
			mvmRAWtest.setRecordName(name);
		}
		public static String getSDPath() {
			return mvmRAWtest.getSDPath();
		}
		public static void setChkDebugPlay(boolean setDebugPlay) {
			mvmRAWtest.setChkDebugPlay(setDebugPlay);
		}
		public static void setDualMode(boolean setDual) {
			mvmRAWtest.setDualMode(setDual);
		}
		public static void setPlayMode(int mode) {
			mvmRAWtest.setPlayMode(mode);
		}
	}*/




    public static class WrapperVideoManager {
        public static void unregistCallback() {
            VideoManager.unregistCallback();
        }
        public static void registCallback(IViewComment cb) {
            VideoManager.registCallback(cb);
        }
        public static boolean setVideoID(int videoID) {
            return mVManager.setVideoID(videoID);
        }
        public static void setCurrentPosition(int currentPosition) {
            mVManager.setCurrentPosition(currentPosition);
        }
        public static boolean isRVideo() {
            return mVManager.isRVideo();
        }
        public static void setPlaying(boolean setPlay) {
            mVManager.setPlaying(setPlay);
        }
        public static boolean play() {
            return mVManager.play();
        }
        public static boolean play2() {
            return mVManager.play2();
        }
        public static void end() {
            mVManager.end();
        }
        public static void setChkSave(boolean setSave) {
            mVManager.setChkSave(setSave);
        }
        public static void setRecordName(String name) {
            mVManager.setRecordName(name);
        }
        public static void initFileManager(String acc, String hr){
            mVManager.initFileManager(acc,hr);
        }
        public static String getSDPath() {
            return mVManager.getSDPath();
        }
        public static void setChkDebugPlay(boolean setDebugPlay) {
            mVManager.setChkDebugPlay(setDebugPlay);
        }
        public static void setLocale(String Locale) {
            mVManager.setLocale(Locale);
        }
        public static void setDualMode(boolean setDual) {
            mVManager.setDualMode(setDual);
        }
        public static void setPlayMode(int mode) {
            mVManager.setPlayMode(mode);
        }

        public static boolean isDisableUI(){
            return mVManager.isDisableUI();
        }
    }
	/*
	public static class WrapperVideoManager2 {
		public static void unregistCallback() {
			VideoManager2.unregistCallback();
		}
		public static void registCallback(IViewComment cb) {
			VideoManager2.registCallback(cb);
		}
		public static boolean setVideoID(int videoID) {
			return mVManager2.setVideoID(videoID);
		}
		public static void setCurrentPosition(int currentPosition) {
			mVManager2.setCurrentPosition(currentPosition);
		}
		public static boolean isRVideo() {
			return mVManager2.isRVideo();
		}
		public static void setPlaying(boolean setPlay) {
			mVManager2.setPlaying(setPlay);
		}
		public static boolean play() throws InterruptedException {
			return mVManager2.play();
		}
		public static void end() {
			mVManager2.end();
		}
		public static void setChkSave(boolean setSave) {
			mVManager2.setChkSave(setSave);
		}
		public static void setRecordName(String name) {
			mVManager2.setRecordName(name);
		}
		public static String getSDPath() {
			return mVManager2.getSDPath();
		}
		public static void setChkDebugPlay(boolean setDebugPlay) {
			mVManager2.setChkDebugPlay(setDebugPlay);
		}
		//public static void setDualMode(boolean setDual) {
		//	mVManager2.setDualMode(setDual);
		//}
		public static void setPlayMode(int mode) {
			mVManager2.setPlayMode(mode);
		}
	}*/
}
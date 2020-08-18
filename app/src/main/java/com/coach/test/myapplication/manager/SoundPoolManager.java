package com.coach.test.myapplication.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import com.coach.test.myapplication.R;

/**
 * Created by Administrator on 2017-02-17.
 */
public final class SoundPoolManager {


    public static void playBeep(Context context) {
        SoundPool mPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mPool.play(mPool.load(context, R.raw.ddok, 1), 1f, 1f, 0, 0, 1);
    }

    public static void playEnd(Context context) {
        SoundPool mPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mPool.play(mPool.load(context, R.raw.end, 1), 1f, 1f, 0, 0, 1);
    }
}
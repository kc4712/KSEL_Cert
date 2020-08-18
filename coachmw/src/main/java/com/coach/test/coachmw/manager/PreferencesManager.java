package com.coach.test.coachmw.manager;

import com.coach.test.coachmw.util.InnerEditor;
import android.content.Context;

/**
 * Created by Administrator on 2017-02-17.
 */

/**
 * 기본적인 MW 관련 환경설정을 담당하는 클래스.
 * @author user
 *
 */
public final class PreferencesManager extends InnerEditor{
    /** Variable **/
    private static final int NOVICE_REFERENCE = 1;
    private static final int MID_REFERENCE = 2;
    private static final int ADVANCED_REFERENCE = 3;

    private static final int BOOT_NORMAL_MODE = 1; // 부팅 mode를 위해 작성한 reference 변수인데, 아직 2인용 협의가 끝나지 않았음.
    private static final int BOOT_DUAL_MODE = 2;

    private Context mContext;

    protected PreferencesManager(Context context) {
        mContext = context;
        if(getUserReference() == 0)
            setUserReference(NOVICE_REFERENCE);
    }


    /**
     * 초급,중급,고급 선택 시, 실행.
     * @param ref
     */
    protected void setUserReference(int ref) {
        InnerEditor.setUserReference(mContext, ref);
    }

    /**
     * 3단계 정보를 얻어온다.
     * @return
     */
    protected int getUserReference() {
        return InnerEditor.getUserReference(mContext);
    }


    protected void setBootMode(int mode) {
        InnerEditor.setUserBootMode(mContext, mode);
    }
    protected int getBootMode() {
        return InnerEditor.getUserBootMode(mContext);
    }

}

package com.coach.test.myapplication.activity;

import com.coach.test.myapplication.Main;
import com.coach.test.myapplication.R;
import com.coach.test.myapplication.util.MwApiWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.coach.test.myapplication.R;

public class IntroActivity extends WActivity {
    /** Variable **/
    private boolean destroyAPP = false;
    //private Button mBtnGo;
    /** DB 확인 **/
/*	public boolean isEmptyUserProfile() {
		DBContactHelper mDBHelper = DBContactHelper.getInstance();
		if (mDBHelper.getProfile() == null)
			return true;
		else
			return false;
	}
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //mBtnGo = (Button) findViewById(R.id.button1);
        //mBtnGo.setOnClickListener(mBtnClick);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Main();

                if (!destroyAPP) {
                    if (MwApiWrapper.WrapperDBHelper.isEmptyUserData())
                        startActivity(new Intent(IntroActivity.this, UserInformActivity.class));
                    else
                        startActivity(new Intent(IntroActivity.this, BluetoothActivity.class));
                }
                finish();
            }
        }, 2000);
    }
	/*OnClickListener mBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1: // 검색 시작.
				startActivity(new Intent(IntroActivity.this, VideoListActivity2.class));
				finish();
			}
		}
	};*/

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        destroyAPP = true;
    }
}

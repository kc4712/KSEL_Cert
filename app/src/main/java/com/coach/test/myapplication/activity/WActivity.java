package com.coach.test.myapplication.activity;

import com.coach.test.myapplication.R;
import com.coach.test.myapplication.dialog.MessageDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-02-17.
 */

/**
 * Activity 상속 class.
 * @author user
 *
 */
public class WActivity extends Activity{
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        pDialog = new ProgressDialog(WActivity.this);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
    }
    /** 진동 실행 **/
    protected boolean vib() {
        Vibrator mVib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if(!mVib.hasVibrator())
            return false;

        mVib.vibrate(500);
        return true;
    }

    /**
     * 간단한 메세지 출력을 위한 Alert
     *
     * @param resId
     *            R.string
     */
    protected void alert(int resId) {
        alert(getString(resId));
    }

    /**
     * 간단한 메세지 출력을 위한 Alert
     */
    protected void alert(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(WActivity.this);

                builder.setMessage(msg)
                        .setNegativeButton(getString(R.string.is_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    protected void alert(int resId, Class<?> toClass) {
        alert(getString(resId), toClass);
    }

    protected void alert(final String msg, final Class<?> toClass) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(WActivity.this);

                builder.setMessage(msg)
                        .setNegativeButton(getString(R.string.is_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                                startActivity(new Intent(WActivity.this, toClass));
                                finish();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
    /**
     * 간단한 메세지 출력을 위한 Toast
     *
     * @param resId
     *            R.string
     */
    protected void toast(int resId) {
        toast(getString(resId));
    }

    /**
     *	간단한 메세지 출력을 위한 Toast
     */
    protected void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * progressdialog 출력
     *  msg
     */
    protected void progress(int resId) {
        progress(getString(resId));
    }

    protected void progress(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pDialog.setMessage(msg);
                pDialog.show();
            }
        });
    }

    protected void progress_dismiss() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pDialog.dismiss();
            }
        });
    }
    //----------------------------------------------------------------------------------------------
    // 다이아로그 페시지 창 표시
    //----------------------------------------------------------------------------------------------

    /**
     * 간단한 메세지 출력을 위한 Alert
     */
    protected void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(WActivity.this);
                builder.setMessage(msg)
                        .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    protected void displayMsg(String msg, View.OnClickListener listener) {
        MessageDialog dialog = new MessageDialog(this, msg, listener);
        dialog.show();
    }
    protected void displayMsg(int resId) { displayMsg(getString(resId), null); }
    protected void displayMsg(int resId, View.OnClickListener listener) { displayMsg(getString(resId), listener); }
    protected void displayMsg(String msg) { displayMsg(msg, null); }
}
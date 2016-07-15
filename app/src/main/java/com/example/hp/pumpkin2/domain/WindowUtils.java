package com.example.hp.pumpkin2.domain;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.db.MySqliteHelper;


/**
 * Created by yx on 2016/3/6.
 */
public class WindowUtils {


    private static final String LOG_TAG = "WindowUtils";
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Context mContext = null;

    public static Boolean isShown = false;

    public static void showPopupWindow(final Context context,Intent  intent) {
        if (isShown) {
            Log.d(LOG_TAG, "return cause already shown");
            return;
        }

        isShown = true;
        Log.d(LOG_TAG, "showPopupWindow");


        mContext = context.getApplicationContext();

        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        mView = setUpView(context,intent);     //设置显示界面

        final LayoutParams params = new LayoutParams();

        params.type = LayoutParams.TYPE_SYSTEM_ALERT;

        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT


        int flags = LayoutParams.FLAG_ALT_FOCUSABLE_IM;

        params.flags = flags;

        params.format = PixelFormat.TRANSLUCENT;

        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;

        params.gravity = Gravity.CENTER;

        mWindowManager.addView(mView, params);

        Log.d(LOG_TAG, "add view");

    }

    public static void hidePopupWindow() {
        Log.d(LOG_TAG, "hide " + isShown + ", " + mView);
        if (isShown && null != mView) {
            Log.d(LOG_TAG, "hidePopupWindow");
            mWindowManager.removeView(mView);
            isShown = false;
        }

    }

    private static View setUpView(final Context context,Intent intent) {

        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);  //mydata.db的数据库
        final SQLiteDatabase db=mySqliteHelper.getWritableDatabase();


        Log.d(LOG_TAG, "setUp view");

       final  int task_id=intent.getExtras().getInt("_id");
        String date=intent.getStringExtra("date");
        String task=intent.getStringExtra("task");


        View view = LayoutInflater.from(context).inflate(R.layout.window_set,
                null);

        TextView  tv_task= (TextView) view.findViewById(R.id.textView19);
        TextView  tv_date= (TextView) view.findViewById(R.id.textView20);


        tv_task.setText(task);
        tv_date.setText(date);


        ImageView positiveBtn = (ImageView) view.findViewById(R.id.button2);  //完成,将数据库里面的任务标记为已完成
        positiveBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ContentValues  values=new ContentValues();
                values.put("state","done");
                db.update("my_table",values,"_id=" + String.valueOf(task_id), null);

                WindowUtils.hidePopupWindow();

            }
        });

        ImageView negativeBtn = (ImageView) view.findViewById(R.id.button4);  //不再显示
        negativeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                WindowUtils.hidePopupWindow();

            }
        });


        final View popupWindowView = view.findViewById(R.id.relativeLayoutxuanfuchuan);

        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(LOG_TAG, "onTouch");
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = new Rect();
                popupWindowView.getGlobalVisibleRect(rect);
                if (!rect.contains(x, y)) {
                    WindowUtils.hidePopupWindow();
                }

                Log.d(LOG_TAG, "onTouch : " + x + ", " + y + ", rect: "
                        + rect);
                return false;
            }
        });

        view.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        WindowUtils.hidePopupWindow();
                        return true;
                    default:
                        return false;
                }
            }
        });

        return view;

    }



}

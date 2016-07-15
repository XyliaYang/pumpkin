package com.example.hp.pumpkin2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.activity.CalendarListActivity;
import com.example.hp.pumpkin2.activity.LandActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hp on 2016/6/7.
 */
public class LeftMenuFragment  extends BaseFragment  {

    LinearLayout ll_land;
    LinearLayout ll_calendar;

    public View initView() {
        // TODO Auto-generated method stub
        View view = View.inflate(myactivity, R.layout.fragment_left_menu, null);
        ll_land= (LinearLayout) view.findViewById(R.id.ll_land);
        ll_calendar= (LinearLayout) view.findViewById(R.id.ll_calendar);

        return view;
    }

    @Override
    public void initData() {


        ll_land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日 HH:mm");
                Date curDate   =   new   Date(System.currentTimeMillis());
                String   str   =   formatter.format(curDate).substring(0,11);
                Bundle data=new Bundle();
                data.putString("curTime",str);

                Intent intent = new Intent(myactivity, LandActivity.class);
                intent.putExtras(data);
                startActivity(intent);

            }
        });


        ll_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(myactivity, CalendarListActivity.class);
                startActivity(intent1);
            }
        });



    }

}

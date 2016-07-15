package com.example.hp.pumpkin2.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.hp.pumpkin2.MainActivity;
import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.db.MySqliteHelper;
import com.example.hp.pumpkin2.db.my_table;
import com.example.hp.pumpkin2.fragment.ContentFragment;
import com.example.hp.pumpkin2.receiver.MyReceiver;
import com.example.hp.pumpkin2.utils.DateTimePickDialogUtil1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hp on 2016/6/9.
 */
public class AddActivity extends Activity{

    ImageButton ib_back; //回到主界面按钮
    ImageView iv_comfirm;  //确认按钮
    EditText et_type;  //分类文本框
    EditText et_date; //日期文本框
    EditText et_add; //任务文本框
    EditText et_alarm; //重复周期文本框
    EditText  et_remark; //备注文本框
    ImageView iv_type_icon; //分类按钮
    ImageView iv_date_icon; //日期选择按钮
    ImageView iv_alarm_icon;  //设置提醒按钮
    ImageView  iv_remark_icon; //备注按钮
    Spinner sp_num; //选择周期对话框  数目
    Spinner  sp_cycle; //             周期单位
    private AlertDialog ad_alarm;  //重复周期设置对话框
    SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日 HH:mm");
    Date curDate   =   new   Date(System.currentTimeMillis());
    String   initEndDateTime   =   formatter.format(curDate);  // 初始化点击开始时间
    private AlarmManager mAlarm; //定时器
    MySqliteHelper mySqliteHelper=new MySqliteHelper(this,"mydata.db",null,1);  //mydata.db的数据库
    String cycleTime;
    //    SimpleDateFormat  format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private int alarm_id=0; //区别不同的任务id
    private Calendar  c= Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout repeat_layout = (LinearLayout)AddActivity.this.getLayoutInflater().inflate(R.layout.repeat_set_custom, null);   //自定义周期界面


        initView();
        initdata();


    }

    private void initdata() {

        //备注按钮
        iv_remark_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, RemarkActivity.class);
                startActivityForResult(intent, 3);

            }
        });


        //提醒周期
        iv_alarm_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final LinearLayout repeat_layout = (LinearLayout)AddActivity.this.getLayoutInflater().inflate(R.layout.repeat_set_custom, null);   //自定义周期界面


                ad_alarm = new AlertDialog.Builder(AddActivity.this)
                        .setTitle("设置重复周期")
                        .setView(repeat_layout)
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                cycleTime="每"+sp_num.getSelectedItem().toString()+sp_cycle.getSelectedItem().toString();
                                et_alarm.setText(cycleTime);
                                ad_alarm.dismiss();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();

            }});



        //回主界面
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //保存到数据库并返回主界面
        iv_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tasktext = et_add.getText().toString();


                if (tasktext.equals("")) {
                    new AlertDialog.Builder(AddActivity.this)
                            .setTitle("不能添加空任务！").create().show();

                } else {                                 //每次创建完成任务之后需要执行提醒的广播
                    String datetext = et_date.getText().toString();
                    String task_typetext = et_type.getText().toString();
                    String cycle_timetext = et_alarm.getText().toString();
                    String remarktext = et_remark.getText().toString();
                    String doneState = "todo";
                    String date_init = "notComplete";



                    ContentValues values = new ContentValues();
                    values.put("task", tasktext);
                    values.put("date", datetext);
                    values.put("task_type", task_typetext);
                    values.put("cycle_time", cycle_timetext);
                    values.put("remark", remarktext);
                    values.put("state", doneState);
                    values.put("done_time", date_init);

                    my_table.Insert(mySqliteHelper, values);

                    if(!(datetext.equals(""))){

                        String  year=datetext.substring(0,4);
                        String  month= datetext.substring(5,7);
                        String  day=datetext.substring(8,10);
                        String  hour=datetext.substring(12,14);
                        String minute =datetext.substring(15, 17);

                        int year_int=Integer.parseInt(year);
                        int month_int=Integer.parseInt(month);
                        int day_int=Integer.parseInt(day);
                        int hour_int=Integer.parseInt(hour);
                        int minute_int=Integer.parseInt(minute);



                        c.set(Calendar.YEAR,year_int);
                        c.set(Calendar.MONTH,month_int-1);//也可以填数字，0-11,一月为0
                        c.set(Calendar.DAY_OF_MONTH,day_int);
                        c.set(Calendar.HOUR_OF_DAY,hour_int);
                        c.set(Calendar.MINUTE,minute_int);
                        c.set(Calendar.SECOND,0);


                        mAlarm= (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                        Intent intent=new Intent(AddActivity.this, MyReceiver.class);
                        Bundle data=new Bundle();
                        data.putString("task",tasktext);
                        data.putString("date",datetext);
                        intent.putExtras(data);

                        PendingIntent pendingIntent=PendingIntent.getBroadcast(AddActivity.this,alarm_id,intent,0);


                        if(cycle_timetext.equals("")) {
                            mAlarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);


                        }else
                        {
                            String  number_ofCycle=cycle_timetext.substring(1,2);
                            String  unit_ofCycle=cycle_timetext.substring(3);

                            int  number_int=Integer.parseInt(number_ofCycle);
                            long  cycle_int;
                            if(unit_ofCycle.equals("分"))
                                cycle_int=number_int*60000;
                            else if(unit_ofCycle.equals("时"))
                                cycle_int=number_int*3600000;
                            else if(unit_ofCycle.equals("天"))
                                cycle_int=number_int*3600000*24;
                            else if(unit_ofCycle.equals("周"))
                                cycle_int=number_int*3600000*24*7;
                            else
                                cycle_int=number_int*3600000*24*30;

                            Log.d("有重复提醒周期", cycle_timetext + "");

                            mAlarm.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),cycle_int,pendingIntent);
                        }

                    }

                    Intent intent1=new Intent(AddActivity.this,MainActivity.class);
                    startActivity(intent1);
                    finish();

                }
            }
        });


        //分类按钮
        iv_type_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, SortActivity.class);
                startActivityForResult(intent,0);
            }
        });

        //日期选择按钮
        iv_date_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateTimePickDialogUtil1 dateTimePicKDialog = new DateTimePickDialogUtil1(
                        AddActivity.this, initEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(et_date);
            }
        });
    }


    private void initView() {
        setContentView(R.layout.activity_add);
        final LinearLayout repeat_layout = (LinearLayout)AddActivity.this.getLayoutInflater().inflate(R.layout.repeat_set_custom, null);   //自定义周期界面


        ib_back = (ImageButton) findViewById(R.id.ib_back);  //回到主界面按钮
        iv_comfirm = (ImageView) findViewById(R.id.iv_confirm);  //确认按钮
        iv_type_icon = (ImageView) findViewById(R.id.iv_type_icon); //分类按钮
        iv_date_icon = (ImageView) findViewById(R.id.iv_calendar_icon);  //日期选择按钮
        iv_alarm_icon=(ImageView)findViewById(R.id.iv_alarm_icon); //提醒按钮
        iv_remark_icon=(ImageView)findViewById(R.id.iv_remark_icon);  //备注按钮
        et_date = (EditText)findViewById(R.id.et_date); //日期文本框
        et_add=(EditText)findViewById(R.id.et_add); //任务文本框
        et_type=(EditText)findViewById(R.id.et_type);  //分类文本框
        et_alarm=(EditText)findViewById(R.id.et_alarm);  //重复周期文本框
        et_remark=(EditText)findViewById(R.id.et_remark); //备注文本框
        sp_num=(Spinner)repeat_layout.findViewById(R.id.spinner2);
        sp_cycle=(Spinner)repeat_layout.findViewById(R.id.spinner3);

    }


}

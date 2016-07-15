package com.example.hp.pumpkin2.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.hp.pumpkin2.MainActivity;
import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.db.MySqliteHelper;
import com.example.hp.pumpkin2.utils.DateTimePickDialogUtil1;

/**
 * Created by hp on 2016/6/9.
 */
public class UpdateActivity extends Activity {


    EditText et_add; //任务文本框
    EditText et_date; //日期文本框
    EditText et_type; //任务类型文本框
    EditText et_alarm; //周期文本框
    EditText et_remark; //备注文本框
    Button bt_edit; //修改按钮&&保存按钮
    ImageView iv_delete;//删除按钮
    ImageView iv_date_icon; //日期选择
    ImageView  iv_type_icon; //分类按钮
    ImageView  iv_alarm_icon; //提醒周期按钮
    ImageView  iv_remark_icon; //备注按钮
    ImageButton ib_back; //返回主界面
    boolean isread=true; //是否在阅读状态
    MySqliteHelper mySqliteHelper=new MySqliteHelper(this,"mydata.db",null,1);  //mydata.db的数据库
    private String initEndDateTime = "2016年2月22日 17:44"; // 初始化结束时间
    AlertDialog ad; //提醒周期对话框


    Spinner sp1; //选择周期对话框  数目
    Spinner  sp2; //             周期单位




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        et_type=(EditText)findViewById(R.id.et_type);  //分类文本框
        if (requestCode == 0 && resultCode == 0) {
            String resultsort = data.getStringExtra("sort");
            et_type.setText(resultsort);

        }

        et_remark=(EditText)findViewById(R.id.et_remark);  //备注文本框
        if(requestCode == 1 && resultCode == 3){
            String  resultRemark=data.getStringExtra("remark");
            et_remark.setText(resultRemark);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
        initData();

    }

    private void initData() {
        final SQLiteDatabase db=mySqliteHelper.getWritableDatabase();

        final Intent intent =getIntent();
        String  tasktext=intent.getStringExtra("task");
        String  datetext =intent.getStringExtra("date");
        String  task_typetext=intent.getStringExtra("task_type");
        String  cycle_time=intent.getStringExtra("cycle_time");
        final String  remark=intent.getStringExtra("remark");

        final int id=intent.getExtras().getInt("_id");

        setEdittext(tasktext, datetext, task_typetext,cycle_time,remark);
        setEdittextDisable();
        setColortoWhite();

        //备注点击按钮
        iv_remark_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1=new Intent(UpdateActivity.this,RemarkActivity.class);
                Bundle data=new Bundle();
                data.putString("remark", remark);
                intent1.putExtras(data);
                startActivityForResult(intent1, 1);


            }
        });


        //返回主界面
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });


        //修改
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isread)   //阅读状态
                {
                    setEdittextAble();
                    setColortoBlack();
                    isread=false;

                    iv_delete.setBackgroundResource(R.drawable.after_bianji_queren);

                    //修改日期时间
                    iv_date_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DateTimePickDialogUtil1 dateTimePicKDialog = new DateTimePickDialogUtil1(
                                    UpdateActivity.this, initEndDateTime);
                            dateTimePicKDialog.dateTimePicKDialog(et_date);


                        }
                    });

                    //分类按钮
                    iv_type_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UpdateActivity.this, SortActivity.class);
                            startActivityForResult(intent, 0);
                            finish();
                        }
                    });

                    //提醒周期按钮
                    iv_alarm_icon.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            final LinearLayout repeat_layout = (LinearLayout)UpdateActivity.this.getLayoutInflater().inflate(R.layout.repeat_set_custom, null);   //自定义周期界面

                            ad = new AlertDialog.Builder(UpdateActivity.this)
                                    .setTitle("设置重复周期")
                                    .setView(repeat_layout)
                                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String cycleTime="每 "+sp1.getSelectedItem().toString()+""+sp2.getSelectedItem().toString();
                                            et_alarm.setText(cycleTime);
                                            ad.dismiss();

                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                        }
                                    }).show();

                        }

                    });

                }

            }
        });

        //删除&&保存按钮
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isread){   //阅读状态可删除
                    new AlertDialog.Builder(UpdateActivity.this)
                            .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.delete("my_table","_id="+String.valueOf(id),null);

                                    Intent  intent1=new Intent(UpdateActivity.this,MainActivity.class);
                                    startActivity(intent1);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setTitle("是否要删除").create().show();

                }
                else{  //修改状态可以保存

                    ContentValues values=new ContentValues();
                    String task_text=et_add.getText().toString();
                    String date_text=et_date.getText().toString();
                    String task_type_text=et_type.getText().toString();
                    String  cycle_timetext=et_alarm.getText().toString();
                    String  remarktext=et_remark.getText().toString();

                    values.put("task", task_text);
                    values.put("date", date_text);
                    values.put("task_type", task_type_text);
                    values.put("cycle_time",cycle_timetext);
                    values.put("remark",remarktext);

                    db.update("my_table", values, "_id=" + String.valueOf(id), null);

                    setEdittextDisable();
                    setColortoWhite();
//                    bt1.setText("");
                    isread=true;
                    iv_delete.setBackgroundResource(R.drawable.lajitong_2);

                }


            }
        });



    }

    private void initView() {
        final LinearLayout repeat_layout = (LinearLayout)UpdateActivity.this.getLayoutInflater().inflate(R.layout.repeat_set_custom, null);   //自定义周期界面
        et_add=(EditText)findViewById(R.id.et_add);
        et_date=(EditText)findViewById(R.id.et_date);
        et_type=(EditText)findViewById(R.id.et_type);
        et_alarm=(EditText)findViewById(R.id.et_alarm);
        et_remark=(EditText)findViewById(R.id.et_remark);
        bt_edit=(Button)findViewById(R.id.bt_edit);
        iv_delete=(ImageView)findViewById(R.id.iv_delete);
        ib_back=(ImageButton)findViewById(R.id.ib_back);
        iv_date_icon=(ImageView)findViewById(R.id.iv_date_icon);
        iv_type_icon=(ImageView)findViewById(R.id.iv_type_icon);
        iv_alarm_icon=(ImageView)findViewById(R.id.iv_alarm_icon);
        iv_remark_icon=(ImageView)findViewById(R.id.iv_remark_icon);
        sp1=(Spinner)repeat_layout.findViewById(R.id.spinner2);
        sp2=(Spinner)repeat_layout.findViewById(R.id.spinner3);


    }


    //设置文本框的显示内容
    public void setEdittext(String task,String date,String task_type,String  cycle_time,String remark)
    {
        et_add=(EditText)findViewById(R.id.et_add);
        et_date=(EditText)findViewById(R.id.et_date);
        et_type=(EditText)findViewById(R.id.et_type);
        et_alarm=(EditText)findViewById(R.id.et_alarm);
        et_remark=(EditText)findViewById(R.id.et_remark);

        et_add.setText(task);
        et_date.setText(date);
        et_type.setText(task_type);
        et_alarm.setText(cycle_time);
        et_remark.setText(remark);
    }

    //设置文本框不可编辑&&按钮不可点击
    public void  setEdittextDisable()
    {
        et_add=(EditText)findViewById(R.id.et_add);
        et_date=(EditText)findViewById(R.id.et_date);
        et_type=(EditText)findViewById(R.id.et_type);
        et_alarm=(EditText)findViewById(R.id.et_alarm);
        et_remark=(EditText)findViewById(R.id.et_remark);
        iv_date_icon=(ImageView)findViewById(R.id.iv_date_icon);
        iv_type_icon=(ImageView)findViewById(R.id.iv_type_icon);
        iv_alarm_icon=(ImageView)findViewById(R.id.iv_alarm_icon);
        iv_remark_icon=(ImageView)findViewById(R.id.iv_remark_icon);

        et_add.setEnabled(false);
        et_date.setEnabled(false);
        et_type.setEnabled(false);
        et_alarm.setEnabled(false);
        et_remark.setEnabled(false);
        iv_date_icon.setEnabled(false);
        iv_type_icon.setEnabled(false);
        iv_alarm_icon.setEnabled(false);
        iv_remark_icon.setEnabled(false);



    }


    //设置字体颜色为白色
    public  void setColortoWhite()
    {
        et_add=(EditText)findViewById(R.id.et_add);
        et_date=(EditText)findViewById(R.id.et_date);
        et_type=(EditText)findViewById(R.id.et_type);
        et_alarm=(EditText)findViewById(R.id.et_alarm);
        et_remark=(EditText)findViewById(R.id.et_remark);

        et_add.setTextColor(getResources().getColor(R.color.gray));
        et_date.setTextColor(getResources().getColor(R.color.gray));
        et_type.setTextColor(getResources().getColor(R.color.gray));
        et_alarm.setTextColor(getResources().getColor(R.color.gray));
        et_remark.setTextColor(getResources().getColor(R.color.gray));
    }

    //设置文本框可编辑&&按钮可点击
    public void  setEdittextAble()
    {
        et_add=(EditText)findViewById(R.id.et_add);
        et_date=(EditText)findViewById(R.id.et_date);
        et_type=(EditText)findViewById(R.id.et_type);
        et_alarm=(EditText)findViewById(R.id.et_alarm);
        et_remark=(EditText)findViewById(R.id.et_remark);
        iv_date_icon=(ImageView)findViewById(R.id.iv_date_icon);
        iv_type_icon=(ImageView)findViewById(R.id.iv_type_icon);
        iv_alarm_icon=(ImageView)findViewById(R.id.iv_alarm_icon);
        iv_remark_icon=(ImageView)findViewById(R.id.iv_remark_icon);

        et_add.setEnabled(true);
        et_date.setEnabled(true);
        et_type.setEnabled(true);
        et_alarm.setEnabled(true);
        et_remark.setEnabled(true);
        iv_date_icon.setEnabled(true);
        iv_type_icon.setEnabled(true);
        iv_alarm_icon.setEnabled(true);
        iv_remark_icon.setEnabled(true);


    }


    //设置字体颜色为黑色
    public  void setColortoBlack()
    {
        et_add=(EditText)findViewById(R.id.et_add);
        et_date=(EditText)findViewById(R.id.et_date);
        et_type=(EditText)findViewById(R.id.et_type);
        et_alarm=(EditText)findViewById(R.id.et_alarm);
        et_remark=(EditText)findViewById(R.id.et_remark);


        et_add.setTextColor(getResources().getColor(R.color.black));
        et_date.setTextColor(getResources().getColor(R.color.black));
        et_type.setTextColor(getResources().getColor(R.color.black));
        et_alarm.setTextColor(getResources().getColor(R.color.black));
        et_remark.setTextColor(getResources().getColor(R.color.black));

    }



}

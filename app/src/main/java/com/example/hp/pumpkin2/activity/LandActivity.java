package com.example.hp.pumpkin2.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hp.pumpkin2.MainActivity;
import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.db.MySqliteHelper;
import com.example.hp.pumpkin2.db.my_table;
import com.example.hp.pumpkin2.domain.coordinate;
import com.example.hp.pumpkin2.domain.date_woods;
import com.example.hp.pumpkin2.utils.DateTimePickDialogUtil2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hp on 2016/6/9.
 */
public class LandActivity extends Activity{
    public  static  final  int UPDATE_SIGN=1; //执行修改界面
    SimpleDateFormat formatter   =   new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    Date curDate   =   new   Date(System.currentTimeMillis());
    String   initEndDateTime =   formatter.format(curDate);  // 初始化点击开始时间
    private EditText et_date ;  //日期显示
    private ImageButton ib_back;  //返回按钮
    RelativeLayout rl_land; //土地界面
    private ImageView iv_calendar; //日期选择
    TextView  tv_live_num; //南瓜存活个数
    TextView tv_dead_num; //南瓜存活个数
    ImageView iv_share; //分享图标
    ImageView  iv_live; //活南瓜
    ImageView iv_dead; //死南瓜
    ImageView iv_xiaomiao; //南瓜苗
    MySqliteHelper mySqliteHelper=new MySqliteHelper(this,"mydata.db",null,1);  //mydata.db的数据库
    public  static int  last_code=0;
    public   int  code=0;
    private date_woods dateWoods=new date_woods();
    public Handler handler;


    ImageView[] ngs=new ImageView[36];
    Integer[]  ids=new Integer[]{R.id.nangua1,R.id.nangua2,R.id.nangua3,R.id.nangua4,R.id.nangua5,R.id.nangua6
            ,R.id.nangua7,R.id.nangua8,R.id.nangua9,R.id.nangua10,R.id.nangua11,R.id.nangua12
            ,R.id.nangua13,R.id.nangua14,R.id.nangua15,R.id.nangua16,R.id.nangua17,R.id.nangua18
            ,R.id.nangua19,R.id.nangua20,R.id.nangua21,R.id.nangua22,R.id.nangua23,R.id.nangua24
            ,R.id.nangua25,R.id.nangua26,R.id.nangua27,R.id.nangua28,R.id.nangua29,R.id.nangua30
            ,R.id.nangua31,R.id.nangua32,R.id.nangua33,R.id.nangua34,R.id.nangua35,R.id.nangua36
    };

    coordinate[]  position=new coordinate[]{new coordinate(453,116),new coordinate(381,156),new coordinate(279,214),new coordinate(209,271),new coordinate(129,316),new coordinate(41,364)
            ,new coordinate(526,164),new coordinate(459,205),new coordinate(381,255),new coordinate(309,319),new coordinate(220,367),new coordinate(128,426)
            ,new coordinate(617,208),new coordinate(542,264),new coordinate(468,310),new coordinate(398,366),new coordinate(307,414),new coordinate(220,469)
            ,new coordinate(704,266),new coordinate(623,311),new coordinate(552,358),new coordinate(448,423),new coordinate(379,469),new coordinate(287,520)
            ,new coordinate(772,313),new coordinate(687,356),new coordinate(614,404),new coordinate(508,474),new coordinate(441,513),new coordinate(369,563)
            ,new coordinate(871,360),new coordinate(784,419),new coordinate(703,465),new coordinate(614,532),new coordinate(531,578),new coordinate(467,620)};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);

        initView();
        initdata();

    }

    private void initdata() {
        //子线程中更新UI
        handler=new Handler(){

            public  void  handleMessage(Message msg){
                if(msg.what==UPDATE_SIGN){
                    int  code=0;

                    tv_live_num.setText(dateWoods.done_on + "");
                    tv_dead_num.setText(dateWoods.done_not + "");


                    for(int i=0;i<last_code;i++){
                        ngs[i].setVisibility(View.GONE);
                    }



                    for (int i = 0; i < dateWoods.todo_num; i++, code++) {
                        ngs[code].setX(position[code].x);
                        ngs[code].setY(position[code].y);
                        ngs[code].setBackgroundResource(R.drawable.land_miao_p);


                    }

                    for (int i = 0; i < dateWoods.done_on; i++, code++) {
                        ngs[code].setX(position[code].x);
                        ngs[code].setY(position[code].y);
                        ngs[code].setBackgroundResource(R.drawable.land_live_pumpkin);
                    }

                    for (int i = 0; i < dateWoods.done_not; i++, code++) {
                        ngs[code].setX(position[code].x);
                        ngs[code].setY(position[code].y);
                        ngs[code].setBackgroundResource(R.drawable.land_death_pumpkin);

                    }


                    for(int i=0;i<code;i++){
                        ngs[i].setVisibility(View.VISIBLE);
                    }


                    last_code=code;

                }
            }
        };


        //初始化为当前时间
        Intent intent=getIntent();
        String curdate=intent.getStringExtra("curTime");
        et_date.setText(curdate);

        String dateText=et_date.getText().toString().substring(0,10);

        Log.d("----------",""+dateText);
        dateWoods.todo_list = new my_table(LandActivity.this).getTodolist(dateText);
        dateWoods.done_on_list = new my_table(LandActivity.this).getDone_onlist(dateText);
        dateWoods.done_not_list = new my_table(LandActivity.this).getDone_notlist(dateText);

        dateWoods.todo_num = new my_table(LandActivity.this).getNum(dateWoods.todo_list);
        dateWoods.done_on = new my_table(LandActivity.this).getNum(dateWoods.done_on_list);
        dateWoods.done_not = new my_table(LandActivity.this).getNum(dateWoods.done_not_list);

        dateWoods.todo_taskArray =new my_table(LandActivity.this).getStringArray(dateWoods.todo_list);
        dateWoods.doneOn_taskArray = new my_table(LandActivity.this).getStringArray(dateWoods.done_on_list);
        dateWoods.doneNot_taskArray = new my_table(LandActivity.this).getStringArray(dateWoods.done_not_list);



        tv_live_num.setText(dateWoods.done_on + "");
        tv_dead_num.setText(dateWoods.done_not + "");

        for(int i=0;i<last_code;i++){
            ngs[i].setVisibility(View.GONE);
        }



        for (int i = 0; i < dateWoods.todo_num; i++, code++) {
            ngs[code].setX(position[code].x);
            ngs[code].setY(position[code].y);
            ngs[code].setBackgroundResource(R.drawable.land_miao_p);


        }

        for (int i = 0; i < dateWoods.done_on; i++, code++) {
            ngs[code].setX(position[code].x);
            ngs[code].setY(position[code].y);
            ngs[code].setBackgroundResource(R.drawable.land_live_pumpkin);
        }

        for (int i = 0; i < dateWoods.done_not; i++, code++) {
            ngs[code].setX(position[code].x);
            ngs[code].setY(position[code].y);
            ngs[code].setBackgroundResource(R.drawable.land_death_pumpkin);

        }


        for(int i=0;i<code;i++){
            ngs[i].setVisibility(View.VISIBLE);
        }


        last_code=code;



        //分享
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  dateText=et_date.getText().toString();
                String shareText=dateText+",我在pumkin里面通过完成"+dateWoods.done_on+"个任务后种植成功了"+dateWoods.done_on+"个南瓜。你也来试试吧！\r\n(下载地址)";
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,"分享到"));


            }
        });


        //南瓜苗
        iv_xiaomiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LandActivity.this);  //先得到构造器
                builder.setTitle("待办任务: " + dateWoods.todo_num); //设置标题
                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                builder.setItems(dateWoods.todo_taskArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();

            }
        });


        //活南瓜任务详情
        iv_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LandActivity.this);  //先得到构造器
                builder.setTitle("按时完成的任务:  " + dateWoods.done_on); //设置标题
                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                builder.setItems(dateWoods.doneOn_taskArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();


            }
        });

        //死南瓜任务详情
        iv_dead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LandActivity.this);  //先得到构造器
                builder.setTitle("未能按时完成的任务:  " + dateWoods.done_not); //设置标题
                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                builder.setItems(dateWoods.doneNot_taskArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();

            }
        });



        //点击日期选择对话框
        iv_calendar.setOnClickListener(new View.OnClickListener() {  //日期选择
            @Override
            public void onClick(View v) {

                DateTimePickDialogUtil2 dateTimePicKDialog = new DateTimePickDialogUtil2(
                        LandActivity.this, initEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(et_date);

            }
        });




        //当日期显示框的字体改变后事实刷新当日南瓜情况
        et_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message=new Message();
                        message.what=UPDATE_SIGN;
                        handler.sendMessage(message);

                    }
                }).start();
                String need_time = et_date.getText().toString().substring(0,10);  //需要显示日期时间   年-月-日

                dateWoods.todo_list = new my_table(LandActivity.this).getTodolist(need_time);
                dateWoods.done_on_list = new my_table(LandActivity.this).getDone_onlist(need_time);
                dateWoods.done_not_list = new my_table(LandActivity.this).getDone_notlist(need_time);

                dateWoods.todo_num = new my_table(LandActivity.this).getNum(dateWoods.todo_list);
                dateWoods.done_on = new my_table(LandActivity.this).getNum(dateWoods.done_on_list);
                dateWoods.done_not = new my_table(LandActivity.this).getNum(dateWoods.done_not_list);

                dateWoods.todo_taskArray =new my_table(LandActivity.this).getStringArray(dateWoods.todo_list);
                dateWoods.doneOn_taskArray = new my_table(LandActivity.this).getStringArray(dateWoods.done_on_list);
                dateWoods.doneNot_taskArray = new my_table(LandActivity.this).getStringArray(dateWoods.done_not_list);

            }
        });


        //返回
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


    }

    private void initView() {
        et_date = (EditText) findViewById(R.id.et_date);  //日期显示
        ib_back= (ImageButton)  findViewById(R.id.ib_back);  //返回按钮
        iv_calendar=(ImageView)findViewById(R.id.iv_calendar); //日期选择按钮
        tv_live_num=(TextView)findViewById(R.id.tv_live_num); //南瓜存活个数
        tv_dead_num=(TextView)findViewById(R.id.tv_dead_num); //南瓜死亡个数
        rl_land=(RelativeLayout)findViewById(R.id.rl_land);
        iv_live=(ImageView)findViewById(R.id.iv_live);
        iv_dead=(ImageView)findViewById(R.id.iv_dead);
        iv_xiaomiao=(ImageView)findViewById(R.id.iv_xiaomiao);
        iv_share=(ImageView)findViewById(R.id.iv_share);

        for(int i=0;i<35;i++){                           //初始化南瓜控件
            ngs[i]=(ImageView)findViewById(ids[i]);
        }

    }
}

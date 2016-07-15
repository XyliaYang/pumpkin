package com.example.hp.pumpkin2.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.activity.AddActivity;
import com.example.hp.pumpkin2.activity.UpdateActivity;
import com.example.hp.pumpkin2.adapter.ListItemClickAdapter;
import com.example.hp.pumpkin2.adapter.ListItemClickAdapter2_done;
import com.example.hp.pumpkin2.db.MySqliteHelper;
import com.example.hp.pumpkin2.db.my_table;
import com.example.hp.pumpkin2.utils.ListItemClickHelp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by hp on 2016/6/7.
 */
public class ContentFragment extends BaseFragment  implements ListItemClickHelp,GestureDetector.OnGestureListener,
        View.OnTouchListener {

    Button   bt_todo;
    Button   bt_done;
    ListView lv_task;  //任务列表
    ImageView iv_add; //添加任务按钮
    ArrayList<HashMap<String,Object>> listItem_todo=new ArrayList<HashMap<String, Object>>(); //显示所有待办list
    ArrayList<String> listString_todo;  //显示待办任务数据list,因为点击项的两个按钮需要才加入这个string数组
    private ListItemClickAdapter madapter ;
    ArrayList<HashMap<String,Object>> listItem_done=new ArrayList<HashMap<String, Object>>(); //所有已完成数组信息
    ArrayList<String>   listString_done; //显示完成列表
    private ListItemClickAdapter2_done madapter_done;  //完成显示列表
    private   static int encourage_int=0;  //控制完成任务鼓励话语的输出种类
    MySqliteHelper mySqliteHelper=new MySqliteHelper(myactivity,"mydata.db",null,1);  //mydata.db的数据库
    RelativeLayout rl_bg;
    int[] bg_resources;
    int flag=0;
    private GestureDetector mGesture = null;

    public View initView() {
        // TODO Auto-generated method stub
        View view = View.inflate(myactivity, R.layout.fragment_content,null);

        bt_todo=(Button)view.findViewById(R.id.bt_todo);  //
        bt_done=(Button)view.findViewById(R.id.bt_done);  //
        lv_task= (ListView) view.findViewById(R.id.lv_task);  //
        iv_add= (ImageView)view.findViewById(R.id.iv_add); //
        rl_bg= (RelativeLayout) view.findViewById(R.id.rl_bg);
        bg_resources=new int[]{R.drawable.background1,R.drawable.background2,R.drawable.background3,R.drawable.background4};


        return view;
    }

    @Override
    public void initData() {

        rl_bg.setOnTouchListener((View.OnTouchListener) this);
        rl_bg.setLongClickable(true);
        mGesture = new GestureDetector((GestureDetector.OnGestureListener) this);
        listItem_todo=new my_table(myactivity).getALLlist_todo();
        listString_todo=new my_table(myactivity).getTaskList_todo();
        listItem_done= new my_table(myactivity).getALLlist_done();
        listString_done= new my_table(myactivity).getTaskList_done();

        //todo按钮
        bt_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listString_todo=new my_table(myactivity).getTaskList_todo();
                madapter = new ListItemClickAdapter(myactivity,listString_todo,(ListItemClickHelp) ContentFragment.this);
                lv_task.setAdapter(madapter);

            }
        });


        //done按钮
        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //列表显示
                listString_done= new my_table(myactivity).getTaskList_done();
                madapter_done = new ListItemClickAdapter2_done(myactivity,listString_done,(ListItemClickHelp) ContentFragment.this);
                lv_task.setAdapter(madapter_done);
            }
        });


        //未完成列表显示
        listString_todo=new my_table(myactivity).getTaskList_todo();
        madapter = new ListItemClickAdapter(myactivity,listString_todo,(ListItemClickHelp) this);
        lv_task.setAdapter(madapter);


        //添加任务,如果需要刷新就必须start activity,不需要则可直接finish
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(myactivity,AddActivity.class);
                startActivity(intent1);
                myactivity.finish();
            }
        });

    }


    @Override
    public void onClick(View item, View widget, int position, int which) {  //position代表该List的行数，从第0行开始,而数据库的id从1开始

        MySqliteHelper mySqliteHelper=new MySqliteHelper(myactivity,"mydata.db",null,1);  //mydata.db的数据库
        final SQLiteDatabase db=mySqliteHelper.getWritableDatabase();
        switch (which) {
            case R.id.not_complete:                  //撤销此任务的完成状态
                listItem_done= new my_table(myactivity).getALLlist_done();

                HashMap one1 = listItem_done.get(position);
                int sql_id= Integer.parseInt(String.valueOf(one1.get("_id")));
                ContentValues values=new ContentValues();
                values.put("state","todo");              //原来更新一行数据只需要将需要修改的字段更新就可以了
                values.put("done_time", "");
                db.update("my_table", values, "_id=" + String.valueOf(sql_id), null);

                //重修刷新列表显示,把撤销完成的任务从完成列表里面删除
                listString_done= new my_table(myactivity).getTaskList_done();
                madapter_done = new ListItemClickAdapter2_done(myactivity,listString_done,(ListItemClickHelp) ContentFragment.this);
                lv_task.setAdapter(madapter_done);


                break;

            case  R.id.done_delete:          //在数据库删除此任务
                listItem_done=new my_table(myactivity).getALLlist_done();

                HashMap one = listItem_done.get(position);
                final int sql_id2=Integer.parseInt(String.valueOf(one.get("_id")));

                new AlertDialog.Builder(myactivity)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                db.delete("my_table", "_id=" + String.valueOf(sql_id2), null);

                                //重修刷新列表显示,把撤销完成的任务从完成列表里面删除*/
                                listString_done= new my_table(myactivity).getTaskList_done();
                                madapter_done= new ListItemClickAdapter2_done(myactivity,listString_done,(ListItemClickHelp)ContentFragment.this);
                                lv_task.setAdapter(madapter_done);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle("是否要删除该任务?").create().show();



                break;

            case R.id.complete_view:                  //在完成时将任务完成状态和完成的日期时间输入到数据库
                listItem_todo= new my_table(myactivity).getALLlist_todo();     //原因在于需要更新吗？？？？

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                Date  date=new Date(System.currentTimeMillis());
                String date_text=simpleDateFormat.format(date);

                HashMap one2 = listItem_todo.get(position);
                int sql_id3= Integer.parseInt(String.valueOf(one2.get("_id")));
                ContentValues values1=new ContentValues();
                values1.put("state","done");              //原来更新一行数据只需要将需要修改的字段更新就可以了
                values1.put("done_time", date_text);
                db.update("my_table", values1, "_id=" + String.valueOf(sql_id3), null);



                switch(encourage_int) {

                    case 0:
                        Toast.makeText(myactivity,"用行动祈祷比用言语更能够使上帝了解。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;
                        break;

                    case 1:
                        Toast.makeText(myactivity,"当你尽了自己的最大努力时，失败也是伟大的。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;
                    case 2:
                        Toast.makeText(myactivity,"勤奋是天才的摇篮，耕耘是智慧的源泉。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;

                    case 3:
                        Toast.makeText(myactivity,"与其临渊羡鱼，不如退而结网。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;

                    case 4:
                        Toast.makeText(myactivity,"精神的浩瀚，想象的活跃，心灵的勤奋，孩子，这是你成功的保证。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;

                    case 5:
                        Toast.makeText(myactivity,"做勤劳的小蜜蜂吧，你会品尝到成功的喜悦。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;

                    case 6:
                        Toast.makeText(myactivity,"精诚所至，金石为开。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;

                    case 7:
                        Toast.makeText(myactivity,"路是自己选的，就要对自己负责。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;

                    case 8:
                        Toast.makeText(myactivity,"不要等待机会，而要创造机会。",Toast.LENGTH_LONG).show();
                        encourage_int=(encourage_int+1)%9;

                        break;

                    default:
                        System.out.println("不知道应该输出什么。");

                }


                //重修刷新列表显示,把完成的内容在界面上删除
                listString_todo=new my_table(myactivity).getTaskList_todo();
                madapter = new ListItemClickAdapter(myactivity,listString_todo,(ListItemClickHelp) this);
                lv_task.setAdapter(madapter);

                break;
            case  R.id.read_view:

                listItem_todo=new my_table(myactivity).getALLlist_todo();

                HashMap  one3 = listItem_todo.get(position);
                Intent intent = new Intent(myactivity,UpdateActivity.class);

                Bundle data = new Bundle();
                data.putInt("_id", Integer.parseInt(String.valueOf(one3.get("_id"))));
                data.putString("task", String.valueOf(one3.get("task")));
                data.putString("date", String.valueOf(one3.get("date")));
                data.putString("task_type", String.valueOf(one3.get("task_type")));
                data.putString("cycle_time", String.valueOf(one3.get("cycle_time")));
                data.putString("remark", String.valueOf(one3.get("remark")));

                intent.putExtras(data);

                startActivityForResult(intent, position);  //标识修改项的位置
//                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() < -100) { // 向右滑动
            rl_bg.setBackgroundResource(bg_resources[(flag++)%4]);

        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.mGesture.onTouchEvent(event);
    }
}
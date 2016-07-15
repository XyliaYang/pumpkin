package com.example.hp.pumpkin2.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;

import com.example.hp.pumpkin2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by hp on 2016/6/9.
 * 南瓜信息表及方法集合
 */
public class my_table {

    private static Context context;
    public my_table(Context context)
    {
        this.context = context;
    }


    public static ArrayList<HashMap<String,Object>>   getALLlist_todo(){
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String , Object>>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();
        Cursor c = db.query("my_table", new String[]{"_id", "task", "date", "task_type","cycle_time","remark","state","done_time"}, null, null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                HashMap<String, Object> item = new HashMap<String, Object>();

                if(c.getString(c.getColumnIndex("state")).equals("todo")) {
                    item.put("_id", c.getInt(c.getColumnIndex("_id")));
                    item.put("task", c.getString(c.getColumnIndex("task")));
                    item.put("date", c.getString(c.getColumnIndex("date")));
                    item.put("task_type", c.getString(c.getColumnIndex("task_type")));
                    item.put("cycle_time", c.getString(c.getColumnIndex("cycle_time")));
                    item.put("remark", c.getString(c.getColumnIndex("remark")));
                    item.put("state", c.getString(c.getColumnIndex("state")));
                    item.put("done_time",c.getString(c.getColumnIndex("done_time")));
                    item.put("image", R.drawable.quan1);

                    list.add(item);
                }
            }
        }
        c.close();
        return list;

    }


    //取出数据库里面所有待办的任务到一个list里
    public static ArrayList<String>  getTaskList_todo()
    {
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();
        Cursor c = db.query("my_table", new String[]{"task","state"}, null, null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                if(c.getString(c.getColumnIndex("state")).equals("todo")) {

                    String item = new String();
                    item = c.getString(c.getColumnIndex("task"));
                    list.add(item);

                }

            }
        }

        c.close();
        return list;

    }

    //获取数据库的所有不重复的分类信息到List
    public ArrayList<HashMap<String,Object>>   getTypelist() {
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();
        Cursor c = db.query("my_table", new String[]{"_id", "task", "date", "task_type"}, null, null, null, null, null);
        boolean  isreapt=false; //重复
        boolean  isaddnotsorted=false; //有没有添加未分类选项

        if (c != null) {
            while (c.moveToNext()) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                for (HashMap<String, Object>  temp:list){
                    if (c.getString(c.getColumnIndex("task_type")).equals(temp.get("task_type"))){
                        isreapt=true;
                        break;
                    }
                }

                if(isreapt==false){
                    if((c.getString(c.getColumnIndex("task_type")).equals(""))&&(isaddnotsorted==false))
                    {
                        item.put("_id", c.getInt(c.getColumnIndex("_id")));
                        item.put("task_type","未分类");
                        item.put("image", R.drawable.type_two);

                        list.add(item);
                        isaddnotsorted=true;
                    }


                    else if (!(c.getString(c.getColumnIndex("task_type")).equals(""))) {
                        item.put("_id", c.getInt(c.getColumnIndex("_id")));
                        item.put("task_type", c.getString(c.getColumnIndex("task_type")));
                        item.put("image", R.drawable.type_two);

                        list.add(item);
                    }
                }
                isreapt=false;
            }
        }

        c.close();
        return list;
    }



    //获取数据库的所有已完成信息到List
    public ArrayList<HashMap<String,Object>>   getALLlist_done() {
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String , Object>>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();
        Cursor c = db.query("my_table", new String[]{"_id", "task", "date", "task_type","cycle_time","remark","state"}, null, null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                HashMap<String, Object> item = new HashMap<String, Object>();

                if(c.getString(c.getColumnIndex("state")).equals("done")) {
                    item.put("_id", c.getInt(c.getColumnIndex("_id")));
                    item.put("task", c.getString(c.getColumnIndex("task")));
                    item.put("date", c.getString(c.getColumnIndex("date")));
                    item.put("task_type", c.getString(c.getColumnIndex("task_type")));
                    item.put("cycle_time", c.getString(c.getColumnIndex("cycle_time")));
                    item.put("remark", c.getString(c.getColumnIndex("remark")));
                    item.put("state", c.getString(c.getColumnIndex("state")));
                    item.put("image", R.drawable.after_bianji_queren);

                    list.add(item);

                }

            }
        }
        c.close();
        return list;
    }



    //取出数据库里面所有已完成的任务到一个list里
    public  ArrayList<String>  getTaskList_done()
    {
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();
        Cursor c = db.query("my_table", new String[]{"task","state"}, null, null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                if(c.getString(c.getColumnIndex("state")).equals("done")) {

                    String item = new String();
                    item = c.getString(c.getColumnIndex("task"));
                    list.add(item);

                }

            }
        }

        c.close();
        return list;

    }



    //获取id、task的arraylist转换为string数组
    public String[]  getStringArray(ArrayList<HashMap<String, Object>> todo_list) {
        ArrayList<String>  list1=new  ArrayList<String>();

        for (HashMap<String, Object>  one:todo_list) {
            list1.add(one.get("task").toString());

        }

        String[] list=new String[list1.size()];
        for(int i=0;i<list1.size();i++){
            list[i]=list1.get(i);
        }

        return list;
    }


    //返回当天的任务数量
    public int getNum(ArrayList<HashMap<String, Object>> list) {
        return list.size();
    }



    //返回need_time日期要求当天未按时完成的list   id、task
    public ArrayList<HashMap<String, Object>> getDone_notlist(String need_time) {
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        SimpleDateFormat  simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        Date date=new Date(System.currentTimeMillis());
        String  curToday=simpleDateFormat.format(date).substring(0,10);

        SQLiteDatabase db=mySqliteHelper.getReadableDatabase();
        ArrayList<HashMap<String, Object>>  list=new   ArrayList<HashMap<String, Object>>();

        Cursor c = db.query("my_table", new String[]{"_id", "task", "date", "task_type","cycle_time","remark","state","done_time"}, null, null, null, null, null);
        if(c!=null){
            while (c.moveToNext()) {
                if(!(c.getString(c.getColumnIndex("date")).equals(""))){


                    HashMap<String, Object> item = new HashMap<String, Object>();
                    String date_day = c.getString(c.getColumnIndex("date")).substring(0, 10);
                    String date_complete = c.getString(c.getColumnIndex("done_time")).substring(0, 10);
                    if (need_time.equals(date_day)) {
                        if (c.getString(c.getColumnIndex("state")).equals("done")) {  //做完但是做完时间延长
                            if (!Dayudengyu(date_day, date_complete)) {
                                item.put("_id", c.getInt(c.getColumnIndex("_id")));
                                item.put("task", c.getString(c.getColumnIndex("task")));

                                list.add(item);

                            }

                        } else if (c.getString(c.getColumnIndex("state")).equals("todo")) {  //一直没做的且规定时间小于今天时间
                            if (!Dayudengyu(date_day, curToday)) {
                                item.put("_id", c.getInt(c.getColumnIndex("_id")));
                                item.put("task", c.getString(c.getColumnIndex("task")));

                                list.add(item);

                                Log.d("date_day", date_day);
                                Log.d("curToday", curToday);
                                Log.d("com", "coming");

                            }
                        }
                    }
                }
            }

        }
        c.close();
        return list;


    }


    //返回need_time日期要求当天按时完成的list   id、task
    public ArrayList<HashMap<String, Object>> getDone_onlist(String need_time) {
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        SQLiteDatabase db=mySqliteHelper.getReadableDatabase();
        ArrayList<HashMap<String, Object>>  list=new   ArrayList<HashMap<String, Object>>();

        Cursor c = db.query("my_table", new String[]{"_id", "task", "date", "task_type", "cycle_time", "remark", "state", "done_time"}, null, null, null, null, null);
        if(c!=null){
            while (c.moveToNext()) {

                if(!(c.getString(c.getColumnIndex("date")).equals(""))){
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    String date_day = c.getString(c.getColumnIndex("date")).substring(0, 10);
                    String date_complete = c.getString(c.getColumnIndex("done_time")).substring(0, 10);
                    if (need_time.equals(date_day)) {
                        if (c.getString(c.getColumnIndex("state")).equals("done")) {
                            if (Dayudengyu(date_day, date_complete)) {
                                item.put("_id", c.getInt(c.getColumnIndex("_id")));
                                item.put("task", c.getString(c.getColumnIndex("task")));

                                list.add(item);

                            }
                        }
                    }

                }
            }

        }
        c.close();
        return list;


    }


    //返回need_time日期要求当天未完成的List        id、task
    public ArrayList<HashMap<String, Object>> getTodolist(String need_time) {
        MySqliteHelper mySqliteHelper=new MySqliteHelper(context,"mydata.db",null,1);
        SQLiteDatabase db=mySqliteHelper.getReadableDatabase();
        ArrayList<HashMap<String, Object>>  list=new   ArrayList<HashMap<String, Object>>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        Date date=new Date(System.currentTimeMillis());
        String  curToday=simpleDateFormat.format(date).substring(0, 10);

        Cursor c = db.query("my_table", new String[]{"_id", "task", "date", "task_type", "cycle_time", "remark", "state", "done_time"}, null, null, null, null, null);
        if(c!=null){
            while (c.moveToNext()) {
                if(!(c.getString(c.getColumnIndex("date")).equals(""))){

                    HashMap<String, Object> item = new HashMap<String, Object>();
                    String date_day = c.getString(c.getColumnIndex("date")).substring(0, 10);
                    if (need_time.equals(date_day)) {
                        if (c.getString(c.getColumnIndex("state")).equals("todo")) {    //未能完成且日期要求完成日期大于等于今天
                            if (Dayudengyu(date_day, curToday)) {
                                item.put("_id", c.getInt(c.getColumnIndex("_id")));
                                item.put("task", c.getString(c.getColumnIndex("task")));

                                list.add(item);
                            }

                        }
                    }
                }

            }
        }
        c.close();
        return list;
    }

    public boolean Dayudengyu(String date_day, String date_complete) {  //》=


        //自定义完成时间
        String  year_day=date_day.substring(0,4);
        String  month_day= date_day.substring(5,7);
        String  day_day=date_day.substring(8,10);

        int year_day_int=Integer.parseInt(year_day);
        int month_day_int=Integer.parseInt(month_day);
        int day_day_int=Integer.parseInt(day_day);
        int  day_num=year_day_int*10000+month_day_int*100+day_day_int;



        //实际完成时间
        String  year_complete=date_complete.substring(0, 4);
        String  moth_complete= date_complete.substring(5,7);
        String  day_complete=date_complete.substring(8,10);

        int year_complete_int=Integer.parseInt(year_complete);
        int month_complete_int=Integer.parseInt(moth_complete);
        int day_complte_int=Integer.parseInt(day_complete);
        int complete_num=year_complete_int*10000+month_complete_int*100+day_complte_int;


        if(day_num>=complete_num)
            return true;
        else
            return  false;

    }

    //插入
    public static void Insert(MySqliteHelper mySqliteHelper, ContentValues values)
    {
        SQLiteDatabase db=mySqliteHelper.getWritableDatabase();
        db.insert("my_table", null, values);
        db.close();
    }












}

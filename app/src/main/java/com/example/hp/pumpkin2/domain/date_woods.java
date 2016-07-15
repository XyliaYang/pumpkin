package com.example.hp.pumpkin2.domain;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yx on 2016/2/25.
 * 某一天截止的任务在某一天的田上出现苗
 * 在截止日期之前做出来的任务都在设置的日期苗上长出南瓜
 * 未在截止日期包括当天之前做出来的任务都在设置的日期苗上长出坏南瓜
 *
 *
 */
public class date_woods {
    public  String date; //某一天日期
    public  int todo_num; //未做数目                [》=当天]
    public int  done_on; //按时完成数目              []
    public int done_not; //未能在规定日期之前完成
    public  ArrayList<HashMap<String,Object>>  todo_list; //未做完的组合    id,task,date
    public   ArrayList<HashMap<String,Object>>  done_on_list; //按时完成的组合
    public   ArrayList<HashMap<String,Object>>  done_not_list; //未按时完成的组合
    public   String[]  todo_taskArray; //待做的任务列表
    public  String[]     doneOn_taskArray; //按时完成的任务列表
    public  String[]      doneNot_taskArray; //未按时完成任务列表



}



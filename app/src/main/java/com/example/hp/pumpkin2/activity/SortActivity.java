package com.example.hp.pumpkin2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.db.MySqliteHelper;
import com.example.hp.pumpkin2.db.my_table;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hp on 2016/6/9.
 */
public class SortActivity  extends Activity {

    private View view;
    private ListView lv_type;  //任务分类
    private EditText et_type;  //编辑分类文本
    private  ImageButton ib_comfirm;  //确认按钮
    ImageButton ib_back; //返回按钮
    ArrayList<HashMap<String,Object>> listItem=new ArrayList<HashMap<String, Object>>();  //存储分类信息


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        initview();
        initdata();


    }

    private void initdata() {

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        //确认按钮返回给add_task文本框的值
        ib_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editstring = et_type.getText().toString();
                Intent intent = getIntent();
                Bundle data = new Bundle();
                data.putString("sort", editstring);
                intent.putExtras(data);
                SortActivity.this.setResult(0, intent);
                SortActivity.this.finish();
            }
        });


        //显示分类列表
        listItem=new my_table(SortActivity.this).getTypelist();
        SimpleAdapter listadapet=new SimpleAdapter(SortActivity.this,listItem,R.layout.list_adapter,new String[]{"image","task_type"},new int[]{R.id.imageView2,R.id.textView3});
        lv_type.setAdapter(listadapet);

        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap item=(HashMap)parent.getItemAtPosition(position);
                String task_type_choose=String.valueOf(item.get("task_type"));
                et_type.setText(task_type_choose);
            }
        });


    }

    private void initview() {
        et_type = (EditText) findViewById(R.id.et_type);  //编辑分类文本
        ib_comfirm = (ImageButton) findViewById(R.id.ib_comfirm);  //确认按钮
        lv_type= (ListView) findViewById(R.id.lv_type);
        ib_back=(ImageButton) findViewById(R.id.ib_back); //返回按钮
    }


}

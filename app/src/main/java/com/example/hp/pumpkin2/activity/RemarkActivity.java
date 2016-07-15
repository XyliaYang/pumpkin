package com.example.hp.pumpkin2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.hp.pumpkin2.R;

/**
 * Created by hp on 2016/6/9.
 */
public class RemarkActivity  extends Activity {

    EditText et_remark; //备注文本框
    ImageView iv_comfirm; //确认
    ImageButton ib_back; //返回按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);

        initview();
        initdata();
    }

    private void initdata() {
        //返回
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });


        //确认返回值
        iv_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editstring=et_remark.getText().toString();
                Intent intent=getIntent();
                Bundle data=new Bundle();
                data.putString("remark",editstring);
                intent.putExtras(data);
                RemarkActivity.this.setResult(3, intent);
                RemarkActivity.this.finish();
            }
        });


    }

    private void initview() {
        et_remark=(EditText)findViewById(R.id.et_remark);
        iv_comfirm=(ImageView)findViewById(R.id.iv_comfirm);
        ib_back=(ImageButton)findViewById(R.id.ib_back);

    }

}

package com.example.hp.pumpkin2;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.hp.pumpkin2.fragment.ContentFragment;
import com.example.hp.pumpkin2.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {


    private String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private String FRAGMENT_CONTENT = "fragment_content";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu); //加载左侧边栏

        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(200);

        initFragment();
    }

    public void initFragment() {
        // TODO Auto-generated method stub

         FragmentManager fManager=getSupportFragmentManager();

        FragmentTransaction transaction=fManager.beginTransaction();

        transaction.replace(R.id.fl_content,new ContentFragment(), FRAGMENT_CONTENT);  //是否为构造时要求的假的fragment容器??new对象的时候才是真正显示的布局
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(), FRAGMENT_LEFT_MENU);

        transaction.commit();

        fManager.findFragmentByTag(FRAGMENT_LEFT_MENU);

    }

}
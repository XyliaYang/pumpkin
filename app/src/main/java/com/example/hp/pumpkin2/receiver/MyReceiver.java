package com.example.hp.pumpkin2.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.domain.WindowUtils;

/**
 * Created by yx on 2016/2/23.
 */
public class MyReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("receiver","---------------");

            String date = intent.getStringExtra("date");
            String task = intent.getStringExtra("task");

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, new Intent("android.settings.SETTINGS"), 0);

//            WindowUtils.showPopupWindow(context,intent);

            Notification notify2 = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.pumpkin_icon_aa) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                            // icon)
                    .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
                            // bar上显示的提示文字
                    .setContentTitle(task)// 设置在下拉status
                            // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                    .setContentText(date)// TextView中显示的详细内容
                    .setContentIntent(pendingIntent1) // 关联PendingIntent
                    .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                    .getNotification(); // 需要注意build()是在API level
            // 16及之后增加的，在API11中可以使用getNotificatin()来代替
            notify2.flags |= Notification.FLAG_AUTO_CANCEL;
//        notify2.defaults |= Notification.DEFAULT_VIBRATE;//添加震动
     /*   long[] vibrates = {0, 2000, 1000, 2000,1000,2000};    //静止和震动间隔交替时间，第一个静止，第二个震动
       notify2.vibrate = vibrates;
*/
            long[] vibrates = {0, 5000};    //静止和震动间隔交替时间，第一个静止，第二个震动
            notify2.vibrate = vibrates;

            manager.notify(2, notify2);




        }
    }

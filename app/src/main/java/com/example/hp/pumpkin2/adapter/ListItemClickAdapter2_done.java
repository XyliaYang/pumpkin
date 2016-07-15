package com.example.hp.pumpkin2.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.pumpkin2.R;
import com.example.hp.pumpkin2.utils.ListItemClickHelp;

import java.util.ArrayList;

/**
 * Created by hp on 2016/2/20.
 */
public class ListItemClickAdapter2_done extends BaseAdapter {
    private Context contxet;
    private ArrayList<String> list;
    public ListItemClickHelp callback;
    private LayoutInflater mInflater;

    public ListItemClickAdapter2_done(Context contxet, ArrayList<String> list,
                                      ListItemClickHelp callback) {
        this.contxet = contxet;
        this.list = list;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        mInflater = (LayoutInflater) contxet
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_adapter3, null);
            holder = new ViewHolder();
            holder.mAd_tv_show = (TextView) convertView
                    .findViewById(R.id.done_textview);
            holder.mAd_tv_show.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mAd_btn_one = (ImageView) convertView
                    .findViewById(R.id.not_complete);
            holder.mAd_btn_two= (ImageView) convertView
                    .findViewById(R.id.done_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mAd_tv_show.setText(list.get(position));

        final View view = convertView;
        final int p = position;
        final int one = holder.mAd_btn_one.getId();
    holder.mAd_btn_one.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callback.onClick(view, parent, p, one);
        }
    });
        final int two = holder.mAd_btn_two.getId();
        holder.mAd_btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, two);
            }
        });

    return convertView;
}

    public static class ViewHolder {
        TextView mAd_tv_show;
        ImageView mAd_btn_one;
        ImageView mAd_btn_two;
    }

}

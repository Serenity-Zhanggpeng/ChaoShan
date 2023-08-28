package com.example.chaoshan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chaoshan.R;
import com.example.chaoshan.bean.Meal;

import java.util.List;

public  class MyAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Meal> mList;
    private Context context;
    public MyAdapter(Context context, List<Meal> mList){
        this.mList = mList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.item_grideview_layout,null);
        ImageView iv = v.findViewById(R.id.iv_gridView_item);
        TextView tv = v.findViewById(R.id.tv_gridView_item);
        Meal meal = mList.get(position);
        if (meal != null){
            Glide.with(context).load(meal.getImg()).into(iv);
            tv.setText(meal.getName());
        }

        return v;
    }
}

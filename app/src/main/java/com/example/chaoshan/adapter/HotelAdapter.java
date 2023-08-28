package com.example.chaoshan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chaoshan.R;
import com.example.chaoshan.bean.Hotel;

import java.util.List;

public class HotelAdapter extends ArrayAdapter<Hotel> {

    public HotelAdapter(@NonNull Context context,
                        int resource,
                        @NonNull List<Hotel> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Hotel hotel = getItem(position);//得到当前项的 Fruit 实例
        //为每一个子项加载设定的布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hotel_item, parent, false);
        //分别获取 image view 和 textview 的实例
        ImageView image = view.findViewById(R.id.image_view);
        TextView name = view.findViewById(R.id.name_tv);
        TextView price = view.findViewById(R.id.pric_tv);
        // 设置要显示的图片和文字
        if (hotel != null){
            image.setImageResource(hotel.getImg());
            name.setText(hotel.getName());
            price.setText(hotel.getPrice());
        }

        return view;

    }
}

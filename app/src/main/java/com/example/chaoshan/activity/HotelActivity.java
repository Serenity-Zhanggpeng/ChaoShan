package com.example.chaoshan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chaoshan.R;
import com.example.chaoshan.adapter.HotelAdapter;
import com.example.chaoshan.bean.Details;
import com.example.chaoshan.bean.Hotel;
import com.example.chaoshan.bean.Meal;
import com.example.chaoshan.utils.AssertUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class HotelActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private TextView title;
    private ListView listView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private int[] src = {R.drawable.v1c000001d5vin,
            R.drawable.a504fc2d562853530ed95cbf00096cfa5ef63c0,
            R.drawable.q357603307,
            R.drawable.a50653830,
            R.drawable.y60310917,
            R.drawable.yy93551601};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        imageViewBack = findViewById(R.id.back_img);
        title = findViewById(R.id.title_tv);
        listView = findViewById(R.id.list_view);

        title.setText("酒店");
        hotelList = new ArrayList<>();
        initData();
        hotelAdapter = new HotelAdapter(HotelActivity.this,
                R.layout.hotel_item,
                hotelList);
        listView.setAdapter(hotelAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Hotel m = hotelList.get(position);
            if (m != null) {
                Details details = new Details();
                details.setName(m.getName());
                details.setImag(m.getImg());
                details.setDes(m.getDes());
                details.setPric(m.getPrice());
                startActivity(new Intent(HotelActivity.this, DetailsActivity.class)
                        .putExtra("data", details)
                .putExtra("type",0));
            }
        });

        imageViewBack.setOnClickListener(v -> finish());
    }

    private void initData() {
        Gson gson = new Gson();
        hotelList.addAll(gson.fromJson(AssertUtils.initMaps("hotel.json", this),
                new TypeToken<List<Hotel>>() {}.getType()));
        int i = 0;
        for (Hotel hotel:hotelList){
            hotel.setImg(src[i]);
            i++;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

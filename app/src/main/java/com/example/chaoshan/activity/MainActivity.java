package com.example.chaoshan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chaoshan.R;
import com.example.chaoshan.utils.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewBack;
    private TextView title, recommend_tv, meal_tv, hotel_tv, mine_tv, userTv, weather;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //通过id获取控件对象   下面都是获取的textView
        weather = findViewById(R.id.weather);
        imageViewBack = findViewById(R.id.back_img);
        title = findViewById(R.id.title_tv);
        recommend_tv = findViewById(R.id.recommend_tv);
        meal_tv = findViewById(R.id.meal_tv);
        hotel_tv = findViewById(R.id.hotel_tv);
        mine_tv = findViewById(R.id.mine_tv);
        userTv = findViewById(R.id.user_list_tv);

        //app授权功能   Read_external_storage    permission_granted     Write_external_storage
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);   //1是请求码，只要唯一就行
        } else {
            //如果有权限就直接调用c
        }

        imageViewBack.setVisibility(View.GONE);
        title.setText("我的首页");

        // textView控件绑定监听事件 , 不仅仅是按钮可以绑定监听事件 textView也可以绑定监听事件
        weather.setOnClickListener(this);
        recommend_tv.setOnClickListener(this);
        meal_tv.setOnClickListener(this);
        hotel_tv.setOnClickListener(this);
        mine_tv.setOnClickListener(this);
        userTv.setOnClickListener(this);

        //从database 查询 若此时查询的用户是admin 则在用户管理里面可以看见注册用户  SharedPreferenceUtil自定义的类
        if ("admin".equals(SharedPreferenceUtil.getUserName())) {
            userTv.setVisibility(View.VISIBLE);  //用户可见
        }
    }

    //直接通过控件按钮绑定监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather:
                startActivity(new Intent(MainActivity.this, WetherActivity.class));
                break;
            case R.id.recommend_tv:
                startActivity(new Intent(MainActivity.this, RecommendActivity.class));
                break;
            case R.id.meal_tv:
                startActivity(new Intent(MainActivity.this, MealActivity.class));
                break;
            case R.id.hotel_tv:
                startActivity(new Intent(MainActivity.this, HotelActivity.class));
                break;
            case R.id.mine_tv:
                startActivity(new Intent(MainActivity.this, MineActivity.class));
                break;
            case R.id.user_list_tv:
                startActivity(new Intent(MainActivity.this, UserListActivity.class));
                break;
        }
    }
}

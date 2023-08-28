package com.example.chaoshan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaoshan.R;
import com.example.chaoshan.adapter.HotelAdapter;
import com.example.chaoshan.bean.Details;
import com.example.chaoshan.bean.Hotel;
import com.example.chaoshan.utils.AssertUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张鹏
 * @date 2023/2/21
 * @Description
 */
public class WetherActivity extends AppCompatActivity {
    private Button backBtn, queryWeatherBtn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        textView = findViewById(R.id.textView);
        backBtn = findViewById(R.id.button);
        queryWeatherBtn = findViewById(R.id.button2);

        //返回按钮绑定监听事件
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WetherActivity.this.finish();
            }
        });

        //查询按钮绑定监听事件
        queryWeatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestWithHttpURLConnection2(); //发送http求情
            }
        });
    }

    //发送hhtps请求,获取武汉市天气的相关数据(json形式)
    private void sendRequestWithHttpURLConnection2() {

        //开启新的线程 why?
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://t.weather.itboy.net/api/weather/city/101200101");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //请求方式
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if(connection.getResponseCode()==200) {//连接成功并返回状态码 200       执行成功返回200状态码
                        InputStream in = connection.getInputStream();//获取输入流对象读取数据
                        reader = new BufferedReader(new InputStreamReader(in));//转为bufferRead更为高效读取
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {  //读一行
                            response.append(line);   //读取数据，并将其存入 StringBuilder 对象中(response)
                        }
                        pareJSON2(response.toString());   //对获取到的 JSON 数据进行按照自己的需求处理
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //对获取到的 JSON 数据进行按照自己的需求处理的方法
    private void pareJSON2(String jsonData){
        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            String date=jsonObject.getString("date");
            StringBuilder weather=new StringBuilder();
            weather.append("日期:     "+date+"\n");

            String cityInfo=jsonObject.getString("cityInfo");
            JSONObject cityinfoObject=new JSONObject(cityInfo);
            String city=cityinfoObject.getString("city");
            weather.append("城市:     "+city+"\n");

            String data=jsonObject.getString("data");
            JSONObject dataObj=new JSONObject(data);
            weather.append("温度:     "+dataObj.getString("wendu")+"\n");
            weather.append("湿度:     "+dataObj.getString("shidu")+"\n");

            String forecast=dataObj.getString("forecast");
            JSONArray forecast_arr=new JSONArray(forecast);

            for (int i = 0; i < forecast_arr.length(); i++) {
                JSONObject jo=forecast_arr.getJSONObject(i);
                String next_date=jo.getString("ymd");
                String high=jo.getString("high");
                String week=jo.getString("week");
                Log.d("MainActivity", "预报日期: "+next_date);
                Log.d("MainActivity", "高温: "+high);
                Log.d("MainActivity", "星期: "+week);
                weather.append(next_date+" | "+high+" | "+week+"\n");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("");
                    textView.append(weather);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}





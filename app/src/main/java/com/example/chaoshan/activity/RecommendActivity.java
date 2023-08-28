package com.example.chaoshan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chaoshan.R;
import com.example.chaoshan.adapter.HotelAdapter;
import com.example.chaoshan.adapter.RecommendAdapter;
import com.example.chaoshan.bean.Details;
import com.example.chaoshan.bean.Hotel;
import com.example.chaoshan.utils.AssertUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private TextView title;
    private ListView listView;
    private RecommendAdapter recommendAdapter;
    private List<Hotel> hotelList;
    private int[] src = {R.drawable.i202011101031561376,
            R.drawable.t202011101032553632,
            R.drawable.w202011101033109462,
            R.drawable.t2y102011101033348261,
            R.drawable.i202011101037033203};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        imageViewBack = findViewById(R.id.back_img);
        title = findViewById(R.id.title_tv);
        listView = findViewById(R.id.list_view);

        title.setText("旅游景点");
        hotelList = new ArrayList<>();
        Gson gson = new Gson();
        //使用Gson库从JSON文件中读取景点数据，并将它们添加到hotelList中，
        hotelList.addAll(gson.fromJson(AssertUtils.initMaps("jing.json", this),
                new TypeToken<List<Hotel>>() {}.getType()));
        //此时hotelList数据源对象已经有数据了
        int i = 0;

        //通过循环遍历为每个hotel对象设置一个对应的图片src数组中的值。
        for (Hotel hotel:hotelList){
            hotel.setImg(src[i]);
            i++;
        }
       //创建了RecommendAdapter适配器，用于将hotelList数据源中的数据绑定到列表项视图中。
        recommendAdapter = new RecommendAdapter(this,R.layout.recommend_item,hotelList);
        listView.setAdapter(recommendAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Hotel m = hotelList.get(position);
            if (m != null) {
                Details details = new Details();
                details.setName(m.getName());
                details.setImag(m.getImg());
                details.setDes(m.getDes());
                startActivity(new Intent(RecommendActivity.this, DetailsActivity.class)
                        .putExtra("data", details)
                        .putExtra("type",2));
            }
        });

        //设置了返回键的监听事件，当用户点击返回键时，该Activity将被销毁。
        imageViewBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

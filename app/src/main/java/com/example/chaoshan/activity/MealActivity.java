package com.example.chaoshan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chaoshan.R;
import com.example.chaoshan.adapter.MyAdapter;
import com.example.chaoshan.bean.Details;
import com.example.chaoshan.bean.Meal;
import com.example.chaoshan.utils.AssertUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MealActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private TextView title;
    private GridView gridView;
    private List<Meal> mList;
    private List<Meal> stringList;
    private int[] src = {R.drawable.c1ed21b0ef41bd5d3f3ca02313561c23bdb3d98,
            R.drawable.a504fc2d562853530ed95cbf00096cfa5ef63c0,
            R.drawable.bd315c6034a85edf465c59bc29bbe92adf547552,
            R.drawable.d338744ebf81a4c115d115eb3c58050242da637,
            R.drawable.d9f2d3572c11dfbed25bbf03c882d9f603c220,
            R.drawable.cf1b9d16fdfaaf51da4690e2efbb74e7f11f7a3a,
            R.drawable.d8f9d72a6059252d1a166bf65474e3325ab5b93c,
            R.drawable.c16fdfaaf51f3de16942251f50110163b2979de,
            R.drawable.e824b899a9014c089938fb836b94e2017af4f48b,
            R.drawable.adcbef76094b36a9601e38c3239cd08f109d69,
            R.drawable.d31b0ef41bd5ad6eab9e4e70e024d9d2b7fd3c39};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        imageViewBack = findViewById(R.id.back_img);
        title = findViewById(R.id.title_tv);
        gridView = findViewById(R.id.grid_view);

        mList = new ArrayList<>();
        Gson gson = new Gson();
        stringList = gson.fromJson(AssertUtils.initMaps("meal.json", this),
                new TypeToken<List<Meal>>() {
                }.getType());
        if (stringList == null) {
            stringList = new ArrayList<>();
        }
        int i = 0;
        for (Meal meal : stringList) {
            meal.setImg(src[i]);
            i++;
        }

        gridView.setAdapter(new MyAdapter(this, stringList));
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Meal m = stringList.get(position);
            if (m != null) {
                Details details = new Details();
                details.setName(m.getName());
                details.setImag(m.getImg());
                details.setDes(m.getDes());
                startActivity(new Intent(MealActivity.this, DetailsActivity.class)
                        .putExtra("data", details)
                        .putExtra("type",1));
            }
        });

        title.setText("美食特产");
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

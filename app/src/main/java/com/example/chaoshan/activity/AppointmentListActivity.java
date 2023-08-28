package com.example.chaoshan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chaoshan.R;
import com.example.chaoshan.bean.Appointment;
import com.example.chaoshan.bean.LoginUser;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author 张鹏
 * @date 2023/2/21
 * @Description
 */
public class AppointmentListActivity extends AppCompatActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.recyclerview)
    ListView listView;
    private LoginUser loginUser;
    private List<Appointment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        ButterKnife.bind(this);
        titleTv.setText("我的预约");
        loginUser = LitePal.findFirst(LoginUser.class);
        mList = new ArrayList<>();
        if (loginUser != null) {
            List<Appointment> list = LitePal.where("userNid = ? ",
                    loginUser.getNid()).find(Appointment.class);
            if (list != null) {
                mList.addAll(list);
            }
        }
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(this,R.layout.appointment_item,mList);
        listView.setAdapter(appointmentAdapter);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(AppointmentListActivity.this)
                    .setTitle("提示")
                    .setMessage("确定要删除该预约信息吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        Appointment a = mList.get(position);
                        if (a == null)return;
                        int count = LitePal.deleteAll(Appointment.class,"nid = ?",a.getNid());
                        mList.remove(position);
                        appointmentAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {

                    }).show();
            return false;
        });
    }

    @OnClick({R.id.back_img, R.id.title_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.title_tv:
                break;
        }
    }

    class AppointmentAdapter extends ArrayAdapter<Appointment> {

        public AppointmentAdapter(@NonNull Context context,
                            int resource,
                            @NonNull List<Appointment> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Appointment  hotel = getItem(position);//得到当前项的 Fruit 实例
            //为每一个子项加载设定的布局
            View view = LayoutInflater.from(getContext()).inflate(R.layout.appointment_item, parent, false);
            //分别获取 image view 和 textview 的实例
            TextView name = view.findViewById(R.id.title_tv);
            TextView price = view.findViewById(R.id.time_tv);
            // 设置要显示的图片和文字
            if (hotel != null){
                if (0 == hotel.getType()){
                    name.setText("酒店："+hotel.getTypeName());
                }
                if (1 == hotel.getType()){
                    name.setText("美食："+hotel.getTypeName());
                }
                if (2 == hotel.getType()){
                    name.setText("景点："+hotel.getTypeName());
                }

                price.setText("时间："+hotel.getTime());
            }

            return view;

        }
    }
}

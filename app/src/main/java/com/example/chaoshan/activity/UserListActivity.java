package com.example.chaoshan.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chaoshan.R;
import com.example.chaoshan.bean.Hotel;
import com.example.chaoshan.bean.User;
import com.example.chaoshan.utils.SharedPreferenceUtil;
import com.example.chaoshan.wight.CircleImageView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private TextView title;
    private ListView listView;
    private UserListAdapter userListAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        title = findViewById(R.id.title_tv);
        imageViewBack = findViewById(R.id.back_img);
        listView = findViewById(R.id.list_view);
        if (userList == null)
            userList = new ArrayList<>();
        List<User> users = LitePal.findAll(User.class);
        userList.clear();
        for (User user:users){
            if (!user.getNid().equals(SharedPreferenceUtil.getUserId())
                    && !"admin".equals(user.getAccount())){
                userList.add(user);
            }
        }

        userListAdapter = new UserListAdapter(this, R.layout.user_item, userList);
        listView.setAdapter(userListAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            User user = userList.get(position);
            startActivityForResult(new Intent(UserListActivity.this, RegisterActivity.class)
                    .putExtra("data", user), 1001);
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            User user = userList.get(position);
            AlertDialog alertDialog = new AlertDialog.Builder(UserListActivity.this)
                    //标题
                    .setTitle("提示")
                    //内容
                    .setMessage("确认要删除此数据吗？")
                    .setPositiveButton("确认", (dialog, which) -> {
                        userList.remove(position);
                        userListAdapter.notifyDataSetChanged();
                        int a = LitePal.deleteAll(User.class, "nid = ?", user.getNid());
                        Log.e("TAG", "onClick: " + a);
                    })
                    .setNegativeButton("取消", (dialog, which) -> {

                    })
                    .create();
            alertDialog.show();


            return true;
        });

        title.setText("用户管理");


        imageViewBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && requestCode == RESULT_OK){
            List<User> users = LitePal.findAll(User.class);
            userList.clear();
            for (User user:users){
                if (!user.getNid().equals(SharedPreferenceUtil.getUserId())
                && !"admin".equals(user.getAccount())){
                    userList.add(user);
                }
            }
            userListAdapter.notifyDataSetChanged();
        }
    }

    public class UserListAdapter extends ArrayAdapter<User> {

        public UserListAdapter(@NonNull Context context,
                               int resource,
                               @NonNull List<User> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            User user = getItem(position);//得到当前项的 Fruit 实例
            //为每一个子项加载设定的布局
            View view = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
            //分别获取 image view 和 textview 的实例
            CircleImageView image = view.findViewById(R.id.circle_image);
            TextView name = view.findViewById(R.id.name_tv);
            TextView time = view.findViewById(R.id.time_tv);
            // 设置要显示的图片和文字
            if (user != null) {
                image.setImageResource(R.drawable.ic_default_head);
                name.setText(user.getAccount() + "     女");
                time.setText(user.getTime());
                /*
                image.setImageResource(hotel.getImg());
                name.setText(hotel.getName());
                price.setText(hotel.getPrice());
                */
            }

            return view;

        }
    }
}

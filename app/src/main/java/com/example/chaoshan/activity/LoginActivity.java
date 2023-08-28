package com.example.chaoshan.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chaoshan.R;
import com.example.chaoshan.bean.LoginUser;
import com.example.chaoshan.bean.User;
import com.example.chaoshan.utils.SharedPreferenceUtil;
import com.example.chaoshan.utils.UUID32;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText accountEdit, pwdEdit;
    private Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountEdit = findViewById(R.id.edit_account);
        pwdEdit = findViewById(R.id.edit_pwd);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);

        //Check the read/write permissions of external storage
        //start entering privilege  where approve  authorization function
        //aim  whether reading the information of phone‘s external storage

        //Check that you have granted permission to read and write to external storage
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //If you read and write permissions of external storage has not been awarded,
            // then call ActivityCompat. RequestPermissions request these permissions () method
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//1是请求码，只要唯一就行
        } else {
            //如果有权限就直接调用c
        }

        //first query the databases where hava amdin   LitePal practice the database 类
        //先查询数据库用户数据,目的防止用户注册repetition
        List<User> userList = LitePal.where("account = ?", "admin")
                .find(User.class);
        if (userList == null || userList.size() == 0) {
            User user = new User();
            user.setAccount("admin");
            user.setNid(UUID32.getUUID32());
            user.setPwd("admin");
            if (user.save()) {
                Log.e("TAG", "管理员账号注册成功");
            }
        } else {
            Log.e("TAG", "onCreate: 该账户已被注册");
        }

        // utilise the utils set 控件的value   控件输入框设置value
        // 登陆时直接从数据库查询     有数据设置到对应的控件
        accountEdit.setText(SharedPreferenceUtil.getUserName());
        pwdEdit.setText(SharedPreferenceUtil.getPwd());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this,
                        RegisterActivity.class), 1001);
            }
        });

        //登录按钮   lambda
        loginBtn.setOnClickListener(v -> {
            //获取控件的值经行逻辑处理
            String account = accountEdit.getText().toString();
            String pwd = pwdEdit.getText().toString();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
                Toast.makeText(LoginActivity.this, "请输入账号或者密码", Toast.LENGTH_SHORT).show();
            } else {
                //查数据库
                List<User> userList1 = LitePal.where("account = ?", account)
                        .find(User.class);
                if (userList1 == null || userList1.size() == 0) {
                    Toast.makeText(LoginActivity.this, "该账户不存在", Toast.LENGTH_SHORT).show();
                } else {
                    User user = userList1.get(0);
                    if (user != null) {
                        if (pwd.equals(user.getPwd())) {
                            //set value from database
                            LoginUser loginUser = new LoginUser();
                            loginUser.setAccount(user.getAccount());
                            loginUser.setGender(user.getGender());
                            loginUser.setNid(user.getNid());
                            loginUser.setPwd(user.getPwd());
                            loginUser.setTime(user.getTime());
                            loginUser.setEmail(user.getEmail());
                            loginUser.setYear(user.getYear());
                            loginUser.setMonth(user.getMonth());
                            loginUser.setDay(user.getDay());
                            loginUser.setImg(user.getImg());
                            loginUser.saveOrUpdate();

                            SharedPreferenceUtil.setUserName(account);
                            SharedPreferenceUtil.setPwd(pwd);
                            SharedPreferenceUtil.setUserId(user.getNid());
                            Log.e("TAG", "onClick: " + user.getNid() + "   " + SharedPreferenceUtil.getUserId());
                            SharedPreferenceUtil.setUserGender(user.getGender());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


    //获取Register activity传回intent里的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            String account = data.getStringExtra("account");
            if (!TextUtils.isEmpty(account)) {
                accountEdit.setText(account);
            }
        }
    }
}

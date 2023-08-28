package com.example.chaoshan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaoshan.R;
import com.example.chaoshan.bean.User;
import com.example.chaoshan.utils.DateUtils;
import com.example.chaoshan.utils.UUID32;

import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private EditText accountEdit, pwdEdit, checkEdit;
    private Button registerBtn;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2;
    private TextView title;
    int type = 0;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //获取intent传过来的值 在序列化为Java dto
        //https://blog.csdn.net/gebitan505/article/details/15811981
        //点击注册时user为null
        user = (User) getIntent().getSerializableExtra("data");

        imageViewBack = findViewById(R.id.back_img);
        title = findViewById(R.id.title_tv);
        accountEdit = findViewById(R.id.edit_account);
        pwdEdit = findViewById(R.id.edit_pwd);
        checkEdit = findViewById(R.id.check_edit_pwd);
        registerBtn = findViewById(R.id.register_btn);
        radioGroup = findViewById(R.id.radio_group);
        radioButton1 = findViewById(R.id.man_radio_btn);
        radioButton2 = findViewById(R.id.woman_radio_btn);

        title.setText("注册");
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //复选框代码 选则男女  type为0表示男，为1表示女，在将数据存储到数据库时type的值为0或1
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.man_radio_btn) {
                    type = 0;
                }
                if (checkedId == R.id.woman_radio_btn) {
                    type = 1;
                }
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取控件中的值
                String account = accountEdit.getText().toString();
                String pwd = pwdEdit.getText().toString();
                String checkPwd = checkEdit.getText().toString();
                String str = registerBtn.getText().toString();

                //逻辑判断控件的值
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this, "请输入账号或者密码", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(checkPwd)) {
                        Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!checkPwd.equals(pwd)) {
                            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        } else {
                            //如果控件的值为修改  这时已经登录进去了  点击的修改用户信息
                            if ("修改".equals(str)){
                                //封装到实体类以及ContentValues对象中去
                                user.setPwd(pwd);
                                user.setGender(type);
                                ContentValues c = new ContentValues();
                                c.put("pwd",pwd);
                                c.put("gender",type);
                                //更新数据库
                                int a = LitePal.updateAll(User.class,c,"nid = ?",user.getNid());
                                if (a > 0){
                                    Toast.makeText(RegisterActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }
                                setResult(RESULT_OK);
                                finish();
                            }else {
                                //否则就是登录时点击的是注册按钮注册
                                User mUser = new User();
                                mUser.setAccount(account);
                                mUser.setPwd(pwd);
                                mUser.setNid(UUID32.getUUID32());  //随机生成
                                mUser.setTime(DateUtils.getStringDateToSecond());
                                mUser.setGender(type);
                                //先查询数据库  select * from 表 where account=mUser.getAccount()  查询出来的数据封装到user entity
                                List<User> userList = LitePal.where("account = ?", mUser.getAccount())
                                        .find(User.class);
                                if (userList == null || userList.size() == 0) {
                                    //若果为说明account没有重复，是新用户，则就插入数据库,之后把account回传到登录界面
                                    if (mUser.save()) {
                                        Intent intent = new Intent();
                                        intent.putExtra("account", mUser.getAccount());
                                        intent.setClass(RegisterActivity.this,LoginActivity.class);
                                        setResult(RESULT_OK, intent);
                                        //提示
                                        Toast.makeText(RegisterActivity.this, "注册成功，请返回登录", Toast.LENGTH_SHORT).show();
                                        startActivity(intent); //跳回登录界面
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "该账户已被注册", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
        });

        if (user != null) {
            title.setText("修改");
            accountEdit.setEnabled(false);
            accountEdit.setText(user.getAccount());
            pwdEdit.setText(user.getPwd());
            checkEdit.setText(user.getPwd());
            if (1 == user.getGender()) {
                radioButton2.setChecked(true);
            } else {
                radioButton1.setChecked(true);
            }

            registerBtn.setText("修改");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

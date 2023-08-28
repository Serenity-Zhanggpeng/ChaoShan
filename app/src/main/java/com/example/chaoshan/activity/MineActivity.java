package com.example.chaoshan.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.chaoshan.R;
import com.example.chaoshan.bean.LoginUser;
import com.example.chaoshan.bean.User;
import com.example.chaoshan.wight.CircleImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineActivity extends AppCompatActivity {
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.account_tv)
    TextView accountTv;
    @BindView(R.id.nick_tv)
    EditText nickTv;
    @BindView(R.id.email_tv)
    EditText emailTv;
    @BindView(R.id.man_radio_btn)
    RadioButton manRadioBtn;
    @BindView(R.id.woman_radio_btn)
    RadioButton womanRadioBtn;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.gender_tv)
    TextView genderTv;
    @BindView(R.id.year_tv)
    EditText yearTv;
    @BindView(R.id.month_tv)
    EditText monthTv;
    @BindView(R.id.day_tv)
    EditText dayTv;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.image_view)
    CircleImageView imageView;
    @BindView(R.id.yy_btn)
    Button yyBtn;
    private ImageView imageViewBack;
    private LoginUser loginUser;
    private User user;
    private int gender;
    private final int requset_photo = 1001;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);

        loginUser = LitePal.findFirst(LoginUser.class);
        List<User> userList = LitePal.where("nid = ?", loginUser.getNid())
                .find(User.class);
        if (userList != null && userList.size() > 0) {
            user = userList.get(0);
            if (user != null) {
                accountTv.setText(user.getAccount());
                gender = user.getGender();
                if (1 == user.getGender())
                    womanRadioBtn.setChecked(true);
                else manRadioBtn.setChecked(true);
                nameTv.setText(user.getNickName());
                emailTv.setText(user.getEmail());
                nickTv.setText(user.getNickName());
                yearTv.setText(user.getYear());
                monthTv.setText(user.getMonth());
                dayTv.setText(user.getDay());
                path = user.getImg();
                if (!TextUtils.isEmpty(path)) {
                    Glide.with(MineActivity.this).load(path).into(imageView);
                }
            }
        }
        titleTv.setText("个人中心");

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.woman_radio_btn) {
                gender = 1;
            }
            if (checkedId == R.id.man_radio_btn) {
                gender = 0;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.back_img, R.id.register_btn, R.id.image_view,R.id.yy_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.yy_btn:
                startActivity(new Intent(this,AppointmentListActivity.class));
                break;
            case R.id.image_view:
                if (ContextCompat.checkSelfPermission(MineActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MineActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MineActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//1是请求码，只要唯一就行
                } else {
                    //如果有权限就直接调用c
                    PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            //.loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                            .maxSelectNum(1)
                            .enableCrop(false)           //是否裁剪
                            .withAspectRatio(1, 1)
                            .compress(true)
                            .forResult(requset_photo);
                }

                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.register_btn:
                String nick = nickTv.getText().toString();
                String email = emailTv.getText().toString();
                String year = yearTv.getText().toString();
                String month = monthTv.getText().toString();
                String day = dayTv.getText().toString();

                if (user != null) {
                    user.setNickName(nick);
                    user.setGender(gender);
                    user.setEmail(email);
                    user.setYear(year);
                    user.setMonth(month);
                    user.setImg(path);
                    user.setDay(day);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("gender", gender);
                    contentValues.put("nickName", nick);
                    contentValues.put("email", email);
                    contentValues.put("year", year);
                    contentValues.put("month", month);
                    contentValues.put("day", day);
                    contentValues.put("img", path);
                    int a = LitePal.updateAll(User.class, contentValues, "nid = ?", user.getNid());
                    if (a > 0) {
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case requset_photo:

                List<LocalMedia> uris = PictureSelector.obtainMultipleResult(data);
                List<String> pathList = new ArrayList<>();
                if (uris.size() > 0) {
                    for (int i = 0; i < uris.size(); i++) {
                        if (uris.get(i).isCompressed()) {
                            pathList.add(uris.get(i).getCompressPath());

                        } else {

                            pathList.add(uris.get(i).getPath());
                        }
                    }
                    if (pathList.size() > 0) {
                        path = pathList.get(0);
                        if (!TextUtils.isEmpty(path)) {
                            Glide.with(MineActivity.this).load(path).into(imageView);
                        }
                    }

                    Log.i("TAG", "onActivityResult: " + pathList.toString());

                }
                break;

        }
    }
}

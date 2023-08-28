package com.example.chaoshan.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.chaoshan.R;
import com.example.chaoshan.bean.Appointment;
import com.example.chaoshan.bean.Details;
import com.example.chaoshan.bean.LoginUser;
import com.example.chaoshan.utils.DateUtils;
import com.example.chaoshan.utils.UUID32;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.yy_btn)
    Button yyBtn;
    @BindView(R.id.back_img)
    ImageView imageViewBack;

    private ImageView imageView;
    private TextView title, nameTv, contentTv;
    private Details details;
    private int type;
    private Appointment appointment;
    private LoginUser loginUser;
    private Calendar selectedDate;
    private Calendar startDate;
    private Calendar endDate;
    private TimePickerView pvTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 0);
        loginUser = LitePal.findFirst(LoginUser.class);


        title = findViewById(R.id.title_tv);
        imageView = findViewById(R.id.image_view);
        nameTv = findViewById(R.id.name_tv);
        contentTv = findViewById(R.id.des_tv);


        details = (Details) getIntent().getSerializableExtra("data");

        if (details != null) {
            imageView.setImageResource(details.getImag());
            nameTv.setText(details.getName());
            contentTv.setText(details.getDes());
            if (!TextUtils.isEmpty(details.getPric())) {
                contentTv.setText(details.getDes() + "\n\n" + details.getPric());
            }
            if (loginUser != null) {
                List<Appointment> list = LitePal.where("userNid = ? and typeName = ?", loginUser.getNid(), details.getName()).find(Appointment.class);
                if (list != null) {
                    if (list.size() == 0) {
                        yyBtn.setText("预约");
                        return;
                    }
                    for (Appointment a : list) {
                        if (!TextUtils.isEmpty(a.getTime())) {
                            if (timeCompare(a.getTime(), DateUtils.getStringDateToSecond()) == 3) {
                                yyBtn.setText("预约");
                                yyBtn.setEnabled(true);
                            }
                            if (timeCompare(a.getTime(), DateUtils.getStringDateToSecond()) == 1) {
                                yyBtn.setText("已预约");
                                yyBtn.setEnabled(false);
                            }
                        }
                    }

                }
            }
        }


        title.setText("详情");


        //系统当前时间
        selectedDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
        //startDate.set(1965, 4, 24);//第一个选择
        endDate = Calendar.getInstance();
        endDate.set(2069, 7, 28);//最后一个选择

    }


    private void initTimePicker() {//Dialog 模式下，在底部弹出
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        pvTime = new TimePickerBuilder(this, (date, v) -> {
            //Toast.makeText(mContext, getTime(date), Toast.LENGTH_SHORT).show();
            Log.i("pvTime", "onTimeSelect"+timeCompare(DateUtils.getStringDateToSecond(),getTime(date)));
            //planTimeTv.setText(getTime(date));
            if (timeCompare(DateUtils.getStringDateToSecond(),getTime(date)) != 3){
                Toast.makeText(this, "预约失败,日期小于当前日期，请重新选择！", Toast.LENGTH_SHORT).show();
            }else {
                appointment = new Appointment();
                appointment.setNid(UUID32.getUUID32());
                appointment.setType(type);
                appointment.setTime(getTime(date));
                appointment.setTypeName(details.getName());
                appointment.setUserName(loginUser.getNickName());
                appointment.setUserNid(loginUser.getNid());
                appointment.save();
                if (appointment.isSaved()){
                    Toast.makeText(this, "预约成功,预约日期："+getTime(date), Toast.LENGTH_SHORT).show();
                    yyBtn.setText("已预约");
                    yyBtn.setEnabled(false);
                }
            }
        })
                .setTimeSelectChangeListener(date -> Log.i("pvTime", "onTimeSelectChanged"))
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(view -> Log.i("pvTime", "onCancelClickListener"))
                .setItemVisibleCount(3) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .setDate(calendar1)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private String getTime(Date date) {
        //根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return format.format(date);
    }


    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int timeCompare(String startTime, String endTime) {
        int i = 0;
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //结束时间小于开始时间
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                //开始时间与结束时间相同
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //结束时间大于开始时间
                i = 3;
            }
        } catch (Exception e) {

        }
        return i;
    }

    @OnClick({R.id.back_img, R.id.yy_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.yy_btn:
                initTimePicker();
                if (pvTime != null)
                    pvTime.show(view);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
                break;
        }
    }
}

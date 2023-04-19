package com.example0.checkmachine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example0.BaseActivity;
import com.example0.checkmachine.R;
import com.example0.retrofit.ApiService;
import com.example0.retrofit.Location;
import com.example0.retrofit.PhotoData;
import com.example0.retrofit.ResponseData;
import com.example0.retrofit.Student;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import android_serialport_api.SerialPortUtil;
import callback.SerialCallBack;
import callback.SerialPortCallBackUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityMorning extends BaseActivity implements View.OnClickListener , RadioGroup.OnCheckedChangeListener,SoundPool.OnLoadCompleteListener,SerialCallBack{

    private static final String TAG = "ActivityMorning";
    private int stu_id=-1;
    private Bitmap bitmap=null;
    private Uri imageUri1,imageUri2=null;
    private double temperature_value=36.5;
    private ImageView iv_photo_top,iv_left_hand,iv_right_hand,iv_eye,iv_mouth;
    private TextView  tv_school,tv_classname,tv_stu_name;
    private TextView tv_temperature;
    private TextView tv_show;
    private RadioGroup rg_eye,rg_mouth,rg_hand;
    private RadioButton rb_eye_status_abnormal,rb_mouth_status_abnormal,rb_hand_status_abnormal;
    private String eye_status="正常";
    private String mouth_status="正常";
    private String hand_status="正常";
    private int hand=0;
    private int eye=0;
    private int mouth=0;
    private TextView tv_recollect,tv_submit;
    private boolean isOpen=false;
    private  boolean isClose=true;
    private Context mContext;
    private SoundPool mSoundPool = null;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();

    //创建4个坐标对象
    Location location1,location2,location3,location4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning_index2);
        mContext=this;
        initView();

        if(stu_id<0){
            Intent intent=getIntent();
           // Bundle bd=intent.getExtras();
            String strID =intent.getStringExtra("id");
            stu_id=Integer.parseInt(strID);
            String uri1=intent.getStringExtra("uri1");
            imageUri1=Uri.parse(uri1);
            Log.e(TAG, "image1: "+uri1);
            String uri2=intent.getStringExtra("uri2");
            imageUri2=Uri.parse(uri2);
            Log.e(TAG, "image1: "+uri2);
            hand=intent.getIntExtra("hand",0);
            eye=intent.getIntExtra("eye",0);
            mouth=intent.getIntExtra("mouth",0);
            location1 = (Location) getIntent().getSerializableExtra("location1");
            location2 = (Location) getIntent().getSerializableExtra("location2");
            location3 = (Location) getIntent().getSerializableExtra("location3");
            location4 = (Location) getIntent().getSerializableExtra("location4");
        }


        initStu_info();
        SerialPortCallBackUtils.setCallBack(this);
        //保证串口关闭
        isClose = SerialPortUtil.close();
//        折叠双屏串口为ttyS0；
        isOpen = SerialPortUtil.open("/dev/ttyS0", 115200, 0);//里面的参数根据自己的需求自己更改
        if (isOpen) {
            Log.e("TAG", "打开成功");
        } else {
            Log.e("TAG", "打开失败");
        }
        //向串口发送数据
        SerialPortUtil.sendString("A55501FB");
        SerialPortUtil.receive();

        initHealth();

        try {
            initSP();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initHealth() {
        if(hand==0){
            rg_hand.check(R.id.rb_hand_status_normal);
        }else {
            rg_hand.check(R.id.rb_hand_status_abnormal);
            rb_hand_status_abnormal.setTextColor(Color.RED);
        }

        if(eye==0){
            rg_eye.check(R.id.rb_eye_status_normal);
        }else {
            rg_eye.check(R.id.rb_eye_status_abnormal);
            rb_eye_status_abnormal.setTextColor(Color.RED);
        }

        if(mouth==0){
            rg_mouth.check(R.id.rb_mouth_status_normal);
        }else {
            rg_mouth.check(R.id.rb_mouth_status_abnormal);
            rb_mouth_status_abnormal.setTextColor(Color.RED);
        }

    }

    private void initSP() throws Exception{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder().setMaxStreams(2).build();
        } else {
            mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
        soundID.put(1, mSoundPool.load(this, R.raw.normal, 1));
        soundID.put(2, mSoundPool.load(this, R.raw.abnormal, 1));

        mSoundPool.setOnLoadCompleteListener(this);
    }

    //更新学生信息
    private void initStu_info() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kg.ykwell.cn/api/device/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseData<Student>> call = apiService.myStudent(stu_id);
        call.enqueue(new Callback<ResponseData<Student>>() {

            @Override
            public void onResponse(Call<ResponseData<Student>> call, Response<ResponseData<Student>> response) {
                Log.e(TAG, "onResponse: " + response);
                Log.e(TAG, "stuid "+stu_id );
               // Log.e(TAG, "onResponse: " + response.body().getData().getName());

                if(response.body()!=null) {
                    Student student=response.body().getData();
                    tv_school.setText(student.getKindergarten().getName());
                    tv_classname.setText(student.getClassX().getName());
                    tv_stu_name.setText(student.getName());

                    //加载照片信息
                    try{
                        InputStream inputStream=getContentResolver().openInputStream(imageUri1);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        InputStream inputStream2=getContentResolver().openInputStream(imageUri2);
                        Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream2);
                        iv_photo_top.setImageBitmap(bitmap);

                        //分割图片
                        iv_right_hand.setImageBitmap(Bitmap.createBitmap(bitmap,location1.getReX(),location1.getReY(),
                                location1.getWidth(),location1.getHeight(),null,false));

                        iv_left_hand.setImageBitmap(Bitmap.createBitmap(bitmap,location2.getReX(),location2.getReY(),
                                location2.getWidth(),location2.getHeight(),null,false));
                        iv_eye.setImageBitmap(Bitmap.createBitmap(bitmap,location3.getReX(),location3.getReY(),
                                location3.getWidth(),location3.getHeight(),null,false));
                        iv_mouth.setImageBitmap(Bitmap.createBitmap(bitmap2,location4.getReX(),location4.getReY(),
                                location4.getWidth(),location4.getHeight(),null,false));





                        //将转换过后的灰度图显示出来
                      // iv_photo_bottom.setImageBitmap(convertGreyImg(bitmap));
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseData<Student>> call, Throwable t) {
                Log.e(TAG, "onResponse: " + t);
            }
        });
    }



    private void initView() {
        iv_photo_top =findViewById(R.id.iv_photo_top);
        iv_left_hand=findViewById(R.id.iv_left_hand);
        iv_right_hand=findViewById(R.id.iv_right_hand);
        iv_eye=findViewById(R.id.iv_eye);
        iv_mouth=findViewById(R.id.iv_mouth);

        tv_school=findViewById(R.id.tv_school);
        tv_classname=findViewById(R.id.tv_classname);
        tv_stu_name=findViewById(R.id.tv_stu_name);

        tv_temperature=findViewById(R.id.tv_temperature);
        tv_show=findViewById(R.id.tv_show);
        rg_eye=findViewById(R.id.rg_eye);
        rg_eye.setOnCheckedChangeListener(this);
        rg_mouth=findViewById(R.id.rg_mouth);
        rg_mouth.setOnCheckedChangeListener(this);
        rg_hand=findViewById(R.id.rg_hand);
        rg_hand.setOnCheckedChangeListener(this);
        //rg_eye.check(R.id.rb_eye_status_abnormal);
        rb_eye_status_abnormal=findViewById(R.id.rb_eye_status_abnormal);
        rb_mouth_status_abnormal=findViewById(R.id.rb_mouth_status_abnormal);
        rb_hand_status_abnormal=findViewById(R.id.rb_hand_status_abnormal);

        tv_recollect=findViewById(R.id.tv_recollect);
        tv_recollect.setOnClickListener(this);
        tv_submit=findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_recollect:
                startActivity(new Intent(ActivityMorning.this,TestCamera1.class));
                finish();
                break;
            case R.id.tv_submit:
                Log.e(TAG, "onClick:submit。。。stuid "+stu_id );
                //提交上传晨检信息
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://kg.ykwell.cn/api/device/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<ResponseData> call = apiService.uploadMorning(stu_id,temperature_value,
                        hand_status,mouth_status,eye_status);
                call.enqueue(new Callback<ResponseData>() {

                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                       // Log.e(TAG, "onResponse: " + response);
                        Log.e(TAG, "stuid "+stu_id );
                        // Log.e(TAG, "onResponse: " + response.body().getData().getName());

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Log.e(TAG, "onResponse: " + t);
                        Toast.makeText(ActivityMorning.this,"遇到错误，请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });

                //--收集上传测试数据--
                String bit_base64 = FaceCheck.bitmapToBase64(bitmap);
//                uploadphoto(bit_base64);


                startActivity(new Intent(ActivityMorning.this,TestCamera1.class));
                finish();
                break;
        }

    }

//    private void uploadphoto(String bit_base64) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://47.101.136.0:8081/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiService apiService = retrofit.create(ApiService.class);
//        Call<Object> call = apiService.uploadImg(new PhotoData(bit_base64));
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                Log.e("TAG333", "onResponse-body: " + response.body());
//
//            }
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                Log.e("TAG300", "onResponse: " + t);
//            }
//        });
//
//
//    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton checked= (RadioButton) radioGroup.findViewById(i);
         String status =checked.getText().toString();

        switch (i){
            case R.id.rb_eye_status_normal:
                eye_status=status;
                Log.e(TAG, "眼睛"+eye_status);
                break;

            case R.id.rb_eye_status_abnormal:
                eye_status=status;
                Log.e(TAG, "眼睛"+eye_status);
                break;

            case R.id.rb_mouth_status_normal:
                mouth_status=status;
                Log.e(TAG, "口部"+mouth_status);
                break;

            case R.id.rb_mouth_status_abnormal:
                mouth_status=status;
                Log.e(TAG, "口部"+mouth_status);
                break;

            case R.id.rb_hand_status_normal:
                hand_status=status;
                Log.e(TAG, "手部"+hand_status);
                break;

            case R.id.rb_hand_status_abnormal:
                hand_status=status;
                Log.e(TAG, "手部"+hand_status);
                break;

        }
    }

    @Override
    public void onSerialPortData(String serialPortData) {
        String str=serialPortData.trim();
        String highStr= str.substring(4,6);

        String lowStr= str.substring(6,8);
        int high=Integer.parseInt(highStr, 16);
        int low=Integer.parseInt(lowStr, 16);
        double result=(high + low * 256)/100.0;
        temperature_value = new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        Log.e("TAG", "temperature数据:" + highStr+"...."+lowStr+"------"+high+".."+low+"====="+temperature_value);
        //温度更新到ui
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(temperature_value>37.0){
                    tv_temperature.setTextColor(getResources().getColor(R.color.red));
                }
                if(temperature_value<=35.0){
                    tv_temperature.setTextColor(getResources().getColor(R.color.blue));
                }
                tv_temperature.setText(temperature_value+"℃");

            }
        });

        Log.e("TAG", "来自串口的数据:" + serialPortData);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放soundpool
        mSoundPool.release();
        //串口关闭
        if(isOpen) {
            isClose = SerialPortUtil.close();
            if (isClose) {
                Log.e(TAG, "关闭成功");
            }
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
//        if(temperature_value<=37.0){
//            mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
//            Log.e(TAG, "正常");
//        }else {
//            mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
//            Log.e(TAG, "有异常" );
//        }
        if(temperature_value>37.0|temperature_value<=35.0|hand==1|mouth==1|eye==1){
            mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
            Log.e(TAG, "异常");
        }else {
            mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
            Log.e(TAG, "正常" );
        }

    }
}

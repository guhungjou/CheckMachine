package com.example0.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiService {
    //园长登录
    @PUT("teacher/login")
    @FormUrlEncoded
    Call<ResponseData<Teacher>> login(@Field("username") String username, @Field("password") String password );

    //通过id获取学生信息
    @GET("student/{id}?type=id")
    Call<ResponseData<Student>> myStudent(@Path("id") int id);

    /** 晨检 **/
    //上传晨检信息
    @POST("student/morning/check")
    @FormUrlEncoded
    Call<ResponseData> uploadMorning(@Field("student_id") int student_id, @Field("temperature") double temperature ,
                                     @Field("hand") String hand , @Field("mouth") String mouth ,@Field("eye") String eye);
   //手口眼检测
    @POST("diagnose")
    Call<CameraData> cameraup(@Body PhotoData photoData);

    //上传照片保存
    @POST("uploadImg")
    Call<Object> uploadImg(@Body PhotoData photoData);

}


package com.apoorv.camera.data.network;

import com.apoorv.camera.app.Constants;
import com.apoorv.camera.data.response.ImageDateByResponse;
import com.apoorv.camera.data.response.ImageUploadResponse;
import com.apoorv.camera.data.response.LoginResponse;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Apoorv Vardhman on 6/26/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public interface AppApi {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(@FieldMap HashMap<String,String> param);

    @GET("images-date-by")
    Call<ImageDateByResponse> imageDateWise(@Query("date") String date);

    @Multipart
    @POST("upload")
    Call<ImageUploadResponse> upload(@Part MultipartBody.Part[] images, @PartMap HashMap<String, RequestBody> param);


}

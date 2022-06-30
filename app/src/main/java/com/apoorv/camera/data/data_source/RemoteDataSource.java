package com.apoorv.camera.data.data_source;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.apoorv.camera.app.App;
import com.apoorv.camera.app.Constants;
import com.apoorv.camera.data.local.PrefConnect;
import com.apoorv.camera.data.network.AppApi;
import com.apoorv.camera.data.response.ImageDateByResponse;
import com.apoorv.camera.data.response.ImageUploadResponse;
import com.apoorv.camera.data.response.LoginResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Apoorv Vardhman on 6/26/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class RemoteDataSource {

    private static RemoteDataSource remoteDataSource;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    AppApi apiInterface;

    private RemoteDataSource(){

        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(50000, TimeUnit.MILLISECONDS)
                .connectTimeout(50000, TimeUnit.MILLISECONDS)
                .addInterceptor(new RequestHeadersNetworkInterceptor(new HashMap<String,String>(){
                    {
                        put("Accept","application/json");
                        put("Authorization", "Bearer "+PrefConnect.readString(App.getContext(),Constants.TOKEN,""));
                    }
                }))
                .addNetworkInterceptor(loggingInterceptor)
                .build();



        /*okHttpClient.networkInterceptors().add(new RequestHeadersNetworkInterceptor(new HashMap<String, String>()
        {
            {
                put("Accept", "application/json");
                //put("Authorization", "Bearer "+PrefConnect.readString(App.getContext(),Constants.TOKEN,""));
            }
        }));*/

        retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiInterface = retrofit.create(AppApi.class);
    }

    public static RemoteDataSource getRemoteDataSource(){
        if(remoteDataSource==null){
            remoteDataSource=new RemoteDataSource();
        }
        return  remoteDataSource;
    }

    public Call<LoginResponse> login(HashMap<String,String> param)
    {
        return  apiInterface.login(param);
    }

    public Call<ImageDateByResponse> imageDateWise(String date)
    {
        return apiInterface.imageDateWise(date);
    }

    public Call<ImageUploadResponse> upload(MultipartBody.Part[] images, HashMap<String, RequestBody> param)
    {
        Log.e("images",images.toString());
        Log.e("param",param.toString());

        return apiInterface.upload(images,param);
    }
}

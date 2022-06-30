package com.apoorv.camera.presentation.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apoorv.camera.data.data_source.RemoteDataSource;
import com.apoorv.camera.data.response.ApiResource;
import com.apoorv.camera.data.response.ImageUploadResponse;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Apoorv Vardhman on 6/30/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class MainViewModel extends ViewModel {
    private MutableLiveData<ApiResource<ImageUploadResponse>> imageUploadResponse = new MutableLiveData<>();

    public void setImageUploadResponse(MultipartBody.Part[] images, HashMap<String, RequestBody> param)
    {
        imageUploadResponse.setValue(ApiResource.loading(null));
        RemoteDataSource.getRemoteDataSource().upload(images,param).enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                if(response.code()==200)
                {
                    imageUploadResponse.setValue(ApiResource.success(response.body()));
                }
                else if(response.code()==422)
                {
                    imageUploadResponse.setValue(ApiResource.error("Invalid Data",null));
                }
                else
                {
                    imageUploadResponse.setValue(ApiResource.error(response.errorBody().toString(),null));
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                Log.e("error",t.getLocalizedMessage());
                imageUploadResponse.setValue(ApiResource.error(t.getMessage()+"\n"+t.getLocalizedMessage(),null));
            }
        });
    }

    public LiveData<ApiResource<ImageUploadResponse>> getImageResponse()
    {
        return imageUploadResponse;
    }
}

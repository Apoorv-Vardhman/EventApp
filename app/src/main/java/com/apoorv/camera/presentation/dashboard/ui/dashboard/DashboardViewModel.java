package com.apoorv.camera.presentation.dashboard.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apoorv.camera.app.Utility;
import com.apoorv.camera.data.data_source.RemoteDataSource;
import com.apoorv.camera.data.response.ApiResource;
import com.apoorv.camera.data.response.ImageDateByResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<ApiResource<ImageDateByResponse>> imageResponse;

    public DashboardViewModel() {
        new MutableLiveData<>();
    }

    public void setImageResponse()
    {
        imageResponse.setValue(ApiResource.loading(null));
        RemoteDataSource.getRemoteDataSource().imageDateWise(Utility.getCurrentDate()).enqueue(new Callback<ImageDateByResponse>() {
            @Override
            public void onResponse(Call<ImageDateByResponse> call, Response<ImageDateByResponse> response) {
                if(response.code()==200)
                {
                    imageResponse.setValue(ApiResource.success(response.body()));
                }
                else
                {
                    imageResponse.setValue(ApiResource.error(response.errorBody().toString(),null));
                }
            }

            @Override
            public void onFailure(Call<ImageDateByResponse> call, Throwable t) {
                imageResponse.setValue(ApiResource.error(t.getMessage(),null));
            }
        });
    }

    public LiveData<ApiResource<ImageDateByResponse>> getImageResponse()
    {
        return imageResponse;
    }

}
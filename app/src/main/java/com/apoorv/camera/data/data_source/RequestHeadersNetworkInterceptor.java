package com.apoorv.camera.data.data_source;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Apoorv Vardhman on 6/28/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class RequestHeadersNetworkInterceptor implements Interceptor {

    private final Map<String, String> headers;

    public RequestHeadersNetworkInterceptor(@NonNull Map<String, String> headers) {
        this.headers = headers;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            if (header.getKey() == null || header.getKey().trim().isEmpty()) {
                continue;
            }
            if (header.getValue() == null || header.getValue().trim().isEmpty()) {
                builder.removeHeader(header.getKey());
            } else {
                builder.header(header.getKey(), header.getValue());
            }
        }
        return chain.proceed(builder.build());
    }

}

package com.apoorv.camera.data.response;

import static com.apoorv.camera.data.response.Status.ERROR;
import static com.apoorv.camera.data.response.Status.LOADING;
import static com.apoorv.camera.data.response.Status.SUCCESS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Apoorv Vardhman on 6/28/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class ApiResource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @Nullable
    public final T data;

    public ApiResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResource<T> success(@Nullable T data) {
        return new ApiResource<>(SUCCESS, data, null);
    }

    public static <T> ApiResource<T> successWithMsg(String msg, @Nullable T data) {
        return new ApiResource<>(SUCCESS, data, msg);
    }

    public static <T> ApiResource<T> error(String msg, @Nullable T data) {
        return new ApiResource<>(ERROR, data, msg);
    }

    public static <T> ApiResource<T> loading(@Nullable T data) {
        return new ApiResource<>(LOADING, data, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiResource<?> resource = (ApiResource<?>) o;

        return status == resource.status && (message != null ? message.equals(resource.message) : resource.message == null) && (data != null ? data.equals(resource.data) : resource.data == null);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ApiResource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}


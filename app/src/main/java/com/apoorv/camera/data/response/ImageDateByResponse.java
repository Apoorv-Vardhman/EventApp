package com.apoorv.camera.data.response;

import com.apoorv.camera.domain.model.ImageUpload;

/**
 * Created by Apoorv Vardhman on 6/29/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class ImageDateByResponse {
    private String message;
    private ImageUpload data;

    public ImageDateByResponse(String message, ImageUpload data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ImageUpload getData() {
        return data;
    }

    public void setData(ImageUpload data) {
        this.data = data;
    }
}

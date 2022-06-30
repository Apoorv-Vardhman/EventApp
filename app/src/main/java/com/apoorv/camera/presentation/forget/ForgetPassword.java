package com.apoorv.camera.presentation.forget;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.apoorv.camera.R;
import com.apoorv.camera.presentation.base.BaseActivity;

/**
 * Created by Apoorv Vardhman on 6/26/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class ForgetPassword extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        setTitle("Reset Password");
    }
}

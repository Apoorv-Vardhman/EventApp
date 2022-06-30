package com.apoorv.camera.presentation.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.apoorv.camera.R;
import com.apoorv.camera.app.Constants;
import com.apoorv.camera.data.local.PrefConnect;
import com.apoorv.camera.presentation.dashboard.Dashboard;
import com.apoorv.camera.presentation.login.LoginActivity;
import com.apoorv.camera.presentation.main.MainActivity;

/**
 * Created by Apoorv Vardhman on 6/29/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class SplashActivity extends Activity {
    private final int SPLASH_SCREEN_TIME_OUT = 2000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = PrefConnect.readString(SplashActivity.this, Constants.TOKEN,"");
                if(token.isEmpty())
                {
                    Intent i=new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashActivity.this, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}

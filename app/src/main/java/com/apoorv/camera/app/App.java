package com.apoorv.camera.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Apoorv Vardhman on 6/26/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class App extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}

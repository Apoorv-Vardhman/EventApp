package com.apoorv.camera.app;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Apoorv Vardhman on 6/29/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class Utility {

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate()
    {
        Date cDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    }
}

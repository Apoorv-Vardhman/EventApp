package com.apoorv.camera.presentation.base;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Apoorv Vardhman on 6/26/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class BaseActivity extends AppCompatActivity {

    public void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int message)
    {
        Toast.makeText(this, message+"", Toast.LENGTH_SHORT).show();
    }
}

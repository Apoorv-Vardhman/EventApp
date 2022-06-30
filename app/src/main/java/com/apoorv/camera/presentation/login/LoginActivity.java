package com.apoorv.camera.presentation.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.apoorv.camera.R;
import com.apoorv.camera.app.Constants;
import com.apoorv.camera.app.Validator;
import com.apoorv.camera.data.data_source.RemoteDataSource;
import com.apoorv.camera.data.local.PrefConnect;
import com.apoorv.camera.data.response.ApiResource;
import com.apoorv.camera.data.response.LoginResponse;
import com.apoorv.camera.presentation.base.BaseActivity;
import com.apoorv.camera.presentation.dashboard.Dashboard;
import com.apoorv.camera.presentation.forget.ForgetPassword;
import com.apoorv.camera.presentation.main.MainActivity;
import com.apoorv.camera.presentation.register.RegisterActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Apoorv Vardhman on 6/26/2022
 *
 * @Email :  apoorv.vardhman@gmail.com
 * @Author :  Apoorv Vardhman (devapoorv.com)
 * @Linkedin :  https://in.linkedin.com/in/apoorv-vardhman
 * @Skype :  apoorv.vardhman
 * Contact :  +91 8434014444
 */
public class LoginActivity extends BaseActivity {

    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    MaterialButton loginButton,registerButton,forgetButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        setTitle("Login");
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password =passwordEditText.getText().toString();
                String email = emailEditText.getText().toString().trim();
                if(email.isEmpty())
                {
                    showToast(getString(R.string.invalid_email));
                    return;
                }
                if(!Validator.isValidEmail(email) && !Validator.isValidPhoneNumber(email))
                {
                    showToast(getString(R.string.invalid_email));
                    return;
                }
                if(password.isEmpty())
                {
                    showToast(getString(R.string.invalid_password));
                    return;
                }
                if(!Validator.isValidPassword(password))
                {
                    showToast(getString(R.string.invalid_password));
                    return;
                }
                HashMap<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",password);
                progressBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                RemoteDataSource.getRemoteDataSource().login(param).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if(response.code()==200)
                        {
                            /*login*/
                            assert response.body() != null;
                            showToast(response.body().getMessage());
                            PrefConnect.writeString(LoginActivity.this, Constants.TOKEN,response.body().getData().getToken());
                            PrefConnect.writeString(LoginActivity.this,Constants.NAME,response.body().getData().getName());
                            PrefConnect.writeString(LoginActivity.this,Constants.EMAIL,response.body().getData().getEmail());
                            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);

                        }
                        else if(response.code()==401)
                        {
                            showToast("Enter valid email or password");
                        }
                        else
                        {
                            showToast("Network error");
                        }
                        progressBar.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        showToast("Check Internet Connection");
                        progressBar.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void initialize() {
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.button_login);
        emailEditText = findViewById(R.id.email);
        registerButton = findViewById(R.id.button_register);
        forgetButton = findViewById(R.id.button_reset);
        progressBar = findViewById(R.id.progress);

    }
}

package com.example.findthesolution.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.findthesolution.activity.LogInActivity;
import com.example.findthesolution.activity.MainActivity;
import com.example.findthesolution.databinding.ActivitySplashBinding;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {


    private ActivitySplashBinding binding;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreference =  getSharedPreferences("User_Sh_Reg", Context.MODE_PRIVATE);
        Boolean isActive = sharedPreference.getBoolean("user_isActive",false);

        try {

            handler = new Handler();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (isActive){

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getBaseContext(), "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        }
                    });
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 3000);
        } catch (Exception ignored) {
        }


    }


}
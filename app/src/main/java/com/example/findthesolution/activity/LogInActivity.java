package com.example.findthesolution.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findthesolution.database.MyAppDatabase;
import com.example.findthesolution.database.Users;
import com.example.findthesolution.databinding.ActivityLogInBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding binding;
    TextInputEditText et_email, et_password;
    Button btn_logIn;
    TextView tv_regs;

    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    private MyAppDatabase myAppDatabase;
    public static final String USERS_DB = "usersDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        et_email = binding.logInEtEmail;
        et_password = binding.logInEtPassword;
        btn_logIn = binding.logInBtnSingIn;
        tv_regs = binding.logInTvRegsTitle;

        sharedPreference = getSharedPreferences("User_Sh_Reg", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();

        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, USERS_DB).allowMainThreadQueries().build();

        tv_regs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getBaseContext(), "تأكد من إدخال البريد وكلمة المرور", Toast.LENGTH_SHORT).show();
                } else {

                    List<Users> usersList = myAppDatabase.myDao().getUsers();
                    for (int x = 0; x < usersList.size(); x++) {
                        Users users = usersList.get(x);
                        int id = users.getId();
                        String s_email = users.getEmail();
                        String s_password = users.getPassword();

                        if (email.equals(s_email) && password.equals(s_password)) {

                            editor.putInt("user_id", id);
                            editor.putBoolean("user_isActive", true);
                            editor.apply();

                            Intent intent = new Intent(getBaseContext(), Splash.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }
        });


    }
}
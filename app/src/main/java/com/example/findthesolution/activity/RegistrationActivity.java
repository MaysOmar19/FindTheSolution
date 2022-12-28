package com.example.findthesolution.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.findthesolution.database.MyAppDatabase;
import com.example.findthesolution.R;
import com.example.findthesolution.database.Users;
import com.example.findthesolution.databinding.ActivityRegistrationBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {


    ActivityRegistrationBinding binding;
    TextInputEditText et_email, et_password, et_name, et_country;
    MaterialButton btn_regs, btn_birth_date;
    RadioButton rb_male, rb_female;
    String birth_date, gender;

    DatePickerDialog datePickerDialog;

    private MyAppDatabase myAppDatabase;
    public static final String USERS_DB = "usersDB";

    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        et_name = binding.regsEtName;
        et_email = binding.regsEtEmail;
        et_password = binding.regsEtPassword;
        et_country = binding.regsEtCountry;
        btn_birth_date = binding.regsBtnBirthDate;
        rb_male = binding.regsLanguageRbMale;
        rb_female = binding.regsLanguageRbFemale;
        btn_regs = binding.regsBtnRegs;

        sharedPreference =  getSharedPreferences("User_Sh_Reg", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();

        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, USERS_DB).allowMainThreadQueries().build();

        btn_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // choose birth date
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) { // i year - i1 month - i2 day
                        i1 = i1 + 1;
                        String date = i1 + " - " + i2 + " - " + i;
                        btn_birth_date.setText(date);
                        birth_date = date;

                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                int style = AlertDialog.THEME_HOLO_LIGHT;

                datePickerDialog = new DatePickerDialog(RegistrationActivity.this, style, dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        rb_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    gender = getResources().getString(R.string.regs_language_rb_male);
                }
            }
        });

        rb_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    gender = getResources().getString(R.string.regs_language_rb_female);

                }
            }
        });

        btn_regs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String country = et_country.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !country.isEmpty() && !birth_date.isEmpty() && !gender.isEmpty()) {

                    myAppDatabase.myDao().addUser(new Users(name, email, password, country, birth_date, gender));

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

                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "تأكد من ادخال كافة البيانات", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
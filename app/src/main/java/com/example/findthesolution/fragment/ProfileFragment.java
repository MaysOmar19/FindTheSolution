package com.example.findthesolution.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.findthesolution.database.MyAppDatabase;
import com.example.findthesolution.R;
import com.example.findthesolution.database.Users;
import com.example.findthesolution.databinding.FragmentProfileBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentProfileBinding binding;

    TextInputEditText et_email, et_password, et_name, et_country;
    MaterialButton btn_edit, btn_birth_date;
    RadioButton rb_male, rb_female;
    String b_date, g_nder, name, email, password, birth_date, gender, country;

    DatePickerDialog datePickerDialog;

    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    private MyAppDatabase myAppDatabase;
    public static final String USERS_DB = "usersDB";

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        et_name = binding.fgProfEtName;
        et_email = binding.fgProfEtEmail;
        et_password = binding.fgProfEtPassword;
        et_country = binding.fgProfEtCountry;
        btn_birth_date = binding.fgProfBtnBirthDate;
        rb_male = binding.fgProfLanguageRbMale;
        rb_female = binding.fgProfLanguageRbFemale;
        btn_edit = binding.fgProfBtnEdit;

        myAppDatabase = Room.databaseBuilder(getContext(), MyAppDatabase.class, USERS_DB).allowMainThreadQueries().build();

        sharedPreference = getActivity().getSharedPreferences("User_Sh_Reg", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();

        int id = sharedPreference.getInt("user_id", 0);


        List<Users> usersList = myAppDatabase.myDao().getUsers();
        for (int x = 0; x < usersList.size(); x++) {
            Users users = usersList.get(x);
            if (id == users.getId()) {
                name = users.getName();
                email = users.getEmail();
                password = users.getPassword();
                country = users.getCountry();
                birth_date = users.getBirth_date();
                gender = users.getGender();
            }
        }

        et_name.setText(name);
        et_email.setText(email);
        et_password.setText(password);
        et_country.setText(country);
        btn_birth_date.setText(birth_date);
        b_date = birth_date; //init
        if (gender.equals(getResources().getString(R.string.regs_language_rb_male))) {
            g_nder = getResources().getString(R.string.regs_language_rb_male); //init
            rb_male.setChecked(true);
        } else if (gender.equals(getResources().getString(R.string.regs_language_rb_female))) {
            g_nder = getResources().getString(R.string.regs_language_rb_female); //init
            rb_female.setChecked(true);
        }


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
                        b_date = date;

                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                int style = AlertDialog.THEME_HOLO_LIGHT;

                datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        rb_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    g_nder = getResources().getString(R.string.regs_language_rb_male);
                }
            }
        });

        rb_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    g_nder = getResources().getString(R.string.regs_language_rb_female);

                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String country = et_country.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !country.isEmpty() && !b_date.isEmpty() && !g_nder.isEmpty()) {

                    myAppDatabase.myDao().updateUser(new Users(id, name, email, password, country, b_date, g_nder));

                    Toast.makeText(getContext(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "لا يمكن ترك أي معلومة فارغة", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }
}
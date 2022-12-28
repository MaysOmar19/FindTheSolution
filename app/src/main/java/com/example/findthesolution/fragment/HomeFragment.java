package com.example.findthesolution.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.findthesolution.activity.LogInActivity;
import com.example.findthesolution.R;
import com.example.findthesolution.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {


    public static final String START_PLAY_FRAGMENT = "startPlayFragment";
    public static final String SETTINGS_FRAGMENT = "settingsFragment";

    FragmentHomeBinding binding;
    Button btn_start_play , btn_settings , btn_log_out ;

    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    //todo تم اضافة هذا الكلاس الخاص بالتحكم بالصوت
    public static MediaPlayer mp ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        btn_start_play = binding.fgHomeBtnStartPlay;
        btn_settings = binding.fgHomeBtnSettings;
        btn_log_out = binding.fgHomeBtnLogOut;
        //todo
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

        mp = MediaPlayer.create(getActivity() , R.raw.sound) ;
        mp.setLooping(true);
        sound_player();
        //todo

        sharedPreference = getActivity().getSharedPreferences("User_Sh_Reg", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();


        btn_start_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content_main,new StartPlayFragment(),START_PLAY_FRAGMENT).addToBackStack(null).commit();

            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content_main,new SettingsFragment(),SETTINGS_FRAGMENT).addToBackStack(null).commit();

            }
        });

        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("تطبيق أوجد الحل");
                builder.setMessage("هل انت متأكد من عملية تسجيل الخروج ؟");

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putBoolean("user_isActive", false);
                        editor.apply();

                        Intent intent = new Intent(getContext(), LogInActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getContext(), "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return binding.getRoot() ;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        sound_player();
    }

    public void sound_player(){
        mp.setVolume(0.25f,0.25f);
        if (sharedPreference.getBoolean(SettingsFragment.APP_SOUNDS_KEY , false)){
            mp.start();
        }
        else{
            mp.pause();
        }
    }

}
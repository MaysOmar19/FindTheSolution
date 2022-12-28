package com.example.findthesolution.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.findthesolution.R;
import com.example.findthesolution.databinding.FragmentProfileBinding;
import com.example.findthesolution.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    public static final String PROFILE_FRAGMENT = "settingsFragment";

    FragmentSettingsBinding binding;
    SwitchCompat switch_sound , switch_notification ;
    Button btn_profile , btn_reset;

    // todo تم اضافة هذه القيمة لتخزين قيمة افتراضية بالشيرد بريفيرنس حتى يتم ايقاف أو تشغيل الصوت
    public static final String APP_SOUNDS_KEY = "sound_play" ;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        switch_sound = binding.fgSettingsSwitchSound;
        switch_notification = binding.fgSettingsSwitchNotification;
        btn_reset = binding.fgSettingsBtnReset;
        btn_profile = binding.fgSettingsBtnProfile;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
        SharedPreferences.Editor DF_Editor = sharedPreferences.edit() ;

        if (sharedPreferences.getBoolean(APP_SOUNDS_KEY , false)){
            binding.fgSettingsSwitchSound.setChecked(true);
        }
        else {
            binding.fgSettingsSwitchSound.setChecked(false);
        }
        binding.fgSettingsSwitchSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean(APP_SOUNDS_KEY, false)) {
                    DF_Editor.putBoolean(APP_SOUNDS_KEY, false);
                    HomeFragment.mp.pause();
                } else {
                    DF_Editor.putBoolean(APP_SOUNDS_KEY, true);
                    HomeFragment.mp.start();
                }
                DF_Editor.apply();
            }
        });



        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content_main,new ProfileFragment(),PROFILE_FRAGMENT).addToBackStack(null).commit();

            }
        });

        return binding.getRoot();
    }


}
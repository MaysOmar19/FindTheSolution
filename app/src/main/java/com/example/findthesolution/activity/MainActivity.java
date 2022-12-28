package com.example.findthesolution.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.findthesolution.R;
import com.example.findthesolution.databinding.ActivityMainBinding;
import com.example.findthesolution.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    public static final String HOME_FRAGMENT = "homeFragment";

    ActivityMainBinding binding;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
           getSupportFragmentManager().beginTransaction().replace( R.id.main_fragment_content_main, new HomeFragment(), HOME_FRAGMENT).commit();
        }

    }
}
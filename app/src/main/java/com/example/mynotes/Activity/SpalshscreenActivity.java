package com.example.mynotes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.databinding.ActivitySpalshscreenBinding;

public class SpalshscreenActivity extends AppCompatActivity {

    private ActivitySpalshscreenBinding binding;
    private AppCompatActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpalshscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
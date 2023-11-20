package com.example.mynotes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.Activity.RoomDatabase.DatabaseHelper;
import com.example.mynotes.Activity.RoomDatabase.Note;
import com.example.mynotes.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;

    private AppCompatActivity activity = this;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Note note1 = (Note) getIntent().getSerializableExtra("data");
        if (note1 != null) {
            binding.tvTitle.setText(note1.getTitle());
            binding.tvContent.setText(note1.getContent());

        }

        databaseHelper = DatabaseHelper.getInstance(activity);

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.tvTitle.getText().toString();
                String content = binding.tvContent.getText().toString();
                databaseHelper.noteDao().updateData(note1.getId(), title, content);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
}
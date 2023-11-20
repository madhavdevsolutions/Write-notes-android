package com.example.mynotes.Activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mynotes.Activity.RoomDatabase.DatabaseHelper;
import com.example.mynotes.Activity.RoomDatabase.Note;
import com.example.mynotes.Adapter.NoteAdapter;
import com.example.mynotes.R;
import com.example.mynotes.databinding.ActivityMainBinding;
import com.example.mynotes.databinding.CustomDialogBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    CustomDialogBinding customDialogBinding;
    DatabaseHelper databaseHelper;
    ArrayList<Note> arrnotes;
    NoteAdapter adapter;
    private ActivityMainBinding binding;
    private AppCompatActivity activity = this;
    private boolean isStaggeredGridLayoutManager = true;

    private boolean isIcon1 = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initVar();

        showNotes();

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(activity);
                customDialogBinding = CustomDialogBinding.inflate(getLayoutInflater());
                dialog.setContentView(customDialogBinding.getRoot());
                dialog.setCancelable(false);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Window window = dialog.getWindow();
                if (window != null) {
                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                    window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
                }

                window.getAttributes().windowAnimations = R.style.DialogAnimation;


                customDialogBinding.btnAddNotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (customDialogBinding.edtTitle.getText().toString().trim().equals("")) {
                            Toast.makeText(MainActivity.this, "Please enter the title of the note", Toast.LENGTH_SHORT).show();
                        } else if (customDialogBinding.edtContent.getText().toString().trim().equals("")) {
                            Toast.makeText(MainActivity.this, "Please enter the content of the note", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseHelper.noteDao().addNote(new Note(customDialogBinding.edtTitle.getText().toString(), customDialogBinding.edtContent.getText().toString()));
                            showNotes();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();

                customDialogBinding.btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }

        });

        binding.btnCreateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fabAdd.performClick();
            }
        });
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    private void filter(String newText) {
        List<Note> filteredList = new ArrayList<>();
        for (Note singleNext : arrnotes) {
            if (singleNext.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNext.getContent().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleNext);
            }
        }
        adapter.filterList(filteredList);
    }

    public void showNotes() {
        arrnotes = (ArrayList<Note>) databaseHelper.noteDao().getNotes();
        if (arrnotes.size() > 0) {
            binding.recyclerNotes.setVisibility(View.VISIBLE);
            binding.llLayout.setVisibility(View.GONE);
            binding.fabAdd.setVisibility(View.VISIBLE);
            adapter = new NoteAdapter(activity, arrnotes, databaseHelper);
            binding.recyclerNotes.setAdapter(adapter);
            binding.searchView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerNotes.setVisibility(View.GONE);
            binding.fabAdd.setVisibility(View.GONE);
            binding.llLayout.setVisibility(View.VISIBLE);
            binding.searchView.setVisibility(View.GONE);
        }
    }

    private void initVar() {
        setRecyclerViewLayoutManager();
        binding.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStaggeredGridLayoutManager = !isStaggeredGridLayoutManager;
                setRecyclerViewLayoutManager();
                changeIcon();
            }
        });
        databaseHelper = DatabaseHelper.getInstance(activity);
    }

    private void setRecyclerViewLayoutManager() {
        RecyclerView.LayoutManager layoutManager;
        if (isStaggeredGridLayoutManager) {
            layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        binding.recyclerNotes.setLayoutManager(layoutManager);
    }


    public void changeIcon() {
        if (isIcon1) {
            binding.ivUpdate.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.grid));
        } else {
            binding.ivUpdate.setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.linear));
        }
        isIcon1 = !isIcon1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNotes();
    }
}
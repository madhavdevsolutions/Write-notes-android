package com.example.mynotes.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.Activity.DetailsActivity;
import com.example.mynotes.Activity.MainActivity;
import com.example.mynotes.Activity.RoomDatabase.DatabaseHelper;
import com.example.mynotes.Activity.RoomDatabase.Note;
import com.example.mynotes.R;
import com.example.mynotes.databinding.DeleteItemDilaogboxBinding;
import com.example.mynotes.databinding.RecyclerNotesItemBinding;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    Context context;
    ArrayList<Note> arrNotes;

    DatabaseHelper databaseHelper;

    public NoteAdapter(Context context, ArrayList<Note> arrNotes, DatabaseHelper databaseHelper) {
        this.context = context;
        this.arrNotes = arrNotes;
        this.databaseHelper = databaseHelper;

    }

    @NonNull
    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerNotesItemBinding recyclerNotesItemBinding = RecyclerNotesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(recyclerNotesItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.MyViewHolder holder, int position) {

        Note note = arrNotes.get(position);

        holder.binding.tvItemTitle.setText(note.getTitle());
        holder.binding.tvItemContent.setText(note.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("data", note);
                context.startActivity(intent);
            }
        });
        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                DeleteItemDilaogboxBinding deleteItemDilaogboxBinding = DeleteItemDilaogboxBinding.inflate(LayoutInflater.from(context.getApplicationContext()));
                dialog.setContentView(deleteItemDilaogboxBinding.getRoot());
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();
                if (window != null) {
                    int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
                    window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
                }

                window.getAttributes().windowAnimations = R.style.DialogAnimation;

                deleteItemDilaogboxBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseHelper.noteDao().deleteNote(new Note(note.getId(), note.getTitle(), note.getContent()));
                        ((MainActivity) context).showNotes();
                        dialog.dismiss();
                    }
                });
                dialog.show();


                deleteItemDilaogboxBinding.btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public void filterList(List<Note> filteredList) {
        arrNotes = (ArrayList<Note>) filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RecyclerNotesItemBinding binding;

        public MyViewHolder(RecyclerNotesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

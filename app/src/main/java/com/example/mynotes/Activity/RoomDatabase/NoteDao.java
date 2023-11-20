package com.example.mynotes.Activity.RoomDatabase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("select * from note")
    List<Note> getNotes();

    @Insert
    void addNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("Update note Set title = :title, content = :content WHERE ID = :id")
    void updateData(int id, String title, String content);
}

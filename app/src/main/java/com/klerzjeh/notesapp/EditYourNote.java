package com.klerzjeh.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class EditYourNote extends AppCompatActivity implements TextWatcher {

    EditText et1;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_note);
        et1 = (EditText) findViewById(R.id.et1);

        Intent i = getIntent();
        noteId = i.getIntExtra("noteId", -1);

        if (noteId != -1) {
            et1.setText(MainActivity.notes.get(noteId));
        }
        et1.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        MainActivity.notes.set(noteId, String.valueOf(s));
        MainActivity.adapter.notifyDataSetChanged();

        SharedPreferences pref = this.getSharedPreferences("com.klerzjeh.notesapp", Context.MODE_PRIVATE);
        if (MainActivity.set == null) {
            MainActivity.set = new HashSet<>();

        } else {
            MainActivity.set.clear();
        }

        MainActivity.set.addAll(MainActivity.notes);
        pref.edit().remove("notes").apply();
        pref.edit().putStringSet("notes", MainActivity.set).apply();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

package com.klerzjeh.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> adapter;
    ListView myListView;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListView = (ListView) findViewById(R.id.myListView);

        SharedPreferences pref = this.getSharedPreferences("com.klerzjeh.notesapp", Context.MODE_PRIVATE);
        set = pref.getStringSet("notes", null);
        notes.clear();
        if (set != null) {

            notes.addAll(set);
        } else {
            notes.add("primjer");
            set = new HashSet<>();
            set.addAll(notes);
            pref.edit().putStringSet("notes", set).apply();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent i = new Intent(getApplicationContext(), EditYourNote.class);
                i.putExtra("noteId", position);
                startActivity(i);

            }
        });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(position);
                                SharedPreferences pref = MainActivity.this.getSharedPreferences("com.klerzjeh.notesapp", Context.MODE_PRIVATE);
                                if (set == null) {
                                    set = new HashSet<>();

                                } else {
                                    set.clear();
                                }

                                set.addAll(notes);
                                pref.edit().putStringSet("notes", set).apply();
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addNote) {

            notes.add("");
            SharedPreferences pref = this.getSharedPreferences("com.klerzjeh.notesapp", Context.MODE_PRIVATE);
            if (set == null) {
                set = new HashSet<>();

            } else {
                set.clear();
            }

            set.addAll(notes);
            adapter.notifyDataSetChanged();
            pref.edit().remove("notes").apply();
            pref.edit().putStringSet("notes", set).apply();


            Intent i = new Intent(getApplicationContext(), EditYourNote.class);
            i.putExtra("noteId", notes.size() - 1);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

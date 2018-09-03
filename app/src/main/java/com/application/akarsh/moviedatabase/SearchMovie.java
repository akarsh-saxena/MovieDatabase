package com.application.akarsh.moviedatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

public class SearchMovie extends AppCompatActivity {

    EditText etName;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_search);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");

        etName = (EditText) findViewById(R.id.etName);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        etName.setTypeface(custom_font);
        final Animation hide_fab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);
        floatingActionButton.setOnClickListener(
                new FloatingActionButton.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString();
                        if(name.isEmpty()){
                            etName.setError("Enter a name");
                        }
                        else {
                            floatingActionButton.startAnimation(hide_fab);
                            Intent intent = new Intent(SearchMovie.this, DisplayPage.class);
                            intent.putExtra("Intent_Value", name);
                            intent.putExtra("Intent_Tag", "search_movie");
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("About")
                    .setMessage("Made by Akarsh Saxena")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}

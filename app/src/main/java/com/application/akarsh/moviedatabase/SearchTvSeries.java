package com.application.akarsh.moviedatabase;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchTvSeries extends AppCompatActivity {

    EditText etName;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv_series);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");

        etName = (EditText) findViewById(R.id.etName);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        etName.setTypeface(custom_font);

        floatingActionButton.setOnClickListener(
                new FloatingActionButton.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString();
                        if(name.isEmpty()){
                            etName.setError("Enter a name");
                        }
                        else {
                            Intent intent = new Intent(SearchTvSeries.this, DisplayPage.class);
                            intent.putExtra("Intent_Value", name);
                            intent.putExtra("Intent_Tag", "search_tv_series");
                            startActivity(intent);
                        }
                    }
                }
        );
    }
}

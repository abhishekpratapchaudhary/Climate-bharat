package com.bharatvarsham.climapm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangeCityController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);
        final EditText editText=(EditText)findViewById(R.id.queryET);

        ImageButton backbutton=(ImageButton)findViewById(R.id.backButton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChangeCityController.this,WeatherController.class);
                startActivity(intent);
                finish();
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String newcity=editText.getText().toString();
                Intent intent=new Intent(ChangeCityController.this,WeatherController.class);
                intent.putExtra("City",newcity);
                startActivity(intent);
                return false;
            }
        });

    }

};

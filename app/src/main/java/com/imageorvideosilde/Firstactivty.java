package com.imageorvideosilde;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Firstactivty extends AppCompatActivity {

    Button simple,custom,recy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstactivty);
        simple = (Button)findViewById(R.id.simple);
        custom = (Button)findViewById(R.id.custom);
        recy = (Button)findViewById(R.id.recy);

        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Firstactivty.this,MainActivity.class);
                startActivity(i);
            }
        });

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Firstactivty.this,CustomPlayer.class);
                startActivity(i);
            }
        });

        recy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Firstactivty.this,Imageviewdisplay.class);
                startActivity(i);
            }
        });


    }
}

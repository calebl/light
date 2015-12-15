package com.bigndesign.light;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_blank);

        final Intent readIntent = new Intent(this, ReadActivity.class);
        final Intent askIntent = new Intent(this, AskActivity.class);
        final Intent learnIntent = new Intent(this, LearnActivity.class);

        Button read = (Button)findViewById(R.id.readButton);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(readIntent);
            }
        });

        Button ask = (Button)findViewById(R.id.askButton);
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(askIntent);
            }
        });

        Button learn = (Button)findViewById(R.id.learnButton);
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(learnIntent);
            }
        });


    }
}

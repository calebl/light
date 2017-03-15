package com.bigndesign.light;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.io.File;

import io.realm.Realm;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_home);

        final Intent readIntent = new Intent(this, ReadActivity.class);
        final Intent askIntent = new Intent(this, LoginOrSignupActivity.class);
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

//        exportDatabase();
    }

    public void exportDatabase() {

        // init realm
        Realm realm = Realm.getDefaultInstance();

        File exportRealmFile = null;
//        try {
            // get or create an "export.realm" file
            exportRealmFile = new File(getExternalCacheDir(), "export.realm");

            // if "export.realm" already exists, delete
            exportRealmFile.delete();

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile);

//        }
        realm.close();

        // init email intent and add export.realm as attachment
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, "YOUR MAIL");
        intent.putExtra(Intent.EXTRA_SUBJECT, "YOUR SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT");
        Uri u = Uri.fromFile(exportRealmFile);
        intent.putExtra(Intent.EXTRA_STREAM, u);

        // start email intent
        startActivity(Intent.createChooser(intent, "YOUR CHOOSER TITLE"));
    }
}

package com.bigndesign.light;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Greg on 3/8/2017.
 */

public class LanguageSelectMenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language_select, menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Show current language selection in overflow menu
        SharedPreferences settings = getSharedPreferences("language_pref", 0);
        String language = settings.getString("language","none");

        if(language.equals("arabic")){
            toolbar.getMenu().getItem(0).setChecked(true);
        } else if(language.equals("french")){
            toolbar.getMenu().getItem(1).setChecked(true);
        } else if (language.equals("spanish")){
            toolbar.getMenu().getItem(2).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings = getSharedPreferences("language_pref", 0);
        SharedPreferences.Editor editor = settings.edit();

        switch (item.getItemId()) {
            case R.id.menu_arabic:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                editor.putString("language", "arabic");
                // Commit the edits
                editor.commit();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.menu_french:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                editor.putString("language", "french");
                // Commit the edits
                editor.commit();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.menu_spanish:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                editor.putString("language", "spanish");
                // Commit the edits
                editor.commit();
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

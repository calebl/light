package com.bigndesign.light;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Greg on 3/8/2017.
 */

public class LanguageSelectMenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language_select, menu);
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
                return true;
            case R.id.menu_french:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                editor.putString("language", "french");
                // Commit the edits
                editor.commit();
                return true;
            case R.id.menu_spanish:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                editor.putString("language", "spanish");
                // Commit the edits
                editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

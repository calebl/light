package com.bigndesign.light;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.activeandroid.query.Select;
import com.bigndesign.light.Model.Book;
import com.bigndesign.light.Model.Chapter;
import com.bigndesign.light.Model.Verses;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView webView = (WebView) findViewById(R.id.readView);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String versesString = loadVerses();
        try {
            JSONObject versesObj = new JSONObject(versesString);
            String text = versesObj.getString("text");

            webView.loadData(text,"text/html; charset=utf-8", "UTF-8");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String loadVerses() {
        String json = null;

        Book book = new Select().from(Book.class).orderBy("bookOrder").executeSingle();

        Chapter chapter = new Select().from(Chapter.class).where("book = ?", book.getId()).orderBy("chapterOrder").executeSingle();

        Verses verses = chapter.verses;

        if(verses == null){
            verses = new Select().from(Verses.class).where("chapter = ?", chapter).executeSingle();
        }

        return verses.text;

    }

}

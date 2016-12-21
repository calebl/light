package com.bigndesign.light;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.bigndesign.light.Model.Book;
import com.bigndesign.light.Model.Chapter;
import com.bigndesign.light.Model.Verses;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import io.realm.Realm;

public class ReadActivity extends AppCompatActivity {

    private Verses verses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final WebView webView = (WebView) findViewById(R.id.readView);

        Button previousButton = (Button) findViewById(R.id.previousButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPreviousChapterVerses();

                webView.loadData(verses.getText(),"text/html; charset=utf-8", "UTF-8");
            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextChapterVerses();

                webView.loadData(verses.getText(),"text/html; charset=utf-8", "UTF-8");
            }
        });

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDefaultVerses();

        webView.loadData(verses.getText(),"text/html; charset=utf-8", "UTF-8");
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

    public void loadDefaultVerses() {
        Realm realm = Realm.getDefaultInstance();

        Book book = realm.where(Book.class).findAllSorted("bookOrder").first();

        Chapter chapter = book.getChapters().sort("chapterOrder").first();

        verses = chapter.getVerses();
    }

    public void loadPreviousChapterVerses(){
        Realm realm = Realm.getDefaultInstance();

        String previousChapterId = verses.getPrevChapterId();

        Chapter chapter = realm.where(Chapter.class).equalTo("id", previousChapterId).findFirst();

        if (chapter != null){
            verses = chapter.getVerses();
        }
    }

    public void loadNextChapterVerses(){
        Realm realm = Realm.getDefaultInstance();

        String nextChapterId = verses.getNextChapterId();

        Chapter chapter = realm.where(Chapter.class).equalTo("id", nextChapterId).findFirst();

        if (chapter != null){
            verses = chapter.getVerses();
        }
    }
}

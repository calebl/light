package com.bigndesign.light;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.activeandroid.ActiveAndroid;
import com.bigndesign.light.Model.Book;
import com.bigndesign.light.Model.Chapter;
import com.bigndesign.light.Model.Verses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class LanguageSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selector);

        final Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        final Bundle bundle = new Bundle();

        Button selectArabic = (Button)findViewById(R.id.selectArabic);
        selectArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTexts("arabic");
                bundle.putString("language", "arabic");

                startActivity(homeIntent, bundle);
                finish();
            }
        });

        Button selectSpanish = (Button)findViewById(R.id.selectSpanish);
        selectSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTexts("spanish");
                bundle.putString("language", "spanish");

                startActivity(homeIntent, bundle);
                finish();
            }
        });

        Button selectFrench = (Button)findViewById(R.id.selectFrench);
        selectFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTexts("french");
                bundle.putString("language", "french");

                startActivity(homeIntent, bundle);
                finish();
            }
        });

    }

    public void loadTexts(String language) {
        String booksString = null;
        try {

            InputStream is = getAssets().open(language + "/books.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            booksString = new String(buffer, "UTF-8");

            JSONArray books = new JSONArray(booksString);

            ActiveAndroid.beginTransaction();

            Verses.truncate();
            Chapter.truncate();
            Book.truncate();

            ActiveAndroid.endTransaction();

            ActiveAndroid.beginTransaction();

            for (int i = 0; i < books.length(); i++) {
                JSONObject bookObj = books.getJSONObject(i);

                Book book = new Book();

                book.version_id = bookObj.getString("version_id");
                book.abbreviation = bookObj.getString("abbr");
                book.book_group_id = bookObj.getInt("book_group_id");
                book.book_order = bookObj.getInt("ord");
                book.id = bookObj.getString("id");
                book.name = bookObj.getString("name");
                book.osis_end = bookObj.getString("osis_end");
                book.testament = bookObj.getString("testament");

                book.save();

                String chaptersFileName = book.id.replace(":","_")+".json";
                String chaptersString;

                //load chapters files for this book
                is = getAssets().open(language + "/chapters/" + chaptersFileName);
                size = is.available(); buffer = new byte[size]; is.read(buffer); is.close();

                chaptersString = new String(buffer, "UTF-8");

                JSONArray chapters = new JSONArray(chaptersString);

                for(int j=0; j < chapters.length(); j++){
                    JSONObject chapterObj = chapters.getJSONObject(j);

                    Chapter chapter = new Chapter();

                    chapter.book = book;

                    chapter.id = chapterObj.getString("id");
                    chapter.chapter_order = chapterObj.getInt("order");
                    chapter.chapter = chapterObj.getString("chapter");
                    chapter.label = chapterObj.getString("label");
                    chapter.display = chapterObj.getString("display");
                    chapter.osis_end = chapterObj.getString("osis_end");

                    chapter.save();

                    String versesFileName = chapter.id.replace(":","_")+".json";
                    String versesString;

                    //load verses files for this book
                    is = getAssets().open(language + "/verses/" + versesFileName);
                    size = is.available(); buffer = new byte[size]; is.read(buffer); is.close();

                    versesString = new String(buffer, "UTF-8");

                    try {

                        JSONObject versesObj = new JSONObject(versesString);

                        Verses verses = new Verses();

                        verses.book = book;
                        verses.chapter = chapter;

                        verses.label = versesObj.getString("label");
                        verses.display = versesObj.getString("display");
                        verses.id = versesObj.getString("id");
                        verses.nextChapterId = versesObj.getString("nextChapterId");
                        verses.prevChapterId = versesObj.getString("prevChapterId");
                        verses.text = versesObj.getString("text");
                        verses.verse_order = versesObj.getInt("order");

                        verses.save();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                }




            }

            ActiveAndroid.setTransactionSuccessful();

        }catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }
}

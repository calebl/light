package com.bigndesign.light;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bigndesign.light.Model.Book;
import com.bigndesign.light.Model.Chapter;
import com.bigndesign.light.Model.Verses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;

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

            Verses.truncate();
            Chapter.truncate();
            Book.truncate();

            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();

            for (int i = 0; i < books.length(); i++) {
                JSONObject bookObj = books.getJSONObject(i);

                Book book = new Book();

                book.setVersionId(bookObj.getString("version_id"));
                book.setAbbreviation(bookObj.getString("abbr"));
                book.setBookGroupId(bookObj.getInt("book_group_id"));
                book.setBookOrder(bookObj.getInt("ord"));
                book.setId(bookObj.getString("id"));
                book.setName(bookObj.getString("name"));
                book.setOsisEnd(bookObj.getString("osis_end"));
                book.setTestament(bookObj.getString("testament"));

                realm.copyToRealm(book);


                String chaptersFileName = book.getId().replace(":","_")+".json";
                String chaptersString;

                //load chapters files for this book
                is = getAssets().open(language + "/chapters/" + chaptersFileName);
                size = is.available(); buffer = new byte[size]; is.read(buffer); is.close();

                chaptersString = new String(buffer, "UTF-8");

                JSONArray chapters = new JSONArray(chaptersString);

                for(int j=0; j < chapters.length(); j++){
                    JSONObject chapterObj = chapters.getJSONObject(j);

                    Chapter chapter = new Chapter();

                    chapter.setBook(book);

                    chapter.setId(chapterObj.getString("id"));
                    chapter.setChapterOrder(chapterObj.getInt("order"));
                    chapter.setChapter(chapterObj.getString("chapter"));
                    chapter.setLabel(chapterObj.getString("label"));
                    chapter.setDisplay(chapterObj.getString("display"));
                    chapter.setOsisEnd(chapterObj.getString("osis_end"));

                    realm.copyToRealmOrUpdate(chapter);

                    book.getChapters().add(chapter);

                    String versesFileName = chapter.getId().replace(":","_")+".json";
                    String versesString;

                    //load verses files for this book
                    is = getAssets().open(language + "/verses/" + versesFileName);
                    size = is.available(); buffer = new byte[size]; is.read(buffer); is.close();

                    versesString = new String(buffer, "UTF-8");

                    try {

                        JSONObject versesObj = new JSONObject(versesString);

                        Verses verses = new Verses();

                        verses.setBook(book);
                        verses.setChapter(chapter);

                        verses.setLabel(versesObj.getString("label"));
                        verses.setDisplay(versesObj.getString("display"));
                        verses.setId(versesObj.getString("id"));
                        verses.setNextChapterId(versesObj.getString("nextChapterId"));
                        verses.setPrevChapterId(versesObj.getString("prevChapterId"));
                        verses.setText(versesObj.getString("text"));
                        verses.setVerseOrder(versesObj.getInt("order"));

                        realm.copyToRealmOrUpdate(verses);

                        chapter.setVerses(verses);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                }




            }



        }catch (JSONException | IOException e) {
            e.printStackTrace();
            Realm.getDefaultInstance().cancelTransaction();
        } finally {
            Realm.getDefaultInstance().commitTransaction();
        }

    }
}

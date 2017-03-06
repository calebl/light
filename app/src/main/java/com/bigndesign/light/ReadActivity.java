package com.bigndesign.light;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.bigndesign.light.Model.Book;
import com.bigndesign.light.Model.Chapter;
import com.bigndesign.light.Model.Verses;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ReadActivity extends AppCompatActivity {

    private Verses verses;
    private Button nextButton;
    private Button previousButton;
    private Button menuButton;
    private TextView title;
    private List<Book> bookList;
    private PopupMenu popupMenu;
    private Realm realm;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences settings = getSharedPreferences("language_pref", 0);
        language = settings.getString("language","none");

        DrawableAwesome drable = new DrawableAwesome.DrawableAwesomeBuilder( getApplicationContext(),R.string.fa_comments).build();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(drable);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        realm = getRealmDatabase();

        final WebView webView = (WebView) findViewById(R.id.readView);

        //Populate bookList
        loadBookList();

        //Create menu
        createPopupMenu();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Register popupMenu with OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Get chapter id and load new verses
                        String bookName = bookList.get(item.getGroupId()).getName();
                        String chapterId = (String)item.getTitle();
                        loadVerses(bookName, chapterId);

                        //Load new verses
                        webView.loadData(verses.getText(),"text/html; charset=utf-8", "UTF-8");
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //Make previous/next buttons functional
        nextButton     = (Button) findViewById(R.id.nextButton);
        previousButton = (Button) findViewById(R.id.previousButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPreviousChapterVerses();

                webView.loadData(verses.getText(),"text/html; charset=utf-8", "UTF-8");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextChapterVerses();

                webView.loadData(verses.getText(),"text/html; charset=utf-8", "UTF-8");
            }
        });

        //Load the startup text
        loadDefaultVerses();

        webView.loadData(verses.getText(),"text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language_select, menu);
        return true;
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

    public void createPopupMenu() {
        //Load menu
        menuButton = (Button) findViewById(R.id.menuButton);
        popupMenu = new PopupMenu(ReadActivity.this, menuButton);

        try{
            addOptionsToMenu();
        } catch (NullPointerException e){
            //If the bookList is null, load it; then try again
            loadBookList();
            addOptionsToMenu();
        }
    }

    public void addOptionsToMenu(){
        //Add options to menu
        for (int i = 0; i < bookList.size(); i++) {
            SubMenu subMenu = popupMenu.getMenu().addSubMenu(i, i, i, bookList.get(i).getName());

            //Add chapters
            for (int j = 1; j <= bookList.get(i).getChapters().size(); j++) {
                String number = "" + j;
                subMenu.add(i, j, j, number);
            }
        }
    }

    public void loadDefaultVerses() {
        Book book = realm.where(Book.class).findAllSorted("bookOrder").first();

        Chapter chapter = book.getChapters().sort("chapterOrder").first();

        verses = chapter.getVerses();

        setButtonVisibility(previousButton, false);

        title.setText(verses.getDisplay());
    }

    public void loadPreviousChapterVerses(){
        String previousChapterId = verses.getPrevChapterId();
        Chapter chapter = realm.where(Chapter.class).equalTo("id", previousChapterId).findFirst();
        Book book = chapter.getBook();

        if (chapter != null){
            verses = chapter.getVerses();
        }

        //Update header
        title.setText(verses.getDisplay());

        //If at first chapter, disable previous button
        if(verses.getPrevChapterId().equals("null")){
            setButtonVisibility(previousButton, false);
        }

        //If going back from last chapter, make next button visible again
        if(nextButton.getVisibility() == View.INVISIBLE){
            setButtonVisibility(nextButton, true);
        }
    }

    public void loadNextChapterVerses(){
        String nextChapterId = verses.getNextChapterId();
        Chapter chapter = realm.where(Chapter.class).equalTo("id", nextChapterId).findFirst();
        Book book = chapter.getBook();

        if (chapter != null){
            verses = chapter.getVerses();
        }

        //Update header
        title.setText(verses.getDisplay());

        //If at last chapter, disable next button
        if(verses.getNextChapterId().equals("null")){
            setButtonVisibility(nextButton, false);
        }

        //If moving forward from first chapter, make previous button visible again
        if(previousButton.getVisibility() == View.INVISIBLE){
            setButtonVisibility(previousButton, true);
        }
    }

    public void loadVerses(String book, String chapterNumber){
        Chapter chapter = realm.where(Chapter.class).equalTo("book.name", book).equalTo("chapter",chapterNumber).findFirst();

        if (chapter != null){
            verses = chapter.getVerses();
        }

        //Update header
        title.setText(verses.getDisplay());

        //If at first chapter, disable previous button
        if(chapter != null && chapter.getChapterOrder().equals(1001)){
            setButtonVisibility(previousButton, false);
        } else if (previousButton.getVisibility() == View.INVISIBLE){
            setButtonVisibility(previousButton, true);
        }

        //If at last chapter, disable next button
        if(chapter != null && chapter.getChapterOrder().equals(75022)){
            setButtonVisibility(nextButton, false);
        } else if (nextButton.getVisibility() == View.INVISIBLE){
            setButtonVisibility(nextButton, true);
        }
    }

    /**
     * Sets next/previous button visibility
     * @param button The button to be changed
     * @param isVisible
     */
    public void setButtonVisibility(Button button, boolean isVisible){
        if(isVisible){
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    public void loadBookList(){
        bookList = realm.where(Book.class).findAllSorted("bookOrder");
    }

    public Realm getRealmDatabase(){

        String realmFile = "spanish.realm";
        if(language.equals("spanish")){
            realmFile = "spanish.realm";
        } else if(language.equals("arabic")){
            realmFile= "arabic.realm";
        } else if (language.equals("french")){
            realmFile = "french.realm";
        }

        RealmConfiguration config = new RealmConfiguration.Builder()
                .assetFile(realmFile)
                .build();

        return Realm.getInstance(config);
    }
}

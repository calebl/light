package com.bigndesign.light;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigndesign.light.Model.Book;
import com.bigndesign.light.Model.Chapter;
import com.bigndesign.light.Model.Verses;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.List;

import io.realm.Realm;

public class ReadActivity extends AppCompatActivity {

    private Verses verses;
    private Button nextButton;
    private Button previousButton;
    private Button menuButton;
    private TextView title;
    private List<Book> bookList;

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
        }

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final WebView webView = (WebView) findViewById(R.id.readView);

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

        //Load menu
        menuButton = (Button) findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Popup menu
                PopupMenu popupMenu = new PopupMenu(ReadActivity.this, menuButton);

                //Add options to menu
                for(int i = 0; i < bookList.size(); i++){
                    //popupMenu.getMenu().add(i, i, i, bookList.get(i).getName());
                    SubMenu subMenu = popupMenu.getMenu().addSubMenu(i, i, i, bookList.get(i).getName());

                    //Add chapters
                    for(int j = 1; j <= bookList.get(i).getChapters().size(); j++){
                        String number = "" + j;
                        //popupMenu.getMenu().addSubMenu(j, j, j, number);

                        //subMenu.setGroupVisible(j, false);
                        subMenu.add(number);
                    }
                }

                //registering popup with OnMenuItemClickListener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(
                                ReadActivity.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //Load the startup text
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
        bookList = realm.where(Book.class).findAllSorted("bookOrder");

        Chapter chapter = book.getChapters().sort("chapterOrder").first();

        verses = chapter.getVerses();

        setButtonVisibility(previousButton, false);

        title.setText(verses.getDisplay());
    }

    public void loadPreviousChapterVerses(){
        Realm realm = Realm.getDefaultInstance();

        String previousChapterId = verses.getPrevChapterId();
        Chapter chapter = realm.where(Chapter.class).equalTo("id", previousChapterId).findFirst();
        Book book = chapter.getBook();

        if (chapter != null){
            verses = chapter.getVerses();
        }

        //Update header
        title.setText(verses.getDisplay());

        //If at first chapter, disable previous button
        if(chapter != null && chapter.getChapterOrder().equals(1001)){
            setButtonVisibility(previousButton, false);
        }

        //If going back from last chapter, make next button visible again
        if(nextButton.getVisibility() == View.INVISIBLE){
            setButtonVisibility(nextButton, true);
        }
    }

    public void loadNextChapterVerses(){
        Realm realm = Realm.getDefaultInstance();

        String nextChapterId = verses.getNextChapterId();
        Chapter chapter = realm.where(Chapter.class).equalTo("id", nextChapterId).findFirst();
        Book book = chapter.getBook();

        if (chapter != null){
            verses = chapter.getVerses();
        }

        //Update header
        title.setText(verses.getDisplay());

        //If at last chapter, disable next button
        if(chapter != null && chapter.getChapterOrder().equals(75022)){
            setButtonVisibility(nextButton, false);
        }

        //If moving forward from first chapter, make previous button visible again
        if(previousButton.getVisibility() == View.INVISIBLE){
            setButtonVisibility(previousButton, true);
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
}

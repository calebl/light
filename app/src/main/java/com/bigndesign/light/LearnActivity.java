package com.bigndesign.light;

/**
 *  ExpandableListView adapted from http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bigndesign.light.adapters.Adapters.ExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LearnActivity extends LanguageSelectMenuActivity {
    private List<String> groupList;
    private List<String> childList;
    private Map<String, List<String>> faqCollection;
    private ExpandableListView expListView;
    private JSONArray data;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.toolbar_title_learn);
        title.setText(R.string.learn_title);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        findViewById(R.id.appbarLearn).bringToFront();

        //Ask a question icon
        DrawableAwesome drable = new DrawableAwesome.DrawableAwesomeBuilder( getApplicationContext(),R.string.fa_comments).build();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(drable);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginOrSignupActivity.class);
                startActivity(loginActivity);
            }
        });


        //Load faq data
        loadText();
        createContent();

        expListView = (ExpandableListView) findViewById(R.id.faq_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, faqCollection);
        expListView.setAdapter(expListAdapter);
    }

    public void loadText(){
        try{
            InputStream is = getAssets().open("learn_content/faqs.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String faqsString = new String(buffer, "UTF-8");

            data = new JSONArray(faqsString);
        } catch (JSONException | IOException e){
            e.printStackTrace();
        }

    }

    private void createContent() {
        groupList = new ArrayList<String>();
        faqCollection = new LinkedHashMap<String, List<String>>();

        try{
            for (int i = 0; i < data.length(); i++){
                JSONObject dataObj = data.getJSONObject(i);
                groupList.add(dataObj.getString("title"));
                loadChild(dataObj.getString("content"));
                faqCollection.put(dataObj.getString("title"), childList);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void loadChild(String content) {
        childList = new ArrayList<String>();
        childList.add(content);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
}

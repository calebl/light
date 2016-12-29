package com.bigndesign.light;

/**
 *  ExpandableListView adapted from http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;

import com.bigndesign.light.adapters.ExpandableListAdapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LearnActivity extends AppCompatActivity {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> faqCollection;
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.faq_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, faqCollection);
        expListView.setAdapter(expListAdapter);
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Suspendisse purus leo?");
        groupList.add("Aliquam vehicula sed erat in blandit?");
        groupList.add("Curabitur sed justo vel diam egestas rhoncus eu gravida nunc?");
        groupList.add("Vestibulum lorem odio, faucibus sit amet lectus et?");
        groupList.add("Sed dignissim consectetur elit, bibendum egestas ipsum molestie at. Suspendisse quis ultricies magna?");
        groupList.add("Vivamus efficitur ultricies tincidunt?");
        groupList.add("Suspendisse purus leo?");
        groupList.add("Aliquam vehicula sed erat in blandit?");
        groupList.add("Curabitur sed justo vel diam egestas rhoncus eu gravida nunc?");
        groupList.add("Vestibulum lorem odio, faucibus sit amet lectus et?");
        groupList.add("Sed dignissim consectetur elit, bibendum egestas ipsum molestie at. Suspendisse quis ultricies magna?");
        groupList.add("Vivamus efficitur ultricies tincidunt?");
        groupList.add("Vestibulum lorem odio, faucibus sit amet lectus et?");
        groupList.add("Sed dignissim consectetur elit, bibendum egestas ipsum molestie at. Suspendisse quis ultricies magna?");
        groupList.add("Vivamus efficitur ultricies tincidunt?");
    }

    private void createCollection() {
        // preparing faq collection(child)
        String[] hpModels = { "HP Pavilion G6-2014TX", "ProBook HP 4540",
                "HP Envy 4-1025TX" };
        String[] hclModels = { "HCL S2101", "HCL L2102", "HCL V2002" };
        String[] lenovoModels = { "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "      +
                "Suspendisse purus leo, aliquet sed purus eu, lacinia semper mi. Aliquam "         +
                "vehicula sed erat in blandit. Maecenas auctor, est nec tristique pellentesque, "  +
                "tellus urna venenatis risus, convallis lacinia nibh nibh eget felis. Sed "        +
                "tristique rhoncus sagittis. Aenean congue mattis pretium. Maecenas mollis felis " +
                "et felis vestibulum elementum. Suspendisse sit amet suscipit magna. Quisque "     +
                "aliquam interdum dictum." };
        String[] sonyModels = { "VAIO E Series", "VAIO Z Series",
                "VAIO S Series", "VAIO YB Series" };
        String[] dellModels = { "Inspiron", "Vostro", "XPS" };
        String[] samsungModels = { "NP Series", "Series 5", "SF Series" };

        faqCollection = new LinkedHashMap<String, List<String>>();

        for (String faq : groupList) {
            if (faq.equals("HP")) {
                loadChild(hpModels);
            } else if (faq.equals("Dell"))
                loadChild(dellModels);
            else if (faq.equals("Sony"))
                loadChild(sonyModels);
            else if (faq.equals("HCL"))
                loadChild(hclModels);
            else if (faq.equals("Samsung"))
                loadChild(samsungModels);
            else
                loadChild(lenovoModels);

            faqCollection.put(faq, childList);
        }
    }

    private void loadChild(String[] faqs) {
        childList = new ArrayList<String>();
        for (String faq : faqs)
            childList.add(faq);
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

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_learn, menu);
        return true;
    }*/
}

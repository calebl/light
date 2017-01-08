package com.bigndesign.light.adapters.Adapters;

/**
 * Created by Greg on 12/29/2016.
 * This class adapted from http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bigndesign.light.R;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> faqCollections;
    private List<String> faqs;

    public ExpandableListAdapter(Activity context, List<String> faqs,
                                 Map<String, List<String>> faqCollections) {
        this.context = context;
        this.faqCollections = faqCollections;
        this.faqs = faqs;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return faqCollections.get(faqs.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String faq = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.faq);

        item.setText(faq);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return faqCollections.get(faqs.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return faqs.get(groupPosition);
    }

    public int getGroupCount() {
        return faqs.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String faqName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.faq);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(faqName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

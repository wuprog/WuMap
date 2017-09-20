package com.example.zzmech.wumap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Connor on 9/19/2017.
 */

public class BldgActivity extends AppCompatActivity {

    public ListView bldgListView;
    private int bldgSel = -1;
    private TextView textView;
    private LinearLayout layout;
    private String[] bldgDesc;
    private String[] bldgNames;

    //Intent example -Connor
    //TODO Add ListViews in here and set the content
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Not sure about this yet :RIP:
        Typeface boldFace = Typeface.createFromAsset(getAssets(), "Gravity_Bold.otf");
        final Typeface face = Typeface.createFromAsset(getAssets(), "Gravity_Regular.otf");

        textView = (TextView) findViewById(R.id.textView);
        textView.setTypeface(boldFace);
        layout = (LinearLayout) findViewById(R.id.layout);
        bldgDesc = getResources().getStringArray(R.array.bldg_desc);

        bldgListView = new ListView(this);
        bldgListView.setBackgroundColor(Color.parseColor("#002c5f"));
        bldgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bldgSel = position;
                setContentView(layout);
                textView.setText(bldgDesc[bldgSel]);
            }
        });


        ArrayList<String> bldgList = new ArrayList<String>();
        for (int i = 0; i < bldgNames.length; i++) bldgList.add(bldgNames[i]);
        bldgListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bldgList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                tv.setTypeface(face);
                return view;
            }
        });

        setContentView(bldgListView); //Not actually displaying yet
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, BldgActivity.class);
        return i;
    }

}

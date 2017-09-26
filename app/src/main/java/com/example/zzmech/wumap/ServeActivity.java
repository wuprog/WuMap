package com.example.zzmech.wumap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//Fonts
//Gravity_Bold.otf
//Gravity_Regular.otf

public class ServeActivity extends AppCompatActivity {
    private ListView serveListView;
    private String[]
            serveNames,
            serveDesc;
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, ServeActivity.class);
        return i;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.setContentView(R.layout.listview_simple);
        serveNames = getResources().getStringArray(R.array.serve_names);
        serveDesc = getResources().getStringArray(R.array.serve_desc);
        serveListView = (ListView)findViewById(R.id.listview_simple);
        serveListView.setBackgroundColor(Color.parseColor("#002c5f"));
        serveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO Set Current Building with Service
            }
        });
        ArrayList<String> serveList = new ArrayList<String>();
        for (int i = 0; i < serveNames.length; i++) {serveList.add(serveNames[i]);}
        serveListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, serveList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                tv.setTypeface(Typeface.createFromAsset(getAssets(),"Gravity_Regular.otf"));
                return view;
            }
        });
    }
}

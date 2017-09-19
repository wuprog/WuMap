package com.example.zzmech.wumap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Connor on 9/19/2017.
 */

public class BldgActivity extends AppCompatActivity {

    //Intent example -Connor
    //TODO Add ListViews in here and set the content
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, BldgActivity.class);
        return i;
    }
}

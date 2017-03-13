package com.example.zzmech.wumap;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class InfoFrag extends Fragment
{
    private TextView editText;
    private TextView textView;
    private TextView textView1;
    private Button saveBut;
    private WebView wv;

    public InfoFrag()
    {

    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_info, parent, false);

        return v;
    }
}

package com.example.zzmech.wumap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class WarningFrag extends Fragment
{
    private TextView textView;
    private TextView textView1;
    private Button intentBut;

    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;

    public WarningFrag()
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
        View v = inflater.inflate(R.layout.fragment_warning_frag, parent, false);

        intentBut = (Button)v.findViewById(R.id.intentButton);

        intentBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(action));
            }
        });



        return v;
    }

}
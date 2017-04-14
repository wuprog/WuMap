package com.example.zzmech.wumap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
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
    private TextView titleView;
    private Button intentBut;
    private Button xBut;

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
        xBut = (Button)v.findViewById(R.id.xButton);

        Typeface boldFace = Typeface.createFromAsset(getActivity().getAssets(), "Sansation_Bold.ttf");
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Sansation_Regular.ttf");

        textView = (TextView)v.findViewById(R.id.warning_text);
        textView.setTypeface(face);
        titleView = (TextView)v.findViewById(R.id.warning_title);
        titleView.setTypeface(boldFace);

        intentBut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(action));
            }
        });

        xBut.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Fragment dummy;

                                        dummy = getActivity().getFragmentManager().findFragmentByTag("warning");

                                        getActivity().getFragmentManager().beginTransaction().remove(dummy).commit();

                                        ((MainActivity)getActivity()).setWarningOpen(false);
                                    }


                                });

        return v;
    }

}
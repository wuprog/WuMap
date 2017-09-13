package com.example.zzmech.wumap;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class InfoFrag extends Fragment {
    private TextView descText;
    private TextView titleText;
    private Button xButton;
    private ImageView imageView;

    private int[] imgRes;
    private String selName;
    private String description;
    private int selectedInt;
    private String usedRes;
    private Bundle args;
    private TypedArray bldgPhotos;

    public InfoFrag() {

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, parent, false);

        bldgPhotos = getResources().obtainTypedArray(R.array.bldg_photos);

        titleText = (TextView) v.findViewById(R.id.titleText);

        descText = (TextView) v.findViewById(R.id.descText);
        imageView = (ImageView) v.findViewById(R.id.photoView);

        Typeface boldFace = Typeface.createFromAsset(getActivity().getAssets(), "Gravity_Bold.otf");
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Gravity_Regular.otf");

        titleText.setTypeface(boldFace);
        args = this.getArguments();

        selectedInt = args.getInt("value");
        selName = args.getString("name");
        description = args.getString("description");

        titleText.setText(selName);
        descText.setText(description);
        descText.setTypeface(face);
        imageView.setImageResource(bldgPhotos.getResourceId(selectedInt, -1));

        bldgPhotos.recycle();
        xButton = (Button) v.findViewById(R.id.xButton1);

        xButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           CharSequence text = "You pressed the info x!";
                                           int duration = Toast.LENGTH_SHORT;

                                           Log.d("CLICK", "XX");

                                           Fragment dummy;

                                           dummy = getActivity().getFragmentManager().findFragmentByTag("info");

                                           getActivity().getFragmentManager().beginTransaction().remove(dummy).commit();

                                           ((MainActivity) getActivity()).setInfoOpen(false);
                                       }
                                   }
        );

        return v;
    }
}

package com.example.zzmech.wumap;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.graphics.*;
import android.content.*;

import java.util.*;

import static com.example.zzmech.wumap.R.id.info;
import static com.example.zzmech.wumap.R.id.text;
import static java.security.AccessController.getContext;

public class MainActivity extends Activity {

    public static final int REQUEST_CODE_CALCULATE = 1;

    private CustomView customView;
    private LinearLayout layout;
    private TextView textView;
    private TextView mainText;
    private ListView bldgListView;
    private ListView deptListView;
    private ListView adminListView;
    private ListView serveListView;
    private ImageButton bldgButton;
    private ImageButton deptButton;
    private Button adminButton;
    private ImageButton serveButton;
    private ImageButton warningButton;

    private String[] bldgNames;
    private String[] bldgDesc;
    private String[] bldgXS;
    private String[] bldgYS;
    private float[] bldgX;
    private float[] bldgY;

    private String[] deptXVals;
    private String[] deptYVals;
    private float[] deptXVal;
    private float[] deptYVal;

    private String[] deptNames;
    private String[] deptDesc;
    private int[] deptBldg = {2, 0, 16, 16, 13, 16, 2, 4, 13, 7, 2, 15, 7, 13, 13, 6, 13, 16, 7, 7, 2, 7, 6};
    private String[] serveNames;
    private String[] serveDesc;
    private int[] serveBldg = {12, 12};
    private int bldgSel = -1;
    private TextView myLocationText;

    private ImageButton hamBurgMenu;
    private ImageButton dotButton;

    Location globalLoc;
    float newXf;
    float newYf;
    boolean outOfBounds;
    boolean locationFound;
    private Bitmap bitmap = null;
    private Bitmap marker = null;
    private Bitmap markerScaled = null;
    private Bitmap[] markerArray;
    float markerSize = 20;//100
    float[] markerSizeArray;
    boolean startOK = false;
    boolean grow = true;
    boolean cycleBitmapUp = true;
    double resizer = .32;//.31
    int BMArraySize = 100;//200
    int BMCtr = 0;

    boolean created = false;

    private WarningFrag warningFrag;
    private CompassFrag compassFrag;
    private InfoFrag infoFrag;
    private FragmentManager fm;
    private String infoTag, warningTag;

    private ImageButton infoButton;
    private Boolean infoOpen, warningOpen;
    Bundle args;
    Resources r;
    int px;

    RelativeLayout.LayoutParams params;
    LinearLayout.LayoutParams params_rl;

    private RelativeLayout relativeLayout;

    public void setInfoOpen(Boolean infoOpen) {
        this.infoOpen = infoOpen;
    }

    public void setWarningOpen(Boolean warningOpen) {
        this.warningOpen = warningOpen;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBitmaps(BMArraySize);

        args = new Bundle();

        Typeface boldFace = Typeface.createFromAsset(getAssets(), "Gravity_Bold.otf");
        final Typeface face = Typeface.createFromAsset(getAssets(), "Gravity_Regular.otf");

        myLocationText = (TextView) findViewById(R.id.myLocationText);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_top);

        textView = (TextView) findViewById(R.id.textView);
        textView.setTypeface(boldFace);

        infoOpen = false;
        warningOpen = false;
        infoTag = "info";
        warningTag = "warning";

        warningButton = (ImageButton) findViewById(R.id.warningButton);
        infoButton = (ImageButton) findViewById(R.id.infoButton);

        infoButton.setVisibility(View.GONE);
        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params_rl = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.text_height));

        params.setMargins(0, 0, 0, 0);

        textView.setLayoutParams(params);

        fm = getFragmentManager();

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (fm.getBackStackEntryCount() == 0) {
                    infoOpen = false;
                }
                String cnt = "" + fm.getBackStackEntryCount();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplication(), cnt, duration);
                toast.show();
            }
        });

        warningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!warningOpen) {
                    warningFrag = new WarningFrag();

                    /* This is used for debugging, keeping as an example -Connor
                    CharSequence text = "You pressed the text button!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplication(), text, duration);
                    toast.show();
                    */

                    fm.beginTransaction().add(R.id.rl, warningFrag, warningTag)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    warningOpen = true;
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!infoOpen) {
                    infoFrag = new InfoFrag();
                    infoOpen = true;
                    CharSequence text = "You pressed the text button!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplication(), text, duration);
                    toast.show();

                    args.putString("name", bldgNames[bldgSel]);
                    args.putString("description", bldgDesc[bldgSel]);
                    args.putInt("value", bldgSel);
                    infoFrag.setArguments(args);

                    fm.beginTransaction().add(R.id.rl, infoFrag, infoTag)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
            }
        });

        hamBurgMenu = (ImageButton) findViewById(R.id.imageButton);
        hamBurgMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "That button doesn't do anything, yet.", Toast.LENGTH_SHORT).show();
            }
        });

        bldgNames = getResources().getStringArray(R.array.bldg_names);
        bldgDesc = getResources().getStringArray(R.array.bldg_desc);
        bldgXS = getResources().getStringArray(R.array.bldg_x);
        bldgYS = getResources().getStringArray(R.array.bldg_y);
        bldgX = new float[bldgXS.length];
        bldgY = new float[bldgYS.length];

        for (int i = 0; i < bldgXS.length; i++) {
            bldgX[i] = Float.parseFloat(bldgXS[i]);
            bldgY[i] = Float.parseFloat(bldgYS[i]);
        }

        deptNames = getResources().getStringArray(R.array.dept_names);
        deptDesc = getResources().getStringArray(R.array.dept_desc);

        serveNames = getResources().getStringArray(R.array.serve_names);
        serveDesc = getResources().getStringArray(R.array.serve_desc);

        layout = (LinearLayout) findViewById(R.id.layout);

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


        deptListView = new ListView(this);
        deptListView.setBackgroundColor(Color.parseColor("#002c5f"));
        deptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bldgSel = deptBldg[position];
                setContentView(layout);
                textView.setText(deptDesc[position]);
            }
        });

        ArrayList<String> deptList = new ArrayList<String>();
        for (int i = 0; i < deptNames.length; i++) deptList.add(deptNames[i]);
        deptListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deptList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                tv.setTypeface(face);
                return view;
            }
        });

        serveListView = new ListView(this);
        serveListView.setBackgroundColor(Color.parseColor("#002c5f"));
        serveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bldgSel = serveBldg[position];
                setContentView(layout);
                textView.setText(serveDesc[position]);
            }
        });

        ArrayList<String> serveList = new ArrayList<String>();
        for (int i = 0; i < serveNames.length; i++) serveList.add(serveNames[i]);
        serveListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, serveList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                tv.setTypeface(face);
                return view;
            }
        });

        LinearLayout dummy = (LinearLayout) findViewById(R.id.dummyView);
        customView = new CustomView(this);
        customView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        dummy.addView(customView);

        bldgButton = (ImageButton) findViewById(R.id.bldgButton);
        bldgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setContentView(bldgListView);
                Intent i = ServeActivity.newIntent(MainActivity.this);
                startActivityForResult(i, REQUEST_CODE_CALCULATE);
            }
        });

        deptButton = (ImageButton) findViewById(R.id.deptButton);
        deptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setContentView(deptListView);
                Intent i = ServeActivity.newIntent(MainActivity.this);
                startActivityForResult(i, REQUEST_CODE_CALCULATE);
            }
        });

        serveButton = (ImageButton) findViewById(R.id.serveButton);
        //Intent example -Connor
        serveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = ServeActivity.newIntent(MainActivity.this);
                startActivityForResult(i, REQUEST_CODE_CALCULATE);
            }
        });
        
        //Set up to interact with the "Dot" button, but has no content -Connor
        /*
        dotButton = (ImageButton)findViewById(R.id.dotBut);
        dotButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    protected void onDestroy() {
        clearBitmaps(BMArraySize);
        super.onDestroy();
    }

    //No purpose, not sure why it's here -Connor
    private void clearFrags() {
    }

    //Shannon
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
            Log.d("onLocationChanged!!!!", "");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("onStatusChanged!!!!", "");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("onProviderEnabled!!!!!", "");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("onProviderDisabled!!!!!", "");
        }

    };

    private void updateWithNewLocation(Location location) {

        String latLongString = "No location found";
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            latLongString = "Lat: " + lat + "\nLong: " + lng;
        }

        globalLoc = location;

        myLocationText.setText("Your current Position is:\n" + latLongString);
    }

    public void locationFound(Boolean val) {
        locationFound = val;
    }

    public void clearBitmaps(int numOfBitmaps) {
        if (created) {
            for (int j = 0; j < numOfBitmaps; j++) {
                markerArray[j].recycle();
                markerSizeArray[j] = 0;
            }
        }

        bitmap.recycle();
    }

    public void createBitmaps(int numOfBitmaps) {
        if (!created) {
            markerArray = new Bitmap[numOfBitmaps];
            markerSizeArray = new float[numOfBitmaps];

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inScaled = true;

            marker = BitmapFactory.decodeResource(getResources(), R.drawable.ich_sm1, options);
            marker = Bitmap.createScaledBitmap(marker, 50, 50, true);

            for (int j = 0; j < numOfBitmaps; j++) {
                if (grow) {
                    markerSize = markerSize + (float) resizer;
                } else {
                    markerSize = markerSize - (float) resizer;
                }

                if (markerSize > 94) {
                    grow = false;
                }
                if (markerSize < 30) {
                    grow = true;
                }
                markerArray[j] = Bitmap.createScaledBitmap(marker, (int) markerSize, (int) markerSize, true);
                markerSizeArray[j] = markerSize;
            }
            marker.recycle();
        }
        created = true;
    }

    public void setPosition() {
        double latMin = 39.029503;
        double latMax = 39.037157;
        double lngMin = -95.696138;
        double lngMax = -95.707268;

        newXf = 0;
        newYf = 0;

        if (globalLoc != null) {
            double lat = globalLoc.getLatitude();
            double lng = globalLoc.getLongitude();

            if (lat < latMin) {
                outOfBounds = true;
                return;
            }
            if (lat > latMax) {
                outOfBounds = true;
                return;
            }
            if (lng > lngMin) {
                outOfBounds = true;
                return;
            }
            if (lng < lngMax) {
                outOfBounds = true;
                return;
            }
            outOfBounds = false;

            double newX = map(lng, lngMax, lngMin, 0, 1);
            double newY = map(lat, latMax, latMin, 0, 1);

            newXf = (float) newX;
            newYf = (float) newY;
        }
    }

    public double map(double num, double in_min, double in_max, double out_min, double out_max) {
        return (num - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private class CustomView extends ImageView {
        private float xo, yo;
        private boolean moving = false;
        private ScaleGestureDetector scaleDetector;
        private float scale = 1f;
        private float left = 0f, top = 0f;

        private Paint paint1, paint2, paint3, paint4;
        private float width;
        private float height;
        private int w;
        private int h;

        public CustomView(Context c) {
            super(c);
            startLocation();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wumap_photoshoped3);
            setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            scaleDetector = new ScaleGestureDetector(c
                    , new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                public boolean onScale(ScaleGestureDetector s) {
                    moving = false;
                    float oldScale = scale;
                    float width = getWidth();
                    float height = getHeight();
                    xo = left - ((scale - 1f) * width) / 2;
                    yo = top - ((scale - 1f) * height) / 2;
                    scale *= s.getScaleFactor();
                    if (scale < 1f) scale = 1f;
                    left = -((scale / oldScale - 1f) * width) / 2 + left * scale / oldScale;
                    top = -((scale / oldScale - 1f) * height) / 2 + top * scale / oldScale;
                    clampLeftTop();
                    invalidate();
                    return true;
                }
            });
            paint1 = new Paint();
            paint1.setColor(Color.parseColor("#cc2255"));
            paint1.setStrokeWidth(2f);
            paint1.setStyle(Paint.Style.FILL);
            paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            paint2.setTextSize(20f);
            paint3 = new Paint();
            paint3.setColor(Color.parseColor("#002c5f"));
            paint3.setTextSize(20f);
            paint3.setStyle(Paint.Style.FILL);
            paint4 = new Paint();
            paint4.setColor(Color.BLACK);
            paint4.setStrokeWidth(2f);
            paint4.setStyle(Paint.Style.STROKE);
        }


        //Might need to add permission check here -Connor
        private void startLocation() {
            LocationManager locationManager;
            String svcName = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) getSystemService(svcName);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(true);

            String provider = locationManager.getBestProvider(criteria, true);

            if (provider != null) {
                Location l = locationManager.getLastKnownLocation(provider);

                updateWithNewLocation(l);
                locationFound(true);

                locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
            } else {
                Log.d("WE HAVE A NULL!!!!", "");
                locationFound(false);
            }

        }

        public void onDraw(Canvas c) {
            super.onDraw(c);
            if (bitmap == null) return;
            width = getWidth();
            height = getHeight();
            c.translate(left, top);
            c.scale(scale, scale);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            h = (int) (width * bitmapHeight) / bitmapWidth;
            w = (int) (height * bitmapWidth) / bitmapHeight;
            int xoff = 0;
            int yoff = 0;
            float startX;
            float startY;

            if (w >= width) {
                yoff = (int) (height - h) / 2;
                c.drawBitmap(bitmap, null, new Rect(0, yoff, (int) width, h + yoff), null);
            } else {
                xoff = (int) (width - w) / 2;
                c.drawBitmap(bitmap, null, new Rect(xoff, 0, w + xoff, (int) height), null);
            }
            if (bldgSel != -1) {
                if (w >= width) {
                    yoff = (int) (height - h) / 2;
                    startX = bldgX[bldgSel] * width;
                    startY = bldgY[bldgSel] * h + yoff;
                } else {
                    xoff = (int) (width - w) / 2;
                    startX = bldgX[bldgSel] * w + xoff;
                    startY = bldgY[bldgSel] * height;
                }

                c.drawCircle(startX, startY, 8f, paint1);
                c.drawCircle(startX, startY, 8f, paint4);
            }

            if (w >= width) {
                setPosition();
                newXf = newXf * width;
                newYf = newYf * h + yoff;
            } else {
                setPosition();
                newXf = newXf * w + xoff;
                newYf = newYf * height;
            }

            c.save();
            if (!outOfBounds && locationFound) {
                c.drawBitmap(markerArray[BMCtr], newXf - (markerSizeArray[BMCtr] / 2), newYf - (markerSizeArray[BMCtr] / 2), null);
                warningButton.setVisibility(View.GONE);
            } else {
                warningButton.animate().scaleX(0);
                warningButton.setVisibility(View.VISIBLE);
                warningButton.animate().scaleX(1).setDuration(500);
            }

            if (cycleBitmapUp) {
                BMCtr++;
            } else {
                BMCtr--;
            }
            if (BMCtr == BMArraySize - 1) {
                cycleBitmapUp = false;
            }
            if (BMCtr == 0) {
                cycleBitmapUp = true;
            }
            c.restore();

            startOK = true;
            invalidate();
        }

        public boolean onTouchEvent(MotionEvent e) {
            scaleDetector.onTouchEvent(e);
            int action = e.getAction();
            int actionIndex = e.getActionIndex();
            float width = getWidth();
            float height = getHeight();
            if (action == MotionEvent.ACTION_DOWN) {
                if (!moving && actionIndex == 0) {
                    xo = e.getX() - left;
                    yo = e.getY() - top;
                    moving = true;
                    int xoff = 0;
                    int yoff = 0;
                    for (int i = 0; i < bldgX.length; i++) {
                        if (w >= width) {
                            yoff = (int) (height - h) / 2;
                            float x1 = scale * width * bldgX[i];
                            float y1 = scale * (h * bldgY[i] + yoff);
                            if (Math.abs(x1 - xo) < 50 && Math.abs(y1 - yo) < 50) {
                                bldgSel = i;
                                relativeLayout.setLayoutParams(params_rl);
                                textView.setText(bldgDesc[bldgSel]);

                                r = getContext().getResources();
                                px = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        50,
                                        r.getDisplayMetrics()
                                );

                                params.setMargins(0, 0, px, 0);
                                textView.setLayoutParams(params);
                                textView.setEllipsize(TextUtils.TruncateAt.END);
                                textView.setSingleLine(false);
                                textView.setLines(2);
                                textView.setGravity(Gravity.START);
                                infoButton.setVisibility(View.VISIBLE);

                                break;
                            }
                        } else {
                            xoff = (int) (width - w) / 2;
                            float x1 = scale * (w * bldgX[i] + xoff);
                            float y1 = scale * height * bldgY[i];
                            if (Math.abs(x1 - xo) < 50 && Math.abs(y1 - yo) < 50) {
                                bldgSel = i;
                                relativeLayout.setLayoutParams(params_rl);
                                textView.setText(bldgDesc[bldgSel]);

                                r = getContext().getResources();
                                px = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        50,
                                        r.getDisplayMetrics()
                                );

                                params.setMargins(0, 0, px, 0);
                                textView.setLayoutParams(params);
                                textView.setEllipsize(TextUtils.TruncateAt.END);
                                textView.setSingleLine(false);
                                textView.setLines(2);
                                textView.setGravity(Gravity.START);
                                infoButton.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                }
            } else if (action == MotionEvent.ACTION_UP && actionIndex == 0) {
                if (moving && actionIndex == 0) {
                    left = e.getX() - xo;
                    top = e.getY() - yo;
                    clampLeftTop();
                    moving = false;
                }
            }
            if (moving && actionIndex == 0) {
                left = e.getX() - xo;
                top = e.getY() - yo;
                clampLeftTop();
            }
            invalidate();
            return true;
        }

        private void clampLeftTop() {
            float width = getWidth();
            float height = getHeight();
            if (left < (1f - scale) * width) left = (1f - scale) * width;
            if (top < (1f - scale) * height) top = (1f - scale) * height;
            if (left > 0) left = 0;
            if (top > 0) top = 0;
        }
    }
}
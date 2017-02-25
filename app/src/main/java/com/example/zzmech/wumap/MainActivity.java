package com.example.zzmech.wumap;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.graphics.*;
import android.content.*;

import java.util.*;

public class MainActivity extends Activity
{
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
    private int[] deptBldg = {2, 0, 12, 12, 10, 12, 2, 3, 10, 4, 2, 11, 4, 10, 10, 5, 10, 12, 4, 4, 2, 4, 5};
    private String[] serveNames;
    private String[] serveDesc;
    private int[] serveBldg = {10, 10};
    private int bldgSel = -1;
    TextView myLocationText;

    ImageButton hamBurgMenu;

    //private float cvLeft;
    //private float cvRight;
    //private float cvTop;
    //private float cvBottom;
    private ImageButton dotButton;

    //Shannon
    //private Button gpsButton;
    //private GridView gpsView;
    //Animation animScale;
    Location globalLoc;
    //private TextView dbView;
    //ImageView imV;
    float newXf;
    float newYf;
    boolean outOfBounds;
    //float finalXf;
    //float finalYf;
    //int absViewX;
    //int absViewY;

    double resizer = .31;

    //float markerSize = 100;

    //private Bitmap bitmap = null;
    //private Bitmap marker = null;
    //private Bitmap markerScaled = null;
    //private Bitmap markerScaled1 = null;

    //BitmapFactory.Options options = new BitmapFactory.Options();


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mainText = (TextView) findViewById(R.id.main_text);
        Typeface face = Typeface.createFromAsset(getAssets(), "Asimov.otf");
        //mainText.setTypeface(face);

        myLocationText = (TextView) findViewById(R.id.myLocationText);

        textView = (TextView) findViewById(R.id.textView);
        textView.setTypeface(face);

        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wumap_photoshoped3);


            //Log.d("", "" + bitmap.getWidth());
            //BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inMutable = true;
            //options.inScaled = true;

        //marker = BitmapFactory.decodeResource(getResources(), R.drawable.ich_sm1, options);
        //markerScaled = Bitmap.createScaledBitmap(marker, (int) markerSize, (int) markerSize, true);



        hamBurgMenu = (ImageButton) findViewById(R.id.imageButton);
        hamBurgMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "That button doesn't do anything, yet.", Toast.LENGTH_SHORT).show();
            }
        });

        bldgNames = getResources().getStringArray(R.array.bldg_names);
        bldgDesc = getResources().getStringArray(R.array.bldg_desc);
        bldgXS = getResources().getStringArray(R.array.bldg_x);
        bldgYS = getResources().getStringArray(R.array.bldg_y);
        bldgX = new float[bldgXS.length];
        bldgY = new float[bldgYS.length];

        for (int i = 0; i < bldgXS.length; i++)
        {
            bldgX[i] = Float.parseFloat(bldgXS[i]);
            bldgY[i] = Float.parseFloat(bldgYS[i]);
        }

        deptNames = getResources().getStringArray(R.array.dept_names);
        deptDesc = getResources().getStringArray(R.array.dept_desc);
//        deptXVals = getResources().getStringArray(R.array.dept_xVal);
//        deptYVals = getResources().getStringArray(R.array.dept_yVal);
//        deptXVal = new float[deptXVals.length];
//        deptYVal = new float[deptYVals.length];
//
//        for (int i = 0; i < deptYVal.length; i++)
//        {
//            deptXVal[i] = Float.parseFloat(deptXVals[i]);
//            deptYVal[i] = Float.parseFloat(deptYVals[i]);
//        }

        serveNames = getResources().getStringArray(R.array.serve_names);
        serveDesc = getResources().getStringArray(R.array.serve_desc);

        layout = (LinearLayout) findViewById(R.id.layout);

        bldgListView = new ListView(this);
        bldgListView.setBackgroundColor(Color.parseColor("#002c5f"));
        bldgListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                bldgSel = position;
                setContentView(layout);
                textView.setText(bldgDesc[bldgSel]);
            }
        });

        ArrayList<String> bldgList = new ArrayList<String>();
        for (int i = 0; i < bldgNames.length; i++) bldgList.add(bldgNames[i]);
        bldgListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bldgList)
        {
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        });

        deptListView = new ListView(this);
        deptListView.setBackgroundColor(Color.parseColor("#002c5f"));
        deptListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                bldgSel = deptBldg[position];
                setContentView(layout);
                textView.setText(deptDesc[position]);
            }
        });

        ArrayList<String> deptList = new ArrayList<String>();
        for (int i = 0; i < deptNames.length; i++) deptList.add(deptNames[i]);
        deptListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deptList)
        {
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        });

        serveListView = new ListView(this);
        serveListView.setBackgroundColor(Color.parseColor("#002c5f"));
        serveListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                bldgSel = serveBldg[position];
                setContentView(layout);
                textView.setText(serveDesc[position]);
            }
        });

        ArrayList<String> serveList = new ArrayList<String>();
        for (int i = 0; i < serveNames.length; i++) serveList.add(serveNames[i]);
        serveListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, serveList)
        {
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        });

        LinearLayout dummy = (LinearLayout) findViewById(R.id.dummyView);
        //FrameLayout frameDummy=(FrameLayout)findViewById(R.id.frameDummy);
        customView = new CustomView(this);
        customView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        dummy.addView(customView);

        //frameDummy.addView(customView);
        bldgButton = (ImageButton) findViewById(R.id.bldgButton);
        bldgButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                setContentView(bldgListView);
            }
        });

        deptButton = (ImageButton) findViewById(R.id.deptButton);
        deptButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                setContentView(deptListView);
            }
        });

        serveButton = (ImageButton) findViewById(R.id.serveButton);
        serveButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                setContentView(serveListView);
            }
        });

        dotButton = (ImageButton)findViewById(R.id.dotBut);
        dotButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        //textView = (TextView) findViewById(R.id.textView);
        //dbView = (TextView) findViewById(R.id.debug);
    }

    //Shannon
    private final LocationListener locationListener = new LocationListener()
    {

        @Override
        public void onLocationChanged(Location location)
        {
            updateWithNewLocation(location);
            Log.d("onLocationChanged!!!!", "");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.d("onStatusChanged!!!!", "");
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.d("onProviderEnabled!!!!!", "");
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.d("onProviderDisabled!!!!!", "");
        }

    };

    private void updateWithNewLocation(Location location)
    {

        String latLongString = "No location found";
        if (location != null)
        {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            latLongString = "Lat: " + lat + "\nLong: " + lng;
        }

        globalLoc = location;

        myLocationText.setText("Your current Position is:\n" + latLongString);
    }

    public void noLocation()
    {
        TextView myLocationText;
        myLocationText = (TextView) findViewById(R.id.myLocationText);
        String latLongString = "No location found";
        myLocationText.setText(latLongString);
    }

    public void setPosition(){

//        double latMin = 39.03070; //39.03085486396841;
//        double latMax = 39.03093; //39.03087636904064;
//        double lngMin = -95.7531; //-95.75338172265077;
//        double lngMax = -95.7539; //-95.75346807625606;

        // GOOD:
//        double latMin = 39.029330;
//        double latMax = 39.037764;
//        double lngMin = -95.696309;
//        double lngMax = -95.707102;

        //New top left: 39.037157, -95.707268
        //and new bottom right: 39.029503, -95.696138

        double latMin = 39.029503;
        double latMax = 39.037157;
        double lngMin = -95.696138;
        double lngMax = -95.707268;

        newXf = 0;
        newYf = 0;

        if (globalLoc != null)
        {
            double lat = globalLoc.getLatitude();
            double lng = globalLoc.getLongitude();

            if (lat < latMin){
                outOfBounds = true;
                //Log.d("1","!!!!!!!");
                return;
            }
            if (lat > latMax){
                outOfBounds = true;
                //Log.d("2","!!!!!!!");
                return;
            }
            if (lng > lngMin){
                outOfBounds = true;
                //Log.d("3","!!!!!!");
                return;
            }
            if (lng < lngMax){
                outOfBounds = true;
                //Log.d("4","!!!!!!!!");
                return;
            }
            outOfBounds = false;

            double newX = map(lng, lngMax, lngMin, 0, 1);
            double newY = map(lat, latMax, latMin, 0, 1);

            newXf = (float)newX;
            newYf = (float)newY;
        }
    }

    public double map(double num, double in_min, double in_max, double out_min, double out_max)
    {
        return (num - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private class CustomView extends ImageView
    {
        private float xo, yo;
        private boolean moving = false;
        private ScaleGestureDetector scaleDetector;
        private float scale = 1f;
        private float left = 0f, top = 0f;

        private Bitmap bitmap = null;
        private Bitmap marker = null;
        private Bitmap markerScaled = null;
        private Bitmap markerScaled1 = null;

        private Paint paint1, paint2, paint3, paint4;
        private float width;
        private float height;
        private int w;
        private int h;

        //int cTop = 0;
        //int cLeft = 0;

        float markerSize = 100;

        boolean startOK = false;
        boolean grow = true;

        public CustomView(Context c)
        {
            super(c);
            startLocation();
            //worker.start();
            //dotThrob();

            //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wu_map);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wumap_photoshoped3);
//
//            Log.d("", "" + bitmap.getWidth());
//
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inScaled = true;

            marker = BitmapFactory.decodeResource(getResources(), R.drawable.ich_sm1, options);
//
            //Log.d("markerscaled", "Size:   " + markerScaled.getWidth());
            setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            scaleDetector = new ScaleGestureDetector(c
                    , new ScaleGestureDetector.SimpleOnScaleGestureListener()
            {
                public boolean onScale(ScaleGestureDetector s)
                {
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
            paint1.setColor(Color.RED);
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


        private void startLocation()
        {
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

            if (provider != null)
            {
                Location l = locationManager.getLastKnownLocation(provider);

                updateWithNewLocation(l);

                locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
            } else
            {
                Log.d("WE HAVE A NULL!!!!", "");
                noLocation();
            }

        }

        public void onDraw(Canvas c)
        {
            super.onDraw(c);
            if (bitmap == null) return;
            width = getWidth();
            height = getHeight();
            c.translate(left, top);
            c.scale(scale, scale);
            //c.scale(scale+canCtr, scale+canCtr);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            h = (int) (width * bitmapHeight) / bitmapWidth;
            w = (int) (height * bitmapWidth) / bitmapHeight;
            int xoff = 0;
            int yoff = 0;
            float startX;
            float startY;

//            cLeft = c.getClipBounds().left;
//            cTop = c.getClipBounds().top;
//
//            cvLeft = getLeft();
//            cvRight = getLeft() + getWidth();
//            cvTop = getBottom() - getHeight();
//            cvBottom = getBottom();


            if (w >= width)
            {
                yoff = (int) (height - h) / 2;
                c.drawBitmap(bitmap, null, new Rect(0, yoff, (int) width, h + yoff), null);
            } else
            {
                xoff = (int) (width - w) / 2;
                c.drawBitmap(bitmap, null, new Rect(xoff, 0, w + xoff, (int) height), null);
            }
            if (bldgSel != -1)
            {
                if (w >= width)
                {
                    yoff = (int) (height - h) / 2;
                    startX = bldgX[bldgSel] * width;
                    startY = bldgY[bldgSel] * h + yoff;
                } else
                {
                    xoff = (int) (width - w) / 2;
                    startX = bldgX[bldgSel] * w + xoff;
                    startY = bldgY[bldgSel] * height;
                }

                c.drawCircle(startX, startY, 8f, paint1);
                c.drawCircle(startX, startY, 8f, paint4);
//        dotThrob(startX*2, startY*2);
//        float tw=paint2.measureText(bldgNames[bldgSel]);
//        c.drawRect(startX-tw/2-3,startY+20,startX+tw/2+3,startY+46,paint3);
//        c.drawText(bldgNames[bldgSel],startX-tw/2,startY+40,paint2);
//        c.drawRect(startX-tw/2-3,startY+20,startX+tw/2+3,startY+46,paint4);
            }

            if (w >= width)
            {
                setPosition();
                newXf = newXf * width;
                newYf = newYf * h + yoff;
            } else
            {
                setPosition();
                newXf = newXf * w + xoff;
                newYf = newYf * height;
            }

            c.save();
            if(grow){
                markerSize = markerSize + (float).34;}
            else{
                markerSize = markerSize - (float).34;}

            if(markerSize > 70){
                grow = false;
            }
            if(markerSize < 30){
                grow = true;
            }

            //markerScaled1 = Bitmap.createScaledBitmap(marker, 15, 15, true);

            markerScaled = Bitmap.createScaledBitmap(marker, (int) markerSize, (int) markerSize, true);

            //markerScaled.setWidth((int) markerSize);
            //markerScaled.setHeight((int) markerSize);

            //markerScaled.setDensity(1020);
            c.drawBitmap(markerScaled, newXf - (markerSize / 2), newYf - (markerSize/2), null);
            //c.drawBitmap(markerScaled1, (float)200, (float)200, null);

            //markerScaled.recycle();
            //markerScaled.
            c.restore();

            startOK = true;

            // dbView.setText("canCtr: " + canCtr + " OOB: " + outOfBounds + "  cLeft :" + cLeft + "  cTop:  " + cTop
            //+ " newXf: " + newXf + " newYf: " + newYf
            //+ " width: " + width + " height: " + height + " w: " + w + " h: " + h);

            invalidate();
            //System.gc();

        }

        public boolean onTouchEvent(MotionEvent e)
        {
            scaleDetector.onTouchEvent(e);
            int action = e.getAction();
            int actionIndex = e.getActionIndex();
            float width = getWidth();
            float height = getHeight();
            if (action == MotionEvent.ACTION_DOWN)
            {
                if (!moving && actionIndex == 0)
                {
                    xo = e.getX() - left;
                    yo = e.getY() - top;
                    moving = true;
                    //          textView.setText("");
                    //          bldgSel=-1;
                    int xoff = 0;
                    int yoff = 0;
                    for (int i = 0; i < bldgX.length; i++)
                    {
                        if (w >= width)
                        {
                            yoff = (int) (height - h) / 2;
                            float x1 = scale * width * bldgX[i];
                            float y1 = scale * (h * bldgY[i] + yoff);
                            if (Math.abs(x1 - xo) < 50 && Math.abs(y1 - yo) < 50)
                            {
                                bldgSel = i;
                                textView.setText(bldgDesc[bldgSel]);
                                break;
                            }
                        } else
                        {
                            xoff = (int) (width - w) / 2;
                            float x1 = scale * (w * bldgX[i] + xoff);
                            float y1 = scale * height * bldgY[i];
                            if (Math.abs(x1 - xo) < 50 && Math.abs(y1 - yo) < 50)
                            {
                                bldgSel = i;
                                textView.setText(bldgDesc[bldgSel]);
                                break;
                            }
                        }
                    }
                }
            } else if (action == MotionEvent.ACTION_UP && actionIndex == 0)
            {
                if (moving && actionIndex == 0)
                {
                    left = e.getX() - xo;
                    top = e.getY() - yo;
                    clampLeftTop();
                    moving = false;
                }
            }
            if (moving && actionIndex == 0)
            {
                left = e.getX() - xo;
                top = e.getY() - yo;
                clampLeftTop();
            }
            invalidate();
            return true;
        }

        private void clampLeftTop()
        {
            float width = getWidth();
            float height = getHeight();
            if (left < (1f - scale) * width) left = (1f - scale) * width;
            if (top < (1f - scale) * height) top = (1f - scale) * height;
            if (left > 0) left = 0;
            if (top > 0) top = 0;
        }
    }
}
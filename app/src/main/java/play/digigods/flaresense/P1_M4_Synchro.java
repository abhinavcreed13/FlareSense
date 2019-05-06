package play.digigods.flaresense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import play.digigods.flaresense.Utilities.JSONParser;


public class P1_M4_Synchro extends Activity {

    //GCM VARIABLES
    private static final String GCM_SENDER_ID = "356591684101";
    private static final int ACTION_PLAY_SERVICES_DIALOG = 99;
    GoogleCloudMessaging gcm;
    private String gcmRegId;

    //HANDLER VARIABLES
    private static final int GET_REGID = 100;
    protected static final int INSERT_DATA = 101;
    protected static final int CREATE_CIRCLES = 102;
    protected static final int CREATE_NOTIFICATIONS = 103;
    protected static final int CREATE_PROFILE = 104;
    protected static final int CREATE_CUSTOMLOCS = 105;
    protected static final int CREATE_SOS = 106;
    JSONParser jsonParser = new JSONParser();
    JSONArray products = null;
    private static final String CREATE_FSUSER_URL = "http://play.digigods.in/flaresense/Master/create_FS_user.php";
    AlertDialog.Builder netbuilder;

    //USER VARIABLES
    String MyFID,MyName,MyEmailID,MyProfilepic,MyREGID,MyDoomsDay,MyAccess;

    //XML VARIABLES
    ImageView imageprofile;
    TextView welcome,welcome2,setup;
    ProgressBar progress,progress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.m4_test);

        imageprofile=(ImageView)findViewById(R.id.profile);
        welcome=(TextView)findViewById(R.id.welcome);
        welcome2=(TextView)findViewById(R.id.welcome_2);
        setup=(TextView)findViewById(R.id.settingup);
        progress=(ProgressBar)findViewById(R.id.setupprogress);

        //RECIEVED FROM MODULE 3
        Intent data=getIntent();
        MyFID=data.getStringExtra("FID");
        MyFID=MyFID.toUpperCase();
        MyName=data.getStringExtra("NAME");
        MyEmailID=data.getStringExtra("EMAILID");
        MyProfilepic=data.getStringExtra("PROFILEPIC");
        MyAccess=data.getStringExtra("ACCOUNT");
        Log.d("CREED", MyFID + "," + MyName + "," + MyEmailID + "," + MyProfilepic);

        //SETTING IMAGE IN BETWEEN
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        new LoadProfileImagePROFILE(imageprofile).execute(MyProfilepic);

        int density= getResources().getDisplayMetrics().densityDpi;

        float pixels_profile=0;

        //YET TO BE RESOLVED...
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(this, "LDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(this, "MDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(this, "HDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                pixels_profile = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());
                Toast.makeText(this, "XHDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                pixels_profile = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
                Toast.makeText(this, "XXHDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                pixels_profile = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
                Toast.makeText(this, "XXXHDPI", Toast.LENGTH_SHORT).show();
                break;
        }

        //TICKET NOT YET RESOLVED...
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)pixels_profile,(int)pixels_profile);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.setMargins(0, (int) (0.23 * height), 0, 0);
        imageprofile.setLayoutParams(lp);

        Typeface typeFace2;
        typeFace2 = Typeface.createFromAsset(getAssets(), "fonts/fonthead.ttf");
        welcome2.setText("Welcome");
        welcome2.setTypeface(typeFace2);
        welcome2.setTextSize((float) 0.02 * height);
        welcome2.setGravity(Gravity.CENTER);

        Typeface typeFace;
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/fontbody.ttf");
        welcome.setText(MyName);
        welcome.setTypeface(typeFace);
        welcome.setTextSize((float) 0.02 * height);
        welcome.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp3.setMargins(0, (int) (0.46 * height), 0, 0);
        welcome2.setLayoutParams(lp3);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp2.setMargins(0, (int) ((0.46 * height) + (0.04 * height)), 0, 0);
        welcome.setLayoutParams(lp2);

        setup.setText("Setting up your FlareSense");
        setup.setTypeface(typeFace2);
        setup.setTextSize((float) 0.015 * height);
        setup.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp4.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp4.setMargins(0, (int) (0.80 * height), 0, 0);
        setup.setLayoutParams(lp4);


        RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp5.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp5.setMargins(0, (int) (0.72 * height), 0, 0);
        progress.setLayoutParams(lp5);

        handler.sendEmptyMessage(GET_REGID); //ROUTE STARTS HERE

    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GET_REGID:
                    new GCMRegistrationTask().execute();
                    break;
                case INSERT_DATA:
                    //CREATING DOOMSDAY DATE
                    /*
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.YEAR,1);
                    SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                    String formattedDate = df.format(c.getTime());
                    MyDoomsDay=formattedDate;
                    */
                    MyDoomsDay="DIGIDOOM";
                    Log.d("CREED",MyREGID+","+MyDoomsDay);
                    new CreateNewUser().execute();
                    break;
                case CREATE_CIRCLES:
                    Toast.makeText(getApplicationContext(),"MODULE 4 DONE!! PHASE 1 of PHASE 4+ completed here!!",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    //GCM GETTING REGID
    private boolean isGooglePlayInstalled() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        ACTION_PLAY_SERVICES_DIALOG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Google Play Service is not installed",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private class GCMRegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if (gcm == null && isGooglePlayInstalled()) {
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            }
            try {
                gcmRegId = gcm.register(GCM_SENDER_ID);
                MyREGID=gcmRegId;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return gcmRegId;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(getApplicationContext(), "registered with GCM",
                        Toast.LENGTH_LONG).show();
                handler.sendEmptyMessage(INSERT_DATA);
            }
        }

    }

    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        int success=37;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("fid",MyFID));
                    params.add(new BasicNameValuePair("name", MyName));
                    params.add(new BasicNameValuePair("emailID", MyEmailID));
                    params.add(new BasicNameValuePair("profilepic", MyProfilepic));
                    params.add(new BasicNameValuePair("REGID", MyREGID));
                    params.add(new BasicNameValuePair("DOOMSDAY", MyDoomsDay));
                    params.add(new BasicNameValuePair("Account",MyAccess));

                    // getting JSON Object
                    // Note that create product url accepts POST method
                    JSONObject json = jsonParser.makeHttpRequest(CREATE_FSUSER_URL,
                            "POST", params);
                    // check log cat fro response
                    Log.d("Create Response", json.toString());
                    // check for success tag
                    try {
                        success = json.getInt("success");
                        } catch (JSONException e) {
                        e.printStackTrace();
                    }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            if(success==1)
            {
                handler.sendEmptyMessage(CREATE_CIRCLES);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_p1_m4_synchro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadProfileImagePROFILE extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImagePROFILE(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());    //ERROR -> println() needs a message
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    class RoundedImageViewPROFILE {

        public Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) {
            // TODO Auto-generated method stub
            int targetWidth = width;
            int targetHeight = width;
            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                    targetHeight,Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),
                            ((float) targetHeight)) / 2),
                    Path.Direction.CCW);
            canvas.clipPath(path);
            Bitmap sourceBitmap = scaleBitmapImage;
            canvas.drawBitmap(sourceBitmap,
                    new Rect(0, 0, sourceBitmap.getWidth(),
                            sourceBitmap.getHeight()),
                    new Rect(0, 0, targetWidth,
                            targetHeight), null);
            return targetBitmap;
        }
    }
}

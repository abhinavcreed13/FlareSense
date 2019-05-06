package play.digigods.flaresense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.digigods.flaresense.Utilities.JSONParser;


public class P1_M2M3_Java extends Activity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //OTHER VARIABLES
    TextView Name, Email, Title, Subtitle,Checking;
    ImageView icon,available,lock;
    Button ButtonTitle;
    private EditText FSPIN;
    String SYS_name,SYS_emailID,SYS_profilepic;
    String SYS_FID,SYS_Account;
    private int bypassvalue=67;
    private ProgressBar loading,loadingavail;

    //VARIABLES - GOOGLE+ SIGN IN
    private static final int RC_SIGN_IN = 0;
    public GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;

    //FACEBOOK LOGIN VARIABLES
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    LoginManager loginManager;
    AccessTokenTracker accessTokenTracker;
    ProfilePictureView profilePictureView;

    //HANDLER VARIABLES
    private static final int CHECKING_FSPIN_AVAIL = 100;
    protected static final int REDIRECTING_RESULTS_AVAIL = 101;
    protected static final int REDIRECTING_RESULTS_NOAVAIL = 102;
    JSONParser jsonParser = new JSONParser();
    JSONArray products = null;
    private static final String CHECK_FSPIN_URL = "http://play.digigods.in/flaresense/Master/check_FSPIN.php";
    public CHECKFID checkfid;
    AlertDialog.Builder netbuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.p1_m2_m3_java);

        Name=(TextView)findViewById(R.id.text_name);
        Email=(TextView)findViewById(R.id.text_email);
        Title=(TextView)findViewById(R.id.text_page);
        Subtitle=(TextView)findViewById(R.id.text_page2);
        Checking=(TextView)findViewById(R.id.text_checking);
        ButtonTitle=(Button)findViewById(R.id.nextbutton);
        lock=(ImageView)findViewById(R.id.lock);
        loading=(ProgressBar)findViewById(R.id.login_loading);
        loadingavail=(ProgressBar)findViewById(R.id.loading_avail);

        icon=(ImageView)findViewById(R.id.icon);

        available=(ImageView)findViewById(R.id.available);
        FSPIN=(EditText)findViewById(R.id.editText_FSPIN);

        Typeface typeFace;
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/fonthead.ttf");
        Typeface typeFace2;
        typeFace2 = Typeface.createFromAsset(getAssets(), "fonts/fontbody.ttf");

        Name.setTypeface(typeFace);
        Email.setTypeface(typeFace2);
        Title.setTypeface(typeFace);
        Subtitle.setTypeface(typeFace2);
        ButtonTitle.setTypeface(typeFace2);
        FSPIN.setTypeface(typeFace);
        Checking.setTypeface(typeFace);

        FSPIN.setEnabled(false);

        ButtonTitle.setOnClickListener(this);

        //GOOGLE+
        btnSignIn = (SignInButton) findViewById(R.id.google_login);
        btnSignIn.setSize(1);
        // Button click listeners
        btnSignIn.setOnClickListener(this);
        setGooglePlusButtonText(btnSignIn, "Register Using Google+");
        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE).addScope(Plus.SCOPE_PLUS_LOGIN).build();


        //FACEBOOK
        loginButton=(LoginButton)findViewById(R.id.FB_login);
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email,public_profile,user_friends"));

        /*accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            Log.d("VIEW","Already Logged in!");
            updateWithToken(AccessToken.getCurrentAccessToken());
        }*/
        // Other app specific specialization
        // Callback registration
        final AlertDialog.Builder builderFB = new AlertDialog.Builder(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loading.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"FB Logged in successful!",Toast.LENGTH_SHORT).show();
                SYS_Account="FACEBOOK";
                accessToken=loginResult.getAccessToken();
                getProfileInformation_fb();
            }
            @Override
            public void onCancel() {
                // App code
                builderFB.setMessage("Oops! No Internet Connection Available.\n Please Check your Network and Try Again!")
                        .setCancelable(false)
                        .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builderFB.create();
                alert.show();
            }
            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"error!",Toast.LENGTH_SHORT).show();
            }
        });

        //EDITTEXT FILTERS
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter filter2 = new InputFilter.LengthFilter(7);
        FSPIN.setFilters(new InputFilter[] { filter,filter2 });
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                if(s.length() == 7) {
                    loadingavail.setVisibility(View.VISIBLE);
                    Checking.setVisibility(View.VISIBLE);
                    View view = getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    SYS_FID=FSPIN.getText().toString();
                    handler.sendEmptyMessage(CHECKING_FSPIN_AVAIL);
                }
                else
                {
                    if(checkfid!=null) {
                        if (checkfid.getStatus() == AsyncTask.Status.RUNNING) {
                            checkfid.cancel(true);
                        }
                    }
                    if(loadingavail.getVisibility()==View.VISIBLE)
                        loadingavail.setVisibility(View.INVISIBLE);
                    if(Checking.getVisibility()==View.VISIBLE)
                        Checking.setVisibility(View.INVISIBLE);
                    if(available.getVisibility()==View.VISIBLE)
                        available.setVisibility(View.INVISIBLE);
                }
            }
            public void afterTextChanged(Editable s) {
            }
        };
        FSPIN.addTextChangedListener(mTextEditorWatcher);
        netbuilder=new AlertDialog.Builder(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login:
                if (isNetworkAvailable()) {
                    // Signin button clicked
                    loading.setVisibility(View.VISIBLE);
                    signInWithGplus();
                } else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Oops! No Internet Connection Available.\n Please Check your Network and Try Again!")
                            .setCancelable(false)
                            .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                    break;
            case R.id.nextbutton:
                Intent i = new Intent(getApplicationContext(), P1_M4_Synchro.class);
                i.putExtra("FID",SYS_FID);
                i.putExtra("NAME",SYS_name);
                i.putExtra("PROFILEPIC",SYS_profilepic);
                i.putExtra("EMAILID",SYS_emailID);
                i.putExtra("ACCOUNT",SYS_Account);
                startActivity(i);
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CHECKING_FSPIN_AVAIL:
                    checkfid=new CHECKFID();
                    if(isNetworkAvailable()) {
                        checkfid.execute();
                    }
                    else
                    {
                        netbuilder.setMessage("Oops! No Internet Connection Available.\n Please Check your Network and Try Again!")
                                .setCancelable(false)
                                .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        loadingavail.setVisibility(View.INVISIBLE);
                                        Checking.setVisibility(View.INVISIBLE);
                                        FSPIN.setText("");
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = netbuilder.create();
                        alert.show();
                    }
                        break;
                case REDIRECTING_RESULTS_AVAIL:
                    loadingavail.setVisibility(View.INVISIBLE);
                    Checking.setVisibility(View.INVISIBLE);
                    available.setImageResource(R.drawable.available);
                    available.setVisibility(View.VISIBLE);
                    break;
                case REDIRECTING_RESULTS_NOAVAIL:
                    loadingavail.setVisibility(View.INVISIBLE);
                    Checking.setVisibility(View.INVISIBLE);
                    available.setImageResource(R.drawable.unavailable);
                    available.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    //HANDLER CLASSES
    class CHECKFID extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        int success=37;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
                    // Building Parameters
                    List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                    // getting JSON string from URL
                    Log.d("CREED",SYS_FID);
                    params2.add(new BasicNameValuePair("fid",SYS_FID));
                    JSONObject json = jsonParser.makeHttpRequest(CHECK_FSPIN_URL, "POST", params2);
                    //Check your log cat for JSON reponse
                    Log.d("RESPONSE:", json.toString());
                    try {
                        // Checking for SUCCESS TAG

                        success = json.getInt("success");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            if (success == 1) {
                handler.sendEmptyMessage(REDIRECTING_RESULTS_AVAIL);
            }
            else if(success==0) {
                handler.sendEmptyMessage(REDIRECTING_RESULTS_NOAVAIL);
            }
        }
    }

    //GOOGLE+ API FUNCTIONS
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    protected void onStop()
    {
        super.onStop();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }
    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        SYS_Account="GOOGLEPLUS";
        // Get user's information
        getProfileInformation();
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                SYS_name=personName;
                SYS_emailID=email;
                SYS_profilepic=personPhotoUrl;
                //PERFORMING REQUIRED TASKS
                btnSignIn.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
                Name.setVisibility(View.VISIBLE);
                Email.setVisibility(View.VISIBLE);
                Name.setText(SYS_name);
                Email.setText(SYS_emailID);
                SYS_profilepic = SYS_profilepic.substring(0,
                        SYS_profilepic.length() - 2)
                        + 400;

                RoundedImageViewPROFILE imgi=new RoundedImageViewPROFILE();
                Bitmap pro = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
                icon.setImageBitmap(imgi.getRoundedShape(pro,200));

                new LoadProfileImagePROFILE(icon).execute(SYS_profilepic); //NO FB

                lock.setVisibility(View.INVISIBLE);
                ButtonTitle.setVisibility(View.VISIBLE);
                FSPIN.setEnabled(true);
                available.setVisibility(View.INVISIBLE);

                 } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();

        }
    }
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e("G+", "User access revoked!");
                            mGoogleApiClient.connect();
                        }

                    });
        }
    }

    //FACEBOOK API
    private void updateWithToken(final AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    accessToken = currentAccessToken;
                    //getProfileInformation_fb();
                    finish();
                }
            }, 100);
        } else {
            //DO NOTHING!! STAY ON LOGIN SCREEN!!
        }
    }
    public void getProfileInformation_fb() {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String id = object.getString("id");
                            String email = object.getString("email");
                            String name = object.getString("name");
                            URL img_value = null;
                            img_value = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                            SYS_name=name;
                            SYS_emailID=email;
                            Log.d("CREED", "" + id);
                            //PERFORMING REQUIRED ACTIONS
                            SYS_profilepic=img_value.toString();
                            btnSignIn.setVisibility(View.INVISIBLE);
                            loginButton.setVisibility(View.INVISIBLE);
                            Name.setVisibility(View.VISIBLE);
                            Email.setVisibility(View.VISIBLE);
                            Name.setText(SYS_name);
                            Email.setText(SYS_emailID);

                            //BACKUP CODE
                            /*InputStream in = (InputStream) img_value.getContent();
                            Bitmap  bitmap = BitmapFactory.decodeStream(in);
                            icon.setImageBitmap(bitmap);*/

                            RoundedImageViewPROFILE imgi=new RoundedImageViewPROFILE();
                            Bitmap pro = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
                            icon.setImageBitmap(imgi.getRoundedShape(pro, 200));

                            new LoadProfileImagePROFILE(icon).execute(SYS_profilepic); //NO FB

                            lock.setVisibility(View.INVISIBLE);
                            ButtonTitle.setVisibility(View.VISIBLE);
                            FSPIN.setEnabled(true);
                            available.setVisibility(View.INVISIBLE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAndWait();

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            //FB
            super.onActivityResult(requestCode, responseCode, intent);
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_p1_m2m3, menu);
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

    //LOADING PROFILE IMAGES...
    //FASTER AND BETTER..
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
            RoundedImageViewPROFILE imgi=new RoundedImageViewPROFILE();
            bmImage.setImageBitmap(imgi.getRoundedShape(result, 200));
            loading.setVisibility(View.INVISIBLE);
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


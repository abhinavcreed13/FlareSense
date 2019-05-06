package play.digigods.flaresense;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import play.digigods.flaresense.P1_Mod1_Intro.P1_FragmentPagerAdapter;
import play.digigods.flaresense.Utilities.GraphicConversion;


public class P1_M1_intro extends FragmentActivity {

    private SharedPreferences prefs;
    private ViewPager viewPager;
    private P1_FragmentPagerAdapter mAdapter;
    private Button enterflare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intropager);

        Typeface typeFace;
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/fonthead.ttf");

        enterflare=(Button) findViewById(R.id.enterflare);
        enterflare.setTypeface(typeFace);
        enterflare.setVisibility(View.INVISIBLE);

        final Animation fadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mainfadein);
        final Animation fadeout=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mainfadeout);

        viewPager = (ViewPager) findViewById(R.id.intro_pager);
        FragmentManager fm = this.getSupportFragmentManager();
        mAdapter = new P1_FragmentPagerAdapter(fm);
        viewPager.setAdapter(mAdapter);
        viewPager.setVisibility(View.INVISIBLE);

        //MICHEAL BAY - AMAZING
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                // Ensures the views overlap each other.
                view.setTranslationX(view.getWidth() * -position);
                // Alpha property is based on the view position.
                if (position <= -1.0F || position >= 1.0F) {
                    view.setAlpha(0.0F);
                } else if (position == 0.0F) {
                    view.setAlpha(1.0F);
                } else { // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                    view.setAlpha(1.0F - Math.abs(position));
                }
                // TextView transformation
                view.findViewById(R.id.text_page).setTranslationX(view.getWidth() * position);
            }
        });

        //GETTING DIMENSIONS
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //STORING IN SHARED PREFERENCES - ALWAYS
        saveInSharedPref("FS_screenwidth", Integer.toString(width));
        saveInSharedPref("FS_screenheight", Integer.toString(height));

        float margintop=(float)0.3*height; //LOGIC

        new Handler().postDelayed(new Runnable() {

            public void run() {

                overridePendingTransition(R.anim.scale, R.anim.splashfadeout);
            }
        }, 2000);

        Animation am = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);

        //SETTING MARGIN TOP OF IMAGEVIEW
        ImageView i = (ImageView)findViewById(R.id.icon);
        ImageView i2 = (ImageView) findViewById(R.id.icon2);
        TextView text=(TextView)findViewById(R.id.text1);
        TextView text2=(TextView) findViewById(R.id.text2);

        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105, getResources().getDisplayMetrics());
        float adjust = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());

        float dp_margintop= GraphicConversion.convertPixelsToDp(margintop, getApplicationContext()); //FOR FINAL IMAGEVIEW
        float dp_margintop_text=dp_margintop+adjust; //MARGINTOP FOR FINAL TEXT

        //SETTING IMAGVIEW NEW MARGINS
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)pixels,(int)pixels);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.setMargins(0, (int) dp_margintop, 0, 0);
        i2.setLayoutParams(lp);

        //SETTING TEXTVIEW NEW MARGINS
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp2.setMargins(0, (int) dp_margintop_text, 0, 0);
        text2.setLayoutParams(lp2);


        text.setTypeface(typeFace);
        text2.setTypeface(typeFace);

        //SETTING ANIMATION OF IMAGEVIEW
        i.setAnimation(am);
        text.setAnimation(am);


        //ANIMATION LISTENER
        am.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                TextView text = (TextView) findViewById(R.id.text1);
                text.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mainfadeout));
                text.setVisibility(View.INVISIBLE);
                ImageView i = (ImageView) findViewById(R.id.icon);
                i.setAnimation(fadeout);
            }
        });

        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

                ImageView i = (ImageView) findViewById(R.id.icon);
                i.setVisibility(View.INVISIBLE);
                ImageView i2 = (ImageView) findViewById(R.id.icon2);
                i2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeinfast));
                i2.setVisibility(View.VISIBLE);
                TextView text2 = (TextView) findViewById(R.id.text2);
                text2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeinfast));
                text2.setVisibility(View.VISIBLE);
                viewPager.setAnimation(fadein);
                viewPager.setVisibility(View.VISIBLE);
               // enterflare.setAnimation(fadein);
               // enterflare.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {

            }
        });


    }

    private SharedPreferences getSharedPreferences() {
        if (prefs == null) {
            prefs = getApplicationContext().getSharedPreferences(
                    "FlareSense", Context.MODE_PRIVATE);
        }
        return prefs;
    }

    public void saveInSharedPref(String result,String Type) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(Type, result);
        editor.commit();
    }
}

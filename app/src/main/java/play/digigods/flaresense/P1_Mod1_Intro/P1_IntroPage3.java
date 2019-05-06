package play.digigods.flaresense.P1_Mod1_Intro;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import play.digigods.flaresense.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link P1_IntroPage3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class P1_IntroPage3 extends Fragment {
    private TextView FeatureText, OtherText;
    private ImageView arrowright,arrowrighthover,arrowleft,arrowlefthover;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment P1_IntroPage3.
     */
    // TODO: Rename and change types and number of parameters
    public static P1_IntroPage3 newInstance(String param1, String param2) {
        P1_IntroPage3 fragment = new P1_IntroPage3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public P1_IntroPage3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.p1_intro_page3, container, false);
        Typeface typeFace;
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fonthead.ttf");

        FeatureText=(TextView)rootview.findViewById(R.id.text_page);
        FeatureText.setTypeface(typeFace);

        Typeface typeFace2;
        typeFace2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontbody.ttf");

        OtherText=(TextView)rootview.findViewById(R.id.text_page2);
        OtherText.setTypeface(typeFace2);

        arrowright=(ImageView)rootview.findViewById(R.id.image_arrow3);
        arrowrighthover=(ImageView)rootview.findViewById(R.id.image_arrow_hover3);
        arrowleft=(ImageView)rootview.findViewById(R.id.image_arrow_left3);
        arrowlefthover=(ImageView)rootview.findViewById(R.id.image_arrow_left_hover3);

        final Animation fadeinright = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fadeinrepeat);
        final Animation fadeinright2 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fadeinrepeat);

        final Animation fadeinleft = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fadeinrepeat2);
        final Animation fadeinleft2 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fadeinrepeat2);

        arrowright.setAnimation(fadeinright);


        fadeinright.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                arrowright.setVisibility(View.INVISIBLE);
                arrowrighthover.startAnimation(fadeinright2);
                arrowrighthover.setVisibility(View.VISIBLE);
            }
        });

        fadeinright2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                arrowrighthover.setVisibility(View.INVISIBLE);
                arrowright.startAnimation(fadeinright);
                arrowright.setVisibility(View.VISIBLE);
            }
        });

        arrowleft.setAnimation(fadeinleft);

        fadeinleft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                arrowleft.setVisibility(View.INVISIBLE);
                arrowlefthover.startAnimation(fadeinleft2);
                arrowlefthover.setVisibility(View.VISIBLE);
            }
        });

        fadeinleft2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                arrowlefthover.setVisibility(View.INVISIBLE);
                arrowleft.startAnimation(fadeinleft);
                arrowleft.setVisibility(View.VISIBLE);
            }
        });

        return rootview;
    }


}

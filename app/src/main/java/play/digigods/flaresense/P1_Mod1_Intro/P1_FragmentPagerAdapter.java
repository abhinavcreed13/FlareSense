package play.digigods.flaresense.P1_Mod1_Intro;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by abhin_000 on 8/10/2015.
 */
public class P1_FragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;

    // Tab Titles
    private String tabtitles[] = new String[] { "Feature 1", "Feature 2", "Feature 3", "Feature 4"};
    Context context;

    public P1_FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                P1_IntroPage1 fragmenttab1 = new P1_IntroPage1();
                return fragmenttab1;

            // Open FragmentTab2.java
            case 1:
                P1_IntroPage2 fragmenttab2 = new P1_IntroPage2();
                return fragmenttab2;

            // Open FragmentTab3.java
            case 2:
                P1_IntroPage3 fragmenttab3 = new P1_IntroPage3();
                return fragmenttab3;

            case 3:
                P1_IntroPage4 fragmenttab4 = new P1_IntroPage4();
                return fragmenttab4;

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}

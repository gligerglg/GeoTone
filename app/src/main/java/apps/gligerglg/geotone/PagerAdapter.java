package apps.gligerglg.geotone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Gayan Lakshitha on 3/3/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private String[] tabTitles = new String[]{"Places","Tasks"};

    public PagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                Places_Fragment places_fragment = new Places_Fragment();
                return places_fragment;
            case 1:
                Task_Fragment task_fragment = new Task_Fragment();
                return task_fragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

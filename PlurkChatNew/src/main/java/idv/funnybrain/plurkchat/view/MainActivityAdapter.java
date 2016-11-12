package idv.funnybrain.plurkchat.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainActivityAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "MainActivityAdapter";

    private String[] titles;

    public MainActivityAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Me, Friend, Following.
                return MeFriendsFollowingFragment.newInstance();
        }
        return MeFriendsFollowingFragment.newInstance();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

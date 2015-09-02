package in.sadrudd.contactanalyser.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by sjunjo on 23/08/15.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    public static int pos = 0;

    private List<Fragment> fragments;

    public FragmentPagerAdapter(FragmentManager fm, List<Fragment> frags){
        super(fm);
        this.fragments = frags;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public static int getPos(){
        return pos;
    }

    public static void setPos(int pos){
        FragmentPagerAdapter.pos = pos;
    }

}

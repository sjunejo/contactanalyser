package in.sadrudd.contactanalyser.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by sjunjo on 23/08/15.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

    public static int pos = 0;

    private List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, List<Fragment> frags){
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
        FragmentAdapter.pos = pos;
    }

}
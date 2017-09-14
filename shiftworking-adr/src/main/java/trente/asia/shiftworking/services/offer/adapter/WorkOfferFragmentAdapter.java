package trente.asia.shiftworking.services.offer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by tien on 9/14/2017.
 */

public class WorkOfferFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public WorkOfferFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

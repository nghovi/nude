package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDay;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;

import static trente.asia.calendar.services.calendar.model.CalendarDay.*;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends AbstractClFragment{

	private ViewPager	mViewPager;
	private ScreenSlidePagerAdapter mPagerAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_weekly_pager, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, "Weekly", null);
		mViewPager = (ViewPager)getView().findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
	}

	@Override
	protected void initData(){
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
	 * sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

		public ScreenSlidePagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int position){
			return new WeeklyPageFragment();
		}

		@Override
		public int getCount(){
			return 5;
		}
	}
}

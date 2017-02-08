package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarPagerAdapter;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class MonthlyFragment extends AbstractClFragment{

	private ImageView			mImgLeftHeader;
	private WfSlideMenuLayout	mSlideMenuLayout;

	private ViewPager			viewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		mImgLeftHeader = (ImageView)getView().findViewById(R.id.img_id_header_left_icon);
		viewPager = (ViewPager)getView().findViewById(R.id.view_pager_id_calendar);
		MonthlyCalendarPagerAdapter pagerAdapter = new MonthlyCalendarPagerAdapter(activity);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2);

		mSlideMenuLayout = (WfSlideMenuLayout)getView().findViewById(R.id.drawer_layout);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		CalendarListFragment calendarListFragment = new CalendarListFragment();
		transaction.replace(R.id.slice_menu_board, calendarListFragment).commit();

		mImgLeftHeader.setOnClickListener(this);

        Calendar calendar = CCDateUtil.makeCalendar(CsDateUtil.makeMonthWithFirstDate());
        List<Date> lstDate = CsDateUtil.getAllDate4Month(calendar);
	}

	@Override
	protected void initData(){
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_left_icon:
			mSlideMenuLayout.toggleMenu();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}

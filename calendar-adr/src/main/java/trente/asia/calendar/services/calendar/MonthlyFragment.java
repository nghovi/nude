package trente.asia.calendar.services.calendar;

import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class MonthlyFragment extends AbstractClFragment{

	private ImageView					mImgLeftHeader;
	private WfSlideMenuLayout			mSlideMenuLayout;

	private ViewPager					viewPager;
	private TextView					txtMonth;
	private MonthlyCalendarPagerAdapter	pagerAdapter;

	private final Date					TODAY		= CsDateUtil.makeMonthWithFirstDate();
	private final int					ACTIVE_PAGE	= Integer.MAX_VALUE / 2;

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
		txtMonth = (TextView)getView().findViewById(R.id.txt_id_month);

		pagerAdapter = new MonthlyCalendarPagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(ACTIVE_PAGE);
		setActiveMonth(ACTIVE_PAGE);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int state){
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
			}

			public void onPageSelected(int position){
				setActiveMonth(position);
//                load schedule list
				MonthlyPageFragment fragment = (MonthlyPageFragment)pagerAdapter.getItem(position);
				fragment.loadScheduleList();
			}
		});

		mSlideMenuLayout = (WfSlideMenuLayout)getView().findViewById(R.id.drawer_layout);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		CalendarListFragment calendarListFragment = new CalendarListFragment();
		transaction.replace(R.id.slice_menu_board, calendarListFragment).commit();

		mImgLeftHeader.setOnClickListener(this);
	}

	@Override
	protected void initData(){
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	private void setActiveMonth(int position){
		Date activeMonth = CsDateUtil.addMonth(TODAY, position - ACTIVE_PAGE);
		txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, activeMonth));
        prefAccUtil.set(ClConst.PREF_ACTIVE_DATE, CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeMonth));
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

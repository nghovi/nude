package trente.asia.calendar.commons.fragments;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.commons.views.NavigationHeader;
import trente.asia.calendar.commons.views.PageSharingHolder;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.CalendarListFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarListener;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * PageContainerFragment
 *
 * @author TrungND
 */
public abstract class PageContainerFragment extends AbstractClFragment{

	protected PageSharingHolder			holder;
	protected UserListLinearLayout		lnrUserList;
	protected ViewPager					mViewPager;
	protected WfSlideMenuLayout			mSlideMenuLayout;
	protected ClFragmentPagerAdapter	mPagerAdapter;

	protected final int					INITIAL_POSITION			= Integer.MAX_VALUE / 2;
	protected final Date				TODAY						= Calendar.getInstance().getTime();

	protected NavigationHeader			navigationHeader;
	protected OnChangeCalendarListener	onChangeCalendarListener	= new OnChangeCalendarListener() {

																		@Override
																		public void onChangeCalendarListener(String subTitle, boolean isRefresh){
																			TextView txtHeaderSubtitle = (TextView)PageContainerFragment.this.getView().findViewById(R.id.txt_id_header_title_sub);
																			txtHeaderSubtitle.setText(subTitle);

																			if(isRefresh){
																				holder.isRefreshUserList = true;
																				ClPageFragment fragment = (SchedulesPageFragment)mPagerAdapter.getItem(holder.selectedPagePosition);
																				fragment.loadData();
																			}
																		}
																	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_pager_container, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		navigationHeader = (NavigationHeader)getView().findViewById(R.id.lnr_navigation_header);
		lnrUserList = (UserListLinearLayout)activity.findViewById(R.id.lnr_fragment_pager_container_user_list);
		if(!isShowUserList()){
			lnrUserList.setVisibility(View.GONE);
		}
		mSlideMenuLayout = (WfSlideMenuLayout)getView().findViewById(R.id.drawer_layout);
		navigationHeader.slideMenu = mSlideMenuLayout;

		holder = new PageSharingHolder(navigationHeader, lnrUserList, this);
		holder.selectedPagePosition = INITIAL_POSITION;

		mViewPager = (ViewPager)getView().findViewById(R.id.view_id_pager);
		mPagerAdapter = initPagerAdapter();
		mPagerAdapter.setPageSharingHolder(holder);
		mPagerAdapter.setInitialPosition(INITIAL_POSITION);
		mViewPager.setAdapter(mPagerAdapter);

		prefAccUtil.saveActiveDate(TODAY);
		setActiveDate(INITIAL_POSITION);
		mViewPager.setCurrentItem(INITIAL_POSITION);

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int state){
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
			}

			public void onPageSelected(int position){
				setActiveDate(position);
				holder.selectedPagePosition = position;
				ClPageFragment fragment = (ClPageFragment)mPagerAdapter.getItem(position);
				fragment.loadData();
			}
		});

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		CalendarListFragment calendarListFragment = new CalendarListFragment();
		calendarListFragment.setOnChangeCalendarListener(onChangeCalendarListener);
		transaction.replace(R.id.slice_menu_board, calendarListFragment).commit();
	}

	protected void setActiveDate(int position){

	}

	protected abstract boolean isShowUserList();

	protected abstract ClFragmentPagerAdapter initPagerAdapter();

	@Override
	public void onResume(){
		super.onResume();

		if(mSlideMenuLayout != null && mSlideMenuLayout.isMenuShown()){
			mSlideMenuLayout.toggleMenu();
		}
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

}

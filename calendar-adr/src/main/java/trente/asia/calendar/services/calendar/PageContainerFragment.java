package trente.asia.calendar.services.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarListener;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.calendar.services.calendar.view.PageSharingHolder;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public abstract class PageContainerFragment extends AbstractClFragment{

	protected ViewPager						mViewPager;
	protected SchedulesPagerAdapter			mPagerAdapter;
	protected WfSlideMenuLayout				mSlideMenuLayout;

	protected final int						INITIAL_POSITION			= Integer.MAX_VALUE / 2;
	protected final Date					TODAY						= Calendar.getInstance().getTime();

	protected ImageView						mImgLeftHeader;
	protected NavigationHeader				navigationHeader;
	protected UserListLinearLayout			lnrUserList;
	private PageSharingHolder				holder;

	private boolean							isRefreshFilterUser			= true;
	protected OnChangeCalendarListener		onChangeCalendarListener	= new OnChangeCalendarListener() {

																			@Override
																			public void onChangeCalendarListener(String subTitle, boolean isRefresh){
																				TextView txtHeaderSubtitle = (TextView)PageContainerFragment.this.getView().findViewById(R.id.txt_id_header_title_sub);
																				txtHeaderSubtitle.setText(subTitle);

																				if(isRefresh){
																					prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, "");
																					isRefreshFilterUser = true;
																					// load schedule list
																					SchedulesPageFragment fragment = (SchedulesPageFragment)mPagerAdapter.getItem(holder.selectedPagePosition);
																					fragment.loadScheduleList();
																				}
																			}
																		};

	protected OnChangeCalendarUserListener	changeCalendarUserListener	= new OnChangeCalendarUserListener() {

																			@Override
																			public void onChangeCalendarUserListener(List<UserModel> lstCalendarUser){
																				if(isRefreshFilterUser){
																					isRefreshFilterUser = false;
																					if(!CCCollectionUtil.isEmpty(lstCalendarUser)){
																						String targetUserData = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
//																						lnrUserList.show(ClUtil.getTargetUserList(lstCalendarUser, targetUserData), (int)getResources().getDimension(R.dimen.margin_30dp));
																						holder.updateFilter(lstCalendarUser, targetUserData);
																					}else{
																						lnrUserList.removeAllViews();
																						lnrUserList.setVisibility(View.GONE);
																					}
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
		lnrUserList = (UserListLinearLayout)activity.findViewById(R.id.lnr_fragment_pager_container_user_list);
		navigationHeader = (NavigationHeader)getView().findViewById(R.id.lnr_navigation_header);
		mImgLeftHeader = (ImageView)getView().findViewById(R.id.img_id_header_left_icon);
		mViewPager = (ViewPager)getView().findViewById(R.id.view_id_pager);

		mPagerAdapter = initPagerAdapter();
		holder = new PageSharingHolder(navigationHeader, lnrUserList, this);
		mPagerAdapter.setPageSharingHolder(holder);
		mPagerAdapter.setInitialPosition(INITIAL_POSITION);
		mViewPager.setAdapter(mPagerAdapter);
		holder.selectedPagePosition = INITIAL_POSITION;
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
                // load schedule list
				SchedulesPageFragment fragment = (SchedulesPageFragment)mPagerAdapter.getItem(position);
                fragment.loadScheduleList();
			}
		});

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);

		mImgLeftHeader.setOnClickListener(this);
		mSlideMenuLayout = (WfSlideMenuLayout)getView().findViewById(R.id.drawer_layout);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		CalendarListFragment calendarListFragment = new CalendarListFragment();
		calendarListFragment.setOnChangeCalendarListener(onChangeCalendarListener);
		transaction.replace(R.id.slice_menu_board, calendarListFragment).commit();
	}

	protected void setActiveDate(int position){
		Date activeDate = CsDateUtil.addWeek(TODAY, position - INITIAL_POSITION);
		prefAccUtil.saveActiveDate(activeDate);
		String title = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, activeDate);
		TextView txtHeaderTitle = (TextView)getView().findViewById(R.id.txt_id_header_title);
		txtHeaderTitle.setText(title);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_left_icon:
			mSlideMenuLayout.toggleMenu();
			break;
		case R.id.img_id_header_right_icon:
			gotoScheduleFormFragment();
			break;
		case R.id.img_id_done:
			SchedulesPageFragment schedulesPageFragment = (SchedulesPageFragment)mPagerAdapter.getItem(holder.selectedPagePosition);
			schedulesPageFragment.loadScheduleList();
			break;
		default:
			break;
		}
	}

	private void gotoScheduleFormFragment(){
		ScheduleFormFragment fragment = new ScheduleFormFragment();
		gotoFragment(fragment);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	abstract SchedulesPagerAdapter initPagerAdapter();
}

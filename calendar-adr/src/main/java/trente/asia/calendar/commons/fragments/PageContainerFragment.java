package trente.asia.calendar.commons.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.views.ClFragmentPagerAdapter;
import trente.asia.calendar.commons.views.PageSharingHolder;
import trente.asia.calendar.commons.views.UserFacilityView;
import trente.asia.calendar.services.calendar.RoomFilterFragment;
import trente.asia.calendar.services.calendar.ScheduleFormFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.UserFilterFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * PageContainerFragment
 *
 * @author TrungND
 */
public abstract class PageContainerFragment extends AbstractClFragment{

	protected PageSharingHolder			holder;
	protected ViewPager					mViewPager;
	protected ClFragmentPagerAdapter	mPagerAdapter;

	protected final int					INITIAL_POSITION	= Integer.MAX_VALUE / 2;
	protected final Date				TODAY				= Calendar.getInstance().getTime();
	protected TextView					txtToday;
	private UserFacilityView			userFacilityView;
	private int							pagerScrollingState;
	public ClPageFragment				leftNeighborFragment;
	public ClPageFragment				rightNeiborFragment;

	@Override
	public void onResume(){
		super.onResume();
		boolean isUpdate = CCConst.YES.equals(((WelfareActivity)activity).dataMap.get(ClConst.ACTION_SCHEDULE_UPDATE));
		boolean isDelete = CCConst.YES.equals(((WelfareActivity)activity).dataMap.get(ClConst.ACTION_SCHEDULE_DELETE));
		if(isUpdate || isDelete){
			((WelfareActivity)activity).dataMap.clear();
			SchedulesPageFragment fragment = (SchedulesPageFragment)mPagerAdapter.getItem(holder.selectedPagePosition);
			fragment.loadScheduleList();
		}
		if(prefAccUtil != null){
			String filterType = prefAccUtil.get(ClConst.PREF_FILTER_TYPE);
			userFacilityView.updateButtonBackground(filterType);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_done:
			SchedulesPageFragment schedulesPageFragment = (SchedulesPageFragment)mPagerAdapter.getItem(holder.selectedPagePosition);
			schedulesPageFragment.loadScheduleList();
			break;
		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_pager_container, container, false);
		}
		return mRootView;
	}

	@Override
	public void initData(){
		super.initData();
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(null, "", R.drawable.cl_action_add);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				gotoScheduleForm();
			}
		});

		userFacilityView = (UserFacilityView)getView().findViewById(R.id.user_facility_view);
		userFacilityView.initChildren(new UserFacilityView.OnTabClickListener() {

			@Override
			public void onBtnUserClicked(){
				gotoUserFilterFragment();
			}

			@Override
			public void onBtnFacilityClicked(){
				gotoRoomFilterFragment();
			}
		});

		txtToday = (TextView)getView().findViewById(R.id.txt_today);
		holder = new PageSharingHolder();
		holder.selectedPagePosition = INITIAL_POSITION;

		mViewPager = (ViewPager)getView().findViewById(R.id.view_id_pager);
		mPagerAdapter = initPagerAdapter();
		mPagerAdapter.setPageSharingHolder(holder);
		mPagerAdapter.setInitialPosition(INITIAL_POSITION);
		mViewPager.setAdapter(mPagerAdapter);

		prefAccUtil.saveActiveDate(TODAY);
		setActiveDate(INITIAL_POSITION);
		setWizardProgress(INITIAL_POSITION + 1);
		mViewPager.setCurrentItem(INITIAL_POSITION);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int state){
				pagerScrollingState = state;
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
			}

			public void onPageSelected(int position){
				if(isSwipeRightToLeftOnly()){
					setWizardProgress(position);
				}
				setActiveDate(position);
				holder.selectedPagePosition = position;
				ClPageFragment fragment = (ClPageFragment)mPagerAdapter.getItem(position);
				leftNeighborFragment = (ClPageFragment)mPagerAdapter.getItem(position - 1);
				rightNeiborFragment = (ClPageFragment)mPagerAdapter.getItem(position + 1);
				fragment.loadData();
				onFragmentSelected(fragment);
			}
		});
		leftNeighborFragment = (ClPageFragment)mPagerAdapter.getItem(INITIAL_POSITION - 1);
		rightNeiborFragment = (ClPageFragment)mPagerAdapter.getItem(INITIAL_POSITION + 1);
	}

	protected void onFragmentSelected(ClPageFragment fragment){

	}

	public int getScrollingState(){
		return pagerScrollingState;
	}

	protected void gotoRoomFilterFragment(){
		RoomFilterFragment roomFilterFragment = new RoomFilterFragment();
		gotoFragment(roomFilterFragment);
	}

	protected void gotoUserFilterFragment(){
		UserFilterFragment userFilterFragment = new UserFilterFragment();
		gotoFragment(userFilterFragment);
	}

	private void gotoScheduleForm(){
		ScheduleFormFragment scheduleFormFragment = new ScheduleFormFragment();
		Date date = holder.getClickedDate();
		scheduleFormFragment.setSelectedDate(date);
		gotoFragment(scheduleFormFragment);
	}

	protected void setWizardProgress(int progress){
	}

	protected boolean isSwipeRightToLeftOnly(){
		return false;
	}

	protected void setActiveDate(int position){
		Date activeDate = getActiveDate(position);
		String title = new SimpleDateFormat("yyyy/M/d").format(activeDate);
		updateHeader(title);
	}

	protected abstract Date getActiveDate(int position);

	protected abstract ClFragmentPagerAdapter initPagerAdapter();

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

}

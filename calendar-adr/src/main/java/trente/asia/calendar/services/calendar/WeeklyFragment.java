package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends AbstractClFragment{

	private ViewPager					mViewPager;
	private WeeklyCalendarPagerAdapter	mPagerAdapter;
	private WfSlideMenuLayout			mSlideMenuLayout;

	private final int					ACTIVE_PAGE	= Integer.MAX_VALUE / 2;
	private final Date					TODAY		= CsDateUtil.makeMonthWithFirstDate();

	private ImageView					mImgLeftHeader;

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
		mImgLeftHeader = (ImageView)getView().findViewById(R.id.img_id_header_left_icon);

		// initHeader(R.drawable.wf_back_white, "Weekly", null);
		mViewPager = (ViewPager)getView().findViewById(R.id.pager);
		mPagerAdapter = new WeeklyCalendarPagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(ACTIVE_PAGE);
		setActiveDate(ACTIVE_PAGE);
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int state){
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
			}

			public void onPageSelected(int position){
				setActiveDate(position);
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
		transaction.replace(R.id.slice_menu_board, calendarListFragment).commit();
	}

	private void setActiveDate(int position){
		Date activeDate = CsDateUtil.addMonth(TODAY, position - ACTIVE_PAGE);
		prefAccUtil.saveActiveDate(activeDate);
	}

	@Override
	protected void initData(){
		// Calendar c = Calendar.getInstance();
		// JSONObject jsonObject = new JSONObject();
		// try{
		// jsonObject.put("startDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, c.getTime()));
		// c.add(Calendar.DATE, 7);
		// jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, c.getTime()));
		// }catch(JSONException e){
		// e.printStackTrace();
		// }
		// requestLoad(WfUrlConst.WF_CL_WEEK_SCHEDULE, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_WEEK_SCHEDULE.equals(url)){
			onLoadWeeklySchedulesSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	private void onLoadWeeklySchedulesSuccess(JSONObject response){
		// List<ScheduleModel> schedules = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);
		// // rooms = CCJsonUtil.convertToModelList(response.optString("rooms"), ApiObjectModel.class);
		// List<UserModel> calendarUsers = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);
		//
		// List<CalendarDayModel> dummy = getCalendarDayModels(schedules);

	}

	private List<CalendarDayModel> getCalendarDayModels(List<ScheduleModel> schedules){
		List<CalendarDayModel> calendarDayModels = new ArrayList<>();
		for(ScheduleModel scheduleModel : schedules){
			CalendarDayModel calendarDayModel = getCalendarDayModel(scheduleModel.startDate, calendarDayModels);
			if(calendarDayModel == null){
				calendarDayModel = new CalendarDayModel();
				calendarDayModel.date = scheduleModel.startDate;
				calendarDayModel.schedules = new ArrayList<>();
			}
			calendarDayModel.schedules.add(scheduleModel);
		}
		return calendarDayModels;
	}

	private CalendarDayModel getCalendarDayModel(String day, List<CalendarDayModel> calendarDayModels){
		for(CalendarDayModel calendarDayModel : calendarDayModels){
			if(calendarDayModel.date.equals(day)){
				return calendarDayModel;
			}
		}
		return null;
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
		default:
			break;
		}
	}

	private void gotoScheduleFormFragment(){
		ScheduleFormFragment fragment = new ScheduleFormFragment();
		// fragment.setRooms(rooms);
		// fragment.setSchedule(schedule);
		gotoFragment(fragment);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}

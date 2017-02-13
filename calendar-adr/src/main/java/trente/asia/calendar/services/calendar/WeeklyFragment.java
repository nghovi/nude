package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarPagerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends AbstractClFragment{

	private ViewPager					mViewPager;
	private WeeklyCalendarPagerAdapter	mPagerAdapter;

	private final int					ACTIVE_PAGE	= Integer.MAX_VALUE / 2;

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
		mPagerAdapter = new WeeklyCalendarPagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(ACTIVE_PAGE);

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		Calendar c = Calendar.getInstance();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("startDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, c.getTime()));
			c.add(Calendar.DATE, 7);
			jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, c.getTime()));
		}catch(JSONException e){
			e.printStackTrace();
		}
//		 requestLoad(WfUrlConst.WF_CL_WEEK_SCHEDULE, jsonObject, true);
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
		List<ScheduleModel> schedules = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);
		// rooms = CCJsonUtil.convertToModelList(response.optString("rooms"), ApiObjectModel.class);
		List<UserModel> calendarUsers = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);

		List<CalendarDayModel> dummy = getCalendarDayModels(schedules);

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

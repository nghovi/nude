package trente.asia.calendar.services.calendar;

import static trente.asia.calendar.services.calendar.ScheduleDetailFragment.inflateWithData;
import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.dialogs.ClScheduleRepeatDialog;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * ScheduleDetailFragment
 *
 * @author VietNH
 */
public class ScheduleFormFragment extends AbstractClFragment{

	private ScheduleModel			schedule;
	private ChiaseListDialog		dlgChooseRoom;
	private ChiaseListDialog		dlgChooseCalendar;
	private ChiaseListDialog		dlgChooseCategory;
	private List<CalendarModel>		calendars;
	private CalendarModel			activeCalendar;
	private List<ApiObjectModel>	calendarHolders;
	private UserListLinearLayout	lnrUserList;

	private List<ApiObjectModel>	rooms;
	private ChiaseTextView			txtRoom;
	private DatePickerDialog		datePickerDialogStart;
	private DatePickerDialog		datePickerDialogEnd;
	private TimePickerDialog		timePickerDialogStart;
	private TimePickerDialog		timePickerDialogEnd;
	private ChiaseTextView			txtStartTime;
	private ChiaseTextView			txtEndTime;
	private ChiaseTextView			txtStartDate;
	private ChiaseTextView			txtEndDate;
	private ChiaseTextView			txtCalendar;

	private List<ApiObjectModel>	categories;
	private ChiaseTextView			txtCategory;
	private ClFilterUserListDialog	filterDialog;
	private ClScheduleRepeatDialog	repeatDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_schedule_form, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.fragment_schedule_form_title), R.drawable.cl_action_save);

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);

		txtRoom = (ChiaseTextView)getView().findViewById(R.id.txt_id_meeting_room);
		txtCalendar = (ChiaseTextView)getView().findViewById(R.id.txt_id_calendar);
		txtCategory = (ChiaseTextView)getView().findViewById(R.id.txt_id_category);
		lnrUserList = (UserListLinearLayout)getView().findViewById(R.id.lnr_id_user_list);
		filterDialog = new ClFilterUserListDialog(activity, lnrUserList);
		repeatDialog = new ClScheduleRepeatDialog(activity);

		getView().findViewById(R.id.lnr_id_meeting_room).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_category).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_calendar).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_repeat).setOnClickListener(this);
		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_id_start_date);
		txtEndDate = (ChiaseTextView)getView().findViewById(R.id.txt_id_end_date);
		txtStartTime = (ChiaseTextView)getView().findViewById(R.id.txt_id_start_time);
		txtEndTime = (ChiaseTextView)getView().findViewById(R.id.txt_id_end_time);

		txtStartDate.setOnClickListener(this);
		txtEndDate.setOnClickListener(this);
		txtStartTime.setOnClickListener(this);
		txtEndTime.setOnClickListener(this);
		lnrUserList.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		JSONObject jsonObject = new JSONObject();
		try{
			if(schedule != null){
				jsonObject.put("key", schedule.key);
			}
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_CL_SCHEDULE_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_SCHEDULE_DETAIL.equals(url)){
			onLoadScheduleDetailSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	private void buildDatePickerDialogs(ScheduleModel schedule){
		initListDialog();

		Calendar calendar = Calendar.getInstance();
		Date starDate = new Date();
		Date endDate = new Date();
		int startHour = 0, startMinute = 0;
		int endHour = 0, endMinute = 0;

		if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
			starDate = CCDateUtil.makeDateCustom(schedule.startDate, WelfareConst.WL_DATE_TIME_7);
			endDate = CCDateUtil.makeDateCustom(schedule.endDate, WelfareConst.WL_DATE_TIME_7);
			startHour = CCStringUtil.isEmpty(schedule.startTime) ? 0 : CCNumberUtil.toInteger(schedule.startTime.split(":")[0]);
			startMinute = CCStringUtil.isEmpty(schedule.startTime) ? 0 : CCNumberUtil.toInteger(schedule.startTime.split(":")[1]);
			endHour = CCStringUtil.isEmpty(schedule.endTime) ? 0 : CCNumberUtil.toInteger(schedule.endTime.split(":")[0]);
			endMinute = CCStringUtil.isEmpty(schedule.endTime) ? 0 : CCNumberUtil.toInteger(schedule.endTime.split(":")[1]);
			calendar.setTime(starDate);
		}else{
			starDate = calendar.getTime();
			endDate = calendar.getTime();
		}

		txtStartDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, starDate));
		txtStartTime.setText(CCFormatUtil.formatZero(startHour) + ":" + CCFormatUtil.formatZero(startMinute));
		txtEndDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, endDate));
		txtEndTime.setText(CCFormatUtil.formatZero(startHour) + ":" + CCFormatUtil.formatZero(startMinute));

		calendar.setTime(starDate);
		datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtStartDate.setText(startDateStr);
				txtStartDate.setValue(startDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		calendar.setTime(endDate);
		datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String endDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtEndDate.setText(endDateStr);
				txtEndDate.setValue(endDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		timePickerDialogEnd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtEndTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtEndTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, endHour, endMinute, true);

		timePickerDialogStart = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtStartTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtStartTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, startHour, startMinute, true);
	}

	private void initListDialog(){
		dlgChooseRoom = new ChiaseListDialog(activity, getString(R.string.cl_schedule_form_item_meeting_room), convertList2Map(rooms), txtRoom, null);
		dlgChooseCategory = new ChiaseListDialog(activity, getString(R.string.cl_schedule_form_item_category), convertList2Map(categories), txtCategory, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				txtCategory.setTextColor(Color.parseColor("#" + selectedKey));
			}
		});

		dlgChooseCalendar = new ChiaseListDialog(getContext(), getString(R.string.cl_schedule_form_item_calendar), WelfareFormatUtil.convertList2Map(calendarHolders), txtCalendar, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				onCalendarSelected(selectedKey);
			}
		});
	}

	private void onCalendarSelected(String selectedKey){
		List<UserModel> calendarUsers = getAllCalendarUsers(calendars, selectedKey);
		if(!CCCollectionUtil.isEmpty(calendarUsers)){
			filterDialog.updateUserList(calendarUsers);
			lnrUserList.show(calendarUsers, (int)getResources().getDimension(R.dimen.margin_30dp));
		}
	}

	private void onLoadScheduleDetailSuccess(JSONObject response){
		schedule = CCJsonUtil.convertToModel(response.optString("schedule"), ScheduleModel.class);
		rooms = CCJsonUtil.convertToModelList(response.optString("rooms"), ApiObjectModel.class);
		calendars = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		calendarHolders = getCalendarHolders(calendars);
		categories = CCJsonUtil.convertToModelList(response.optString("categories"), ApiObjectModel.class);

		inflateWithData((ViewGroup)getView(), txtRoom, txtCalendar, txtCategory, rooms, calendars, categories, schedule);

		// allCalenarUsers = getAllCalendarUsers(calendars, schedule != null && schedule.calendarId != null ? schedule.calendarId : null);
		// List<UserModel> joinUserList = ScheduleDetailFragment.getJoinedUserModels(schedule, allCalenarUsers);
		// horizontalUserListView.show(joinUserList, allCalenarUsers, false, 32, 10);

		buildDatePickerDialogs(schedule);
	}

	private List<UserModel> getAllCalendarUsers(List<CalendarModel> calendars, String calendarId){
		if(!CCStringUtil.isEmpty(calendarId)){
			for(CalendarModel calendarModel : calendars){
				if(calendarModel.key.equals(calendarId)){
					activeCalendar = calendarModel;
					if(CCBooleanUtil.checkBoolean(calendarModel.isMyself)){
						calendarModel.calendarUsers = new ArrayList<>();
						calendarModel.calendarUsers.add(prefAccUtil.getUserPref());
					}
					return calendarModel.calendarUsers;
				}
			}
		}
		return new ArrayList<>();
	}

	public static List<ApiObjectModel> getCalendarHolders(List<CalendarModel> calendars){
		List<ApiObjectModel> apiObjectModels = new ArrayList<>();
		for(CalendarModel calendarModel : calendars){
			ApiObjectModel apiObjectModel = new ApiObjectModel(calendarModel.key, calendarModel.calendarName);
			apiObjectModels.add(apiObjectModel);
		}
		return apiObjectModels;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			sendUpdatedRequest();
			break;
		case R.id.lnr_id_meeting_room:
			dlgChooseRoom.show();
			break;
		case R.id.lnr_id_category:
			dlgChooseCategory.show();
			break;
		case R.id.lnr_id_calendar:
			dlgChooseCalendar.show();
			break;
		case R.id.txt_id_start_date:
			datePickerDialogStart.show();
			break;
		case R.id.txt_id_end_date:
			datePickerDialogEnd.show();
			break;
		case R.id.txt_id_start_time:
			timePickerDialogStart.show();
			break;
		case R.id.txt_id_end_time:
			timePickerDialogEnd.show();
			break;
		case R.id.lnr_id_user_list:
			filterDialog.show();
			break;
		case R.id.lnr_id_repeat:
			repeatDialog.show();
			break;
		default:
			break;
		}
	}

	private void sendUpdatedRequest(){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), null);
		boolean isDayPeriod = true;// / TODO: 2/10/2017
		try{
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
				jsonObject.put("key", schedule.key);
			}
			jsonObject.put("isDayPeriod", isDayPeriod);
			jsonObject.put("joinUsers", lnrUserList.formatUserList());

		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_CL_SCHEDULE_UPD, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_CL_SCHEDULE_UPD.equals(url)){
			schedule = CCJsonUtil.convertToModel(response.optString("schedule"), ScheduleModel.class);
			onScheduleUpdateSuccess();
		}else{
			super.successUpdate(response, url);
		}
	}

	private void onScheduleUpdateSuccess(){
		((ChiaseActivity)activity).isInitData = true;
		onClickBackBtn();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void setSchedule(ScheduleModel schedule){
		this.schedule = schedule;
	}
}

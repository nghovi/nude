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
import android.widget.Toast;

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
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.HorizontalUserListView;
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
	private HorizontalUserListView	horizontalUserListView;
	private ChiaseListDialog		dlgChooseRoom;
	private ChiaseListDialog		dlgChooseCalendar;
	private ChiaseListDialog		dlgChooseCategory;
	private List<CalendarModel>		calendars;
	private List<ApiObjectModel>	calendarHolders;
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
	private List<UserModel>			allCalenarUsers;
	private List<ApiObjectModel>	categories;
	private ChiaseTextView			txtCategory;

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

		txtRoom = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_schedule_form_room);
		txtCalendar = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_schedule_form_calendar);
		txtCategory = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_schedule_form_category);

		getView().findViewById(R.id.lnr_fragment_schedule_form_room).setOnClickListener(this);
		getView().findViewById(R.id.lnr_fragment_schedule_form_category).setOnClickListener(this);
		getView().findViewById(R.id.lnr_fragment_schedule_form_calendar).setOnClickListener(this);
		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.fragment_schedule_detail_start_date);
		txtEndDate = (ChiaseTextView)getView().findViewById(R.id.fragment_schedule_detail_end_date);
		txtStartTime = (ChiaseTextView)getView().findViewById(R.id.fragment_schedule_detail_start_time);
		txtEndTime = (ChiaseTextView)getView().findViewById(R.id.fragment_schedule_detail_end_time);

		txtStartDate.setOnClickListener(this);
		txtEndDate.setOnClickListener(this);
		txtStartTime.setOnClickListener(this);
		txtEndTime.setOnClickListener(this);
		horizontalUserListView = (HorizontalUserListView)getView().findViewById(R.id.view_horizontal_user_list);
	}

	private void buildDatePickerDialogs(ScheduleModel schedule){
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
		txtStartTime.setText(getDisplayNum(startHour) + ":" + getDisplayNum(startMinute));
		txtEndDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, endDate));
		txtEndTime.setText(getDisplayNum(startHour) + ":" + getDisplayNum(startMinute));

		calendar.setTime(starDate);
		datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
				txtStartDate.setText(startDateStr);
				txtStartDate.setValue(startDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		calendar.setTime(endDate);
		datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String endDateStr = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
				txtEndDate.setText(endDateStr);
				txtEndDate.setValue(endDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		timePickerDialogEnd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtEndTime.setText(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
				txtEndTime.setValue(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
			}
		}, endHour, endMinute, true);

		timePickerDialogStart = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtStartTime.setText(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
				txtStartTime.setValue(getDisplayNum(hourOfDay) + ":" + getDisplayNum(minute));
			}
		}, startHour, startMinute, true);
	}

	public String getDisplayNum(int num){
		if(num <= 9){
			return "0" + num;
		}
		return String.valueOf(num);
	}

	private void showChooseRoomDialog(){
		if(dlgChooseRoom != null){
			dlgChooseRoom.show();
		}else{
			dlgChooseRoom = new ChiaseListDialog(getContext(), "Select " + "venue", convertList2Map(rooms), txtRoom, null);
			dlgChooseRoom.show();
		}
	}

	private void showChooseCategoryDialog(){
		if(dlgChooseCategory != null){
			dlgChooseCategory.show();
		}else{
			dlgChooseCategory = new ChiaseListDialog(getContext(), "Select " + "Category", convertList2Map(categories), txtCategory, new ChiaseListDialog.OnItemClicked() {

				@Override
				public void onClicked(String selectedKey, boolean isSelected){
					txtCategory.setTextColor(Color.parseColor("#" + selectedKey));
				}
			});
			dlgChooseCategory.show();
		}
	}

	private void showChooseCalendarDialog(){
		if(dlgChooseCalendar != null){
			dlgChooseCalendar.show();
		}else{
			dlgChooseCalendar = new ChiaseListDialog(getContext(), "Select " + "calendar", WelfareFormatUtil.convertList2Map(calendarHolders), txtCalendar, new ChiaseListDialog.OnItemClicked() {

				@Override
				public void onClicked(String selectedKey, boolean isSelected){
					onCalendarSelected(selectedKey, isSelected);
				}
			});
			dlgChooseCalendar.show();
		}
	}

	private void onCalendarSelected(String selectedKey, boolean isSelected){
		allCalenarUsers = getAllCalendarUsers(calendars, selectedKey);
		horizontalUserListView.updateAllUsers(allCalenarUsers);
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

	private void onLoadScheduleDetailSuccess(JSONObject response){
		schedule = CCJsonUtil.convertToModel(response.optString("schedule"), ScheduleModel.class);
		rooms = CCJsonUtil.convertToModelList(response.optString("rooms"), ApiObjectModel.class);
		calendars = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		calendarHolders = getCalendarHolders(calendars);
		categories = CCJsonUtil.convertToModelList(response.optString("categories"), ApiObjectModel.class);

		inflateWithData((ViewGroup)getView(), txtRoom, txtCalendar, txtCategory, rooms, calendars, categories, schedule);

		allCalenarUsers = getAllCalendarUsers(calendars, schedule != null && schedule.calendarId != null ? schedule.calendarId : null);
		List<UserModel> joinUserList = ScheduleDetailFragment.getJoinedUserModels(schedule, allCalenarUsers);
		horizontalUserListView.show(joinUserList, allCalenarUsers, false, 32, 10);

		buildDatePickerDialogs(schedule);
	}

	private List<UserModel> getAllCalendarUsers(List<CalendarModel> calendars, String calendarId){
		if(!CCStringUtil.isEmpty(calendarId)){
			for(CalendarModel calendarModel : calendars){
				if(calendarModel.key.equals(calendarId)){
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
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			sendUpdatedRequest();
			break;
		case R.id.lnr_fragment_schedule_form_room:
			showChooseRoomDialog();
			break;
		case R.id.lnr_fragment_schedule_form_category:
			showChooseCategoryDialog();
			break;
		case R.id.lnr_fragment_schedule_form_calendar:
			showChooseCalendarDialog();
			break;
		case R.id.fragment_schedule_detail_start_date:
			datePickerDialogStart.show();
			break;
		case R.id.fragment_schedule_detail_end_date:
			datePickerDialogEnd.show();
			break;
		case R.id.fragment_schedule_detail_start_time:
			timePickerDialogStart.show();
			break;
		case R.id.fragment_schedule_detail_end_time:
			timePickerDialogEnd.show();
			break;
		default:
			break;
		}
	}

	private void sendUpdatedRequest(){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), null);
		String joinUsers = horizontalUserListView.getSelectedUserListString();
		boolean isDayPeriod = true;// / TODO: 2/10/2017
		try{
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
				jsonObject.put("key", schedule.key);
				// 2/13/2017
			}
			jsonObject.put("categoryId", txtCategory.getValue());
			jsonObject.put("isDayPeriod", isDayPeriod);
			jsonObject.put("joinUsers", joinUsers);
			jsonObject.put("roomId", txtRoom.getValue());
			jsonObject.put("calendarId", txtCalendar.getValue());
			jsonObject.put("startDate", txtStartDate.getText());
			jsonObject.put("endDate", txtEndDate.getText());
			jsonObject.put("startTime", txtStartTime.getText());
			jsonObject.put("endTime", txtEndTime.getText());

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
		Toast.makeText(activity, "Updated successfully", Toast.LENGTH_LONG).show();
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

	// public void setRooms(List<ApiObjectModel> rooms){
	// this.rooms = WelfareFormatUtil.convertList2Map(rooms);
	// }
}

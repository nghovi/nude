package trente.asia.calendar.services.calendar;

import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.dialogs.ClScheduleRepeatDialog;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * ScheduleFormFragment
 *
 * @author VietNH
 */
public class ScheduleFormFragment extends AbstractScheduleFragment{

	private ChiaseListDialog		dlgChooseRoom;
	private ChiaseListDialog		dlgChooseCalendar;
	private ChiaseListDialog		dlgChooseCategory;

	private DatePickerDialog		datePickerDialogStart;
	private DatePickerDialog		datePickerDialogEnd;
	private TimePickerDialog		timePickerDialogStart;
	private TimePickerDialog		timePickerDialogEnd;

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

//		filterDialog = new ClFilterUserListDialog(activity, lnrUserList);
		repeatDialog = new ClScheduleRepeatDialog(activity, txtRepeat);

		getView().findViewById(R.id.lnr_id_meeting_room).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_category).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_calendar).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_repeat).setOnClickListener(this);

		txtStartDate.setOnClickListener(this);
		txtEndDate.setOnClickListener(this);
		txtStartTime.setOnClickListener(this);
		txtEndTime.setOnClickListener(this);
		lnrUserList.setOnClickListener(this);

		swtAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				if(isChecked){
					txtStartTime.setVisibility(View.INVISIBLE);
					txtEndTime.setVisibility(View.INVISIBLE);
				}else{
					txtStartTime.setVisibility(View.VISIBLE);
					txtEndTime.setVisibility(View.VISIBLE);
				}
			}
		});
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
		txtStartDate.setValue(WelfareFormatUtil.formatDate(calendar.getTime()));

		calendar.setTime(endDate);
		datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String endDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtEndDate.setText(endDateStr);
				txtEndDate.setValue(endDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		txtEndDate.setValue(WelfareFormatUtil.formatDate(calendar.getTime()));

		timePickerDialogEnd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtEndTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtEndTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, endHour, endMinute, true);
		txtEndTime.setValue(CCFormatUtil.formatZero(endHour) + ":" + CCFormatUtil.formatZero(endMinute));

		timePickerDialogStart = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtStartTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtStartTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, startHour, startMinute, true);
		txtStartTime.setValue(CCFormatUtil.formatZero(startHour) + ":" + CCFormatUtil.formatZero(startMinute));
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
				onChangeCalendar(selectedKey);
			}
		});
	}

	protected void onChangeCalendar(String calendarId){
		super.onChangeCalendar(calendarId);
		List<UserModel> calendarUsers = getAllCalendarUsers(calendars, calendarId);
		if(!CCCollectionUtil.isEmpty(calendarUsers)){
			filterDialog.updateUserList(calendarUsers);
		}
	}

	@Override
	protected void onLoadScheduleDetailSuccess(JSONObject response){
		super.onLoadScheduleDetailSuccess(response);
        filterDialog = new ClFilterUserListDialog(activity, lnrUserList);
		buildDatePickerDialogs(schedule);
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
		case R.id.lnr_fragment_pager_container_user_list:
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
			jsonObject.put("isDayPeriod", swtAllDay.isChecked());
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

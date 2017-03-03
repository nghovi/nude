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
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.dialogs.ClScheduleRepeatDialog;
import trente.asia.calendar.commons.model.ScheduleRepeatModel;
import trente.asia.calendar.commons.utils.ClRepeatUtil;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

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
	private ClDialog				editModeDialog;

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
		if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
			initHeader(R.drawable.wf_back_white, getString(R.string.fragment_schedule_edit_title), R.drawable.cl_action_save);
		}else{
			initHeader(R.drawable.wf_back_white, getString(R.string.fragment_schedule_form_title), R.drawable.cl_action_save);
		}

		repeatDialog = new ClScheduleRepeatDialog(activity, txtRepeat);
		editModeDialog = new ClDialog(activity);
		editModeDialog.setDialogScheduleEditMode();
		if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
			editModeDialog.findViewById(R.id.lnr_id_only_this).setOnClickListener(this);
			editModeDialog.findViewById(R.id.lnr_id_only_future).setOnClickListener(this);
			editModeDialog.findViewById(R.id.lnr_id_all).setOnClickListener(this);
			editModeDialog.findViewById(R.id.lnr_id_cancel).setOnClickListener(this);
		}

		getView().findViewById(R.id.lnr_id_meeting_room).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_category).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_calendar).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_repeat).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_join_user_list).setOnClickListener(this);

		txtStartDate.setOnClickListener(this);
		txtEndDate.setOnClickListener(this);
		txtStartTime.setOnClickListener(this);
		txtEndTime.setOnClickListener(this);

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
		// int startHour = 0, startMinute = 0;
		// int endHour = 0, endMinute = 0;

		if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
			starDate = WelfareUtil.makeDate(schedule.startDate);
			endDate = WelfareUtil.makeDate(schedule.endDate);
		}else{
			starDate = calendar.getTime();
			endDate = calendar.getTime();
		}

		if(CCStringUtil.isEmpty(schedule.key)){
			repeatDialog.setStartDate(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, starDate));
		}else{

		}

		WelfareFormatUtil.setChiaseTextView(txtStartDate, WelfareFormatUtil.formatDate(starDate));
		WelfareFormatUtil.setChiaseTextView(txtStartTime, CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_9, starDate));
		WelfareFormatUtil.setChiaseTextView(txtEndDate, WelfareFormatUtil.formatDate(endDate));
		WelfareFormatUtil.setChiaseTextView(txtEndTime, CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_9, endDate));

		calendar.setTime(starDate);
		datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtStartDate.setText(startDateStr);
				txtStartDate.setValue(startDateStr);
				repeatDialog.setStartDate(startDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		timePickerDialogStart = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtStartTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtStartTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

		calendar.setTime(endDate);
		datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String endDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtEndDate.setText(endDateStr);
				txtEndDate.setValue(endDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		// txtEndDate.setValue(WelfareFormatUtil.formatDate(calendar.getTime()));

		timePickerDialogEnd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtEndTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtEndTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
		// txtEndTime.setValue(CCFormatUtil.formatZero(endHour) + ":" + CCFormatUtil.formatZero(endMinute));

	}

	private void initListDialog(){
		dlgChooseRoom = new ChiaseListDialog(activity, getString(R.string.cl_schedule_form_item_meeting_room), convertList2Map(rooms), txtRoom, null);
		dlgChooseCategory = new ChiaseListDialog(activity, getString(R.string.cl_schedule_form_item_category), ClUtil.convertCategoryList2Map(categories), txtCategory, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				CategoryModel categoryModel = ClUtil.findCategory4Id(categories, selectedKey);
				if(categoryModel != null && !CCStringUtil.isEmpty(categoryModel.categoryColor)){
					txtCategory.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));
				}
			}
		});

		onChangeCalendar(calendars.get(0).key);
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
		filterDialog = new ClFilterUserListDialog(activity, lnrUserList, getString(R.string.cl_join_user_dialog_title));
		filterDialog.findViewById(R.id.img_id_done).setOnClickListener(this);
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
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key) && ClRepeatUtil.isRepeat(schedule.repeatType)){
				editModeDialog.show();
			}else{
				updateSchedule(null);
			}
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
		case R.id.lnr_id_repeat:
			repeatDialog.show();
			break;
		case R.id.img_id_done:
			filterDialog.dismiss();
			filterDialog.saveActiveUserList();
			break;
		case R.id.lnr_id_join_user_list:
			filterDialog.show();
			break;
		case R.id.lnr_id_only_this:
			updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_THIS);
			break;
		case R.id.lnr_id_only_future:
			updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_FUTURE);
			break;
		case R.id.lnr_id_all:
			updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ALL);
			break;
		case R.id.lnr_id_cancel:
			editModeDialog.dismiss();
			break;
		default:
			break;
		}
	}

	private void updateSchedule(String modifyType){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), null);
		try{
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
				jsonObject.put("key", schedule.key);
			}
			jsonObject.put("joinUsers", lnrUserList.formatUserList());
			jsonObject.put("isAllDay", swtAllDay.isChecked());

			ScheduleRepeatModel scheduleRepeatModel = repeatDialog.getRepeatModel();
			jsonObject.put("repeatType", scheduleRepeatModel.repeatType);
			if(ClRepeatUtil.isRepeat(scheduleRepeatModel.repeatType)){
				if(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(scheduleRepeatModel.repeatType)){
					jsonObject.put("repeatData", scheduleRepeatModel.repeatData);
				}

				jsonObject.put("repeatLimitType", scheduleRepeatModel.repeatLimitType);
				if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(scheduleRepeatModel.repeatLimitType)){
					jsonObject.put("repeatEnd", scheduleRepeatModel.repeatEnd);
				}else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleRepeatModel.repeatLimitType)){
					jsonObject.put("repeatInterval", scheduleRepeatModel.repeatInterval);
				}

			}

			if(!CCStringUtil.isEmpty(modifyType)){
				jsonObject.put("modifyType", modifyType);
			}
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
		((WelfareActivity)activity).dataMap.put(ClConst.IS_UPDATE_SCHEDULE, true);
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

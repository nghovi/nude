package trente.asia.calendar.services.calendar;

import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.CLOutboundDismissListDialog;
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

	private CLOutboundDismissListDialog	dlgChooseRoom;
	private CLOutboundDismissListDialog	dlgChooseCalendar;
	private CLOutboundDismissListDialog	dlgChooseCategory;

	private DatePickerDialog			datePickerDialogStart;
	private DatePickerDialog			datePickerDialogEnd;
	private TimePickerDialog			timePickerDialogStart;
	private TimePickerDialog			timePickerDialogEnd;

	private ClFilterUserListDialog		filterDialog;
	private ClScheduleRepeatDialog		repeatDialog;
	private ClDialog					editModeDialog;
	private Date						selectedDate;

	private final String				SCHEDULE_EDIT_MODE		= "E";
	private final String				SCHEDULE_DELETE_MODE	= "D";
	private String						editMode;

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
		repeatDialog.setOnChangeRepeatTypeListener(new ClScheduleRepeatDialog.OnChangeRepeatTypeListener() {

			@Override
			public void onChange(boolean isRepeated){
				if(isRepeated){
					if(swtAllDay.isChecked()){
						lnrEndDate.setVisibility(View.GONE);
					}else{
						txtEndDate.setVisibility(View.INVISIBLE);
					}
				}else if(swtAllDay.isChecked()){
					lnrEndDate.setVisibility(View.VISIBLE);
					txtEndDate.setVisibility(View.VISIBLE);
				}
			}
		});
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

		txtStartTime.setOnClickListener(this);
		txtEndTime.setOnClickListener(this);

		swtAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
				if(isChecked){
					txtStartTime.setVisibility(View.INVISIBLE);
					txtEndTime.setVisibility(View.INVISIBLE);
					if(ClConst.SCHEDULE_REPEAT_TYPE_NONE.equals(repeatDialog.getRepeatModel().repeatType)){
						lnrEndDate.setVisibility(View.VISIBLE);
						lnrEndDate.setVisibility(View.VISIBLE);
						txtEndDate.setVisibility(View.VISIBLE);
					}else{
						lnrEndDate.setVisibility(View.GONE);
					}
				}else{
					lnrEndDate.setVisibility(View.VISIBLE);
					txtStartTime.setVisibility(View.VISIBLE);
					txtEndTime.setVisibility(View.VISIBLE);
					txtEndDate.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void buildDatePickerDialogs(ScheduleModel schedule){
		initListDialog(schedule);

		Calendar calendar = Calendar.getInstance();
		if(selectedDate == null){
			selectedDate = calendar.getTime();
		}else{
			calendar.setTime(selectedDate);
		}

		Date startDate = new Date();
		Date endDate = new Date();
		String startTimeStr;
		String endTimeStr;

		if(schedule != null && !CCStringUtil.isEmpty(schedule.key) && (!schedule.isAllDay || schedule.isPeriodSchedule())){
			startDate = WelfareUtil.makeDate(schedule.startDate);
			startDate = CCDateUtil.makeDateTime(startDate, schedule.startTime);
			endDate = WelfareUtil.makeDate(schedule.endDate);
			endDate = CCDateUtil.makeDateTime(endDate, schedule.endTime);
			startTimeStr = schedule.startTime;
			endTimeStr = schedule.endTime;
		}else{
			startDate = calendar.getTime();
			endDate = calendar.getTime();
			startTimeStr = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, Calendar.getInstance().getTime());
			startTimeStr = getRoundedTime(startTimeStr);
			endTimeStr = addAnHour(startTimeStr);
		}

		repeatDialog.setStartDate(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, startDate));
		if(ClRepeatUtil.isRepeat(schedule.repeatType)){
			// set repeat dialog values
			ScheduleRepeatModel repeatModel = new ScheduleRepeatModel(schedule);
			repeatDialog.setRepeatModel(repeatModel);
		}else{
			repeatDialog.initDefaultValue();
		}

		WelfareFormatUtil.setChiaseTextView(txtStartDate, WelfareFormatUtil.formatDate(startDate));
		WelfareFormatUtil.setChiaseTextView(txtStartTime, startTimeStr);
		WelfareFormatUtil.setChiaseTextView(txtEndDate, WelfareFormatUtil.formatDate(endDate));
		WelfareFormatUtil.setChiaseTextView(txtEndTime, endTimeStr);

		calendar.setTime(startDate);
		datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtStartDate.setText(startDateStr);
				txtStartDate.setValue(startDateStr);
				repeatDialog.setStartDate(startDateStr);
				repeatDialog.initDefaultValue();
				Date startDate = CCDateUtil.makeDateCustom(startDateStr, WelfareConst.WF_DATE_TIME_DATE);
				Date endDate = CCDateUtil.makeDateCustom(txtEndDate.getText().toString(), WelfareConst.WF_DATE_TIME_DATE);
				datePickerDialogEnd.getDatePicker().setMinDate(startDate.getTime());
				// // TODO: 3/21/2017 it's not good initDataPickerDialogEnd everytime
				Calendar cal = Calendar.getInstance();
				if(CCDateUtil.compareDate(startDate, endDate, false) > 0){
					cal.setTime(startDate);
				}else{
					cal.setTime(endDate);
				}
				initDatePickerDialogEnd(cal, startDate);

			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		timePickerDialogStart = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtStartTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtStartTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, CCDateUtil.getHourFromTimeString(startTimeStr), CCDateUtil.getMinuteFromTimeString(startTimeStr), true);

		calendar.setTime(endDate);
		initDatePickerDialogEnd(calendar, startDate);

		timePickerDialogEnd = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute){
				txtEndTime.setText(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
				txtEndTime.setValue(CCFormatUtil.formatZero(hourOfDay) + ":" + CCFormatUtil.formatZero(minute));
			}
		}, CCDateUtil.getHourFromTimeString(endTimeStr), CCDateUtil.getMinuteFromTimeString(endTimeStr), true);
		// txtEndTime.setValue(CCFormatUtil.formatZero(endHour) + ":" + CCFormatUtil.formatZero(endMinute));

	}

	private void initDatePickerDialogEnd(Calendar calendar, Date startDate){
		datePickerDialogEnd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				onEndDateSet(year, month, dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialogEnd.getDatePicker().setMinDate(startDate.getTime());
		onEndDateSet(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}

	private void onEndDateSet(int year, int month, int dayOfMonth){
		String endDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
		txtEndDate.setText(endDateStr);
		txtEndDate.setValue(endDateStr);
	}

	private String addAnHour(String startTimeStr){
		int hour = CCDateUtil.getHourFromTimeString(startTimeStr);
		int minute = CCDateUtil.getMinuteFromTimeString(startTimeStr);
		if(hour == 23){
			hour = 0;
		}else{
			hour = hour + 1;
		}
		return String.format("%02d", hour) + ":" + String.format("%02d", minute);
	}

	public static String getRoundedTime(String startTimeStr){
		int hour = CCDateUtil.getHourFromTimeString(startTimeStr);
		int minute = CCDateUtil.getMinuteFromTimeString(startTimeStr);
		if(minute < 30){
			minute = 30;
		}else{
			minute = 0;
			if(hour == 23){
				hour = 0;
			}else{
				hour = hour + 1;
			}
		}
		return String.format("%02d", hour) + ":" + String.format("%02d", minute);
	}

	private void initListDialog(ScheduleModel scheduleModel){
		dlgChooseRoom = new CLOutboundDismissListDialog(activity, getString(R.string.cl_schedule_form_item_meeting_room), convertList2Map(rooms), txtRoom, null);
		dlgChooseCategory = new CLOutboundDismissListDialog(activity, getString(R.string.cl_schedule_form_item_category), ClUtil.convertCategoryList2Map(categories), txtCategory, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				CategoryModel categoryModel = ClUtil.findCategory4Id(categories, selectedKey);
				if(categoryModel != null && !CCStringUtil.isEmpty(categoryModel.categoryColor)){
					txtCategory.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));
				}
			}
		});

		if(!CCStringUtil.isEmpty(scheduleModel.key)){
			if(!CCCollectionUtil.isEmpty(scheduleModel.calendar.calendarUsers)){
				filterDialog.updateUserList(scheduleModel.calendar.calendarUsers);
			}
		}else{
			onChangeCalendar(calendars.get(0).key);
		}

		// Collections.sort(calendarHolders, new Comparator<ApiObjectModel>() {
		// @Override
		// public int compare(ApiObjectModel o1, ApiObjectModel o2) {
		// return o1.value.compareToIgnoreCase(o2.value);
		// }
		// });
		Map<String, String> calendarMap = WelfareFormatUtil.convertList2Map(calendarHolders);
		dlgChooseCalendar = new CLOutboundDismissListDialog(getContext(), getString(R.string.cl_schedule_form_item_calendar), calendarMap, txtCalendar, new ChiaseListDialog.OnItemClicked() {

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

		buildCalendarChooseDialog();
		buildDatePickerDialogs(schedule);

		if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
			Button btnDelete = (Button)getView().findViewById(R.id.btn_id_delete);
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setOnClickListener(this);
		}

		if(!ScheduleModel.isRepeat(schedule)){
			txtStartDate.setOnClickListener(this);
			txtEndDate.setOnClickListener(this);
		}
	}

	private void buildCalendarChooseDialog(){
		if(schedule != null && schedule.key != null){
			getView().findViewById(R.id.img_arrow_right_calendar).setVisibility(View.GONE);
			getView().findViewById(R.id.lnr_id_calendar).setClickable(false);
		}
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
				editMode = SCHEDULE_EDIT_MODE;
				editModeDialog.updateScheduleEditModeTitle(getString(R.string.cl_schedule_edit_mode_title));
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
			showCalendarChooseDialog();
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
			editModeDialog.dismiss();
			if(SCHEDULE_EDIT_MODE.equals(editMode)){
				updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_THIS);
			}else{
				deleteSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_THIS);
			}
			break;
		case R.id.lnr_id_only_future:
			editModeDialog.dismiss();
			if(SCHEDULE_EDIT_MODE.equals(editMode)){
				updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_FUTURE);
			}else{
				deleteSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_FUTURE);
			}
			break;
		case R.id.lnr_id_all:
			editModeDialog.dismiss();
			if(SCHEDULE_EDIT_MODE.equals(editMode)){
				updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ALL);
			}else{
				deleteSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ALL);
			}
			break;
		case R.id.lnr_id_cancel:
			editModeDialog.dismiss();
			break;
		case R.id.btn_id_delete:
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key) && ClRepeatUtil.isRepeat(schedule.repeatType)){
				editMode = SCHEDULE_DELETE_MODE;
				editModeDialog.updateScheduleEditModeTitle(getString(R.string.cl_schedule_delete_mode_title));
				editModeDialog.show();
			}else{
				deleteSchedule(null);
			}
			break;
		default:
			break;
		}
	}

	private void showCalendarChooseDialog(){
		if(schedule != null && schedule.key == null){
			dlgChooseCalendar.show();
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
				}
				// else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleRepeatModel.repeatLimitType)){
				// jsonObject.put("repeatInterval", scheduleRepeatModel.repeatInterval);
				// }

			}

			if(!CCStringUtil.isEmpty(modifyType)){
				jsonObject.put("modifyType", modifyType);
				jsonObject.put("targetDate", WelfareFormatUtil.formatDate(selectedDate));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_CL_SCHEDULE_UPD, jsonObject, true);
	}

	private void deleteSchedule(String modifyType){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", schedule.key);
			if(!CCStringUtil.isEmpty(modifyType)){
				jsonObject.put("modifyType", modifyType);
				jsonObject.put("targetDate", WelfareFormatUtil.formatDate(selectedDate));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_CL_SCHEDULE_DEL, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_CL_SCHEDULE_UPD.equals(url)){
			String oldKey = schedule.key;
			try{
				schedule = LoganSquare.parse(response.optString("schedule"), ScheduleModel.class);
				String newKey = response.optString("returnValue");
				onScheduleUpdateSuccess(CCStringUtil.isEmpty(newKey) ? oldKey : newKey);
			}catch(IOException e){
				e.printStackTrace();
			}
		}else if(WfUrlConst.WF_CL_SCHEDULE_DEL.equals(url)){
			((WelfareActivity)activity).dataMap.put(ClConst.ACTION_SCHEDULE_DELETE, CCConst.YES);
			getFragmentManager().popBackStack();
		}else{
			super.successUpdate(response, url);
		}
	}

	private void onScheduleUpdateSuccess(String newKey){
		((WelfareActivity)activity).dataMap.put(ClConst.ACTION_SCHEDULE_UPDATE, CCConst.YES);
		((WelfareActivity)activity).dataMap.put(ClConst.ACTION_SCHEDULE_UPDATE_NEW_KEY, newKey);
		onClickBackBtn();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void setSchedule(ScheduleModel schedule){
		this.schedule = schedule;
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}

	@Override
	protected void onClickBackBtn(){
		((ChiaseActivity)activity).isInitData = true;
		super.onClickBackBtn();
	}

}

package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.OnTimePickerListener;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.CLOutboundDismissListDialog;
import trente.asia.calendar.commons.dialogs.CLTimePicker;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.commons.dialogs.ClScheduleRepeatDialog;
import trente.asia.calendar.commons.utils.ClRepeatUtil;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.RoomModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * ScheduleFormFragment
 *
 * @author VietNH
 */
public class ScheduleFormFragment extends AbstractScheduleFragment implements OnTimePickerListener{

	private CLOutboundDismissListDialog	dlgChooseRoom;
	private CLOutboundDismissListDialog	dlgChooseCategory;
	private CLOutboundDismissListDialog	dlgChooseScope;

	private DatePickerDialog			datePickerDialogStart;
	private DatePickerDialog			datePickerDialogEnd;
	private CLTimePicker				timePickerDialog;

	private ClScheduleRepeatDialog		repeatDialog;

	private ClDialog					editModeDialog;
	private ClDialog					confirmModeDialog;
	private Date						selectedDate;

	private final String				SCHEDULE_EDIT_MODE		= "E";
	private final String				SCHEDULE_DELETE_MODE	= "D";
	private String						editMode;
	private List<UserModel>				joinUsers;
	private boolean						timePickerStart;
	private int							selectedHour;
	private int							selectedMinute;
	ImageView							imgChecked;

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

		imgChecked = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);

		repeatDialog = new ClScheduleRepeatDialog(activity, txtRepeat, txtRepeatUntil);
		repeatDialog.setOnChangeRepeatTypeListener(new ClScheduleRepeatDialog.OnChangeRepeatTypeListener() {

			@Override
			public void onChange(boolean isRepeated){
				if(isRepeated){
					lnrRepeatUntil.setVisibility(View.VISIBLE);
					if(swtAllDay.isChecked()){
						lnrEndDate.setVisibility(View.GONE);
					}else{
						txtEndDate.setVisibility(View.INVISIBLE);
					}
				}else{
					lnrRepeatUntil.setVisibility(View.GONE);
					if(swtAllDay.isChecked()){
						lnrRepeatUntil.setVisibility(View.GONE);
						lnrEndDate.setVisibility(View.VISIBLE);
						txtEndDate.setVisibility(View.VISIBLE);
					}
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

		confirmModeDialog = new ClDialog(activity);
		confirmModeDialog.setDialogScheduleConfirmMode();
		confirmModeDialog.findViewById(R.id.lnr_id_confirm_ok).setOnClickListener(this);
		confirmModeDialog.findViewById(R.id.lnr_id_confirm_cancel).setOnClickListener(this);

		getView().findViewById(R.id.lnr_id_scope).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_meeting_room).setOnClickListener(this);
		getView().findViewById(R.id.lnr_id_category).setOnClickListener(this);
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
		initScopeDialog(schedule);

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

		if(schedule != null && !CCStringUtil.isEmpty(schedule.key) && (!schedule.isAllDay || schedule.isPeriod)){
			startDate = schedule.startDate;
			startDate = CCDateUtil.makeDateTime(startDate, schedule.startTime);
			endDate = schedule.endDate;
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

		repeatDialog.setStartDateStr(CCFormatUtil.formatDateCustom("yyyy/M/d", startDate));
		if(ClRepeatUtil.isRepeat(schedule.repeatType)){
			// set repeat dialog values
			repeatDialog.setRepeatModel(schedule);
		}else{
			repeatDialog.initDefaultValue();
		}

		WelfareFormatUtil.setChiaseTextView(txtStartDate, CCFormatUtil.formatDateCustom("yyyy/M/d", startDate));
		WelfareFormatUtil.setChiaseTextView(txtStartTime, startTimeStr);
		WelfareFormatUtil.setChiaseTextView(txtEndDate, CCFormatUtil.formatDateCustom("yyyy/M/d", endDate));
		WelfareFormatUtil.setChiaseTextView(txtEndTime, endTimeStr);

		calendar.setTime(startDate);
		datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + (month + 1) + "/" + (dayOfMonth);
				txtStartDate.setText(startDateStr);
				txtStartDate.setValue(startDateStr);
				repeatDialog.setStartDateStr(startDateStr);
				repeatDialog.initDefaultValue();
				Date startDate = CCDateUtil.makeDateCustom(startDateStr, WelfareConst.WF_DATE_TIME_DATE);
				Date endDate = CCDateUtil.makeDateCustom(txtEndDate.getText().toString(), WelfareConst.WF_DATE_TIME_DATE);
				if(!swtAllDay.isChecked()){
					endDate = startDate;
				}
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

		calendar.setTime(endDate);
		initDatePickerDialogEnd(calendar, startDate);
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
		String endDateStr = year + "/" + (month + 1) + "/" + (dayOfMonth);
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

	private void initScopeDialog(ScheduleModel scheduleModel){
		final Map<String, String> scopes = getPublicityMap();
		dlgChooseScope = new CLOutboundDismissListDialog(activity, getString(R.string.cl_schedule_form_item_scope), scopes, txtScope, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				txtScope.setText(getTitle(scopes.get(selectedKey)));
			}
		});
		dlgChooseRoom = new CLOutboundDismissListDialog(activity, getString(R.string.cl_schedule_form_item_meeting_room), getRoomMap(rooms), txtRoom, null);
		dlgChooseCategory = new CLOutboundDismissListDialog(activity, getString(R.string.cl_schedule_form_item_category), ClUtil.convertCategoryList2Map(categories), txtCategory, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				// CategoryModel categoryModel = ClUtil.findCategory4Id(categories, selectedKey);
				// if(categoryModel != null && !CCStringUtil.isEmpty(categoryModel.categoryColor)){
				// txtCategory.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));
				// }
			}
		});
	}

	// public String getDescriptionfromKey(String key, Map<String,String> scopes){
	//// for(int temp =0;temp<scopes.size();temp++){
	// return scopes.get(key);
	//// }
	// }
	private Map<String, String> getRoomMap(List<RoomModel> rooms){

		if(CCCollectionUtil.isEmpty(rooms)){
			return null;
		}

		Map<String, String> map = new LinkedHashMap<>();
		for(RoomModel model : rooms){
			map.put(model.key, model.roomName);
		}
		return map;
	}

	@Override
	protected void onLoadScheduleDetailSuccess(JSONObject response){
		super.onLoadScheduleDetailSuccess(response);
		joinUsers = schedule.scheduleJoinUsers;
		if(CCCollectionUtil.isEmpty(joinUsers)){
			joinUsers.add(myself);
		}
		updateJoinUsers(joinUsers);

		buildDatePickerDialogs(schedule);

		if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
			Button btnDelete = (Button)getView().findViewById(R.id.btn_id_delete);
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setOnClickListener(this);
		}

		if(!ScheduleModel.isRepeat(schedule)){
			txtStartDate.setOnClickListener(this);
			txtEndDate.setOnClickListener(this);
			lnrRepeatUntil.setVisibility(View.GONE);
		}else{
			lnrRepeatUntil.setVisibility(View.VISIBLE);
			if(!CCStringUtil.isEmpty(schedule.repeatEnd)){
				txtRepeatUntil.setText(WelfareUtil.getDateString(schedule.repeatEnd));
			}else{
				txtRepeatUntil.setText(getString(R.string.cl_schedule_repeat_limit_forever));
			}
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
			imgChecked.setClickable(false);
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key) && ClRepeatUtil.isRepeat(schedule.repeatType)){
				editMode = SCHEDULE_EDIT_MODE;
				editModeDialog.updateScheduleEditModeTitle(getString(R.string.cl_schedule_edit_mode_title));
				editModeDialog.show();
			}else{
				updateSchedule(null, ClConst.API_SCHEDULE_CHECK_UPDATE);
			}
			break;
		case R.id.lnr_id_meeting_room:
			dlgChooseRoom.show();
			break;
		case R.id.lnr_id_scope:
			dlgChooseScope.show();
			break;
		case R.id.lnr_id_category:
			dlgChooseCategory.show();
			break;
		case R.id.txt_id_start_date:
			datePickerDialogStart.show();
			break;
		case R.id.txt_id_end_date:
			datePickerDialogEnd.show();
			break;
		case R.id.txt_id_start_time:
			timePickerStart = true;
			selectedHour = Integer.parseInt(txtStartTime.getText().toString().split(":")[0]);
			selectedMinute = Integer.parseInt(txtStartTime.getText().toString().split(":")[1]);
			openTimePicker();
			break;
		case R.id.txt_id_end_time:
			timePickerStart = false;
			selectedHour = Integer.parseInt(txtEndTime.getText().toString().split(":")[0]);
			selectedMinute = Integer.parseInt(txtEndTime.getText().toString().split(":")[1]);
			openTimePicker();
			break;
		case R.id.lnr_id_repeat:
			repeatDialog.show();
			break;
		// case R.id.img_id_done:
		// filterDialog.dismiss();
		// filterDialog.saveSelectUserList();
		// break;
		case R.id.lnr_id_join_user_list:
		case R.id.lnr_id_container_join_user_list:
			gotoSelectUserFragment();
			break;
		case R.id.lnr_id_only_this:
			editModeDialog.dismiss();
			if(SCHEDULE_EDIT_MODE.equals(editMode)){
				updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_THIS, ClConst.API_SCHEDULE_UPDATE);
			}else{
				deleteSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_THIS);
			}
			break;
		case R.id.lnr_id_only_future:
			editModeDialog.dismiss();
			if(SCHEDULE_EDIT_MODE.equals(editMode)){
				updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_FUTURE, ClConst.API_SCHEDULE_UPDATE);
			}else{
				deleteSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ONLY_FUTURE);
			}
			break;
		case R.id.lnr_id_all:
			editModeDialog.dismiss();
			if(SCHEDULE_EDIT_MODE.equals(editMode)){
				updateSchedule(ClConst.SCHEDULE_MODIFY_TYPE_ALL, ClConst.API_SCHEDULE_UPDATE);
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
		case R.id.lnr_id_confirm_ok:
			confirmModeDialog.dismiss();
			updateSchedule(null, ClConst.API_SCHEDULE_UPDATE);
		case R.id.lnr_id_confirm_cancel:
			confirmModeDialog.dismiss();
		default:
			break;
		}
	}

	private void openTimePicker(){
		timePickerDialog = new CLTimePicker();
		timePickerDialog.setStartTime(timePickerStart);
		timePickerDialog.setCallback(this);
		timePickerDialog.setSelectedHour(selectedHour);
		timePickerDialog.setSelectedMinute(selectedMinute);
		FragmentManager fm = getFragmentManager();
		timePickerDialog.show(fm, "dialog");
	}

	private void gotoSelectUserFragment(){
		UserSelectFragment userSelectFragment = new UserSelectFragment();
		userSelectFragment.setJoinUsers(joinUsers);
		userSelectFragment.setFormFragment(this);
		gotoFragment(userSelectFragment);
	}

	public void updateJoinUsers(List<UserModel> lstSelectedUser){
		joinUsers = lstSelectedUser;
		lnrUserList.show(joinUsers, WelfareUtil.dpToPx(30));
	}

	@Override
	public void onTimePickerCompleted(int hour, int minute){
		if(timePickerStart){
			txtStartTime.setText(CCFormatUtil.formatZero(hour) + ":" + CCFormatUtil.formatZero(minute));
			txtStartTime.setValue(CCFormatUtil.formatZero(hour) + ":" + CCFormatUtil.formatZero(minute));
		}else{
			txtEndTime.setText(CCFormatUtil.formatZero(hour) + ":" + CCFormatUtil.formatZero(minute));
			txtEndTime.setValue(CCFormatUtil.formatZero(hour) + ":" + CCFormatUtil.formatZero(minute));
		}
	}

	public static class ChangeCalendarConfirmDialog extends DialogFragment{

		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(getString(R.string.cl_schedule_confirm_change_cal)).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialogInterface, int i){
				}
			});
			return builder.create();
		}
	}

	private void updateSchedule(String modifyType, String url){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), null);
		try{
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
				jsonObject.put("key", schedule.key);
			}
			jsonObject.put("joinUsers", lnrUserList.formatUserList());
			jsonObject.put("isAllDay", swtAllDay.isChecked());

			ScheduleModel scheduleRepeatModel = repeatDialog.getRepeatModel();
			jsonObject.put("repeatType", scheduleRepeatModel.repeatType);
			if(ClRepeatUtil.isRepeat(scheduleRepeatModel.repeatType)){
				if(ClConst.SCHEDULE_REPEAT_TYPE_WEEKLY.equals(scheduleRepeatModel.repeatType)){
					jsonObject.put("repeatData", scheduleRepeatModel.repeatData);
				}

				jsonObject.put("repeatLimitType", scheduleRepeatModel.repeatLimitType);
				if(ClConst.SCHEDULE_REPEAT_LIMIT_UNTIL.equals(scheduleRepeatModel.repeatLimitType)){
					jsonObject.put("repeatEnd", WelfareUtil.getDateString(scheduleRepeatModel.repeatEnd));
				}
				// else if(ClConst.SCHEDULE_REPEAT_LIMIT_AFTER.equals(scheduleRepeatModel.repeatLimitType)){
				// jsonObject.put("repeatInterval", scheduleRepeatModel.repeatInterval);
				// }

			}

			if(!CCStringUtil.isEmpty(modifyType)){
				jsonObject.put("modifyType", modifyType);
				jsonObject.put("targetDate", WelfareUtil.getDateString(selectedDate));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(url, jsonObject, true);
	}

	private void deleteSchedule(String modifyType){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", schedule.key);
			if(!CCStringUtil.isEmpty(modifyType)){
				jsonObject.put("modifyType", modifyType);
				jsonObject.put("targetDate", WelfareUtil.getDateString(selectedDate));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_SCHEDULE_DEL, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){

		switch(CCStringUtil.checkNull(url)){
		case ClConst.API_SCHEDULE_CHECK_UPDATE:

			List<ApiObjectModel> users = null;
			List<ApiObjectModel> rooms = null;
			try{
				users = LoganSquare.parseList(response.optString("dupUsers"), ApiObjectModel.class);
				rooms = LoganSquare.parseList(response.optString("dupRooms"), ApiObjectModel.class);
			}catch(IOException e){
				e.printStackTrace();
			}

			if(CCCollectionUtil.isEmpty(users) && CCCollectionUtil.isEmpty(rooms)){ // update directly
				updateSchedule(null, ClConst.API_SCHEDULE_UPDATE);
			}else{ // show dialog

				TextView textMessageUser = (TextView)confirmModeDialog.findViewById(R.id.txt_cl_confirm_message1);
				if(!CCCollectionUtil.isEmpty(users)){
					StringBuffer sbUser = new StringBuffer();
					for(ApiObjectModel obj : users){
						sbUser.append(obj.value).append("\n");
					}
					textMessageUser.setText(sbUser.toString());
					confirmModeDialog.findViewById(R.id.txt_cl_confirm_label1).setVisibility(View.VISIBLE);
					textMessageUser.setVisibility(View.VISIBLE);
				}else{
					confirmModeDialog.findViewById(R.id.txt_cl_confirm_label1).setVisibility(View.GONE);
					textMessageUser.setVisibility(View.GONE);
				}

				TextView textMessageRoom = (TextView)confirmModeDialog.findViewById(R.id.txt_cl_confirm_message2);
				if(!CCCollectionUtil.isEmpty(rooms)){
					StringBuffer sbRoom = new StringBuffer();
					for(ApiObjectModel obj : rooms){
						sbRoom.append(obj.value).append("\n");
					}
					textMessageRoom.setText(sbRoom.toString());
					confirmModeDialog.findViewById(R.id.txt_cl_confirm_label2).setVisibility(View.VISIBLE);
					textMessageRoom.setVisibility(View.VISIBLE);
				}else{
					confirmModeDialog.findViewById(R.id.txt_cl_confirm_label2).setVisibility(View.GONE);
					textMessageRoom.setVisibility(View.GONE);
				}

				confirmModeDialog.updateScheduleEditModeTitle(getString(R.string.cl_schedule_confirm_mode_title));
				confirmModeDialog.show();
			}

			break;
		case ClConst.API_SCHEDULE_UPDATE:
			String oldKey2 = schedule.key;
			try{
				schedule = LoganSquare.parse(response.optString("schedule"), ScheduleModel.class);
				String newKey = response.optString("returnValue");
				onScheduleUpdateSuccess(CCStringUtil.isEmpty(newKey) ? oldKey2 : newKey);
			}catch(IOException e){
				e.printStackTrace();
			}

			break;

		case ClConst.API_SCHEDULE_DEL:
			((WelfareActivity)activity).dataMap.put(ClConst.ACTION_SCHEDULE_DELETE, CCConst.YES);
			getFragmentManager().popBackStack();

			break;
		default:
			super.successUpdate(response, url);
		}
		imgChecked.setClickable(true);
	}

	@Override
	protected void commonNotSuccess(JSONObject response, String url){
		super.commonNotSuccess(response, url);
		imgChecked.setClickable(true);
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

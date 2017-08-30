package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClRepeatUtil;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.RoomModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * AbstractScheduleFragment
 *
 * @author TrungND
 */
public class AbstractScheduleFragment extends AbstractClFragment{

	protected ScheduleModel			schedule;
	protected UserListLinearLayout	lnrUserList;

	protected List<RoomModel>		rooms;
	protected ChiaseTextView		txtRoom;
	protected ChiaseTextView		txtScope;
	protected ChiaseTextView		txtStartTime;
	protected ChiaseTextView		txtEndTime;
	protected ChiaseTextView		txtStartDate;
	protected ChiaseTextView		txtEndDate;
	protected ChiaseTextView		txtRepeat;

	protected List<CategoryModel>	categories;
	protected ChiaseTextView		txtCategory;
	protected SwitchCompat			swtAllDay;
	protected LinearLayout			lnrEndDate;
	// protected ClFilterUserListDialog filterDialog;
	protected List<UserModel>		users;
	protected LinearLayout			lnrRepeatUntil;
	protected TextView				txtRepeatUntil;

	@Override
	protected void initView(){
		super.initView();

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);

		txtRoom = (ChiaseTextView)getView().findViewById(R.id.txt_id_meeting_room);
		txtScope = (ChiaseTextView)getView().findViewById(R.id.txt_id_scope);
		txtCategory = (ChiaseTextView)getView().findViewById(R.id.txt_id_category);
		lnrUserList = (UserListLinearLayout)getView().findViewById(R.id.lnr_id_container_join_user_list);
		swtAllDay = (SwitchCompat)getView().findViewById(R.id.swt_id_all_day);
		txtRepeat = (ChiaseTextView)getView().findViewById(R.id.txt_id_repeat);

		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_id_start_date);
		txtEndDate = (ChiaseTextView)getView().findViewById(R.id.txt_id_end_date);
		lnrEndDate = (LinearLayout)getView().findViewById(R.id.lnr_id_end_date);
		txtStartTime = (ChiaseTextView)getView().findViewById(R.id.txt_id_start_time);
		txtEndTime = (ChiaseTextView)getView().findViewById(R.id.txt_id_end_time);
		lnrRepeatUntil = (LinearLayout)getView().findViewById(R.id.lnr_id_repeat_until);
		txtRepeatUntil = (TextView)getView().findViewById(R.id.txt_repeat_until);
		lnrUserList.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		JSONObject jsonObject = new JSONObject();
		try{
			if(schedule != null){
				jsonObject.put("key", schedule.key);
				jsonObject.put("searchDateString", WelfareUtil.getDateString(schedule.startDate));
			}
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(ClConst.API_SCHEDULE_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(ClConst.API_SCHEDULE_DETAIL.equals(url)){
			onLoadScheduleDetailSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	protected void onLoadScheduleDetailSuccess(JSONObject response){
		try{
			schedule = LoganSquare.parse(response.optString("schedule"), ScheduleModel.class);
			rooms = LoganSquare.parseList(response.optString("rooms"), RoomModel.class);
			users = LoganSquare.parseList(response.optString("users"), UserModel.class);
			categories = LoganSquare.parseList(response.optString("categories"), CategoryModel.class);
			if(getView() != null) inflateWithData(txtRoom, txtCategory, rooms, categories, schedule);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	protected void inflateWithData(ChiaseTextView txtRoom, ChiaseTextView txtCategory, List<RoomModel> rooms, List<CategoryModel> categories, ScheduleModel schedule){

		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), new JSONObject(gson.toJson(schedule)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		Map<String, String> scopes = getPublicityMap();

		if(!CCStringUtil.isEmpty(schedule.key)){
			txtRoom.setText(getRoomName(rooms, schedule.roomId));
			txtRoom.setValue(schedule.roomId);
			swtAllDay.setChecked(CCBooleanUtil.checkBoolean(schedule.isAllDay));

			CategoryModel categoryModel = ClUtil.findCategory4Id(categories, schedule.categoryId);
			if(categoryModel != null){
				txtCategory.setText(categoryModel.categoryName);
				txtCategory.setValue(schedule.categoryId);
				// txtCategory.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));
			}

			txtScope.setText(scopes.get(schedule.scheduleType));
			txtScope.setValue(schedule.scheduleType);

			showJoinUserList();

			// set time
			txtStartDate.setText(WelfareUtil.getDateString(schedule.startDate));
			txtStartTime.setText(schedule.startTime);
			txtEndDate.setText(WelfareUtil.getDateString(schedule.endDate));
			txtEndTime.setText(schedule.endTime);

			// show repeat data
			if(ClRepeatUtil.isRepeat(schedule.repeatType)){
				txtRepeat.setText(ClRepeatUtil.getRepeatDescription(schedule, activity));
			}else{
				txtRepeat.setText(getString(R.string.chiase_common_none));
			}
		}else{
			txtRoom.setText(rooms.get(0).roomName);
			txtRoom.setValue(rooms.get(0).key);
			txtCategory.setText(categories.get(0).categoryName);
			txtCategory.setValue(categories.get(0).key);
			// txtCategory.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(categories.get(0).categoryColor)));
			txtScope.setValue(ClConst.SCHEDULE_TYPE_PUB);
			txtScope.setText(scopes.get(ClConst.SCHEDULE_TYPE_PUB));
		}

		lnrEndDate.setVisibility(View.VISIBLE);
		if(swtAllDay.isChecked()){
			txtStartTime.setVisibility(View.INVISIBLE);
			txtEndTime.setVisibility(View.INVISIBLE);
			if(!CCStringUtil.isEmpty(schedule.key) && !schedule.isRepeat(schedule)){
				txtEndDate.setVisibility(View.VISIBLE);
			}else{
				lnrEndDate.setVisibility(View.GONE);
			}
		}else{
			txtStartTime.setVisibility(View.VISIBLE);
			txtEndTime.setVisibility(View.VISIBLE);
			txtEndDate.setVisibility(View.INVISIBLE);
		}
	}

	public static String getRoomName(List<RoomModel> rooms, String roomId){
		for(RoomModel roomModel : rooms){
			if(roomModel.key.equals(roomId)){
				return roomModel.roomName;
			}
		}
		return "";
	}

	protected void showJoinUserList(){
		if(lnrUserList != null){
			lnrUserList.show(schedule.scheduleJoinUsers, (int)getResources().getDimension(R.dimen.margin_30dp));
		}
	}

	protected List<ApiObjectModel> getCalendarHolders(List<CalendarModel> calendars){
		List<ApiObjectModel> apiObjectModels = new ArrayList<>();
		for(CalendarModel calendarModel : calendars){
			ApiObjectModel apiObjectModel = new ApiObjectModel(calendarModel.key, calendarModel.calendarName);
			apiObjectModels.add(apiObjectModel);
		}
		return apiObjectModels;
	}

	protected List<UserModel> getAllCalendarUsers(List<CalendarModel> calendars, String calendarId){
		if(!CCStringUtil.isEmpty(calendarId)){
			for(CalendarModel calendarModel : calendars){
				if(calendarModel.key.equals(calendarId)){
					// activeCalendar = calendarModel;
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

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			// sendUpdatedRequest();
			break;
		default:
			break;
		}
	}

	public Map<String, String> getPublicityMap(){
		Map<String, String> scopes = new LinkedHashMap<>();
		scopes.put(ClConst.SCHEDULE_TYPE_PUB, getString(R.string.public_str));
		scopes.put(ClConst.SCHEDULE_TYPE_PRI, getString(R.string.private_str));
		scopes.put(ClConst.SCHEDULE_TYPE_PRI_COM, getString(R.string.complete_private));
		return scopes;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void setSchedule(ScheduleModel schedule){
		this.schedule = schedule;
	}
}

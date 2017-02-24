package trente.asia.calendar.services.calendar;

import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.graphics.Color;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * AbstractScheduleFragment
 *
 * @author TrungND
 */
public class AbstractScheduleFragment extends AbstractClFragment{

	protected ScheduleModel			schedule;
	protected List<CalendarModel>	calendars;
	protected List<ApiObjectModel>	calendarHolders;
	protected UserListLinearLayout	lnrUserList;

	protected List<ApiObjectModel>	rooms;
	protected ChiaseTextView		txtRoom;
	protected ChiaseTextView		txtStartTime;
	protected ChiaseTextView		txtEndTime;
	protected ChiaseTextView		txtStartDate;
	protected ChiaseTextView		txtEndDate;
	protected ChiaseTextView		txtCalendar;

	protected List<ApiObjectModel>	categories;
	protected ChiaseTextView		txtCategory;
	protected SwitchCompat			swtAllDay;

	@Override
	protected void initView(){
		super.initView();

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);

		txtRoom = (ChiaseTextView)getView().findViewById(R.id.txt_id_meeting_room);
		txtCalendar = (ChiaseTextView)getView().findViewById(R.id.txt_id_calendar);
		txtCategory = (ChiaseTextView)getView().findViewById(R.id.txt_id_category);
		lnrUserList = (UserListLinearLayout)getView().findViewById(R.id.lnr_fragment_pager_container_user_list);
		swtAllDay = (SwitchCompat)getView().findViewById(R.id.swt_id_all_day);

		txtStartDate = (ChiaseTextView)getView().findViewById(R.id.txt_id_start_date);
		txtEndDate = (ChiaseTextView)getView().findViewById(R.id.txt_id_end_date);
		txtStartTime = (ChiaseTextView)getView().findViewById(R.id.txt_id_start_time);
		txtEndTime = (ChiaseTextView)getView().findViewById(R.id.txt_id_end_time);
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

	protected void onLoadScheduleDetailSuccess(JSONObject response){
		schedule = CCJsonUtil.convertToModel(response.optString("schedule"), ScheduleModel.class);
		rooms = CCJsonUtil.convertToModelList(response.optString("rooms"), ApiObjectModel.class);
		calendars = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		calendarHolders = getCalendarHolders(calendars);
		categories = CCJsonUtil.convertToModelList(response.optString("categories"), ApiObjectModel.class);

		inflateWithData(txtRoom, txtCalendar, txtCategory, rooms, calendars, categories, schedule);
	}

	protected void inflateWithData(ChiaseTextView txtRoom, ChiaseTextView txtCalendar, ChiaseTextView txtCategory, List<ApiObjectModel> rooms, List<CalendarModel> calendars, List<ApiObjectModel> categories, ScheduleModel schedule){

		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), new JSONObject(gson.toJson(schedule)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		if(!CCStringUtil.isEmpty(schedule.key)){
			txtRoom.setText(convertList2Map(rooms).get(Integer.parseInt(schedule.roomId)));
			txtRoom.setValue(schedule.roomId);
			txtCalendar.setText(convertList2Map(getCalendarHolders(calendars)).get(Integer.parseInt(schedule.calendarId)));
			txtCalendar.setValue(schedule.calendarId);
			txtCategory.setValue(schedule.categoryId);
			if(!CCStringUtil.isEmpty(schedule.categoryId)){
				txtCategory.setTextColor(Color.parseColor("#" + schedule.categoryId));
			}
			txtCategory.setText(convertList2Map(categories).get(schedule.categoryId));
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

	protected void onChangeCalendar(String calendarId){
		List<UserModel> calendarUsers = getAllCalendarUsers(calendars, calendarId);
		if(!CCCollectionUtil.isEmpty(calendarUsers)){
			lnrUserList.show(calendarUsers, (int)getResources().getDimension(R.dimen.margin_30dp));
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
			// sendUpdatedRequest();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void setSchedule(ScheduleModel schedule){
		this.schedule = schedule;
	}
}

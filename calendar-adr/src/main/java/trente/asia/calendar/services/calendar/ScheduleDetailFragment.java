package trente.asia.calendar.services.calendar;

import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.HorizontalUserListView;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * ScheduleDetailFragment
 *
 * @author VietNH
 */
public class ScheduleDetailFragment extends AbstractClFragment{

	private ScheduleModel			schedule;
	private HorizontalUserListView	horizontalUserListView;
	private List<CalendarModel>		calendars;
	private List<ApiObjectModel>	rooms;
	private ChiaseTextView			txtRoom;
	private ChiaseTextView			txtCalendar;
	private ChiaseTextView			txtCategory;
	private List<ApiObjectModel>	categories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_schedule_detail, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, "Weekly", null);
		horizontalUserListView = (HorizontalUserListView)getView().findViewById(R.id.view_horizontal_user_list);
		txtRoom = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_schedule_detail_form);
		txtCalendar = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_schedule_detail_calendar);
		txtCategory = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_schedule_detail_category);
	}

	@Override
	protected void initData(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", schedule.key);
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
		categories = CCJsonUtil.convertToModelList(response.optString("categories"), ApiObjectModel.class);
		calendars = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);

		inflateWithData((ViewGroup)getView(), txtRoom, txtCalendar, txtCategory, rooms, calendars, categories, schedule);

		List<UserModel> joinUserList = getJoinedUserModels(schedule, schedule.calendar.calendarUsers);
		horizontalUserListView.show(joinUserList, schedule.calendar.calendarUsers, true, 32, 10);

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setImageResource(R.drawable.abc_btn_check_material);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);
	}

	public static void inflateWithData(ViewGroup view, ChiaseTextView txtRoom, ChiaseTextView txtCalendar, ChiaseTextView txtCategory, List<ApiObjectModel> rooms, List<CalendarModel> calendars, List<ApiObjectModel> categories, ScheduleModel schedule){

		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject((ViewGroup)view.findViewById(R.id.lnr_id_content), new JSONObject(gson.toJson(schedule)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		if(!CCStringUtil.isEmpty(schedule.key)){
			txtRoom.setText(convertList2Map(rooms).get(Integer.parseInt(schedule.roomId)));
			txtRoom.setValue(schedule.roomId);
			txtCalendar.setText(convertList2Map(ScheduleFormFragment.getCalendarHolders(calendars)).get(Integer.parseInt(schedule.calendarId)));
			txtCalendar.setValue(schedule.calendarId);
			txtCategory.setValue(schedule.categoryId);
			if(!CCStringUtil.isEmpty(schedule.categoryId)){
				txtCategory.setTextColor(Color.parseColor("#" + schedule.categoryId));
			}
			txtCategory.setText(convertList2Map(categories).get(schedule.categoryId));
		}
	}

	public static List<UserModel> getJoinedUserModels(ScheduleModel schedule, List<UserModel> userModels){
		List<UserModel> joinUsers = new ArrayList<>();
		if(userModels != null && !CCStringUtil.isEmpty(schedule.joinUsers)){
			for(String userId : schedule.joinUsers.split(",")){
				UserModel userModel = UserModel.getUserModel(userId, userModels);
				if(userModel != null){
					joinUsers.add(userModel);
				}
			}
		}
		return joinUsers;
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
		default:
			break;
		}
	}

	private void gotoScheduleFormFragment(){
		ScheduleFormFragment fragment = new ScheduleFormFragment();
		// fragment.setRooms(rooms);
		fragment.setSchedule(schedule);
		gotoFragment(fragment);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		horizontalUserListView = null;
	}

	public void setSchedule(ScheduleModel schedule){
		this.schedule = schedule;
	}
}

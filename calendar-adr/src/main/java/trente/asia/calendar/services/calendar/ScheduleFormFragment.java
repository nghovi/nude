package trente.asia.calendar.services.calendar;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.HorizontalUserListView;
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
	private List<CalendarModel>		calendars;
	private Map<String, String>		rooms;
	private ChiaseTextView			txtRoom;

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
		initHeader(R.drawable.wf_back_white, "Weekly", null);

		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), new JSONObject(gson.toJson(schedule)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setImageResource(R.drawable.abc_btn_radio_material);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);

		txtRoom = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_schedule_form_room);
		if(schedule != null){
			txtRoom.setText(rooms.get(schedule.roomId));
			txtRoom.setValue(schedule.roomId);
		}
		getView().findViewById(R.id.lnr_fragment_schedule_form_room).setOnClickListener(this);
		final List<UserModel> joinedUserList = ScheduleDetailFragment.getJoinedUserModels(schedule, schedule.calendar.calendarUsers);
		horizontalUserListView = (HorizontalUserListView)getView().findViewById(R.id.view_horizontal_user_list);
		horizontalUserListView.post(new Runnable() {

			@Override
			public void run(){
				horizontalUserListView.inflateWith(joinedUserList, schedule.calendar.calendarUsers, false, 32, 10);
			}
		});
	}

	private void showChooseRoomDialog(){
		if(dlgChooseRoom != null){
			dlgChooseRoom.show();
		}else{
			dlgChooseRoom = new ChiaseListDialog(getContext(), "Select venue", rooms, txtRoom, null);
			dlgChooseRoom.show();
		}
	}

	@Override
	protected void initData(){
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
		default:
			break;
		}
	}

	private void sendUpdatedRequest(){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), null);
		String joinUsers = horizontalUserListView.getUserListString();
		boolean isDayPeriod = true;// / TODO: 2/10/2017
		try{
			if(schedule != null && !CCStringUtil.isEmpty(schedule.key)){
				jsonObject.put("key", schedule.key);
				jsonObject.put("calendarId", schedule.calendarId);
				jsonObject.put("roomId", txtRoom.getValue());
				jsonObject.put("joinUsers", joinUsers);
				jsonObject.put("isDayPeriod", isDayPeriod);
				jsonObject.put("scheduleColor", schedule.scheduleColor);
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
		Toast.makeText(activity, "Updated successfully", Toast.LENGTH_LONG).show();
		// // TODO: 2/9/2017 back to detail screen ?
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void setSchedule(ScheduleModel schedule){
		this.schedule = schedule;
	}

	public void setRooms(List<ApiObjectModel> rooms){
		this.rooms = WelfareFormatUtil.convertList2Map(rooms);
	}
}

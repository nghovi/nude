package trente.asia.calendar.services.calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.HorizontalUserListView;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * ScheduleDetailFragment
 *
 * @author VietNH
 */
public class ScheduleFormFragment extends AbstractClFragment{

	private ScheduleModel			schedule;
	private HorizontalUserListView	horizontalUserListView;

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
		horizontalUserListView = (HorizontalUserListView)getView().findViewById(R.id.view_horizontal_user_list);

		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), new JSONObject(gson.toJson(schedule)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		List<UserModel> joinedUserList = getJoinedUserModels(schedule, schedule.calendar.calendarUsers);
		horizontalUserListView.inflateWith(joinedUserList, schedule.calendar.calendarUsers, false);

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setImageResource(R.drawable.abc_btn_radio_material);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);

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
				jsonObject.put("roomId", schedule.roomId);
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

	private List<UserModel> getJoinedUserModels(ScheduleModel schedule, List<UserModel> userModels){
		List<UserModel> joinUsers = new ArrayList<>();
		if(userModels != null && !CCStringUtil.isEmpty(schedule.joinUsers)){
			for(String userId : schedule.joinUsers.split(",")){
				joinUsers.add(UserModel.getUserModel(userId, userModels));
			}
		}
		return joinUsers;
	}
}

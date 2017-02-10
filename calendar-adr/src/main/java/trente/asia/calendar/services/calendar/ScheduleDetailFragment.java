package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
public class ScheduleDetailFragment extends AbstractClFragment{

	private ScheduleModel			schedule;
	private HorizontalUserListView	horizontalUserListView;

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
	}

	@Override
	protected void initData(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", schedule.key);
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

		List<UserModel> joinUserList = getJoinedUserModels(schedule, CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class));
		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject((ViewGroup)getView().findViewById(R.id.lnr_id_content), new JSONObject(gson.toJson(schedule)));
		}catch(JSONException e){
			e.printStackTrace();
		}

		horizontalUserListView.inflateWith(joinUserList, schedule.calendar.calendarUsers, true);

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setImageResource(R.drawable.abc_btn_check_material);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);
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

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			ScheduleFormFragment fragment = new ScheduleFormFragment();
			fragment.setSchedule(schedule);
			gotoFragment(fragment);
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

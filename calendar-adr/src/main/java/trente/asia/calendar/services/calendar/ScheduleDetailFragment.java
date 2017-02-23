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
import trente.asia.calendar.commons.utils.ClUtil;
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
public class ScheduleDetailFragment extends AbstractScheduleFragment{

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
	}

    @Override
	protected void onLoadScheduleDetailSuccess(JSONObject response){
        super.onLoadScheduleDetailSuccess(response);
		List<UserModel> joinUserList = ClUtil.getJoinedUserModels(schedule, schedule.calendar.calendarUsers);
		lnrUserList.show(joinUserList, (int)getResources().getDimension(R.dimen.margin_30dp));

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setImageResource(R.drawable.abc_btn_check_material);
		imgRightIcon.setVisibility(View.VISIBLE);
		imgRightIcon.setOnClickListener(this);
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
		lnrUserList = null;
	}
}

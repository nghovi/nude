package trente.asia.calendar.services.calendar;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.welfare.adr.activity.WelfareActivity;

/**
 * ScheduleDetailFragment
 *
 * @author VietNH
 */
public class ScheduleDetailFragment extends AbstractScheduleFragment{

	private TextView	txtScheduleName;
	private TextView	txtScheduleUrl;
	private TextView	txtScheduleNote;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_schedule_detail, container, false);
		}else{
			String isDelete = CCStringUtil.toString(((WelfareActivity)activity).dataMap.get(ClConst.ACTION_SCHEDULE_DELETE));
			if(CCConst.YES.equals(isDelete)){
				((WelfareActivity)activity).dataMap.clear();
				((WelfareActivity)activity).isInitData = true;
				getFragmentManager().popBackStack();
			}
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.fragment_schedule_detail_title), null);

		txtScheduleName = (TextView)getView().findViewById(R.id.txt_id_schedule_name);
		txtScheduleUrl = (TextView)getView().findViewById(R.id.txt_id_schedule_url);
		txtScheduleNote = (TextView)getView().findViewById(R.id.txt_id_schedule_note);
	}

	@Override
	protected void onLoadScheduleDetailSuccess(JSONObject response){
		super.onLoadScheduleDetailSuccess(response);

		txtScheduleName.setText(schedule.scheduleName);
		txtScheduleNote.setText(schedule.scheduleNote);
		if(!CCStringUtil.isEmpty(schedule.scheduleUrl)){
			txtScheduleUrl.setText(schedule.scheduleUrl);
			getView().findViewById(R.id.lnr_id_schedule_url).setOnClickListener(this);
		}

		// check user is owner
		if(myself.key.equals(schedule.owner.key)){
			ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
			imgRightIcon.setImageResource(R.drawable.cl_action_edit);
			imgRightIcon.setVisibility(View.VISIBLE);
			imgRightIcon.setOnClickListener(this);
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
			gotoScheduleFormFragment();
			break;
		case R.id.lnr_id_schedule_url:
			gotoBrowser(schedule.scheduleUrl);
			break;
		default:
			break;
		}
	}

	private void gotoScheduleFormFragment(){
		ScheduleFormFragment fragment = new ScheduleFormFragment();
		fragment.setSchedule(schedule);
		gotoFragment(fragment);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		lnrUserList = null;
	}
}

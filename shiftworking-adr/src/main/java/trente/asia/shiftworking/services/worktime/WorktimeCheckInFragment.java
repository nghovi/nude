package trente.asia.shiftworking.services.worktime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractLocationFragment;
import trente.asia.shiftworking.services.transit.WorkTransitListFragment;
import trente.asia.shiftworking.services.worktime.listener.ItemWorkTimeClickListener;
import trente.asia.shiftworking.services.worktime.model.ProjectModel;
import trente.asia.shiftworking.services.worktime.model.WorkingTimeModel;
import trente.asia.shiftworking.services.worktime.view.WorkTimeAdapter;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class WorktimeCheckInFragment extends AbstractLocationFragment{

	private ListView					lsvWorkTime;
	private TextView					txtCheckInTime;
	private TextView					txtCheckInDate;

	private Button						btnCheckIn;
	private TextView					txtProjectName;
	private TextView					txtProjectLocation;
	private ImageView					imgRightIcon;
	private ImageView					imgRightSubIcon;

	private ProjectModel				activeProject;
	private Timer						mTimer;
	private LinearLayout				lnrProjectInfo;
	private LinearLayout				lnrNoProject;
	private WorkingTimeModel			activeWorkingTime;

	private ItemWorkTimeClickListener	itemWorkTimeClickListener	= new ItemWorkTimeClickListener() {

																		@Override
																		public void onItemModifyListener(WorkingTimeModel item){
																			activeWorkingTime = item;
																			modifyWorkTime(item);
																		}

																		@Override
																		public void onItemDeleteListener(WorkingTimeModel item){
																			activeWorkingTime = item;
																			deleteWorkTime();
																		}
																	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_check_in, container, false);
		}else{
			((WelfareActivity)activity).isInitData = true;
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_work_time;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(null, myself.userName, null);

		lsvWorkTime = (ListView)getView().findViewById(R.id.lsv_id_work_time);
		txtCheckInTime = (TextView)getView().findViewById(R.id.txt_id_check_in_time);
		txtCheckInDate = (TextView)getView().findViewById(R.id.txt_id_check_in_date);
		btnCheckIn = (Button)getView().findViewById(R.id.btn_id_check_in);
		imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightSubIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_sub_icon);
		txtProjectName = (TextView)getView().findViewById(R.id.txt_id_project_name);
		txtProjectLocation = (TextView)getView().findViewById(R.id.txt_id_project_location);

		lnrProjectInfo = (LinearLayout)getView().findViewById(R.id.lnr_id_project_info);
		lnrNoProject = (LinearLayout)getView().findViewById(R.id.lnr_id_no_project);

		btnCheckIn.setOnClickListener(this);
		imgRightIcon.setOnClickListener(this);
		imgRightSubIcon.setOnClickListener(this);

		setCheckInTime();
	}

	@Override
	public void onResume(){
		super.onResume();

		Calendar calendar = Calendar.getInstance();
		int delayTime = 60 - calendar.get(Calendar.SECOND);
		if(mTimer == null) mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run(){
				activity.runOnUiThread(new Runnable() {

					public void run(){
						setCheckInTime();
					}
				});
			}
		}, delayTime * 1000, WelfareConst.ONE_MINUTE_TIME_LOAD);
	}

	private void setCheckInTime(){
		Date date = new Date();
		txtCheckInDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_WEEKDAY, date));
		txtCheckInTime.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, date));
	}

	@Override
	protected void initData(){
		loadProjectList();
	}

	private void loadProjectList(){
		JSONObject jsonObject = new JSONObject();
		try{
			Date date = new Date();
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date));
			jsonObject.put("userId", prefAccUtil.getUserPref().key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_PRO_0001, jsonObject, true);
	}

	private void loadCheckInList(){
		JSONObject jsonObject = new JSONObject();
		try{
			Date date = new Date();
			jsonObject.put("workDate", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date));
			jsonObject.put("projectId", activeProject.key);
			jsonObject.put("userId", prefAccUtil.getUserPref().key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_CKI_0002, jsonObject, false);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			if(WfUrlConst.WF_PRO_0001.equals(url)){
				List<ProjectModel> lstProject = CCJsonUtil.convertToModelList(response.optString("projects"), ProjectModel.class);
				if(!CCCollectionUtil.isEmpty(lstProject)){
					lnrProjectInfo.setVisibility(View.VISIBLE);
					lnrNoProject.setVisibility(View.GONE);
					activeProject = lstProject.get(0);
					txtProjectName.setText(CCStringUtil.toString(activeProject.projectName));
					txtProjectLocation.setText(CCStringUtil.toString(activeProject.projectLocation));

					imgRightIcon.setVisibility(View.VISIBLE);
					imgRightSubIcon.setVisibility(View.VISIBLE);
					loadCheckInList();
				}else{
					btnCheckIn.setEnabled(false);
				}
			}
			if(WfUrlConst.WF_CKI_0002.equals(url)){
				List<WorkingTimeModel> lstWorkingTime = CCJsonUtil.convertToModelList(response.optString("checkins"), WorkingTimeModel.class);
				WorkTimeAdapter adapter = new WorkTimeAdapter(activity, lstWorkingTime, itemWorkTimeClickListener);
				lsvWorkTime.setAdapter(adapter);

				// show unread transit, unread notice offer
				String noticeCount = response.optString("noticeCount");
				String transitCount = response.optString("transitCount");
				if(!CCStringUtil.isEmpty(noticeCount) && !CCConst.NONE.equals(noticeCount)){
					TextView txtUnreadNotice = (TextView)getView().findViewById(R.id.txt_id_unread_notice_send);
					txtUnreadNotice.setVisibility(View.VISIBLE);
					txtUnreadNotice.setText(noticeCount);
				}
				if(!CCStringUtil.isEmpty(transitCount) && !CCConst.NONE.equals(transitCount)){
					TextView txtUnreadTransit = (TextView)getView().findViewById(R.id.txt_id_unread_transit);
					txtUnreadTransit.setVisibility(View.VISIBLE);
					txtUnreadTransit.setText(transitCount);
				}
			}else{
				super.successLoad(response, url);
			}
		}
	}

	private void checkIn(String location, String longitude, String latitude){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("projectId", activeProject.key);
			jsonObject.put("location", location);
			jsonObject.put("gpsLongtitude", longitude);
			jsonObject.put("gpsLatitude", latitude);
			jsonObject.put("userId", prefAccUtil.getUserPref().key);
			jsonObject.put("workDate", CCFormatUtil.formatDate(new Date()));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_CKI_0001, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_CKI_0001.equals(url)){
			loadCheckInList();
		}else if(WfUrlConst.WF_CKI_0003.equals(url)){
			loadCheckInList();
		}else if(WfUrlConst.WF_NOTICE_0002.equals(url)){
			Toast.makeText(activity, getString(R.string.sw_work_time_modify_request_success), Toast.LENGTH_LONG).show();
		}else{
			super.successUpdate(response, url);
		}
	}

	private void deleteWorkTime(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeWorkingTime.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_CKI_0003, jsonObject, true);
	}

	private void modifyWorkTime(WorkingTimeModel item){
		JSONObject jsonObject = new JSONObject();
		int reasonStringId = R.string.sw_work_time_modify_request;
		switch(item.workingType){
		case SwConst.SW_WORK_TIME_TYPE_UNDEFINED:
			reasonStringId = R.string.sw_work_time_modify_request_undefined;
			break;
		case SwConst.SW_WORK_TIME_TYPE_START_TIME:
			reasonStringId = R.string.sw_work_time_modify_request_start;
			break;
		case SwConst.SW_WORK_TIME_TYPE_END_TIME:
			reasonStringId = R.string.sw_work_time_modify_request_end;
			break;
		default:
			break;
		}
		try{
			jsonObject.put("projectId", activeProject.key);
			jsonObject.put("noticeType", SwConst.SW_WORK_NOTICE_TYPE_MODIFY);
			jsonObject.put("reason", getString(reasonStringId, activeWorkingTime.timeLog));
			jsonObject.put("deptId", myself.dept.key);
			jsonObject.put("userId", myself.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_NOTICE_0002, jsonObject, true);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			WorknoticeOfferFragment fragment = new WorknoticeOfferFragment();
			// fragment.setActiveProject(activeProject);
			gotoFragment(fragment);
			break;
		case R.id.img_id_header_right_sub_icon:
			WorkTransitListFragment transitFragment = new WorkTransitListFragment();
			gotoFragment(transitFragment);
			break;
		case R.id.btn_id_check_in:
			getLocation();
			break;
		default:
			break;
		}
	}

	@Override
	protected void successLocation(){
		Location lastKnownLocation = WelfareUtil.getLocation(activity);
		if(lastKnownLocation != null){
			String latitude = CCStringUtil.toString(lastKnownLocation.getLatitude());
			String longitude = CCStringUtil.toString(lastKnownLocation.getLongitude());
			String address = WelfareUtil.getAddress4Location(activity, lastKnownLocation);
			checkIn(address, longitude, latitude);
		}else{
			Toast.makeText(activity, getString(R.string.chiase_common_not_working_gps), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}

		lsvWorkTime = null;
		txtCheckInTime = null;
		btnCheckIn = null;
		txtProjectName = null;
		txtProjectLocation = null;
		activeProject = null;
		lnrProjectInfo = null;
		lnrNoProject = null;
	}
}

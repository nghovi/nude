package nguyenhoangviet.vpcorp.dailyreport.services.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import nguyenhoangviet.vpcorp.dailyreport.DRConst;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.fragments.AbstractDRFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.activities.model.ActivityModel;
import nguyenhoangviet.vpcorp.dailyreport.services.activities.view.ActivityListAdapter;
import nguyenhoangviet.vpcorp.dailyreport.services.report.MyReportFragment;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;
import nguyenhoangviet.vpcorp.welfare.adr.view.WfSpinner;

/**
 * Created by viet on 2/15/2016.
 */
public class ActivityFragment extends AbstractDRFragment{

	private WfSpinner			wfSpinnerActionUser;
	private WfSpinner			wfSpinnerReportUser;
	private ListView			lstView;
	private List<ActivityModel>	activities		= new ArrayList<>();

	private List<UserModel>		reportUsers		= new ArrayList<>();
	private List<UserModel>		actionUsers		= new ArrayList<>();
	private UserModel			selectedReportUser;
	private UserModel			selectedActionUser;

	private boolean				isBuildSpinner	= false;
	private ActivityListAdapter	mAdapter;
	private List<UserModel>		userModels;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_activity;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_ap;
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_activities_title), null);

		wfSpinnerReportUser = (WfSpinner)getView().findViewById(R.id.fragment_other_report_spn_dept);
		wfSpinnerActionUser = (WfSpinner)getView().findViewById(R.id.fragment_other_report_spn_user);

		mAdapter = new ActivityListAdapter(activity, R.layout.item_activity, activities);
		lstView = (ListView)getView().findViewById(R.id.fragment_activity_list);
		lstView.setAdapter(mAdapter);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				ActivityModel selectedActivity = activities.get(i);
				MyReportFragment.gotoReportDetailFragment(activity, selectedActivity.report);
			}
		});
	}

	@Override
	public void initData(){
		loadActivities();
	}

	public void loadActivities(){
		JSONObject jsonObject = new JSONObject();
		try{
			if(selectedReportUser != null){
				jsonObject.put("targetReportUser", selectedReportUser.key);
			}
			if(selectedActionUser != null){
				jsonObject.put("targetUserId", selectedActionUser.key);
			}
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestLoad(DRConst.API_REPORT_ACTIVITY, jsonObject, true);
	}

	protected void successLoad(JSONObject response, String url){
		List<ActivityModel> lstActivity = CCJsonUtil.convertToModelList(response.optString("activities"), ActivityModel.class);
		userModels = CCJsonUtil.convertToModelList(response.optString("users"), UserModel.class);
		activities.clear();
		activities.addAll(lstActivity);
		if(!isBuildSpinner){
			buildSpinners();
		}
		mAdapter.notifyDataSetChanged();
	}

	private void buildSpinners(){
		buildReportUsers();
		buildActionUsers();
		selectedReportUser = reportUsers.size() > 0 ? reportUsers.get(0) : null;
		selectedActionUser = actionUsers.size() > 0 ? actionUsers.get(0) : null;
		wfSpinnerReportUser.setupLayout(getString(R.string.spinner_name_report_user), WelfareUtil.convert2UserName(reportUsers), 0, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				UserModel selectedItem = reportUsers.get(selectedPosition);
				if(!selectedItem.key.equals(selectedReportUser.key)){
					selectedReportUser = selectedItem;
					loadActivities();
				}
			}
		}, false);

		wfSpinnerActionUser.setupLayout(getString(R.string.spinner_name_action_user), WelfareUtil.convert2UserName(actionUsers), 0, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				UserModel selectedItem = actionUsers.get(selectedPosition);
				if(!selectedItem.key.equals(selectedActionUser.key)){
					selectedActionUser = selectedItem;
					loadActivities();
				}
			}
		}, true);
		isBuildSpinner = true;
	}

	private void buildReportUsers(){
		for(UserModel userModel : userModels){
			reportUsers.add(userModel);
		}
		reportUsers.add(0, new UserModel(CCConst.NONE, getString(R.string.str_all)));
	}

	private void buildActionUsers(){
		for(UserModel userModel : userModels){
			actionUsers.add(userModel);
		}
		actionUsers.add(0, new UserModel(CCConst.NONE, getString(R.string.str_all)));
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		wfSpinnerActionUser = null;
		wfSpinnerReportUser = null;
		lstView = null;
		activities = null;

		reportUsers = null;
		actionUsers = null;
		selectedReportUser = null;
		selectedActionUser = null;
		mAdapter = null;
	}

}

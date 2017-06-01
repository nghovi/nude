package trente.asia.dailyreport.services.kpigraph;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpigraph.model.ReportMonthlyGoalModel;
import trente.asia.dailyreport.services.kpigraph.model.ReportMonthyGraphHolderModel;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class KPIMonthlyGoalGraphFragment extends AbstractDRFragment implements View.OnClickListener{

	private WfSpinner							wfSpinnerGoals;
	private RelativeLayout						lnrHaveGoals;
	private TextView							lnrNoneGoals;

	private TextView							txtViewGraphUser;
	private TextView							txtViewGraphMonth;

	private List<ReportMonthlyGoalModel>		goalModels;
	private List<ReportMonthyGraphHolderModel>	goalHolderModels;
	private ReportMonthlyGoalModel				selectedActionGoals;
	private String								graphTitle;

	public void setGraphTitle(String graphTitle){
		this.graphTitle = graphTitle;
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_kpimonthly_goal_graph;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_KPI;
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.dlr_monthly_goal_title), null);

		wfSpinnerGoals = (WfSpinner)getView().findViewById(R.id.spn_kpi_goal);
		lnrHaveGoals = (RelativeLayout)getView().findViewById(R.id.lnr_kpi_graph);
		lnrNoneGoals = (TextView)getView().findViewById(R.id.txt_none_kpi_graph);

		txtViewGraphUser = (TextView)getView().findViewById(R.id.txt_graph_by_user);
		txtViewGraphUser.setOnClickListener(this);
		txtViewGraphMonth = (TextView)getView().findViewById(R.id.txt_graph_by_month);
		txtViewGraphMonth.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		loadMonthlyGoals();
	}

	private void loadMonthlyGoals(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, jsonObject, true);
	}

	public void loadKPIGraph(String execType){
		JSONObject jsonObject = new JSONObject();
		try{
			// if (DRConst.BY_USER.equals(execType)) {
			// jsonObject.put("targetGoalId", selectedActionGoals.key);
			// jsonObject.put("execType", execType);
			// } else {

			/* Following API Document */
			jsonObject.put("targetGoalId", selectedActionGoals.parentId);
			jsonObject.put("execType", execType);

			// jsonObject.put("baseId", selectedActionGoals.parentId);
			// }
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(DRConst.API_REPORT_GRAPH_RATE, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)){
			goalModels = CCJsonUtil.convertToModelList(response.optString("goals"), ReportMonthlyGoalModel.class);
			if(CCCollectionUtil.isEmpty(goalModels)){
				lnrNoneGoals.setVisibility(View.VISIBLE);
			}else{
				lnrHaveGoals.setVisibility(View.VISIBLE);
				selectedActionGoals = goalModels.size() > 0 ? goalModels.get(0) : null;
				buildSpinner();
			}
		}else if(DRConst.API_REPORT_GRAPH_RATE.equals(url)){
			goalHolderModels = CCJsonUtil.convertToModelList(response.optString("results"), ReportMonthyGraphHolderModel.class);
			if(!CCCollectionUtil.isEmpty(goalHolderModels)){
				if(DRConst.BY_USER.equals(graphTitle)){
					KPIGraphByUserFragment byUserFragment = new KPIGraphByUserFragment();
					byUserFragment.setModelList(goalHolderModels);
					gotoFragment(byUserFragment);
				}else if(DRConst.BY_MONTH.equals(graphTitle)){
					KPIGraphByMonthFragment byMonthFragment = new KPIGraphByMonthFragment();
					byMonthFragment.setModelList(goalHolderModels);
					gotoFragment(byMonthFragment);
				}
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void buildSpinner(){
		wfSpinnerGoals.setupLayout("Goals", getGoalDisplayedValue(goalModels), 0, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				ReportMonthlyGoalModel selectedItem = goalModels.get(selectedPosition);
				if(!selectedItem.key.equals(selectedActionGoals.key)){
					selectedActionGoals = selectedItem;
				}
			}
		}, true);
	}

	private List<String> getGoalDisplayedValue(List<ReportMonthlyGoalModel> goalModels){
		List<String> values = new ArrayList<>();
		for(ReportMonthlyGoalModel goalModel : goalModels){
			values.add(goalModel.goalName);
		}
		return values;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.txt_graph_by_user:
			loadKPIGraph(DRConst.BY_USER);
			setGraphTitle(DRConst.BY_USER);
			break;
		case R.id.txt_graph_by_month:
			loadKPIGraph(DRConst.BY_MONTH);
			setGraphTitle(DRConst.BY_MONTH);
			break;
		default:
			break;
		}
	}
}

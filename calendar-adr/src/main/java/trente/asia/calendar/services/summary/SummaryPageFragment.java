package trente.asia.calendar.services.summary;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.SummaryModel;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * SummaryPageFragment
 *
 * @author VietNH
 */
public class SummaryPageFragment extends ClPageFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_summary_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void loadData(){
		loadSummaryInfo();
	}

	private void loadSummaryInfo(){
		String targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));

		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(WfUrlConst.WF_CL_WEEK_SCHEDULE, jsonObject, true);
	}

	@Override
	protected void initView(){
		super.initView();
		GraphColumn graphColumn = (GraphColumn)getView().findViewById(R.id.view_graph_column);
		SummaryModel model = new SummaryModel();
		model.categoryModelList = new ArrayList<>();
		model.categoryModelList.add(new CategoryModel("ff0000"));
		model.categoryModelList.add(new CategoryModel("00ff00"));
		model.categoryModelList.add(new CategoryModel("0000ff"));
		model.categoryModelList.add(new CategoryModel("ffcd00"));
		graphColumn.initLayout(model);
	}

	protected String getUpperTitle(){
		return getString(R.string.fragment_summary_title);
	}

	@Override
	public void onClick(View v){

	}
}

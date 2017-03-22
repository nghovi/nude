package trente.asia.calendar.services.summary;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * SummaryPageFragment
 *
 * @author VietNH
 */
public class SummaryPageFragment extends ClPageFragment{

	private static final int	SQUARE_SIZE_PX	= WelfareUtil.dpToPx(10);
	private LinearLayout		lnrGraphContainer;
	private LayoutInflater		inflater;
	private LinearLayout		lnrGraphExplain;
	private List<SummaryModel>	summaryModels	= new ArrayList<>();
	private List<CategoryModel>	categories		= new ArrayList<>();

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

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_SUMMARY.equals(url)){
			onLoadSummarySuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	private void onLoadSummarySuccess(JSONObject response){
		summaryModels = CCJsonUtil.convertToModelList(response.optString("months"), SummaryModel.class);
		categories = CCJsonUtil.convertToModelList(response.optString("categories"), CategoryModel.class);
		// buildGraphExplain();
		buildGraphColumns();
	}

	private void buildGraphColumns(){
		for(SummaryModel summaryModel : summaryModels){
			GraphColumn graphColumn = (GraphColumn)inflater.inflate(R.layout.graph_column, null);
			graphColumn.initLayout(summaryModel);
			lnrGraphContainer.addView(graphColumn);
		}
	}

	private void buildGraphExplain(){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SQUARE_SIZE_PX, SQUARE_SIZE_PX);
		params.setMargins(0, MARGIN_LEFT_RIGHT_PX, 0, 0);
		for(CategoryModel categoryModel : categories){
			LinearLayout square = new LinearLayout(getContext());
			square.setLayoutParams(params);
			square.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));

			TextView txtCategoryName = new TextView(activity);
			txtCategoryName.setText(categoryModel.categoryName);
			txtCategoryName.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));

			LinearLayout row = new LinearLayout(getContext());
			row.addView(square);
			row.addView(txtCategoryName);

			lnrGraphExplain.addView(row);
		}
	}

	private void loadSummaryInfo(){
		String targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));
			jsonObject.put("startMonthString", "2017/01");
			jsonObject.put("endMonthString", "2017/12");
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(WfUrlConst.WF_CL_SUMMARY, jsonObject, true);
	}

	@Override
	protected void initView(){
		super.initView();
		inflater = getLayoutInflater(null);
		updateHeaderTitles();
		lnrGraphContainer = (LinearLayout)getView().findViewById(R.id.lnr_graph_container);
		lnrGraphExplain = (LinearLayout)getView().findViewById(R.id.lnr_graph_explain);
	}

	protected String getUpperTitle(){
		return getString(R.string.fragment_summary_title);
	}

	@Override
	public void onClick(View v){

	}
}

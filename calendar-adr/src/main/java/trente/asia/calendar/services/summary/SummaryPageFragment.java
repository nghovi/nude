package trente.asia.calendar.services.summary;

import static trente.asia.calendar.services.summary.SummaryPagerAdapter.GRAPH_COLUMN_NUM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
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
	private LinearLayout		lnrGraphExplainLeft;
	private List<SummaryModel>	summaryModels	= new ArrayList<>();
	private List<CategoryModel>	categories		= new ArrayList<>();
	private LinearLayout		lnrGraphFooter;
	private LinearLayout		lnrGraphExplainRight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_summary_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		inflater = getLayoutInflater(null);
		pageSharingHolder.hideNavigationRightBtn();
		updateHeaderTitles();
		lnrGraphContainer = (LinearLayout)getView().findViewById(R.id.lnr_graph_container);
		lnrGraphExplainLeft = (LinearLayout)getView().findViewById(R.id.lnr_graph_explain_left);
		lnrGraphExplainRight = (LinearLayout)getView().findViewById(R.id.lnr_graph_explain_right);
		lnrGraphFooter = (LinearLayout)getView().findViewById(R.id.lnr_graph_footer_container);
	}

	@Override
	protected void loadData(){
		loadSummaryInfo();
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(ClConst.API_SUMMARY.equals(url)){
			onLoadSummarySuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	private void onLoadSummarySuccess(JSONObject response){
		try{
			summaryModels = LoganSquare.parseList(response.optString("months"), SummaryModel.class);
			categories = LoganSquare.parseList(response.optString("categories"), CategoryModel.class);
			buildGraphExplain();
			buildGraphColumns();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void buildGraphColumns(){
		lnrGraphContainer.removeAllViews();
		lnrGraphFooter.removeAllViews();
		for(final SummaryModel summaryModel : summaryModels){
			GraphColumn graphColumn = (GraphColumn)inflater.inflate(R.layout.graph_column, null);
			graphColumn.initLayout(summaryModel);
			// graphColumn.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v){
			// gotoSummaryDetail(summaryModel);
			// }
			// });
			lnrGraphContainer.addView(graphColumn);
			LinearLayout lnrMonth = buildTxtMonth(summaryModel);
			lnrGraphFooter.addView(lnrMonth);
		}
	}

	private LinearLayout buildTxtMonth(SummaryModel summaryModel){

		LinearLayout lnrText = new LinearLayout(getContext());
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
		;
		lnrText.setGravity(Gravity.CENTER);

		lnrText.setLayoutParams(param);

		TextView txtMonth = new TextView(getContext());
		txtMonth.setText(summaryModel.month);
		lnrText.addView(txtMonth);
		return lnrText;
	}

	private void gotoSummaryDetail(SummaryModel summaryModel){
		SummaryDetailFragment fragment = new SummaryDetailFragment();
		fragment.setSummaryModel(summaryModel);
		((WelfareActivity)activity).addFragment(fragment);
	}

	private void buildGraphExplain(){
		lnrGraphExplainLeft.removeAllViews();
		lnrGraphExplainRight.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SQUARE_SIZE_PX, SQUARE_SIZE_PX);
		params.setMargins(0, 0, MARGIN_LEFT_RIGHT_PX, 0);
		if(!CCStringUtil.isEmpty(categories)){
			for(int i = 0; i < categories.size(); i++){
				CategoryModel categoryModel = categories.get(i);
				LinearLayout square = new LinearLayout(getContext());
				square.setLayoutParams(params);
				square.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));

				TextView txtCategoryName = new TextView(activity);
				txtCategoryName.setText(categoryModel.categoryName);
				txtCategoryName.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(categoryModel.categoryColor)));

				LinearLayout row = new LinearLayout(getContext());
				row.setGravity(Gravity.CENTER_VERTICAL);
				row.setOrientation(LinearLayout.HORIZONTAL);
				row.addView(square);
				row.addView(txtCategoryName);
				row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				if(i % 2 == 0){
					lnrGraphExplainLeft.addView(row);
				}else{
					lnrGraphExplainRight.addView(row);
				}
			}
		}
	}

	private void loadSummaryInfo(){
		String targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
		Calendar c = CCDateUtil.makeCalendar(selectedDate);
		String endMonthStr = CCFormatUtil.formatMonth(selectedDate);
		c.add(Calendar.MONTH, 1 - GRAPH_COLUMN_NUM);
		String startMonthStr = CCFormatUtil.formatMonth(c.getTime());
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));
			jsonObject.put("startMonthString", startMonthStr);
			jsonObject.put("endMonthString", endMonthStr);
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(ClConst.API_SUMMARY, jsonObject, true);
	}

	protected String getUpperTitle(){
		return "";
		// return getString(R.string.fragment_summary_title);
	}

	@Override
	public void onClick(View v){

	}
}

package trente.asia.calendar.services.summary;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * DailyFragment
 *
 * @author VietNH
 */
public class SummaryFragment extends AbstractClFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_summary, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(null, getString(R.string.fragment_summary_title), null);
	}

	@Override
	protected void initData(){
		// loadSummaryInfo();
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
	public int getFooterItemId(){
		return R.id.lnr_view_footer_summary;
	}

	@Override
	public void onClick(View v){

	}
}

package trente.asia.shiftworking.services.requests.list;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.services.requests.detail.HolidayWorkingDetailFragment;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.models.VacationRequestModel;

public class HolidayWorkingListFragment extends AbstractListFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_holiday_working_list, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initTextFilter(TextView txtFilter) {
		txtFilter.setText(getString(R.string.sw_work_offer_list_filter, ALL + " - " + ALL));
	}

	@Override
	protected void getRequestList(JSONObject response) {
		myRequests = CCJsonUtil.convertToModelList(response.optString("myHolidayWorkOffers"), VacationRequestModel.class);
		otherRequests = CCJsonUtil.convertToModelList(response.optString("otherHolidayWorkOffers"), VacationRequestModel.class);
	}

	@Override
	protected String getApiList() {
		return SwConst.API_HOLIDAY_WORKING_LIST;
	}

	private void log(String msg){
		Log.e("HolidayWorkingList", msg);
	}


	@Override
	protected String getFilter(DeptModel dept, UserModel user, ApiObjectModel type) {
		return dept.deptName + " - " + user.userName;
	}

	@Override
	protected void gotoWorkOfferDetail(VacationRequestModel offer, String execType) {
		HolidayWorkingDetailFragment fragment = new HolidayWorkingDetailFragment();
		fragment.setActiveOfferId(offer.key);
		fragment.setExecType(execType);
		gotoFragment(fragment);
	}

	@Override
	protected boolean getEnableType() {
		return false;
	}
}

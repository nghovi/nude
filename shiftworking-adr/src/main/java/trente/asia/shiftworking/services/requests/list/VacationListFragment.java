package nguyenhoangviet.vpcorp.shiftworking.services.requests.list;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.detail.VacationDetailFragment;
import nguyenhoangviet.vpcorp.welfare.adr.models.ApiObjectModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.VacationRequestModel;

public class VacationListFragment extends AbstractListFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_vacation_list, container, false);
		}
		return mRootView;
	}

	@Override
	protected String getFilter(DeptModel dept, UserModel user, ApiObjectModel type) {
		return dept.deptName + " - " + user.userName + " - " + type.value;
	}

	@Override
	protected void gotoWorkOfferDetail(VacationRequestModel offer, String execType) {
		VacationDetailFragment fragment = new VacationDetailFragment();
		fragment.setActiveOfferId(offer.key);
		fragment.setExecType(execType);
		gotoFragment(fragment);
	}

	@Override
	protected void getRequestList(JSONObject response) {
		myRequests = CCJsonUtil.convertToModelList(response.optString("myVacationOffers"), VacationRequestModel.class);
		otherRequests = CCJsonUtil.convertToModelList(response.optString("otherVacationOffers"), VacationRequestModel.class);
		requestTypes = new ArrayList<>();
		requestTypes.add(new ApiObjectModel(CCConst.ALL, getString(R.string.chiase_common_all)));
	}

	@Override
	protected String getApiList() {
		return SwConst.API_VACATION_LIST;
	}

	@Override
	protected void initTextFilter(TextView txtFilter) {
		txtFilter.setText(getString(R.string.sw_work_offer_list_filter, ALL + " - " + ALL + " - " + ALL));
	}

	@Override
	protected boolean getEnableType() {
		return false;
	}
}

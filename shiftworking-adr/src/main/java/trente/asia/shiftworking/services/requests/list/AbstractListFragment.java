package nguyenhoangviet.vpcorp.shiftworking.services.requests.list;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnFilterListener;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnRequestAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.adapter.RequestAdapter;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.filter.RequestFilterFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.view.CommonMonthView;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.ApiObjectModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.VacationRequestModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

public abstract class AbstractListFragment extends AbstractSwFragment implements OnFilterListener,OnRequestAdapterListener{

	private RequestAdapter					adapter;
	protected List<VacationRequestModel>	myRequests;
	protected List<VacationRequestModel>	otherRequests;
	protected List<ApiObjectModel>			requestTypes;
	private List<DeptModel>					depts;
	private ListView						mLstOffer;
	protected CommonMonthView				monthView;
	private RequestAdapter					adapterOther;
	private ListView						mLstOfferOther;
	private TextView						txtFilter;

	protected String						ALL;
	protected DeptModel						selectedDept;
	protected UserModel						selectedUser;
	protected ApiObjectModel				selectedType;

	@Override
	protected void initData(){
		loadRequestList();
		requestAccountInfo();
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		ALL = getString(R.string.chiase_common_all);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void initView(){
		super.initView();
		monthView = (CommonMonthView)getView().findViewById(R.id.view_id_month);
		monthView.initialization();
		monthView.imgBack.setOnClickListener(this);
		monthView.imgNext.setOnClickListener(this);
		monthView.btnThisMonth.setOnClickListener(this);
		mLstOffer = (ListView)getView().findViewById(R.id.lst_work_offer);
		mLstOfferOther = (ListView)getView().findViewById(R.id.lst_fragment_work_offer_other);
		txtFilter = (TextView)getView().findViewById(R.id.txt_filter);
		initTextFilter(txtFilter);
		getView().findViewById(R.id.lnr_id_filter).setOnClickListener(this);
	}

	protected abstract void initTextFilter(TextView txtFilter);

	private void requestAccountInfo(){
		JSONObject param = new JSONObject();
		requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, param, true);
	}

	public void loadRequestList() {
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			if(selectedDept != null && !CCConst.ALL.equals(selectedDept.key)){
				jsonObject.put("offerDept", selectedDept.key);
			}
			if(selectedType != null && !CCConst.ALL.equals(selectedType.key)){
				jsonObject.put("overtimeType", selectedType.key);
			}

			if(selectedUser != null && !CCConst.ALL.equals(selectedUser.key)){
				jsonObject.put("targetUserId", selectedUser.key);
			}
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(getApiList(), jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getApiList().equals(url)){
			getRequestList(response);
			adapter = new RequestAdapter(activity, myRequests);
			mLstOffer.setAdapter(adapter);
			adapter.setCallback(this);
			adapter.setType(SwConst.SW_OFFER_EXEC_TYPE_VIEW);

			adapterOther = new RequestAdapter(activity, otherRequests);
			mLstOfferOther.setAdapter(adapterOther);
			adapterOther.setCallback(this);
			adapterOther.setType(SwConst.SW_OFFER_EXEC_TYPE_APR);
		}else if(WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)){
			depts = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
			DeptModel department = new DeptModel(CCConst.ALL, ALL);
			department.members = new ArrayList<>();
			UserModel user = new UserModel(CCConst.ALL, ALL);
			for(DeptModel dept : depts){
				for(UserModel member : dept.members){
					department.members.add(member);
				}
				dept.members.add(0, user);
			}
			department.members.add(0, user);
			depts.add(0, department);
			selectedDept = depts.get(0);
			selectedUser = selectedDept.members.get(0);
		}else{
			super.successLoad(response, url);
		}
	}

	protected abstract void getRequestList(JSONObject response);

	protected abstract String getApiList();

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_filter:
			gotoOfferFilterFragment();
			break;
		case R.id.btn_id_back:
			monthView.workMonth = WelfareUtil.addMonth(monthView.workMonth, -1);
			loadRequestList();
			break;
		case R.id.btn_id_next:
			monthView.workMonth = WelfareUtil.addMonth(monthView.workMonth, 1);
			loadRequestList();
			break;
		case R.id.img_id_this_month:
			monthView.workMonth = WelfareUtil.makeMonthWithFirstDate();
			loadRequestList();
			break;
		default:
			break;
		}
	}

	private void gotoOfferFilterFragment(){
		RequestFilterFragment fragment = new RequestFilterFragment();
		fragment.setSelected(selectedDept, selectedUser, selectedType);
		fragment.setDepts(depts);
		fragment.setRequestTypes(requestTypes);
		fragment.setEnableSelectType(getEnableType());
		fragment.setCallback(this);
		gotoFragment(fragment);
	}

	protected abstract boolean getEnableType();

	@Override
	public void onDestroyView(){
		super.onDestroyView();
		setmIsNewFragment(true);
	}

	private void log(String msg){
		Log.e("VacationList", msg);
	}

	@Override
	public void onFilterCompleted(DeptModel dept, UserModel user, ApiObjectModel type){
		selectedDept = dept;
		selectedUser = user;
		selectedType = type;
		txtFilter.setText(getString(R.string.sw_work_offer_list_filter, getFilter(dept, user, type)));
		loadRequestList();
	}

	protected abstract String getFilter(DeptModel dept, UserModel user, ApiObjectModel type);

	@Override
	public void onRequestAdapterClick(VacationRequestModel vacationRequest, String type){
		gotoWorkOfferDetail(vacationRequest, type);
	}

	protected abstract void gotoWorkOfferDetail(VacationRequestModel offer, String execType);
}

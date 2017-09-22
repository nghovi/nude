package trente.asia.shiftworking.services.offer.list;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnFilterListener;
import trente.asia.shiftworking.common.interfaces.OnVacationAdapterListener;
import trente.asia.shiftworking.databinding.FragmentVacationListBinding;
import trente.asia.shiftworking.services.offer.adapter.VacationAdapter;
import trente.asia.shiftworking.services.offer.detail.VacationDetailFragment;
import trente.asia.shiftworking.services.offer.filter.VacationFilterFragment;
import trente.asia.shiftworking.services.offer.model.VacationModel;
import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class VacationListFragment extends AbstractSwFragment implements OnFilterListener, OnVacationAdapterListener{

	private VacationAdapter				adapter;
	private List<VacationModel>			offers;
	private List<VacationModel>			otherOffers;
	private List<ApiObjectModel>		vacationTypes;
	private List<DeptModel>				depts;
	private ListView					mLstOffer;
	private CommonMonthView				monthView;
	private VacationAdapter				adapterOther;
	private ListView					mLstOfferOther;
	private FragmentVacationListBinding	binding;
	private String						ALL;
	private DeptModel					selectedDept;
	private UserModel					selectedUser;
	private ApiObjectModel				selectedType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_vacation_list, container, false);
			binding = DataBindingUtil.bind(mRootView);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		requestOfferList();
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
		binding.txtFilter.setText(getString(R.string.sw_work_offer_list_filter, ALL + " - " + ALL + " - " + ALL));
		binding.lnrIdFilter.setOnClickListener(this);
	}

	private void gotoWorkOfferDetail(VacationModel offer, String execType){
		VacationDetailFragment fragment = new VacationDetailFragment();
		fragment.setActiveOfferId(offer.key);
		fragment.setExecType(execType);
		gotoFragment(fragment);
	}

	private void requestAccountInfo(){
		JSONObject param = new JSONObject();
		requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, param, true);
	}

	private void requestOfferList(){
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			if(selectedDept != null && !CCConst.ALL.equals(selectedDept.key)){
				jsonObject.put("offerDept", selectedDept.key);
			}

			if(selectedUser != null && !CCConst.ALL.equals(selectedUser.key)){
				jsonObject.put("targetUserId", selectedUser.key);
			}

			if(selectedType != null && !CCConst.ALL.equals(selectedType.key)){
				jsonObject.put("vacationId", selectedType.key);
			}
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(SwConst.API_VACATION_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_VACATION_LIST.equals(url)){
			offers = CCJsonUtil.convertToModelList(response.optString("myVacationOffers"), VacationModel.class);
			otherOffers = CCJsonUtil.convertToModelList(response.optString("otherVacationOffers"), VacationModel.class);

			vacationTypes = CCJsonUtil.convertToModelList(response.optString("vacationList"), ApiObjectModel.class);
			ApiObjectModel allType = new ApiObjectModel(CCConst.ALL, ALL);
			vacationTypes.add(0, allType);
			if(vacationTypes == null){
				selectedType = vacationTypes.get(0);
			}

			adapterOther = new VacationAdapter(activity, otherOffers);
			mLstOfferOther.setAdapter(adapterOther);
			adapterOther.setCallback(this);
			adapterOther.setType(SwConst.SW_OFFER_EXEC_TYPE_APR);

			adapter = new VacationAdapter(activity, offers);
			mLstOffer.setAdapter(adapter);
			adapter.setCallback(this);
			adapter.setType(SwConst.SW_OFFER_EXEC_TYPE_VIEW);
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

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_filter:
			gotoOfferFilterFragment();
			break;
		case R.id.btn_id_back:
			monthView.workMonth = WelfareUtil.addMonth(monthView.workMonth, -1);
			requestOfferList();
			break;
		case R.id.btn_id_next:
			monthView.workMonth = WelfareUtil.addMonth(monthView.workMonth, 1);
			requestOfferList();
			break;
		case R.id.img_id_this_month:
			monthView.workMonth = WelfareUtil.makeMonthWithFirstDate();
			requestOfferList();
			break;
		default:
			break;
		}
	}

	private void gotoOfferFilterFragment(){
		VacationFilterFragment fragment = new VacationFilterFragment();
		fragment.setSelected(selectedDept, selectedUser, selectedType);
		fragment.setDepts(depts);
		fragment.setVacationTypes(vacationTypes);
		fragment.setCallback(this);
		gotoFragment(fragment);
	}

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
		String filter = dept.deptName + " - " + user.userName + " - " + type.value;
		binding.txtFilter.setText(getString(R.string.sw_work_offer_list_filter, filter));
		requestOfferList();
	}

	@Override
	public void onVationAdapterClick(VacationModel vacation, String type) {
		gotoWorkOfferDetail(vacation, type);
	}
}

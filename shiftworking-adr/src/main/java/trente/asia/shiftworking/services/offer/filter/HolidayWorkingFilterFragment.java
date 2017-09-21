package trente.asia.shiftworking.services.offer.filter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import trente.asia.android.activity.ChiaseActivity;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnFilterListener;
import trente.asia.shiftworking.databinding.FragmentOfferFilterBinding;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;

public class HolidayWorkingFilterFragment extends AbstractSwFragment{

	public static final String			TYPE	= "TYPE";
	public static final String			DEPT	= "DEPT";
	public static final String			USER	= "USER";

	private FragmentOfferFilterBinding	binding;

	private Map<String, String>			filters;
	private OnFilterListener			callback;
	private List<ApiObjectModel>		holidayWorkType;
	private List<DeptModel>				depts;

	private DeptModel					selectedDept;
	private UserModel					selectedUser;
	private ApiObjectModel				selectedType;

	public void setDepts(List<DeptModel> depts){
		this.depts = depts;
	}

	public void setHolidayWorkType(List<ApiObjectModel> holidayWorkType){
		this.holidayWorkType = holidayWorkType;
	}

	public void setCallback(OnFilterListener callback){
		this.callback = callback;
	}

	public void setFilters(Map<String, String> filters){
		this.filters = filters;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_filter, container, false);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, getString(R.string.fragment_offer_filter_title), null);
		binding.lnrIdOfferDept.setOnClickListener(this);
		binding.lnrIdOfferUser.setOnClickListener(this);
		binding.lnrIdOfferType.setOnClickListener(this);
		binding.btnFragmentFilterClear.setOnClickListener(this);
		binding.btnFragmentFilterUpdate.setOnClickListener(this);
		updateSelectedValues();
	}

	private void updateSelectedValues() {
		if (filters.containsKey(HolidayWorkingFilterFragment.DEPT)) {
			for (DeptModel dept : depts) {
				if (filters.get(HolidayWorkingFilterFragment.DEPT).equals(dept.key)) {
					selectedDept = dept;
					break;
				}
			}
		} else {
			selectedDept = depts.get(0);
		}

		if (filters.containsKey(HolidayWorkingFilterFragment.USER)) {
			for (UserModel user : selectedDept.members) {
				if (filters.get(HolidayWorkingFilterFragment.USER).equals(user.key)) {
					selectedUser = user;
					break;
				}
			}
		} else {
			selectedUser = selectedDept.members.get(0);
		}

		if (filters.containsKey(HolidayWorkingFilterFragment.TYPE)) {
			for (ApiObjectModel type : holidayWorkType) {
				if (filters.get(HolidayWorkingFilterFragment.TYPE).equals(type.key)) {
					selectedType = type;
					break;
				}
			}
		} else {
			selectedType = holidayWorkType.get(0);
		}

		binding.txtOfferDept.setText(selectedDept.deptName);
		binding.txtOfferUser.setText(selectedUser.userName);
		binding.txtOfferType.setText(selectedType.value);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_fragment_filter_clear:
			filters.remove(TYPE);
			filters.remove(DEPT);
			filters.remove(USER);
			updateSelectedValues();
			break;
		case R.id.btn_fragment_filter_update:
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
			if(callback != null){
				callback.onFilterCompleted(filters);
			}
			break;
		case R.id.lnr_id_offer_type:
			break;
		case R.id.lnr_id_offer_dept:
			break;
		case R.id.lnr_id_offer_user:
			break;
		default:
			break;
		}
	}
}

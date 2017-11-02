package nguyenhoangviet.vpcorp.shiftworking.services.requests.filter;

import java.util.List;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.android.activity.ChiaseActivity;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnDepartmentAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnFilterListener;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnTypeAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnUserAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.databinding.FragmentOfferFilterBinding;
import nguyenhoangviet.vpcorp.welfare.adr.models.ApiObjectModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

public class RequestFilterFragment extends AbstractSwFragment implements OnDepartmentAdapterListener,OnUserAdapterListener,OnTypeAdapterListener{

	private FragmentOfferFilterBinding	binding;

	private OnFilterListener			callback;
	private List<ApiObjectModel>		requestTypes;
	private List<DeptModel>				depts;

	private DeptModel					selectedDept;
	private UserModel					selectedUser;
	private ApiObjectModel				selectedType;
	private boolean						enableSelectType;

	public void setSelected(DeptModel dept, UserModel user, ApiObjectModel type){
		this.selectedDept = dept;
		this.selectedUser = user;
		this.selectedType = type;
	}

	public void setDepts(List<DeptModel> depts){
		this.depts = depts;
	}

	public void setRequestTypes(List<ApiObjectModel> requestTypes){
		this.requestTypes = requestTypes;
	}

	public void setCallback(OnFilterListener callback){
		this.callback = callback;
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

	private void updateSelectedValues(){
		if(selectedDept == null){
			selectedDept = depts.get(0);
		}

		if(selectedUser == null){
			selectedUser = selectedDept.members.get(0);
		}

		if(selectedType == null && requestTypes != null){
			selectedType = requestTypes.get(0);

		}

		if(requestTypes == null){
			binding.lnrIdOfferType.setVisibility(View.GONE);
		}

		binding.txtOfferDept.setText(selectedDept.deptName);
		binding.txtOfferUser.setText(selectedUser.userName);
		if (selectedType != null) {
			binding.txtOfferType.setText(selectedType.value);
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_fragment_filter_clear:
			selectedDept = null;
			selectedUser = null;
			selectedType = null;
			updateSelectedValues();
			break;
		case R.id.btn_fragment_filter_update:
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
			if(callback != null){
				callback.onFilterCompleted(selectedDept, selectedUser, selectedType);
			}
			break;
		case R.id.lnr_id_offer_user:
			SelectUserFragment selectUserFragment = new SelectUserFragment();
			selectUserFragment.setData(this, selectedDept.members, selectedUser);
			gotoFragment(selectUserFragment);
			break;
		case R.id.lnr_id_offer_dept:
			SelectDeptFragment fragment = new SelectDeptFragment();
			fragment.setData(this, depts, selectedDept);
			gotoFragment(fragment);
			break;
		case R.id.lnr_id_offer_type:
			if(enableSelectType){
				SelectTypeFragment selectTypeFragment = new SelectTypeFragment();
				selectTypeFragment.setData(this, requestTypes, selectedType);
				gotoFragment(selectTypeFragment);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onSelectDepartment(DeptModel deptModel){
		selectedDept = deptModel;
		binding.txtOfferDept.setText(selectedDept.deptName);
		if(!selectedDept.members.contains(selectedUser)){
			selectedUser = selectedDept.members.get(0);
			binding.txtOfferUser.setText(selectedUser.userName);
		}
	}

	@Override
	public void onSelectUser(UserModel user){
		selectedUser = user;
		binding.txtOfferUser.setText(selectedUser.userName);
	}

	@Override
	public void onSelectType(ApiObjectModel type){
		selectedType = type;
		binding.txtOfferType.setText(type.value);
	}

	private void log(String msg){
		Log.e("VacationFilter", msg);
	}

	public void setEnableSelectType(boolean enableSelectType) {
		this.enableSelectType = enableSelectType;
	}
}

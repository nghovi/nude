package trente.asia.shiftworking.services.offer.filter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnFilterListener;
import trente.asia.shiftworking.databinding.FragmentOfferFilterBinding;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.DeptModel;

public class VacationFilterFragment extends AbstractSwFragment{

	public static final String			TYPE	= "TYPE";
	public static final String			DEPT	= "DEPT";
	public static final String			USER	= "USER";

	private LinearLayout				lnrType;
	private LinearLayout				lnrDept;
	private FragmentOfferFilterBinding	binding;

	private Map<String, String>			filters;
	private OnFilterListener			callback;
	private List<ApiObjectModel>		vacationTypes;
	private List<DeptModel>				depts;

	public void setDepts(List<DeptModel> depts) {
		this.depts = depts;
	}

	public void setVacationTypes(List<ApiObjectModel> vacationTypes) {
		this.vacationTypes = vacationTypes;
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
		lnrType = (LinearLayout)getView().findViewById(R.id.lnr_id_offer_type);
		lnrDept = (LinearLayout)getView().findViewById(R.id.lnr_id_offer_dept);
		lnrType.setOnClickListener(this);
		lnrDept.setOnClickListener(this);

		getView().findViewById(R.id.btn_fragment_filter_clear).setOnClickListener(this);
		getView().findViewById(R.id.btn_fragment_filter_update).setOnClickListener(this);
		buildDialog();
	}

	private void buildDialog(){

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
			buildDialog();
			break;
		case R.id.btn_fragment_filter_update:
			setFilterValues();
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
		default:
			break;
		}
	}

	private void setFilterValues(){

	}
}

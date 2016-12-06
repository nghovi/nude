package trente.asia.shiftworking.services.offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.android.activity.ChiaseActivity;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.welfare.adr.view.WfSpinner;

public class WorkOfferFilterFragment extends AbstractSwFragment{

	public static final String	TYPE	= "TYPE";
	public static final String	STATUS	= "STATUS";
	public static final String	DEPT	= "DEPT";

	WfSpinner					spnType;
	WfSpinner					spnStatus;
	WfSpinner					spnDept;
	private int					selectedType;
	private int					selectedStatus;
	private int					selectedDept;
	private Map<String, String>	depts;
	private Map<String, String>	offerTypesMaster;
	private Map<String, String>	offerStatusMaster;

	public void setOfferTypeStatusMaster(Map<String, String> offerTypesMaster, Map<String, String> offerStatusMaster){
		this.offerTypesMaster = offerTypesMaster;
		this.offerStatusMaster = offerStatusMaster;
	}

	public void setFiltersAndDepts(Map<String, Integer> filters, Map<String, String> offerDepts){
		this.filters = filters;
		depts = offerDepts;
	}

	private Map<String, Integer>	filters;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_offer_filter, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_offer;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_offer_filter_title), null);
		spnType = (WfSpinner)getView().findViewById(R.id.spn_fragment_offer_filter_type);
		spnStatus = (WfSpinner)getView().findViewById(R.id.spn_fragment_offer_filter_status);
		spnDept = (WfSpinner)getView().findViewById(R.id.spn_fragment_offer_filter_dept);
		getView().findViewById(R.id.btn_fragment_filter_clear).setOnClickListener(this);
		getView().findViewById(R.id.btn_fragment_filter_update).setOnClickListener(this);
		buildSpinners();
	}

	private void buildSpinners(){
		selectedType = 0;
		if(filters.containsKey(TYPE)){
			selectedType = filters.get(TYPE);
		}
		spnType.setupLayout("", new ArrayList<String>(offerTypesMaster.values()), selectedType, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedType = selectedPosition;
			}
		}, false);

		selectedStatus = 0;
		if(filters.containsKey(STATUS)){
			selectedStatus = filters.get(STATUS);
		}
		spnStatus.setupLayout("", new ArrayList<String>(offerStatusMaster.values()), selectedStatus, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedStatus = selectedPosition;
			}
		}, false);

		selectedDept = 0;
		if(filters.containsKey(DEPT)){
			selectedDept = filters.get(DEPT);
		}
		spnDept.setupLayout("", new ArrayList<String>(depts.values()), selectedDept, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedDept = selectedPosition;
			}
		}, false);
	}

	@Override
	public void onDestroy(){
		this.spnType = null;
		this.spnStatus = null;
		this.spnDept = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_fragment_filter_clear:
			filters.remove(TYPE);
			filters.remove(STATUS);
			filters.remove(DEPT);
			buildSpinners();
			// ((ChiaseActivity)activity).isInitData = true;
			// onClickBackBtn();
			break;
		case R.id.btn_fragment_filter_update:
			setFilterValues();
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
			break;
		default:
			break;
		}
	}

	private void setFilterValues(){
		filters.put(TYPE, selectedType);
		filters.put(STATUS, selectedStatus);
		filters.put(DEPT, selectedDept);
	}
}

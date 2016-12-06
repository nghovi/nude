package trente.asia.shiftworking.services.offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.welfare.adr.view.WfSpinner;

public class WorkOfferFilterFragment extends AbstractSwFragment{

	public static final String	TYPE	= "TYPE";
	public static final String	STATUS	= "STATUS";
	public static final String	DEPT	= "DEPT";

	ChiaseListDialog			spnType;
	ChiaseListDialog			spnStatus;
	ChiaseListDialog			spnDept;
	ChiaseTextView				txtType;
	ChiaseTextView				txtStatus;
	ChiaseTextView				txtDept;
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
		txtType = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_filter_type);
		txtStatus = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_filter_status);
		txtDept = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_filter_dept);
		txtType.setOnClickListener(this);
		txtStatus.setOnClickListener(this);
		txtDept.setOnClickListener(this);
		getView().findViewById(R.id.btn_fragment_filter_clear).setOnClickListener(this);
		getView().findViewById(R.id.btn_fragment_filter_update).setOnClickListener(this);
		buildSpinners();
	}

	private void buildSpinners(){

		selectedType = 0;
		if(filters.containsKey(TYPE)){
			selectedType = filters.get(TYPE);
		}
		String offerTypeCode = (String)offerTypesMaster.keySet().toArray()[selectedType];
		txtType.setText(offerTypesMaster.get(offerTypeCode));
		txtType.setValue(offerTypeCode);
		spnType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), offerTypesMaster, txtType, new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				selectedType = position;

			}
		});

		selectedStatus = 0;
		if(filters.containsKey(STATUS)){
			selectedStatus = filters.get(STATUS);
		}
		String offerStatusCode = (String)offerStatusMaster.keySet().toArray()[selectedStatus];
		txtStatus.setText(offerStatusMaster.get(offerStatusCode));
		txtStatus.setValue(offerStatusCode);
		spnStatus = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_status), offerStatusMaster, txtStatus, new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				selectedStatus = position;

			}
		});

		selectedDept = 0;
		if(filters.containsKey(DEPT)){
			selectedDept = filters.get(DEPT);
		}
		String offerDeptCode = (String)depts.keySet().toArray()[selectedDept];
		txtDept.setText(depts.get(offerDeptCode));
		txtDept.setValue(offerDeptCode);
		spnDept = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_dept), depts, txtDept, new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				selectedDept = position;

			}
		});
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
		case R.id.txt_fragment_offer_filter_type:
			spnType.show();
			break;
		case R.id.txt_fragment_offer_filter_status:
			spnStatus.show();
			break;
		case R.id.txt_fragment_offer_filter_dept:
			spnDept.show();
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

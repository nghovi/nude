package trente.asia.shiftworking.services.offer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.shiftworking.services.offer.view.WorkOfferAdapter;
import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.WfSpinner;

public class WorkOfferFilterFragment extends AbstractSwFragment{

	public static final String	TYPE	= "TYPE";
	public static final String	STATUS	= "STATUS";
	public static final String	DEPT	= "DEPT";

	WfSpinner					spnType;
	WfSpinner					spnStatus;
	WfSpinner					spnDept;
	private String				selectedType;
	private String				selectedStatus;
	private String				selectedDept;
	private Map<String, String>	depts;

	public void setFiltersAndDepts(Map<String, String> filters, List<DeptModel> deptModels){
		this.filters = filters;
		depts = new HashMap<String, String>();
		for(DeptModel deptModel : deptModels){
			depts.put(deptModel.key, deptModel.deptName);
		}
	}

	private Map<String, String>	filters;

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
		spnType.setupLayout("", new ArrayList<String>(SwConst.offerTypes.values()), 0, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedType = (String)SwConst.offerTypes.keySet().toArray()[selectedPosition];
			}
		}, false);

		spnStatus.setupLayout("", new ArrayList<String>(SwConst.offerStatus.values()), 0, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedStatus = String.valueOf(SwConst.offerStatus.keySet().toArray()[selectedPosition]);
			}
		}, false);

		spnDept.setupLayout("", new ArrayList<String>(depts.values()), 0, new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				selectedDept = (String)depts.keySet().toArray()[selectedPosition];
			}
		}, false);
	}

	private void gotoWorkOfferDetail(WorkOffer offer){
		WorkOfferDetailFragment fragment = new WorkOfferDetailFragment();
		fragment.setWorkOffer(offer);
		gotoFragment(fragment);
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
			filters = null;
			onClickBackBtn();
			break;
		case R.id.btn_fragment_filter_update:
			setFilterValues();
			onClickBackBtn();
			break;
		default:
			break;
		}
	}

	private void setFilterValues(){
		if(filters == null){
			filters = new HashMap<String, String>();
		}
		filters.put(TYPE, "2");
		filters.put(STATUS, "2");
		filters.put(DEPT, "2");
	}
}

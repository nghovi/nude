package trente.asia.shiftworking.services.offer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.Map;

import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.databinding.FragmentOfferFilterBinding;

public class WorkOfferFilterFragment extends AbstractSwFragment {

	public static final String TYPE = "TYPE";
	public static final String STATUS = "STATUS";
	public static final String DEPT = "DEPT";
	public static final String SICK_ABSENT = "SICK_ABSENT";

	private LinearLayout lnrType;
	private LinearLayout lnrStatus;
	private LinearLayout lnrDept;
	private FragmentOfferFilterBinding binding;

	private ChiaseListDialog dlgType;
	private ChiaseListDialog dlgStatus;
	private ChiaseListDialog dlgDept;
	private ChiaseListDialog dlgSickAbsent;
	private ChiaseTextView txtType;
	private ChiaseTextView txtStatus;
	private ChiaseTextView txtDept;

	private String selectedType;
	private String selectedStatus;
	private String selectedDept;
	private String selectedSickAbsentFilter;
	private Map<String, String> depts;
	private Map<String, String> offerTypesMaster;
	private Map<String, String> offerStatusMaster;
	public static Map<String, String> sickAbsentFilters = new LinkedHashMap<>();

	static {
		sickAbsentFilters.put("All","All");
		sickAbsentFilters.put("1","Yes");
		sickAbsentFilters.put("0","No");
	}

	public void setOfferTypeStatusMaster(Map<String, String> offerTypesMaster, Map<String, String> offerStatusMaster){
		this.offerTypesMaster = offerTypesMaster;
		this.offerStatusMaster = offerStatusMaster;
	}

	public void setFiltersAndDepts(Map<String, String> filters, Map<String, String> offerDepts){
		this.filters = filters;
		depts = offerDepts;
	}

	private Map<String, String> filters;

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
		txtType = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_filter_type);
		txtStatus = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_filter_status);
		txtDept = (ChiaseTextView)getView().findViewById(R.id.txt_fragment_offer_filter_dept);

		lnrType = (LinearLayout)getView().findViewById(R.id.lnr_id_offer_type);
		lnrStatus = (LinearLayout)getView().findViewById(R.id.lnr_id_offer_status);
		lnrDept = (LinearLayout)getView().findViewById(R.id.lnr_id_offer_dept);
		lnrType.setOnClickListener(this);
		lnrStatus.setOnClickListener(this);
		lnrDept.setOnClickListener(this);
		binding.lnrIdOfferSickAbsent.setOnClickListener(this);

		getView().findViewById(R.id.btn_fragment_filter_clear).setOnClickListener(this);
		getView().findViewById(R.id.btn_fragment_filter_update).setOnClickListener(this);
		buildDialog();
	}

	private void buildDialog(){

		selectedType = "0";
		if(filters.containsKey(TYPE)){
			selectedType = filters.get(TYPE);
		}
		String offerTypeCode = selectedType;
		txtType.setText(offerTypesMaster.get(offerTypeCode));
		txtType.setValue(offerTypeCode);
		dlgType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), offerTypesMaster, txtType, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				selectedType = selectedKey;
			}
		});

		selectedStatus = "0";
		if(filters.containsKey(STATUS)){
			selectedStatus = filters.get(STATUS);
		}
		String offerStatusCode = selectedStatus;
		txtStatus.setText(offerStatusMaster.get(offerStatusCode));
		txtStatus.setValue(offerStatusCode);
		dlgStatus = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_status), offerStatusMaster, txtStatus, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				selectedStatus = selectedKey;
			}
		});

		selectedDept = "0";
		if(filters.containsKey(DEPT)){
			selectedDept = filters.get(DEPT);
		}
		String offerDeptCode = selectedDept;
		txtDept.setText(depts.get(offerDeptCode));
		txtDept.setValue(offerDeptCode);
		dlgDept = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_dept), depts, txtDept, new ChiaseListDialog.OnItemClicked() {

			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				selectedDept = selectedKey;
			}
		});

		selectedSickAbsentFilter = filters.get(SICK_ABSENT);
		binding.txtFragmentOfferFilterSickAbsent.setText(sickAbsentFilters.get(selectedSickAbsentFilter));
		binding.txtFragmentOfferFilterSickAbsent.setValue(selectedSickAbsentFilter);
		dlgSickAbsent = new ChiaseListDialog(activity, getString(R.string.fragment_offer_detail_sick_absent),
				sickAbsentFilters, binding.txtFragmentOfferFilterSickAbsent, new ChiaseListDialog.OnItemClicked() {
			@Override
			public void onClicked(String selectedKey, boolean isSelected){
				selectedSickAbsentFilter = selectedKey;
			}
		});
	}

	@Override
	public void onDestroy(){
		this.dlgType = null;
		this.dlgStatus = null;
		this.dlgDept = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_fragment_filter_clear:
			filters.remove(TYPE);
			filters.remove(STATUS);
			filters.remove(DEPT);
			buildDialog();
			break;
		case R.id.btn_fragment_filter_update:
			setFilterValues();
			((ChiaseActivity)activity).isInitData = true;
			onClickBackBtn();
			break;
		case R.id.lnr_id_offer_type:
			dlgType.show();
			break;
		case R.id.lnr_id_offer_status:
			dlgStatus.show();
			break;
		case R.id.lnr_id_offer_dept:
			dlgDept.show();
			break;
		case R.id.lnr_id_offer_sick_absent:
			dlgSickAbsent.show();
			break;
		default:
			break;
		}
	}

	private void setFilterValues(){
		filters.put(TYPE, selectedType);
		filters.put(STATUS, selectedStatus);
		filters.put(DEPT, selectedDept);
		filters.put(SICK_ABSENT, selectedSickAbsentFilter);
	}
}

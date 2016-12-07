package trente.asia.shiftworking.services.offer;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;

public class WorkOfferFilterFragment extends AbstractSwFragment{

	public static final String	TYPE	= "TYPE";
	public static final String	STATUS	= "STATUS";
	public static final String	DEPT	= "DEPT";

    private LinearLayout lnrType;
    private LinearLayout lnrStatus;
    private LinearLayout lnrDept;

	private ChiaseListDialog dlgType;
	private ChiaseListDialog dlgStatus;
	private ChiaseListDialog dlgDept;
	private ChiaseTextView		txtType;
	private ChiaseTextView		txtStatus;
	private ChiaseTextView		txtDept;

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

	private Map<String, Integer> filters;

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

        lnrType = (LinearLayout) getView().findViewById(R.id.lnr_id_offer_type);
        lnrStatus = (LinearLayout) getView().findViewById(R.id.lnr_id_offer_status);
        lnrDept = (LinearLayout) getView().findViewById(R.id.lnr_id_offer_dept);
		lnrType.setOnClickListener(this);
		lnrStatus.setOnClickListener(this);
		lnrDept.setOnClickListener(this);

		getView().findViewById(R.id.btn_fragment_filter_clear).setOnClickListener(this);
		getView().findViewById(R.id.btn_fragment_filter_update).setOnClickListener(this);
		buildDialog();
	}

	private void buildDialog(){

		selectedType = 0;
		if(filters.containsKey(TYPE)){
			selectedType = filters.get(TYPE);
		}
		String offerTypeCode = (String)offerTypesMaster.keySet().toArray()[selectedType];
		txtType.setText(offerTypesMaster.get(offerTypeCode));
		txtType.setValue(offerTypeCode);
		dlgType = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_type), offerTypesMaster, txtType, new AdapterView.OnItemClickListener() {

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
		dlgStatus = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_status), offerStatusMaster, txtStatus, new AdapterView.OnItemClickListener() {

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
		dlgDept = new ChiaseListDialog(activity, getString(R.string.fragment_work_offer_edit_offer_dept), depts, txtDept, new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				selectedDept = position;

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

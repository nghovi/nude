package trente.asia.shiftworking.services.offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.shiftworking.services.offer.view.WorkOfferAdapter;
import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class WorkOfferFragment extends AbstractSwFragment{

	WorkOfferAdapter				adapter;
	private List<WorkOffer>			offers;
	private ListView				mLstOffer;
	private CommonMonthView			monthView;
	private Map<String, Integer>	filters;
	private TextView				txtFilterDesc;
	private Map<String, String>		offerTypesMaster;
	private Map<String, String>		offerStatusMaster;
	private Map<String, String>		offerDepts;
	private List<WorkOffer>			otherOffers;
	private WorkOfferAdapter		adapterOther;
	private ListView				mLstOfferOther;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_offer, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		requestOfferList();
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_offer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		if(filters != null){
			String filterType = filters.containsKey(WorkOfferFilterFragment.TYPE) ? new ArrayList<String>(offerTypesMaster.values()).get(filters.get(WorkOfferFilterFragment.TYPE)) : "All";
			String filterStatus = filters.containsKey(WorkOfferFilterFragment.STATUS) ? new ArrayList<String>(offerStatusMaster.values()).get(filters.get(WorkOfferFilterFragment.STATUS)) : "All";
			String filterDept = filters.containsKey(WorkOfferFilterFragment.DEPT) ? new ArrayList<String>(offerDepts.values()).get(filters.get(WorkOfferFilterFragment.DEPT)) : "All";
			String filtersDesc = filterType + "-" + filterStatus + "-" + filterDept;
			txtFilterDesc.setText(filtersDesc);
		}else{
			txtFilterDesc.setText(getString(R.string.chiase_common_none));
			filters = new HashMap<>();
		}
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(null, myself.userName, R.drawable.bb_action_add);
		monthView = (CommonMonthView)getView().findViewById(R.id.view_id_month);
		monthView.initialization();
		mLstOffer = (ListView)getView().findViewById(R.id.lst_work_offer);
		mLstOfferOther = (ListView)getView().findViewById(R.id.lst_fragment_work_offer_other);
		mLstOffer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				WorkOffer offer = offers.get(position);
				gotoWorkOfferDetail(offer);
			}
		});
		mLstOfferOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				WorkOffer offer = otherOffers.get(position);
				gotoWorkOfferDetail(offer);
			}
		});

		monthView.imgBack.setOnClickListener(this);
		monthView.imgNext.setOnClickListener(this);
		monthView.btnThisMonth.setOnClickListener(this);

		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);

		txtFilterDesc = (TextView)getView().findViewById(R.id.fragment_work_offer_filter_desc);
		getView().findViewById(R.id.img_fragment_work_offer_filter).setOnClickListener(this);
	}

	private void gotoWorkOfferDetail(WorkOffer offer){
		WorkOfferDetailFragment fragment = new WorkOfferDetailFragment();
		fragment.setOfferTypeStatusMaster(offerTypesMaster, offerStatusMaster);
		fragment.setWorkOffer(offer);
		gotoFragment(fragment);
	}

	private void requestOfferList(){
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_5, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", prefAccUtil.getUserPref().key);
			if(filters != null){
				if(filters.containsKey(WorkOfferFilterFragment.DEPT)){
					jsonObject.put("offerDept", new ArrayList<String>(offerDepts.keySet()).get(filters.get(WorkOfferFilterFragment.DEPT)));
				}
				if(filters.containsKey(WorkOfferFilterFragment.STATUS)){
					jsonObject.put("offerStatus", new ArrayList<String>(offerStatusMaster.keySet()).get(filters.get(WorkOfferFilterFragment.STATUS)));
				}
				if(filters.containsKey(WorkOfferFilterFragment.TYPE)){
					jsonObject.put("offerType", new ArrayList<String>(offerTypesMaster.keySet()).get(filters.get(WorkOfferFilterFragment.TYPE)));
				}
			}
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_5, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_SW_OFFER_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		offers = CCJsonUtil.convertToModelList(response.optString("myOffers"), WorkOffer.class);
		otherOffers = CCJsonUtil.convertToModelList(response.optString("otherOffers"), WorkOffer.class);
		offerTypesMaster = buildOfferTypeMaster(activity, response);
		offerStatusMaster = buildOfferStatusMaster(activity, response);
		offerDepts = buildOfferDepts(activity, response);
		adapter = new WorkOfferAdapter(activity, offers, offerTypesMaster, offerStatusMaster);
		mLstOffer.setAdapter(adapter);
		adapterOther = new WorkOfferAdapter(activity, otherOffers, offerTypesMaster, offerStatusMaster);
		mLstOfferOther.setAdapter(adapterOther);
		((ScrollView)getView().findViewById(R.id.src_fragment_work_offer)).fullScroll(ScrollView.FOCUS_UP);
	}

	private Map<String, String> buildOfferDepts(Context context, JSONObject response){
		Map<String, String> offerDepts = new LinkedHashMap<>();
		List<WorkOffer.OfferDept> depts = CCJsonUtil.convertToModelList(response.optString("offerDeptList"), WorkOffer.OfferDept.class);
		offerDepts.put("0", context.getResources().getString(R.string.chiase_common_all));

		for(WorkOffer.OfferDept dept : depts){
			offerDepts.put(dept.key, dept.value);
		}
		return offerDepts;
	}

	public static Map<String, String> buildOfferTypeMaster(Context context, JSONObject response){
		Map<String, String> offerTypesMaster = new LinkedHashMap<>();
		List<WorkOffer.OfferType> types = CCJsonUtil.convertToModelList(response.optString("offerTypeList"), WorkOffer.OfferType.class);
		offerTypesMaster.put("0", context.getResources().getString(R.string.chiase_common_all));
		for(WorkOffer.OfferType type : types){
			offerTypesMaster.put(type.key, type.value);
		}
		return offerTypesMaster;
	}

	public static Map<String, String> buildOfferStatusMaster(Context context, JSONObject response){
		Map<String, String> offerStatusMaster = new LinkedHashMap<>();
		List<WorkOffer.OfferStatus> statuses = CCJsonUtil.convertToModelList(response.optString("offerStatusList"), WorkOffer.OfferStatus.class);
		offerStatusMaster.put("0", context.getResources().getString(R.string.chiase_common_all));
		for(WorkOffer.OfferStatus status : statuses){
			offerStatusMaster.put(status.key, status.value);
		}

		return offerStatusMaster;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_fragment_work_offer_filter:
			gotoOfferFilterFragment();
			break;
		case R.id.img_id_header_right_icon:
			gotoOfferEditFragment();
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

	private void gotoOfferEditFragment(){
		WorkOfferEditFragment fragment = new WorkOfferEditFragment();
		fragment.setOfferTypeStatusMaster(offerTypesMaster, offerStatusMaster);
		gotoFragment(fragment);
	}

	private void gotoOfferFilterFragment(){
		WorkOfferFilterFragment fragment = new WorkOfferFilterFragment();
		fragment.setOfferTypeStatusMaster(offerTypesMaster, offerStatusMaster);
		fragment.setFiltersAndDepts(filters, this.offerDepts);
		gotoFragment(fragment);
	}
}

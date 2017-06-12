package trente.asia.shiftworking.services.offer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.WorkOfferModel;
import trente.asia.shiftworking.services.offer.view.WorkOfferAdapter;
import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class WorkOfferListFragment extends AbstractSwFragment{

	WorkOfferAdapter				adapter;
	private List<WorkOfferModel>	offers;
	private ListView				mLstOffer;
	private CommonMonthView			monthView;
	private Map<String, String>	filters;
	private TextView				txtFilterDesc;
	private Map<String, String>		offerTypesMaster;
	private Map<String, String>		offerStatusMaster;
	private Map<String, String>		offerDepts;
	private List<WorkOfferModel>	otherOffers;
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
            String filterType = getString(R.string.chiase_common_all);
            if(filters.containsKey(WorkOfferFilterFragment.TYPE)){
                filterType = offerTypesMaster.get(filters.get(WorkOfferFilterFragment.TYPE));
            }
			String filterStatus = getString(R.string.chiase_common_all);
            if(filters.containsKey(WorkOfferFilterFragment.STATUS)){
                filterStatus = offerStatusMaster.get(filters.get(WorkOfferFilterFragment.STATUS));
            }
			String filterDept = getString(R.string.chiase_common_all);
            if(filters.containsKey(WorkOfferFilterFragment.DEPT)){
                filterDept = offerDepts.get(filters.get(WorkOfferFilterFragment.DEPT));
            }
			String filtersDesc = filterType + " - " + filterStatus + " - " + filterDept;
			txtFilterDesc.setText(getString(R.string.sw_work_offer_list_filter, filtersDesc));
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
				WorkOfferModel offer = offers.get(position);
				gotoWorkOfferDetail(offer);
			}
		});
		mLstOfferOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				WorkOfferModel offer = otherOffers.get(position);
				gotoWorkOfferDetail(offer);
			}
		});

		monthView.imgBack.setOnClickListener(this);
		monthView.imgNext.setOnClickListener(this);
		monthView.btnThisMonth.setOnClickListener(this);

		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);

		txtFilterDesc = (TextView)getView().findViewById(R.id.fragment_work_offer_filter_desc);
		getView().findViewById(R.id.lnr_id_filter).setOnClickListener(this);
	}

	private void gotoWorkOfferDetail(WorkOfferModel offer){
		WorkOfferDetailFragment fragment = new WorkOfferDetailFragment();
		fragment.setActiveOfferId(offer.key);
		gotoFragment(fragment);
	}

	private void requestOfferList(){
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", prefAccUtil.getUserPref().key);
			Log.e("WorkOfferList", "targetUserId" + prefAccUtil.getUserPref().key);
			if(filters != null){
				if(filters.containsKey(WorkOfferFilterFragment.DEPT)){
					jsonObject.put("offerDept", filters.get(WorkOfferFilterFragment.DEPT));
				}
				if(filters.containsKey(WorkOfferFilterFragment.STATUS)){
					jsonObject.put("offerStatus", filters.get(WorkOfferFilterFragment.STATUS));
				}
				if(filters.containsKey(WorkOfferFilterFragment.TYPE)){
					jsonObject.put("offerType", filters.get(WorkOfferFilterFragment.TYPE));
				}
			}
			jsonObject.put("searchDateString", CCFormatUtil.
					formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
			Log.e("WorkOfferList", "searchDateString" + CCFormatUtil.formatDateCustom
					(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(SwConst.API_OFFER_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
        if(SwConst.API_OFFER_LIST.equals(url)){
            offers = CCJsonUtil.convertToModelList(response.optString("myOffers"), WorkOfferModel.class);
            otherOffers = CCJsonUtil.convertToModelList(response.optString("otherOffers"), WorkOfferModel.class);
            offerTypesMaster = buildOfferTypeMaster(activity, response);
            offerStatusMaster = buildOfferStatusMaster(response);
            offerDepts = buildOfferDepts(activity, response);

            if(filters == null || filters.isEmpty()){
                String filtersDesc = getString(R.string.chiase_common_all) + " - " + getString(R.string.chiase_common_all) + " - " + getString(R.string.chiase_common_all);
                txtFilterDesc.setText(getString(R.string.sw_work_offer_list_filter, filtersDesc));
            }

            adapterOther = new WorkOfferAdapter(activity, otherOffers);
            mLstOfferOther.setAdapter(adapterOther);

            adapter = new WorkOfferAdapter(activity, offers);
            mLstOffer.setAdapter(adapter);
        }else {
            super.successLoad(response, url);
        }
	}

	private Map<String, String> buildOfferDepts(Context context, JSONObject response){
		Map<String, String> offerDepts = new LinkedHashMap<>();
		List<ApiObjectModel> depts = CCJsonUtil.convertToModelList(response.optString("offerDeptList"), ApiObjectModel.class);
		offerDepts.put("0", getString(R.string.chiase_common_all));

		for(ApiObjectModel dept : depts){
			offerDepts.put(dept.key, dept.value);
		}
		return offerDepts;
	}

	public Map<String, String> buildOfferTypeMaster(Context context, JSONObject response){
		List<ApiObjectModel> lstType = CCJsonUtil.convertToModelList(response.optString("offerTypeList"), ApiObjectModel.class);
        lstType.add(0, new ApiObjectModel(CCConst.NONE, getString(R.string.chiase_common_all)));
        return WelfareFormatUtil.convertList2Map(lstType);
	}

	public Map<String, String> buildOfferStatusMaster(JSONObject response){
		Map<String, String> offerStatusMaster = new LinkedHashMap<>();
        List<ApiObjectModel> lstStatus = CCJsonUtil.convertToModelList(response.optString("offerStatusList"), ApiObjectModel.class);
		lstStatus.add(0, new ApiObjectModel(CCConst.NONE, getString(R.string.chiase_common_all)));
		return WelfareFormatUtil.convertList2Map(lstStatus);
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
		gotoFragment(fragment);
	}

	private void gotoOfferFilterFragment(){
		WorkOfferFilterFragment fragment = new WorkOfferFilterFragment();
		fragment.setOfferTypeStatusMaster(offerTypesMaster, offerStatusMaster);
		fragment.setFiltersAndDepts(filters, this.offerDepts);
		gotoFragment(fragment);
	}
}

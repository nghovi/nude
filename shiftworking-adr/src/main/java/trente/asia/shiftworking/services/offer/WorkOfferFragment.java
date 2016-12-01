package trente.asia.shiftworking.services.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.offer.model.ApproveHistory;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.shiftworking.services.offer.view.WorkOfferAdapter;
import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class WorkOfferFragment extends AbstractSwFragment{

	WorkOfferAdapter			adapter;
	private List<WorkOffer>		offers;
	private ListView			mLstOffer;
	private CommonMonthView		monthView;
	private Map<String, String>	filters;
	private TextView			txtFilterDesc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_offer, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_offer;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		if(filters != null){
			String filterType = SwConst.offerTypes.get(filters.get(WorkOfferFilterFragment.TYPE));
			String filterStatus = SwConst.offerTypes.get(filters.get(WorkOfferFilterFragment.STATUS));
			String filterDept = SwConst.offerTypes.get(filters.get(WorkOfferFilterFragment.DEPT));
			String filtersDesc = filterType + "-" + filterStatus + "-" + filterDept;
			txtFilterDesc.setText(filtersDesc);
		}else{
			txtFilterDesc.setText("None");
			// initFilters();
		}
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(null, myself.userName, R.drawable.bb_action_add);
		monthView = (CommonMonthView)getView().findViewById(R.id.view_id_month);
		monthView.initialization();
		mLstOffer = (ListView)getView().findViewById(R.id.lst_work_offer);
		mLstOffer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				WorkOffer offer = offers.get(position);
				gotoWorkOfferDetail(offer);
			}
		});
		monthView.imgBack.setOnClickListener(this);
		monthView.imgNext.setOnClickListener(this);
		monthView.btnThisMonth.setOnClickListener(this);

		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);

		txtFilterDesc = (TextView)getView().findViewById(R.id.fragment_work_offer_filter_desc);
		getView().findViewById(R.id.img_fragment_work_offer_filter).setOnClickListener(this);
		requestOfferList();
	}

	// private void initFilters(){
	// filters = new HashMap<String, String>();
	// filters.put(WorkOfferFilterFragment.TYPE, "1");
	// filters.put(WorkOfferFilterFragment.STATUS, "1");
	// filters.put(WorkOfferFilterFragment.DEPT, "1");
	// }

	private void gotoWorkOfferDetail(WorkOffer offer){
		WorkOfferDetailFragment fragment = new WorkOfferDetailFragment();
		fragment.setWorkOffer(offer);
		gotoFragment(fragment);
	}

	private void requestOfferList(){
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_5, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", prefAccUtil.getUserPref().key);
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_API_WORK_HISTORY, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		offers = CCJsonUtil.convertToModelList(response.optString("offers"), WorkOffer.class);
		offers = new ArrayList<WorkOffer>();

		WorkOffer offer = new WorkOffer();
		offer.key = "20";
		offer.loginUserId = "121";
		offer.targetUserName = "Viet";
		offer.companyId = "4";
		offer.offerDate = "2014/10/10";
		offer.status = WorkOffer.OFFER_STATUS_DONE;
		offer.offerType = SwConst.OFFER_TYPE_ABSENT;
		offer.startDate = "2016/12/01";
		offer.endDate = "2016/12/02";
		offer.startTime = "8:30";
		offer.endTime = "12:30";
		offer.note = "This is note";
		offer.permission = WorkOffer.OFFER_PERMISSION_EDITABLE;
		offer.historyList = new ArrayList<ApproveHistory>();

		offers.add(offer);
		if(adapter == null){
			adapter = new WorkOfferAdapter(activity, offers);
		}
		mLstOffer.setAdapter(adapter);

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
		case R.id.btn_id_next:
			monthView.workMonth = WelfareUtil.addMonth(monthView.workMonth, 1);
		case R.id.img_id_this_month:
			monthView.workMonth = WelfareUtil.makeMonthWithFirstDate();
		default:
			requestOfferList();
			break;
		}
	}

	private void gotoOfferEditFragment(){
		WorkOfferEditFragment fragment = new WorkOfferEditFragment();
		List<DeptModel> deptModels = new ArrayList<>();
		// fragment.setFiltersAndDepts(filters, deptModels);
		gotoFragment(fragment);
	}

	private void gotoOfferFilterFragment(){
		WorkOfferFilterFragment fragment = new WorkOfferFilterFragment();
		List<DeptModel> deptModels = new ArrayList<>();
		fragment.setFiltersAndDepts(filters, deptModels);
		gotoFragment(fragment);
	}
}

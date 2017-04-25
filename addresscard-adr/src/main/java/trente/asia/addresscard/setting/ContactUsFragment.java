package trente.asia.addresscard.setting;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * TcContactUsFragment
 *
 * @author TrungND
 */
public class ContactUsFragment extends AbstractAddressCardFragment implements View.OnClickListener{

	private WfSpinner	spnType;
	private WfSpinner	spnServiceName;
	private Button		btnSend;

	private EditText	edtContent;
	private TextView	txtContentNumber;
	private final int	MAX_LETTER	= 200;
	private String		requestType;
	private String		serviceType;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_setting;
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.wf_setting_contact_us_title), null);

		spnType = (WfSpinner)getView().findViewById(R.id.spn_id_type);
		spnServiceName = (WfSpinner)getView().findViewById(R.id.spn_id_service_name);
		edtContent = (EditText)getView().findViewById(R.id.edt_id_content);
		txtContentNumber = (TextView)getView().findViewById(R.id.txt_id_content_number);
		btnSend = (Button)getView().findViewById(R.id.btn_id_send);

		edtContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){

			}

			@Override
			public void afterTextChanged(Editable s){
				txtContentNumber.setText(String.valueOf(MAX_LETTER - s.length()));
			}
		});
		btnSend.setOnClickListener(this);
	}

	private void initSpinner(List<String> lstType, List<String> lstServiceName){
		spnType.setupLayout(getString(R.string.wf_contact_us_content_title), lstType, 0,

		new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				requestType = WelfareUtil.getContactTypeCd().get(selectedPosition);
			}
		}, true);

		spnServiceName.setupLayout(getString(R.string.wf_contact_us_service_title), lstServiceName, 0,

		new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				serviceType = WelfareUtil.getServiceCd().get(selectedPosition);
			}
		}, true);
	}

	@Override
	public void initData(){
		requestAccountInfoForm();
	}

	private void requestAccountInfoForm(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(WfUrlConst.WF_ACC_INFO_REQUEST_FORM, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_REQUEST_FORM.equals(url)){
			onRequestAccountInfoFormSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	private void onRequestAccountInfoFormSuccess(JSONObject response){
		List<ApiObjectModel> requestTypes = CCJsonUtil.convertToModelList(response.optString("requestTypes"), ApiObjectModel.class);
		List<ApiObjectModel> services = CCJsonUtil.convertToModelList(response.optString("serviceTypes"), ApiObjectModel.class);
		List<String> lstType = new ArrayList<>();
		lstType.add(getString(R.string.wf_contact_us_select_item));
		for(ApiObjectModel requestType : requestTypes){
			if(!CCStringUtil.isEmpty(requestType.value)) lstType.add(requestType.value);
		}

		List<String> lstServiceName = new ArrayList<>();
		lstServiceName.add(getString(R.string.wf_contact_us_select_item));
		for(ApiObjectModel service : services){
			if(!CCStringUtil.isEmpty(service.value)) lstServiceName.add(service.value);
		}

		initSpinner(lstType, lstServiceName);
	}

	private void contactAdmin(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("requestType", requestType);
			jsonObject.put("serviceType", serviceType);
			jsonObject.put("requestContent", edtContent.getText().toString());
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_ACC_INFO_REQUEST_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_INFO_REQUEST_UPDATE.equals(url)){
			getFragmentManager().popBackStack();
		}else{
			super.successUpdate(response, url);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_id_send:
			contactAdmin();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		spnType = null;
		spnServiceName = null;
		btnSend = null;
		edtContent = null;
		txtContentNumber = null;
	}
}

package trente.asia.shiftworking.services.requests.edit;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnUserAdapterListener;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 11/25/2016.
 */

public abstract class AbstractEditFragment extends AbstractSwFragment implements OnUserAdapterListener{
	private DatePickerDialog	datePickerDialogStart;

	private ChiaseTextView		txtStartDate;

	private TextView			txtUserName;

	protected String			activeOfferId;

	protected UserModel			selectedUser;

	public void setActiveOfferId(String activeOfferId){
		this.activeOfferId = activeOfferId;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	protected void initData(){
		txtUserName = ((TextView)getView().findViewById(R.id.txt_offer_user));
		if(CCStringUtil.isEmpty(activeOfferId)){
			selectedUser = myself;
			txtUserName.setText(selectedUser.userName);
		}else{
			loadOfferDetail();
		}
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, myself.userName, R.drawable.sw_action_save);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		txtStartDate = (ChiaseTextView) getView().findViewById(R.id.txt_start_date);
		txtStartDate.setText(CCFormatUtil.formatDate(new Date()));
		buildDatePickerDialogs();
		setOnClickListener();
	}

	protected void setOnClickListener() {
		getView().findViewById(R.id.lnr_start_date).setOnClickListener(this);
		if(Boolean.parseBoolean(myself.adminFlag) && CCStringUtil.isEmpty(activeOfferId)){
			getView().findViewById(R.id.lnr_user).setOnClickListener(this);
		}else{
			getView().findViewById(R.id.user_arrow).setVisibility(View.GONE);
		}
	}

	private void loadOfferDetail(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeOfferId);
			jsonObject.put("execType", SwConst.SW_OFFER_EXEC_TYPE_VIEW);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(getDetailApi(), jsonObject, true);
	}

	protected abstract String getDetailApi();

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(getUpdateApi().equals(url)){
			((ChiaseActivity)activity).isInitData = true;
			((WelfareActivity)activity).dataMap.put(SwConst.ACTION_OFFER_UPDATE, CCConst.YES);
			getFragmentManager().popBackStack();
		}else{
			super.successUpdate(response, url);
		}
	}

	protected abstract String getUpdateApi();

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			onClickBtnDone();
			break;
		case R.id.lnr_start_date:
			datePickerDialogStart.show();
			break;
		case R.id.lnr_user:
			SelectUserEditFragment fragment = new SelectUserEditFragment();
			fragment.setSelectedUser(selectedUser);
			fragment.setCallback(this);
			gotoFragment(fragment);
			break;
		default:
			break;
		}
	}

	protected abstract void onClickBtnDone();

	protected void buildDatePickerDialogs() {
		Calendar calendar = Calendar.getInstance();
		Date starDate = CCDateUtil.makeDate(txtStartDate.getText().toString());
		calendar.setTime(starDate);
		datePickerDialogStart = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDate = year + "/" + getDisplayNum(month + 1) + "/" + getDisplayNum(dayOfMonth);
				setStartDate(startDate);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
	}

	protected void setStartDate(String startDate) {
		txtStartDate.setText(startDate);
	}

	public String getDisplayNum(int num){
		if(num <= 9){
			return "0" + num;
		}
		return String.valueOf(num);
	}

	@Override
	public void onSelectUser(UserModel user){
		selectedUser = user;
		txtUserName.setText(selectedUser.userName);
	}
}

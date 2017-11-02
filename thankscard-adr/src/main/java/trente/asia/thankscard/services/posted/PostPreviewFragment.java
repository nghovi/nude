package nguyenhoangviet.vpcorp.thankscard.services.posted;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import io.realm.Realm;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.activities.MainActivity;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.databinding.FragmentPostPreviewBinding;
import nguyenhoangviet.vpcorp.thankscard.services.common.AbstractPagerFragment;
import nguyenhoangviet.vpcorp.thankscard.services.common.model.Template;
import nguyenhoangviet.vpcorp.thankscard.services.mypage.model.StampModel;
import nguyenhoangviet.vpcorp.thankscard.services.posted.view.StickerViewDetail;
import nguyenhoangviet.vpcorp.thankscard.services.posted.view.StickerViewPost;
import nguyenhoangviet.vpcorp.thankscard.utils.TCUtil;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesSystemUtil;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 2/15/2016.
 */
public class PostPreviewFragment extends AbstractPagerFragment implements View.OnClickListener{

	private float						normalTextSize;
	private float						photoTextSize;
	private Template					template;
	private UserModel					receiver;
	private String						message;
	private List<StickerViewPost>		stickers;
	private boolean						canSendPhoto;
	private String						imagePath;
	private FragmentPostPreviewBinding	binding;

	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}

	public void setCanSendPhoto(boolean canSendPhoto){
		this.canSendPhoto = canSendPhoto;
	}

	public void setStickers(List<StickerViewPost> stickers){
		this.stickers = stickers;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public void setReceiver(UserModel receiver){
		this.receiver = receiver;
	}

	public void setTemplate(Template template){
		this.template = template;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
		PreferencesSystemUtil preference = new PreferencesSystemUtil(getContext());
		normalTextSize = Float.parseFloat(preference.get(TcConst.PREF_NORMAL_TEXT_SIZE));
		photoTextSize = Float.parseFloat(preference.get(TcConst.PREF_PHOTO_TEXT_SIZE));
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_post_preview;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public int getTitle(){
		return R.string.tc_preview;
	}

	@Override
	protected void initView(){
		super.initView();
		binding = DataBindingUtil.bind(mRootView);
		buildTextMessage();
		binding.btnCancel.setOnClickListener(this);
		binding.btnDone.setOnClickListener(this);
	}

	@Override
	public void initData(){

	}

	@Override
	protected void onPageHistorySelected(int position){
	}

	private void buildTextMessage(){
		LinearLayout lnrMessage = (LinearLayout)getView().findViewById(R.id.lnr_message);
		TextView textMessage = (TextView)getView().findViewById(R.id.text_message);
		TextView textDate = (TextView)getView().findViewById(R.id.txt_tc_detail_date);
		TextView textTo = (TextView)getView().findViewById(R.id.txt_tc_detail_to);
		ImageView imgCard = (ImageView)getView().findViewById(R.id.img_card);
		ImageView photoView = (ImageView)getView().findViewById(R.id.layout_photo);
		TCUtil.loadImageWithGlide(template.templateUrl, imgCard);
		textMessage.setText(message);
		String postDateFormat = CCFormatUtil.formatDate(new Date());
		textDate.setText(postDateFormat);
		textTo.setText(getString(R.string.fragment_tc_detail_to, receiver.userName));
		binding.confirmMsg.setText(getString(R.string.tc_confirm_msg, receiver.userName));
		textMessage.setTypeface(Typeface.MONOSPACE);

		if(!canSendPhoto){
			setLayoutMessageCenter(lnrMessage);
			textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, normalTextSize);
		}else{
			setLayoutMessageRight(lnrMessage);
			textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoTextSize);
			if(imagePath != null){
				photoView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
			}
		}
		restoreStickers(stickers);
	}

	private void setLayoutMessageCenter(LinearLayout lnrMessage){
		PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.getPercentLayoutInfo().widthPercent = 1f;
		params.setMargins(WelfareUtil.dpToPx(60), WelfareUtil.dpToPx(76), WelfareUtil.dpToPx(60), WelfareUtil.dpToPx(60));
		lnrMessage.setLayoutParams(params);
	}

	private void setLayoutMessageRight(LinearLayout lnrMessage){
		PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_END);
		params.getPercentLayoutInfo().widthPercent = 0.5f;
		params.setMargins(0, WelfareUtil.dpToPx(36), 0, 0);
		lnrMessage.setLayoutParams(params);
	}

	private void restoreStickers(List<StickerViewPost> stickers){
		for(StickerViewPost sticker : stickers){
			StampModel stamp = StampModel.getStamp(Realm.getDefaultInstance(), sticker.getKey());
			StickerViewDetail stickerViewDetail = new StickerViewDetail(getContext());
			binding.rltStickers.addView(stickerViewDetail);
			stickerViewDetail.restoreSticker(stamp.stampPath, Float.valueOf(sticker.getLocationX()), Float.valueOf(sticker.getLocationY()), Float.valueOf(sticker.getScale()), Float.valueOf(sticker.getDegree()));
		}
	}

	private void requestPostNewCard(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("postDate", CCFormatUtil.formatDate(new Date()));
			jsonObject.put("categoryId", 1);
			jsonObject.put("templateId", template.templateId);

			UserModel userModel = prefAccUtil.getUserPref();
			jsonObject.put("posterId", userModel.key);
			if(receiver == null){
				jsonObject.put("receiverId", null);
			}else{
				jsonObject.put("receiverId", receiver.key);
			}
			jsonObject.put("message", message);
			jsonObject.put("isSecret", binding.checkboxSecret.isChecked());

			JSONArray jsonStickers = new JSONArray();
			for(StickerViewPost sticker : stickers){
				JSONObject jsonSticker = new JSONObject();
				jsonSticker.put("key", sticker.getKey());
				jsonSticker.put("locationX", sticker.getLocationX());
				jsonSticker.put("locationY", sticker.getLocationY());
				jsonSticker.put("scale", sticker.getScale());
				jsonSticker.put("degree", sticker.getDegree());
				jsonStickers.put(jsonSticker);
			}

			jsonObject.put("stickerListString", jsonStickers.toString());

			if(canSendPhoto){
				jsonObject.put("templateType", "PH");
				jsonObject.put("photoLocationX", "0");
				jsonObject.put("photoLocationY", "0");
				jsonObject.put("photoScale", "1");
			}else{
				jsonObject.put("templateType", "NM");
			}
		}catch(JSONException ex){
			ex.printStackTrace();
		}

		if(canSendPhoto){
			HashMap<String, File> photo = new HashMap<>();
			photo.put("photoFile", new File(imagePath));
			requestUpload(TcConst.API_POST_NEW_CARD, jsonObject, photo, true);
		}else{
			requestUpdate(TcConst.API_POST_NEW_CARD, jsonObject, true);
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(TcConst.API_POST_NEW_CARD.equals(url)){
			requestPostNewCardSuccess(response);
		}else{
			super.successUpdate(response, url);
		}
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		if(TcConst.API_POST_NEW_CARD.equals(url)){
			requestPostNewCardSuccess(response);
		}else{
			super.successUpload(response, url);
		}
	}

	private void requestPostNewCardSuccess(JSONObject response){
		showAlertDialog(getString(R.string.fragment_posted_confirm_success_title), getString(R.string.fragment_posted_confirm_success_message), getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which){
				onClickOkButtonAfterShowingSuccessDialog();
			}
		});
	}

	private void onClickOkButtonAfterShowingSuccessDialog(){
		((MainActivity) activity).loadData = true;
		getFragmentManager().popBackStack();
		getFragmentManager().popBackStack();
	}

	public void buildBodyLayout(){
	}

	@Override
	protected void buildPagerHeader(){

	}

	private void log(String msg){
		Log.e("TCDetail", msg);
	}

	@Override
	public boolean hasBackBtn(){
		return false;
	}

	@Override
	public boolean hasSettingBtn(){
		return false;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_cancel:
				getFragmentManager().popBackStack();
				break;
			case R.id.btn_done:
				requestPostNewCard();
				break;
		}
	}
}

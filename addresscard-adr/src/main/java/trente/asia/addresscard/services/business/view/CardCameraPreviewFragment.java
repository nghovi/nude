package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentUploadAddressCardBinding;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/10/2017.2
 */

public class CardCameraPreviewFragment extends AbstractAddressCardFragment{

	private FragmentUploadAddressCardBinding	binding;
	private String								cardPath;
	private String								logoPath;
	private String								apiString;
	private String								text;

	public static CardCameraPreviewFragment newInstance(String cardPath, String logoPath, String text, String apiString){
		CardCameraPreviewFragment fragment = new CardCameraPreviewFragment();
		fragment.cardPath = cardPath;
		fragment.logoPath = logoPath;
		fragment.apiString = apiString;
		fragment.text = text;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload_address_card, container, false);
			mRootView = binding.getRoot();
			mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
			DisplayMetrics metrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
			Picasso.with(getContext()).load(new File(cardPath)).placeholder(R.drawable.loading).resize(metrics.widthPixels, 0).into(binding.cardImage);
			Picasso.with(getContext()).load(new File(logoPath)).placeholder(R.drawable.loading).fit().into(binding.customerLogo);
		    binding.edtNote.setText(text);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		super.initData();
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, "Upload address card", R.drawable.ac_action_done);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_card;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.img_id_header_right_icon:
			uploadAddressCard();
			break;
		default:
			break;
		}
	}

	private void uploadAddressCard(){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
		Map<String, File> fileMap = new HashMap<>();
		File cardImage = new File(cardPath);
		File logo = new File(logoPath);
		fileMap.put("card", cardImage);
		fileMap.put("logo", logo);
		requestUpload(this.apiString, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		super.successUpload(response, url);
		onClickBackBtn();
	}

	private void log(String msg){
		Log.e("UploadAddressCard", msg);
	}
}

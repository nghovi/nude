package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.commons.defines.ACConst;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardDetailFragment;
import trente.asia.addresscard.databinding.FragmentBusinessCardDetailBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCardDetailFragment extends AddressCardDetailFragment{

	protected FragmentBusinessCardDetailBinding binding;

	public static BusinessCardDetailFragment newInstance(int cardKey){
		BusinessCardDetailFragment fragment = new BusinessCardDetailFragment();
		fragment.key = cardKey;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_card_detail, container, false);
			mRootView = binding.getRoot();
			binding.rltPhone.setOnClickListener(this);
			binding.rltEmail.setOnClickListener(this);
			mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		}
		return mRootView;
	}

	@Override
	protected String getApi(){
		return ACConst.AC_BUSINESS_CARD_DETAIL;
	}

	@Override
	protected void loadLayout(JSONObject response){
		card = CCJsonUtil.convertToModel(response.optString("card"), BusinessCardModel.class);
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl)
				.placeholder(R.drawable.loading)
				.resize(metrics.widthPixels, 0)
				.into(binding.cardImage);
		binding.setVariable(BR.card, card);
		binding.executePendingBindings();
		super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_edit);
	}

	@Override
	protected void gotoCardEditFragment(){
		gotoFragment(BusinessCardEditFragment.newInstance(card.key));
	}

}

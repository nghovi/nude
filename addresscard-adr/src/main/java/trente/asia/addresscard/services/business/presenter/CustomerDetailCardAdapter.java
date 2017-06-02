package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CardItemCustomerDetailBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerDetailCardAdapter extends RecyclerView.Adapter<ViewHolder>{

	private CardItemCustomerDetailBinding		binding;
	private List<BusinessCardModel>				cards;
	private Context								context;
	private OnCustomerDetailCardAdapterListener	callback;

	public CustomerDetailCardAdapter(List<BusinessCardModel> cards, OnCustomerDetailCardAdapterListener listener){
		this.cards = cards;
		this.callback = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.card_item_customer_detail, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		BusinessCardModel card = cards.get(position);
		binding = (CardItemCustomerDetailBinding)holder.getBinding();
		binding.setVariable(BR.card, card);
		binding.executePendingBindings();
		binding.rltCard.setOnClickListener((View v) -> {
            callback.onCardClick(card.key);
		});
		WfPicassoHelper.loadImage(context, BuildConfig.HOST + card.attachment.fileUrl, binding.card, null);
	}

	@Override
	public int getItemCount(){
		return cards.size();
	}

	public interface OnCustomerDetailCardAdapterListener{
		void onCardClick(int cardId);
	}
}

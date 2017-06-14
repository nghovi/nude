package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import java.util.List;

import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CardItemCustomerDetailBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;

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
		final BusinessCardModel card = cards.get(position);
		binding = (CardItemCustomerDetailBinding)holder.getBinding();
		binding.setVariable(BR.card, card);
		binding.executePendingBindings();
		binding.rltCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callback.onCardClick(card.key);
			}
		});
		Picasso.with(context)
				.load(BuildConfig.HOST + card.attachment.fileUrl)
				.fit()
				.into(binding.card);
	}

	@Override
	public int getItemCount(){
		return cards.size();
	}

	public interface OnCustomerDetailCardAdapterListener{
		void onCardClick(int cardId);
	}
}

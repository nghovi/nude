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
import trente.asia.addresscard.databinding.CardItemCustomerEditBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerEditCardAdapter extends RecyclerView.Adapter<ViewHolder>{

    private     CardItemCustomerEditBinding             binding;
    private     List<BusinessCardModel>                         cards;
    private     Context                                 context;
    private     OnCardAdapterListener                   callback;

    public CustomerEditCardAdapter(List<BusinessCardModel> cards, OnCardAdapterListener listener) {
        this.cards = cards;
        this.callback = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.card_item_customer_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BusinessCardModel card = cards.get(position);
        binding = (CardItemCustomerEditBinding) holder.getBinding();
        binding.setVariable(BR.card, card);
        binding.executePendingBindings();
        Picasso.with(context)
                .load(BuildConfig.HOST + card.attachment.fileUrl)
                .fit()
                .into(binding.card);

        binding.btnUngroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.ungroupCard(card);
                removeCard(card);
            }
        } );

        binding.btnDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.deleteCard(card);
                removeCard(card);
            }
        });

        binding.rltCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onCardItemClick(card.key);
            }
        });
    }

    private void removeCard(BusinessCardModel card) {
        cards.remove(card);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public interface OnCardAdapterListener {
        void ungroupCard(BusinessCardModel card);
        void deleteCard(BusinessCardModel card);
        void onCardItemClick(int cardId);
    }
}

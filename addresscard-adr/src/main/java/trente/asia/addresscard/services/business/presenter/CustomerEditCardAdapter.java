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
import trente.asia.addresscard.databinding.CardItemCustomerEditBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

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
        BusinessCardModel card = cards.get(position);
        binding = (CardItemCustomerEditBinding) holder.getBinding();
        binding.setVariable(BR.card, card);
        binding.executePendingBindings();
        WfPicassoHelper.loadImage(context, BuildConfig.HOST + card.attachment.fileUrl,
                binding.card, null);
        binding.btnUngroup.setOnClickListener((View v) -> {
            callback.ungroupCard(card);
            removeCard(card);
        });
        binding.btnDeleteCard.setOnClickListener((View v) -> {
            callback.deleteCard(card);
            removeCard(card);
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
        public void ungroupCard(BusinessCardModel card);
        public void deleteCard(BusinessCardModel card);
    }
}

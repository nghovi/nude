package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CardItemBinding;
import trente.asia.addresscard.services.business.model.AddressCardModel;

/**
 * Created by Windows 10 Gamer on 07/05/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<AddressCardModel> cards;
    private List<AddressCardModel> selectedCards;
    private OnItemListener callback;

    public CardAdapter(OnItemListener listener) {
        this.cards = new ArrayList<>();
        this.selectedCards = new ArrayList<>();
        this.callback = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AddressCardModel card = cards.get(position);
        CardItemBinding binding = (CardItemBinding) holder.getBinding();
        binding.setVariable(BR.card, card);
        binding.executePendingBindings();
        Picasso.with(context)
                .load(BuildConfig.HOST + card.attachment.fileUrl).fit().into(binding.cardImage);
        if (!CCStringUtil.isEmpty(card.lastUpdateUserName)) {
            binding.lastUpdateUser.setText(
                    String.format(context.getString(R.string.ac_update_by), card.lastUpdateUserName));
        } else {
            binding.lastUpdateUser.setVisibility(View.GONE);
        }
        View view = binding.getRoot();
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                card.setBackground(true);
                selectedCards.add(card);
                callback.onItemLongClickListener();
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCards.isEmpty()) {
                    callback.onItemClick(card);
                    return;
                }

                if (card.background) {
                    card.setBackground(false);
                    selectedCards.remove(card);
                    if (selectedCards.isEmpty()) {
                        callback.onUnselectAllItems();
                    }
                } else {
                    card.setBackground(true);
                    selectedCards.add(card);
                }
            }
        });
    }

    public List<AddressCardModel> getSelectedCards() {
        return selectedCards;
    }

    public void deleteSelectedCards() {
        for (AddressCardModel card : selectedCards) {
            cards.remove(card);
        }
        selectedCards.clear();
        notifyDataSetChanged();
    }

    public boolean unselectAllCards() {
        if (selectedCards.size() == 0) {
            return false;
        }
        for (AddressCardModel card : selectedCards) {
            card.setBackground(false);
        }
        selectedCards.clear();
        return true;
    }

    public void setCards(List<AddressCardModel> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private void log(String msg) {
        Log.e("CardAdapter", msg);
    }

    public interface OnItemListener {
        void onItemClick(AddressCardModel card);
        void onItemLongClickListener();
        void onUnselectAllItems();
    }
}

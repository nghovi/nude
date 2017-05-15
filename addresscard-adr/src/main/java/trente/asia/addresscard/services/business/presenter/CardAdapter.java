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

import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CardItemBinding;
import trente.asia.addresscard.services.business.model.CardModel;

/**
 * Created by Windows 10 Gamer on 07/05/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<CardModel> list;
    private List<CardModel> listSelected;
    private OnItemListener callback;
    public CardAdapter(List<CardModel> list, OnItemListener listener) {
        this.list = list;
        this.listSelected = new ArrayList<>();
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
        final CardModel card = list.get(position);
        CardItemBinding binding = (CardItemBinding) holder.getBinding();
        binding.setVariable(BR.card, card);
        binding.executePendingBindings();
        Picasso.with(context)
                .load(BuildConfig.HOST + card.attachment.fileUrl).into(binding.cardImage);
        binding.lastUpdateUser.setText(
                String.format(context.getString(R.string.ac_update_by), card.lastUpdateUser));
        View view = binding.getRoot();
        view.setOnLongClickListener((View v) -> {
                card.setBackground(true);
                listSelected.add(card);
                callback.onItemLongClickListener();
                return true;
        });
        view.setOnClickListener((View v) -> {
                if (listSelected.isEmpty()) {
                    callback.onItemClick(card);
                    return;
                }

                if (card.background) {
                    card.setBackground(false);
                    listSelected.remove(card);
                    if (listSelected.isEmpty()) {
                        callback.onUnselectAllItems();
                    }
                } else {
                    card.setBackground(true);
                    listSelected.add(card);
                }
        });
    }

    public void deleteSelectedCards() {
        for (CardModel card : listSelected) {
            list.remove(card);
        }
        listSelected.clear();
        notifyDataSetChanged();
    }

    public boolean unselectAllCards() {
        if (listSelected.size() == 0) {
            return false;
        }
        for (CardModel card : listSelected) {
            card.setBackground(false);
        }
        listSelected.clear();
        return true;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void log(String msg) {
        Log.e("CardAdapter", msg);
    }

    public interface OnItemListener {
        void onItemClick(CardModel card);
        void onItemLongClickListener();
        void onUnselectAllItems();
    }
}

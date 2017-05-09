package trente.asia.addresscard.services.card;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.R;

/**
 * Created by Windows 10 Gamer on 07/05/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {
    private List<Card> list;
    private List<Card> listSelected;
    private OnItemListener callback;
    CardAdapter(List<Card> list, OnItemListener listener) {
        this.list = list;
        this.listSelected = new ArrayList<>();
        this.callback = listener;
    }
    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, int position) {
        final Card card = list.get(position);
        holder.getBinding().setVariable(BR.card, card);
        holder.getBinding().executePendingBindings();
        View view = holder.getBinding().getRoot();
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                card.setBackground(true);
                listSelected.add(card);
                callback.onItemLongClickListener();
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listSelected.isEmpty()) {
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
            }
        });
    }

    void deleteSelectedCards() {
        for (Card card : listSelected) {
            list.remove(card);
        }
        listSelected.clear();
        notifyDataSetChanged();
    }

    void unselectAllCards() {
        for (Card card : listSelected) {
            card.setBackground(false);
        }
        listSelected.clear();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void log(String msg) {
        Log.e("CardAdapter", msg);
    }

    interface OnItemListener {
        void onItemLongClickListener();
        void onUnselectAllItems();
    }
}

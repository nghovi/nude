package trente.asia.thankscard.services.posted.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.ItemCardBinding;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.thankscard.utils.TCUtil;

/**
 * Created by tien on 7/13/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Template> cards;
    private Context context;
    private OnCardAdapterListener callback;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemCardBinding binding = (ItemCardBinding) holder.getBinding();
        final Template card = cards.get(position);
        TCUtil.loadImageWithGlide(card.templateUrl, binding.imgCard);
        binding.imgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onSelectCard(card);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards == null ? 0 : cards.size();
    }

    public void setCards(List<Template> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    public void setCallback(OnCardAdapterListener callback) {
        this.callback = callback;
    }

    public interface OnCardAdapterListener {
        void onSelectCard(Template card);
    }
}

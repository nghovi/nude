package trente.asia.addresscard.services.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import trente.asia.addresscard.R;

/**
 * Created by tien on 4/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<CardHolder> {
    private ArrayList<CardModel> list;
    private Context context;
    RecyclerViewAdapter(Context context, ArrayList<CardModel> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        CardModel cardModel = list.get(position);
        holder.dateItemText.setText(cardModel.date);
        holder.updateByText.setText(cardModel.updateBy);
        holder.cardImage.setImageResource(cardModel.imageId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

package trente.asia.addresscard.services.card;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import trente.asia.addresscard.R;

/**
 * Created by tien on 4/18/2017.
 */

public class CardHolder extends RecyclerView.ViewHolder {
    View view;
    @BindView(R.id.date_item) TextView dateItemText;
    @BindView(R.id.card_image) ImageView cardImage;
    @BindView(R.id.update_by) TextView updateByText;

    public CardHolder(View itemView) {
        super(itemView);
        view = itemView;
        ButterKnife.bind(this, itemView);
    }
}

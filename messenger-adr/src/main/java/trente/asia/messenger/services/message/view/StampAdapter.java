package trente.asia.messenger.services.message.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import trente.asia.messenger.R;
import trente.asia.messenger.databinding.ItemStampBinding;
import trente.asia.messenger.services.message.model.SSStampModel;

/**
 * Created by tien on 6/6/2017.
 */

public class StampAdapter extends RecyclerView.Adapter<ViewHolder>{
    private List<SSStampModel> stamps;
    private Context context;
    private int imageId;
    public StampAdapter() {
        stamps = new ArrayList<>();
        imageId = R.drawable.test_thai_airline;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_stamp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemStampBinding binding = (ItemStampBinding) holder.getBinding();
        Picasso.with(context).load(imageId).into(binding.imageView);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
        notifyDataSetChanged();
    }
}

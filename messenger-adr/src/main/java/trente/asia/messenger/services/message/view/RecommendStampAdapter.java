package trente.asia.messenger.services.message.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.databinding.ItemStampBinding;
import trente.asia.messenger.services.message.model.WFMStampModel;

/**
 * Created by tien on 6/9/2017.
 */

public class RecommendStampAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<WFMStampModel> recommendStamps = new ArrayList<>();
    private Context context;
    private OnRecommendStampAdapterListener callback;

    public RecommendStampAdapter(OnRecommendStampAdapterListener listener) {
        this.callback = listener;
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
        final WFMStampModel recommendStamp = recommendStamps.get(position);
        Picasso.with(context).load(BuildConfig.HOST + recommendStamp.stampPath)
                .fit().into(binding.imageView);
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onRecommendStampAdapterClick(recommendStamp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendStamps.size();
    }

    public void setRecommendStamps(List<WFMStampModel> recommendStamps) {
        this.recommendStamps = recommendStamps;
        notifyDataSetChanged();
    }

    public interface OnRecommendStampAdapterListener {
        void onRecommendStampAdapterClick(WFMStampModel recommendStamp);
    }
}

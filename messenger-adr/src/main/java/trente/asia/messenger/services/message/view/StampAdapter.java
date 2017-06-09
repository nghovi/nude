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
 * Created by tien on 6/6/2017.
 */

public class StampAdapter extends RecyclerView.Adapter<ViewHolder>{

	private List<WFMStampModel>		stamps	= new ArrayList<>();
	private Context					context;
	private OnStampAdapterListener	callback;

	public StampAdapter(OnStampAdapterListener listener){
		this.callback = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.item_stamp, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		final WFMStampModel stamp = stamps.get(position);
		ItemStampBinding binding = (ItemStampBinding)holder.getBinding();
		Picasso.with(context).load(BuildConfig.HOST + stamp.stampUrl).into(binding.imageView);
		binding.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callback.onStampClick(stamp);
			}
		});
	}

	@Override
	public int getItemCount(){
		return stamps.size();
	}

	public void setStamps(List<WFMStampModel> stamps){
		this.stamps = stamps;
		notifyDataSetChanged();
	}

	public interface OnStampAdapterListener{

		void onStampClick(WFMStampModel stamp);
	}
}

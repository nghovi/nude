package trente.asia.messenger.services.message.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmList;
import trente.asia.messenger.BR;
import trente.asia.messenger.R;
import trente.asia.messenger.databinding.ItemStampBinding;
import trente.asia.messenger.services.message.model.WFMStampModel;

/**
 * Created by tien on 6/6/2017.
 */

public class StampAdapter extends RecyclerView.Adapter<ViewHolder> {

	private RealmList<WFMStampModel> stamps;
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
		binding.setVariable(BR.stamp, stamp);
		binding.executePendingBindings();
		binding.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callback.onStampClick(stamp);
			}
		});
	}

	@Override
	public int getItemCount(){
		return stamps == null ? 0 : stamps.size();
	}

	public void setStamps(RealmList<WFMStampModel> stamps) {
		this.stamps = stamps;
		notifyDataSetChanged();
	}

	public interface OnStampAdapterListener{
		void onStampClick(WFMStampModel stamp);
	}
}

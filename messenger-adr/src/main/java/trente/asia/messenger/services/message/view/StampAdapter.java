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
import trente.asia.messenger.services.message.model.SSStampCategoryModel;
import trente.asia.messenger.services.message.model.SSStampModel;

/**
 * Created by tien on 6/6/2017.
 */

public class StampAdapter extends RecyclerView.Adapter<ViewHolder>{

	private List<SSStampModel>		stamps	= new ArrayList<>();
	private Context					context;
	private int						imageId;
	private OnStampAdapterListener	callback;
	private SSStampCategoryModel	stampCategory;

	public StampAdapter(OnStampAdapterListener listener){
		this.callback = listener;
		imageId = R.drawable.test_thai_airline;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.item_stamp, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		SSStampModel stamp = stamps.get(position);
		ItemStampBinding binding = (ItemStampBinding)holder.getBinding();
		Picasso.with(context).load(BuildConfig.HOST + stamp.attachment.fileUrl).into(binding.imageView);
		binding.imageView.setOnClickListener((View v) -> {
			callback.onStampClick(stamp);
		});
	}

	@Override
	public int getItemCount(){
		return stamps.size();
	}

	public void setStamps(List<SSStampModel> stamps){
		this.stamps = stamps;
		notifyDataSetChanged();
	}

	public void setImageId(int imageId){
		this.imageId = imageId;
		notifyDataSetChanged();
	}

	public interface OnStampAdapterListener{

		void onStampClick(SSStampModel stamp);
	}
}

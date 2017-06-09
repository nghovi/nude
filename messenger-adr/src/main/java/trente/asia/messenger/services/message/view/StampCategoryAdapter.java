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
import trente.asia.messenger.databinding.ItemStampCategoryBinding;
import trente.asia.messenger.services.message.model.WFMStampCategoryModel;

/**
 * Created by tien on 6/8/2017.
 */

public class StampCategoryAdapter extends RecyclerView.Adapter<ViewHolder>{

	private List<WFMStampCategoryModel>		stampCategories	= new ArrayList<>();
	private Context							context;
	private OnStampCategoryAdapterListener	callback;
    private int    selectedStampCategory = 0;

	public StampCategoryAdapter(OnStampCategoryAdapterListener listener){
        this.callback = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.item_stamp_category, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		WFMStampCategoryModel stampCategory = stampCategories.get(position);
		ItemStampCategoryBinding binding = (ItemStampCategoryBinding)holder.getBinding();
		Picasso.with(context).load(BuildConfig.HOST + stampCategory.categoryUrl)
                .into(binding.imageView);
//        binding.imageView.setOnClickListener((View v) -> {
//            callback.onStampCategoryClick(stampCategory);
//            selectedStampCategory = holder.getAdapterPosition();
//            v.setBackground(context.getResources().getDrawable(R.drawable.select_stamp_category_background, null));
//			notifyDataSetChanged();
//        });
        binding.imageView.setBackground(selectedStampCategory == position ?
            context.getResources().getDrawable(R.drawable.select_stamp_category_background, null) :
            context.getResources().getDrawable(R.drawable.normal_stamp_category_background, null));
	}

	@Override
	public int getItemCount(){
		return stampCategories.size();
	}

	public void setStampCategories(List<WFMStampCategoryModel> stampCategories){
		this.stampCategories = stampCategories;
		notifyDataSetChanged();
	}

	public interface OnStampCategoryAdapterListener{
		void onStampCategoryClick(WFMStampCategoryModel stampCategory);
	}
}

package trente.asia.thankscard.services.posted.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import trente.asia.thankscard.BR;
import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.ItemStampCategoryBinding;
import trente.asia.thankscard.services.mypage.model.StampCategoryModel;

/**
 * Created by tien on 6/8/2017.
 */

public class StampCategoryAdapter extends RecyclerView.Adapter<ViewHolder>
		implements RealmChangeListener<RealmResults<StampCategoryModel>> {

	private List<StampCategoryModel> stampCategories;
	private Context							context;
	private OnStampCategoryAdapterListener	callback;
	private int								selectedStampCategory	= 0;

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
	public void onBindViewHolder(final ViewHolder holder, int position){
		final StampCategoryModel stampCategory = stampCategories.get(position);
		ItemStampCategoryBinding binding = (ItemStampCategoryBinding)holder.getBinding();
		binding.setVariable(BR.category, stampCategory);
		binding.executePendingBindings();
		binding.imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				callback.onStampCategoryClick(stampCategory);
				selectedStampCategory = holder.getAdapterPosition();
				view.setBackground(context.getResources().getDrawable(R.drawable.select_stamp_category_background, null));
				notifyDataSetChanged();
			}
		});
		binding.imageView.setBackground(selectedStampCategory == position ? context.getResources().getDrawable(R.drawable.select_stamp_category_background, null) : context.getResources().getDrawable(R.drawable.normal_stamp_category_background, null));
	}

	@Override
	public int getItemCount(){
		return stampCategories == null ? 0 : stampCategories.size();
	}

	public void setStampCategories(List<StampCategoryModel> stampCategories) {
		this.stampCategories = stampCategories;
		notifyDataSetChanged();
	}

	@Override
	public void onChange(RealmResults<StampCategoryModel> wfmStampCategoryModels) {
		notifyDataSetChanged();
	}

	public interface OnStampCategoryAdapterListener{
		void onStampCategoryClick(StampCategoryModel stampCategory);
	}
}

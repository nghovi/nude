package nguyenhoangviet.vpcorp.messenger.services.message.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import nguyenhoangviet.vpcorp.messenger.BR;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.databinding.ItemStampCategoryBinding;
import nguyenhoangviet.vpcorp.messenger.services.message.model.WFMStampCategoryModel;

/**
 * Created by tien on 6/8/2017.
 */

public class StampCategoryAdapter extends RecyclerView.Adapter<ViewHolder>
		implements RealmChangeListener<RealmResults<WFMStampCategoryModel>>{

	private RealmResults<WFMStampCategoryModel> stampCategories;
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
		final WFMStampCategoryModel stampCategory = stampCategories.get(position);
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

	public void setStampCategories(RealmResults<WFMStampCategoryModel> stampCategories) {
		this.stampCategories = stampCategories;
		this.stampCategories.addChangeListener(this);
		notifyDataSetChanged();
	}

	@Override
	public void onChange(RealmResults<WFMStampCategoryModel> wfmStampCategoryModels) {
		notifyDataSetChanged();
	}

	public interface OnStampCategoryAdapterListener{
		void onStampCategoryClick(WFMStampCategoryModel stampCategory);
	}
}

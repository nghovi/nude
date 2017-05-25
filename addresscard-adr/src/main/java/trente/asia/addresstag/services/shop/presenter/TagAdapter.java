package trente.asia.addresstag.services.shop.presenter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.ItemTagBinding;
import trente.asia.addresscard.services.business.presenter.ViewHolder;
import trente.asia.addresscard.services.shop.model.TagModel;

/**
 * Created by Windows 10 Gamer on 07/05/2017.
 */

public class TagAdapter extends RecyclerView.Adapter<ViewHolder>{

	private Context			context;
	private List<TagModel>	list;
	private List<TagModel>	listSelected;

	public interface OnItemClickListener{

		public void onItemClick(TagModel tag);
	}

	private OnItemClickListener listener;

	public TagAdapter(List<TagModel> list, OnItemClickListener listener){
		this.list = list;
		this.listener = listener;
		// this.listSelected = new ArrayList<>();
		// this.callback = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position){
		final TagModel tag = list.get(position);
		ItemTagBinding binding = (ItemTagBinding)holder.getBinding();
		binding.setTagModel(tag);
		binding.executePendingBindings();
		// Picasso.with(context)
		// .load(BuildConfig.HOST + tag.attachment.fileUrl).into(binding.tagImage);
		// binding.lastUpdateUser.setText(
		// String.format(context.getString(R.string.ac_update_by), tag.lastUpdateUserName));
		View view = binding.getRoot();
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				listener.onItemClick(tag);
				tag.setSelected(!tag.selected);
			}
		});

	}

	// public List<TagModel> getListSelected() {
	// return listSelected;
	// }

	// public void deleteSelectedCards() {
	// for (TagModel tag : listSelected) {
	// list.remove(tag);
	// }
	// listSelected.clear();
	// notifyDataSetChanged();
	// }
	//
	// public boolean unselectAllCards() {
	// if (listSelected.size() == 0) {
	// return false;
	// }
	// for (TagModel tag : listSelected) {
	// tag.setBackground(false);
	// }
	// listSelected.clear();
	// return true;
	// }

	@Override
	public int getItemCount(){
		return list.size();
	}

	// public interface OnItemListener {
	// void onItemClick(TagModel tag);
	// void onItemLongClickListener();
	// void onUnselectAllItems();
	// }
}

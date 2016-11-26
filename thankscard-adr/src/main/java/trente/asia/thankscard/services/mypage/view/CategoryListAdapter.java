package trente.asia.thankscard.services.mypage.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import trente.asia.thankscard.R;
import trente.asia.thankscard.services.common.model.Category;

/**
 * Created by viet on 3/16/2016.
 */
public class CategoryListAdapter extends ArrayAdapter<Category>{

	private Context	context;
	private int		resourceId;
	private int		mSelectedPosition;

	public CategoryListAdapter(Context context, int resource, List<Category> objects){
		super(context, resource, objects);
		this.context = context;
		this.resourceId = resource;
	}

	public int getSelectedPosition(){
		return mSelectedPosition;
	}

	public void setmSelectedPosition(int selectedPosition){
		this.mSelectedPosition = selectedPosition;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;// ViewHolder pattern http://developer.android.com/training/improving-layouts/smooth-scrolling.html
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.radioBtn = (RadioButton)convertView.findViewById(R.id.rb_item_category);
			viewHolder.txtCategoryName = (TextView)convertView.findViewById(R.id.txt_item_category_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.radioBtn.setChecked(position == mSelectedPosition);
		viewHolder.radioBtn.setTag(position);
		viewHolder.radioBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				mSelectedPosition = (Integer)view.getTag();
				notifyDataSetChanged();
			}
		});
		Category category = getItem(position);
		viewHolder.txtCategoryName.setText(category.categoryName);
		return convertView;
	}

	private class ViewHolder{

		TextView	txtCategoryName;
		RadioButton	radioBtn;
	}
}

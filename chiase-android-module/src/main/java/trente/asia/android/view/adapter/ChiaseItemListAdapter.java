package trente.asia.android.view.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import trente.asia.android.R;
import trente.asia.android.view.ChiaseSingleCheckBox;
import trente.asia.android.view.model.ChiaseListItemModel;

/**
 * Created by huy-nq on 7/30/2015.
 */
public class ChiaseItemListAdapter extends ArrayAdapter<ChiaseListItemModel> {

	List<ChiaseListItemModel> mValues;

	public ChiaseItemListAdapter(Context context, List<ChiaseListItemModel> values){
		super(context, R.layout.adapter_dialog_list_item, values);
		this.mValues = values;
	}

	/* private view holder class */
	private class ViewHolder{

		TextView mTxtValueName;
		ChiaseSingleCheckBox mCbxValue;
	}

	@Override
	public int getCount(){
		return mValues.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
        ChiaseListItemModel rowItem = this.getItem(position);
		if(convertView == null) {
			convertView = ((LayoutInflater)this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_dialog_list_item, null);
			holder = new ViewHolder();
			holder.mTxtValueName = (TextView)convertView.findViewById(R.id.txt_item_value);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
        ChiaseListItemModel value = mValues.get(position);
		holder.mTxtValueName.setText(value.value);
		rowItem.key = value.key;
		rowItem.value = value.value;
		return convertView;
	}

	public Integer findPosition4Code(String key){
		int position = -1;
		for(int i = 0; i < mValues.size(); i++){
            ChiaseListItemModel item = mValues.get(i);
			if(item.key.equals(key)) {
				position = i;
				break;
			}
		}
		return position;
	}
}

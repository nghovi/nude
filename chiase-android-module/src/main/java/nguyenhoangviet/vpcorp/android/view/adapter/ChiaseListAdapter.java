package nguyenhoangviet.vpcorp.android.view.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.android.R;
import nguyenhoangviet.vpcorp.android.view.model.ChiaseListItemModel;

/**
 * ChiaseListAdapter.
 *
 * @author TrungND
 */
public class ChiaseListAdapter extends ArrayAdapter<ChiaseListItemModel>{

	private List<ChiaseListItemModel>	itemList;
	private Context						mContext;

	public ChiaseListAdapter(Context context, List<ChiaseListItemModel> itemList){
		super(context, R.layout.item_chiase_list, itemList);
		this.mContext = context;
		this.itemList = itemList;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		ChiaseListItemModel model = this.itemList.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_chiase_list, null);

		TextView txtItem = (TextView)convertView.findViewById(R.id.txt_id_item);
		txtItem.setText(model.value);
		return convertView;
	}
}

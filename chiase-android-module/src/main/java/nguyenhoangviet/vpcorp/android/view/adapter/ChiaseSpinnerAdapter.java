package nguyenhoangviet.vpcorp.android.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.android.R;
import nguyenhoangviet.vpcorp.android.view.model.ChiaseSpinnerModel;

/**
 * ChiaseSpinnerAdapter
 *
 * @author TrungND
 */
public class ChiaseSpinnerAdapter extends ArrayAdapter<ChiaseSpinnerModel> implements SpinnerAdapter{

	private Context						mContext;
	private List<ChiaseSpinnerModel>	itemList;
	private LayoutInflater				mFactory;

	public ChiaseSpinnerAdapter(Context context, List<ChiaseSpinnerModel> itemList){
		super(context, R.layout.item_spinner_list, itemList);
		this.mContext = context;
		this.itemList = itemList;
		mFactory = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		convertView = mFactory.inflate(R.layout.item_spinner_list, parent, false);
		TextView txtValue = (TextView)convertView.findViewById(R.id.txt_id_value);
		txtValue.setText(itemList.get(position).value);
		return convertView;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		convertView = mFactory.inflate(R.layout.item_spinner_list, parent, false);
		convertView.setPadding(5, 5, 5, 5);
		TextView txtValue = (TextView)convertView.findViewById(R.id.txt_id_value);
		txtValue.setText(itemList.get(position).value);
		return convertView;
	}
}

package trente.asia.android.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import trente.asia.android.item.KeyValuePair;

public class KeyValuePairArrayAdapter extends ArrayAdapter<KeyValuePair>{

	/**
	 * @brief コンストラクタ
	 * @param context
	 * @param textViewResourceId
	 */
	public KeyValuePairArrayAdapter(Context context, int textViewResourceId){
		super(context, textViewResourceId);
	}

	/**
	 * @brief コンストラクタ
	 * @param context
	 * @param textViewResourceId
	 * @param list
	 */
	public KeyValuePairArrayAdapter(Context context, int textViewResourceId, List<KeyValuePair> list){
		super(context, textViewResourceId, list);
	}

	/**
	 * @brief Spinerに表示するViewを取得します。
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		TextView view = (TextView)super.getView(position, convertView, parent);
		view.setText(getItem(position).getValue());
		return view;

	}

	/**
	 * @brief Spinerのドロップダウンアイテムに表示するViewを取得します。
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		TextView view = (TextView)super.getDropDownView(position, convertView, parent);
		view.setText(getItem(position).getValue());
		return view;
	}

	/**
	 * @brief keyに一致するインデックスを取得します。
	 * @param key
	 * @return
	 */
	public int getPosition(int key){
		int position = -1;
		for(int i = 0; i < this.getCount(); i++){
			if(this.getItem(i).getKey().equals(key)){
				position = i;
				break;
			}
		}
		return position;

	}
}

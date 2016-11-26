package trente.asia.android.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.R;
import trente.asia.android.view.adapter.ChiaseItemListAdapter;
import trente.asia.android.view.model.ChiaseListItemModel;

/**
 * QkChiaseDialog
 *
 * @author TrungND
 */
public class ChiaseListDialog extends ChiaseDialog{

	private Context						mContext;

	private List<ChiaseListItemModel>	mLstValue			= new ArrayList<ChiaseListItemModel>();

	public ChiaseListItemModel			mQkItemListModel	= null;

	private Map<String, String>			mValues;
	private ChiaseTextView				mTxtItem;
	private ListView					mLsvValues;
	private ChiaseItemListAdapter mAdapter;

	public ChiaseListDialog(Context context, String title, Map<String, String> map, final ChiaseTextView txtItem, final AdapterView.OnItemClickListener itemClickListener){
		super(context);
		this.setContentView(R.layout.dialog_common_list_values);
		this.mContext = context;
		this.mValues = map;
		this.mTxtItem = txtItem;

		if(!CCStringUtil.isEmpty(title)){
			TextView txtTitle = (TextView)this.findViewById(R.id.txt_title);
			txtTitle.setText(title);
		}

		mLsvValues = (ListView)this.findViewById(R.id.lst_values_select);
		mAdapter = new ChiaseItemListAdapter(mContext, mLstValue);
		mLsvValues.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                ChiaseListItemModel valueSelect = (ChiaseListItemModel)parent.getItemAtPosition(position);
				if(txtItem != null){
					txtItem.setText(valueSelect.value);
					txtItem.setValue(valueSelect.key);
				}
				mQkItemListModel = new ChiaseListItemModel();
				mQkItemListModel.setKey(valueSelect.key);
				mQkItemListModel.setValue(valueSelect.value);
				mLsvValues.setItemChecked(position, true);
				if(itemClickListener != null) itemClickListener.onItemClick(parent, view, position, id);
				ChiaseListDialog.this.dismiss();
			}
		});
		mLsvValues.setAdapter(mAdapter);
	}

	private void initDialog(){
		mLstValue.clear();
		for(String key : mValues.keySet()){
            ChiaseListItemModel selected = new ChiaseListItemModel();
			selected.key = key;
			selected.value = mValues.get(key);
			mLstValue.add(selected);
		}

		// set default value
		if(mTxtItem != null){
			if(!CCStringUtil.isEmpty(mTxtItem.getValue())){
				int position = mAdapter.findPosition4Code(mTxtItem.getValue());
				mLsvValues.setItemChecked(position, true);
			}
		}
	}

	public ChiaseListItemModel getItemChoiced(){
		return mQkItemListModel;
	}

	public int getItemChoicedIdx(){
		return mAdapter.findPosition4Code(mQkItemListModel.key);
	}

	public void setItemChoiced(int itemIdx){
		mQkItemListModel = mLstValue.get(itemIdx);
		mLsvValues.setItemChecked(itemIdx, true);
	}

	@Override
	public void show(){
		initDialog();
		super.show();
	}
}

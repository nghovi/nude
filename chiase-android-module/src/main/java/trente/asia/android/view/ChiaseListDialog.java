package trente.asia.android.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
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
	private ChiaseItemListAdapter		mAdapter;
	private boolean						isSingleChoice		= true;
	private List<String>				selectedValues		= new ArrayList<>();

	public interface OnItemClicked{

		public void onClicked(String selectedKey, boolean isSelected);
	}

	public ChiaseListDialog(Context context, String title, Map<String, String> map, final ChiaseTextView txtItem, final OnItemClicked itemClickListener){
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
		mLsvValues.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mAdapter = new ChiaseItemListAdapter(mContext, mLstValue);
		mLsvValues.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				ChiaseListItemModel valueSelect = (ChiaseListItemModel)parent.getItemAtPosition(position);
				if(txtItem != null && isSingleChoice){
					txtItem.setText(valueSelect.value);
					txtItem.setValue(valueSelect.key);
				}
				mQkItemListModel = new ChiaseListItemModel();
				mQkItemListModel.setKey(valueSelect.key);
				mQkItemListModel.setValue(valueSelect.value);
				boolean check = getCheckedStatus(valueSelect.key);
				if(!check){
					selectedValues.add(valueSelect.key);
				}else{
					selectedValues.remove(valueSelect.key);
				}
				mLsvValues.setItemChecked(position, !check);
				if(itemClickListener != null) itemClickListener.onClicked(valueSelect.key, !check);
				if(isSingleChoice){
					ChiaseListDialog.this.dismiss();
				}
			}
		});
		mLsvValues.setAdapter(mAdapter);
	}

	private boolean getCheckedStatus(String selectedValue){
		for(String value : selectedValues){
			if(value.equals(selectedValue)){
				return true;
			}
		}
		return false;
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
		if(isSingleChoice){
			if(mTxtItem != null){
				if(!CCStringUtil.isEmpty(mTxtItem.getValue())){
					int position = mAdapter.findPosition4Code(mTxtItem.getValue());
					mLsvValues.setItemChecked(position, true);
				}
			}
		}else{
			mLsvValues.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
			for(String selectedValue : selectedValues){
				int position = mAdapter.findPosition4Code(selectedValue);
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

	public void setMultipleChoice(List<String> selectedValues){
		isSingleChoice = false;
		this.selectedValues = selectedValues;
	}
}

package trente.asia.thankscard.fragments.dialogs;

import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import trente.asia.thankscard.R;
import trente.asia.thankscard.services.common.model.Category;
import trente.asia.thankscard.services.mypage.view.CategoryListAdapter;

/**
 * Created by viet on 3/16/2016.
 */
public class CategorySelectionDialog extends TCDialog{

	private CategoryListAdapter	adapter;
	private Category			selectedCategory;
	private List<Category>		categories;

	@Override
	int getDialogLayoutId(){
		return R.layout.dialog_category_selection;
	}

	public void setSelectedCategory(Category selectedCategory){
		this.selectedCategory = selectedCategory;
	}

	public void setCategories(List<Category> categories){
		this.categories = categories;
	}

	public Category getSelectedCategory(){
		selectedCategory = adapter.getItem(adapter.getSelectedPosition());
		return selectedCategory;
	}

	@Override
	void buildDialogLayout(View rootView){
		adapter = new CategoryListAdapter(getActivity(), R.layout.item_category, categories);
		int selectedPosition = getSelectedPosition();
		adapter.setmSelectedPosition(selectedPosition);
		ListView lstCategory = (ListView)rootView.findViewById(R.id.lst_dialog_category_selection);
		lstCategory.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lstCategory.setAdapter(adapter);
		Button btnOk = (Button)rootView.findViewById(R.id.btn_dialog_category_selection_ok);
		btnOk.setOnClickListener(this.btnPositiveListener);
		Button btnCancel = (Button)rootView.findViewById(R.id.btn_dialog_category_selection_cancel);
		btnCancel.setOnClickListener(this.btnNegativeListener);
	}

	private int getSelectedPosition(){
		for(int i = 0; i < categories.size(); i++){
			if(categories.get(i).categoryId.equals(selectedCategory.categoryId)){
				return i;
			}
		}
		return 0;
	}

}

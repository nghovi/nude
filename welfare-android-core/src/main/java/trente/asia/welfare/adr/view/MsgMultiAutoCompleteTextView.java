package trente.asia.welfare.adr.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.QwertyKeyListener;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

import trente.asia.android.util.CAMsgUtil;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by Huy-nq on 7/21/2016.
 */
public class MsgMultiAutoCompleteTextView extends MultiAutoCompleteTextView{

	private UserModel	userModel;
	public String		mName;

	public MsgMultiAutoCompleteTextView(Context context){
		super(context);
	}

	public MsgMultiAutoCompleteTextView(Context context, String name){
		super(context);
		this.mName = name;
	}

	public MsgMultiAutoCompleteTextView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
		// get view control value
		int attrsResourceIdArray[] = {trente.asia.android.R.attr.viewControl, trente.asia.android.R.attr.mName};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);
		List<Integer> list = CAMsgUtil.convertArray2List(attrsResourceIdArray);
		mName = t.getString(list.indexOf(trente.asia.android.R.attr.mName));
	}

	public MsgMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public String getmName(){
		return mName;
	}

	public void setmName(String mName){
		this.mName = mName;
	}

	@Override
	protected void replaceText(CharSequence text){
		Editable editable = getText();
		int cursorPosition = getSelectionEnd();
		int end = cursorPosition;
		int start = editable.toString().lastIndexOf("@") + 1;
		String original = TextUtils.substring(editable, start, end);

		if(editable != null){
			QwertyKeyListener.markAsReplaced(editable, start, end, original);
			editable.replace(start, end, text);
		}
	}

	@SuppressWarnings("unchecked cast")
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem){
		userModel = (UserModel)selectedItem;
		return super.convertSelectionToString(userModel.userAccount);
	}

	@Override
	protected void performFiltering(CharSequence text, int start, int end, int keyCode){
		int lastIndex = text.toString().lastIndexOf('@');
		if(lastIndex < 0){
			start = 0;
			end = 0;
		}else if(lastIndex == 0){
			// start = 1;
		}else{
			if(text.charAt(lastIndex - 1) == ' '){
				start = lastIndex;
			}else{
				start = 0;
				end = 0;
			}
		}
		super.performFiltering(text, start, end, keyCode);
	}
}

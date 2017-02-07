package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseEditText extends EditText{

	public String	mName;

	public String	viewControl;

	public ChiaseEditText(Context context){
		super(context);
	}

	public ChiaseEditText(Context context, String name){
		super(context);
		this.mName = name;
	}

	public ChiaseEditText(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.mName};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
		mName = t.getString(list.indexOf(R.attr.mName));
	}

	public ChiaseEditText(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getViewControl(){
		return viewControl;
	}

	public void setViewControl(String viewControl){
		this.viewControl = viewControl;
	}

	public interface OnKeyBoardClosedListener{

		public void onKeyBoardClose();
	}

	public void setOnKeyBoardClosedListener(OnKeyBoardClosedListener onKeyBoardClosedListener){
		this.onKeyBoardClosedListener = onKeyBoardClosedListener;
	}

	private OnKeyBoardClosedListener onKeyBoardClosedListener;

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event){
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(onKeyBoardClosedListener != null){
				onKeyBoardClosedListener.onKeyBoardClose();
			}
			return super.dispatchKeyEvent(event);
		}
		return super.dispatchKeyEvent(event);
	}

	public String getmName(){
		return mName;
	}

	public void setmName(String mName){
		this.mName = mName;
	}
}

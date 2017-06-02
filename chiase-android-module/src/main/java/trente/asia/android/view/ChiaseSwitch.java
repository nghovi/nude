package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Switch;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseSwitch extends Switch{

	public String	viewControl;

	public String	mName;

	public ChiaseSwitch(Context context){
		super(context);
	}

	public ChiaseSwitch(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.mName};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
		mName = t.getString(list.indexOf(R.attr.mName));
	}

	public ChiaseSwitch(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getViewControl(){
		return viewControl;
	}

	public void setViewControl(String viewControl){
		this.viewControl = viewControl;
	}

	public String getmName(){
		return mName;
	}

	public void setmName(String mName){
		this.mName = mName;
	}
}

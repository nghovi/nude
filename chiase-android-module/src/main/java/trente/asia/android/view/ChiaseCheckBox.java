package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;

import trente.asia.android.R;
import trente.asia.android.util.CAMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseCheckBox extends CheckBox{

	public String viewControl;

	public ChiaseCheckBox(Context context){
		super(context);
	}

	public ChiaseCheckBox(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CAMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
	}

	public ChiaseCheckBox(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getViewControl(){
		return viewControl;
	}

	public void setViewControl(String viewControl){
		this.viewControl = viewControl;
	}
}

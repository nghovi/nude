package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 26/02/2015.
 */
public class ChiaseRadioButton extends RadioButton{

	public String	mName;

	public String	viewControl;

	public String	mValue;

	public ChiaseRadioButton(Context context){
		super(context);
	}

	public ChiaseRadioButton(Context context, String name){
		super(context);
		this.mName = name;
	}

	public ChiaseRadioButton(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.mName, R.attr.mValue};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
		mName = t.getString(list.indexOf(R.attr.mName));
		mValue = t.getString(list.indexOf(R.attr.mValue));
	}
}

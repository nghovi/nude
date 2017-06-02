package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 26/02/2015.
 */
public class ChiaseRadioGroup extends RadioGroup{

	public String	mName;

	public String	viewControl;

	public ChiaseRadioGroup(Context context){
		super(context);
	}

	public ChiaseRadioGroup(Context context, String name){
		super(context);
		this.mName = name;
	}

	public ChiaseRadioGroup(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.mName};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
		mName = t.getString(list.indexOf(R.attr.mName));
	}

	public String getValue(){
		int radioButtonID = this.getCheckedRadioButtonId();
		if(radioButtonID != -1){
			ChiaseRadioButton radioButton = (ChiaseRadioButton)this.findViewById(radioButtonID);
			return radioButton.mValue;
		}
		return "";
	}
}

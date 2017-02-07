package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseButton extends Button{

	public String viewControl;

	public ChiaseButton(Context context){
		super(context);
	}

	public ChiaseButton(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
	}

	public ChiaseButton(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getViewControl(){
		return viewControl;
	}

	public void setViewControl(String viewControl){
		this.viewControl = viewControl;
	}
}

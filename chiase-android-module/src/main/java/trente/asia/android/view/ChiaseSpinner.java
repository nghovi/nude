package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Spinner;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseSpinner extends Spinner{

	public String	mName;

	public String	viewControl;

	public String	nameMap;

	// isCustom mean it has default value: All, None.
	public Boolean	isCustom;

	// custom value
	public String	customValue;

	public String	defaultValue;

	public Boolean	mIsGray;

	// public ChiaseSpinner(Context context, String name){
	// super(context);
	// this.mName = name;
	// }

	public ChiaseSpinner(Context context){
		super(context);
	}

	public ChiaseSpinner(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.nameMap, R.attr.isCustom, R.attr.customValue, R.attr.mName, R.attr.defaultValue, R.attr.mIsGray};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
		nameMap = t.getString(list.indexOf(R.attr.nameMap));
		isCustom = t.getBoolean(list.indexOf(R.attr.isCustom), false);
		customValue = t.getString(list.indexOf(R.attr.customValue));
		mName = t.getString(list.indexOf(R.attr.mName));
		defaultValue = t.getString(list.indexOf(R.attr.defaultValue));
		mIsGray = t.getBoolean(list.indexOf(R.attr.mIsGray), false);
	}

	public ChiaseSpinner(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getViewControl(){
		return viewControl;
	}

	public void setViewControl(String viewControl){
		this.viewControl = viewControl;
	}

	public String getNameMap(){
		return nameMap;
	}

	public void setNameMap(String nameMap){
		this.nameMap = nameMap;
	}

	public Boolean getIsCustom(){
		return isCustom;
	}

	public void setIsCustom(Boolean isCustom){
		this.isCustom = isCustom;
	}

	public String getCustomValue(){
		return customValue;
	}

	public void setCustomValue(String customValue){
		this.customValue = customValue;
	}

	public String getmName(){
		return mName;
	}

	public void setmName(String mName){
		this.mName = mName;
	}
}

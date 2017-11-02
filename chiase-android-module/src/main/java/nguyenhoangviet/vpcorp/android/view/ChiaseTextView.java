package nguyenhoangviet.vpcorp.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.R;
import nguyenhoangviet.vpcorp.android.util.CsMsgUtil;

/**
 * Created by TrungND on 13/11/2014.
 */
public class ChiaseTextView extends TextView{

	public String	value;

	public Boolean	isPressed	= false;

	public String	mName;

	public String	mPrefix;

	public ChiaseTextView(Context context){
		super(context);
	}

	public ChiaseTextView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.value, R.attr.isPressed, R.attr.mName, R.attr.mPrefix};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		value = t.getString(list.indexOf(R.attr.value));
		isPressed = t.getBoolean(list.indexOf(R.attr.isPressed), false);
		mName = t.getString(list.indexOf(R.attr.mName));
		mPrefix = t.getString(list.indexOf(R.attr.mPrefix));
		if(!CCStringUtil.isEmpty(mPrefix)){
			this.setText(mPrefix + " " + this.getText());
		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			this.setPadding(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingRight(), (int)(this.getPaddingBottom() + this.getLineSpacingExtra()));
		}
	}

	public ChiaseTextView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}

	public Boolean getIsPressed(){
		return isPressed;
	}

	public void setIsPressed(Boolean isPressed){
		this.isPressed = isPressed;
	}

	public String getmName(){
		return mName;
	}

	public void setmName(String mName){
		this.mName = mName;
	}

	public static int convertDpiPixel(float dpi, Context context){
		Resources resources = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, resources.getDisplayMetrics());
		return (int)px;
	}
}

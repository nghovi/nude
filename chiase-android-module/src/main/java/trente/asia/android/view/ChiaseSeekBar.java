package trente.asia.android.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseSeekBar extends SeekBar{

	public String	mName;

	public String	viewControl;

	public Integer	min;

	public Float	interval;

	public ChiaseSeekBar(Context context){
		super(context);
	}

	public ChiaseSeekBar(Context context, String name){
		super(context);
		this.mName = name;
	}

	public ChiaseSeekBar(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.mName, R.attr.min, R.attr.interval};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
		mName = t.getString(list.indexOf(R.attr.mName));
		min = t.getInt(list.indexOf(R.attr.min), 0);
		interval = t.getFloat(list.indexOf(R.attr.interval), 1);
	}

	public ChiaseSeekBar(Context context, AttributeSet attributeSet, int defStyle){
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

	public Integer getMin(){
		return min;
	}

	public void setMin(Integer min){
		this.min = min;
	}

	public Float getInterval(){
		return interval;
	}

	public void setInterval(Float interval){
		this.interval = interval;
	}

	public float getValue(){
		return min + (getProgress() * interval);
	}
}

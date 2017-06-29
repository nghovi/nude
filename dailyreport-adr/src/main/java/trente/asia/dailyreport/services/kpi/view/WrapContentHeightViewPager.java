package trente.asia.dailyreport.services.kpi.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hviet on 6/28/17.
 */

public class WrapContentHeightViewPager extends ViewPager{

	/**
	 * Constructor
	 *
	 * @param context the context
	 */
	public WrapContentHeightViewPager(Context context){
		super(context);
	}

	/**
	 * Constructor
	 *
	 * @param context the context
	 * @param attrs the attribute set
	 */
	public WrapContentHeightViewPager(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT){
			View view = getChildAt(0);
			if(view != null){
				view.measure(widthMeasureSpec, heightMeasureSpec);
				int h = view.getMeasuredHeight();
				setMeasuredDimension(getMeasuredWidth(), h);
				getLayoutParams().height = h;
			}
		}
	}

}

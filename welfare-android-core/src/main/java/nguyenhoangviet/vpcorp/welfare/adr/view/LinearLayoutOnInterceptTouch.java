package nguyenhoangviet.vpcorp.welfare.adr.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by viet on 3/30/2017.
 */

public class LinearLayoutOnInterceptTouch extends LinearLayout{

	public boolean	isIntercept	= false;

	public LinearLayoutOnInterceptTouch(Context context){
		super(context);
	}

	public LinearLayoutOnInterceptTouch(Context context, @Nullable AttributeSet attrs){
		super(context, attrs);
	}

	public LinearLayoutOnInterceptTouch(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent motionEvent){
		return isIntercept;
	}
}

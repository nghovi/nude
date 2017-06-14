package trente.asia.android.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hviet on 6/13/17.
 */

public class ZoomableTextView extends ChiaseTextView{

	final static float	STEP			= 200;
	float				mRatio			= 1.0f;
	int					mBaseDist;
	float				mBaseRatio;
	float				initialFontSize	= 13;
	private int			sizeMax			= 60;

	public ZoomableTextView(Context context){
		super(context);
		initialFontSize = this.getTextSize();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		super.onTouchEvent(event);

		if(event.getPointerCount() == 2){
			int action = event.getAction();
			int pureaction = action & MotionEvent.ACTION_MASK;
			int distance = getDistance(event);
			if(pureaction == MotionEvent.ACTION_POINTER_DOWN){
				mBaseDist = distance;
				mBaseRatio = mRatio;
			}else{
				float delta = (distance - mBaseDist) / STEP;
				float multi = (float)Math.pow(2, delta);
				mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
				int newSize = (int)(mRatio + initialFontSize);
				newSize = Math.min(newSize, sizeMax);
				newSize = Math.max(newSize, (int)initialFontSize);
				setTextSize(newSize);
			}
		}

		return true;
	}

	int getDistance(MotionEvent event){
		int dx = (int)(event.getX(0) - event.getX(1));
		int dy = (int)(event.getY(0) - event.getY(1));
		return (int)(Math.sqrt(dx * dx + dy * dy));
	}
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// return false;
	//
	// }

	public ZoomableTextView(Context context, @Nullable AttributeSet attrs){
		super(context, attrs);
	}

	public ZoomableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

}

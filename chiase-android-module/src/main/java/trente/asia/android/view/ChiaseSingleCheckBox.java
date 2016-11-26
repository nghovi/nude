package trente.asia.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.CheckBox;

/**
 * ChiaseSingleCheckBox
 *
 * @author TrungND
 */
public class ChiaseSingleCheckBox extends CheckBox {

	private Drawable buttonDrawable;

	public ChiaseSingleCheckBox(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	public ChiaseSingleCheckBox(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public ChiaseSingleCheckBox(Context context){
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return false;
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event){
		return false;
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event){
		return false;
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event){
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		return false;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event){
		return false;
	}

	@Override
	protected int getSuggestedMinimumWidth(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return getCompoundPaddingLeft() + getCompoundPaddingRight();
		}else{
			return buttonDrawable == null ? 0 : buttonDrawable.getIntrinsicWidth();
		}
	}

	@Override
	public void setButtonDrawable(Drawable d){
		buttonDrawable = d;
		super.setButtonDrawable(d);
	}
}

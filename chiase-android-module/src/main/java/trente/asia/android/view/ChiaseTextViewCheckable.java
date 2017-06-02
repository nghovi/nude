package trente.asia.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * Created by TrungND on 13/11/2014.
 */
public class ChiaseTextViewCheckable extends TextView implements Checkable{

	private boolean		isChecked;

	private final int[]	CHECKED_STATE_SET	= {android.R.attr.state_checked};

	public ChiaseTextViewCheckable(Context context){
		super(context);
	}

	public ChiaseTextViewCheckable(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

	}

	public ChiaseTextViewCheckable(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	@Override
	public int[] onCreateDrawableState(final int extraSpace){
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if(isChecked()) mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		return drawableState;
	}

	@Override
	protected void drawableStateChanged(){
		super.drawableStateChanged();
		invalidate();
	}

	@Override
	public void setChecked(boolean checked){
		this.isChecked = checked;
		refreshDrawableState();
	}

	@Override
	public boolean isChecked(){
		return isChecked;
	}

	@Override
	public void toggle(){
		this.isChecked = !this.isChecked;
	}
}

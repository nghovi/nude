package nguyenhoangviet.vpcorp.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import nguyenhoangviet.vpcorp.android.view.listener.ChiaseOnCheckedChangeListener;

/**
 * Created by NDTrung on 4/30/2015.
 */
public class ChiaseCheckableImageView extends ImageView implements Checkable{

	private boolean							mChecked			= false;

	private final int[]						CHECKED_STATE_SET	= {android.R.attr.state_checked};

	private ChiaseOnCheckedChangeListener	mOnCheckedChangeListener;

	public ChiaseCheckableImageView(Context context){
		super(context);
	}

	public ChiaseCheckableImageView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
		// setOnClickListener(this);
	}

	public ChiaseCheckableImageView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
		// / setOnClickListener(this);
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
		if(mChecked == checked) return;
		mChecked = checked;

		if(mOnCheckedChangeListener != null){
			mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
		}
		refreshDrawableState();
	}

	@Override
	public boolean isChecked(){
		return mChecked;
	}

	@Override
	public void toggle(){
		setChecked(!mChecked);
	}

	@Override
	public boolean performClick(){
		toggle();
		return super.performClick();
	}

	// @Override
	// public void onClick(View v) {
	// toggle();
	// }

	public void setOnCheckedChangeListener(ChiaseOnCheckedChangeListener listener){
		mOnCheckedChangeListener = listener;
	}
}

package trente.asia.android.view.layout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import trente.asia.android.listener.CsOnCheckedChangeListener;

/**
 * Created by TrungND on 10/23/2014.
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable{

	protected boolean					isChecked;

	protected List<Checkable>			checkableViews;

	private final int[]					CHECKED_STATE_SET	= {android.R.attr.state_checked};
	private CsOnCheckedChangeListener	mOnCheckedChangeListener;

	public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		initialise(attrs);
	}

	public CheckableLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		initialise(attrs);
	}

	public CheckableLinearLayout(Context context, int checkableId){
		super(context);
		initialise(null);
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

	/*
	 * @see android.widget.Checkable#isChecked()
	 */
	public boolean isChecked(){
		return isChecked;
	}

	/*
	 * @see android.widget.Checkable#setChecked(boolean)
	 */
	public void setChecked(boolean isChecked){
		this.isChecked = isChecked;
		for(Checkable c : checkableViews){
			c.setChecked(isChecked);
		}

		if(mOnCheckedChangeListener != null){
			mOnCheckedChangeListener.onCheckedChanged(this, isChecked);
		}
		refreshDrawableState();
	}

	/*
	 * @see android.widget.Checkable#toggle()
	 */
	public void toggle(){
		this.isChecked = !this.isChecked;
		for(Checkable c : checkableViews){
			c.toggle();
		}
	}

	@Override
	protected void onFinishInflate(){
		super.onFinishInflate();

		final int childCount = this.getChildCount();
		for(int i = 0; i < childCount; ++i){
			findCheckableChildren(this.getChildAt(i));
		}
	}

	/**
	 * Read the custom XML attributes
	 */
	private void initialise(AttributeSet attrs){
		this.isChecked = false;
		this.checkableViews = new ArrayList<Checkable>(5);
	}

	/**
	 * Add to our checkable list all the children of the view that implement the
	 * interface Checkable
	 */
	private void findCheckableChildren(View v){
		if(v instanceof Checkable){
			this.checkableViews.add((Checkable)v);
		}

		if(v instanceof ViewGroup){
			final ViewGroup vg = (ViewGroup)v;
			final int childCount = vg.getChildCount();
			for(int i = 0; i < childCount; ++i){
				findCheckableChildren(vg.getChildAt(i));
			}
		}
	}

	/**
	 * Register a callback to be invoked when the checked state of this view
	 * changes.
	 *
	 * @param listener the callback to call on checked state change
	 */
	public void setOnCheckedChangeListener(CsOnCheckedChangeListener listener){
		mOnCheckedChangeListener = listener;
	}
}

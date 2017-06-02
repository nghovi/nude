package trente.asia.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom ImageView. The height of the ImageView will expand according to the ratio of the width.
 */
public class ChiaseImageViewRatioWidth extends ChiaseImageView{

	public ChiaseImageViewRatioWidth(Context context){
		super(context);
	}

	public ChiaseImageViewRatioWidth(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
	}

	public ChiaseImageViewRatioWidth(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Drawable d = getDrawable();
		if(d != null){
			int width = View.MeasureSpec.getSize(widthMeasureSpec);
			int height = (int)Math.ceil((float)width * (float)d.getIntrinsicHeight() / (float)d.getIntrinsicWidth());
			setMeasuredDimension(width, height);
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}

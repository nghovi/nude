package trente.asia.welfare.adr.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * WfGridView
 *
 * @author TrungND
 */
public class WfGridView extends GridView{

	public WfGridView(Context context){
		super(context);
	}

	public WfGridView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public WfGridView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public WfGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int heightSpec;

		if(getLayoutParams().height == LayoutParams.WRAP_CONTENT){
			// The great Android "hackatlon", the love, the magic.
			// The two leftmost bits in the height measure spec have
			// a special meaning, hence we can't use them to describe height.
			heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		}else{
			heightSpec = heightMeasureSpec;
		}

		super.onMeasure(widthMeasureSpec, heightSpec);
	}
}

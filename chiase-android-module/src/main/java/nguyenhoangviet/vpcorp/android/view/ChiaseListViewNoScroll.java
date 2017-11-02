package nguyenhoangviet.vpcorp.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by TrungND on 25/05/2015.
 */
public class ChiaseListViewNoScroll extends ListView{

	public ChiaseListViewNoScroll(Context context){
		super(context);
	}

	public ChiaseListViewNoScroll(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public ChiaseListViewNoScroll(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
		ViewGroup.LayoutParams params = getLayoutParams();
		params.height = getMeasuredHeight();
	}
}

package trente.asia.calendar.services.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by hviet on 10/13/17.
 */

public class ObservableScrollView extends ScrollView{

	public int	x;
	public int	y;
	public int	oldx;
	public int	oldy;

	public interface ScrollViewListener{

		void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

	}

	private ScrollViewListener scrollViewListener = null;

	public ObservableScrollView(Context context){
		super(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	public ObservableScrollView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener){
		this.scrollViewListener = scrollViewListener;
	}

	public void setCoordinate(int x, int y, int oldx, int oldy){
		this.x = x;
		this.y = y;
		this.oldx = oldx;
		this.oldy = oldy;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy){
		this.x = x;
		this.y = y;
		this.oldx = oldx;
		this.oldy = oldy;
		super.onScrollChanged(this.x, this.y, this.oldx, this.oldy);
		if(scrollViewListener != null){
			scrollViewListener.onScrollChanged(this, this.x, this.y, this.oldx, this.oldy);
		}
	}
}

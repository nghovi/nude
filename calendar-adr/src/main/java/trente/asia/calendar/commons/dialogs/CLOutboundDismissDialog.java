package trente.asia.calendar.commons.dialogs;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 3/14/2017.
 */

public class CLOutboundDismissDialog extends ClDialog{

	public CLOutboundDismissDialog(Context context){
		super(context);
		Window window = this.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		// window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		this.setCanceledOnTouchOutside(true);
	}

	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(event)){
			dismiss();
			return true;
		}
		return false;
	}

	private boolean isOutOfBounds(MotionEvent event){
		final int x = (int)event.getX();
		final int y = (int)event.getY();
		int marginLeftPx = WelfareUtil.dpToPx(40);
		int marginTopPx = WelfareUtil.dpToPx(120);
		final View decorView = getWindow().getDecorView();
		final int decorViewWidth = decorView.getWidth();
		final int decorViewHeight = decorView.getHeight();
		return x < marginLeftPx || x > decorViewWidth - marginLeftPx || y < marginTopPx || y > decorViewHeight - marginTopPx;
	}
}

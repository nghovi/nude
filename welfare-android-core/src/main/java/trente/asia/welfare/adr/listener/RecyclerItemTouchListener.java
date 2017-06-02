package trente.asia.welfare.adr.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerItemTouchListener
 *
 * @author TrungND
 */
public class RecyclerItemTouchListener implements RecyclerView.OnItemTouchListener{

	private GestureDetector		gestureDetector;
	private OnItemClickListener	clickListener;

	public RecyclerItemTouchListener(Context context, final RecyclerView recyclerView, final OnItemClickListener clickListener){
		this.clickListener = clickListener;
		gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e){
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e){
				View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
				if(child != null && clickListener != null){
					clickListener.onItemLongClick(child, recyclerView.getChildAdapterPosition(child));
				}
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e){

		View child = rv.findChildViewUnder(e.getX(), e.getY());
		if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
			clickListener.onItemClick(child, rv.getChildAdapterPosition(child));
		}
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent e){
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){

	}
}

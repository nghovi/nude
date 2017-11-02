package nguyenhoangviet.vpcorp.messenger.services.message.view;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * HeaderDecoration
 *
 * @author TrungND
 */
public class HeaderDecoration extends RecyclerView.ItemDecoration{

	private View mView;

	public HeaderDecoration(View view){
		this.mView = view;
	}

	@Override
	public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state){
		super.onDraw(canvas, parent, state);

		mView.layout(parent.getLeft(), 0, parent.getRight(), mView.getMeasuredHeight());
	}
}

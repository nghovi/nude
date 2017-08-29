package trente.asia.messenger.services.message.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import trente.asia.messenger.services.message.listener.OnScrollToTopListener;

/**
 * MsgRecyclerView
 *
 * @author TrungND
 */
public class MsgRecyclerView extends RecyclerView{

	private int						lastVisibleItem		= 0;
	private boolean					isScrollToBottom	= true;
	public OnScrollToTopListener	listener;

	public MsgRecyclerView(Context context){
		super(context);
		initLayout();
	}

	public MsgRecyclerView(Context context, @Nullable AttributeSet attrs){
		super(context, attrs);
		initLayout();
	}

	public MsgRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		initLayout();
	}

	private void initLayout(){
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
		this.setLayoutManager(mLayoutManager);
		this.setItemAnimator(new DefaultItemAnimator());
		this.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState){
				super.onScrollStateChanged(recyclerView, newState);
				lastVisibleItem = ((LinearLayoutManager)MsgRecyclerView.this.getLayoutManager()).findLastVisibleItemPosition();
				int firstVisibleItem = ((LinearLayoutManager)MsgRecyclerView.this.getLayoutManager()).findFirstVisibleItemPosition();
				if(firstVisibleItem == 0 && listener != null){
					// check recycler view auto clear
					if(MsgRecyclerView.this.getAdapter().getItemCount() > 0){
						listener.onScrollToTopListener();
					}
				}
			}
		});
	}

	public void setLastVisibleItem(int lastVisibleItem){
		this.lastVisibleItem = lastVisibleItem;
	}

	public void scrollRecyclerToBottom(){
		if(isScrollToBottom){
//			 this.scrollToPosition(this.getAdapter().getItemCount() - 1);
			this.getLayoutManager().smoothScrollToPosition(this, null, this.getAdapter().getItemCount() - 1);
			this.lastVisibleItem = this.getAdapter().getItemCount() - 1;
		}
	}

	public void isScrollToBottom(){
		isScrollToBottom = lastVisibleItem == (this.getAdapter().getItemCount() - 1);
	}

	private void log(String msg) {
		Log.e("MsgRecyclerView", msg);
	}
}

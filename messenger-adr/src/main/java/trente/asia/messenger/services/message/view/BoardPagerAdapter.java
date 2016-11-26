package trente.asia.messenger.services.message.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class BoardPagerAdapter extends PagerAdapter{

	private Context			mContext;
	private LayoutInflater	mInflater;

	private MessageView		mMessageView;
	private NoteView		mNoteView;
	private MemberListView	mMemberListView;

	public BoardPagerAdapter(Context context, MessageView messageView, NoteView noteView, MemberListView memberListView){
		this.mContext = context;
		this.mMessageView = messageView;
		this.mNoteView = noteView;
		this.mMemberListView = memberListView;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return 3;
	}

	/**
	 * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
	 * same object as the {@link android.view.View} added to the {@link android.support.v4.view.ViewPager}.
	 */
	@Override
	public boolean isViewFromObject(View view, Object o){
		return o == view;
	}

	/**
	 * Instantiate the {@link android.view.View} which should be displayed at {@code position}. Here we
	 * inflate a layout from the apps resources and then change the text view to signify the position.
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position){
		View view = null;
		if(position == 0){
			view = mMessageView;
		}else if(position == 1){
			view = mNoteView;
		}else{
			view = mMemberListView;
		}

		container.addView(view);

		return view;
	}

	/**
	 * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the {@link android.view.View}.
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object){
		container.removeView((View)object);
	}
}

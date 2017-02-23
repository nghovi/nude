package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class DailySummaryPagerAdapter extends PagerAdapter{

	private Context			mContext;
	private LayoutInflater	mInflater;

	private final Date		TODAY		= Calendar.getInstance().getTime();
	private final int		ACTIVE_PAGE	= Integer.MAX_VALUE / 2;

	public DailySummaryPagerAdapter(Context context){
		this.mContext = context;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return Integer.MAX_VALUE;
	}

	/**
	 * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
	 * same object as the {@link View} added to the {@link android.support.v4.view.ViewPager}.
	 */
	@Override
	public boolean isViewFromObject(View view, Object o){
		return o == view;
	}

	/**
	 * Instantiate the {@link View} which should be displayed at {@code position}. Here we
	 * inflate a layout from the apps resources and then change the text view to signify the position.
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position){
		Date activeDate = CsDateUtil.addDate(TODAY, position - ACTIVE_PAGE);
		View view = mInflater.inflate(R.layout.view_daily_summary, null);

		container.addView(view);

		return view;
	}

	/**
	 * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the {@link View}.
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object){
		container.removeView((View)object);
	}
}

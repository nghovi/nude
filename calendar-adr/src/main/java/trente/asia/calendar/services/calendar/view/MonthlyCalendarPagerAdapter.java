package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import trente.asia.calendar.R;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class MonthlyCalendarPagerAdapter extends PagerAdapter{

	private Context			mContext;
	private LayoutInflater	mInflater;

	public MonthlyCalendarPagerAdapter(Context context){
		this.mContext = context;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    public class MonthlyViewHolder{

        public TextView txtText;

        public MonthlyViewHolder(View view){
            txtText = (TextView)view.findViewById(R.id.txt_id_text);
        }
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
		View view = mInflater.inflate(R.layout.monthly_view_1, null);
		MonthlyViewHolder holder = new MonthlyViewHolder(view);
        holder.txtText.setText(String.valueOf(position));

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

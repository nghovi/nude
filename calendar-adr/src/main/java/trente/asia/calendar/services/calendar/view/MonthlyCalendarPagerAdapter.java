package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class MonthlyCalendarPagerAdapter extends PagerAdapter{

	private Context			mContext;
	private LayoutInflater	mInflater;
	private final int		ACTIVE_PAGE	= Integer.MAX_VALUE / 2;
	private final Date		TODAY		= CsDateUtil.makeMonthWithFirstDate();

	public MonthlyCalendarPagerAdapter(Context context){
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
		Date activeDate = CsDateUtil.addMonth(TODAY, position - ACTIVE_PAGE);
		Calendar activeCalendar = CCDateUtil.makeCalendar(activeDate);
		activeCalendar.setFirstDayOfWeek(Calendar.THURSDAY);

		MonthlyCalendarView calendarView = new MonthlyCalendarView(mContext);

		// add calendar title
		View titleView = mInflater.inflate(R.layout.monthly_calendar_title, null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		for(String day : CsDateUtil.getAllDay4Week(Calendar.THURSDAY)){
			View titleItem = mInflater.inflate(R.layout.monthly_calendar_title_item, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            titleItem.setLayoutParams(layoutParams);
			TextView txtTitleItem = (TextView)titleItem.findViewById(R.id.txt_id_row_content);
            txtTitleItem.setText(day);
            lnrRowTitle.addView(titleItem);
		}
		calendarView.addView(titleView);

		List<Date> lstDate = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(activeDate));
		View rowView = null;
		LinearLayout lnrRowContent = null;
		for(int index = 0; index < lstDate.size(); index++){
			Date itemDate = lstDate.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				calendarView.addView(rowView);
			}
			View rowItemView = mInflater.inflate(R.layout.monthly_calendar_row_item, null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			rowItemView.setLayoutParams(layoutParams);
			TextView txtContent = (TextView)rowItemView.findViewById(R.id.txt_id_row_content);
			txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_11, itemDate));

			lnrRowContent.addView(rowItemView);
		}

		container.addView(calendarView);

		return calendarView;
	}

	/**
	 * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the {@link View}.
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object){
		container.removeView((View)object);
	}
}

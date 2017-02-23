package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * BoardPagerAdapter
 *
 * @author TrungND
 */
public class DailySummaryPagerAdapter extends PagerAdapter{

	private Context								mContext;
	private LayoutInflater						mInflater;
	private Map<String, List<ScheduleModel>>	mapSchedule;

	private final Date							TODAY		= Calendar.getInstance().getTime();
	private final int							ACTIVE_PAGE	= Integer.MAX_VALUE / 2;

	public class DailySummaryViewHolder{

		public LinearLayout	lnrNoData;
		public ListView		lvSchedule;

		public DailySummaryViewHolder(View view){
			lnrNoData = (LinearLayout)view.findViewById(R.id.lnr_id_no_data);
			lvSchedule = (ListView)view.findViewById(R.id.lsv_id_schedule);
		}
	}

	public DailySummaryPagerAdapter(Context context, Map<String, List<ScheduleModel>> mapSchedule){
		this.mContext = context;
		this.mapSchedule = mapSchedule;
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
		DailySummaryViewHolder viewHolder = new DailySummaryViewHolder(view);

		List<ScheduleModel> lstSchedule4Date = this.mapSchedule.get(WelfareFormatUtil.formatDate(activeDate));
		if(!CCCollectionUtil.isEmpty(lstSchedule4Date)){
			viewHolder.lnrNoData.setVisibility(View.GONE);
			viewHolder.lvSchedule.setVisibility(View.VISIBLE);
			DailySummaryScheduleAdapter adapter = new DailySummaryScheduleAdapter(mContext, lstSchedule4Date);
			viewHolder.lvSchedule.setAdapter(adapter);
		}

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

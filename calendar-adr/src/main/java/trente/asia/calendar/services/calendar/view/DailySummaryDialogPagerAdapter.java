package trente.asia.calendar.services.calendar.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.DailySummaryDialog;
import trente.asia.calendar.services.calendar.model.ScheduleModel;

//import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;

/**
 * SchedulesPagerAdapter
 *
 * @author Vietnh
 */
public class DailySummaryDialogPagerAdapter extends PagerAdapter{

	private final List<Date>								dates;
	private final LayoutInflater							mInflater;
	private List<ScheduleModel>								lstSchedule;
	private DailySummaryDialog.OnAddBtnClickedListener		onAddBtnClickedListener;
	private DailyScheduleList.OnScheduleItemClickListener	listener;
	Map<Date, List<ScheduleModel>>							daySchedulesMap;
	private DailySummaryDialog								dialog;

	public DailySummaryDialogPagerAdapter(DailySummaryDialog dialog, Context context, List<Date> dates){
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.dialog = dialog;
		this.dates = dates;
	}

	/**
	 * Instantiate the {@link View} which should be displayed at {@code position}. Here we
	 * inflate a layout from the apps resources and then change the text view to signify the position.
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position){
		View view = mInflater.inflate(R.layout.dialog_daily_summary_page, null);

		DailyScheduleList dailyScheduleListView;
		ImageView imgAdd;
		TextView txtHeader;
		final TextView txtNoSchedule;

		txtHeader = (TextView)view.findViewById(R.id.txt_dialog_common_daily_summary);
		txtNoSchedule = (TextView)view.findViewById(R.id.txt_dialog_common_daily_summary_no_schedule);
		imgAdd = (ImageView)view.findViewById(R.id.img_id_add);

		final Date selectedDate = dates.get(position);
		imgAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(onAddBtnClickedListener != null){
					dialog.dismiss();// // TODO: 3/13/2017
					onAddBtnClickedListener.onAddBtnClick(selectedDate);
				}
			}
		});
		dailyScheduleListView = (DailyScheduleList)view.findViewById(R.id.lnr_view_daily_schedules);
		dailyScheduleListView.init(mInflater, new DailyScheduleList.OnScheduleItemClickListener() {

			@Override
			public void onClickScheduleItem(ScheduleModel schedule, Date selectedDate){
				if(!schedule.publicMode){
					return;
				}
				dialog.dismiss();// // TODO: 3/13/2017
				listener.onClickScheduleItem(schedule, selectedDate);
			}
		});

		txtHeader.setText(CCFormatUtil.formatDateCustom("yyyy/M/d", selectedDate));
		dailyScheduleListView.hasDisplayedItem = false;
		dailyScheduleListView.initDataWithMap(this.daySchedulesMap);
		dailyScheduleListView.showFor(selectedDate);
		if(!dailyScheduleListView.hasDisplayedItem){
			dailyScheduleListView.setVisibility(View.GONE);
			txtNoSchedule.setVisibility(View.VISIBLE);
		}else{
			dailyScheduleListView.setVisibility(View.VISIBLE);
			txtNoSchedule.setVisibility(View.GONE);
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

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return dates.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object o){
		return o == view;
	}

	public void setData(List<ScheduleModel> lstSchedule, DailySummaryDialog.OnAddBtnClickedListener onAddBtnClickedListener, DailyScheduleList.OnScheduleItemClickListener listener, DailySummaryDialog dialog){
		this.lstSchedule = lstSchedule;
		this.onAddBtnClickedListener = onAddBtnClickedListener;
		this.listener = listener;
		this.dialog = dialog;
		this.daySchedulesMap = DailyScheduleList.buildDaySchedulesMap(dates, this.lstSchedule);
	}

	public int getPositionByDate(Date selectedDate){
		for(int i = 0; i < dates.size(); i++){
			if(CCDateUtil.compareDate(dates.get(i), selectedDate, false) == 0){
				return i;
			}
		}
		return 0;
	}
}

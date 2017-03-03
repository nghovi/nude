package trente.asia.calendar.commons.dialogs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.ScheduleDetailFragment;
import trente.asia.calendar.services.calendar.listener.OnScheduleClickListener;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.DailySummaryPagerAdapter;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * ClDailySummaryDialog
 *
 * @author TrungND
 */
public class ClDailySummaryDialog extends ChiaseDialog{

	private Context								mContext;
	private ViewPager							viewPagerSchedule;
	private TextView							txtActiveDate;

	private DailySummaryPagerAdapter			pagerAdapter;
	private Map<String, List<ScheduleModel>>	mapSchedule;

	private final int							ACTIVE_PAGE				= Integer.MAX_VALUE / 2;
	private final Date							TODAY					= CCDateUtil.makeCalendarToday().getTime();

	private OnScheduleClickListener				scheduleClickListener	= new OnScheduleClickListener() {

																			@Override
																			public void onScheduleClickListener(ScheduleModel scheduleModel){
																				ClDailySummaryDialog.this.dismiss();
																				ScheduleDetailFragment detailFragment = new ScheduleDetailFragment();
																				detailFragment.setSchedule(scheduleModel);
																				((WelfareActivity)mContext).addFragment(detailFragment);
																			}
																		};

	public ClDailySummaryDialog(Context context, List<MonthlyCalendarDayView> lstDayView){
		super(context);
		this.setContentView(R.layout.dialog_common_daily_summary);
		this.mContext = context;

		txtActiveDate = (TextView)this.findViewById(R.id.txt_id_active_date);
		viewPagerSchedule = (ViewPager)this.findViewById(R.id.view_pager_id_schedule);

		convertList2Map(lstDayView);

		pagerAdapter = new DailySummaryPagerAdapter(mContext, mapSchedule, scheduleClickListener);
		viewPagerSchedule.setAdapter(pagerAdapter);
		viewPagerSchedule.setCurrentItem(ACTIVE_PAGE);
		setActiveDate(ACTIVE_PAGE);

		viewPagerSchedule.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			public void onPageScrollStateChanged(int state){
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
			}

			public void onPageSelected(int position){
				setActiveDate(position);
			}
		});
	}

	private void convertList2Map(List<MonthlyCalendarDayView> lstDayView){
		mapSchedule = new HashMap<>();
		if(!CCCollectionUtil.isEmpty(lstDayView)){
			for(MonthlyCalendarDayView dayView : lstDayView){
				mapSchedule.put(dayView.day, dayView.lstSchedule);
				// for(ScheduleModel scheduleModel : lstSchedule){
				// if(ClUtil.belongPeriod(date, scheduleModel.startDate, scheduleModel.endDate)){
				// String dateFormat = WelfareFormatUtil.formatDate(date);
				// if(mapSchedule.containsKey(dateFormat)){
				// List<ScheduleModel> lstSchedule4Date = mapSchedule.get(dateFormat);
				// lstSchedule4Date.add(scheduleModel);
				// mapSchedule.put(dateFormat, lstSchedule4Date);
				// }else{
				// List<ScheduleModel> lstSchedule4Date = new ArrayList<>();
				// lstSchedule4Date.add(scheduleModel);
				// mapSchedule.put(dateFormat, lstSchedule4Date);
				// }
				// }
				// }
			}
		}
	}

	private void setActiveDate(int position){
		Date activeDate = CsDateUtil.addDate(TODAY, position - ACTIVE_PAGE);
		txtActiveDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeDate));
	}

	public void setActiveDate(Date activeDate){
		if(activeDate != null){
			int diff = CsDateUtil.diffDate(activeDate, TODAY);
			int activePosition = ACTIVE_PAGE + diff;
			viewPagerSchedule.setCurrentItem(activePosition);
			setActiveDate(activePosition);
		}
	}

	@Override
	public void show(){
		super.show();
	}
}

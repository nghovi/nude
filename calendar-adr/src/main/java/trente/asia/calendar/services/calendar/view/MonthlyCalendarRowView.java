package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * MonthlyCalendarRowView
 *
 * @author TrungND
 */
public class MonthlyCalendarRowView extends RelativeLayout{

	public static final int				TEXT_SIZE		= 10;
	private Context						mContext;
	public List<MonthlyCalendarDayView>	lstCalendarDay;

	public List<TextView>				lstTextPeriod	= new ArrayList<>();
	private List<ScheduleModel>			schedules		= new ArrayList<>();
	private RelativeLayout				rltPeriod;

	public void setStartDate(Date startDate){
		this.startDate = startDate;
		Calendar cEnd = CCDateUtil.makeCalendar(startDate);
		cEnd.add(Calendar.DATE, 6);
		this.endDate = cEnd.getTime();
		this.rltPeriod = (RelativeLayout)findViewById(R.id.rlt_row_content);
	}

	private Date	startDate;
	private Date	endDate;

	// private int indexOfSchedule = 0;

	public MonthlyCalendarRowView(Context context){
		super(context);
		this.mContext = context;
		initialization();
	}

	public MonthlyCalendarRowView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
		initialization();
	}

	public MonthlyCalendarRowView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
		initialization();
	}

	public void initialization(){
		this.lstCalendarDay = new ArrayList<>();
	}

	public void addSchedule(ScheduleModel scheduleModel){
		schedules.add(scheduleModel);
	}

	public static Comparator<ScheduleModel> getPeriodScheduleComparator(final Date startDate, final Date endDate){
		return new Comparator<ScheduleModel>() {

			@Override
			public int compare(ScheduleModel schedule1, ScheduleModel schedule2){

				if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(schedule1.scheduleType) && !ClConst.SCHEDULE_TYPE_HOLIDAY.equals(schedule2.scheduleType)){
					return -1;
				}else if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(schedule2.scheduleType) && !ClConst.SCHEDULE_TYPE_HOLIDAY.equals(schedule1.scheduleType)){
					return 1;
				}else if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(schedule1.scheduleType) && ClConst.SCHEDULE_TYPE_HOLIDAY.equals(schedule2.scheduleType)){
					return compareOffer(schedule1, schedule2);
				}

				if(ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(schedule1.scheduleType) && !ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(schedule2.scheduleType)){
					return -1;
				}else if(ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(schedule2.scheduleType) && !ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(schedule1.scheduleType)){
					return 1;
				}else if(ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(schedule1.scheduleType) && ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(schedule2.scheduleType)){
					return compareOffer(schedule1, schedule2);
				}

				Date startDate1 = schedule1.startDate;
				Date startDate2 = schedule2.startDate;

				Date endDate1 = schedule1.endDate;
				Date endDate2 = schedule2.endDate;

				startDate1 = CCDateUtil.compareDate(startDate1, startDate, false) <= 0 ? startDate : startDate1;
				startDate2 = CCDateUtil.compareDate(startDate2, startDate, false) <= 0 ? startDate : startDate2;

				int startCompareResult = CCDateUtil.compareDate(startDate1, startDate2, false);

				if(startCompareResult == 0){
					endDate1 = CCDateUtil.compareDate(endDate1, endDate, false) >= 0 ? endDate : endDate1;
					endDate2 = CCDateUtil.compareDate(endDate2, endDate, false) >= 0 ? endDate : endDate2;

					long startDate1Long = CCDateUtil.makeDate(startDate1).getTime();
					long startDate2Long = CCDateUtil.makeDate(startDate2).getTime();

					long period1 = CCDateUtil.makeDate(endDate1).getTime() - startDate1Long;
					long period2 = CCDateUtil.makeDate(endDate2).getTime() - startDate2Long;

					int lengthCompareResult = Long.compare(period2, period1);
					if(lengthCompareResult == 0){
						return schedule1.scheduleName.compareTo(schedule2.scheduleName);
					}
					return lengthCompareResult;
				}

				return startCompareResult;
			}
		};
	}

	private void sortSchedules(){
		Collections.sort(schedules, getPeriodScheduleComparator(startDate, endDate));
	}

	public static int compareOffer(ScheduleModel schedule1, ScheduleModel schedule2){
		return schedule1.scheduleName.compareTo(schedule2.scheduleName);
	}

	private List<MonthlyCalendarDayView> getPassiveCalendarDays(List<MonthlyCalendarDayView>

	lstCalendarDay, List<MonthlyCalendarDayView> lstActiveCalendarDay){
		List<MonthlyCalendarDayView> result = new ArrayList<>();
		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			if(!lstActiveCalendarDay.contains(dayView)){
				result.add(dayView);
			}
		}
		return result;
	}

	public void refreshLayout(){
		sortSchedules();
		for(int i = 0; i < schedules.size(); i++){
			ScheduleModel scheduleModel = schedules.get(i);
			showSchedule(scheduleModel, i);
		}
	}

	private void showSchedule(ScheduleModel scheduleModel, int i){
		double itemWidth = this.getWidth() / 7.0;

		// Date startDate = WelfareFormatUtil.makeDate(WelfareFormatUtil.removeTime4Date(scheduleModel.startDate));
		// Date endDate = WelfareFormatUtil.makeDate(WelfareFormatUtil.removeTime4Date(scheduleModel.endDate));

		List<MonthlyCalendarDayView> lstActiveCalendarDay = new ArrayList<>();
		List<MonthlyCalendarDayView> lstPassiveCalendarDay = new ArrayList<>();

		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			if(ClUtil.belongPeriod(dayView.date, scheduleModel.startDate, scheduleModel.endDate)){
				lstActiveCalendarDay.add(dayView);
			}else{
				lstPassiveCalendarDay.add(dayView);
			}
		}

		int marginTop = ClUtil.getMaxInList(lstActiveCalendarDay) + ClConst.TEXT_VIEW_HEIGHT;

		for(MonthlyCalendarDayView dayView : lstActiveCalendarDay){
			dayView.addPeriod(scheduleModel, marginTop);
		}

		for(MonthlyCalendarDayView dayView : lstPassiveCalendarDay){
			dayView.addPassivePeriod(scheduleModel, marginTop * schedules.size());
		}
		MonthlyCalendarDayView theFirstCalendarDay = lstActiveCalendarDay.get(0);
		// MonthlyCalendarDayView theLastCalendarDay = lstActiveCalendarDay.get(lstActiveCalendarDay.size() - 1);

		int numCol = lstActiveCalendarDay.size();
		int width = (int)(itemWidth * numCol);
		// width = width - WelfareUtil.dpToPx(1);
		int marginLeft = (int)(itemWidth * theFirstCalendarDay.dayOfTheWeek);
		int marginTopAfter = marginTop + WelfareUtil.dpToPx(5);

		TextView txtSchedule = createTextView(getContext(), width, marginLeft, scheduleModel, marginTopAfter + WelfareUtil.dpToPx(4));

		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleModel.scheduleType)){
			txtSchedule.setTextColor(Color.RED);
		}

		this.lstTextPeriod.add(txtSchedule);
		this.rltPeriod.addView(txtSchedule);
	}

	public static TextView createTextView(Context context, int width, int marginLeft, ScheduleModel scheduleModel, int marginTop){
		LayoutParams layoutParams = new LayoutParams(width, ClConst.TEXT_VIEW_HEIGHT);
		layoutParams.setMargins(marginLeft - WelfareUtil.dpToPx(0), marginTop, 0, 0);
		TextView txtSchedule = new TextView(context);
		txtSchedule.setMaxLines(1);
		txtSchedule.setLayoutParams(layoutParams);
		txtSchedule.setGravity(Gravity.CENTER_VERTICAL);
		txtSchedule.setTextColor(Color.BLACK);
		if(scheduleModel.isAllDay || scheduleModel.isPeriod){
			txtSchedule.setBackground(ContextCompat.getDrawable(context, R.drawable.wf_background_black_border2));
		}
		txtSchedule.setPadding(WelfareUtil.dpToPx(2), 0, 0, 0);

		txtSchedule.setTextSize(TEXT_SIZE);
		txtSchedule.setText(scheduleModel.scheduleName);
		return txtSchedule;
	}

	public void removeAllData(){
		if(!CCCollectionUtil.isEmpty(this.lstTextPeriod)){
			for(TextView textView : this.lstTextPeriod){
				this.rltPeriod.removeView(textView);
			}
		}
		this.lstTextPeriod.clear();
		this.schedules.clear();
	}
}

package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * MonthlyCalendarRowView
 *
 * @author TrungND
 */
public class MonthlyCalendarRowView extends RelativeLayout{

	private Context						mContext;
	public List<MonthlyCalendarDayView>	lstCalendarDay;

	public List<TextView>				lstTextPeriod	= new ArrayList<>();
	private List<ScheduleModel>			schedules		= new ArrayList<>();

	public void setStartDate(Date startDate){
		this.startDate = startDate;
		Calendar cEnd = CCDateUtil.makeCalendar(startDate);
		cEnd.add(Calendar.DATE, 7);
		this.endDate = cEnd.getTime();
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

	private void sortSchedules(){
		if(!CCCollectionUtil.isEmpty(schedules)){
			Collections.sort(schedules, new Comparator<ScheduleModel>() {

				@Override
				public int compare(ScheduleModel schedule1, ScheduleModel schedule2){
					Date startDate1 = WelfareUtil.makeDate(schedule1.startDate);
					Date startDate2 = WelfareUtil.makeDate(schedule2.startDate);

					Date endDate1 = WelfareUtil.makeDate(schedule1.endDate);
					Date endDate2 = WelfareUtil.makeDate(schedule2.endDate);

					startDate1 = CCDateUtil.compareDate(startDate1, startDate, false) < 0 ? startDate : startDate1;
					startDate2 = CCDateUtil.compareDate(startDate2, startDate, false) < 0 ? startDate : startDate2;

					endDate1 = CCDateUtil.compareDate(endDate1, endDate, false) > 0 ? endDate : endDate1;
					endDate2 = CCDateUtil.compareDate(endDate2, endDate, false) > 0 ? endDate : endDate2;

					long startDate1Long = CCDateUtil.makeDate(startDate1).getTime();
					long startDate2Long = CCDateUtil.makeDate(startDate2).getTime();

					long period1 = CCDateUtil.makeDate(endDate1).getTime() - startDate1Long;
					long period2 = CCDateUtil.makeDate(endDate2).getTime() - startDate2Long;

					int lengthCompareResult = Long.compare(period2, period1);
					if(lengthCompareResult == 0){
						return Long.compare(startDate1Long, startDate2Long);
					}
					return lengthCompareResult;
				}
			});
		}
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
		for(ScheduleModel scheduleModel : schedules){
			showSchedule(scheduleModel);
		}

		int maxSchedule = 0;
		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			if(dayView.getNumberOfSchedule() > maxSchedule){
				maxSchedule = dayView.getNumberOfSchedule();
			}
		}
		// refresh layout
		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			LinearLayout.LayoutParams layoutParamsDay = new LinearLayout.LayoutParams(0, (int)getResources().getDimension(R.dimen.margin_40dp) + maxSchedule * ClConst.TEXT_VIEW_HEIGHT, 1);
			dayView.setLayoutParams(layoutParamsDay);
		}
	}

	private void showSchedule(ScheduleModel scheduleModel){
		double itemWidth = this.getWidth() / 7.0;
		int textSize = 10;

		Date startDate = WelfareFormatUtil.makeDate(WelfareFormatUtil.removeTime4Date(scheduleModel.startDate));
		Date endDate = WelfareFormatUtil.makeDate(WelfareFormatUtil.removeTime4Date(scheduleModel.endDate));
		List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findListView4Day(lstCalendarDay, startDate, endDate);
		List<MonthlyCalendarDayView> lstPassiveCalendarDay = getPassiveCalendarDays(lstCalendarDay, lstActiveCalendarDay);

		for(MonthlyCalendarDayView dayView : lstActiveCalendarDay){
			dayView.addPeriod(scheduleModel);
		}

		for(MonthlyCalendarDayView dayView : lstPassiveCalendarDay){
			dayView.addPassivePeriod(scheduleModel);
		}
		MonthlyCalendarDayView theFirstCalendarDay = lstActiveCalendarDay.get(0);
		MonthlyCalendarDayView theLastCalendarDay = lstActiveCalendarDay.get(lstActiveCalendarDay.size() - 1);

		int marginTop = (int)getResources().getDimension(R.dimen.margin_20dp) + (ClUtil.getMaxInList(lstActiveCalendarDay) - 1) * ClConst.TEXT_VIEW_HEIGHT;
		LayoutParams layoutParams = new LayoutParams((int)(itemWidth * (theLastCalendarDay.dayOfTheWeek - theFirstCalendarDay.dayOfTheWeek + 1)) - 2, ClConst.TEXT_VIEW_HEIGHT);
		layoutParams.setMargins((int)(itemWidth * theFirstCalendarDay.dayOfTheWeek) + 1, marginTop, 1, 0);

		TextView txtSchedule = new TextView(mContext);
		txtSchedule.setMaxLines(1);
		txtSchedule.setLayoutParams(layoutParams);
		txtSchedule.setPadding(1, 0, 1, 0);

		// set data
		// txtSchedule.setPadding(2, 2, 2, 2);
		txtSchedule.setGravity(Gravity.CENTER_VERTICAL);
		txtSchedule.setTextColor(Color.WHITE);
		String scheduleColor = scheduleModel.getScheduleColor();
		if(!CCStringUtil.isEmpty(scheduleColor)){
			txtSchedule.setBackgroundColor(Color.parseColor(scheduleColor));
		}else{
			txtSchedule.setBackgroundColor(Color.RED);
		}

		txtSchedule.setTextSize(textSize);
		txtSchedule.setText(scheduleModel.scheduleName);
		this.lstTextPeriod.add(txtSchedule);
		this.addView(txtSchedule);
	}

	public void removeAllData(){
		if(!CCCollectionUtil.isEmpty(this.lstTextPeriod)){
			for(TextView textView : this.lstTextPeriod){
				this.removeView(textView);
			}
		}
		this.lstTextPeriod.clear();
		this.schedules.clear();
	}
}

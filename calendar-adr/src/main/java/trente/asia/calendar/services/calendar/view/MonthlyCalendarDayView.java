package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * MonthlyCalendarDayView
 *
 * @author TrungND
 */
public class MonthlyCalendarDayView extends LinearLayout{

	private Context						mContext;
	private TextView					txtContent;
	private LinearLayout				lnrRowContent;
	private DailyScheduleClickListener	mListener;

	private int							numberOfPeriod;
	// private int numberOfSchedule;
	private boolean						isTheFirst	= true;

	public String						day;
	public int							dayOfTheWeek;
	public List<ScheduleModel>			lstSchedule	= new ArrayList<>();

	public MonthlyCalendarDayView(Context context){
		super(context);
		this.mContext = context;

	}

	public MonthlyCalendarDayView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public MonthlyCalendarDayView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public void initialization(Date itemDate, DailyScheduleClickListener listener, boolean isWithoutMonth){
		LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		this.setLayoutParams(params);
		this.day = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, itemDate);
		this.mListener = listener;

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				if(mListener != null){
					mListener.onDailyScheduleClickListener(day);
				}
			}
		});

		txtContent = (TextView)this.findViewById(R.id.txt_id_row_content);
		txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_11, itemDate));
		Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
		if(isWithoutMonth){
			txtContent.setTextColor(Color.GRAY);
		}else{
			if(CCDateUtil.isToday(itemDate)){
				txtContent.setBackgroundResource(R.drawable.shape_background_today);
				txtContent.setTextColor(Color.WHITE);
			}else{
				if(Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
					txtContent.setTextColor(Color.RED);
				}else if(Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
					txtContent.setTextColor(Color.BLUE);
				}
			}
		}

		lnrRowContent = (LinearLayout)this.findViewById(R.id.lnr_id_row_content);
	}

	public void addSchedule(ScheduleModel scheduleModel){
		lstSchedule.add(scheduleModel);
		TextView txtSchedule = new TextView(mContext);
		txtSchedule.setMaxLines(1);
		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleModel.scheduleType)){
			setLayoutHoliday();
		}

		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ClConst.TEXT_VIEW_HEIGHT);
		layoutParams.setMargins(1, 0, 1, 0);
		txtSchedule.setLayoutParams(layoutParams);
		txtSchedule.setGravity(Gravity.CENTER_VERTICAL);
		txtSchedule.setPadding(1, 0, 1, 0);

		String scheduleColor = scheduleModel.getScheduleColor();
		if(CCBooleanUtil.checkBoolean(scheduleModel.isAllDay)){
			txtSchedule.setTextColor(Color.WHITE);
			if(!CCStringUtil.isEmpty(scheduleColor)){
				txtSchedule.setBackgroundColor(Color.parseColor(scheduleColor));
			}else{
				txtSchedule.setBackgroundColor(Color.RED);
			}
		}else{
			if(!CCStringUtil.isEmpty(scheduleColor)){
				txtSchedule.setTextColor(Color.parseColor(scheduleColor));
			}
		}

		int textSize = 10;
		txtSchedule.setTextSize(textSize);

		txtSchedule.setText(scheduleModel.scheduleName);
		if(isTheFirst){
			isTheFirst = false;
			setMarginTop();
		}
		lnrRowContent.addView(txtSchedule);
	}

	private void setMarginTop(){
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, numberOfPeriod * ClConst.TEXT_VIEW_HEIGHT, 0, 0);
		lnrRowContent.setLayoutParams(layoutParams);
	}

	public void removeAllData(){
		lnrRowContent.removeAllViews();
		numberOfPeriod = 0;
		lstSchedule.clear();
		isTheFirst = true;
	}

	public void addPeriod(ScheduleModel scheduleModel){
		numberOfPeriod++;
		lstSchedule.add(scheduleModel);
		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleModel.scheduleType)){
			setLayoutHoliday();
		}
	}

	public int getNumberOfSchedule(){
		return lstSchedule.size();
	}

	private void setLayoutHoliday(){
		txtContent.setBackgroundResource(R.drawable.shape_background_holiday);
		txtContent.setTextColor(Color.WHITE);
	}
}

package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

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
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

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

	public String						day;
	public int							dayOfTheWeek;
	private int							numberOfPeriod;
	private int							numberOfSchedule;
	private boolean						isTheFirst	= true;

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
		numberOfSchedule++;
		TextView txtSchedule = new TextView(mContext);
		txtSchedule.setMaxLines(1);
		if(CCBooleanUtil.checkBoolean(scheduleModel.isHoliday)){
			setLayoutHoliday();
		}

		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ClConst.TEXT_VIEW_HEIGHT);
		txtSchedule.setLayoutParams(layoutParams);
		txtSchedule.setGravity(Gravity.CENTER_VERTICAL);

		if(CCBooleanUtil.checkBoolean(scheduleModel.isAllDay)){
			// txtSchedule.setPadding(2, 2, 2, 2);
			txtSchedule.setTextColor(Color.WHITE);
			if(!CCStringUtil.isEmpty(scheduleModel.scheduleColor)){
				txtSchedule.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(scheduleModel.scheduleColor)));
			}else{
				txtSchedule.setBackgroundColor(Color.RED);
			}
		}else{
			if(!CCStringUtil.isEmpty(scheduleModel.scheduleColor)){
				txtSchedule.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(scheduleModel.scheduleColor)));
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
	}

	public void addPeriod(ScheduleModel scheduleModel){
		numberOfPeriod++;
		numberOfSchedule++;
		if(CCBooleanUtil.checkBoolean(scheduleModel.isHoliday)){
			setLayoutHoliday();
		}
	}

	public int getNumberOfSchedule(){
		return numberOfSchedule;
	}

	private void setLayoutHoliday(){
		txtContent.setBackgroundResource(R.drawable.shape_background_holiday);
        txtContent.setTextColor(Color.WHITE);
	}
}

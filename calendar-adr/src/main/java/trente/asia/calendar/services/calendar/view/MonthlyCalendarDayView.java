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
import android.widget.ImageView;
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

/**
 * MonthlyCalendarDayView
 *
 * @author TrungND
 */
public class MonthlyCalendarDayView extends LinearLayout{

	private Context						mContext;
	private TextView					txtDayLabel;
	private LinearLayout				lnrRowContent;
	private DailyScheduleClickListener	mListener;

	// private int numberOfSchedule;
	private boolean						isTheFirst	= true;

	public String						day;
	public int							dayOfTheWeek;
	public List<ScheduleModel>			lstSchedule	= new ArrayList<>();
	private int							periodNum;
	private int							lastPeriodNum;
	private boolean						isToday		= false;
	private TextView					txtHoliday;

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
		this.day = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, itemDate);
		this.mListener = listener;

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				if(mListener != null){
					mListener.onDailyScheduleClickListener(day);
				}
			}
		});

		txtHoliday = (TextView)this.findViewById(R.id.txt_id_row_holiday);

		txtDayLabel = (TextView)this.findViewById(R.id.txt_id_day_label);
		txtDayLabel.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DD, itemDate));
		Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
		if(isWithoutMonth){
			txtDayLabel.setTextColor(Color.GRAY);
		}else{
			if(CCDateUtil.isToday(itemDate)){
				isToday = true;
				txtDayLabel.setBackgroundResource(R.drawable.shape_background_today);
				txtDayLabel.setTextColor(Color.WHITE);
			}else{
				if(Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
					txtDayLabel.setTextColor(Color.RED);
				}else if(Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
					txtDayLabel.setTextColor(Color.BLUE);
				}
			}
		}

		lnrRowContent = (LinearLayout)this.findViewById(R.id.lnr_id_row_content);
	}

	public void addSchedule(ScheduleModel scheduleModel){
		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleModel.scheduleType)){
			setLayoutHoliday(scheduleModel);
		}else if(ClConst.SCHEDULE_TYPE_BIRTHDAY.equals(scheduleModel.scheduleType)){
			setLayoutBirthday(scheduleModel);
		}else{
			lstSchedule.add(scheduleModel);
			TextView txtSchedule = new TextView(mContext);
			txtSchedule.setMaxLines(1);

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
	}

	private void setMarginTop(){
		LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(0, lastPeriodNum * ClConst.TEXT_VIEW_HEIGHT, 0, 0);
		lnrRowContent.setLayoutParams(layoutParams);
	}

	public void removeAllData(){
		lnrRowContent.removeAllViews();
		periodNum = 0;
		lastPeriodNum = 0;
		lstSchedule.clear();
		isTheFirst = true;
	}

	public void addPeriod(ScheduleModel scheduleModel){
		periodNum++;
		lastPeriodNum = periodNum;
		lstSchedule.add(scheduleModel);
		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleModel.scheduleType)){
			setLayoutHoliday(scheduleModel);
		}
	}

	public int getNumberOfSchedule(){
		return lstSchedule.size();
	}

	private void setLayoutHoliday(ScheduleModel scheduleModel){
		txtHoliday.setVisibility(View.VISIBLE);
		txtHoliday.setTextColor(Color.RED);
		txtHoliday.setText(scheduleModel.scheduleName);
		if(!isToday){
			// txtDayLabel.setBackgroundResource(R.drawable.shape_background_holiday);
			txtDayLabel.setTextColor(Color.RED);
		}else{
			txtDayLabel.setTextColor(Color.WHITE);
		}
	}

	public void addPassivePeriod(ScheduleModel scheduleModel){
		periodNum++;
	}

	public void setLayoutBirthday(ScheduleModel scheduleModel){
		ImageView imgBirthday = (ImageView)findViewById(R.id.img_birthday);
		imgBirthday.setVisibility(View.VISIBLE);
	}
}

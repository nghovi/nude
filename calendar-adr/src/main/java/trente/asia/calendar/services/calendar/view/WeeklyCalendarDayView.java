package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * WeeklyCalendarDayView
 *
 * @author TrungND
 */
public class WeeklyCalendarDayView extends LinearLayout{

	private Context				mContext;
	public String				dayStr;
	public CalendarDayModel		calendarDayModel;
	private OnDayClickListener	onDayClickListener;
	private List<HolidayModel>	holidayModels;
	private TextView			txtContent;
	private int					bgResource		= 0;
	private TextView			txtScheduleMark;
	View						rowItemView;
	private boolean				isInOtherMonth	= false;

	public void setDate(Date date){
		this.date = date;
	}

	private Date	date;

	public Date getDate(){
		return date;
	}

	public interface OnDayClickListener{

		public void onDayClick(String dayStr);
	}

	public WeeklyCalendarDayView(Context context){
		super(context);
		this.mContext = context;

	}

	public WeeklyCalendarDayView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public WeeklyCalendarDayView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public void setData(final CalendarDayModel calendarDayModel, OnDayClickListener listener, List<HolidayModel> holidayModels){
		this.calendarDayModel = calendarDayModel;
		this.onDayClickListener = listener;
		this.holidayModels = holidayModels;

		HolidayModel holidayModel = HolidayModel.getHolidayModel(this.date, this.holidayModels);
		if(bgResource == 0 && holidayModel != null){
			this.bgResource = R.drawable.circle_background_holiday;
		}

		rowItemView.setBackgroundResource(this.bgResource);

		if(calendarDayModel != null && !CCCollectionUtil.isEmpty(calendarDayModel.schedules)){
			txtScheduleMark.setVisibility(View.VISIBLE);
		}else{
			txtScheduleMark.setVisibility(View.INVISIBLE);
		}
	}

	public void setSelected(boolean selected){
		if(selected){
			rowItemView.setBackgroundResource(R.drawable.circle_background_selected);
		}else{
			rowItemView.setBackgroundResource(this.bgResource);
		}
	}

	public void initLayout(Date itemDate, final boolean isInOtherMonth){
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		this.setLayoutParams(params);
		this.date = CCDateUtil.makeDate(itemDate);
		this.dayStr = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, this.date);
		this.isInOtherMonth = isInOtherMonth;

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout itemView = (LinearLayout)inflater.inflate(R.layout.weekly_calendar_row_header_item, null);
		rowItemView = itemView.findViewById(R.id.rlt_day_view);
		txtContent = (TextView)rowItemView.findViewById(R.id.txt_id_row_content);
		LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		itemView.setLayoutParams(layoutParams);

		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				if(onDayClickListener != null && calendarDayModel != null && isInOtherMonth == false){
					onDayClickListener.onDayClick(dayStr);
				}
			}
		});

		txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_11, itemDate));
		Calendar c = Calendar.getInstance();
		if(CCDateUtil.compareDate(c.getTime(), this.date, false) == 0){
			this.bgResource = R.drawable.circle_background_today;
		}

		setSelected(false);

		txtScheduleMark = (TextView)itemView.findViewById(R.id.txt_id_row_schedule_mark);

		Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
		if(this.isInOtherMonth){
			txtContent.setTextColor(Color.GRAY);
		}else{
			if(Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
				txtContent.setTextColor(Color.RED);
			}else if(Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
				txtContent.setTextColor(Color.BLUE);
			}
		}
		this.addView(itemView);
	}
}

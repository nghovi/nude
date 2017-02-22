package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

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
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * MonthlyCalendarDayView
 *
 * @author TrungND
 */
public class WeeklyCalendarDayView extends LinearLayout{

	private Context				mContext;
	public String				dayStr;
	private CalendarDayModel	calendarDayModel;
	private OnDayClickListener	onDayClickListener;

	public void setDate(Date date){
		this.date = date;
	}

	private Date	date;

	public Date getDate() {
		return date;
	}

	public interface OnDayClickListener{

		public void onClick(CalendarDayModel calendarDayModel);
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

	public void setData(final CalendarDayModel calendarDayModel, OnDayClickListener listener){
		this.calendarDayModel = calendarDayModel;
		this.onDayClickListener = listener;
	}

	public void initialization(Date itemDate){
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		this.setLayoutParams(params);
		this.date = itemDate;
		this.dayStr = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, this.date);

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowItemView = inflater.inflate(R.layout.weekly_calendar_row_header_item, null);
		if(onDayClickListener != null && calendarDayModel != null){
			rowItemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v){
					onDayClickListener.onClick(calendarDayModel);
				}
			});
		}
		LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		rowItemView.setLayoutParams(layoutParams);

		TextView txtContent = (TextView)rowItemView.findViewById(R.id.txt_id_row_content);
		txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_11, itemDate));
		PreferencesAccountUtil preferencesAccountUtil = new PreferencesAccountUtil(mContext);
		String itemDateString = WelfareFormatUtil.formatDate(itemDate);
		String activeDateString = preferencesAccountUtil.getActiveDate();
		if(itemDateString.equals(activeDateString)){
			txtContent.setBackgroundResource(R.drawable.shape_active_date_border);
		}

		TextView txtScheduleMark = (TextView)rowItemView.findViewById(R.id.txt_id_row_schedule_mark);
		if(calendarDayModel != null && !CCCollectionUtil.isEmpty(calendarDayModel.schedules)){
			txtScheduleMark.setVisibility(View.VISIBLE);
		}else{
			txtScheduleMark.setVisibility(View.GONE);
		}

		Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
		if(Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
			txtContent.setTextColor(Color.RED);
		}else if(Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
			txtContent.setTextColor(Color.BLUE);
		}

		this.addView(rowItemView);
	}
}

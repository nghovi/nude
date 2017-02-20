package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
	public String				day;
	private CalendarDayModel	calendarDayModel;
	private OnDayClickListener	onDayClickListener;

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

	public void initialization(Date itemDate, final CalendarDayModel calendarDayModel, OnDayClickListener listener){
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		this.setLayoutParams(params);
		this.day = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, itemDate);
		this.calendarDayModel = calendarDayModel;
		this.onDayClickListener = listener;

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

		ImageView imgScheduleSign = (ImageView)rowItemView.findViewById(R.id.img_schedule_sign);
		if(calendarDayModel != null && !CCCollectionUtil.isEmpty(calendarDayModel.schedules)){
			imgScheduleSign.setVisibility(View.VISIBLE);
		}else{
			imgScheduleSign.setVisibility(View.GONE);
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

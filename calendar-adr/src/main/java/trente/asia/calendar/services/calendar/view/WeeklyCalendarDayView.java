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

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * MonthlyCalendarDayView
 *
 * @author TrungND
 */
public class WeeklyCalendarDayView extends LinearLayout{

	private Context			mContext;
	public String			day;

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

	public void initialization(Date itemDate){
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		this.setLayoutParams(params);
		this.day = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, itemDate);

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowItemView = inflater.inflate(R.layout.weekly_calendar_row_header_item, null);
		LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		rowItemView.setLayoutParams(layoutParams);

		TextView txtContent = (TextView)rowItemView.findViewById(R.id.txt_id_row_content);
		txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_11, itemDate));
		Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
		if(Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
			txtContent.setTextColor(Color.RED);
		}else if(Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
			txtContent.setTextColor(Color.BLUE);
		}

		this.addView(rowItemView);
	}
}

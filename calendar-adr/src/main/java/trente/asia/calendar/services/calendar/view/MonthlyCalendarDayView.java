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

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
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
	private LinearLayout				lnrRowContent;

	public String						day;
	private DailyScheduleClickListener	mListener;

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

	public void initialization(Date itemDate, DailyScheduleClickListener listener){
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		this.setLayoutParams(params);
		this.day = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, itemDate);
		this.mListener = listener;

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowItemView = inflater.inflate(R.layout.monthly_calendar_row_item, null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
		rowItemView.setLayoutParams(layoutParams);
		rowItemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				if(mListener != null){
					mListener.onDailyScheduleClickListener(day);
				}
			}
		});

		TextView txtContent = (TextView)rowItemView.findViewById(R.id.txt_id_row_content);
		txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_11, itemDate));
		Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
		if(Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
			txtContent.setTextColor(Color.RED);
		}else if(Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
			txtContent.setTextColor(Color.BLUE);
		}
		lnrRowContent = (LinearLayout)rowItemView.findViewById(R.id.lnr_id_row_content);

		this.addView(rowItemView);
	}

	public void addSchedule(ScheduleModel scheduleModel){
		TextView txtSchedule = new TextView(mContext);
		txtSchedule.setMaxLines(1);
		// txtSchedule.setEllipsize(TextUtils.TruncateAt.END);
		if(!CCBooleanUtil.checkBoolean(scheduleModel.isDayPeriod) && !CCBooleanUtil.checkBoolean(scheduleModel.isRepeat)){
			if(!CCStringUtil.isEmpty(scheduleModel.scheduleColor)){
				txtSchedule.setTextColor(Color.parseColor(WelfareFormatUtil.formatColor(scheduleModel.scheduleColor)));
			}
		}else{
			txtSchedule.setPadding(2, 2, 2, 2);
			txtSchedule.setTextColor(Color.WHITE);
			txtSchedule.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(scheduleModel.scheduleColor)));
		}

		int textSize = (int)(getResources().getDimension(R.dimen.margin_12dp) / getResources().getDisplayMetrics().density);
		txtSchedule.setTextSize(textSize);

		txtSchedule.setText(scheduleModel.scheduleName);
		lnrRowContent.addView(txtSchedule);
	}

    public void removeAllData(){
        lnrRowContent.removeAllViews();
    }
}

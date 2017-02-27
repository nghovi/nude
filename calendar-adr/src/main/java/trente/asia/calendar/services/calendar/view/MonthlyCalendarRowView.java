package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * MonthlyCalendarRowView
 *
 * @author TrungND
 */
public class MonthlyCalendarRowView extends RelativeLayout{

	private Context						mContext;
	public List<MonthlyCalendarDayView>	lstCalendarDay;

	private int							indexOfSchedule	= 0;

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
		indexOfSchedule++;
		int itemWidth = this.getWidth() / 7;
		int textSize = 10;

		List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, scheduleModel.startDate, scheduleModel.endDate);
		MonthlyCalendarDayView theFirstCalendarDay = lstActiveCalendarDay.get(0);
		MonthlyCalendarDayView theLastCalendarDay = lstActiveCalendarDay.get(lstActiveCalendarDay.size() - 1);

		int marginTop = (int)getResources().getDimension(R.dimen.margin_30dp) + (indexOfSchedule - 1) * ClConst.TEXT_VIEW_HEIGHT;
		LayoutParams layoutParams = new LayoutParams(itemWidth * (theLastCalendarDay.dayOfTheWeek - theFirstCalendarDay.dayOfTheWeek + 1), ClConst.TEXT_VIEW_HEIGHT);
		layoutParams.setMargins(itemWidth * theFirstCalendarDay.dayOfTheWeek, marginTop, 0, 0);

		TextView txtSchedule = new TextView(mContext);
		txtSchedule.setMaxLines(1);
		txtSchedule.setLayoutParams(layoutParams);

		// set data
		// txtSchedule.setPadding(2, 2, 2, 2);
		txtSchedule.setGravity(CENTER_VERTICAL);
		txtSchedule.setTextColor(Color.WHITE);
		if(!CCStringUtil.isEmpty(scheduleModel.scheduleColor)){
			txtSchedule.setBackgroundColor(Color.parseColor(WelfareFormatUtil.formatColor(scheduleModel.scheduleColor)));
		}else{
			txtSchedule.setBackgroundColor(Color.RED);
		}

		txtSchedule.setTextSize(textSize);
		txtSchedule.setText(scheduleModel.scheduleName);
		this.addView(txtSchedule);
	}

	public void refreshLayout(){
		if(indexOfSchedule != 0){
			for(MonthlyCalendarDayView dayView : lstCalendarDay){
				LinearLayout.LayoutParams layoutParamsDay = new LinearLayout.LayoutParams(0, dayView.getHeight() + indexOfSchedule * ClConst.TEXT_VIEW_HEIGHT, 1);
				dayView.setLayoutParams(layoutParamsDay);
			}
		}
	}
}
package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.CategoryModel;
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

	public List<TextView>				lstTextPeriod	= new ArrayList<>();
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

	public void addSchedule(ScheduleModel scheduleModel, List<CategoryModel> lstCategory){
		int itemWidth = this.getWidth() / 7;
		int textSize = 10;

		List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, scheduleModel.startDate, scheduleModel.endDate);
		for(MonthlyCalendarDayView dayView : lstActiveCalendarDay){
			dayView.addPeriod(scheduleModel);
		}
		MonthlyCalendarDayView theFirstCalendarDay = lstActiveCalendarDay.get(0);
		MonthlyCalendarDayView theLastCalendarDay = lstActiveCalendarDay.get(lstActiveCalendarDay.size() - 1);

        int textViewHeight = (int)getResources().getDimension(R.dimen.margin_15dp);
		int marginTop = (int)getResources().getDimension(R.dimen.margin_30dp) + (ClUtil.getMaxInList(lstActiveCalendarDay) - 1) * ClConst.TEXT_VIEW_HEIGHT;
		LayoutParams layoutParams = new LayoutParams(itemWidth * (theLastCalendarDay.dayOfTheWeek - theFirstCalendarDay.dayOfTheWeek + 1), ClConst.TEXT_VIEW_HEIGHT);
		layoutParams.setMargins(itemWidth * theFirstCalendarDay.dayOfTheWeek, marginTop, 0, 0);

		TextView txtSchedule = new TextView(mContext);
		txtSchedule.setMaxLines(1);
		txtSchedule.setLayoutParams(layoutParams);

		// set data
		// txtSchedule.setPadding(2, 2, 2, 2);
		txtSchedule.setGravity(Gravity.CENTER_VERTICAL);
		txtSchedule.setTextColor(Color.WHITE);
        String scheduleColor = scheduleModel.getScheduleColor(lstCategory);
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

	public void refreshLayout(){
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

	public void removeAllData(){
		if(!CCCollectionUtil.isEmpty(this.lstTextPeriod)){
			for(TextView textView : this.lstTextPeriod){
				this.removeView(textView);
			}
		}
        this.lstTextPeriod.clear();
//        this.lstCalendarDay.clear();

//        for(MonthlyCalendarDayView dayView : lstCalendarDay){
//            LinearLayout.LayoutParams layoutParamsDay = new LinearLayout.LayoutParams(0, (int)getResources().getDimension(R.dimen.margin_40dp), 1);
////            LinearLayout.LayoutParams layoutParamsDay = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//            dayView.setLayoutParams(layoutParamsDay);
//        }
	}
}

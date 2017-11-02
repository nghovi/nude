package nguyenhoangviet.vpcorp.calendar.services.calendar.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * WeeklyCalendarDayView
 *
 * @author TrungND
 */
public class WeeklyCalendarDayView extends CalendarDayView{

	@Override
	protected boolean isDayEnabled(){
		return true;
	}

	public WeeklyCalendarDayView(Context context){
		super(context);
	}

	public WeeklyCalendarDayView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
}

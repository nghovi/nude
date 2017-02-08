package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * MonthlyCalendarView
 *
 * @author TrungND
 */
public class MonthlyCalendarView extends LinearLayout{

	private Context mContext;

	public MonthlyCalendarView(Context context){
		super(context);
		this.mContext = context;

		initialization();
	}

	public MonthlyCalendarView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
		initialization();
	}

	public MonthlyCalendarView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
		initialization();
	}

	private void initialization(){
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		this.setOrientation(VERTICAL);
	}
}

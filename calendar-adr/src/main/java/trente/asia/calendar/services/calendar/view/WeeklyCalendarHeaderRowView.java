package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import trente.asia.calendar.R;

/**
 * MonthlyCalendarDayView
 *
 * @author TrungND
 */
public class WeeklyCalendarHeaderRowView extends LinearLayout{

	private Context		mContext;
	public int			index;
	public LinearLayout	lnrRowContent;

	public WeeklyCalendarHeaderRowView(Context context){
		super(context);
		this.mContext = context;
	}

	public WeeklyCalendarHeaderRowView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public WeeklyCalendarHeaderRowView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public WeeklyCalendarHeaderRowView(Context context, int index){
		super(context);
		this.mContext = context;
		this.index = index;
	}

	public void initialization(){
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = mInflater.inflate(R.layout.monthly_calendar_row, null);
		lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);

		this.addView(rowView);
	}
}

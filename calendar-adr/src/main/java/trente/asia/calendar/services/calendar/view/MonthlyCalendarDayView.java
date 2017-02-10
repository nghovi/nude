package trente.asia.calendar.services.calendar.view;

import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * MonthlyCalendarDayView
 *
 * @author TrungND
 */
public class MonthlyCalendarDayView extends LinearLayout{

	private Context			mContext;
	private LinearLayout	lnrRowContent;

	public String			day;

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

	public void initialization(Date itemDate){
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		this.setLayoutParams(params);
		this.day = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, itemDate);

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowItemView = inflater.inflate(R.layout.monthly_calendar_row_item, null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		rowItemView.setLayoutParams(layoutParams);
		TextView txtContent = (TextView)rowItemView.findViewById(R.id.txt_id_row_content);
		txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_11, itemDate));
		lnrRowContent = (LinearLayout)rowItemView.findViewById(R.id.lnr_id_row_content);

		this.addView(rowItemView);
	}

	public void addSchedule(ScheduleModel scheduleModel){
		TextView txtSchedule = new TextView(mContext);
        txtSchedule.setMaxLines(1);
//        txtSchedule.setEllipsize(TextUtils.TruncateAt.END);
		txtSchedule.setTextColor(ContextCompat.getColor(mContext, R.color.wf_common_color_text));
        int textSize = (int)(getResources().getDimension(R.dimen.margin_12dp)/getResources().getDisplayMetrics().density);
		txtSchedule.setTextSize(textSize);
        txtSchedule.setTextColor(Color.parseColor(scheduleModel.scheduleColor));

        txtSchedule.setText(scheduleModel.scheduleName);
        lnrRowContent.addView(txtSchedule);
	}
}

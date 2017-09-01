package trente.asia.calendar.services.calendar.view;

import static trente.asia.calendar.services.calendar.MonthlyPageFragment.getScheduleComparator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * MonthlyCalendarDayView
 *
 * @author TrungND
 */
public class MonthlyCalendarDayView extends LinearLayout{

	private Context						mContext;
	private TextView					txtDayLabel;
	private RelativeLayout				lnrRowContent;
	private DailyScheduleClickListener	mListener;

	public String						day;
	public Date							date;
	public int							dayOfTheWeek;
	public List<ScheduleModel>			lstSchedule		= new ArrayList<>();
	private int							periodNum;
	private boolean						isToday			= false;
	private TextView					txtHoliday;
	private List<ScheduleModel>			schedules		= new ArrayList<>();
	private List<TextView>				txtSchedules	= new ArrayList<>();
	private int							maxMarginTop	= 0;
	private List<Integer>				usedMargins		= new ArrayList<>();
	private ImageView					imgBirthday;

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

	public void initialization(Date itemDate, DailyScheduleClickListener listener, boolean isWithoutMonth){
		LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		this.setLayoutParams(params);
		this.setMinimumHeight(WelfareUtil.dpToPx(48));
		this.day = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, itemDate);
		this.date = itemDate;
		this.mListener = listener;

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				if(mListener != null){
					mListener.onDailyScheduleClickListener(date);
				}
			}
		});

		// txtHoliday = (TextView) this.findViewById(R.id.txt_id_row_holiday);

		txtDayLabel = (TextView)this.findViewById(R.id.txt_id_day_label);
		txtDayLabel.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DD, itemDate));
		Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
		if(isWithoutMonth){
			txtDayLabel.setTextColor(Color.GRAY);
		}else{
			if(CCDateUtil.isToday(itemDate)){
				isToday = true;
				txtDayLabel.setBackgroundResource(R.drawable.shape_background_today);
				txtDayLabel.setTextColor(Color.WHITE);
			}else{
				if(Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
					txtDayLabel.setTextColor(Color.RED);
				}else if(Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK)){
					txtDayLabel.setTextColor(Color.BLUE);
				}
			}
		}

		imgBirthday = (ImageView)findViewById(R.id.img_birthday);

		lnrRowContent = (RelativeLayout)this.findViewById(R.id.lnr_id_row_content);
	}

	public void addSchedule(ScheduleModel scheduleModel){
		schedules.add(scheduleModel);
	}

	public void showSchedules(){
		Collections.sort(usedMargins);
		Collections.sort(lstSchedule, getScheduleComparator(false));
		for(ScheduleModel scheduleModel : schedules){
			showSchedule(scheduleModel);
		}
	}

	private void showSchedule(ScheduleModel scheduleModel){
		if(ClConst.SCHEDULE_TYPE_BIRTHDAY.equals(scheduleModel.scheduleType)){
			setLayoutBirthday(scheduleModel);
		}else{
			lstSchedule.add(scheduleModel);
			int marginTop = maxMarginTop;
			while(usedMargins.contains(marginTop)){
				marginTop = marginTop + ClConst.TEXT_VIEW_HEIGHT;
			}
			maxMarginTop = marginTop;
			usedMargins.add(marginTop);

			int width = getWidth();
			TextView txtSchedule = MonthlyCalendarRowView.createTextView(getContext(), width, 0, scheduleModel, marginTop - ClConst.TEXT_VIEW_HEIGHT + WelfareUtil.dpToPx(2));
			lnrRowContent.addView(txtSchedule);
			txtSchedules.add(txtSchedule);
			// if (ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleModel.scheduleType)) {
			// txtSchedule.setTextColor(Color.RED);
			// }
		}
	}

	public void removeAllData(){
		lnrRowContent.removeAllViews();
		periodNum = 0;
		maxMarginTop = 0;
		lstSchedule.clear();
		usedMargins.clear();
		usedMargins.add(0);
		schedules.clear();
		txtSchedules.clear();
		imgBirthday.setVisibility(View.INVISIBLE);
	}

	public void addPeriod(ScheduleModel scheduleModel, int marginTop){
		if(maxMarginTop < marginTop && marginTop - maxMarginTop == ClConst.TEXT_VIEW_HEIGHT){
			maxMarginTop = marginTop;
		}

		periodNum++;
		usedMargins.add(marginTop);
		lstSchedule.add(scheduleModel);
	}

	public void addPassivePeriod(ScheduleModel scheduleModel, int marginTop){
		periodNum++;
	}

	public int getActivePeriodNum(){
		while(usedMargins.contains(maxMarginTop + ClConst.TEXT_VIEW_HEIGHT)){
			maxMarginTop += ClConst.TEXT_VIEW_HEIGHT;
		}
		return maxMarginTop;
	}

	// private void setLayoutHoliday(ScheduleModel scheduleModel) {
	// txtHoliday.setVisibility(View.VISIBLE);
	// txtHoliday.setTextColor(Color.RED);
	// txtHoliday.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.wf_background_gray_border));
	// txtHoliday.setText(scheduleModel.scheduleName);
	// if (!isToday) {
	// txtDayLabel.setTextColor(Color.RED);
	// } else {
	// txtDayLabel.setTextColor(Color.WHITE);
	// }
	// }

	public void setLayoutBirthday(ScheduleModel scheduleModel){
		imgBirthday.setVisibility(View.VISIBLE);
	}
}

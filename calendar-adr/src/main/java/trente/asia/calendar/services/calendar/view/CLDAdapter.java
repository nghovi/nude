package nguyenhoangviet.vpcorp.calendar.services.calendar.view;

/**
 * Created by viet on 7/12/2016.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.CalendarDayModel;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

public class CLDAdapter extends BaseAdapter{

	private Context					mContext;

	private Calendar				month;
	public GregorianCalendar		pmonth;						// view_dr_calendar instance for
	// previous gregorianCalendar
	/**
	 * view_dr_calendar instance for previous gregorianCalendar for getting
	 * complete view
	 */
	public GregorianCalendar		pmonthmaxset;
	private GregorianCalendar		selectedDate;
	int								firstDay;
	int								maxWeeknumber;
	int								maxP;
	int								calMaxP;
	int								mnthlength;
	public List<Calendar>			dayString;
	private List<CalendarDayModel>	calendarDays;
	private List<Cell>				cells	= new ArrayList<>();

	private class Cell{

		public View				view;
		public CalendarDayModel	calendarDay;

		public Cell(View v, CalendarDayModel calendarDay){
			this.view = v;
			this.calendarDay = calendarDay;
		}
	}

	public CLDAdapter(Context c, GregorianCalendar monthCalendar, List<CalendarDayModel> calendarDays){
		this.calendarDays = calendarDays;
		dayString = new ArrayList<Calendar>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;
		selectedDate = (GregorianCalendar)monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		// month.setFirstDayOfWeek(Calendar.WEDNESDAY);
		refreshDays();
	}

	public int getCount(){
		return dayString.size();
	}

	public Object getItem(int position){
		return dayString.get(position);
	}

	public long getItemId(int position){
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		CalendarDayModel calendarDay;
		if(convertView == null){ // if it's not recycled, initialize some
			// attributes
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_calendar, null);
		}
		calendarDay = getCalendarDay(dayString.get(position), calendarDays);
		addCell(convertView, calendarDay);
		TextView txtDay = (TextView)v.findViewById(R.id.item_calendar_txt_date);
		ImageView imgReportStatus = (ImageView)v.findViewById(R.id.item_calendar_img_status);
		int gridvalue = dayString.get(position).get(Calendar.DAY_OF_MONTH);
		int dayColor = Color.BLACK;
		RelativeLayout rltBackground = (RelativeLayout)v.findViewById(R.id.item_calendar_background);
		Calendar c = Calendar.getInstance();
		c.setTime(calendarDay.date);
		// if not current month
		if(c.get(Calendar.MONTH) != month.get(Calendar.MONTH)){
			dayColor = ContextCompat.getColor(mContext, R.color.ripple_material_dark);
			txtDay.setText("");
			txtDay.setClickable(false);
			txtDay.setFocusable(false);
			imgReportStatus.setVisibility(View.INVISIBLE);
			// rltBackground.setBackgroundColor(ContextCompat.getScheduleColor(mContext,
			// R.color.core_silver));
		}else{
			imgReportStatus.setVisibility(View.VISIBLE);
			// setting dayStr text color: red for sundays, blue for saturday and
			// black for normal dayStr
			if(position % 7 == 0){// Sundays
				// dayColor = ContextCompat.getScheduleColor(mContext, R.color
				// .dr_day_color_sun);
				// rltBackground.setBackgroundResource(R.drawable
				// .dr_item_calendar_background_sun);
			}else if(position % 7 == 6){// Saturdays
				// dayColor = ContextCompat.getScheduleColor(mContext, R.color
				// .dr_text_day_color_sat);
				// rltBackground.setBackgroundResource(R.drawable
				// .dr_item_calendar_background_sat);
			}

			// CalendarDay calendarDay = MyReportFragment.getReportByDay
			// (dayString.get(position), calendarDays);
			// if(calendarDay.holiday != null){
			// dayColor = ContextCompat.getScheduleColor(mContext, R.color
			// .dr_day_color_sun);
			// rltBackground.setBackgroundResource(R.drawable
			// .dr_item_calendar_background_holiday);
			// }else if(DRUtil.getDateString(Calendar.getInstance().getTime()
			// , // background for
			// // today
			//
			// DRConst.DATE_FORMAT_YYYY_MM_DD).equals(DRUtil.getDateString
			// (calendarDay.reportDate, DRConst
			// .DATE_FORMAT_YYYY_MM_DD_HH_MM_SS,
			// DRConst.DATE_FORMAT_YYYY_MM_DD))){
			// dayColor = ContextCompat.getScheduleColor(mContext, R.color.core_white);
			// // rltBackground.setBackgroundResource(R.drawable
			// .dr_item_calendar_background_today);
			// txtDate.setBackgroundResource(R.drawable
			// .dr_background_base_color_circle);
			// }

			// if(CCStringUtil.isEmpty(calendarDay.key)){
			// // imgReportStatus.setImageResource(R.drawable.ic_action_new);
			// imgReportStatus.setVisibility(View.INVISIBLE);
			// }else{
			// if(CalendarDay.REPORT_STATUS_DRTT.equals(calendarDay
			// .reportStatus)){
			// imgReportStatus.setImageResource(R.drawable.dr_draft);
			// }else if(CalendarDay.REPORT_STATUS_DONE.equals(calendarDay
			// .reportStatus)){
			// imgReportStatus.setImageResource(R.drawable.wf_check);
			// }
			// }
			txtDay.setTextColor(dayColor);
			txtDay.setText(String.valueOf(gridvalue));
		}

		return v;
	}

	public static CalendarDayModel getCalendarDay(Calendar calendar, List<CalendarDayModel> calendarDays){
		for(CalendarDayModel calendarDay : calendarDays){
			String day = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendarDay.date);
			if(day.equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendar.getTime()))){
				return calendarDay;
			}
		}
		return createEmptyCalendarDay(calendar, null);
	}

	public static CalendarDayModel createEmptyCalendarDay(Calendar c, UserModel reportUser){
		CalendarDayModel calendarDay = new CalendarDayModel();
		calendarDay.date = c.getTime();
		return calendarDay;
	}

	public void setWeekVisibleOnly(String date, boolean visible){
		Calendar c = Calendar.getInstance();
		int rowId = getRowId(date);
		if(visible){
			for(int i = 0; i < cells.size(); i++){
				c.setTime(cells.get(i).calendarDay.date);
				int cellRowId = i / 7;
				if(cellRowId != rowId){
					removeCell(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, cells.get(i).calendarDay.date));
				}
			}
		}else{
			refreshDays();
		}
		notifyDataSetChanged();
	}

	private void removeCell(String date){
		Iterator<Calendar> it = dayString.iterator();
		while(it.hasNext()){
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, it.next().getTime()).equals(date)){
				it.remove();
				break;
			}
		}
	}

	private void addCell(View convertView, CalendarDayModel calendarDay){
		for(Cell c : cells){
			if(c.calendarDay.date.equals(calendarDay.date)){
				c.view = convertView;
				return;
			}
		}
		Cell cell = new Cell(convertView, calendarDay);
		cells.add(cell);
	}

	private int getRowId(String date){
		for(int i = 0; i < cells.size(); i++){
			if(date.equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, cells.get(i).calendarDay.date))){
				return i / 7;
			}
		}
		return 0;
	}

	public void refreshDays(){
		// clear items
		dayString.clear();
		cells.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar)month.clone();
		// gregorianCalendar start dayStr. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current gregorianCalendar.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous gregorianCalendar maximum dayStr 31,30....
		calMaxP = maxP - (firstDay - 1);// view_dr_calendar offday starting
		// 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the
		 * three gregorianCalendar's
		 * (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar)pmonth.clone();
		/**
		 * setting the start date as previous gregorianCalendar's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling gridview.
		 */
		for(int n = 0; n < mnthlength; n++){
			// itemvalue = df.format(pmonthmaxset.getTime());
			Calendar c = Calendar.getInstance();
			c.setTime(pmonthmaxset.getTime());
			dayString.add(c);
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
		}
	}

	private int getMaxP(){
		int maxP;
		if(month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)){
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		}else{
			pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}

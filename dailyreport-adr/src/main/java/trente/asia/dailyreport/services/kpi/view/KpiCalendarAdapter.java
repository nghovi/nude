package trente.asia.dailyreport.services.kpi.view;

/**
 * Created by viet on 7/12/2016.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.kpi.ActionPlansAddFragment;
import trente.asia.dailyreport.services.kpi.model.ActionPlan;
import trente.asia.welfare.adr.define.WelfareConst;

public class KpiCalendarAdapter extends BaseAdapter{

	private Context				mContext;

	private Calendar			month;
	public GregorianCalendar	pmonth;									// view_dr_calendar instance for previous gregorianCalendar
	/**
	 * view_dr_calendar instance for previous gregorianCalendar for getting complete view
	 */
	public GregorianCalendar	pmonthmaxset;
	private GregorianCalendar	selectedDate;
	int							firstDay;
	int							maxWeeknumber;
	int							maxP;
	int							calMaxP;
	int							mnthlength;
	private ArrayList<String>	items;
	public List<Calendar>		dayString;
	private Map<Integer, View>	viewMap				= new HashMap<>();
	private int					selectedPosition	= -1;

	public KpiCalendarAdapter(Context c, GregorianCalendar monthCalendar){
		dayString = new ArrayList<Calendar>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;
		selectedDate = (GregorianCalendar)monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
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
		if(convertView == null){ // if it's not recycled, initialize some
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_calendar, null);

		}
		TextView txtDay = (TextView)v.findViewById(R.id.item_calendar_txt_date);
		ImageView imgReportStatus = (ImageView)v.findViewById(R.id.item_calendar_img_status);
		imgReportStatus.setVisibility(View.INVISIBLE);

		int gridvalue = dayString.get(position).get(Calendar.DAY_OF_MONTH);
		int dayColor = Color.BLACK;

		// checking whether the day is in current month or not.
		if(((gridvalue > 1) && (position < firstDay)) || ((gridvalue < 7) && (position > 28))){
			dayColor = ContextCompat.getColor(mContext, R.color.dr_other_month);
			txtDay.setClickable(false);
			txtDay.setFocusable(false);
		}else{
			if(position % 7 == 0){// Sundays
				dayColor = ContextCompat.getColor(mContext, R.color.dr_day_color_sun);
			}else if(position % 7 == 6){// Saturdays
				dayColor = ContextCompat.getColor(mContext, R.color.dr_text_day_color_sat);
			}
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, Calendar.getInstance().getTime()).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, dayString.get(position).getTime()))){
				dayColor = ContextCompat.getColor(mContext, R.color.core_white);
			}

			updateBackground(v, position, 0);
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, Calendar.getInstance().getTime()).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, dayString.get(position).getTime()))){
				dayColor = ContextCompat.getColor(mContext, R.color.core_white);
				txtDay.setBackgroundResource(R.drawable.dr_background_base_color_circle);
				selectedPosition = position;
				updateBackground(v, position, Color.GRAY);
			}

		}

		txtDay.setTextColor(dayColor);
		txtDay.setText(String.valueOf(gridvalue));
		viewMap.put(position, v);
		return v;
	}

	public void updateBackground(View cell, int position, int bgColor){
		RelativeLayout rltBackground = (RelativeLayout)cell.findViewById(R.id.item_calendar_background);
		if(bgColor != 0){
			rltBackground.setBackgroundColor(bgColor);
		}else{
			if(position % 7 == 0){// Sundays
				rltBackground.setBackgroundResource(R.drawable.dr_item_calendar_background_sun);
			}else if(position % 7 == 6){// Saturdays
				rltBackground.setBackgroundResource(R.drawable.dr_item_calendar_background_sat);
			}else{
				rltBackground.setBackgroundColor(Color.WHITE);
			}
		}
	}

	public void refreshDays(){
		// clear items
		items.clear();
		dayString.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar)month.clone();
		// gregorianCalendar start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current gregorianCalendar.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous gregorianCalendar maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// view_dr_calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three gregorianCalendar's
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
		if(month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)){
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		}else{
			pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

	public void updateLayout(Map<String, String> statusMap){
		for(int i = 0; i < dayString.size(); i++){
			String dayStatus = statusMap.get(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, dayString.get(i).getTime()));
			if(!CCStringUtil.isEmpty(dayStatus)){
				View cell = viewMap.get(i);
				updateCellLayout(cell, dayStatus);
			}
		}
	}

	private void updateCellLayout(View v, String dayStatus){
		ImageView kpiStatus = (ImageView)v.findViewById(R.id.item_calendar_img_status);
		kpiStatus.setVisibility(View.VISIBLE);
		if(ActionPlan.STATUS_OK.equals(dayStatus)){
			kpiStatus.setImageResource(R.drawable.ic_circle);
		}else if(ActionPlan.STATUS_NG.equals(dayStatus)){
			kpiStatus.setImageResource(R.drawable.ic_cross);
		}
	}

	public void updateSelectedCell(int position){
		View cell = viewMap.get(position);
		updateBackground(cell, position, Color.GRAY);
		if(selectedPosition != -1){
			cell = viewMap.get(selectedPosition);
			updateBackground(cell, selectedPosition, 0);
		}
		selectedPosition = position;
	}

	public void deselectCell(){
		if(selectedPosition != -1){
			View cell = viewMap.get(selectedPosition);
			updateBackground(cell, selectedPosition, 0);
			selectedPosition = -1;
		}
	}

	public int getSelectedPosition(){
		return selectedPosition;
	}
}

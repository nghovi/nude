package trente.asia.dailyreport.services.report.view;

/**
 * Created by viet on 7/12/2016.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.report.MyReportFragment;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.welfare.adr.BuildConfig;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

public class CalendarAdapter extends BaseAdapter{

	private Context					mContext;

	private java.util.Calendar		month;
	public GregorianCalendar		pmonth;			// view_dr_calendar instance for previous gregorianCalendar
	/**
	 * view_dr_calendar instance for previous gregorianCalendar for getting complete view
	 */
	public GregorianCalendar		pmonthmaxset;
	private GregorianCalendar		selectedDate;
	int								firstDay;
	int								maxWeeknumber;
	int								maxP;
	int								calMaxP;
	int								mnthlength;
	private ArrayList<String>		items;
	public static List<Calendar>	dayString;
	private List<ReportModel>		reportModels;

	public CalendarAdapter(Context c, GregorianCalendar monthCalendar, List<ReportModel> reportModels){
		this.reportModels = reportModels;
		CalendarAdapter.dayString = new ArrayList<Calendar>();
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
			// attributes
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_calendar, null);

		}
		TextView txtDay = (TextView)v.findViewById(R.id.item_calendar_txt_date);
		ImageView imgReportStatus = (ImageView)v.findViewById(R.id.item_calendar_img_status);
		TextView txtWorkOfferInfo = (TextView)v.findViewById(R.id.item_calendar_txt_work_offer_info);

		int gridvalue = dayString.get(position).get(Calendar.DAY_OF_MONTH);
		int dayColor = Color.BLACK;
		RelativeLayout rltBackground = (RelativeLayout)v.findViewById(R.id.item_calendar_background);

		// checking whether the day is in current month or not.
		if(((gridvalue > 1) && (position < firstDay)) || ((gridvalue < 7) && (position > 28))){
			dayColor = ContextCompat.getColor(mContext, R.color.dr_other_month);
			txtDay.setClickable(false);
			txtDay.setFocusable(false);
			imgReportStatus.setVisibility(View.INVISIBLE);
			// rltBackground.setBackgroundColor(ContextCompat.getColor(mContext,
			// R.color.core_silver));
		}else{
			// setting day text color: red for sundays, blue for saturday and
			// black for normal day
			if(position % 7 == 0){// Sundays
				dayColor = ContextCompat.getColor(mContext, R.color.dr_day_color_sun);
				rltBackground.setBackgroundResource(R.drawable.dr_item_calendar_background_sun);
			}else if(position % 7 == 6){// Saturdays
				dayColor = ContextCompat.getColor(mContext, R.color.dr_text_day_color_sat);
				rltBackground.setBackgroundResource(R.drawable.dr_item_calendar_background_sat);
			}

			ReportModel reportModel = MyReportFragment.getReportByDay(dayString.get(position), reportModels);
			if(reportModel.holiday != null){
				dayColor = ContextCompat.getColor(mContext, R.color.dr_day_color_sun);
				rltBackground.setBackgroundResource(R.drawable.dr_item_calendar_background_holiday);
			}else if(DRUtil.getDateString(Calendar.getInstance().getTime(), // background for
																			// today

							DRConst.DATE_FORMAT_YYYY_MM_DD).equals(DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD))){
				dayColor = ContextCompat.getColor(mContext, R.color.core_white);
				txtDay.setBackgroundResource(R.drawable.dr_background_base_color_circle);
			}

			if(CCStringUtil.isEmpty(reportModel.key)){
				if(!CCStringUtil.isEmpty(reportModel.workingSymbol)){
					WfPicassoHelper.loadImage2(mContext, trente.asia.dailyreport.BuildConfig.HOST, imgReportStatus, reportModel.workingSymbol);
				} else {
					imgReportStatus.setVisibility(View.INVISIBLE);
				}
			}else{
				if(ReportModel.REPORT_STATUS_DRTT.equals(reportModel.reportStatus)){
					imgReportStatus.setImageResource(R.drawable.dr_draft);
				}else if(ReportModel.REPORT_STATUS_DONE.equals(reportModel.reportStatus)){
					imgReportStatus.setImageResource(R.drawable.wf_check);
				}
			}
		}

		txtDay.setTextColor(dayColor);
		txtDay.setText(String.valueOf(gridvalue));
		return v;
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
		if(month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)){
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		}else{
			pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}

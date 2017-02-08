package trente.asia.calendar.services.calendar.view;

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

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDay;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

public class CLDAdapter extends BaseAdapter {

    private Context mContext;

    private Calendar month;
    public GregorianCalendar pmonth;        // view_dr_calendar instance for
    // previous gregorianCalendar
    /**
     * view_dr_calendar instance for previous gregorianCalendar for getting
     * complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int mnthlength;
    public List<Calendar> dayString;
    private List<CalendarDay> calendarDays;
    private List<Cell> cells = new ArrayList<>();

    private class Cell {
        public View view;
        public CalendarDay calendarDay;

        public Cell(View v, CalendarDay calendarDay) {
            this.view = v;
            this.calendarDay = calendarDay;
        }
    }

    public CLDAdapter(Context c, GregorianCalendar monthCalendar,
                      List<CalendarDay> calendarDays) {
        this.calendarDays = calendarDays;
        dayString = new ArrayList<Calendar>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        refreshDays();
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CalendarDay calendarDay;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar, null);
        }
        calendarDay = getCalendarDay(dayString.get(position), calendarDays);
        addCell(convertView, calendarDay);
        TextView txtDay = (TextView) v.findViewById(R.id
                .item_calendar_txt_date);
        ImageView imgReportStatus = (ImageView) v.findViewById(R.id
                .item_calendar_img_status);
        int gridvalue = dayString.get(position).get(Calendar.DAY_OF_MONTH);
        int dayColor = Color.BLACK;
        RelativeLayout rltBackground = (RelativeLayout) v.findViewById(R.id
                .item_calendar_background);

        // checking whether the day is in current month or not.
        if (getCount() > 7 && ((gridvalue > 1 && position < firstDay) ||
                (gridvalue < 7 &&
                        position > 28))) {
            dayColor = ContextCompat.getColor(mContext, R.color
                    .ripple_material_dark);
            txtDay.setClickable(false);
            txtDay.setFocusable(false);
            imgReportStatus.setVisibility(View.INVISIBLE);
            // rltBackground.setBackgroundColor(ContextCompat.getColor(mContext,
            // R.color.core_silver));
        } else {
            imgReportStatus.setVisibility(View.VISIBLE);
            // setting day text color: red for sundays, blue for saturday and
            // black for normal day
            if (position % 7 == 0) {// Sundays
                // dayColor = ContextCompat.getColor(mContext, R.color
                // .dr_day_color_sun);
                // rltBackground.setBackgroundResource(R.drawable
                // .dr_item_calendar_background_sun);
            } else if (position % 7 == 6) {// Saturdays
                // dayColor = ContextCompat.getColor(mContext, R.color
                // .dr_text_day_color_sat);
                // rltBackground.setBackgroundResource(R.drawable
                // .dr_item_calendar_background_sat);
            }

            // CalendarDay calendarDay = MyReportFragment.getReportByDay
            // (dayString.get(position), calendarDays);
            // if(calendarDay.holiday != null){
            // dayColor = ContextCompat.getColor(mContext, R.color
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
            // dayColor = ContextCompat.getColor(mContext, R.color.core_white);
            // // rltBackground.setBackgroundResource(R.drawable
            // .dr_item_calendar_background_today);
            // txtDay.setBackgroundResource(R.drawable
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
        }

        txtDay.setTextColor(dayColor);
        txtDay.setText(String.valueOf(gridvalue));
        return v;
    }

    public static CalendarDay getCalendarDay(Calendar calendar,
                                             List<CalendarDay>
                                                     calendarDays) {
        for (CalendarDay calendarDay : calendarDays) {
            String day = CCFormatUtil.formatDateCustom(WelfareConst
                    .WL_DATE_TIME_7, CCDateUtil.makeDateCustom(calendarDay
                    .date, WelfareConst.WL_DATE_TIME_7));
            if (day.equals(CCFormatUtil.formatDateCustom(WelfareConst
                    .WL_DATE_TIME_7, calendar.getTime()))) {
                return calendarDay;
            }
        }
        return createEmptyCalendarDay(calendar, null);
    }

    public static CalendarDay createEmptyCalendarDay(Calendar c, UserModel
            reportUser) {
        CalendarDay calendarDay = new CalendarDay();
        calendarDay.date = CCFormatUtil.formatDateCustom(WelfareConst
                .WL_DATE_TIME_7, c.getTime());
        return calendarDay;
    }

    public void setWeekVisibleOnly(String date, boolean visible) {
        Calendar c = Calendar.getInstance();
        int rowId = getRowId(date);
        if (visible) {
            for (int i = 0; i < cells.size(); i++) {
                c.setTime(CCDateUtil.makeDateCustom(cells.get(i).calendarDay
                                .date,
                        WelfareConst.WL_DATE_TIME_7));
                int cellRowId = i / 7;
                if (cellRowId != rowId) {
                    removeCell(cells.get(i).calendarDay.date);
                }
            }
        } else {
            refreshDays();
        }
        notifyDataSetChanged();
    }

    private void removeCell(String date) {
        Iterator<Calendar> it = dayString.iterator();
        while (it.hasNext()) {
            if (CCFormatUtil.formatDateCustom(WelfareConst
                    .WL_DATE_TIME_7, it.next().getTime()).equals
                    (date)) {
                it.remove();
                break;
            }
        }
    }

    private void addCell(View convertView, CalendarDay calendarDay) {
        for (Cell c : cells) {
            if (c.calendarDay.date.equals
                    (calendarDay.date)) {
                c.view = convertView;
                return;
            }
        }
        Cell cell = new Cell(convertView, calendarDay);
        cells.add(cell);
    }

    private int getRowId(String date) {
        for (int i = 0; i < cells.size(); i++) {
            if (date.equals(CCFormatUtil.formatDateCustom(WelfareConst
                    .WL_DATE_TIME_7, CCDateUtil.makeDateCustom(cells.get(i)
                    .calendarDay
                    .date, WelfareConst.WL_DATE_TIME_7)))) {
                return i / 7;
            }
        }
        return 0;
    }

    public void refreshDays() {
        // clear items
        dayString.clear();
        cells.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // gregorianCalendar start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current gregorianCalendar.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous gregorianCalendar maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// view_dr_calendar offday starting
        // 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the
         * three gregorianCalendar's
         * (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous gregorianCalendar's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling gridview.
         */
        for (int n = 0; n < mnthlength; n++) {
            // itemvalue = df.format(pmonthmaxset.getTime());
            Calendar c = Calendar.getInstance();
            c.setTime(pmonthmaxset.getTime());
            dayString.add(c);
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum
                (GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month
                    .getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar
                    .MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

}

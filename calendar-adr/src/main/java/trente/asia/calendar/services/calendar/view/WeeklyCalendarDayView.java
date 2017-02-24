package trente.asia.calendar.services.calendar.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * WeeklyCalendarDayView
 *
 * @author TrungND
 */
public class WeeklyCalendarDayView extends LinearLayout {

    private Context mContext;
    public String dayStr;
    public CalendarDayModel calendarDayModel;
    private OnDayClickListener onDayClickListener;
    private List<HolidayModel> holidayModels;
    private TextView txtContent;
    private int bgResource = 0;
    private TextView txtScheduleMark;

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public Date getDate() {
        return date;
    }

    public interface OnDayClickListener {

        public void onDayClick(WeeklyCalendarDayView calendarDayModel);
    }

    public WeeklyCalendarDayView(Context context) {
        super(context);
        this.mContext = context;

    }

    public WeeklyCalendarDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public WeeklyCalendarDayView(Context context, AttributeSet attrs, int
            defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void setData(final CalendarDayModel calendarDayModel,
                        OnDayClickListener listener, List<HolidayModel>
                                holidayModels) {
        this.calendarDayModel = calendarDayModel;
        this.onDayClickListener = listener;
        this.holidayModels = holidayModels;

        HolidayModel holidayModel = HolidayModel.getHolidayModel(this.date,
                this.holidayModels);
        if (bgResource == 0 && holidayModel != null) {
            this.bgResource = R.drawable
                    .circle_background_holiday;
        }

        if (calendarDayModel != null && !CCCollectionUtil.isEmpty
                (calendarDayModel
                        .schedules))

        {
            txtScheduleMark.setVisibility(View.VISIBLE);
        } else {
            txtScheduleMark.setVisibility(View.GONE);
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            //// TODO: 2/23/2017 why setBackgroundResource doesn't work
            txtContent.setTextColor(Color.YELLOW);
            txtContent.setBackgroundResource(R.drawable
                    .circle_background_selected);
        } else {
            txtContent.setBackgroundResource(this.bgResource);
        }
    }

    public void initialization(Date itemDate) {
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        this.setLayoutParams(params);
        this.date = itemDate;
        this.dayStr = CCFormatUtil.formatDateCustom(WelfareConst
                .WL_DATE_TIME_7, this.date);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View rowItemView = inflater.inflate(R.layout
                .weekly_calendar_row_header_item, null);
        txtContent = (TextView) rowItemView.findViewById(R.id
                .txt_id_row_content);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup
                .LayoutParams.WRAP_CONTENT, 1);
        rowItemView.setLayoutParams(layoutParams);

        rowItemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onDayClickListener != null && calendarDayModel != null) {
                    onDayClickListener.onDayClick(WeeklyCalendarDayView.this);
                }
            }
        });

        txtContent.setText(CCFormatUtil.formatDateCustom(WelfareConst
                .WL_DATE_TIME_11, itemDate));
        Calendar c = Calendar.getInstance();
        if (CCDateUtil.compareDate(c.getTime(), this.date, false) == 0) {
            this.bgResource = R.drawable
                    .circle_background_today;
        }

        setSelected(false);

        txtScheduleMark = (TextView) rowItemView.findViewById(R.id
                .txt_id_row_schedule_mark);


        Calendar itemCalendar = CCDateUtil.makeCalendar(itemDate);
        if (Calendar.SUNDAY == itemCalendar.get(Calendar.DAY_OF_WEEK))

        {
            txtContent.setTextColor(Color.RED);
        } else if (Calendar.SATURDAY == itemCalendar.get(Calendar.DAY_OF_WEEK))

        {
            txtContent.setTextColor(Color.BLUE);
        }

        this.

                addView(rowItemView);
    }
}

package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;

import static trente.asia.calendar.services.calendar.CalendarListFragment
        .SELECTED_CALENDAR_STRING;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends SchedulesPageFragment implements
        ObservableScrollViewCallbacks, CalendarView
        .OnCalendarDaySelectedListener, WeeklyCalendarDayView
        .OnDayClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_weekly_page,
                    container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected JSONObject prepareJsonObject() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.week.get(0));

        String selectedCalendarStr = prefAccUtil.get(SELECTED_CALENDAR_STRING);
        String targetUserList = getTargetUserList();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("targetUserList", targetUserList);
            jsonObject.put("calendars", selectedCalendarStr);
            jsonObject.put("startDateString", CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, c.getTime()));
            c.add(Calendar.DATE, 7);
            jsonObject.put("endDateString", CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, c.getTime()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected String getApi() {
        return WfUrlConst.WF_CL_WEEK_SCHEDULE;
    }

    @Override
    protected List<Date> getCalendarDates() {
        if (CCCollectionUtil.isEmpty(this.week)) {
            this.week = CsDateUtil.getAllDate4Week(CCDateUtil.makeCalendar
                    (selectedDate), CCNumberUtil.toInteger(prefAccUtil
                    .getSetting

                            ().CL_START_DAY_IN_WEEK));
        }
        return this.week;
    }
}


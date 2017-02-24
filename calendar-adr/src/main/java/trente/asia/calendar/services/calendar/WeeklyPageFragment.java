package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.welfare.adr.define.WfUrlConst;

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

    protected String getApi() {
        return WfUrlConst.WF_CL_WEEK_SCHEDULE;
    }

    @Override
    protected List<Date> getCalendarDates() {
        List<Date> dates = CsDateUtil.getAllDate4Week(CCDateUtil.makeCalendar
                (selectedDate), CCNumberUtil.toInteger(prefAccUtil
                .getSetting

                        ().CL_START_DAY_IN_WEEK));
        return dates;
    }
}


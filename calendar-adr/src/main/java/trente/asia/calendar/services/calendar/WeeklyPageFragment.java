package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
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

    protected ObservableListView observableListView;
    protected CalendarDayListAdapter adapter;


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
        observableListView = (ObservableListView) getView().findViewById(R.id
                .scr_calendar_day);
        observableListView.setScrollViewCallbacks(this);
        observableListView.setDivider(null);
    }

    @Override
    protected void updateObservableScrollableView() {
        List<CalendarDayModel> displayedModels = getDisplayedDayForList();
        adapter = new CalendarDayListAdapter(activity, R.layout
                .item_calendar_day, displayedModels, new
                CalendarDayListAdapter.OnScheduleClickListener() {

                    @Override
                    public void onClickSchedule(ScheduleModel schedule) {
                        onClickSchedule(schedule);
                    }
                });
        observableListView.setAdapter(adapter);
    }

    protected String getApi() {
        return WfUrlConst.WF_CL_WEEK_SCHEDULE;
    }

    @Override
    public void updateList(String dayStr) {
        int selectedPosition = adapter.findPosition4Code(dayStr);
        observableListView.setSelection(selectedPosition);
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


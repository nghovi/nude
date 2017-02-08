package trente.asia.calendar.services.calendar;

import static trente.asia.calendar.services.calendar.model.CalendarDay.Event;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDay;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.CalendarView;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends AbstractClFragment implements
        ObservableScrollViewCallbacks, CalendarView
        .OnCalendarDaySelectedListener {

    private ObservableListView lstCalendarDay;
    private CalendarDayListAdapter adapter;
    private CalendarView calendarView;

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
        lstCalendarDay = (ObservableListView) getView().findViewById(R.id
                .lst_calendar_day);
        lstCalendarDay.setScrollViewCallbacks(this);
        List<CalendarDay> dummy = getDummyData();
        adapter = new CalendarDayListAdapter(activity, R.layout
                .item_calendar_day, dummy);
        lstCalendarDay.setAdapter(adapter);
        calendarView = (CalendarView) getView().findViewById(R.id
                .calendar_view);
        calendarView.setOnCalendarDaySelectedListener(this);
        calendarView.updateLayout(activity, 2017, 2, dummy);
    }

    @Override
    protected void initData() {
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public List<CalendarDay> getDummyData() {
        List<CalendarDay> dummyData = new ArrayList<>();

        Event e1 = new Event();
        e1.startTime = "10:00";
        e1.endTime = "12:00";

        Event e2 = new Event();
        e2.startTime = "08:00";
        e2.endTime = "11:00";

        CalendarDay d1 = new CalendarDay();
        d1.date = "2017/06/02";
        d1.events = new ArrayList<>();
        d1.events.add(e1);
        d1.events.add(e2);

        CalendarDay d2 = new CalendarDay();
        d2.date = "2017/08/02";
        d2.events = new ArrayList<>();
        d2.events.add(e1);
        d2.events.add(e2);

        dummyData.add(d1);
        dummyData.add(d2);
        dummyData.add(d2);
        dummyData.add(d2);
        dummyData.add(d2);
        dummyData.add(d2);
        dummyData.add(d2);

        return dummyData;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean
            dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        CalendarView calendarView = (CalendarView) getView().findViewById(R
                .id.calendar_view);
        if (scrollState == ScrollState.UP) {
            calendarView.showWeekOnly("2017/02/22");//// TODO: 2/8/2017
        } else if (scrollState == ScrollState.DOWN) {
            calendarView.showAll("2017/02/22");//// TODO: 2/8/2017
        }
    }

    @Override
    public void onCalendarDaySelected(CalendarDay reportModel) {
        // // TODO: 2/8/2017
    }
}

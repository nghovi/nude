package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends SchedulesPageListViewFragment
        implements ObservableScrollViewCallbacks, CalendarView
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

    protected void onLoadSchedulesSuccess(JSONObject response) {
        super.onLoadSchedulesSuccess(response);
        updateObservableScrollableView();
    }

    protected void updateObservableScrollableView() {
        List<CalendarDayModel> displayedModels = getDisplayedDayForList();
        adapter = new CalendarDayListAdapter(activity, R.layout
                .item_calendar_day, displayedModels, this);
        observableListView.setAdapter(adapter);
    }

    @Override
    public void updateList(String dayStr) {
        int selectedPosition = adapter.findPosition4Code(dayStr);
        observableListView.setSelection(selectedPosition);
    }

    @Override
    protected List<Date> getAllDate() {
        List<Date> dates = CsDateUtil.getAllDate4Week(CCDateUtil.makeCalendar
                (selectedDate), CCNumberUtil.toInteger(prefAccUtil.getSetting

                ().CL_START_DAY_IN_WEEK));
        return dates;
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
        if (scrollState == ScrollState.UP) {
            for (WeeklyCalendarHeaderRowView rowView : lstHeaderRow) {
                if (rowView.index != 0) {
                    rowView.setVisibility(View.GONE);
                }
            }
        } else if (scrollState == ScrollState.DOWN) {
            for (WeeklyCalendarHeaderRowView rowView : lstHeaderRow) {
                rowView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDayClick(String dayStr) {
        updateDayViews(dayStr);
        updateList(dayStr);
    }

    protected void updateDayViews(String dayStr) {
        pageSharingHolder.cancelPreviousClickedDayView();
        for (WeeklyCalendarDayView view : calendarDayViews) {
            if (dayStr.equals(view.dayStr)) {
                view.setSelected(true);
                pageSharingHolder.setClickedDayView(view);
                return;
            }
        }
    }

    @Override
    public void onCalendarDaySelected(CalendarDayModel reportModel) {

    }

    @Override
    protected void clearOldData() {
        for (WeeklyCalendarDayView dayView : calendarDayViews) {
            CalendarDayModel calendarDayModel = getCalendarDayModelByDate
                    (dayView.getDate(), calendarDayModels);
            dayView.setData(calendarDayModel, this, lstHoliday);
        }
    }

    private CalendarDayModel getCalendarDayModelByDate(Date date,
                                                       List<CalendarDayModel>
                                                               calendarDayModels) {
        if (!CCStringUtil.isEmpty(calendarDayModels)) {
            for (CalendarDayModel calendarDayModel : calendarDayModels) {
                if (calendarDayModel.date.equals(CCFormatUtil
                        .formatDateCustom(WelfareConst.WL_DATE_TIME_7, date))) {
                    return calendarDayModel;
                }
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}

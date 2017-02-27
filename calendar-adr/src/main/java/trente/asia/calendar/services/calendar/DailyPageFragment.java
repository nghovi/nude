package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.services.calendar.listener
        .DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.view.ViewDayShiftTime;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * DailyPageFragment
 *
 * @author Vietnh
 */
public class DailyPageFragment extends SchedulesPageFragment implements
        DailyScheduleClickListener, ObservableScrollViewCallbacks,
        WeeklyCalendarDayView.OnDayClickListener {

    private ClDialog dialogScheduleList;
    private ViewDayShiftTime viewDayShiftTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_daily_page,
                    container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        viewDayShiftTime = (ViewDayShiftTime) getView().findViewById(R.id
                .view_day_shift_time);
        initDialog();
    }

    @Override
    protected void onLoadSchedulesSuccess(JSONObject response) {
        super.onLoadSchedulesSuccess(response);
        CalendarDayModel calendarDayModel = getCalendarDayModelByDate
                (selectedDate, calendarDayModels);
        if (calendarDayModel != null) {
            viewDayShiftTime.showCategoriesSigns(calendarDayModel);
        }
    }

    private void initDialog() {
        dialogScheduleList = new ClDialog(activity);
        dialogScheduleList.setDialogScheduleList();
    }

    @Override
    protected List<Date> getCalendarDates() {
        Date firstDateOfMonth = CCDateUtil.makeDateWithFirstday(selectedDate);
        List<Date> dates = CsDateUtil.getAllDate4Month(CCDateUtil
                .makeCalendar(firstDateOfMonth), CCNumberUtil.toInteger
                (prefAccUtil.getSetting().CL_START_DAY_IN_WEEK));
        return dates;
    }

    @Override
    public void onDayClick(String dayStr) {
        selectedDate = CCDateUtil.makeDateCustom(dayStr, WelfareConst
                .WL_DATE_TIME_7);
        updateDayViews(dayStr);
        updateObservableListView();
    }

    //Display schedules for selected day only
    @Override
    protected List<CalendarDayModel> getDisplayedDayForList() {
        List<CalendarDayModel> result = new ArrayList<>();
        CalendarDayModel calendarDayModel = getCalendarDayModelByDate
                (selectedDate, calendarDayModels);
        if (calendarDayModel != null) {
            result.add(calendarDayModel);
        }
        return result;
    }

    @Override
    protected String getApi() {
        //// TODO: 2/23/2017
        return WfUrlConst.WF_CL_WEEK_SCHEDULE;
    }


    @Override
    public void onDailyScheduleClickListener(String day) {
        dialogScheduleList.show();
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            lnrDaysContainer.setVisibility(View.GONE);
        } else if (scrollState == ScrollState.DOWN) {
            lnrDaysContainer.setVisibility(View.VISIBLE);
        }
    }

    protected String getUpperTitle() {
        return CCFormatUtil.formatDateCustom(WelfareConst
                .WL_DATE_TIME_12, selectedDate);
    }

}

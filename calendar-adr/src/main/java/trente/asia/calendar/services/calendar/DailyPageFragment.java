package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.services.calendar.listener
        .DailyScheduleClickListener;
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
    LayoutInflater mInflater;

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
        initDialog();
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
    protected JSONObject prepareJsonObject() {
        String targetUserList = getTargetUserList();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("targetUserList", targetUserList);
//            jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom
//                    (WelfareConst.WL_DATE_TIME_7, selectedDate));
            jsonObject.put("calendars", prefAccUtil.get(ClConst
                    .SELECTED_CALENDAR_STRING));


            jsonObject.put("startDateString", CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, selectedDate));
            jsonObject.put("endDateString", CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, selectedDate));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
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

}

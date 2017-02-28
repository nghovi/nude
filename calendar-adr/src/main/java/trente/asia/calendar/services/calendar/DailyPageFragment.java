package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.services.calendar.listener
        .DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * DailyPageFragment
 *
 * @author Vietnh
 */
public class DailyPageFragment extends SchedulesPageFragment implements
        DailyScheduleClickListener, ObservableScrollViewCallbacks {

    private static final String TIME_NOON = "12:00";
    private static final Integer SCHEDULES_ALL_DAY = 1;
    private static final Integer SCHEDULES_MORNING = 2;
    private static final Integer SCHEDULES_AFTERNOON = 3;
    private ClDialog dialogScheduleList;
    //    private ViewDayShiftTime viewDayShiftTime;
    private ObservableScrollView observableScrollView;
    private LinearLayout lnrOffers;

    private List<CalendarDayModel> displayedDayAfternoon;
    private Map<Integer, List<ScheduleModel>> schedulesMap;
    private LayoutInflater layoutInflater;

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
        layoutInflater = getLayoutInflater(null);
        observableScrollView = (ObservableScrollView) getView().findViewById
                (R.id.scr_calendar_day);
        lnrOffers = (LinearLayout) getView().findViewById(R.id
                .lnr_fragment_daily_page_work_offer);
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
    protected void updateObservableScrollableView() {
        schedulesMap = getDisplayedSchedulesMaps();
        buildTimelySchedules(R.id.lnr_fragment_daily_page_all_day, R.string
                .daily_page_all_day, schedulesMap.get(SCHEDULES_ALL_DAY));
        buildTimelySchedules(R.id.lnr_fragment_daily_page_morning, R.string
                .daily_page_morning, schedulesMap.get(SCHEDULES_MORNING));
        buildTimelySchedules(R.id.lnr_fragment_daily_page_afternoon, R.string
                .daily_page_afternoon, schedulesMap.get(SCHEDULES_AFTERNOON));
        buildOffers();
    }

    private void buildOffers() {
        //// TODO: 2/28/2017
    }

    private void buildTimelySchedules(int parentId, int titleId,
                                      List<ScheduleModel> scheduleModels) {
        LinearLayout lnrParent = (LinearLayout) getView().findViewById
                (parentId);
        if (!CCCollectionUtil.isEmpty(scheduleModels)) {
            String title = getString(titleId);
            buildSchedulesList(lnrParent, title, scheduleModels);
        } else {
            lnrParent.removeAllViews();
        }
    }

    private void buildSchedulesList(LinearLayout lnrParent, String title,
                                    List<ScheduleModel>
                                            scheduleModels) {
        lnrParent.removeAllViews();
        TextView textView = new TextView(getContext());
        textView.setText(title);
        lnrParent.addView(textView);

        for (ScheduleModel scheduleModel : scheduleModels) {
            buildScheduleItem(lnrParent, scheduleModel);
        }
    }

    private void buildScheduleItem(LinearLayout lnrParent, ScheduleModel
            scheduleModel) {
        LinearLayout item = (LinearLayout) CalendarDayListAdapter
                .buildScheduleItem
                        (activity, layoutInflater, scheduleModel, this);
        lnrParent.addView(item);
    }

    @Override
    protected String getApi() {
        //// TODO: 2/23/2017
        return WfUrlConst.WF_CL_WEEK_SCHEDULE;
    }

    @Override
    public void updateList(String dayStr) {
        selectedDate = CCDateUtil.makeDateCustom(dayStr, WelfareConst
                .WL_DATE_TIME_7);
        updateObservableScrollableView();
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


    private Map<Integer, List<ScheduleModel>> getDisplayedSchedulesMaps() {
        Map<Integer, List<ScheduleModel>> result = new HashMap<>();
        result.put(SCHEDULES_ALL_DAY, new ArrayList<ScheduleModel>());
        result.put(SCHEDULES_MORNING, new ArrayList<ScheduleModel>());
        result.put(SCHEDULES_AFTERNOON, new ArrayList<ScheduleModel>());
        for (ScheduleModel scheduleModel : schedules) {
            if (isScheduleOf(scheduleModel, selectedDate)) {
                if (scheduleModel.isAllDay) {
                    result.get(SCHEDULES_ALL_DAY).add(scheduleModel);
                } else if (isBeforeNoon(scheduleModel.startTime)) {
                    result.get(SCHEDULES_MORNING).add(scheduleModel);
                } else {
                    result.get(SCHEDULES_AFTERNOON).add(scheduleModel);
                }
            }
        }
        return result;
    }

    private boolean isBeforeNoon(String startTime) {
        int startMinute = CCDateUtil.convertTime2Min(startTime);
        int noonMinute = CCDateUtil.convertTime2Min(TIME_NOON);
        return startMinute < noonMinute;
    }

    private boolean isScheduleOf(ScheduleModel scheduleModel, Date date) {
        return scheduleModel.startDate.equals(CCFormatUtil.formatDateCustom
                (WelfareConst.WL_DATE_TIME_7, date));
    }
}

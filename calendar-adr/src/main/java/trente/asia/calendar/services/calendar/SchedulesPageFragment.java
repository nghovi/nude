package trente.asia.calendar.services.calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview
        .ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.PageSharingHolder;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;

import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class SchedulesPageFragment extends WelfareFragment implements
        ObservableScrollViewCallbacks, CalendarView
        .OnCalendarDaySelectedListener, WeeklyCalendarDayView
        .OnDayClickListener {

    protected ObservableListView observableListView;
    protected Date selectedDate;

    protected LinearLayout lnrDaysContainer;
    protected List<WeeklyCalendarHeaderRowView> lstHeaderRow = new
            ArrayList<>();
    protected List<UserModel> filteredUsers = new ArrayList<>();
    protected List<CalendarDayModel> calendarDayModels;
    protected CalendarDayListAdapter adapter;
    protected List<Date> dates;
    protected List<CalendarModel> calendars;
    protected List<WeeklyCalendarDayView> calendarDayViews = new ArrayList<>();
    protected PageSharingHolder pageSharingHolder;
    protected List<HolidayModel> holidayModels;
    protected List<ScheduleModel> schedules;
    protected int pagePosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        host = BuildConfig.HOST;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void updateHeaderTitles() {
        if (pagePosition == pageSharingHolder.selectedPagePosition) {
            String title = getUpperTitle();
            List<String> selectedCalendarIds = Arrays.asList(prefAccUtil.get
                    (ClConst.SELECTED_CALENDAR_STRING).split(","));
            int selectedCalendarSize = selectedCalendarIds.size();
            String subtitle = selectedCalendarSize > 1 ? selectedCalendarSize
                    + " calendars" : getCalendarName(selectedCalendarIds.get
                    (0));
            pageSharingHolder.navigationHeader.updateHeaderTitles(title,
                    subtitle);
        }
    }

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
        lnrDaysContainer = (LinearLayout) getView().findViewById(R.id
                .lnr_calendar_container);
        observableListView = (ObservableListView) getView().findViewById(R.id
                .lst_calendar_day);
        observableListView.setScrollViewCallbacks(this);
        observableListView.setDivider(null);
        initCalendarView();
    }

    private void initCalendarView() {
        initCalendarHeader();
        initDayViews();
    }

    private void initCalendarHeader() {
        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View titleView = mInflater.inflate(R.layout.monthly_calendar_title,
                null);
        LinearLayout lnrRowTitle = (LinearLayout) titleView.findViewById(R.id
                .lnr_id_row_title);
        List<DayModel> dayModels = CsDateUtil.getAllDay4Week(CCNumberUtil
                .toInteger(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK));
        for (DayModel dayModel : dayModels) {
            View titleItem = mInflater.inflate(R.layout
                    .monthly_calendar_title_item, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            titleItem.setLayoutParams(layoutParams);
            TextView txtTitleItem = (TextView) titleItem.findViewById(R.id
                    .txt_id_row_content);
            if (Calendar.SUNDAY == dayModel.dayOfWeek) {
                txtTitleItem.setTextColor(Color.RED);
            } else if (Calendar.SATURDAY == dayModel.dayOfWeek) {
                txtTitleItem.setTextColor(Color.BLUE);
            }
            txtTitleItem.setText(dayModel.day);
            lnrRowTitle.addView(titleItem);
        }
        lnrDaysContainer.addView(titleView);

    }

    protected void initDayViews() {
        dates = getCalendarDates();
        WeeklyCalendarHeaderRowView rowView = null;
        for (int index = 0; index < dates.size(); index++) {
            Date date = dates.get(index);
            if (index % CsConst.DAY_NUMBER_A_WEEK == 0) {
                rowView = new WeeklyCalendarHeaderRowView(activity, index /
                        CsConst.DAY_NUMBER_A_WEEK);
                rowView.initialization();
                lnrDaysContainer.addView(rowView);
                lstHeaderRow.add(rowView);
            }

            WeeklyCalendarDayView dayView = new WeeklyCalendarDayView(activity);
            dayView.initialization(date);
            rowView.lnrRowContent.addView(dayView);
            calendarDayViews.add(dayView);
        }
    }

    protected List<Date> getCalendarDates() {
        return new ArrayList<>();
    }

    private void callApi() {
        JSONObject jsonObject = prepareJsonObject();
        String api = getApi();
        requestLoad(api, jsonObject, true);
    }

    protected String getTargetUserList() {
        return this.pageSharingHolder.userListLinearLayout
                .formatUserList();
    }

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
                    (WelfareConst.WL_DATE_TIME_7, dates.get(0)));
            jsonObject.put("endDateString", CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, dates.get(dates.size() - 1)));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    protected String getApi() {
        return null;
    }


    private void onClickSchedule(ScheduleModel schedule) {
        ScheduleDetailFragment fragment = new ScheduleDetailFragment();
        fragment.setSchedule(schedule);
        android.support.v4.app.FragmentManager manager = getParentFragment()
                .getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(trente.asia.android.R.id.ipt_id_body, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    protected void initData() {
        callApi();
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (getApi().equals(url)) {
            onLoadSchedulesSuccess(response);
        } else {
            super.successLoad(response, url);
        }
    }

    @Override
    public void onDayClick(String dayStr) {
        updateDayViews(dayStr);
        int selectedPosition = adapter.findPosition4Code(dayStr);
        observableListView.setSelection(selectedPosition);
    }

    protected void updateDayViews(String dayStr) {
        for (WeeklyCalendarDayView view : calendarDayViews) {
            if (dayStr.equals(view.dayStr)) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
    }


    protected void onLoadSchedulesSuccess(JSONObject response) {
        schedules = CCJsonUtil.convertToModelList
                (response.optString("schedules"), ScheduleModel.class);
        separateDateTime(schedules);
        List<ApiObjectModel> categories = CCJsonUtil.convertToModelList
                (response.optString("categories"), ApiObjectModel.class);
        calendars = CCJsonUtil.convertToModelList(response.optString
                ("calendars"), CalendarModel.class);
        List<UserModel> calendarUsers = CCJsonUtil.convertToModelList
                (response.optString("calendarUsers"), UserModel.class);
        filteredUsers = initFilteredUser(calendarUsers);
        updateSchedules(schedules, categories);
        calendarDayModels = buildCalendarDayModels(schedules);
        holidayModels = CCJsonUtil.convertToModelList(response.optString
                ("holidayList"), HolidayModel.class);

        // clear old data
        for (WeeklyCalendarDayView dayView : calendarDayViews) {
            CalendarDayModel calendarDayModel = getCalendarDayModelByDate
                    (dayView.getDate(), calendarDayModels);
            dayView.setData(calendarDayModel, this, holidayModels);
        }

        if (!CCCollectionUtil.isEmpty(calendarUsers)) {
            pageSharingHolder.updateFilter(calendarUsers);
        }

        updateHeaderTitles();
        updateObservableListView();
    }

    protected void updateObservableListView() {
        List<CalendarDayModel> displayedModels = getDisplayedDayForList();
        adapter = new CalendarDayListAdapter(activity, R.layout
                .item_calendar_day, displayedModels, new
                CalendarDayListAdapter.OnScheduleClickListener() {

                    @Override
                    public void onClick(ScheduleModel schedule) {
                        onClickSchedule(schedule);
                    }
                });
        observableListView.setAdapter(adapter);
    }

    protected String getCalendarName(String calendarId) {
        for (CalendarModel calendarModel : calendars) {
            if (calendarModel.key.equals(calendarId)) {
                return calendarModel.calendarName;
            }
        }
        return "";
    }

    private List<UserModel> initFilteredUser(List<UserModel> allUsers) {
        List<UserModel> userModels = new ArrayList<>();
        for (UserModel userModel : allUsers) {
            userModels.add(userModel);
        }
        return userModels;
    }

    public static void updateSchedules(List<ScheduleModel> schedules,
                                       List<ApiObjectModel> categories) {
        Map<String, String> categoriesMap = convertList2Map(categories);
        for (ScheduleModel schedule : schedules) {
            schedule.categoryName = categoriesMap.get(schedule.categoryId);
        }
    }

    public static List<CalendarDayModel> buildCalendarDayModels
            (List<ScheduleModel> schedules) {
        List<CalendarDayModel> calendarDayModels = new ArrayList<>();
        for (ScheduleModel scheduleModel : schedules) {
            CalendarDayModel calendarDayModel = getCalendarDayModel
                    (scheduleModel.startDate, calendarDayModels);
            if (calendarDayModel == null) {
                calendarDayModel = new CalendarDayModel();
                calendarDayModel.date = scheduleModel.startDate;
                calendarDayModel.schedules = new ArrayList<>();
                calendarDayModel.schedules.add(scheduleModel);
                calendarDayModels.add(calendarDayModel);
            } else {
                calendarDayModel.schedules.add(scheduleModel);
            }
        }

        Comparator<CalendarDayModel> comparator = new
                Comparator<CalendarDayModel>() {

                    @Override
                    public int compare(CalendarDayModel left,
                                       CalendarDayModel right) {
                        return CCDateUtil.makeDateCustom(left.date, WelfareConst
                                .WL_DATE_TIME_7).compareTo(CCDateUtil
                                .makeDateCustom
                                        (right.date, WelfareConst
                                                .WL_DATE_TIME_7));
                    }
                };

        Collections.sort(calendarDayModels, comparator);
        return calendarDayModels;
    }

    private static void separateDateTime(List<ScheduleModel> scheduleModels) {
        for (ScheduleModel scheduleModel : scheduleModels) {
            Date startDate = CCDateUtil.makeDateCustom(scheduleModel
                    .startDate, WelfareConst.WL_DATE_TIME_1);
            Date endDate = CCDateUtil.makeDateCustom(scheduleModel.endDate,
                    WelfareConst.WL_DATE_TIME_1);
            scheduleModel.startDate = CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, startDate);
            scheduleModel.endDate = CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, endDate);
            scheduleModel.startTime = CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_9, startDate);
            scheduleModel.endTime = CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_9, endDate);
        }
    }

    public static CalendarDayModel getCalendarDayModelByDate(Date date,
                                                             List<CalendarDayModel> calendarDayModels) {
        for (CalendarDayModel calendarDayModel : calendarDayModels) {
            if (calendarDayModel.date.equals(CCFormatUtil.formatDateCustom
                    (WelfareConst.WL_DATE_TIME_7, date))) {
                return calendarDayModel;
            }
        }
        return null;
    }

    public static CalendarDayModel getCalendarDayModel(String day,
                                                       List<CalendarDayModel>
                                                               calendarDayModels) {
        for (CalendarDayModel calendarDayModel : calendarDayModels) {
            if (calendarDayModel.date.equals(day)) {
                return calendarDayModel;
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void onCalendarDaySelected(CalendarDayModel reportModel) {
        // // TODO: 2/8/2017
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setPageSharingHolder(PageSharingHolder pageSharingHolder) {
        this.pageSharingHolder = pageSharingHolder;
    }

    protected List<CalendarDayModel> getDisplayedDayForList() {
        return calendarDayModels;
    }

    protected String getUpperTitle() {
        return CCFormatUtil.formatDateCustom(WelfareConst
                .WL_DATE_TIME_12, dates.get(0));
    }

    public void setPagePosition(int pagePosition) {
        this.pagePosition = pagePosition;
    }


}

package trente.asia.calendar.services.calendar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.define.CsConst;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;

import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public abstract class SchedulesPageListViewFragment extends
        SchedulesPageFragment implements WeeklyCalendarDayView
        .OnDayClickListener {

    protected List<WeeklyCalendarHeaderRowView> lstHeaderRow = new
            ArrayList<>();
    protected List<CalendarDayModel> calendarDayModels;

    protected List<WeeklyCalendarDayView> calendarDayViews = new ArrayList<>();


    @Override
    protected void initDayViews() {
        dates = getAllDate();
        WeeklyCalendarHeaderRowView rowView = null;
        for (int index = 0; index < dates.size(); index++) {
            Date date = dates.get(index);
            if (index % CsConst.DAY_NUMBER_A_WEEK == 0) {
                rowView = new WeeklyCalendarHeaderRowView(activity, index /
                        CsConst.DAY_NUMBER_A_WEEK);
                rowView.initialization();
                lnrCalendarContainer.addView(rowView);
                lstHeaderRow.add(rowView);
            }

            WeeklyCalendarDayView dayView = new WeeklyCalendarDayView(activity);
            dayView.initialization(date);
            rowView.lnrRowContent.addView(dayView);
            calendarDayViews.add(dayView);
        }
    }

    abstract public void updateList(String dayStr);

    protected void onLoadSchedulesSuccess(JSONObject response) {
        super.onLoadSchedulesSuccess(response);
        separateDateTime(lstSchedule);
        updateSchedules(lstSchedule, lstCategories);
        calendarDayModels = buildCalendarDayModels(lstSchedule);
        if (!CCCollectionUtil.isEmpty(lstCalendarUser)) {
            pageSharingHolder.updateFilter(lstCalendarUser);
        }
        clearOldData();
    }

    @Override
    protected void clearOldData() {
        for (WeeklyCalendarDayView dayView : calendarDayViews) {
            CalendarDayModel calendarDayModel = getCalendarDayModel
                    (dayView.dayStr, calendarDayModels);
            dayView.setData(calendarDayModel, this, lstHoliday);
        }
    }

    abstract protected void updateObservableScrollableView();

    public void updateSchedules(List<ScheduleModel> schedules,
                                List<ApiObjectModel> categories) {
        Map<String, String> categoriesMap = convertList2Map(categories);
        for (ScheduleModel schedule : schedules) {
            schedule.categoryName = categoriesMap.get(schedule.categoryId);
        }
    }

    public List<CalendarDayModel> buildCalendarDayModels(List<ScheduleModel>
                                                                 schedules) {
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

    private void separateDateTime(List<ScheduleModel> scheduleModels) {
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

    public CalendarDayModel getCalendarDayModel(String day,
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

    protected List<CalendarDayModel> getDisplayedDayForList() {
        return calendarDayModels;
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
}

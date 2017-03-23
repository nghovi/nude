package trente.asia.calendar.services.summary;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.calendar.services.calendar.SchedulesPageListViewFragment;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;

/**
 * SummaryPageFragment
 *
 * @author VietNH
 */
public class SummaryDetailFragment extends AbstractClFragment implements WeeklyScheduleListAdapter.OnScheduleItemClickListener{

	private SummaryModel				summaryModel;
	private LinearLayout				lnrBlockContainer;
	private List<CalendarDayModel>		calendarDayModels;
	private WeeklyScheduleListAdapter	adapter;
	private ListView					listView;
	private List<ScheduleModel>			schedules;
	private List<CategoryModel>			categories;
	private List<Date>					dates;
	private Date						selectedDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_summary_detail, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		loadSummaryDetail();
	}

	private void loadSummaryDetail(){
		Calendar calendar = CCDateUtil.makeCalendar(selectedDate);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date endDate = calendar.getTime();

		JSONObject jsonObject = SchedulesPageFragment.buildJsonObjForScheduleListRequest(prefAccUtil, startDate, endDate);
		requestLoad(WfUrlConst.WF_CL_WEEK_SCHEDULE, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_WEEK_SCHEDULE.equals(url)){
			onLoadSchedulesSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, CCDateUtil.makeDateCustom(summaryModel.month, WelfareConst.WL_DATE_TIME_5)), null);
		lnrBlockContainer = (LinearLayout)getView().findViewById(R.id.lnr_block_container);
		listView = (ListView)getView().findViewById(R.id.lst_fragment_summary_detail);
		listView.setDivider(null);
		GraphColumn.addBlocks(getContext(), summaryModel, lnrBlockContainer, false);
		dates = SchedulesPageFragment.getAllDateForMonth(prefAccUtil, selectedDate);
	}

	protected void onLoadSchedulesSuccess(JSONObject response){
		schedules = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);
		categories = CCJsonUtil.convertToModelList(response.optString("categories"), CategoryModel.class);
		List<HolidayModel> lstHoliday = CCJsonUtil.convertToModelList(response.optString("holidayList"), HolidayModel.class);
		SchedulesPageFragment.updateSchedules(schedules, categories);
		calendarDayModels = SchedulesPageListViewFragment.buildCalendarDayModelsFromSchedules(schedules, dates);
		adapter = new WeeklyScheduleListAdapter(activity, R.layout.item_calendar_day, calendarDayModels, lstHoliday, this);
		listView.setAdapter(adapter);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_summary;
	}

	@Override
	public void onClick(View v){

	}

	public void setSummaryModel(SummaryModel summaryModel){
		this.summaryModel = summaryModel;
		Date month = CCDateUtil.makeDateCustom(summaryModel.month, WelfareConst.WL_DATE_TIME_5);
		Calendar c = CCDateUtil.makeCalendar(month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		this.selectedDate = c.getTime();
	}

	@Override
	public void onClickScheduleItem(ScheduleModel schedule, Date selectedDate){
		SchedulesPageFragment.gotoScheduleDetail((WelfareActivity)activity, schedule, selectedDate);
	}
}

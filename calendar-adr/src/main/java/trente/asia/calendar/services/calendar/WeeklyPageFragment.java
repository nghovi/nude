package trente.asia.calendar.services.calendar;

import static trente.asia.android.util.CsDateUtil.CS_DATE_TIME_1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends SchedulesPageFragment{

	protected LinearLayout	lnrHeader;
	private RelativeLayout	rltContent;
	private LinearLayout	lnrPart1;
	private LinearLayout	lnrPart2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_weekly_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
		super.updateSchedules(schedules, categories);

		int cellWidth = lnrHeader.getMeasuredWidth() / 8;

		Calendar c = CCDateUtil.makeCalendarToday();
		Map<String, List<ScheduleModel>> startTimeSchedulesMap = new HashMap<>();
		for(int i = 0; i < 24; i++){
			String startTime = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, c.getTime());
			startTimeSchedulesMap.put(startTime, new ArrayList<ScheduleModel>());
			c.add(Calendar.HOUR, 1);
		}

		for(ScheduleModel scheduleModel : schedules){
			if(!scheduleModel.isAllDay){
				String keyMap = scheduleModel.startTime.split(":")[0] + ":00";
				startTimeSchedulesMap.get(keyMap).add(scheduleModel);
			}
		}

		Calendar c1 = CCDateUtil.makeCalendarWithDateOnly(dates.get(0));

		List<String> keys = new ArrayList<>(startTimeSchedulesMap.keySet());
		Collections.sort(keys);

		for(String key : keys){
			View cell = inflater.inflate(R.layout.item_weekly_schedule, null);
			((TextView)cell.findViewById(R.id.txt_item_daily_schedule_start_time)).setText(key);
			RelativeLayout rltSchedules = (RelativeLayout)cell.findViewById(R.id.rlt_schedule_weekly_container);

			int i = 0;
			for(ScheduleModel schedule : startTimeSchedulesMap.get(key)){
				TextView textView = new TextView(activity);
				Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(schedule.startDate, WelfareConst.WF_DATE_TIME));
				int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
				int leftMargin = cellWidth * (1 + dayDistance);
				int topMargin = (i++) * 48;
				textView.setMaxWidth(cellWidth);
				textView.setMaxLines(1);
				textView.setTextColor(Color.parseColor(schedule.getScheduleColor()));
				textView.setEllipsize(TextUtils.TruncateAt.END);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cellWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(leftMargin, topMargin, 0, 0);
				textView.setLayoutParams(lp);
				textView.setText(schedule.scheduleName);
				rltSchedules.addView(textView);
			}
			lnrPart2.addView(cell);
		}
	}

	protected void initCalendarView(){
		initCalendarHeader();
		initDayViews();
	}

	@Override
	protected void initCalendarHeader(){
		lnrHeader = (LinearLayout)getView().findViewById(R.id.lnr_weekly_header);
		lnrHeader.removeAllViews();
		addCell("", "", 0);
		Calendar c = Calendar.getInstance();
		for(Date date : dates){
			c.setTime(date);
			addCell(String.valueOf(c.get(Calendar.DAY_OF_MONTH)), CCStringUtil.toUpperCase(CCFormatUtil.formatDateCustom(CS_DATE_TIME_1, date)), c.get(Calendar.DAY_OF_WEEK));
		}
	}

	private void addCell(String number, String day, int dayOfWeek){
		LinearLayout lnrDay = new LinearLayout(activity);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.weight = 1;
		lnrDay.setGravity(Gravity.CENTER);
		lnrDay.setLayoutParams(layoutParams);
		lnrDay.setOrientation(LinearLayout.VERTICAL);

		TextView txtDayNumber = new TextView(activity);
		txtDayNumber.setGravity(Gravity.CENTER);
		txtDayNumber.setText(number);

		TextView txtTitleItem = new TextView(activity);
		txtTitleItem.setGravity(Gravity.CENTER);
		txtTitleItem.setText(day);

		if(Calendar.SUNDAY == dayOfWeek){
			txtTitleItem.setTextColor(Color.RED);
			txtDayNumber.setTextColor(Color.RED);
		}else if(Calendar.SATURDAY == dayOfWeek){
			txtTitleItem.setTextColor(Color.BLUE);
			txtDayNumber.setTextColor(Color.BLUE);
		}else{
			txtTitleItem.setTextColor(getNormalDayColor());
			txtDayNumber.setTextColor(getNormalDayColor());
		}

		lnrDay.addView(txtDayNumber);
		lnrDay.addView(txtTitleItem);

		lnrHeader.addView(lnrDay);
	}

	@Override
	protected void initDayViews(){
		LinearLayout lnrVerticalLineContainer = (LinearLayout)getView().findViewById(R.id.lnr_vertical_line_container);

		for(int i = 0; i < 8; i++){
			View verticalBar = new View(activity);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
			verticalBar.setLayoutParams(layoutParams);
			verticalBar.setBackgroundColor(Color.BLUE);

			LinearLayout lnrDay = new LinearLayout(activity);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			lnrDay.setGravity(Gravity.RIGHT);
			lnrDay.setLayoutParams(lp);

			lnrDay.addView(verticalBar);
			lnrVerticalLineContainer.addView(lnrDay);
		}

	}

	@Override
	protected void initView(){
		super.initView();
		rltContent = (RelativeLayout)getView().findViewById(R.id.rlt_content);
		lnrPart1 = (LinearLayout)getView().findViewById(R.id.lnr_part1);
		lnrPart2 = (LinearLayout)getView().findViewById(R.id.lnr_part2);
	}

	@Override
	protected List<Date> getAllDate(){
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		List<Date> dates = CsDateUtil.getAllDate4Week(CCDateUtil.makeCalendar(selectedDate), firstDay);
		return dates;
	}

	@Override
	public void onClick(View v){

	}
}

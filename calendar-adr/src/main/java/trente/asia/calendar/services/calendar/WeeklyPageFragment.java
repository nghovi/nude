package trente.asia.calendar.services.calendar;

import static trente.asia.android.util.CsDateUtil.CS_DATE_TIME_1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends SchedulesPageFragment{

	private static final Integer		CELL_HEIGHT	= 62;
	protected LinearLayout				lnrHeader;
	private RelativeLayout				rltContent;
	private RelativeLayout				rltPart1;
	private LinearLayout				lnrPart2;
	private Map<Integer, List<Integer>>	columnTopMarginsMap;

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
		buildPart1();
		buildPart2(schedules);

	}

	private void buildPart1(){
		rltPart1.removeAllViews();
		int cellWidth = lnrHeader.getMeasuredWidth() / 8;
		Calendar c1 = CCDateUtil.makeCalendarWithDateOnly(dates.get(0));
		columnTopMarginsMap = new HashMap<>();

		// todo
		for(Todo todo : todos){
			if(!CCStringUtil.isEmpty(todo.limitDate)){
				Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(todo.limitDate, WelfareConst.WF_DATE_TIME));
				int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);

				if(!columnTopMarginsMap.keySet().contains(dayDistance)){
					int leftMargin = cellWidth * (1 + dayDistance);
					int topMargin = getNextTopMargin(dayDistance, dayDistance);
					TextView textView = makeTextView(activity, getString(R.string.cl_footer_todo), leftMargin, topMargin, cellWidth, Color.GRAY, 0);
					rltPart1.addView(textView);
				}
			}
		}

		// birthday
		for(UserModel userModel : lstBirthdayUser){
			Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(userModel.dateBirth, WelfareConst.WF_DATE_TIME));
			int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
			int leftMargin = cellWidth * (1 + dayDistance) + (cellWidth - CELL_HEIGHT) / 2;
			int topMargin = getNextTopMargin(dayDistance, dayDistance);
			ImageView imageViewBirthday = new ImageView(activity);
			imageViewBirthday.setImageResource(R.drawable.cl_icon_birthday);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(CELL_HEIGHT, CELL_HEIGHT);
			lp.setMargins(leftMargin, topMargin, 0, 0);
			imageViewBirthday.setLayoutParams(lp);
			rltPart1.addView(imageViewBirthday);
		}

		// holiday
		for(HolidayModel holidayModel : lstHoliday){
			Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(holidayModel.startDate, WelfareConst.WF_DATE_TIME));
			int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
			int leftMargin = cellWidth * (1 + dayDistance);
			int topMargin = getNextTopMargin(dayDistance, dayDistance);
			TextView textView = makeTextView(activity, holidayModel.holidayName, leftMargin, topMargin, cellWidth, Color.WHITE, Color.RED);
			rltPart1.addView(textView);
		}

		// offer
		for(WorkOffer workOffer : lstWorkOffer){
			Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(workOffer.startDate, WelfareConst.WF_DATE_TIME));
			int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
			int leftMargin = cellWidth * (1 + dayDistance);
			int topMargin = getNextTopMargin(dayDistance, dayDistance);
			TextView textView = makeTextView(activity, workOffer.offerTypeName, leftMargin, topMargin, cellWidth, Color.BLACK, Color.WHITE);
			rltPart1.addView(textView);
		}

		// all day schedules
		for(ScheduleModel schedule : lstSchedule){

			if(schedule.isAllDay){
				Calendar cStart = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(schedule.startDate, WelfareConst.WF_DATE_TIME));
				Calendar cEnd = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(schedule.endDate, WelfareConst.WF_DATE_TIME));

				int dayDistance = cStart.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
				dayDistance = dayDistance < 0 ? 0 : dayDistance;
				int dayNumber = cEnd.get(Calendar.DAY_OF_YEAR) - cStart.get(Calendar.DAY_OF_YEAR) + 1;
				int cellNumber = dayNumber > 7 - dayDistance ? 7 - dayDistance : dayNumber;
				int maxWidth = cellWidth * cellNumber;
				int leftMargin = cellWidth * (1 + dayDistance);

				int topMargin = getNextTopMargin(dayDistance, dayDistance + cellNumber - 1);
				TextView textView = makeTextView(activity, schedule.scheduleName, leftMargin, topMargin, maxWidth, getColor(schedule), Color.parseColor(schedule.getScheduleColor()));
				rltPart1.addView(textView);
			}

		}
	}

	private int getNextTopMargin(int keyStart, int keyEnd){
		int result = 0;
		boolean sastisfied = false;
		while(!sastisfied){
			for(int i = keyStart; i <= keyEnd; i++){
				List<Integer> usedMarginTops = columnTopMarginsMap.get(i);
				if(CCCollectionUtil.isEmpty(usedMarginTops)){
					sastisfied = true;
				}else{
					if(!usedMarginTops.contains(result)){
						sastisfied = true;
					}else{
						sastisfied = false;
						break;
					}
				}
			}

			result = sastisfied ? result : result + CELL_HEIGHT;
		}

		for(int i = keyStart; i <= keyEnd; i++){
			List<Integer> usedMarginTops = columnTopMarginsMap.get(i);
			if(CCCollectionUtil.isEmpty(usedMarginTops)){
				usedMarginTops = new ArrayList<>();
				usedMarginTops.add(result);
				columnTopMarginsMap.put(i, usedMarginTops);
			}else{
				usedMarginTops.add(result);
			}
		}

		return result;
	}

	public static TextView makeTextView(Context activity, String text, int leftMargin, int topMargin, int maxWidth, int bgColor, int textColor){
		TextView textView = new TextView(activity);
		textView.setMaxWidth(maxWidth);
		textView.setMaxLines(1);
		// textView.setTextColor(Color.parseColor(schedule.getScheduleColor()));
		textView.setEllipsize(TextUtils.TruncateAt.END);
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(13);
		textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.wf_background_round_border_white));
		GradientDrawable bgShape = (GradientDrawable)textView.getBackground();
		bgShape.setColor(bgColor);
		if(textColor != 0){
			textView.setTextColor(textColor);
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(maxWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(leftMargin, topMargin, 0, 0);
		textView.setLayoutParams(lp);
		textView.setText(text);
		return textView;
	}

	private void buildPart2(List<ScheduleModel> schedules){
		lnrPart2.removeAllViews();
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

		Map<Integer, Integer> leftMarginScheduleNumMap = new HashMap<>();

		for(String key : keys){
			View cell = inflater.inflate(R.layout.item_weekly_schedule, null);
			((TextView)cell.findViewById(R.id.txt_item_daily_schedule_start_time)).setText(key);
			RelativeLayout rltSchedules = (RelativeLayout)cell.findViewById(R.id.rlt_schedule_weekly_container);

			for(final ScheduleModel schedule : startTimeSchedulesMap.get(key)){
				Calendar c2 = CCDateUtil.makeCalendarWithDateOnly(CCDateUtil.makeDateCustom(schedule.startDate, WelfareConst.WF_DATE_TIME));
				int dayDistance = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
				int leftMargin = cellWidth * (1 + dayDistance);
				int topMargin = 0;
				int leftMarginScheduleNum = 0;
				if(leftMarginScheduleNumMap.containsKey(leftMargin)){
					leftMarginScheduleNum = leftMarginScheduleNumMap.get(leftMargin);
					topMargin = leftMarginScheduleNum * CELL_HEIGHT;
				}

				leftMarginScheduleNumMap.put(leftMargin, leftMarginScheduleNum + 1);

				TextView textView = makeTextView(activity, schedule.scheduleName, leftMargin, topMargin, cellWidth, getColor(schedule), Color.RED);
				textView.setTextColor(Color.parseColor(schedule.getScheduleColor()));
				textView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						onDailyScheduleClickListener(schedule.startDate.split(" ")[0]);
					}
				});
				rltSchedules.addView(textView);
			}
			lnrPart2.addView(cell);
		}
	}

	public static int getColor(ScheduleModel scheduleModel){
		return Color.RED;
		// return Color.parseColor(scheduleModel.getScheduleColor());
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
		rltPart1 = (RelativeLayout)getView().findViewById(R.id.rlt_part1);
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

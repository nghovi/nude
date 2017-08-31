package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.TodoDialog;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarRowView;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * MonthlyPageFragment
 *
 * @author TrungND
 */
public class MonthlyPageFragment extends SchedulesPageFragment{

	private static final int					MAX_ROW			= 4;
	private List<MonthlyCalendarDayView>		lstCalendarDay	= new ArrayList<>();
	private List<MonthlyCalendarRowView>		lstCalendarRow	= new ArrayList<>();

	private LinearLayout						lnrTodoSection;
	private LinearLayout						lnrTodos;
	private LayoutInflater						inflater;
	private Todo								selectedTodo;
	private TodoDialog							dlgTodoDetail;
	private WfDialog							dlgDeleteConfirm;
	private ImageView							expIcon;
	private Map<String, MonthlyCalendarDayView>	dateDayViewMap	= new HashMap<>();

	@Override
	protected void initView(){
		super.initView();
		inflater = LayoutInflater.from(activity);

		expIcon = (ImageView)getView().findViewById(R.id.img_fragment_monthly_todo_expand_icon);

		lnrTodoSection = (LinearLayout)getView().findViewById(R.id.lnr_todos);
		lnrTodos = (LinearLayout)getView().findViewById(R.id.lnr_todo_container);
		lnrTodoSection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(!CCCollectionUtil.isEmpty(todos)){
					if(isExpanded){
						expIcon.setImageResource(R.drawable.upt);
						collapse(lnrTodoSection, false);
						isExpanded = false;
					}else{
						expIcon.setImageResource(R.drawable.downt);
						expand(lnrTodoSection);
						isExpanded = true;
					}
				}
			}
		});
	}

	@Override
	protected void initCalendarHeader(){
		LinearLayout lnrHeader = (LinearLayout)getView().findViewById(R.id.lnr_calendar_header);
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		List<DayModel> dayModels = CsDateUtil.getAllDay4Week(firstDay);
		for(DayModel dayModel : dayModels){

			TextView txtDay = new TextView(activity);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			txtDay.setLayoutParams(layoutParams);
			txtDay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

			if(Calendar.SUNDAY == dayModel.dayOfWeek){
				txtDay.setTextColor(Color.RED);
			}else if(Calendar.SATURDAY == dayModel.dayOfWeek){
				txtDay.setTextColor(Color.BLUE);
			}else{
				txtDay.setTextColor(getNormalDayColor());
			}
			txtDay.setText(CCStringUtil.toUpperCase(dayModel.day));
			lnrHeader.addView(txtDay);
		}
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(ClConst.API_TODO_UPDATE.equals(url) || ClConst.API_TODO_DELETE.equals(url)){
			if(dlgTodoDetail != null && dlgTodoDetail.isShowing()){
				dlgTodoDetail.dismiss();
			}
			if(dlgDeleteConfirm != null && dlgDeleteConfirm.isShowing()){
				dlgDeleteConfirm.dismiss();
			}
			View cell = lnrTodoSection.findViewWithTag(selectedTodo.key);
			removeTodo(selectedTodo);
			lnrTodos.removeView(cell);
			collapse(lnrTodoSection, true);
		}else{
			super.successUpdate(response, url);
		}
	}

	private void removeTodo(Todo selectedTodo){
		Iterator<Todo> it = todos.iterator();
		while(it.hasNext()){
			Todo todo = it.next();
			if(todo.key.equals(selectedTodo.key)){
				it.remove();
			}
		}
	}

	public void buildTodoList(List<Todo> todos){
		if(CCCollectionUtil.isEmpty(todos)){
			lnrTodoSection.setVisibility(View.GONE);
			return;
		}else{
			lnrTodoSection.setVisibility(View.VISIBLE);
		}
		for(int i = 0; i < todos.size(); i++){
			final Todo todo = todos.get(i);
			View cell = inflater.inflate(R.layout.item_todo_unfinished_month, null);
			//// TODO: 8/9/17 why inflate don't keep height in xml file ?
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WelfareUtil.dpToPx(44));
			cell.setLayoutParams(lp);
			cell.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					showTodoDetailDialog(todo);
				}
			});
			TextView txtDate = (TextView)cell.findViewById(R.id.txt_item_todo_date);
			TextView txtTitle = (TextView)cell.findViewById(R.id.txt_item_todo_title);
			txtTitle.setText(todo.name);

			if(CCDateUtil.compareDate(todo.limitDate, today, false) <= 0){
				txtDate.setTextColor(Color.RED);
			}
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, today).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, todo.limitDate))){
				txtDate.setText(getString(R.string.chiase_common_today));
			}else{
				txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_MM_DD, todo.limitDate));
			}
			RadioButton radioButton = (RadioButton)cell.findViewById(R.id.radio);
			radioButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					finishTodo(todo);
				}
			});
			cell.setTag(todo.key);
			lnrTodos.addView(cell);
		}
	}

	private void showTodoDetailDialog(final Todo todo){
		if(dlgTodoDetail == null || !dlgTodoDetail.isShowing()){
			dlgTodoDetail = new TodoDialog(activity, todo, new View.OnClickListener() {

				@Override
				public void onClick(View v){
					showDeleteDialog(todo);
				}
			}, new View.OnClickListener() {

				@Override
				public void onClick(View v){
					finishTodo(todo);
				}
			});

			dlgTodoDetail.show();
		}
	}

	private void showDeleteDialog(final Todo todo){
		if(dlgDeleteConfirm == null || !dlgDeleteConfirm.isShowing()){
			dlgDeleteConfirm = new WfDialog(activity);
			dlgDeleteConfirm.setDialogTitleButton(getString(R.string.sure_to_delete), getString(R.string.chiase_common_ok), getString(R.string.wf_cancel), new View.OnClickListener() {

				@Override
				public void onClick(View v){
					callDeleteApi(todo);
				}
			});
			dlgDeleteConfirm.show();
		}

	}

	private void callDeleteApi(Todo todo){
		selectedTodo = todo;
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", todo.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_DELETE, jsonObject, true);
	}

	private void finishTodo(Todo todo){
		this.selectedTodo = todo;
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", todo.key);
			jsonObject.put("name", todo.name);
			jsonObject.put("note", todo.note);
			jsonObject.put("isFinish", true);
			jsonObject.put("limitDate", todo.limitDate == null ? null : CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, todo.limitDate));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_UPDATE, jsonObject, true);
	}

	public static Comparator<ScheduleModel> getScheduleComparator(final boolean checkAllDayTime){
		return new Comparator<ScheduleModel>() {

			@Override
			public int compare(ScheduleModel schedule1, ScheduleModel schedule2){
				if(checkAllDayTime){
					String startDate1 = WelfareUtil.getDateString(schedule1.startDate);
					String endDate1 = WelfareUtil.getDateString(schedule1.endDate);

					String startDate2 = WelfareUtil.getDateString(schedule2.startDate);
					String endDate2 = WelfareUtil.getDateString(schedule2.endDate);

					boolean diff1 = startDate1.equals(endDate1);
					boolean diff2 = startDate2.equals(endDate2);

					if(!diff1 && diff2) return -1;
					if(diff1 && !diff2) return 1;
				}

				// boolean isAll1 = CCBooleanUtil.checkBoolean(schedule1.isAllDay);
				// boolean isAll2 = CCBooleanUtil.checkBoolean(schedule2.isAllDay);

				if(schedule1.isAllDay && !schedule2.isAllDay) return -1;
				if(!schedule1.isAllDay && schedule2.isAllDay) return 1;
				if(schedule1.isAllDay && schedule2.isAllDay && !checkAllDayTime){
					return schedule1.scheduleName.compareTo(schedule2.scheduleName);
				}

				Integer timeStart1 = CCDateUtil.convertTime2Min(schedule1.startTime);
				Integer timeStart2 = CCDateUtil.convertTime2Min(schedule2.startTime);
				if(timeStart1 != timeStart2){
					return timeStart1.compareTo(timeStart2);
				}

				return schedule1.scheduleName.compareTo(schedule2.scheduleName);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly_page, container, false);
		}else{

		}
		return mRootView;
	}

	protected List<Date> getAllDate(){
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		return CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(selectedDate), firstDay);
	}

	@Override
	protected void initDayViews(){
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		MonthlyCalendarRowView rowView = null;
		LinearLayout lnrRowContent = null;
		Calendar cSelected = CCDateUtil.makeCalendar(selectedDate);
		for(int index = 0; index < dates.size(); index++){
			Date itemDate = dates.get(index);
			Calendar cItemDate = CCDateUtil.makeCalendar(itemDate);

			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = (MonthlyCalendarRowView)mInflater.inflate(R.layout.monthly_calendar_row, null);
				rowView.setStartDate(itemDate);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrCalendarContainer.addView(rowView);
				lstCalendarRow.add(rowView);
			}

			MonthlyCalendarDayView dayView = (MonthlyCalendarDayView)mInflater.inflate(R.layout.monthly_calendar_row_item, null);
			dayView.initialization(itemDate, this, cSelected.get(Calendar.MONTH) != cItemDate.get(Calendar.MONTH));
			dayView.dayOfTheWeek = index % CsConst.DAY_NUMBER_A_WEEK;
			lstCalendarDay.add(dayView);
			dateDayViewMap.put(WelfareUtil.getDateString(itemDate), dayView);
			rowView.lstCalendarDay.add(dayView);
			lnrRowContent.addView(dayView);
		}
	}

	@Override
	protected String getExecType(){
		return "M";
	}

	public void expand(final View v){
		v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		int rowNum = Math.min(todos.size() + 1, MAX_ROW + 1);
		final int targetHeight = WelfareUtil.dpToPx(44 * rowNum);

		// Older versions of android (pre API 21) cancel animations for views with a height of 0.
		// v.getLayoutParams().height = 1;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t){
				v.getLayoutParams().height = interpolatedTime == 1 ? targetHeight : (int)(targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds(){
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
		v.startAnimation(a);
	}

	public void collapse(final View v, boolean collapsePart){
		final int initialHeight = v.getMeasuredHeight();
		final int firstChildHeight = ((LinearLayout)v).getChildAt(0).getMeasuredHeight();

		if(collapsePart){
			if(todos.size() == 0){
				lnrTodoSection.setVisibility(View.GONE);
			}else{
				v.getLayoutParams().height = firstChildHeight * (Math.min(MAX_ROW + 1, todos.size() + 1));
				v.requestLayout();
			}
		}else{

			Animation a = new Animation() {

				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t){
					if(interpolatedTime == 1){
						// v.setVisibility(VTodoiew.GONE);
						v.getLayoutParams().height = firstChildHeight;
						v.requestLayout();
					}else{
						v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
						v.requestLayout();
					}
				}

				@Override
				public boolean willChangeBounds(){
					return true;
				}
			};

			// 1dp/ms
			a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
			v.startAnimation(a);
		}

	}

	@Override
	protected void onLoadSchedulesSuccess(JSONObject response){
//		if(pageSharingHolder.selectedPagePosition == pagePosition)
//
//			log("onLoad schedule success!!!");
		super.onLoadSchedulesSuccess(response);

		// add holiday,
		if(!CCCollectionUtil.isEmpty(lstHoliday)){
			for(HolidayModel holidayModel : lstHoliday){
				ScheduleModel scheduleModel = new ScheduleModel(holidayModel);
				// scheduleModel.scheduleName = getString(R.string.cl_schedule_holiday_name, scheduleModel.scheduleName);
				lstSchedule.add(scheduleModel);
			}
		}

		// add birthday
		if(!CCCollectionUtil.isEmpty(lstBirthdayUser)){
			for(UserModel birthday : lstBirthdayUser){
				ScheduleModel scheduleModel = new ScheduleModel(birthday);
				// scheduleModel.scheduleName = getString(R.string.cl_schedule_birth_day_name, scheduleModel.scheduleName);
				lstSchedule.add(scheduleModel);
			}
		}

		// add work offer
		if(!CCCollectionUtil.isEmpty(lstWorkOffer)){
			for(WorkOffer workOffer : lstWorkOffer){
				ScheduleModel scheduleModel = new ScheduleModel(workOffer);
				// scheduleModel.scheduleName = scheduleModel.scheduleName;
				lstSchedule.add(0, scheduleModel);
			}
		}

		clearOldData();
//		if(pageSharingHolder.selectedPagePosition == pagePosition) log("done for clear data");

		for(ScheduleModel schedule : lstSchedule){
			if(schedule.isPeriod){
				for(MonthlyCalendarRowView rowView : lstCalendarRow){
					Date minDate = rowView.lstCalendarDay.get(0).date;
					Date maxDate = rowView.lstCalendarDay.get(rowView.lstCalendarDay.size() - 1).date;
					boolean isStartBelongPeriod = ClUtil.belongPeriod(schedule.startDate, minDate, maxDate);
					boolean isEndBelongPeriod = ClUtil.belongPeriod(schedule.endDate, minDate, maxDate);
					boolean isOverPeriod = schedule.startDate.compareTo(minDate) < 0 && schedule.endDate.compareTo(maxDate) > 0;

					if(isStartBelongPeriod || isEndBelongPeriod || isOverPeriod){
						rowView.addSchedule(schedule);
					}
				}
			}else{
				dateDayViewMap.get(WelfareUtil.getDateString(schedule.startDate)).addSchedule(schedule);
				// MonthlyCalendarDayView activeCalendarDay = ClUtil.findView4Day(lstCalendarDay, schedule.startDate, schedule.endDate);
				// if(activeCalendarDay != null){
				// activeCalendarDay.addSchedule(schedule);
				// }
			}
		}

		for(MonthlyCalendarRowView rowView : lstCalendarRow){
			rowView.refreshLayout();
		}

		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			dayView.showSchedules();
		}
//		if(pageSharingHolder.selectedPagePosition == pagePosition) log("done for schedule");
		buildTodoList(todos);
		// if(pageSharingHolder.selectedPagePosition == pagePosition)
		//
		// log("done for todo");

	}

	@Override
	protected void clearOldData(){
		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			dayView.removeAllData();
		}

		for(MonthlyCalendarRowView rowView : lstCalendarRow){
			rowView.removeAllData();
		}
		// lstScheduleWithoutHoliday.clear();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}
}

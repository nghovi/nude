package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.TodoDialog;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarRowView;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * MonthlyPageFragment
 *
 * @author TrungND
 */
public class MonthlyPageFragment extends SchedulesPageFragment{

	private static final int				MAX_ROW			= 3;
	private List<MonthlyCalendarDayView>	lstCalendarDay	= new ArrayList<>();
	private List<MonthlyCalendarRowView>	lstCalendarRow	= new ArrayList<>();

	private LinearLayout					lnrTodoSection;
	private LinearLayout					lnrTodos;
	private LayoutInflater					inflater;
	private Todo							selectedTodo;
	private TodoDialog						dlgTodoDetail;
	private WfDialog						dlgDeleteConfirm;

	@Override
	protected void initView(){
		super.initView();
		inflater = LayoutInflater.from(activity);

		lnrTodoSection = (LinearLayout)getView().findViewById(R.id.lnr_todos);
		lnrTodos = (LinearLayout)getView().findViewById(R.id.lnr_todo_container);
		lnrTodoSection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(!CCCollectionUtil.isEmpty(todos)){
					if(isExpanded){
						collapse(lnrTodoSection, false);
						isExpanded = false;
					}else{
						expand(lnrTodoSection);
						isExpanded = true;
					}
				}
			}
		});
        int a = 5/0;
	}

	@Override
	protected void initCalendarHeader(){
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View titleView = mInflater.inflate(getCalendarHeaderItem(), null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		List<DayModel> dayModels = CsDateUtil.getAllDay4Week(firstDay);
		for(DayModel dayModel : dayModels){
			View titleItem = mInflater.inflate(R.layout.monthly_calendar_title_item, null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			titleItem.setLayoutParams(layoutParams);
			TextView txtTitleItem = (TextView)titleItem.findViewById(R.id.monthly_calendar_title_day_label);
			titleItem.findViewById(R.id.lnr_title_background).setBackgroundColor(getHeaderBgColor());
			if(Calendar.SUNDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.RED);
			}else if(Calendar.SATURDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.BLUE);
			}else{
				txtTitleItem.setTextColor(getNormalDayColor());
			}
			txtTitleItem.setText(CCStringUtil.toUpperCase(dayModel.day));
			lnrRowTitle.addView(titleItem);
		}
		lnrCalendarContainer.addView(titleView);
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
		Date today = Calendar.getInstance().getTime();
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

			if(CCStringUtil.isEmpty(todo.limitDate)){
				txtTitle.setText(getString(R.string.no_deadline));
			}else{
				Date date = CCDateUtil.makeDateCustom(todo.limitDate, WelfareConst.WF_DATE_TIME);
				if(CCDateUtil.compareDate(date, today, false) >= 0){
					txtDate.setTextColor(Color.RED);
				}
				if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, today).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date))){
					txtDate.setText(getString(R.string.chiase_common_today));
				}else{
					txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_MM_DD, date));
				}
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
			jsonObject.put("limitDate", CCStringUtil.isEmpty(todo.limitDate) ? todo.limitDate : todo.limitDate.split(":")[0]);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ClConst.API_TODO_UPDATE, jsonObject, true);
	}

	@Override
	public int getCalendarHeaderItem(){
		return R.layout.monthly_calendar_title;
	}

	public class ScheduleComparator implements Comparator<ScheduleModel>{

		@Override
		public int compare(ScheduleModel schedule1, ScheduleModel schedule2){
			String startDate1 = WelfareFormatUtil.removeTime4Date(schedule1.startDate);
			String endDate1 = WelfareFormatUtil.removeTime4Date(schedule1.endDate);

			String startDate2 = WelfareFormatUtil.removeTime4Date(schedule2.startDate);
			String endDate2 = WelfareFormatUtil.removeTime4Date(schedule2.endDate);

			boolean diff1 = startDate1.equals(endDate1);
			boolean diff2 = startDate2.equals(endDate2);

			if(!diff1 && diff2) return -1;
			if(diff1 && !diff2) return 1;

			boolean isAll1 = CCBooleanUtil.checkBoolean(schedule1.isAllDay);
			boolean isAll2 = CCBooleanUtil.checkBoolean(schedule2.isAllDay);

			if(isAll1 && !isAll2) return -1;
			if(!isAll1 && isAll2) return 1;
			return 0;
		}
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
		if(!CCCollectionUtil.isEmpty(dates)){
			for(int index = 0; index < dates.size(); index++){
				Date itemDate = dates.get(index);
				if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
					rowView = (MonthlyCalendarRowView)mInflater.inflate(R.layout.monthly_calendar_row, null);
					rowView.setStartDate(itemDate);
					lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
					lnrCalendarContainer.addView(rowView);
					lstCalendarRow.add(rowView);
				}

				MonthlyCalendarDayView dayView = (MonthlyCalendarDayView)mInflater.inflate(R.layout.monthly_calendar_row_item, null);
				dayView.initialization(itemDate, this, CsDateUtil.isDiffMonth(itemDate, selectedDate));
				dayView.dayOfTheWeek = index % CsConst.DAY_NUMBER_A_WEEK;
				lstCalendarDay.add(dayView);
				rowView.lstCalendarDay.add(dayView);
				lnrRowContent.addView(dayView);
			}
		}
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
		super.onLoadSchedulesSuccess(response);
		clearOldData();
		if(!CCCollectionUtil.isEmpty(lstSchedule)){
			Collections.sort(lstSchedule, new ScheduleComparator());
			for(ScheduleModel model : lstSchedule){
				Date startDate = WelfareFormatUtil.makeDate(WelfareFormatUtil.removeTime4Date(model.startDate));
				Date endDate = WelfareFormatUtil.makeDate(WelfareFormatUtil.removeTime4Date(model.endDate));
				if(model.isPeriodSchedule()){
					for(MonthlyCalendarRowView rowView : lstCalendarRow){
						Date minDate = WelfareFormatUtil.makeDate(rowView.lstCalendarDay.get(0).day);
						Date maxDate = WelfareFormatUtil.makeDate(rowView.lstCalendarDay.get(rowView.lstCalendarDay.size() - 1).day);
						boolean isStartBelongPeriod = ClUtil.belongPeriod(startDate, minDate, maxDate);
						boolean isEndBelongPeriod = ClUtil.belongPeriod(endDate, minDate, maxDate);
						boolean isOverPeriod = startDate.compareTo(minDate) < 0 && endDate.compareTo(maxDate) > 0;

						if(isStartBelongPeriod || isEndBelongPeriod || isOverPeriod){
							rowView.addSchedule(model);
						}
					}
				}else{
					MonthlyCalendarDayView activeCalendarDay = ClUtil.findView4Day(lstCalendarDay, startDate, endDate);
					if(activeCalendarDay != null){
						activeCalendarDay.addSchedule(model);
					}
				}
			}
		}

		for(MonthlyCalendarRowView rowView : lstCalendarRow){
			rowView.refreshLayout();
		}

		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			dayView.showSchedules();
		}

		List<Todo> todos = CCJsonUtil.convertToModelList(response.optString("todoList"), Todo.class);
		buildTodoList(todos);
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
	protected int getHeaderBgColor(){
		return ContextCompat.getColor(activity, R.color.cl_cell_bg_color);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}
}

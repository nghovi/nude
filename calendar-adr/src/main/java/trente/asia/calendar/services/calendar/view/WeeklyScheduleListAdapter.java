package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkRequest;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 5/13/2016.
 */
public class WeeklyScheduleListAdapter extends ArrayAdapter<CalendarDayModel>{

	private List<CalendarDayModel>		calendarDayModels;
	private List<HolidayModel>			holidayModels			= new ArrayList<>();
	Context								context;
	int									layoutId;
	LayoutInflater						layoutInflater;
	private OnScheduleItemClickListener	onScheduleItemClickListener;

	private int							scheduleItemLayoutId	= R.layout.item_schedule;

	public interface OnScheduleItemClickListener{

		void onClickScheduleItem(ScheduleModel schedule, Date selectedDate);
	}

	public WeeklyScheduleListAdapter(Context context, int resource, List<CalendarDayModel> objects, List<HolidayModel> holidayModels, OnScheduleItemClickListener onScheduleItemClickListener){
		super(context, resource, objects);
		this.calendarDayModels = objects;
		this.holidayModels = holidayModels;
		this.context = context;
		this.layoutId = resource;
		layoutInflater = ((Activity)context).getLayoutInflater();
		this.onScheduleItemClickListener = onScheduleItemClickListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.txtDay = (TextView)convertView.findViewById(R.id.txt_item_calendar_day_date);
			viewHolder.lnrEventList = (LinearLayout)convertView.findViewById(R.id.lnr_item_calendar_item_list);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		CalendarDayModel calendarDay = getItem(position);
		viewHolder.txtDay.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendarDay.date));
		viewHolder.lnrEventList.removeAllViews();
		buildHolidays(viewHolder, calendarDay);
		buildScheduleList(viewHolder, calendarDay);
		buildWorkOffers(viewHolder, calendarDay);
//		buildBirthdays(viewHolder, calendarDay);

		return convertView;
	}

	private void buildWorkOffers(ViewHolder viewHolder, CalendarDayModel calendarDay){
		List<WorkRequest> workRequests = calendarDay.workRequests;
		if(!CCCollectionUtil.isEmpty(workRequests)){
			DailyScheduleList.sortOffersByType(workRequests);
			for(WorkRequest workRequest : workRequests){
				LinearLayout holidayItem = DailyScheduleList.buildOfferItem(context, layoutInflater, workRequest, R.layout.item_work_offer_weekly);
				viewHolder.lnrEventList.addView(holidayItem);
			}
		}
	}

//	private void buildBirthdays(ViewHolder viewHolder, CalendarDayModel calendarDay){
//		List<UserModel> birthdayUsers = calendarDay.birthdayUsers;
//		if(!CCCollectionUtil.isEmpty(birthdayUsers)){
//			DailyScheduleList.sortBirthdays(birthdayUsers);
//			for(UserModel user : birthdayUsers){
//				LinearLayout holidayItem = DailyScheduleList.buildBirthdayItem(context, layoutInflater, user, R.layout.item_birthday_weekly);
//				viewHolder.lnrEventList.addView(holidayItem);
//			}
//		}
//	}

	private void buildHolidays(ViewHolder viewHolder, CalendarDayModel calendarDay){
		List<HolidayModel> holidayModels = calendarDay.holidayModels;
		if(!CCCollectionUtil.isEmpty(holidayModels)){
			HolidayModel.sortHolidayModels(holidayModels);
			for(HolidayModel holidayModel : holidayModels){
				LinearLayout holidayItem = DailyScheduleList.buildHolidayItem(layoutInflater, holidayModel, 0, R.layout.item_holiday_weekly);
				viewHolder.lnrEventList.addView(holidayItem);
			}
		}
	}

	private void buildScheduleList(ViewHolder viewHolder, CalendarDayModel calendarDay){
		// sortSchedulesByType(calendarDay.schedules);
		for(final ScheduleModel schedule : calendarDay.schedules){
			if (ScheduleModel.EVENT_TYPE_SCHEDULE.equals(schedule.eventType)) {
				View lnrSchedulesContainer = buildWeeklyScheduleItem(getContext(), layoutInflater, schedule, onScheduleItemClickListener, calendarDay.date);
				viewHolder.lnrEventList.addView(lnrSchedulesContainer);
			} else if (ScheduleModel.EVENT_TYPE_BIRTHDAY.equals(schedule.eventType)) {
				LinearLayout birthdayItem = DailyScheduleList.buildBirthdayItem(context, layoutInflater, schedule.showUserModel, R.layout.item_birthday_weekly);
				viewHolder.lnrEventList.addView(birthdayItem);
//			}
			}
		}
	}

	private View buildWeeklyScheduleItem(final Context context, LayoutInflater layoutInflater, final ScheduleModel schedule, final OnScheduleItemClickListener onScheduleItemClickListener, final Date selectedDate){
		if(scheduleItemLayoutId == R.layout.item_schedule){
			return buildScheduleItem(context, layoutInflater, schedule, onScheduleItemClickListener, selectedDate);
		}
		return buildScheduleItemSummary(context, layoutInflater, schedule, onScheduleItemClickListener, selectedDate);
	}

	// private void sortSchedulesByType(List<ScheduleModel> schedules){
	// Collections.sort(schedules, new Comparator<ScheduleModel>() {
	//
	// @Override
	// public int compare(ScheduleModel o1, ScheduleModel o2){
	// Integer o1TypeOrder = getScheduleOrderByType(o1);
	// Integer o2TypeOrder = getScheduleOrderByType(o2);
	// if(o1TypeOrder != o2TypeOrder){
	// return o1TypeOrder.compareTo(o2TypeOrder);
	// }
	// return CCDateUtil.convertHour2Min(o1.startTime).compareTo(CCDateUtil.convertHour2Min(o2.startTime));
	// }
	// });
	// }

	// private int getScheduleOrderByType(ScheduleModel scheduleModel){
	// if(scheduleModel.isPeriodSchedule() && scheduleModel.isAllDay){
	// return 1;
	// }else if(scheduleModel.isAllDay){
	// return 2;
	// }
	// return 3;
	// }

	public View buildScheduleItemSummary(final Context context, LayoutInflater layoutInflater, final ScheduleModel schedule, OnScheduleItemClickListener onScheduleItemClickListener, Date selectedDate){
		LinearLayout lnrSchedulesContainer = (LinearLayout)layoutInflater.inflate(R.layout.item_schedule_summary, null);
		buildScheduleItemCommon(context, lnrSchedulesContainer, schedule, onScheduleItemClickListener, selectedDate);
		return lnrSchedulesContainer;
	}

	public static void buildScheduleItemCommon(Context context, LinearLayout lnrSchedulesContainer, final ScheduleModel schedule, final OnScheduleItemClickListener onScheduleItemClickListener, final Date selectedDate){
		TextView txtScheduleTime = (TextView)lnrSchedulesContainer.findViewById(R.id.txt_item_schedule_time);
		String time;
		if(CCBooleanUtil.checkBoolean(schedule.isAllDay)){
			time = context.getString(R.string.daily_page_all_day);
		}else{
			time = schedule.startTime + " - " + schedule.endTime;
		}
		txtScheduleTime.setText(time);

		TextView txtScheduleName = (TextView)lnrSchedulesContainer.findViewById(R.id.txt_item_schedule_name);
		txtScheduleName.setText(schedule.scheduleName);

		lnrSchedulesContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				onScheduleItemClickListener.onClickScheduleItem(schedule, selectedDate);
			}
		});
	}

	public static View buildScheduleItem(final Context context, LayoutInflater layoutInflater, final ScheduleModel schedule, final OnScheduleItemClickListener onScheduleItemClickListener, final Date selectedDate){
		LinearLayout lnrSchedulesContainer = (LinearLayout)layoutInflater.inflate(R.layout.item_schedule, null);
		buildScheduleItemCommon(context, lnrSchedulesContainer, schedule, onScheduleItemClickListener, selectedDate);
		lnrSchedulesContainer.setFocusable(true);
		return lnrSchedulesContainer;
	}

	private class ViewHolder{

		public TextView		txtDay;
		public LinearLayout	lnrEventList;
	}
}

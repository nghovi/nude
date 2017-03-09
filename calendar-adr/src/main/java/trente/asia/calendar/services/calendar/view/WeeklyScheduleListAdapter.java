package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 5/13/2016.
 */
public class WeeklyScheduleListAdapter extends ArrayAdapter<CalendarDayModel>{

	private List<CalendarDayModel>		calendarDayModels;
	private List<HolidayModel>			holidayModels	= new ArrayList<>();
	Context								context;
	int									layoutId;
	LayoutInflater						layoutInflater;
	private OnScheduleItemClickListener	onScheduleItemClickListener;

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
		viewHolder.txtDay.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, calendarDay.date));
		buildScheduleList(viewHolder, calendarDay);
		buildHolidays(viewHolder, calendarDay);
		return convertView;
	}

	private void buildHolidays(ViewHolder viewHolder, CalendarDayModel calendarDay){
		List<HolidayModel> holidayModels = calendarDay.holidayModels;
		if(!CCCollectionUtil.isEmpty(holidayModels)){
			for(HolidayModel holidayModel : holidayModels){
				LinearLayout holidayItem = DailyScheduleList.buildHolidayItem(layoutInflater, holidayModel, 0);
				viewHolder.lnrEventList.addView(holidayItem);
			}
		}
	}

	private void buildScheduleList(ViewHolder viewHolder, CalendarDayModel calendarDay){
		viewHolder.lnrEventList.removeAllViews();
		for(final ScheduleModel schedule : calendarDay.schedules){
			View lnrSchedulesContainer = buildScheduleItem(getContext(), layoutInflater, schedule, onScheduleItemClickListener, calendarDay.date);
			viewHolder.lnrEventList.addView(lnrSchedulesContainer);
		}
	}

	public static View buildScheduleItem(final Context context, LayoutInflater layoutInflater, final ScheduleModel schedule, final OnScheduleItemClickListener onScheduleItemClickListener, final Date selectedDate){
		LinearLayout lnrSchedulesContainer = (LinearLayout)layoutInflater.inflate(R.layout.item_schedule, null);
		TextView txtScheduleTime = (TextView)lnrSchedulesContainer.findViewById(R.id.txt_item_schedule_time);
		String time;
		if(CCBooleanUtil.checkBoolean(schedule.isAllDay)){
			time = context.getString(R.string.daily_page_all_day);
		}else{
			time = schedule.startTime + " - " + schedule.endTime;
		}
		txtScheduleTime.setText(time);

		TextView txtScheduleName = (TextView)lnrSchedulesContainer.findViewById(R.id.txt_item_schedule_name);
		if(!CCStringUtil.isEmpty(schedule.categoryId)){
			int categoryColor = Color.parseColor(WelfareFormatUtil.formatColor(schedule.categoryModel.categoryColor));
			txtScheduleName.setTextColor(categoryColor);
			txtScheduleTime.setTextColor(categoryColor);
		}
		txtScheduleName.setText(schedule.scheduleName);

		SelectableRoundedImageView imgCalendar = (SelectableRoundedImageView)lnrSchedulesContainer.findViewById(R.id.img_item_schedule_calendar);
		WfPicassoHelper.loadImage(context, BuildConfig.HOST + schedule.calendar.imagePath, imgCalendar, null);

		ImageView imgDup = (ImageView)lnrSchedulesContainer.findViewById(R.id.img_item_schedule_dup);
		if(CCBooleanUtil.checkBoolean(schedule.isWarning)){
			imgDup.setVisibility(View.VISIBLE);
		}else{
			imgDup.setVisibility(View.GONE);
		}

		ImageView imgRepeat = (ImageView)lnrSchedulesContainer.findViewById(R.id.img_item_schedule_repeat);
		if(!CCStringUtil.isEmpty(schedule.repeatType) && !ClConst.SCHEDULE_REPEAT_TYPE_NONE.equals(schedule.repeatType)){
			imgRepeat.setVisibility(View.VISIBLE);
		}else{
			imgRepeat.setVisibility(View.GONE);
		}

		// final List<UserModel> joinedUser = ClUtil.getJoinedUserModels(schedule, schedule.calendar.calendarUsers);
		ImageView imgOwner = (ImageView)lnrSchedulesContainer.findViewById(R.id.img_id_owner);
		if(!CCStringUtil.isEmpty(schedule.owner.avatarPath)){
			WfPicassoHelper.loadImage(context, BuildConfig.HOST + schedule.owner.avatarPath, imgOwner, null);
		}
		final UserListLinearLayout userListLinearLayout = (UserListLinearLayout)lnrSchedulesContainer.findViewById(R.id.lnr_id_container_join_user_list);
		userListLinearLayout.setGravityLeft(true);

		final List<UserModel> joinedUserList = schedule.scheduleJoinUsers;
		userListLinearLayout.post(new Runnable() {

			@Override
			public void run(){
				userListLinearLayout.show(joinedUserList, (int)context.getResources().getDimension(R.dimen.margin_24dp));
			}
		});

		lnrSchedulesContainer.setFocusable(true);
		lnrSchedulesContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				onScheduleItemClickListener.onClickScheduleItem(schedule, selectedDate);
			}
		});

		return lnrSchedulesContainer;
	}

	private class ViewHolder{

		public TextView		txtDay;
		public LinearLayout	lnrEventList;
	}

	public Integer findPosition4Code(String date){
		int position = -1;
		for(int i = 0; i < this.calendarDayModels.size(); i++){
			CalendarDayModel calendarDayModel = calendarDayModels.get(i);
			if(calendarDayModel.date.equals(date)){
				position = i;
				break;
			}
		}
		return position;
	}
}

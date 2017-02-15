package trente.asia.calendar.services.calendar.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.ScheduleDetailFragment;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 5/13/2016.
 */
public class CalendarDayListAdapter extends ArrayAdapter<CalendarDayModel>{

	Context							context;
	int								layoutId;
	LayoutInflater					layoutInflater;
	private OnScheduleClickListener	onScheduleClickListener;

	public interface OnScheduleClickListener{

		public void onClick(ScheduleModel schedule);
	}

	public CalendarDayListAdapter(Context context, int resource, List<CalendarDayModel> objects, OnScheduleClickListener onScheduleClickListener){
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		layoutInflater = ((Activity)context).getLayoutInflater();
		this.onScheduleClickListener = onScheduleClickListener;
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
		viewHolder.txtDay.setText(calendarDay.date);
		buildEventList(viewHolder, calendarDay);
		return convertView;
	}

	private void buildEventList(ViewHolder viewHolder, CalendarDayModel calendarDay){
		viewHolder.lnrEventList.removeAllViews();
		for(final ScheduleModel schedule : calendarDay.schedules){
			View calendarEvents = layoutInflater.inflate(R.layout.item_schedule, null);

			TextView txtScheduleName = (TextView)calendarEvents.findViewById(R.id.txt_item_schedule_name);
			txtScheduleName.setText(schedule.scheduleName);

			TextView txtScheduleCategory = (TextView)calendarEvents.findViewById(R.id.txt_item_schedule_category);
			if(!CCStringUtil.isEmpty(schedule.categoryId)){
				txtScheduleCategory.setTextColor(Color.parseColor("#" + schedule.categoryId));
			}
			txtScheduleCategory.setText(schedule.categoryName);

			TextView txtScheduleTime = (TextView)calendarEvents.findViewById(R.id.txt_item_schedule_time);
			txtScheduleTime.setText(schedule.startTime + " - " + schedule.endTime);

			SelectableRoundedImageView imgCalendar = (SelectableRoundedImageView)calendarEvents.findViewById(R.id.img_item_schedule_calendar);
			WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + schedule.calendar.calendarImagePath, imgCalendar, null);

			SelectableRoundedImageView imgDup = (SelectableRoundedImageView)calendarEvents.findViewById(R.id.img_item_schedule_calendar);
			// WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST +
			// schedule.calendar.calendarImagePath, imgDup, null);

			SelectableRoundedImageView imgType = (SelectableRoundedImageView)calendarEvents.findViewById(R.id.img_item_schedule_calendar);
			// WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST +
			// schedule.calendar.calendarImagePath, imgType, null);

			final HorizontalUserListView horizontalUserListView = (HorizontalUserListView)calendarEvents.findViewById(R.id.view_horizontal_user_list);
			final List<UserModel> joinedUser = ScheduleDetailFragment.getJoinedUserModels(schedule, schedule.calendar.calendarUsers);
			horizontalUserListView.post(new Runnable() {

				@Override
				public void run(){
					horizontalUserListView.inflateWith(joinedUser, joinedUser, true, 32, 10);
				}
			});

			calendarEvents.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					onScheduleClickListener.onClick(schedule);
				}
			});
			viewHolder.lnrEventList.addView(calendarEvents);
		}
	}

	private class ViewHolder{

		public TextView		txtDay;
		public LinearLayout	lnrEventList;
	}
}

package trente.asia.calendar.services.calendar.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDay;

/**
 * Created by viet on 5/13/2016.
 */
public class CalendarDayListAdapter extends ArrayAdapter<CalendarDay>{

	Context			context;
	int				layoutId;
	LayoutInflater	layoutInflater;

	public CalendarDayListAdapter(Context context, int resource, List<CalendarDay> objects){
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		layoutInflater = ((Activity)context).getLayoutInflater();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_item_calendar_day_date);
			viewHolder.lnrEventList = (LinearLayout)convertView.findViewById(R.id.lnr_item_calendar_item_list);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		CalendarDay calendarDay = getItem(position);
		viewHolder.txtDate.setText(calendarDay.date);
		buildEventList(viewHolder, calendarDay);
		return convertView;
	}

	private void buildEventList(ViewHolder viewHolder, CalendarDay calendarDay){
		viewHolder.lnrEventList.removeAllViews();
		for(CalendarDay.Event event : calendarDay.events){
			View calendarEvents = layoutInflater.inflate(R.layout.item_calendar_event, null);

			TextView txtContent = (TextView)calendarEvents.findViewById(R.id.txt_item_calendar_event_content);
			txtContent.setText("Content");

			TextView txtStartTime = (TextView)calendarEvents.findViewById(R.id.txt_item_calendar_event_start_time);
			txtStartTime.setText(event.startTime);

			TextView txtEndTime = (TextView)calendarEvents.findViewById(R.id.txt_item_calendar_event_end_time);
			txtEndTime.setText(event.endTime);

			viewHolder.lnrEventList.addView(calendarEvents);
		}
	}

	private class ViewHolder{

		public TextView		txtDate;
		public LinearLayout	lnrEventList;
	}
}

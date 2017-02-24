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
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 5/13/2016.
 */
public class CalendarDayListAdapter extends ArrayAdapter<CalendarDayModel> {

    private final List<CalendarDayModel> calendarDayModels;
    Context context;
    int layoutId;
    LayoutInflater layoutInflater;
    private OnScheduleClickListener onScheduleClickListener;

    public interface OnScheduleClickListener {

        public void onClick(ScheduleModel schedule);
    }

    public CalendarDayListAdapter(Context context, int resource,
                                  List<CalendarDayModel> objects,
                                  OnScheduleClickListener
                                          onScheduleClickListener) {
        super(context, resource, objects);
        this.calendarDayModels = objects;
        this.context = context;
        this.layoutId = resource;
        layoutInflater = ((Activity) context).getLayoutInflater();
        this.onScheduleClickListener = onScheduleClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup
            parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(layoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.txtDay = (TextView) convertView.findViewById(R.id
                    .txt_item_calendar_day_date);
            viewHolder.lnrEventList = (LinearLayout) convertView.findViewById
                    (R.id.lnr_item_calendar_item_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CalendarDayModel calendarDay = getItem(position);
        viewHolder.txtDay.setText(calendarDay.date);
        buildEventList(viewHolder, calendarDay);
        return convertView;
    }

    private void buildEventList(ViewHolder viewHolder, CalendarDayModel
            calendarDay) {
        viewHolder.lnrEventList.removeAllViews();
        for (final ScheduleModel schedule : calendarDay.schedules) {
            View lnrSchedulesContainer = layoutInflater.inflate(R.layout
                    .item_schedule, null);

            TextView txtScheduleName = (TextView) lnrSchedulesContainer
                    .findViewById(R.id.txt_item_schedule_name);
            txtScheduleName.setText(schedule.scheduleName);

            TextView txtScheduleCategory = (TextView) lnrSchedulesContainer
                    .findViewById(R.id.txt_item_schedule_category);
            if (!CCStringUtil.isEmpty(schedule.categoryId)) {
                txtScheduleCategory.setTextColor(Color.parseColor("#" +
                        schedule.categoryId));
            }
            txtScheduleCategory.setText(schedule.categoryName);

            TextView txtScheduleTime = (TextView) lnrSchedulesContainer
                    .findViewById(R.id.txt_item_schedule_time);
            txtScheduleTime.setText(schedule.startTime + " - " + schedule
                    .endTime);

            SelectableRoundedImageView imgCalendar =
                    (SelectableRoundedImageView) lnrSchedulesContainer
                            .findViewById(R.id.img_item_schedule_calendar);
            WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST +
                    schedule.calendar.imagePath, imgCalendar, null);

            SelectableRoundedImageView imgDup = (SelectableRoundedImageView)
                    lnrSchedulesContainer.findViewById(R.id
                            .img_item_schedule_calendar);
            // WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST +
            // schedule.calendar.imagePath, imgDup, null);

            SelectableRoundedImageView imgType = (SelectableRoundedImageView)
                    lnrSchedulesContainer.findViewById(R.id
                            .img_item_schedule_calendar);
            // WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST +
            // schedule.calendar.imagePath, imgType, null);

//			final HorizontalUserListView horizontalUserListView =
// (HorizontalUserListView)calendarEvents.findViewById(R.id
// .view_horizontal_user_list);
            final List<UserModel> joinedUser = ClUtil.getJoinedUserModels
                    (schedule, schedule.calendar.calendarUsers);
//			horizontalUserListView.post(new Runnable() {
//
//				@Override
//				public void run(){
//					horizontalUserListView.show(joinedUser, joinedUser, true,
// 32, 10);
//				}
//			});

            UserListLinearLayout userListLinearLayout =
                    (UserListLinearLayout) lnrSchedulesContainer.findViewById
                            (R.id.item_schedule_user_list_linear_layout);
            userListLinearLayout.show(joinedUser, (int) context.getResources
                    ().getDimension(R.dimen.margin_30dp));


            lnrSchedulesContainer.setFocusable(true);
            lnrSchedulesContainer.setOnClickListener(new View.OnClickListener
                    () {

                @Override
                public void onClick(View v) {
                    onScheduleClickListener.onClick(schedule);
                }
            });
            viewHolder.lnrEventList.addView(lnrSchedulesContainer);
        }
    }

    private class ViewHolder {

        public TextView txtDay;
        public LinearLayout lnrEventList;
    }

    public Integer findPosition4Code(String date) {
        int position = -1;
        for (int i = 0; i < this.calendarDayModels.size(); i++) {
            CalendarDayModel calendarDayModel = calendarDayModels.get(i);
            if (calendarDayModel.date.equals(date)) {
                position = i;
                break;
            }
        }
        return position;
    }
}

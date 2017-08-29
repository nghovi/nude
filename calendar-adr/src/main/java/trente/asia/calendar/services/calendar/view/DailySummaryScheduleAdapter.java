package trente.asia.calendar.services.calendar.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * DailySummaryScheduleAdapter.
 *
 * @author TrungND
 */
public class DailySummaryScheduleAdapter extends ArrayAdapter<ScheduleModel>{

	private List<ScheduleModel>	lstSchedule;
	private Context				mContext;

	public class DailySummaryScheduleViewHolder{

		public ImageView			imgCalendar;
		public TextView				txtScheduleTime;
		public TextView				txtScheduleName;
		public UserListLinearLayout	lnrUserList;

		public DailySummaryScheduleViewHolder(View view){
			imgCalendar = (ImageView)view.findViewById(R.id.img_id_calendar);
			txtScheduleTime = (TextView)view.findViewById(R.id.txt_id_schedule_time);
			txtScheduleName = (TextView)view.findViewById(R.id.txt_id_schedule_name);
			lnrUserList = (UserListLinearLayout)view.findViewById(R.id.lnr_id_user_list);
		}
	}

	public DailySummaryScheduleAdapter(Context context, List<ScheduleModel> lstSchedule){
		super(context, R.layout.view_daily_summary_item, lstSchedule);
		this.mContext = context;
		this.lstSchedule = lstSchedule;
	}

	public View getView(final int position, View convertView, final ViewGroup parent){

		final ScheduleModel schedule = this.lstSchedule.get(position);
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.view_daily_summary_item, null);

		DailySummaryScheduleViewHolder holder = new DailySummaryScheduleViewHolder(convertView);

		holder.txtScheduleName.setText(schedule.scheduleName);
		holder.txtScheduleTime.setText(WelfareFormatUtil.connect2String(schedule.startTime, schedule.endTime, "-"));

		// if(!CCStringUtil.isEmpty(model.calendar.imagePath)){
		// WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.calendar.imagePath, holder.imgCalendar, null);
		// }

		return convertView;
	}
}

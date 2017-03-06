package trente.asia.calendar.commons.dialogs;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;

import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;

/**
 * ClDailySummaryDialog
 *
 * @author TrungND
 */
public class ClDailySummaryDialog extends ChiaseDialog{

	private DailyScheduleList	dailyScheduleListView;
	private Context				mContext;

	public ClDailySummaryDialog(Context context, LayoutInflater inflater, CalendarDayListAdapter.OnScheduleItemClickListener listener){
		super(context);

		this.setContentView(R.layout.dialog_common_daily_summary);
		this.mContext = context;
		dailyScheduleListView = (DailyScheduleList)findViewById(R.id.lnr_view_daily_schedules);
		dailyScheduleListView.init(inflater, listener);

	}

	public void show(Date selectedDate, List<ScheduleModel> scheduleModels){
		dailyScheduleListView.updateFor(selectedDate, scheduleModels);
		super.show();
	}
}

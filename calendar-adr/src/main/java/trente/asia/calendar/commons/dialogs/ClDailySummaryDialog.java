package trente.asia.calendar.commons.dialogs;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.welfare.adr.models.UserModel;

/**
 * ClDailySummaryDialog
 *
 * @author TrungND
 */
public class ClDailySummaryDialog extends ChiaseDialog{

	private DailyScheduleList	dailyScheduleListView;
	private Context				mContext;
	private ImageView			imgAdd;
	private Date				selectedDate;

	public ClDailySummaryDialog(Context context, LayoutInflater inflater, final CalendarDayListAdapter.OnScheduleItemClickListener listener, final NavigationHeader.OnAddBtnClickedListener onAddBtnClickedListener){
		super(context);

		this.setContentView(R.layout.dialog_common_daily_summary);
		this.mContext = context;
		imgAdd = (ImageView)findViewById(R.id.img_id_add);
		imgAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(onAddBtnClickedListener != null){
					dismiss();
					onAddBtnClickedListener.onAddBtnClick(selectedDate);
				}
			}
		});
		dailyScheduleListView = (DailyScheduleList)findViewById(R.id.lnr_view_daily_schedules);
		dailyScheduleListView.init(inflater, new CalendarDayListAdapter.OnScheduleItemClickListener() {

			@Override
			public void onClickScheduleItem(ScheduleModel schedule){
				dismiss();
				listener.onClickScheduleItem(schedule);
			}
		});
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
	}

	public void show(final Date selectedDate, final List<ScheduleModel> scheduleModels, final List<UserModel> birthdayUsers){
		this.selectedDate = selectedDate;
		dailyScheduleListView.updateFor(selectedDate, scheduleModels, null, null, birthdayUsers);
		super.show();
	}
}

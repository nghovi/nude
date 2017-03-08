package trente.asia.calendar.commons.dialogs;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.welfare.adr.define.WelfareConst;
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
	private TextView			txtHeader;

	public ClDailySummaryDialog(Context context, LayoutInflater inflater, final CalendarDayListAdapter.OnScheduleItemClickListener listener, final NavigationHeader.OnAddBtnClickedListener onAddBtnClickedListener){
		super(context);

		this.setContentView(R.layout.dialog_common_daily_summary);
		this.mContext = context;
		txtHeader = (TextView)findViewById(R.id.txt_dialog_common_daily_summary);
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
			public void onClickScheduleItem(ScheduleModel schedule, Date selectedDate){
				dismiss();
				listener.onClickScheduleItem(schedule, selectedDate);
			}
		});
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		this.setCanceledOnTouchOutside(true);
	}

	public void show(final Date selectedDate, final List<ScheduleModel> scheduleModels, final List<UserModel> birthdayUsers, List<HolidayModel> holidayModels, List<WorkOffer> workOffers){
		this.selectedDate = selectedDate;
		txtHeader.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_10, this.selectedDate));
		dailyScheduleListView.updateFor(selectedDate, scheduleModels, holidayModels, workOffers, birthdayUsers);
		super.show();
	}
}

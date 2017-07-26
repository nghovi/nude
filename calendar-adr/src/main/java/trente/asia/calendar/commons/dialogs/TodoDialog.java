package trente.asia.calendar.commons.dialogs;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.views.NavigationHeader;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.DailySummaryDialogPagerAdapter;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.models.UserModel;

/**
 * ClDailySummaryDialog
 *
 * @author TrungND
 */
public class TodoDialog extends CLOutboundDismissDialog{

	private final Context			mContext;
	private Todo					todo;
	private View.OnClickListener	onDelete;
	private View.OnClickListener	onFinish;

	private Date					selectedDate;

	public TodoDialog(Context context, Todo todo, View.OnClickListener onDelete, View.OnClickListener onFinish){
		super(context);

		this.setContentView(R.layout.dialog_todo);
		this.mContext = context;
		this.todo = todo;
		this.onDelete = onDelete;
		this.onFinish = onFinish;

		((TextView)findViewById(R.id.txt_item_todo_title)).setText(todo.name);
		((TextView)findViewById(R.id.txt_dlg_todo_content)).setText(todo.note);
		findViewById(R.id.radio).setOnClickListener(this.onFinish);
		findViewById(R.id.btn_todo_dialog_delete).setOnClickListener(this.onDelete);

		Window window = this.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
	}

	// public void setData(List<ScheduleModel> lstSchedule, List<UserModel> lstBirthdayUser, List<HolidayModel> lstHoliday, List<WorkOffer>
	// lstWorkOffer){
	// this.lstSchedule = lstSchedule;
	// this.lstBirthdayUser = lstBirthdayUser;
	// this.lstHoliday = lstHoliday;
	// this.lstWorkOffer = lstWorkOffer;
	// mPagerAdapter = new DailySummaryDialogPagerAdapter(this, mContext, dates);
	// mPagerAdapter.setData(this.lstSchedule, this.lstBirthdayUser, this.lstHoliday, this.lstWorkOffer, this.onAddBtnClickedListener, this.listener,
	// this);
	// mViewPager.setAdapter(mPagerAdapter);
	// if (selectedDate != null) {
	// int currentItemPosition = mPagerAdapter.getPositionByDate(selectedDate);
	// mViewPager.setCurrentItem(currentItemPosition, false);
	// }
	// }
	//
	// public void show(Date selectedDate){
	// this.selectedDate = selectedDate;
	// int currentItemPosition = mPagerAdapter.getPositionByDate(selectedDate);
	// mViewPager.setCurrentItem(currentItemPosition, false);
	// super.show();
	// }
	//
	// public void notifyDataUpdated(List<ScheduleModel> lstSchedule, List<UserModel> lstBirthdayUser, List<HolidayModel> lstHoliday, List<WorkOffer>
	// lstWorkOffer){
	// this.lstSchedule = lstSchedule;
	// this.lstBirthdayUser = lstBirthdayUser;
	// this.lstHoliday = lstHoliday;
	// this.lstWorkOffer = lstWorkOffer;
	// mPagerAdapter.notifyDataSetChanged();
	// }
}

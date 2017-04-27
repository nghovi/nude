package trente.asia.calendar.commons.dialogs;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.views.NavigationHeader;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.DailySummaryDialogPagerAdapter;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;
import trente.asia.welfare.adr.models.UserModel;

/**
 * ClDailySummaryDialog
 *
 * @author TrungND
 */
public class DailySummaryDialog extends CLOutboundDismissDialog{

	private final Context												mContext;
	private final NavigationHeader.OnAddBtnClickedListener				onAddBtnClickedListener;
	private final WeeklyScheduleListAdapter.OnScheduleItemClickListener	listener;

	private final List<Date>											dates;
	private DailySummaryDialogPagerAdapter								mPagerAdapter;
	protected ViewPager													mViewPager;

	private List<ScheduleModel>											lstSchedule;
	private List<UserModel>												lstBirthdayUser;
	private List<HolidayModel>											lstHoliday;
	private List<WorkOffer>												lstWorkOffer;

	public DailySummaryDialog(Context context, final WeeklyScheduleListAdapter.OnScheduleItemClickListener listener, final NavigationHeader.OnAddBtnClickedListener onAddBtnClickedListener, List<Date> dates){
		super(context);

		this.setContentView(R.layout.dialog_daily_summary);
		this.mContext = context;
		this.dates = dates;

		mViewPager = (ViewPager)findViewById(R.id.viewpager_dialog_daily_summary);
		this.listener = listener;
		this.onAddBtnClickedListener = onAddBtnClickedListener;

		Window window = this.getWindow();
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
	}

	public void setData(List<ScheduleModel> lstSchedule, List<UserModel> lstBirthdayUser, List<HolidayModel> lstHoliday, List<WorkOffer> lstWorkOffer){
		this.lstSchedule = lstSchedule;
		this.lstBirthdayUser = lstBirthdayUser;
		this.lstHoliday = lstHoliday;
		this.lstWorkOffer = lstWorkOffer;
		mPagerAdapter = new DailySummaryDialogPagerAdapter(this, mContext, dates);
		mPagerAdapter.setData(this.lstSchedule, this.lstBirthdayUser, this.lstHoliday, this.lstWorkOffer, this.onAddBtnClickedListener, this.listener, this);
		mViewPager.setAdapter(mPagerAdapter);
	}

	public void show(Date selectedDate){
		int currentItemPosition = mPagerAdapter.getPositionByDate(selectedDate);
		mViewPager.setCurrentItem(currentItemPosition);
		super.show();
	}

	public void notifyDataUpdated(List<ScheduleModel> lstSchedule, List<UserModel> lstBirthdayUser, List<HolidayModel> lstHoliday, List<WorkOffer> lstWorkOffer){
		this.lstSchedule = lstSchedule;
		this.lstBirthdayUser = lstBirthdayUser;
		this.lstHoliday = lstHoliday;
		this.lstWorkOffer = lstWorkOffer;
		mPagerAdapter.notifyDataSetChanged();
	}
}

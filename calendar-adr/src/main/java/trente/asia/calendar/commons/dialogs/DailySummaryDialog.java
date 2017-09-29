package trente.asia.calendar.commons.dialogs;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.WeeklyPageFragment;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkRequest;
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
	private final WeeklyScheduleListAdapter.OnScheduleItemClickListener	listener;

	private final List<Date>											dates;
	private DailySummaryDialogPagerAdapter								mPagerAdapter;
	protected ViewPager													mViewPager;

	private List<ScheduleModel>											lstSchedule;
	private List<UserModel>												lstBirthdayUser;
	private List<HolidayModel>											lstHoliday;
	private List<WorkRequest> lstWorkRequest;

	private Date														selectedDate;
	private OnAddBtnClickedListener										onAddBtnClickedListener;

	public interface OnAddBtnClickedListener{

		public void onAddBtnClick(Date selectedDate);
	}

	public DailySummaryDialog(Context context, final WeeklyScheduleListAdapter.OnScheduleItemClickListener listener, final OnAddBtnClickedListener onAddBtnClickedListener, List<Date> dates){
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

	public void setData(List<ScheduleModel> lstSchedule, List<UserModel> lstBirthdayUser, List<HolidayModel> lstHoliday, List<WorkRequest> lstWorkRequest){
		this.lstSchedule = lstSchedule;
		this.lstBirthdayUser = lstBirthdayUser;
		this.lstHoliday = lstHoliday;
		this.lstWorkRequest = lstWorkRequest;

		WeeklyPageFragment.sortSchedules(lstSchedule, dates.get(0), dates.get(dates.size() - 1), false);
		Collections.sort(lstWorkRequest, new Comparator<WorkRequest>() {

			@Override
			public int compare(WorkRequest o1, WorkRequest o2){
				return o1.offerTypeName.compareTo(o2.offerTypeName);
			}
		});

		mPagerAdapter = new DailySummaryDialogPagerAdapter(this, mContext, dates);
		mPagerAdapter.setData(this.lstSchedule, this.lstBirthdayUser, this.lstHoliday, this.lstWorkRequest, this.onAddBtnClickedListener, this.listener, this);
		mViewPager.setAdapter(mPagerAdapter);
		if(selectedDate != null){
			int currentItemPosition = mPagerAdapter.getPositionByDate(selectedDate);
			mViewPager.setCurrentItem(currentItemPosition, false);
		}
	}

	public void show(Date selectedDate){
		this.selectedDate = selectedDate;
		int currentItemPosition = mPagerAdapter.getPositionByDate(selectedDate);
		mViewPager.setCurrentItem(currentItemPosition, false);
		super.show();
	}

	public void notifyDataUpdated(List<ScheduleModel> lstSchedule, List<UserModel> lstBirthdayUser, List<HolidayModel> lstHoliday, List<WorkRequest> lstWorkRequest){
		this.lstSchedule = lstSchedule;
		this.lstBirthdayUser = lstBirthdayUser;
		this.lstHoliday = lstHoliday;
		this.lstWorkRequest = lstWorkRequest;
		mPagerAdapter.notifyDataSetChanged();
	}
}

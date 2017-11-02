package nguyenhoangviet.vpcorp.calendar.commons.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import asia.chiase.core.util.CCFormatUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.services.calendar.SchedulesPageFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.CalendarBirthdayModel;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.HolidayModel;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.ScheduleModel;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.WorkRequest;
import nguyenhoangviet.vpcorp.calendar.services.calendar.view.DailyScheduleList;
import nguyenhoangviet.vpcorp.calendar.services.calendar.view.DailySummaryDialogPagerAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

/**
 * DailySummaryDialog
 *
 * @author TrungND
 */
public class DailySummaryDialog extends CLOutboundDismissDialog{

	private final Context												mContext;
	private final DailyScheduleList.OnScheduleItemClickListener listener;

	private final List<Date>											dates;
	private DailySummaryDialogPagerAdapter								mPagerAdapter;
	protected ViewPager													mViewPager;

	private List<ScheduleModel>											lstSchedule;
	private List<CalendarBirthdayModel>									calendarBirthdayModels	= new ArrayList<>();
	private List<HolidayModel>											lstHoliday				= new ArrayList<>();
	private Date														selectedDate;
	private OnAddBtnClickedListener										onAddBtnClickedListener;

	public interface OnAddBtnClickedListener{

		public void onAddBtnClick(Date selectedDate);
	}

	public DailySummaryDialog(Context context, final DailyScheduleList.OnScheduleItemClickListener listener, final OnAddBtnClickedListener onAddBtnClickedListener, List<Date> dates){
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

	public void setData(List<ScheduleModel> lstSchedule, List<CalendarBirthdayModel> calendarBirthdays, List<HolidayModel> lstHoliday, String screenMode){
		this.lstSchedule = ignoreDuplicatedSchedule(lstSchedule);
		if(!SchedulesPageFragment.SCREEN_MODE_MONTH.equals(screenMode)){
			this.calendarBirthdayModels = calendarBirthdays;
			this.lstHoliday = lstHoliday;
		}
		mPagerAdapter = new DailySummaryDialogPagerAdapter(this, mContext, dates);
		mPagerAdapter.setData(this.lstSchedule, this.onAddBtnClickedListener, this.listener, this);
		mViewPager.setAdapter(mPagerAdapter);
		if(selectedDate != null){
			int currentItemPosition = mPagerAdapter.getPositionByDate(selectedDate);
			mViewPager.setCurrentItem(currentItemPosition, false);
		}
	}

	private List<ScheduleModel> ignoreDuplicatedSchedule(List<ScheduleModel> scheduleModels){
		List<ScheduleModel> result = new ArrayList<>();
		List<String> scheduleKeys = new ArrayList<>();
		for(ScheduleModel scheduleModel : scheduleModels){
			if(ScheduleModel.EVENT_TYPE_SCHEDULE.equals(scheduleModel.eventType)){
				String code = scheduleModel.key + CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, scheduleModel.startDate);
				if(scheduleKeys.contains(code)){
					continue;
				}else{
					scheduleKeys.add(code);
				}
			}
			result.add(scheduleModel);
		}
		return result;
	}

	public void show(Date selectedDate){
		this.selectedDate = selectedDate;
		int currentItemPosition = mPagerAdapter.getPositionByDate(selectedDate);
		mViewPager.setCurrentItem(currentItemPosition, false);
		super.show();
	}
}

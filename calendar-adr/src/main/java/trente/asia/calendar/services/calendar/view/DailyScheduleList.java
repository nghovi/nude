package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 3/6/2017.
 */

public class DailyScheduleList extends LinearLayout{

	private static final String									TIME_NOON				= "12:00";
	private static final Integer								SCHEDULES_ALL_DAY		= 1;
	private static final Integer								SCHEDULES_MORNING		= 2;
	private static final Integer								SCHEDULES_AFTERNOON		= 3;
	private static final int									MARGIN_LEFT_RIGHT		= WelfareUtil.dpToPx(16);
	private static final int									MARGIN_TEXT_TOP_BOTTM	= WelfareUtil.dpToPx(4);
	private LinearLayout										lnrOffers;

	private Map<Integer, List<ScheduleModel>>					schedulesMap;
	private LayoutInflater										inflater;
	private List<ScheduleModel>									lstSchedule;
	private Date												selectedDate;
	private CalendarDayListAdapter.OnScheduleItemClickListener	onScheduleItemClickListener;

	private LinearLayout										lnrHolidays;
	private LinearLayout										lnrBirthdays;

	public DailyScheduleList(Context context){
		super(context);
	}

	public DailyScheduleList(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void init(LayoutInflater inflater, CalendarDayListAdapter.OnScheduleItemClickListener onScheduleItemClickListener){
		this.inflater = inflater;
		this.onScheduleItemClickListener = onScheduleItemClickListener;
		lnrHolidays = (LinearLayout)findViewById(R.id.lnr_daily_schedules_list_holiday);
		lnrOffers = (LinearLayout)findViewById(R.id.lnr_daily_schedules_list_work_offer);
		lnrBirthdays = (LinearLayout)findViewById(R.id.lnr_daily_schedules_list_birthday);

	}

	public void updateFor(Date selectedDate, List<ScheduleModel> lstSchedule, List<HolidayModel> holidayModels, List<WorkOffer> offers, List<UserModel> userModels){
		this.lstSchedule = lstSchedule;
		this.selectedDate = selectedDate;
		schedulesMap = getDisplayedSchedulesMaps();
		buildTimelySchedules(R.id.lnr_daily_schedule_list_all_day, R.string.daily_page_all_day, schedulesMap.get(SCHEDULES_ALL_DAY));
		buildTimelySchedules(R.id.lnr_daily_schedule_list_morning, R.string.daily_page_morning, schedulesMap.get(SCHEDULES_MORNING));
		buildTimelySchedules(R.id.lnr_daily_schedule_list_afternoon, R.string.daily_page_afternoon, schedulesMap.get(SCHEDULES_AFTERNOON));
		buildHolidays(holidayModels);
		buildOffers(offers);
		buildBirthdays(userModels);
	}

	private void buildHolidays(List<HolidayModel> holidayModels){
		lnrHolidays.removeAllViews();
		// // TODO: 2/28/2017
	}

	private void buildOffers(List<WorkOffer> offers){
		lnrOffers.removeAllViews();
		// // TODO: 2/28/2017
	}

	private void buildBirthdays(List<UserModel> userModels){
		lnrBirthdays.removeAllViews();
		if(!CCCollectionUtil.isEmpty(userModels)){
			lnrBirthdays.setVisibility(View.VISIBLE);
			for(UserModel user : userModels){
				LinearLayout birthdayItem = buildBirthdayItem(user);
				lnrBirthdays.addView(birthdayItem);
			}
		}else{
			lnrBirthdays.setVisibility(View.GONE);
		}
		// // TODO: 2/28/2017
	}

	private LinearLayout buildBirthdayItem(UserModel userModel){
		LinearLayout birthdayItem = (LinearLayout)inflater.inflate(R.layout.item_birthday, null);
		SelectableRoundedImageView imgUser = (SelectableRoundedImageView)birthdayItem.findViewById(R.id.img_item_birthday_user);
		WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + userModel.avatarPath, imgUser, null);
		return birthdayItem;
	}

	private void buildTimelySchedules(int parentId, int titleId, List<ScheduleModel> scheduleModels){
		LinearLayout lnrParent = (LinearLayout)findViewById(parentId);
		lnrParent.removeAllViews();
		if(!CCCollectionUtil.isEmpty(scheduleModels)){
			lnrParent.setVisibility(View.VISIBLE);
			String title = getContext().getString(titleId);
			buildSchedulesList(lnrParent, title, scheduleModels);
		}else{
			lnrParent.setVisibility(View.GONE);
		}
	}

	private void buildSchedulesList(LinearLayout lnrParent, String title, List<ScheduleModel> scheduleModels){
		lnrParent.removeAllViews();
		TextView textView = new TextView(getContext());

		textView.setPadding(MARGIN_LEFT_RIGHT, MARGIN_TEXT_TOP_BOTTM, MARGIN_LEFT_RIGHT, MARGIN_TEXT_TOP_BOTTM);
		textView.setText(title);
		textView.setBackgroundColor(WelfareUtil.getColor(getContext(), R.color.cl_title_bg));
		lnrParent.addView(textView);

		for(ScheduleModel scheduleModel : scheduleModels){
			buildScheduleItem(lnrParent, scheduleModel);
		}
	}

	private void buildScheduleItem(LinearLayout lnrParent, ScheduleModel scheduleModel){
		LinearLayout item = (LinearLayout)CalendarDayListAdapter.buildScheduleItem(getContext(), this.inflater, scheduleModel, onScheduleItemClickListener);
		item.setPadding(MARGIN_LEFT_RIGHT, 0, MARGIN_LEFT_RIGHT, 0);
		lnrParent.addView(item);
	}

	private Map<Integer, List<ScheduleModel>> getDisplayedSchedulesMaps(){
		Map<Integer, List<ScheduleModel>> result = new HashMap<>();
		result.put(SCHEDULES_ALL_DAY, new ArrayList<ScheduleModel>());
		result.put(SCHEDULES_MORNING, new ArrayList<ScheduleModel>());
		result.put(SCHEDULES_AFTERNOON, new ArrayList<ScheduleModel>());
		for(ScheduleModel scheduleModel : lstSchedule){
			if(isScheduleOf(scheduleModel, selectedDate)){
				if(CCBooleanUtil.checkBoolean(scheduleModel.isAllDay)){
					result.get(SCHEDULES_ALL_DAY).add(scheduleModel);
				}else if(isBeforeNoon(scheduleModel.startTime)){
					result.get(SCHEDULES_MORNING).add(scheduleModel);
				}else{
					result.get(SCHEDULES_AFTERNOON).add(scheduleModel);
				}
			}
		}
		return result;
	}

	private boolean isBeforeNoon(String startTime){
		int startMinute = CCDateUtil.convertTime2Min(startTime);
		int noonMinute = CCDateUtil.convertTime2Min(TIME_NOON);
		return startMinute < noonMinute;
	}

	private boolean isScheduleOf(ScheduleModel scheduleModel, Date date){
		return scheduleModel.startDate.equals(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, date));
	}

}

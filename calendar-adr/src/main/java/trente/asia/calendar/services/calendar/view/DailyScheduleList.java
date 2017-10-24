package trente.asia.calendar.services.calendar.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.CalendarBirthdayModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkRequest;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 3/6/2017.
 */

public class DailyScheduleList extends LinearLayout{

	private static final int			MARGIN_LEFT_RIGHT		= WelfareUtil.dpToPx(16);
	private static final int			MARGIN_TEXT_TOP_BOTTOM	= WelfareUtil.dpToPx(4);
	private static final int			MARGIN_TOP_BOTTOM		= WelfareUtil.dpToPx(4);

	private List<ScheduleModel>			mSchedules;
	Map<Date, List<ScheduleModel>>		daySchedulesMap;
	private LayoutInflater				inflater;
	private Date						selectedDate;
	private OnScheduleItemClickListener	onScheduleItemClickListener;
	private LinearLayout				lnrEvents;
	public boolean						hasDisplayedItem		= false;

	public DailyScheduleList(Context context){
		super(context);
	}

	public DailyScheduleList(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void init(LayoutInflater inflater, OnScheduleItemClickListener onScheduleItemClickListener){
		this.inflater = inflater;
		this.onScheduleItemClickListener = onScheduleItemClickListener;
		lnrEvents = (LinearLayout)findViewById(R.id.lnr_daily_schedules_list_event);
	}

	public void showFor(Date selectedDate){
		this.selectedDate = CCDateUtil.makeDate(selectedDate);
		mSchedules = daySchedulesMap.get(this.selectedDate);
		lnrEvents.removeAllViews();
		buildTimelySchedules(R.id.lnr_daily_schedule_list_all_day, R.string.schedule, mSchedules);
	}

	public void initDataWithMap(Map<Date, List<ScheduleModel>> daySchedulesMap){
		this.daySchedulesMap = daySchedulesMap;
	}

	public static Map<Date, List<CalendarBirthdayModel>> buildDayBirthdayUserMap(List<Date> dates, List<CalendarBirthdayModel> userModels){
		Map<Date, List<CalendarBirthdayModel>> result = new HashMap<>();
		for(Date date : dates){
			Date dateOnly = CCDateUtil.makeDate(date);
			List<CalendarBirthdayModel> dayBirthdayUsers = getBirthdayUsersByDate(userModels, date);
			result.put(dateOnly, dayBirthdayUsers);
		}
		return result;

	}

	public static Map<Date, List<ScheduleModel>> buildDaySchedulesMap(List<Date> dates, List<ScheduleModel> lstSchedule){
		Map<Date, List<ScheduleModel>> result = new HashMap<>();
		for(Date date : dates){
			Date dateOnly = CCDateUtil.makeDate(date);
			List<ScheduleModel> scheduleModels = getDisplayedSchedules(dateOnly, lstSchedule);
			result.put(dateOnly, scheduleModels);
		}
		return result;
	}

	public LinearLayout buildBirthdayItem(Context context, LayoutInflater inflater, ScheduleModel scheduleModel, int layoutId){
		LinearLayout birthdayItem = (LinearLayout)inflater.inflate(layoutId, null);
		SelectableRoundedImageView imgUser = (SelectableRoundedImageView)birthdayItem.findViewById(R.id.img_item_birthday_user);
		WfPicassoHelper.loadImage(context, BuildConfig.HOST + scheduleModel.showUserModel.avatarPath, imgUser, null);
		TextView txtUser = (TextView)birthdayItem.findViewById(R.id.txt_item_birthday_username);
		txtUser.setText(scheduleModel.showUserModel.userName);
		return birthdayItem;
	}

	public static LinearLayout buildHolidayItem(LayoutInflater inflater, HolidayModel holidayModel, int paddingLeftRightPx, int layoutId){
		LinearLayout itemHoliday = (LinearLayout)inflater.inflate(layoutId, null);
		itemHoliday.setPadding(paddingLeftRightPx, MARGIN_TOP_BOTTOM, paddingLeftRightPx, MARGIN_TOP_BOTTOM);
		TextView txtHolidayName = (TextView)itemHoliday.findViewById(R.id.txt_item_holiday_name);
		txtHolidayName.setText(holidayModel.holidayName);
		return itemHoliday;
	}

	public static LinearLayout buildHolidayItemFromSchedule(LayoutInflater inflater, ScheduleModel scheduleModel, int paddingLeftRightPx, int layoutId){
		LinearLayout itemHoliday = (LinearLayout)inflater.inflate(layoutId, null);
		itemHoliday.setPadding(paddingLeftRightPx, MARGIN_TOP_BOTTOM, paddingLeftRightPx, MARGIN_TOP_BOTTOM);
		TextView txtHolidayName = (TextView)itemHoliday.findViewById(R.id.txt_item_holiday_name);
		txtHolidayName.setText(scheduleModel.scheduleName);
		return itemHoliday;
	}

	private void buildOffers(List<WorkRequest> offers){

	}

	public static List<WorkRequest> getSortedWorkOffersByDate(List<WorkRequest> offers, Date date){
		List<WorkRequest> result = new ArrayList<>();
		for(WorkRequest offer : offers){
			if(ClUtil.belongPeriod(date, offer.startDate, offer.endDate)){
				result.add(offer);
			}
		}
		// sortOffersByType(result);
		return result;
	}

	public static void sortOffersByType(List<WorkRequest> offers){
		final Map<String, Integer> offer_type_order = new HashMap<>();
		offer_type_order.put(WorkRequest.REQUEST_TYPE_PAID_VACATION_ALL, 1);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_PAID_VACATION_MORNING, 2);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_PAID_VACATION_AFTERNOON, 3);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_SPECIAL_HOLIDAY, 4);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_COMPENSATORY_HOLIDAY, 5);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_ABSENT, 6);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_OVERTIME_WORKING, 7);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_OVERTIME, 8);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_HOLIDAY_WORKING, 9);
		offer_type_order.put(WorkRequest.REQUEST_TYPE_SHORT_TIME, 10);

		Collections.sort(offers, new Comparator<WorkRequest>() {

			@Override
			public int compare(WorkRequest o1, WorkRequest o2){
				return offer_type_order.get(o1.offerType).compareTo(offer_type_order.get(o2.offerType));
			}
		});
	}

	public static LinearLayout buildOfferItem(Context context, LayoutInflater inflater, WorkRequest offer, int layoutId){
		LinearLayout offerItemView = (LinearLayout)inflater.inflate(layoutId, null);
		ImageView imgAvatar = (ImageView)offerItemView.findViewById(R.id.img_item_offer_avatar);
		TextView txtUsername = (TextView)offerItemView.findViewById(R.id.txt_item_offer_username);
		TextView txtDate = (TextView)offerItemView.findViewById(R.id.txt_item_offer_date);
		TextView txtType = (TextView)offerItemView.findViewById(R.id.txt_item_offer_type);
		TextView txtStatus = (TextView)offerItemView.findViewById(R.id.txt_item_offer_status);
		// TextView txtNote = (TextView)offerItemView.findViewById(R.id.txt_item_offer_note);

		WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, imgAvatar, offer.userAvatarPath, R.drawable.wf_profile);
		txtUsername.setText(offer.userName);

		if(WorkRequest.REQUEST_TYPE_HOLIDAY_WORKING.equals(offer.offerType) || WorkRequest.REQUEST_TYPE_OVERTIME.equals(offer.offerType) || WorkRequest.REQUEST_TYPE_SHORT_TIME.equals(offer.offerType)){
			txtDate.setText(" - " + offer.startTimeString + " - " + offer.endTimeString);
		}else{
			txtDate.setVisibility(View.GONE);
		}

		txtType.setText(offer.offerTypeName);
		txtStatus.setText(offer.offerStatusName);
		return offerItemView;
	}

	public static LinearLayout buildWorkRequestItemFromSchedule(Context context, LayoutInflater inflater, ScheduleModel workRequest, int layoutId){
		LinearLayout offerItemView = (LinearLayout)inflater.inflate(layoutId, null);
		ImageView imgAvatar = (ImageView)offerItemView.findViewById(R.id.img_item_offer_avatar);
		TextView txtUsername = (TextView)offerItemView.findViewById(R.id.txt_item_offer_username);
		TextView txtDate = (TextView)offerItemView.findViewById(R.id.txt_item_offer_date);
		TextView txtType = (TextView)offerItemView.findViewById(R.id.txt_item_offer_type);
		TextView txtStatus = (TextView)offerItemView.findViewById(R.id.txt_item_offer_status);
		WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, imgAvatar, workRequest.showUserModel.avatarPath, R.drawable.wf_profile);
		txtUsername.setText(workRequest.showUserModel.userName);
		txtDate.setText(" - " + workRequest.startTime + " - " + workRequest.endTime);
		txtType.setText(workRequest.scheduleName);
		txtStatus.setText(workRequest.scheduleNote);
		return offerItemView;
	}

	public static List<CalendarBirthdayModel> getBirthdayUsersByDate(List<CalendarBirthdayModel> userModels, Date date){
		List<CalendarBirthdayModel> result = new ArrayList<>();
		for(CalendarBirthdayModel userModel : userModels){
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_HYPHEN, date).equals(userModel.birthDay)){
				result.add(userModel);
			}
		}
		return result;
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
		TextView textView = buildTextView(title);
		lnrParent.addView(textView);

		lnrEvents.removeAllViews();
		lnrEvents.setVisibility(View.VISIBLE);
		TextView header = buildTextView(getContext().getString(R.string.daily_schedules_event_title));
		lnrEvents.addView(header);

		for(ScheduleModel scheduleModel : scheduleModels){
			buildScheduleItem(lnrParent, scheduleModel);
		}

		if(lnrEvents.getChildCount() <= 1){
			lnrEvents.setVisibility(View.GONE);
		}
	}

	private TextView buildTextView(String title){
		TextView textView = new TextView(getContext());

		textView.setPadding(MARGIN_LEFT_RIGHT, MARGIN_TEXT_TOP_BOTTOM, MARGIN_LEFT_RIGHT, MARGIN_TEXT_TOP_BOTTOM);
		textView.setText(title);
		textView.setBackgroundColor(WelfareUtil.getColor(getContext(), R.color.cl_title_bg));
		return textView;
	}

	public LinearLayout buildScheduleItem(final Context context, LayoutInflater layoutInflater, final ScheduleModel schedule, OnScheduleItemClickListener onScheduleItemClickListener, final Date selectedDate){
		LinearLayout lnrSchedulesContainer = (LinearLayout)layoutInflater.inflate(R.layout.item_schedule, null);
		buildScheduleItemCommon(context, lnrSchedulesContainer, schedule, onScheduleItemClickListener, selectedDate);
		lnrSchedulesContainer.setFocusable(true);
		return lnrSchedulesContainer;
	}

	public void buildScheduleItemCommon(Context context, LinearLayout lnrSchedulesContainer, final ScheduleModel schedule, final OnScheduleItemClickListener onScheduleItemClickListener, final Date selectedDate){
		TextView txtScheduleTime = (TextView)lnrSchedulesContainer.findViewById(R.id.txt_item_schedule_time);
		String time;
		if(CCBooleanUtil.checkBoolean(schedule.isAllDay)){
			time = context.getString(R.string.daily_page_all_day);
		}else{
			time = schedule.startTime + " - " + schedule.endTime;
		}
		txtScheduleTime.setText(time);

		TextView txtScheduleName = (TextView)lnrSchedulesContainer.findViewById(R.id.txt_item_schedule_name);
		txtScheduleName.setText(schedule.scheduleName);

		lnrSchedulesContainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){

				onScheduleItemClickListener.onClickScheduleItem(schedule, selectedDate);
			}
		});
	}

	private void buildScheduleItem(LinearLayout lnrParent, ScheduleModel scheduleModel){
		switch(scheduleModel.eventType){
		case ScheduleModel.EVENT_TYPE_SCHEDULE:
			LinearLayout item = (LinearLayout)buildScheduleItem(getContext(), this.inflater, scheduleModel, onScheduleItemClickListener, selectedDate);
			item.setPadding(MARGIN_LEFT_RIGHT, 0, MARGIN_LEFT_RIGHT, 0);
			lnrParent.addView(item);
			break;
		case ScheduleModel.EVENT_TYPE_BIRTHDAY:
			LinearLayout birthdayItem = buildBirthdayItem(getContext(), inflater, scheduleModel, R.layout.item_birthday);
			lnrEvents.addView(birthdayItem);
			break;
		case ScheduleModel.EVENT_TYPE_WORK_OFFER:
			LinearLayout offerItem = buildWorkRequestItemFromSchedule(getContext(), inflater, scheduleModel, R.layout.item_work_offer);
			lnrEvents.addView(offerItem);
			break;
		case ScheduleModel.EVENT_TYPE_HOLIDAY_OLD:
			LinearLayout holidayItem = buildHolidayItemFromSchedule(inflater, scheduleModel, WelfareUtil.dpToPx(WelfareFragment.MARGIN_LEFT_RIGHT_PX), R.layout.item_holiday);
			lnrEvents.addView(holidayItem);
			break;
		default:
			break;
		}
		hasDisplayedItem = true;
	}

	public static List<ScheduleModel> getDisplayedSchedules(Date date, List<ScheduleModel> lstSchedule){
		List<ScheduleModel> scheduleModels = new ArrayList<>();
		for(ScheduleModel scheduleModel : lstSchedule){
			if(isScheduleOf(scheduleModel, date)){
				scheduleModels.add(scheduleModel);
			}
		}
		return scheduleModels;
	}

	public static boolean isScheduleOf(ScheduleModel scheduleModel, Date date){
		return CCDateUtil.compareDate(scheduleModel.startDate, date, false) <= 0 && CCDateUtil.compareDate(date, scheduleModel.endDate, false) <= 0;
	}

	public interface OnScheduleItemClickListener{

		void onClickScheduleItem(ScheduleModel schedule, Date selectedDate);
	}
}

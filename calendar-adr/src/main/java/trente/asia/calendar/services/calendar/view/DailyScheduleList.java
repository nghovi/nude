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
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 3/6/2017.
 */

public class DailyScheduleList extends LinearLayout{

	private static final String										TIME_NOON				= "12:00";
	private static final Integer									SCHEDULES_ALL_DAY		= 1;
	private static final Integer									SCHEDULES_MORNING		= 2;
	private static final Integer									SCHEDULES_AFTERNOON		= 3;
	private static final int										MARGIN_LEFT_RIGHT		= WelfareUtil.dpToPx(16);
	private static final int										MARGIN_TEXT_TOP_BOTTOM	= WelfareUtil.dpToPx(4);
	private static final int										MARGIN_TOP_BOTTOM		= WelfareUtil.dpToPx(4);

	// private LinearLayout lnrOffers;

	private Map<Integer, List<ScheduleModel>>						schedulesMap;
	private LayoutInflater											inflater;
	private List<ScheduleModel>										lstSchedule;
	private Date													selectedDate;
	private WeeklyScheduleListAdapter.OnScheduleItemClickListener	onScheduleItemClickListener;

	private LinearLayout											lnrEvents;
	// private LinearLayout lnrBirthdays;
	public boolean													hasDisplayedItem		= false;

	public DailyScheduleList(Context context){
		super(context);
	}

	public DailyScheduleList(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void init(LayoutInflater inflater, WeeklyScheduleListAdapter.OnScheduleItemClickListener onScheduleItemClickListener){
		this.inflater = inflater;
		this.onScheduleItemClickListener = onScheduleItemClickListener;
		lnrEvents = (LinearLayout)findViewById(R.id.lnr_daily_schedules_list_event);
		// lnrOffers = (LinearLayout)findViewById(R.id.lnr_daily_schedules_list_work_offer);
		// lnrBirthdays = (LinearLayout)findViewById(R.id.lnr_daily_schedules_list_birthday);

	}

	public void updateFor(Date selectedDate, List<ScheduleModel> lstSchedule, List<HolidayModel> holidayModels, List<WorkOffer> offers, List<UserModel> userModels){
		this.lstSchedule = lstSchedule;
		this.selectedDate = selectedDate;
		schedulesMap = getDisplayedSchedulesMaps();
		buildTimelySchedules(R.id.lnr_daily_schedule_list_all_day, R.string.daily_page_all_day, schedulesMap.get(SCHEDULES_ALL_DAY));
		buildTimelySchedules(R.id.lnr_daily_schedule_list_morning, R.string.daily_page_morning, schedulesMap.get(SCHEDULES_MORNING));
		buildTimelySchedules(R.id.lnr_daily_schedule_list_afternoon, R.string.daily_page_afternoon, schedulesMap.get(SCHEDULES_AFTERNOON));
		buildEvents(holidayModels, offers, userModels);
	}

	private void buildEvents(List<HolidayModel> holidayModels, List<WorkOffer> offers, List<UserModel> userModels){
		lnrEvents.removeAllViews();
		List<UserModel> birthdayUsers = getBirthdayUsersToday(userModels);
		List<WorkOffer> workOffers = getWorkOfferToday(offers);
		List<HolidayModel> holidayModelList = HolidayModel.getHolidayModels(selectedDate, holidayModels);
		if(!CCCollectionUtil.isEmpty(holidayModelList) || !CCCollectionUtil.isEmpty(workOffers) || !CCCollectionUtil.isEmpty(birthdayUsers)){
			lnrEvents.setVisibility(View.VISIBLE);
			TextView header = buildTextView(getContext().getString(R.string.daily_schedules_event_title));
			lnrEvents.addView(header);
			for(HolidayModel holidayModel : holidayModelList){
				LinearLayout holidayItem = buildHolidayItem(inflater, holidayModel, WelfareFragment.MARGIN_LEFT_RIGHT_PX, R.layout.item_holiday);
				lnrEvents.addView(holidayItem);
				hasDisplayedItem = true;
			}

			for(UserModel user : birthdayUsers){
				LinearLayout birthdayItem = buildBirthdayItem(getContext(), inflater, user, R.layout.item_birthday);
				lnrEvents.addView(birthdayItem);
				hasDisplayedItem = true;
			}

			for(WorkOffer offer : workOffers){
				LinearLayout birthdayItem = buildOfferItem(getContext(), inflater, offer, R.layout.item_work_offer);
				lnrEvents.addView(birthdayItem);
				hasDisplayedItem = true;
			}

		}else{
			lnrEvents.setVisibility(View.GONE);
		}
	}

	public static LinearLayout buildHolidayItem(LayoutInflater inflater, HolidayModel holidayModel, int paddingLeftRightPx, int layoutId){
		LinearLayout itemHoliday = (LinearLayout)inflater.inflate(layoutId, null);
		itemHoliday.setPadding(paddingLeftRightPx, MARGIN_TOP_BOTTOM, paddingLeftRightPx, MARGIN_TOP_BOTTOM);
		TextView txtHolidayName = (TextView)itemHoliday.findViewById(R.id.txt_item_holiday_name);
		txtHolidayName.setText(holidayModel.holidayName);
//		ImageView imgHoliday = (ImageView)itemHoliday.findViewById(R.id.img_item_holiday_image);
//		WfPicassoHelper.loadImage(itemHoliday.getContext(), holidayModel.imgPath, imgHoliday, null);
		return itemHoliday;
	}

	private void buildOffers(List<WorkOffer> offers){

	}

	private List<WorkOffer> getWorkOfferToday(List<WorkOffer> offers){
		List<WorkOffer> result = new ArrayList<>();
		for(WorkOffer offer : offers){
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate).equals(offer.startDateString)){
				result.add(offer);
			}
		}
		sortOffersByType(result);
		return result;
	}

	public static void sortOffersByType(List<WorkOffer> offers){
		final Map<String, Integer> offer_type_order = new HashMap<>();
		offer_type_order.put(WorkOffer.OFFER_TYPE_PAID_VACATION_ALL, 1);
		offer_type_order.put(WorkOffer.OFFER_TYPE_PAID_VACATION_MORNING, 2);
		offer_type_order.put(WorkOffer.OFFER_TYPE_PAID_VACATION_AFTERNOON, 3);
		offer_type_order.put(WorkOffer.OFFER_TYPE_SPECIAL_HOLIDAY, 4);
		offer_type_order.put(WorkOffer.OFFER_TYPE_COMPENSATORY_HOLIDAY, 5);
		offer_type_order.put(WorkOffer.OFFER_TYPE_ABSENT, 6);
		offer_type_order.put(WorkOffer.OFFER_TYPE_OVERTIME_WORKING, 7);
		offer_type_order.put(WorkOffer.OFFER_TYPE_OVERTIME, 8);
		offer_type_order.put(WorkOffer.OFFER_TYPE_HOLIDAY_WORKING, 9);
		offer_type_order.put(WorkOffer.OFFER_TYPE_SHORT_TIME, 10);

		Collections.sort(offers, new Comparator<WorkOffer>() {

			@Override
			public int compare(WorkOffer o1, WorkOffer o2){
				return offer_type_order.get(o1.offerType).compareTo(offer_type_order.get(o2.offerType));
			}
		});
	}

	public static LinearLayout buildOfferItem(Context context, LayoutInflater inflater, WorkOffer offer, int layoutId){
		LinearLayout offerItemView = (LinearLayout)inflater.inflate(layoutId, null);
		ImageView imgAvatar = (ImageView)offerItemView.findViewById(R.id.img_item_offer_avatar);
		TextView txtUsername = (TextView)offerItemView.findViewById(R.id.txt_item_offer_username);
		TextView txtDate = (TextView)offerItemView.findViewById(R.id.txt_item_offer_date);
		TextView txtType = (TextView)offerItemView.findViewById(R.id.txt_item_offer_type);
		TextView txtStatus = (TextView)offerItemView.findViewById(R.id.txt_item_offer_status);
		TextView txtNote = (TextView)offerItemView.findViewById(R.id.txt_item_offer_note);

		WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, imgAvatar, offer.userAvatarPath, R.drawable.wf_profile);
		txtUsername.setText(offer.userName);

		if(WorkOffer.OFFER_TYPE_HOLIDAY_WORKING.equals(offer.offerType) || WorkOffer.OFFER_TYPE_OVERTIME.equals(offer.offerType) || WorkOffer.OFFER_TYPE_SHORT_TIME.equals(offer.offerType)){
			txtDate.setText(" - " + offer.startTimeString + " - " + offer.endTimeString);
		}else{
			txtDate.setVisibility(View.GONE);
		}

		txtType.setText(offer.offerTypeName);
		txtStatus.setText(offer.offerStatusName);
//		txtNote.setText(offer.note);
		return offerItemView;
	}

	private void buildBirthdays(List<UserModel> userModels){

	}

	public static void sortBirthdays(List<UserModel> userModels){
		Collections.sort(userModels, new Comparator<UserModel>() {

			@Override
			public int compare(UserModel o1, UserModel o2){
				return o1.userName.compareTo(o2.userName);
			}
		});
	}

	public List<UserModel> getBirthdayUsersToday(List<UserModel> userModels){
		List<UserModel> result = new ArrayList<>();
		for(UserModel userModel : userModels){
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_MM_DD, selectedDate).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_MM_DD, CCDateUtil.makeDateCustom(userModel.dateBirth, WelfareConst.WF_DATE_TIME)))){
				result.add(userModel);
			}
		}
		sortBirthdays(result);
		return result;
	}

	public static LinearLayout buildBirthdayItem(Context context, LayoutInflater inflater, UserModel userModel, int layoutId){
		LinearLayout birthdayItem = (LinearLayout)inflater.inflate(layoutId, null);
		SelectableRoundedImageView imgUser = (SelectableRoundedImageView)birthdayItem.findViewById(R.id.img_item_birthday_user);
		WfPicassoHelper.loadImage(context, BuildConfig.HOST + userModel.avatarPath, imgUser, null);
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
		TextView textView = buildTextView(title);
		lnrParent.addView(textView);

		for(ScheduleModel scheduleModel : scheduleModels){
			buildScheduleItem(lnrParent, scheduleModel);
		}
	}

	private TextView buildTextView(String title){
		TextView textView = new TextView(getContext());

		textView.setPadding(MARGIN_LEFT_RIGHT, MARGIN_TEXT_TOP_BOTTOM, MARGIN_LEFT_RIGHT, MARGIN_TEXT_TOP_BOTTOM);
		textView.setText(title);
		textView.setBackgroundColor(WelfareUtil.getColor(getContext(), R.color.cl_title_bg));
		return textView;
	}

	private void buildScheduleItem(LinearLayout lnrParent, ScheduleModel scheduleModel){
		LinearLayout item = (LinearLayout)WeeklyScheduleListAdapter.buildScheduleItem(getContext(), this.inflater, scheduleModel, onScheduleItemClickListener, selectedDate);
		item.setPadding(MARGIN_LEFT_RIGHT, 0, MARGIN_LEFT_RIGHT, 0);
		lnrParent.addView(item);
		hasDisplayedItem = true;
	}

	private Map<Integer, List<ScheduleModel>> getDisplayedSchedulesMaps(){
		Map<Integer, List<ScheduleModel>> result = new HashMap<>();
		result.put(SCHEDULES_ALL_DAY, new ArrayList<ScheduleModel>());
		result.put(SCHEDULES_MORNING, new ArrayList<ScheduleModel>());
		result.put(SCHEDULES_AFTERNOON, new ArrayList<ScheduleModel>());
		for(ScheduleModel scheduleModel : lstSchedule){
			if(!CCStringUtil.isEmpty(scheduleModel.key) && isScheduleOf(scheduleModel, selectedDate)){
				if(CCBooleanUtil.checkBoolean(scheduleModel.isAllDay)){
					result.get(SCHEDULES_ALL_DAY).add(scheduleModel);
				}else if(isBeforeNoon(scheduleModel.startTime)){
					result.get(SCHEDULES_MORNING).add(scheduleModel);
				}else{
					result.get(SCHEDULES_AFTERNOON).add(scheduleModel);
				}
			}
		}
		sortByTime(result.get(SCHEDULES_ALL_DAY));
		sortByTime(result.get(SCHEDULES_MORNING));
		sortByTime(result.get(SCHEDULES_AFTERNOON));
		return result;
	}

	public static void sortByTime(List<ScheduleModel> scheduleModels){
		Collections.sort(scheduleModels, new Comparator<ScheduleModel>() {

			@Override
			public int compare(ScheduleModel o1, ScheduleModel o2){
				return CCDateUtil.compareDate(CCDateUtil.makeDateCustom(o1.startDate, WelfareConst.WF_DATE_TIME), CCDateUtil.makeDateCustom(o2.startDate, WelfareConst.WF_DATE_TIME), true);
			}
		});
	}

	private boolean isBeforeNoon(String startTime){
		int startMinute = CCDateUtil.convertTime2Min(startTime);
		int noonMinute = CCDateUtil.convertTime2Min(TIME_NOON);
		return startMinute < noonMinute;
	}

	private boolean isScheduleOf(ScheduleModel scheduleModel, Date date){
		Date startDate = WelfareUtil.makeDate(scheduleModel.startDate);
		Date endDate = WelfareUtil.makeDate(scheduleModel.endDate);
		return CCDateUtil.compareDate(startDate, date, false) <= 0 && CCDateUtil.compareDate(date, endDate, false) <= 0;
	}

}

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
import android.widget.ImageView;
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
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
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
	private static final int									MARGIN_TEXT_TOP_BOTTOM	= WelfareUtil.dpToPx(4);
	private static final int									MARGIN_TOP_BOTTOM		= WelfareUtil.dpToPx(4);

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
		List<HolidayModel> holidayModelList = HolidayModel.getHolidayModels(selectedDate, holidayModels);
		if(!CCCollectionUtil.isEmpty(holidayModelList)){
			lnrHolidays.setVisibility(View.VISIBLE);
			TextView header = buildTextView(getContext().getString(R.string.holiday_title));
			lnrHolidays.addView(header);
			for(HolidayModel holidayModel : holidayModelList){
				LinearLayout holidayItem = buildHolidayItem(inflater, holidayModel, WelfareFragment.MARGIN_LEFT_RIGHT);
				lnrHolidays.addView(holidayItem);
			}
		}else{
			lnrHolidays.setVisibility(View.GONE);
		}
	}

	public static LinearLayout buildHolidayItem(LayoutInflater inflater, HolidayModel holidayModel, int paddingLeftRightPx){
		LinearLayout itemHoliday = (LinearLayout)inflater.inflate(R.layout.item_holiday, null);
		itemHoliday.setPadding(paddingLeftRightPx, MARGIN_TOP_BOTTOM, paddingLeftRightPx, MARGIN_TOP_BOTTOM);
		TextView txtHolidayName = (TextView)itemHoliday.findViewById(R.id.txt_item_holiday_name);
		txtHolidayName.setText(holidayModel.holidayName);
		return itemHoliday;
	}

	private void buildOffers(List<WorkOffer> offers){
		lnrOffers.removeAllViews();
		List<WorkOffer> workOffers = getWorkOfferToday(offers);
		if(!CCCollectionUtil.isEmpty(workOffers)){
			lnrOffers.setVisibility(View.VISIBLE);
			TextView header = buildTextView(getContext().getString(R.string.offer_title));
			lnrOffers.addView(header);
			for(WorkOffer offer : workOffers){
				LinearLayout birthdayItem = buildOfferItem(offer);
				lnrOffers.addView(birthdayItem);
			}
		}else{
			lnrOffers.setVisibility(View.GONE);
		}
	}

	private List<WorkOffer> getWorkOfferToday(List<WorkOffer> offers){
		List<WorkOffer> result = new ArrayList<>();
		for(WorkOffer offer : offers){
			if(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate).equals(offer.startDateString)){
				result.add(offer);
			}
		}
		return result;
	}

	private LinearLayout buildOfferItem(WorkOffer offer){
		LinearLayout offerItemView = (LinearLayout)inflater.inflate(R.layout.item_work_offer, null);
		ImageView imgAvatar = (ImageView)offerItemView.findViewById(R.id.img_item_offer_avatar);
		TextView txtUsername = (TextView)offerItemView.findViewById(R.id.txt_item_offer_username);
		TextView txtDate = (TextView)offerItemView.findViewById(R.id.txt_item_offer_date);
		TextView txtType = (TextView)offerItemView.findViewById(R.id.txt_item_offer_type);
		TextView txtStatus = (TextView)offerItemView.findViewById(R.id.txt_item_offer_status);

		WfPicassoHelper.loadImageWithDefaultIcon(getContext(), BuildConfig.HOST, imgAvatar, offer.userAvatarPath, R.drawable.wf_profile);
		txtUsername.setText(offer.userName);
		txtDate.setText(offer.startDateString);
		txtType.setText(offer.offerTypeName);
		txtStatus.setText(offer.offerStatusName);
		return offerItemView;
	}

	private void buildBirthdays(List<UserModel> userModels){
		lnrBirthdays.removeAllViews();
		List<UserModel> birthdayUsers = getBirthdayUsersToday(userModels);
		if(!CCCollectionUtil.isEmpty(birthdayUsers)){
			lnrBirthdays.setVisibility(View.VISIBLE);
			TextView header = buildTextView(getContext().getString(R.string.birthday_title));
			lnrBirthdays.addView(header);
			for(UserModel user : birthdayUsers){
				LinearLayout birthdayItem = buildBirthdayItem(user);
				lnrBirthdays.addView(birthdayItem);
			}
		}else{
			lnrBirthdays.setVisibility(View.GONE);
		}
	}

	public List<UserModel> getBirthdayUsersToday(List<UserModel> userModels){
		List<UserModel> result = new ArrayList<>();
		for(UserModel userModel : userModels){
			if(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_6, selectedDate).equals(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_6, CCDateUtil.makeDateCustom(userModel.dateBirth, WelfareConst.WL_DATE_TIME_1)))){
				result.add(userModel);
			}
		}
		return result;
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
		LinearLayout item = (LinearLayout)CalendarDayListAdapter.buildScheduleItem(getContext(), this.inflater, scheduleModel, onScheduleItemClickListener, WelfareFormatUtil.formatDate(selectedDate));
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

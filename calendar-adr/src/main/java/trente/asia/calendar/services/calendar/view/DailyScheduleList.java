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
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.welfare.adr.activity.WelfareFragment;
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
		Collections.sort(mSchedules, new Comparator<ScheduleModel>() {

			@Override
			public int compare(ScheduleModel o1, ScheduleModel o2){
				if(ScheduleModel.EVENT_TYPE_WORK_OFFER.equals(o1.eventType) && ScheduleModel.EVENT_TYPE_BIRTHDAY.equals(o2.eventType)){
					return -1;
				}else if(ScheduleModel.EVENT_TYPE_BIRTHDAY.equals(o1.eventType) && ScheduleModel.EVENT_TYPE_WORK_OFFER.equals(o2.eventType)){
					return 1;
				}
				return 0;
			}
		});
		buildTimelySchedules(R.id.lnr_daily_schedule_list_all_day, R.string.schedule, mSchedules);
	}

	public void initDataWithMap(Map<Date, List<ScheduleModel>> daySchedulesMap){
		this.daySchedulesMap = daySchedulesMap;
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

	public static LinearLayout buildHolidayItemFromSchedule(LayoutInflater inflater, ScheduleModel scheduleModel, int paddingLeftRightPx, int layoutId){
		LinearLayout itemHoliday = (LinearLayout)inflater.inflate(layoutId, null);
		itemHoliday.setPadding(paddingLeftRightPx, MARGIN_TOP_BOTTOM, paddingLeftRightPx, MARGIN_TOP_BOTTOM);
		TextView txtHolidayName = (TextView)itemHoliday.findViewById(R.id.txt_item_holiday_name);
		txtHolidayName.setText(scheduleModel.scheduleName);
		return itemHoliday;
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
			LinearLayout holidayItem = buildHolidayItemFromSchedule(inflater, scheduleModel, WelfareUtil.dpToPx(WelfareFragment.MARGIN_LEFT_RIGHT_DP), R.layout.item_holiday);
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

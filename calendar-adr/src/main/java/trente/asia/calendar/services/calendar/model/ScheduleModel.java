package trente.asia.calendar.services.calendar.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * ScheduleModel
 *
 * @author TrungND
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ScheduleModel{

	public static final String	EVENT_TYPE_SCHEDULE		= "SH";
	public static final String	EVENT_TYPE_WORK_OFFER	= "WO";
	public static final String	EVENT_TYPE_HOLIDAY_OLD	= "HO";
	public static final String	EVENT_TYPE_BIRTHDAY		= "BI";
	// public static final String SCHEDULE_TYPE_HOLIDAY = "H";
	// public static final String SCHEDULE_TYPE_WORK_OFFER = "O";
	// public static final String SCHEDULE_TYPE_BIRTHDAY = "B";
	public static final String	SCHEDULE_TYPE_PUB		= "PUB";
	public static final String	SCHEDULE_TYPE_PRI		= "PRI";
	public static final String	SCHEDULE_TYPE_PRI_COM	= "COMPPRI";
	public static final String	SCHEDULE_COLOR_NORMAL	= "444444";
	public static final String	SCHEDULE_COLOR_HOLIDAY	= "D22DB6";
	public static final String	SCHEDULE_COLOR_OFFER	= "359CF3";
	public static final String	SCHEDULE_COLOR_BIRTHDAY	= "24C772";

	public String				scheduleName;
	public String				scheduleNote;
	public String				scheduleUrl;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date					startDate;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date					endDate;
	public String				startTime;
	public String				endTime;
	public String				key;
	public String				calendarId;
	public String				roomId;
	public String				joinUsers;
	public String				scheduleType;
	public String				categoryId;
	public CategoryModel		categoryModel;
	public Boolean				isAllDay;
	public List<UserModel>		scheduleJoinUsers;
	public String				repeatType				= ClConst.SCHEDULE_REPEAT_TYPE_NONE;
	public String				repeatLimitType;
	public String				repeatData;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date					repeatEnd;
	public String				repeatInterval;
	public RoomModel			roomModel;
	public Boolean				isWarning;
	public UserModel			owner;
	public String				scheduleColor			= "#FF0000";
	public String				scheduleTextColor		= "#000000";
	public boolean				isPeriod;
	public String				eventType;
	public List<CalendarUser>	calendarUsers;
	public UserModel			showUserModel;
	private boolean				isBelongToLoginUser		= false;

	public ScheduleModel(){

	}

	public ScheduleModel(HolidayModel holidayModel){
		this.scheduleName = holidayModel.holidayName;
		this.startDate = holidayModel.startDate;
		this.endDate = holidayModel.endDate;
		this.isAllDay = true;
		this.isPeriod = true;
		this.eventType = EVENT_TYPE_HOLIDAY_OLD;
	}

	public ScheduleModel(CalendarBirthdayModel calendarBirthdayModel, Date start, Date end){
		this.scheduleName = calendarBirthdayModel.message;
		Calendar birthdayCalendar = CCDateUtil.makeCalendar(CCDateUtil.makeDateCustom(calendarBirthdayModel.birthDay, WelfareConst.WF_DATE_TIME_DATE_HYPHEN));
		Calendar cStart = CCDateUtil.makeCalendar(start);
		Calendar cEnd = CCDateUtil.makeCalendar(end);

		birthdayCalendar.set(Calendar.YEAR, cStart.get(Calendar.YEAR));

		if(cStart.getTimeInMillis() > birthdayCalendar.getTimeInMillis() || birthdayCalendar.getTimeInMillis() > cEnd.getTimeInMillis()){
			birthdayCalendar.set(Calendar.YEAR, cEnd.get(Calendar.YEAR));
		}

		this.startDate = birthdayCalendar.getTime();
		this.endDate = this.startDate;
		this.isAllDay = true;
		this.scheduleType = SCHEDULE_TYPE_PUB;
		this.eventType = EVENT_TYPE_BIRTHDAY;
	}

	public static void determinePeriod(List<ScheduleModel> scheduleModels){
		for(ScheduleModel scheduleModel : scheduleModels){
			scheduleModel.determinePeriodSchedule();
		}
	}

	public void determinePeriodSchedule(){
		isPeriod = CCDateUtil.compareDate(startDate, endDate, false) != 0;
	}

	public void determineBelongToLoginUser(UserModel loginUser){
		isBelongToLoginUser = UserModel.contain(scheduleJoinUsers, loginUser);
	}

	public String getScheduleColor(){
		if(EVENT_TYPE_HOLIDAY_OLD.equals(eventType)){
			return WelfareFormatUtil.formatColor(SCHEDULE_COLOR_HOLIDAY);
		}else if(EVENT_TYPE_BIRTHDAY.equals(eventType)){
			return WelfareFormatUtil.formatColor(SCHEDULE_COLOR_BIRTHDAY);
		}else if(EVENT_TYPE_WORK_OFFER.equals(eventType)){
			return WelfareFormatUtil.formatColor(SCHEDULE_COLOR_OFFER);
		}

		return scheduleColor;
	}

	public static boolean isRepeat(ScheduleModel schedule){
		return schedule != null && !CCStringUtil.isEmpty(schedule.repeatType) && !ClConst.SCHEDULE_REPEAT_TYPE_NONE.equals(schedule.repeatType);
	}

	// public static ScheduleModel clone(ScheduleModel scheduleModel, UserModel userModel){
	// ScheduleModel cloned = new ScheduleModel();
	// cloned.scheduleColor = userModel.color;
	//
	// cloned.scheduleName = scheduleModel.scheduleName;
	// cloned.scheduleNote = scheduleModel.scheduleNote;
	// cloned.scheduleUrl = scheduleModel.scheduleUrl;
	// cloned.startDate = scheduleModel.startDate;
	// cloned.endDate = scheduleModel.endDate;
	// cloned.startTime = scheduleModel.startTime;
	// cloned.endTime = scheduleModel.endTime;
	// cloned.key = scheduleModel.key;
	// cloned.calendarId = scheduleModel.calendarId;
	// cloned.roomId = scheduleModel.roomId;
	// cloned.joinUsers = scheduleModel.joinUsers;
	// cloned.scheduleType = scheduleModel.scheduleType;
	// cloned.categoryId = scheduleModel.categoryId;
	// cloned.categoryModel = scheduleModel.categoryModel;
	// cloned.isAllDay = scheduleModel.isAllDay;
	// cloned.scheduleJoinUsers = scheduleModel.scheduleJoinUsers;
	// cloned.repeatType = scheduleModel.repeatType;
	// cloned.repeatLimitType = scheduleModel.repeatLimitType;
	// cloned.repeatData = scheduleModel.repeatData;
	// cloned.repeatEnd = scheduleModel.repeatEnd;
	// cloned.repeatInterval = scheduleModel.repeatInterval;
	// cloned.isWarning = scheduleModel.isWarning;
	// cloned.owner = scheduleModel.owner;
	// return cloned;
	// }

	public long getStartTimeMilis(){
		return CCDateUtil.convertTime2Min(startTime) * 60 * 1000;
	}

	public long getEndTimeMilis(){
		return CCDateUtil.convertTime2Min(endTime) * 60 * 1000;
	}

	public boolean isBelongToLoginUser(){
		return isBelongToLoginUser;
	}

	public static void determineBelongToLoginUser(List<ScheduleModel> lstSchedule, UserModel loginUser){
		for(ScheduleModel scheduleModel : lstSchedule){
			scheduleModel.determineBelongToLoginUser(loginUser);
		}
	}
}

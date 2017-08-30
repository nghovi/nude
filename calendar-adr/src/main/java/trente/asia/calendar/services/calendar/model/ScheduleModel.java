package trente.asia.calendar.services.calendar.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * ScheduleModel
 *
 * @author TrungND
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ScheduleModel{

	public String			scheduleName;
	public String			scheduleNote;
	public String			scheduleUrl;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date				startDate;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date				endDate;
	public String			startTime;
	public String			endTime;
	public String			key;
	public String			calendarId;
	public String			roomId;
	public String			joinUsers;
	public String			scheduleType;
	public String			categoryId;
	public CategoryModel	categoryModel;
	public Boolean			isAllDay;
	public List<UserModel>	scheduleJoinUsers;
	public String			repeatType;
	public String			repeatLimitType;
	public String			repeatData;
	@JsonField(typeConverter = WelfareActivity.WelfareTimeConverter.class)
	public Date				repeatEnd;
	public String			repeatInterval;
	public RoomModel		roomModel;
	public Boolean			isWarning;
	public UserModel		owner;
	public String			scheduleColor	= "#FF0000";

	public ScheduleModel(){

	}

	public ScheduleModel(HolidayModel holidayModel){
		this.scheduleName = holidayModel.holidayName;
		this.startDate = holidayModel.startDate;
		this.endDate = holidayModel.endDate;
		this.isAllDay = true;
		this.scheduleType = ClConst.SCHEDULE_TYPE_HOLIDAY;
	}

	public ScheduleModel(UserModel userModel){
		this.scheduleName = userModel.userName;
		Calendar calendar = Calendar.getInstance();
		Calendar birthdayCalendar = CCDateUtil.makeCalendar(userModel.dateBirth);
		Calendar scheduleCalendar = CCDateUtil.makeCalendar(calendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH) + 1, birthdayCalendar.get(Calendar.DAY_OF_MONTH));
		this.startDate = scheduleCalendar.getTime();
		this.endDate = this.startDate;
		this.isAllDay = true;
		this.scheduleType = ClConst.SCHEDULE_TYPE_BIRTHDAY;
	}

	public ScheduleModel(WorkOffer workOffer){
		this.scheduleName = workOffer.offerTypeName;
		this.startDate = workOffer.startDate;
		this.endDate = workOffer.endDate;
		// boolean isPaidVacationAll = ClConst.WORKING_OFFER_TYPE_PAID_VACATION_ALL.equals(workOffer.offerType);
		// boolean isSpecialHoliday = ClConst.WORKING_OFFER_TYPE_SPECIAL_HOLIDAY.equals(workOffer.offerType);
		// boolean isCompensatoryHoliday = ClConst.WORKING_OFFER_TYPE_COMPENSATORY_HOLIDAY.equals(workOffer.offerType);
		// boolean isAbsent = ClConst.WORKING_OFFER_TYPE_ABSENT.equals(workOffer.offerType);
		// if(isPaidVacationAll || isSpecialHoliday || isCompensatoryHoliday || isAbsent){
		// this.isAllDay = true;
		// }
		this.isAllDay = true;
		this.scheduleType = ClConst.SCHEDULE_TYPE_WORK_OFFER;
	}

	public boolean isPeriodSchedule(){
		if(startDate == null || endDate == null){
			return false;
		}

		if(ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(scheduleType) || ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleType)){
			return true;
		}

		return CCDateUtil.compareDate(startDate, endDate, false) != 0;
	}

	public String getScheduleColor(){
		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_HOLIDAY);
		}else if(ClConst.SCHEDULE_TYPE_BIRTHDAY.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_BIRTHDAY);
		}else if(ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_OFFER);
		}

		return scheduleColor;
	}

	public static boolean isRepeat(ScheduleModel schedule){
		return schedule != null && !CCStringUtil.isEmpty(schedule.repeatType) && !ClConst.SCHEDULE_REPEAT_TYPE_NONE.equals(schedule.repeatType);
	}

	public static ScheduleModel clone(ScheduleModel scheduleModel, UserModel userModel){
		ScheduleModel cloned = new ScheduleModel();
		cloned.scheduleColor = userModel.color;

		cloned.scheduleName = scheduleModel.scheduleName;
		cloned.scheduleNote = scheduleModel.scheduleNote;
		cloned.scheduleUrl = scheduleModel.scheduleUrl;
		cloned.startDate = scheduleModel.startDate;
		cloned.endDate = scheduleModel.endDate;
		cloned.startTime = scheduleModel.startTime;
		cloned.endTime = scheduleModel.endTime;
		cloned.key = scheduleModel.key;
		cloned.calendarId = scheduleModel.calendarId;
		cloned.roomId = scheduleModel.roomId;
		cloned.joinUsers = scheduleModel.joinUsers;
		cloned.scheduleType = scheduleModel.scheduleType;
		cloned.categoryId = scheduleModel.categoryId;
		cloned.categoryModel = scheduleModel.categoryModel;
		cloned.isAllDay = scheduleModel.isAllDay;
		cloned.scheduleJoinUsers = scheduleModel.scheduleJoinUsers;
		cloned.repeatType = scheduleModel.repeatType;
		cloned.repeatLimitType = scheduleModel.repeatLimitType;
		cloned.repeatData = scheduleModel.repeatData;
		cloned.repeatEnd = scheduleModel.repeatEnd;
		cloned.repeatInterval = scheduleModel.repeatInterval;
		cloned.isWarning = scheduleModel.isWarning;
		cloned.owner = scheduleModel.owner;
		return cloned;
	}
}

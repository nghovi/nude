package trente.asia.calendar.services.calendar.model;

import java.util.Calendar;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * ScheduleModel
 *
 * @author TrungND
 */

public class ScheduleModel{

	public String			scheduleName;
	public String			scheduleNote;
	public String			scheduleUrl;
	// public String scheduleColor;
	public String			startDate;
	public String			endDate;
	public String			startTime;
	public String			endTime;

	public String			key;
	public String			calendarId;
	public String			roomId;
	public String			joinUsers;
	public CalendarModel	calendar;

	public String			scheduleType;
	public String			categoryId;
	public CategoryModel	categoryModel;
	public Boolean			isAllDay;
	public Boolean			isRepeat;
	public List<UserModel>	scheduleJoinUsers;
	// public Boolean isHoliday;

	public String			repeatType;
	public String			repeatLimitType;
	public String			repeatData;
	public String			repeatEnd;
	public String			repeatInterval;
	public Boolean			isWarning;
	public String			ownerId;
	public UserModel		owner;

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
		Calendar birthdayCalendar = CCDateUtil.makeCalendar(WelfareUtil.makeDate(userModel.dateBirth));
		Calendar scheduleCalendar = CCDateUtil.makeCalendar(calendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH) + 1, birthdayCalendar.get(Calendar.DAY_OF_MONTH));
		this.startDate = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME, scheduleCalendar.getTime());
		this.endDate = this.startDate;
		this.isAllDay = true;
		this.scheduleType = ClConst.SCHEDULE_TYPE_BIRTHDAY;
	}

	public ScheduleModel(WorkOffer workOffer){
		this.scheduleName = workOffer.offerTypeName;
		this.startDate = workOffer.startDate;
		this.endDate = workOffer.endDate;
		boolean isPaidVacationAll = ClConst.WORKING_OFFER_TYPE_PAID_VACATION_ALL.equals(workOffer.offerType);
		boolean isSpecialHoliday = ClConst.WORKING_OFFER_TYPE_SPECIAL_HOLIDAY.equals(workOffer.offerType);
		boolean isCompensatoryHoliday = ClConst.WORKING_OFFER_TYPE_COMPENSATORY_HOLIDAY.equals(workOffer.offerType);
		boolean isAbsent = ClConst.WORKING_OFFER_TYPE_ABSENT.equals(workOffer.offerType);
		if(isPaidVacationAll || isSpecialHoliday || isCompensatoryHoliday || isAbsent){
			this.isAllDay = true;
		}
		this.scheduleType = ClConst.SCHEDULE_TYPE_WORK_OFFER;
	}

	public boolean isPeriodSchedule(){
		if(startDate == null || endDate == null){
			return false;
		}

		// String startDateFormat = WelfareFormatUtil.formatDate(WelfareUtil.makeDate(startDate));
		// String endDateFormat = WelfareFormatUtil.formatDate(WelfareUtil.makeDate(endDate));
		String startDateFormat = WelfareFormatUtil.removeTime4Date(startDate);
		String endDateFormat = WelfareFormatUtil.removeTime4Date(endDate);
		return !startDateFormat.equals(endDateFormat);
	}

	public String getScheduleColor(){
		if(!CCStringUtil.isEmpty(this.calendarId)){
			// CategoryModel categoryModel = ClUtil.findCategory4Id(lstCategory, this.categoryId);
			if(categoryModel != null){
				return WelfareFormatUtil.formatColor(categoryModel.categoryColor);
			}
		}
		if(ClConst.SCHEDULE_TYPE_HOLIDAY.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_HOLIDAY);
		}else if(ClConst.SCHEDULE_TYPE_BIRTHDAY.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_BIRTHDAY);
		}else if(ClConst.SCHEDULE_TYPE_WORK_OFFER.equals(scheduleType)){
			return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_OFFER);
		}

		return WelfareFormatUtil.formatColor(ClConst.SCHEDULE_COLOR_NORMAL);
	}

	public ScheduleModel cloneCommonData(){
		ScheduleModel clonedModel = new ScheduleModel();
		clonedModel.scheduleName = this.scheduleName;
		clonedModel.scheduleNote = this.scheduleNote;
		clonedModel.scheduleUrl = this.scheduleUrl;
		clonedModel.startDate = this.startDate;
		clonedModel.endDate = this.endDate;
		clonedModel.startTime = this.startTime;
		clonedModel.endTime = this.endTime;
		clonedModel.key = this.key;
		clonedModel.calendarId = this.calendarId;
		clonedModel.roomId = this.roomId;
		clonedModel.joinUsers = this.joinUsers;
		// clonedModel.calendar = this.calendar;
		clonedModel.scheduleType = this.scheduleType;
		clonedModel.categoryId = this.categoryId;
		clonedModel.categoryModel = this.categoryModel;
		clonedModel.isAllDay = this.isAllDay;
		clonedModel.isRepeat = this.isRepeat;
		clonedModel.scheduleJoinUsers = this.scheduleJoinUsers;
		clonedModel.repeatType = this.repeatType;
		clonedModel.repeatLimitType = this.repeatLimitType;
		clonedModel.repeatData = this.repeatData;
		clonedModel.repeatEnd = this.repeatEnd;
		clonedModel.repeatInterval = this.repeatInterval;
		clonedModel.isWarning = this.isWarning;
		clonedModel.ownerId = this.ownerId;
		clonedModel.owner = this.owner;
		return clonedModel;
	}
}

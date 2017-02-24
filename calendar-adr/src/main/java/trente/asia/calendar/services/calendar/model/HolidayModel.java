package trente.asia.calendar.services.calendar.model;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * HolidayModel
 *
 * @author TrungND
 */

public class HolidayModel {

    public String holidayName;
    public String startDate;
    public String endDate;

    public HolidayModel() {

    }

    public static HolidayModel getHolidayModel(Date date, List<HolidayModel>
            holidayModels) {
        for (HolidayModel holidayModel : holidayModels) {
            Date startDate = CCDateUtil.makeDateCustom(holidayModel.startDate,
                    WelfareConst.WL_DATE_TIME_7);
            Date endDate = CCDateUtil.makeDateCustom(holidayModel.endDate,
                    WelfareConst.WL_DATE_TIME_7);
            if (startDate.getTime() <= date.getTime() && date.getTime() <=
                    endDate.getTime()) {
                return holidayModel;
            }
        }
        return null;
    }
}

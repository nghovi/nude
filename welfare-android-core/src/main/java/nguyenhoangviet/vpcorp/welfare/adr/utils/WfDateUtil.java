package nguyenhoangviet.vpcorp.welfare.adr.utils;

import java.util.Date;

import asia.chiase.core.util.CCDateUtil;
import nguyenhoangviet.vpcorp.android.util.CsDateUtil;

/**
 * WfDateUtil
 *
 * @author TrungND
 */
public class WfDateUtil {

    public static int diffDate(Date date1, Date date2) {
        Date dateWithoutTime1 = CCDateUtil.makeDate(date1);
        Date dateWithoutTime2 = CCDateUtil.makeDate(date2);
        return CsDateUtil.diffDate(dateWithoutTime1, dateWithoutTime2);
    }

    /**
     * <strong>compareDate</strong><br>
     * <br> compare date
     * date format: yyyy/MM/dd
     *
     * @return
     */
    public static int compareDate(String date1, String date2) {
        Date dateWithoutTime1 = WelfareFormatUtil.makeDate(date1);
        Date dateWithoutTime2 = WelfareFormatUtil.makeDate(date2);
        return dateWithoutTime1.compareTo(dateWithoutTime2);
    }
}

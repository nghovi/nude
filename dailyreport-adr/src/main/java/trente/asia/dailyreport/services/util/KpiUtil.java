package trente.asia.dailyreport.services.util;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;

/**
 * Created by takano-yasuhiro on 2017/07/21.
 */
public class KpiUtil {

    public static final String KPI_UNIT_SALE = "SALE";
    public static final String KPI_UNIT_COUNT = "COUNT";

    private static final int MAX_CHARACTER_COUNT = 9999;
    private static final int MAX_CHARACTER_SALE = 9999999;

    public static Boolean isCheckSize(String unit, String value) {

        Integer val = CCNumberUtil.toInteger(value);
        if (val == null) {
            return false;
        }
        switch (CCStringUtil.checkNull(unit)) {
            case KPI_UNIT_SALE:
                if (val > MAX_CHARACTER_SALE) {
                    return false;
                }
                break;
            case KPI_UNIT_COUNT:
                if (val > MAX_CHARACTER_COUNT) {
                    return false;
                }

                break;
            default:
                if (val > MAX_CHARACTER_SALE) {
                    return false;
                }
                break;
        }
        return true;

    }

    public static String getMax(String unit) {

        switch (CCStringUtil.checkNull(unit)) {
            case KPI_UNIT_SALE:
                return CCFormatUtil.formatAmount(MAX_CHARACTER_SALE);

            case KPI_UNIT_COUNT:
                return CCFormatUtil.formatAmount(MAX_CHARACTER_COUNT);
            default:
                return CCFormatUtil.formatAmount(MAX_CHARACTER_SALE);
        }

    }
}

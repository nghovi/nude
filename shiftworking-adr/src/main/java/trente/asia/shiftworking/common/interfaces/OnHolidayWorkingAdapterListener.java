package trente.asia.shiftworking.common.interfaces;

import trente.asia.shiftworking.services.offer.model.HolidayWorkingModel;
import trente.asia.shiftworking.services.offer.model.OvertimeModel;

/**
 * Created by tien on 9/22/2017.
 */

public interface OnHolidayWorkingAdapterListener {
    void onHolidayWorkingAdapterClick(HolidayWorkingModel holiday, String type);
}
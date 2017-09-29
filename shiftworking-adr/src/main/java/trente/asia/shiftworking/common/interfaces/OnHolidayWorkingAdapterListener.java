package trente.asia.shiftworking.common.interfaces;

import trente.asia.shiftworking.services.requests.model.HolidayWorkingModel;

/**
 * Created by tien on 9/22/2017.
 */

public interface OnHolidayWorkingAdapterListener {
    void onHolidayWorkingAdapterClick(HolidayWorkingModel holiday, String type);
}

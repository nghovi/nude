package trente.asia.shiftworking.common.interfaces;

import trente.asia.shiftworking.services.offer.model.VacationModel;

/**
 * Created by tien on 9/22/2017.
 */

public interface OnVacationAdapterListener {
    void onVationAdapterClick(VacationModel vacation, String type);
}

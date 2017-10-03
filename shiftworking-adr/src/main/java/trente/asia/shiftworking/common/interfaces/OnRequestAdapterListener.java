package trente.asia.shiftworking.common.interfaces;

import trente.asia.welfare.adr.models.VacationRequestModel;

/**
 * Created by tien on 9/22/2017.
 */

public interface OnRequestAdapterListener {
	void onRequestAdapterClick(VacationRequestModel vacationRequest, String type);
}

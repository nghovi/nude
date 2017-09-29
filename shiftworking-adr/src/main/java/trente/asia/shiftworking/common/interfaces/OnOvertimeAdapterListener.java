package trente.asia.shiftworking.common.interfaces;

import trente.asia.shiftworking.services.requests.model.OtRequestModel;

/**
 * Created by tien on 9/22/2017.
 */

public interface OnOvertimeAdapterListener{

	void onOvertimeAdapterClick(OtRequestModel otRequest, String type);
}

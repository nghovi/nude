package trente.asia.shiftworking.services.worktime.listener;

import trente.asia.shiftworking.services.worktime.model.WorkingTimeModel;

/**
 * ItemWorkTimeClickListener
 *
 * @author TrungND
 */
public interface ItemWorkTimeClickListener {

	public void onItemModifyListener(WorkingTimeModel item);

	public void onItemDeleteListener(WorkingTimeModel item);
}

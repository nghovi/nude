package trente.asia.shiftworking.services.worktime.listener;

import android.widget.TextView;

import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.services.worktime.model.WorkingTimeModel;

/**
 * ItemWorkTimeClickListener
 *
 * @author TrungND
 */
public interface ItemWorkTimeClickListener{

	public void onItemModifyListener(WorkingTimeModel item);

	public void onItemDeleteListener(WorkingTimeModel item);

	public boolean onItemLongClicked(WorkingTimeModel model, ChiaseTextView txtWorkingType);
}

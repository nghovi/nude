package nguyenhoangviet.vpcorp.shiftworking.services.worktime.listener;

import android.widget.TextView;

import nguyenhoangviet.vpcorp.android.view.ChiaseTextView;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.WorkingTimeModel;

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

package nguyenhoangviet.vpcorp.messenger.services.message.listener;

import nguyenhoangviet.vpcorp.android.view.model.ChiaseListItemModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.MessageContentModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmMessageModel;

/**
 * OnActionClickListener
 *
 * @author TrungND
 */
public interface OnActionClickListener{

	public void onActionClickListener(ChiaseListItemModel item, RealmMessageModel message);
}

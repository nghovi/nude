package trente.asia.messenger.services.message.listener;

import trente.asia.android.view.model.ChiaseListItemModel;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.messenger.services.message.model.RealmMessageModel;

/**
 * OnActionClickListener
 *
 * @author TrungND
 */
public interface OnActionClickListener{

	public void onActionClickListener(ChiaseListItemModel item, RealmMessageModel message);
}

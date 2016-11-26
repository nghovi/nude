package trente.asia.messenger.services.message.listener;

import trente.asia.android.view.model.ChiaseListItemModel;
import trente.asia.messenger.services.message.model.MessageContentModel;

/**
 * OnActionClickListener
 *
 * @author TrungND
 */
public interface OnActionClickListener{

	public void onActionClickListener(ChiaseListItemModel item, MessageContentModel message);
}

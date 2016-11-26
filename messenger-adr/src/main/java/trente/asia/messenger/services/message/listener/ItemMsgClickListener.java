package trente.asia.messenger.services.message.listener;

import trente.asia.messenger.services.message.model.MessageContentModel;

/**
 * Created by Huy-nq on 7/15/2016.
 */
public interface ItemMsgClickListener{

	public void onItemMsgClickListener(MessageContentModel item);

	public void onItemCheckClickListener(MessageContentModel item);

	public void onItemMsgLongClickListener(MessageContentModel item);
}

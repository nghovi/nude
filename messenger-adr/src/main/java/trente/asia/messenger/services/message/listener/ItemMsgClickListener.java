package nguyenhoangviet.vpcorp.messenger.services.message.listener;

import nguyenhoangviet.vpcorp.messenger.services.message.model.MessageContentModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmMessageModel;

/**
 * Created by Huy-nq on 7/15/2016.
 */
public interface ItemMsgClickListener{

	public void onItemMsgClickListener(RealmMessageModel item);

	public void onItemCheckClickListener(RealmMessageModel item);

	public void onItemMsgLongClickListener(RealmMessageModel item);
}

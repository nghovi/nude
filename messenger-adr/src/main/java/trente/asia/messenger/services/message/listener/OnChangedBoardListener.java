package trente.asia.messenger.services.message.listener;

import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.RealmBoardModel;

/**
 * OnChangedBoardListener
 *
 * @author TrungND
 */
public interface OnChangedBoardListener{

	public void onChangedBoard(RealmBoardModel boardModel, boolean isLoad);

	public void onRefreshUnreadMessage(Integer unreadMessage);
}

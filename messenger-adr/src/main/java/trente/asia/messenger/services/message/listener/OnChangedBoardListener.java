package trente.asia.messenger.services.message.listener;

import trente.asia.messenger.services.message.model.BoardModel;

/**
 * OnChangedBoardListener
 *
 * @author TrungND
 */
public interface OnChangedBoardListener{

	public void onChangedBoard(BoardModel boardModel, boolean isLoad);

	public void onRefreshUnreadMessage(Integer unreadMessage);
}

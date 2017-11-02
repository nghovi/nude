package nguyenhoangviet.vpcorp.messenger.services.message.listener;

import nguyenhoangviet.vpcorp.messenger.services.message.model.BoardModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmBoardModel;

/**
 * OnChangedBoardListener
 *
 * @author TrungND
 */
public interface OnChangedBoardListener{

	public void onChangedBoard(RealmBoardModel boardModel, boolean isLoad);

	public void onRefreshUnreadMessage(Integer unreadMessage);
}

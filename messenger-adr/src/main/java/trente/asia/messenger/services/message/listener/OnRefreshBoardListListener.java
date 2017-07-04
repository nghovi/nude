package trente.asia.messenger.services.message.listener;

import java.util.List;

import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.RealmBoardModel;

/**
 * OnRefreshBoardListListener
 *
 * @author TrungND
 */
public interface OnRefreshBoardListListener{

	public void onRefreshBoardListListener(List<RealmBoardModel> lstBoard);

}

package trente.asia.messenger.services.message.listener;

import java.util.List;

import trente.asia.messenger.services.message.model.BoardModel;

/**
 * OnRefreshBoardListListener
 *
 * @author TrungND
 */
public interface OnRefreshBoardListListener{

	public void onRefreshBoardListListener(List<BoardModel> lstBoard);

}

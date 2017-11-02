package nguyenhoangviet.vpcorp.messenger.services.message.listener;

import java.util.List;

import nguyenhoangviet.vpcorp.messenger.services.message.model.BoardModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmBoardModel;

/**
 * OnRefreshBoardListListener
 *
 * @author TrungND
 */
public interface OnRefreshBoardListListener{

	public void onRefreshBoardListListener(List<RealmBoardModel> lstBoard);

}

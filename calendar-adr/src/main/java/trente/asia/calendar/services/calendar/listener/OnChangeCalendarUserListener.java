package nguyenhoangviet.vpcorp.calendar.services.calendar.listener;

import java.util.List;

import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * OnChangeCalendarUserListener
 *
 * @author TrungND
 */
public interface OnChangeCalendarUserListener {

	public void onChangeCalendarUserListener(List<UserModel> lstCalendarUser);
}

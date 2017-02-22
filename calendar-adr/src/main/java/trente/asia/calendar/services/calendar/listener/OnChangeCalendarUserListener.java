package trente.asia.calendar.services.calendar.listener;

import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * OnChangeCalendarUserListener
 *
 * @author TrungND
 */
public interface OnChangeCalendarUserListener {

	public void onChangeCalendarUserListener(List<UserModel> lstCalendarUser);
}

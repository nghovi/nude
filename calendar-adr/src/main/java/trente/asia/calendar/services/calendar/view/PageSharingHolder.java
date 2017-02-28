package trente.asia.calendar.services.calendar.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/23/2017.
 */

public class PageSharingHolder{

	public NavigationHeader				navigationHeader;
	public final UserListLinearLayout	userListLinearLayout;
	public ClFilterUserListDialog		filterDialog;
	private Context						mContext;
	public int							selectedPagePosition;

	public PageSharingHolder(NavigationHeader navigationHeader, UserListLinearLayout userListLinearLayout, final View.OnClickListener listener){
		this.mContext = navigationHeader.getContext();
		this.navigationHeader = navigationHeader;
		this.userListLinearLayout = userListLinearLayout;
		filterDialog = new ClFilterUserListDialog(mContext, userListLinearLayout, null);
		ImageView imgDone = (ImageView)filterDialog.findViewById(R.id.img_id_done);
		imgDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				filterDialog.dismiss();
				filterDialog.saveActiveUserList();
				listener.onClick(v);
			}
		});
		this.userListLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				filterDialog.show();
			}
		});
	}

	public void updateFilter(List<UserModel> calendarUsers){
		userListLinearLayout.show(calendarUsers, (int)mContext.getResources().getDimension(R.dimen.margin_30dp));
		filterDialog.updateUserList(calendarUsers);
	}
}

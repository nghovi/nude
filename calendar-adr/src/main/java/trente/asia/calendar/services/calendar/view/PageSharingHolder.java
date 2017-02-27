package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.view.View;

import java.util.List;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.SchedulesPageFragment;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/23/2017.
 */

public class PageSharingHolder {

    public NavigationHeader navigationHeader;
    public final UserListLinearLayout userListLinearLayout;
    public ClFilterUserListDialog filterDialog;
    private Context mContext;
    public SchedulesPageFragment selectedFragment;
    public int selectedPagePosition;

    public PageSharingHolder(NavigationHeader navigationHeader,
                             UserListLinearLayout userListLinearLayout) {
        this.mContext = navigationHeader.getContext();
        this.navigationHeader = navigationHeader;
        this.userListLinearLayout = userListLinearLayout;
        filterDialog = new ClFilterUserListDialog(mContext,
                userListLinearLayout);
        this.userListLinearLayout.setOnClickListener(new View.OnClickListener
                () {

            @Override
            public void onClick(View v) {
                filterDialog.show();
            }
        });
    }

    public void updateFilter(List<UserModel> calendarUsers) {
        userListLinearLayout.show(calendarUsers, (int) mContext.getResources()
                .getDimension(R.dimen.margin_30dp));
        filterDialog.updateUserList(calendarUsers);
    }
}

package trente.asia.calendar.commons.fragments;

import android.os.Bundle;
import android.view.View;

import java.util.Date;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseFragment;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.views.NavigationHeader;
import trente.asia.calendar.commons.views.PageSharingHolder;
import trente.asia.calendar.services.calendar.ScheduleFormFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;
import trente.asia.welfare.adr.models.UserModel;

/**
 * ClPageFragment
 *
 * @author VietNH
 */
public abstract class ClPageFragment extends AbstractClFragment implements NavigationHeader.OnAddBtnClickedListener {

    protected Date selectedDate;
    protected int pagePosition;
    protected PageSharingHolder pageSharingHolder;
    protected OnChangeCalendarUserListener changeCalendarUserListener = new OnChangeCalendarUserListener() {

        @Override
        public void onChangeCalendarUserListener(List<UserModel> lstCalendarUser) {
            if (pageSharingHolder.isRefreshUserList) {
                pageSharingHolder.isRefreshUserList = false;
                String targetUserData = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
                if (!CCCollectionUtil.isEmpty(lstCalendarUser)) {
                    pageSharingHolder.userListLinearLayout.setVisibility(View.VISIBLE);
                    pageSharingHolder.updateFilter(lstCalendarUser, targetUserData);
                } else {
                    pageSharingHolder.userListLinearLayout.removeAllViews();
                }
                pageSharingHolder.updateFilter(lstCalendarUser, targetUserData);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        host = BuildConfig.HOST;
    }

    @Override
    protected void initView() {
        super.initView();
        if (pageSharingHolder != null)
            pageSharingHolder.navigationHeader.setOnHeaderActionsListener(this);
    }

    @Override
    protected void initData() {
        if (pageSharingHolder != null && pageSharingHolder.selectedPagePosition == pagePosition) {
            loadData();
        }
    }

    protected abstract void loadData();

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setPageSharingHolder(PageSharingHolder pageSharingHolder) {
        this.pageSharingHolder = pageSharingHolder;
    }

    public void setPagePosition(int pagePosition) {
        this.pagePosition = pagePosition;
    }

    @Override
    public void onAddBtnClick(Date date) {
        String selectedCalendarString = prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING);
        if (!CCStringUtil.isEmpty(selectedCalendarString)) {
            gotoScheduleFormFragment(date);
        } else {
            alertDialog.setMessage(getString(R.string.cl_common_validate_no_calendar_msg));
            alertDialog.show();
        }
    }

    private void gotoScheduleFormFragment(Date date) {
        ScheduleFormFragment scheduleFormFragment = new ScheduleFormFragment();
        if (date == null) {
            date = pageSharingHolder.getClickedDate();
        }
        scheduleFormFragment.setSelectedDate(date);
        ChiaseFragment parentFragment = (ChiaseFragment) getParentFragment();
        parentFragment.gotoFragment(scheduleFormFragment);
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    protected String getUpperTitle() {
        return "";
    }

    protected void updateHeaderTitles() {
        if (pagePosition == pageSharingHolder.selectedPagePosition) {
            String title = getUpperTitle();
            pageSharingHolder.navigationHeader.updateMainHeaderTitle(title);
        }
    }

}

package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.calendar.services.calendar.view.PageSharingHolder;
import trente.asia.calendar.services.calendar.view.SchedulesPagerAdapter;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * WeeklyFragment
 *
 * @author TrungND
 */
public abstract class PageContainerFragment extends AbstractClFragment {

    protected ViewPager mViewPager;
    protected SchedulesPagerAdapter mPagerAdapter;
    protected WfSlideMenuLayout mSlideMenuLayout;

    protected final int INITIAL_POSITION = Integer.MAX_VALUE / 2;
    protected final Date TODAY = Calendar.getInstance().getTime();

    protected ImageView mImgLeftHeader;
    protected NavigationHeader navigationHeader;
    protected UserListLinearLayout userListLinearLayout;
    private PageSharingHolder holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_pager_container,
                    container,
                    false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        userListLinearLayout = (UserListLinearLayout) activity.findViewById(R
                .id.lnr_fragment_pager_container_user_list);
        navigationHeader = (NavigationHeader) getView().findViewById(R.id
                .lnr_navigation_header);
        mImgLeftHeader = (ImageView) getView().findViewById(R.id
                .img_id_header_left_icon);
        mViewPager = (ViewPager) getView().findViewById(R.id.pager);
        mPagerAdapter = initPagerAdapter();
        holder = new PageSharingHolder(navigationHeader,
                userListLinearLayout, this);
        mPagerAdapter.setPageSharingHolder(holder);
        mPagerAdapter.setInitialPosition(INITIAL_POSITION);
        mViewPager.setAdapter(mPagerAdapter);
        holder.selectedPagePosition = INITIAL_POSITION;
        prefAccUtil.saveActiveDate(TODAY);
        setActiveDate(INITIAL_POSITION);
        mViewPager.setCurrentItem(INITIAL_POSITION);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener
                () {

            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                setActiveDate(position);
                holder.selectedPagePosition = position;
                SchedulesPageFragment fragment = (SchedulesPageFragment)
                        mPagerAdapter.getItem(position);
                if (fragment != null) {
                    fragment.updateHeaderTitles();
                }
            }
        });


        ImageView imgRightIcon = (ImageView) getView().findViewById(R.id
                .img_id_header_right_icon);
        imgRightIcon.setVisibility(View.VISIBLE);
        imgRightIcon.setOnClickListener(this);

        mImgLeftHeader.setOnClickListener(this);
        mSlideMenuLayout = (WfSlideMenuLayout) getView().findViewById(R.id
                .drawer_layout);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CalendarListFragment calendarListFragment = new CalendarListFragment();
        transaction.replace(R.id.slice_menu_board, calendarListFragment)
                .commit();
    }

    private Date setActiveDate(int position) {
        Date activeDate = CsDateUtil.addWeek(TODAY, position -
                INITIAL_POSITION);
        prefAccUtil.saveActiveDate(activeDate);
        return activeDate;
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_weekly;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_id_header_left_icon:
                mSlideMenuLayout.toggleMenu();
                break;
            case R.id.img_id_header_right_icon:
                gotoScheduleFormFragment();
                break;
            case R.id.img_id_done:
                SchedulesPageFragment schedulesPageFragment =
                        (SchedulesPageFragment) mPagerAdapter.getItem(holder
                                .selectedPagePosition);
                schedulesPageFragment.callApi();
                break;
            default:
                break;
        }
    }

    private void gotoScheduleFormFragment() {
        ScheduleFormFragment fragment = new ScheduleFormFragment();
        gotoFragment(fragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    abstract SchedulesPagerAdapter initPagerAdapter();
}

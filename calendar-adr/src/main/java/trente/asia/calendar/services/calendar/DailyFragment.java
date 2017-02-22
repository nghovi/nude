package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.DailyCalendarPagerAdapter;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * DailyFragment
 *
 * @author VietNH
 */
public class DailyFragment extends AbstractClFragment {

    private ViewPager mViewPager;
    private DailyCalendarPagerAdapter mPagerAdapter;
    private WfSlideMenuLayout mSlideMenuLayout;

    private final int ACTIVE_PAGE = Integer.MAX_VALUE / 2;
    private final Date TODAY = Calendar.getInstance().getTime();

    private ImageView mImgLeftHeader;
    private NavigationHeader navigationHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_daily, container,
                    false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        navigationHeader = (NavigationHeader) getView().findViewById(R.id
                .lnr_navigation_header);
        mImgLeftHeader = (ImageView) getView().findViewById(R.id
                .img_id_header_left_icon);

        mViewPager = (ViewPager) getView().findViewById(R.id.pager);
        mPagerAdapter = new DailyCalendarPagerAdapter(getChildFragmentManager
                ());
        mPagerAdapter.setNavigationHeader(navigationHeader);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(ACTIVE_PAGE);
        prefAccUtil.saveActiveDate(TODAY);
        setActiveDate(ACTIVE_PAGE);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener
                () {

            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                setActiveDate(position);
                navigationHeader.isUpdated = false;
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
        Date activeDate = CsDateUtil.addDate(TODAY, position - ACTIVE_PAGE);
        prefAccUtil.saveActiveDate(activeDate);
        return activeDate;
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_daily;
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
}

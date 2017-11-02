package nguyenhoangviet.vpcorp.shiftworking.services.worktime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.view.CommonMonthView;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.view.SegmentedButton;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.view.WorkPagerAdapter;

public class WorkStatusFragment extends AbstractSwFragment {

    private ViewPager viewPager;
    private CommonMonthView monthView;
    private WorkPagerAdapter adapter;
    private List<Fragment> lstFragment;

    private SegmentedButton buttons;
    private Fragment activeFragment;
    private int isShowNoticeListFirst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_work_status, container, false);
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_work_status;
    }

    @Override
    public void initView() {
        super.initView();
        super.initHeader(null, myself.userName, null);

        viewPager = (ViewPager) getView().findViewById(R.id.view_pager_id_work);
        monthView = (CommonMonthView) getView().findViewById(R.id.view_id_month);
        monthView.initialization();
        monthView.workMonth = new Date();
        monthView.btnThisMonth.setText(getString(R.string.chiase_common_today));

        lstFragment = new ArrayList<>();
        WorkerFragment workerFragment = new WorkerFragment();
        workerFragment.setMonthView(monthView);
        lstFragment.add(workerFragment);
        lstFragment.add(new WorknoticeListFragment());
//        activeFragment = workerFragment;
        adapter = new WorkPagerAdapter(getFragmentManager(), lstFragment);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (buttons.getSelectedButtonIndex() != position) {
                    buttons.setPushedButtonIndex(position);
                }
                switch (position) {
                    case 0:
                        WorkerFragment workerFragment = (WorkerFragment) lstFragment.get(position);
                        activeFragment = workerFragment;
                        workerFragment.initMonthView(monthView);
                        break;
                    case 1:
                        WorknoticeListFragment noticeFragment = (WorknoticeListFragment) lstFragment.get(position);
                        activeFragment = noticeFragment;
                        noticeFragment.initMonthView(monthView);
                        break;
                    default:
                        break;
                }
            }
        });

        initSegment();
        if (isShowNoticeListFirst == 1) {
            buttons.post(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(1, true);
                }
            });
        }
    }

    private void initSegment() {
        buttons = (SegmentedButton) getView().findViewById(R.id.seg_id_work);
        buttons.clearButtons();
        buttons.addButtons(getString(R.string.sw_worker_title), getString(R.string.sw_notice_list_title));

        buttons.setPushedButtonIndex(0);
        buttons.setOnClickListener(new SegmentedButton.OnClickListenerSegmentedButton() {

            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index, true);
            }
        });
    }

    @Override
    protected void initData() {
        if (activeFragment != null) {
            if (activeFragment instanceof WorkerFragment) {
                ((WorkerFragment) activeFragment).initMonthView(monthView);
            } else if (activeFragment instanceof WorknoticeListFragment) {
                ((WorknoticeListFragment) activeFragment).initMonthView(monthView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        activeFragment = null;
        monthView = null;
        adapter = null;
        viewPager = null;
    }

    public void setIsShowNoticeListFirst(int isShowNoticeListFirst) {
        this.isShowNoticeListFirst = isShowNoticeListFirst;
    }
}

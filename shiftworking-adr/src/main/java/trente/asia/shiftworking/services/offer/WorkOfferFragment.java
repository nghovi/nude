package trente.asia.shiftworking.services.offer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.databinding.FragmentWorkOfferBinding;
import trente.asia.shiftworking.services.offer.adapter.WorkOfferFragmentAdapter;
import trente.asia.shiftworking.services.offer.edit.VacationEditFragment;
import trente.asia.shiftworking.services.offer.list.HolidayWorkingListFragment;
import trente.asia.shiftworking.services.offer.list.OvertimeListFragment;
import trente.asia.shiftworking.services.offer.list.VacationListFragment;

/**
 * Created by tien on 9/14/2017.
 */

public class WorkOfferFragment extends AbstractSwFragment implements ViewPager.OnPageChangeListener{
    private FragmentWorkOfferBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_work_offer, container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(null, myself.userName, R.drawable.bb_action_add);
        getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (binding != null) {
            return;
        }
        binding = DataBindingUtil.bind(mRootView);
        WorkOfferFragmentAdapter adapter = new WorkOfferFragmentAdapter(getFragmentManager());
        adapter.addFragment(new VacationListFragment());
        adapter.addFragment(new OvertimeListFragment());
        adapter.addFragment(new HolidayWorkingListFragment());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(this);
        binding.tabVacation.setOnClickListener(this);
        binding.tabOvertime.setOnClickListener(this);
        binding.tabHolidayWorking.setOnClickListener(this);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_offer;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_vacation:
                binding.viewPager.setCurrentItem(0);
                break;
            case R.id.tab_overtime:
                binding.viewPager.setCurrentItem(1);
                break;
            case R.id.tab_holiday_working:
                binding.viewPager.setCurrentItem(2);
                break;
            case R.id.img_id_header_right_icon:
                gotoAddVacationFragment();
                break;
        }
    }

    private void gotoAddVacationFragment(){
        VacationEditFragment fragment = new VacationEditFragment();
        gotoFragment(fragment);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        binding.vacationIndicator.setAlpha(.5f);
        binding.overtimeIndicator.setAlpha(.5f);
        binding.holidayWorkingIndicators.setAlpha(.5f);
        switch (position) {
            case 0:
                binding.vacationIndicator.setAlpha(1f);
                break;
            case 1:
                binding.overtimeIndicator.setAlpha(1f);
                break;
            case 2:
                binding.holidayWorkingIndicators.setAlpha(1f);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void log(String msg) {
        Log.e("WorkOffer", msg);
    }
}
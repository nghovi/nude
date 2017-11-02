package nguyenhoangviet.vpcorp.shiftworking.services.requests;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.databinding.FragmentWorkOfferBinding;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.adapter.WorkOfferFragmentAdapter;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.edit.HolidayWorkingEditFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.edit.OvertimeEditFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.edit.VacationEditFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.list.HolidayWorkingListFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.list.OvertimeListFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.list.VacationListFragment;

/**
 * Created by tien on 9/14/2017.
 */

public class WorkRequestFragment extends AbstractSwFragment implements ViewPager.OnPageChangeListener{

	private FragmentWorkOfferBinding	binding;
	private VacationListFragment		vacationListFragment;
	private OvertimeListFragment		overtimeListFragment;
	private HolidayWorkingListFragment	holidayWorkingListFragment;
	private boolean						loadAgain	= false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_offer, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(null, myself.userName, R.drawable.bb_action_add);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		if(binding != null){
			return;
		}
		binding = DataBindingUtil.bind(mRootView);
		WorkOfferFragmentAdapter adapter = new WorkOfferFragmentAdapter(getFragmentManager());
		vacationListFragment = new VacationListFragment();
		overtimeListFragment = new OvertimeListFragment();
		holidayWorkingListFragment = new HolidayWorkingListFragment();
		adapter.addFragment(vacationListFragment);
		adapter.addFragment(overtimeListFragment);
		adapter.addFragment(holidayWorkingListFragment);
		binding.viewPager.setAdapter(adapter);
		binding.viewPager.addOnPageChangeListener(this);
		binding.tabVacation.setOnClickListener(this);
		binding.tabOvertime.setOnClickListener(this);
		binding.tabHolidayWorking.setOnClickListener(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		if (!loadAgain) {
			loadAgain = true;
			return;
		}
		if(binding.viewPager.getCurrentItem() == 0){
			vacationListFragment.loadRequestList();
		}else if(binding.viewPager.getCurrentItem() == 1){
			overtimeListFragment.loadRequestList();
		}else if(binding.viewPager.getCurrentItem() == 2){
			holidayWorkingListFragment.loadRequestList();
		}
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_offer;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.tab_vacation:
			binding.viewPager.setCurrentItem(0);
			vacationListFragment.loadRequestList();;
			break;
		case R.id.tab_overtime:
			binding.viewPager.setCurrentItem(1);
			overtimeListFragment.loadRequestList();
			break;
		case R.id.tab_holiday_working:
			binding.viewPager.setCurrentItem(2);
			holidayWorkingListFragment.loadRequestList();
			break;
		case R.id.img_id_header_right_icon:
			if(binding.viewPager.getCurrentItem() == 0){
				gotoAddVacationFragment();
			}else if(binding.viewPager.getCurrentItem() == 1){
				gotoAddOvertimeFragment();
			}else if(binding.viewPager.getCurrentItem() == 2){
				gotoAddHolidayWorkingFragment();
			}
			break;
		}
	}

	private void gotoAddHolidayWorkingFragment(){
		HolidayWorkingEditFragment fragment = new HolidayWorkingEditFragment();
		gotoFragment(fragment);
	}

	private void gotoAddOvertimeFragment(){
		OvertimeEditFragment fragment = new OvertimeEditFragment();
		gotoFragment(fragment);
	}

	private void gotoAddVacationFragment(){
		VacationEditFragment fragment = new VacationEditFragment();
		gotoFragment(fragment);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

	}

	@Override
	public void onPageSelected(int position){
		binding.vacationIndicator.setAlpha(.5f);
		binding.overtimeIndicator.setAlpha(.5f);
		binding.holidayWorkingIndicators.setAlpha(.5f);
		switch(position){
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
	public void onPageScrollStateChanged(int state){

	}

	private void log(String msg){
		Log.e("WorkOffer", msg);
	}
}

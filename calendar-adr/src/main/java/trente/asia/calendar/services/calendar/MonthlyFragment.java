package nguyenhoangviet.vpcorp.calendar.services.calendar;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.android.util.CsDateUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.PageContainerFragment;
import nguyenhoangviet.vpcorp.calendar.commons.views.ClFragmentPagerAdapter;
import nguyenhoangviet.vpcorp.calendar.services.calendar.view.MonthlyCalendarPagerAdapter;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class MonthlyFragment extends PageContainerFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_pager_container, container, false);
		}
		return mRootView;
	}

	// @Override
	// public void onActivityCreated(Bundle savedInstanceState){
	// super.onActivityCreated(savedInstanceState);
	// final LinearLayout itemMonth = (LinearLayout)getView().findViewById(R.id.lnr_view_footer_monthly);
	// itemMonth.setOnClickListener(null);
	// final Handler handler = new Handler();
	// handler.postDelayed(new Runnable() {
	//
	// @Override
	// public void run(){
	// // Do something after 100ms
	// if(itemMonth != null){
	// if(getView() != null){
	// buildFooter();
	// }
	// }
	// }
	// }, 3000);
	// }

	@Override
	protected void initView(){
		super.initView();
		txtToday.setText(R.string.show_current_month);
		txtToday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickFooterItemMonthly();
			}
		});
	}

	@Override
	protected Date getActiveDate(int position){
		return CsDateUtil.addMonth(TODAY, position - INITIAL_POSITION);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	@Override
	protected ClFragmentPagerAdapter initPagerAdapter(){
		return new MonthlyCalendarPagerAdapter(getChildFragmentManager());
	}
}

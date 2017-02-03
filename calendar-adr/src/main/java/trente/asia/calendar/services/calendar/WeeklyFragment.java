package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;

/**
 * MonthlyFragment
 *
 * @author TrungND
 */
public class WeeklyFragment extends AbstractClFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
//		initHeader(R.drawable.wf_back_white, "Monthly", null);
	}

	@Override
	protected void initData(){
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}

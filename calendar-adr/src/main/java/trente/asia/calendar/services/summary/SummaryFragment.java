package trente.asia.calendar.services.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;

/**
 * DailyFragment
 *
 * @author VietNH
 */
public class SummaryFragment extends AbstractClFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_summary, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(null, getString(R.string.fragment_summary_title), null);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_summary;
	}

	@Override
	public void onClick(View v){

	}
}

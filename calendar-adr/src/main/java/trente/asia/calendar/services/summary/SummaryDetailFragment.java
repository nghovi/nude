package trente.asia.calendar.services.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;

/**
 * SummaryPageFragment
 *
 * @author VietNH
 */
public class SummaryDetailFragment extends AbstractClFragment{

	private SummaryModel	summaryModel;
	private LinearLayout	lnrBlockContainer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_summary_detail, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		lnrBlockContainer = (LinearLayout)getView().findViewById(R.id.lnr_block_container);
		GraphColumn.addBlocks(getContext(), summaryModel, lnrBlockContainer, false);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_summary;
	}

	@Override
	public void onClick(View v){

	}

	public void setSummaryModel(SummaryModel summaryModel){
		this.summaryModel = summaryModel;
	}
}

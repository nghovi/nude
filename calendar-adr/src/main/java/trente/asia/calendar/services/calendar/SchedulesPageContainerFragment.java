package trente.asia.calendar.services.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.define.CCConst;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.PageContainerFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;

/**
 * SchedulesPageContainerFragment
 *
 * @author TrungND
 */
public abstract class SchedulesPageContainerFragment extends PageContainerFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_pager_container, container, false);
		}
		return mRootView;
	}

	@Override
	protected boolean isShowUserList(){
		return true;
	}

	@Override
	public void onResume(){
		super.onResume();
		boolean isUpdate = CCConst.YES.equals(((WelfareActivity)activity).dataMap.get(ClConst.ACTION_SCHEDULE_UPDATE));
		boolean isDelete = CCConst.YES.equals(((WelfareActivity)activity).dataMap.get(ClConst.ACTION_SCHEDULE_DELETE));
		if(isUpdate || isDelete){
			((WelfareActivity)activity).dataMap.clear();
			SchedulesPageFragment fragment = (SchedulesPageFragment)mPagerAdapter.getItem(holder.selectedPagePosition);
			fragment.loadScheduleList();
		}
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_weekly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_done:
			SchedulesPageFragment schedulesPageFragment = (SchedulesPageFragment)mPagerAdapter.getItem(holder.selectedPagePosition);
			schedulesPageFragment.loadScheduleList();
			break;
		default:
			break;
		}
	}

}
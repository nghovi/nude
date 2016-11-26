package trente.asia.shiftworking.services.transit;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.worktime.model.ProjectModel;
import trente.asia.welfare.adr.define.WfUrlConst;

public class WorkTransitFragment extends AbstractSwFragment{

	private ProjectModel activeProject;

	public void setActiveProject(ProjectModel activeProject){
		this.activeProject = activeProject;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_transit, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_work_time;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.wf_back_white, myself.userName, R.drawable.bb_action_add);

		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
		imgRightIcon.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		loadWorkerList();
	}

	private void loadWorkerList(){

	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_API_WOKER_LIST.equals(url)){

		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			WorkTransitFormFragment fragment = new WorkTransitFormFragment();
			gotoFragment(fragment);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}

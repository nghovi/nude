package trente.asia.shiftworking.services.transit;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import trente.asia.android.util.AndroidUtil;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractPhotoFragment;
import trente.asia.shiftworking.services.worktime.model.ProjectModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;

public class WorkTransitFormFragment extends AbstractPhotoFragment{

	private ProjectModel		activeProject;
	private ChiaseTextView		txtTransitType;
	private ChiaseTextView		txtWayType;
	private ChiaseTextView		txtCostType;

	private ImageView			imgPhoto;

	private LinearLayout		lnrTransitType;
	private LinearLayout		lnrWayType;
	private LinearLayout		lnrCostType;

	private ChiaseListDialog	dlgTransitType;
	private ChiaseListDialog	dlgWayType;
	private ChiaseListDialog	dlgCostType;

	public void setActiveProject(ProjectModel activeProject){
		this.activeProject = activeProject;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_transit_form, container, false);
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
		super.initHeader(R.drawable.wf_back_white, myself.userName, R.drawable.cs_dummy_small);

		txtTransitType = (ChiaseTextView)getView().findViewById(R.id.txt_id_transit_type);
		txtWayType = (ChiaseTextView)getView().findViewById(R.id.txt_id_way_type);
		txtCostType = (ChiaseTextView)getView().findViewById(R.id.txt_id_cost_type);

		imgPhoto = (ImageView)getView().findViewById(R.id.img_id_photo);

		lnrTransitType = (LinearLayout)getView().findViewById(R.id.lnr_id_transit_type);
		lnrWayType = (LinearLayout)getView().findViewById(R.id.lnr_id_way_type);
		lnrCostType = (LinearLayout)getView().findViewById(R.id.lnr_id_cost_type);

		lnrTransitType.setOnClickListener(this);
		lnrWayType.setOnClickListener(this);
		lnrCostType.setOnClickListener(this);
		imgPhoto.setOnClickListener(this);

		initDialog();
	}

	private void initDialog(){
		Map<String, String> exampleMap = new LinkedHashMap<>();
		exampleMap.put("0", "Example-01");
		exampleMap.put("1", "Example-02");
		exampleMap.put("2", "Example-03");
		dlgTransitType = new ChiaseListDialog(activity, "Transit type", exampleMap, txtTransitType, null);
		dlgWayType = new ChiaseListDialog(activity, "Way type", exampleMap, txtWayType, null);
		dlgCostType = new ChiaseListDialog(activity, "Cost type", exampleMap, txtCostType, null);
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
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        if(resultCode != Activity.RESULT_OK) return;
        switch(requestCode){
            case WelfareConst.RequestCode.PHOTO_CHOOSE:
                if(returnedIntent != null){
                    String detailMessage = returnedIntent.getExtras().getString("detail");
                    if(WelfareConst.WF_FILE_SIZE_NG.equals(detailMessage)){
                        alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
                        alertDialog.show();
                    }else{
                        mOriginalPath = returnedIntent.getExtras().getString(WelfareConst.IMAGE_PATH_KEY);
                        Uri uri = AndroidUtil.getUriFromFileInternal(activity, new File(mOriginalPath));
                        imgPhoto.setImageURI(uri);
                    }
                }

                break;

            default:
                break;
        }
    }

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_transit_type:
			dlgTransitType.show();
			break;
		case R.id.lnr_id_way_type:
			dlgWayType.show();
			break;
		case R.id.lnr_id_cost_type:
			dlgCostType.show();
			break;
		case R.id.img_id_photo:
			menuManager.openMenu(imgPhoto);
			mViewForMenuBehind.setVisibility(View.VISIBLE);
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

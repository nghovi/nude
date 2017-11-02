package nguyenhoangviet.vpcorp.dailyreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.util.RecorderVideoFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

public class RecorderVideoActivity extends WelfareActivity{

	public static void starVideoPreviewActivity(Fragment fragment, String reportId){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), RecorderVideoActivity.class);
			intent.putExtra(WelfareConst.Extras.ACTIVE_REPORT_ID, reportId);
			fragment.startActivityForResult(intent, WelfareConst.RequestCode.VIDEO_CHOOSE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);

		addFragment(new RecorderVideoFragment());
	}
}

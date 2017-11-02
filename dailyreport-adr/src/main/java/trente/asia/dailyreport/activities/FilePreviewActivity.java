package nguyenhoangviet.vpcorp.dailyreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.util.FilePreviewFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

public class FilePreviewActivity extends WelfareActivity{

	/**
	 * start preview for file
	 * 
	 * @param fragment
	 */
	public static void startFilePreviewActivity(Fragment fragment, String reportId, String commentContent){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), FilePreviewActivity.class);
			intent.putExtra(WelfareConst.Extras.ACTIVE_REPORT_ID, reportId);
			intent.putExtra(WelfareConst.Extras.COMMENT_CONTENT, commentContent);

			fragment.startActivityForResult(intent, WelfareConst.RequestCode.PICK_FILE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);

		addFragment(new FilePreviewFragment());
	}
}

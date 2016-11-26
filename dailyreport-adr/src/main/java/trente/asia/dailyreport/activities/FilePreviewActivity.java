package trente.asia.dailyreport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import trente.asia.android.util.AndroidUtil;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.util.FilePreviewFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;

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

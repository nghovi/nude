package trente.asia.messenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import trente.asia.android.util.AndroidUtil;
import trente.asia.messenger.R;
import trente.asia.messenger.services.util.FilePreviewFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;

public class FilePreviewActivity extends WelfareActivity{

	/**
	 * start preview for file
	 * 
	 * @param fragment
	 */
	public static void startFilePreviewActivity(Fragment fragment, String activeBoardId){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), FilePreviewActivity.class);
			intent.putExtra(WelfareConst.Extras.ACTIVE_BOARD_ID, activeBoardId);
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

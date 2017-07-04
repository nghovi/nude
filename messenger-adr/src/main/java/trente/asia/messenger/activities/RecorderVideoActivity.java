package trente.asia.messenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import trente.asia.android.util.AndroidUtil;
import trente.asia.messenger.R;
import trente.asia.messenger.services.util.RecorderVideoFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;

public class RecorderVideoActivity extends WelfareActivity{

	public static void starVideoPreviewActivity(Fragment fragment, int activeBoardId){
		if(AndroidUtil.verifyStoragePermissions(fragment.getActivity())){
			Intent intent = new Intent(fragment.getActivity(), RecorderVideoActivity.class);
			intent.putExtra(WelfareConst.Extras.ACTIVE_BOARD_ID, activeBoardId + "");
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

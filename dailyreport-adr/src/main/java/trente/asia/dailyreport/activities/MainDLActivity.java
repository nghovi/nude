package nguyenhoangviet.vpcorp.dailyreport.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Date;

import asia.chiase.core.util.CCDateUtil;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.kpi.GroupActualFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.kpi.UserActualFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.report.MyReportFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.report.ReportDetailFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.ReportModel;
import nguyenhoangviet.vpcorp.dailyreport.services.user.DrLoginFragment;
import nguyenhoangviet.vpcorp.dailyreport.utils.DRUtil;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

public class MainDLActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
		UserModel userModel = prefAccUtil.getUserPref();
		Bundle mExtras = getIntent().getExtras();
		if(DRUtil.isNotEmpty(userModel.key)){
			if(mExtras != null){
				showFragment(mExtras);
			}else{
				showMyReportFragment();
			}
		}else{
			showLogInFragment();
		}
	}

	private void showMyReportFragment(){
		MyReportFragment myReportFragment = new MyReportFragment();
		addFragment(myReportFragment);
	}

	private void showLogInFragment(){
		DrLoginFragment loginFragment = new DrLoginFragment();
		addFragment(loginFragment);
	}

	private void showFragment(Bundle mExtras){
		String serviceCode = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_TYPE);
		String key = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY);
		String parentKey = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_PARENT_KEY);
		switch(serviceCode){
		case WelfareConst.NotificationType.DR_NOTI_NEW_REPORT:
		case WelfareConst.NotificationType.DR_NOTI_COMMENT_REPORT:
		case WelfareConst.NotificationType.DR_NOTI_LIKE_REPORT:
			gotoReportDetail(key);
			break;
		case WelfareConst.NotificationType.DR_NOTI_CHECKPOINT:
			gotoUserActualFragment(key, parentKey);
			break;
		case WelfareConst.NotificationType.DR_NOTI_COMPLETE_PROGRESS:
		case WelfareConst.NotificationType.DR_NOTI_UNCOMPLETE_PROGRESS:
		case WelfareConst.NotificationType.DR_NOTI_COMPLETE_GROUP_GOAL:
			gotoGroupActualFragment(key, parentKey);
			break;
		default:
			break;
		}
	}

	private void gotoGroupActualFragment(String key, String selectedDate){
		GroupActualFragment groupActualFragment = new GroupActualFragment();
		groupActualFragment.setGroupKpiKey(key);
		Date date = CCDateUtil.makeDateCustom(selectedDate, WelfareConst.WF_DATE_TIME_DATE);
		groupActualFragment.setSelectedDate(date);
		addFragment(groupActualFragment);
	}

	private void gotoUserActualFragment(String groupId, String targetDate){
		UserActualFragment userActualFragment = new UserActualFragment();
		userActualFragment.setGroupId(groupId);
		userActualFragment.setTargetDate(targetDate);
		addFragment(userActualFragment);
	}

	private void gotoReportDetail(String key){
		ReportDetailFragment reportDetailFragment = new ReportDetailFragment();
		ReportModel reportModel = new ReportModel(key);
		reportDetailFragment.setReportModel(reportModel);
		reportDetailFragment.isClickNotification = true;
		addFragment(reportDetailFragment);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static final int DR_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1101;

	public interface OnWriteExternalStoragePermissionGranted{

		public void onGranted();
	}

	private OnWriteExternalStoragePermissionGranted onWriteExternalStoragePermissionGranted;

	// http://stackoverflow.com/questions/3853472/creating-a-directory-in-sdcard-fails
	public void grantWritePermission(OnWriteExternalStoragePermissionGranted onWriteExternalStoragePermissionGranted){
		this.onWriteExternalStoragePermissionGranted = onWriteExternalStoragePermissionGranted;
		String state = Environment.getExternalStorageState();
		if(!Environment.MEDIA_MOUNTED.equals(state)){
			Log.d("myAppName", "Error: external storage is unavailable");
			return;
		}
		if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			Log.d("myAppName", "Error: external storage is read only.");
			return;
		}
		Log.d("myAppName", "External storage is not read only or unavailable");

		if(ContextCompat.checkSelfPermission(this, // request permission when it is not granted.
						Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
			Log.d("myAppName", "permission:WRITE_EXTERNAL_STORAGE: NOT granted!");
			// Should we show an explanation?
			// if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
			// // Show an expanation to the user *asynchronously* -- don't block
			// // this thread waiting for the user's response! After the user
			// // sees the explanation, try again to request the permission.
			// }else{
			// No explanation needed, we can request the permission.
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, DR_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
			// }
		}else{
			if(onWriteExternalStoragePermissionGranted != null){
				onWriteExternalStoragePermissionGranted.onGranted();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
		switch(requestCode){
		case DR_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:{
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
				if(onWriteExternalStoragePermissionGranted != null){
					onWriteExternalStoragePermissionGranted.onGranted();
				}
			}
			return;
		}
		}
	}
}

package trente.asia.welfare.adr.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.utils.BuildTempFileAsync;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * AbstractFilePreviewFragment
 *
 * @author TrungND
 */
public abstract class AbstractFilePreviewFragment extends WelfareFragment{

	protected String	mOriginalPath;
	protected String	mItemFileType;

	@Override
	public void initView(){
		super.initView();

		openFilePicker();
	}

	/**
	 * open camera to record video
	 */
	private void openFilePicker(){

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		// intent.putExtra(MsConst.Extras.ACTIVE_BOARD_ID, activeBoardId);
		startActivityForResult(intent, WelfareConst.RequestCode.PICK_FILE);
	}

	abstract protected String makeAppFile(String fileName);

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);

		if(resultCode == Activity.RESULT_CANCELED){
			activity.setResult(Activity.RESULT_CANCELED);
			activity.finish();
			return;
		}

		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case WelfareConst.RequestCode.PICK_FILE:
			getFile(returnedIntent);
			break;
		default:
			break;
		}
	}

	private void getFile(Intent data){
		Uri fileUri = data.getData();
		String fileName = null;
		String filePath = null;

		if(fileUri.getScheme().equals("content")){

			String proj[];
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
				proj = new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME};
			}else{
				proj = new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME};
			}
			Cursor cursor = activity.getContentResolver().query(fileUri, proj, null, null, null);
			cursor.moveToFirst();

			int column_index_name = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
			int column_index_path = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

			fileName = cursor.getString(column_index_name);
			filePath = makeAppFile(fileName);

			// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			try{
				loadingDialog.show();
				new BuildTempFileAsync(activity, filePath, fileName, new BuildTempFileAsync.OnTempFileCreatedListener() {

					@Override
					public void onTempFileCreated(String path, String name){
						loadingDialog.dismiss();
						if(TextUtils.isEmpty(path)){
							onFileSelected(Activity.RESULT_CANCELED, null, null);
						}else{
							onFileSelected(Activity.RESULT_OK, path, name);
						}
					}
				}).execute(activity.getContentResolver().openInputStream(fileUri));
				// async task initialized, exit
				cursor.close();
				return;
			}catch(FileNotFoundException ignored){
				filePath = "";
			}
			// }else{
			// filePath = cursor.getString(column_index_path);
			// }

			cursor.close();

		}else if(fileUri.getScheme().equals("file")){

			File file = new File(URI.create(fileUri.toString()));
			fileName = file.getName();
			filePath = file.getAbsolutePath();

			if(!CCStringUtil.isEmpty(fileName) && !CCStringUtil.isEmpty(filePath)){
				onFileSelected(Activity.RESULT_OK, filePath, fileName);
			}else{
				onFileSelected(Activity.RESULT_CANCELED, null, null);
			}
		}
	}

	protected void onFileSelected(int resultOk, String filePath, String fileName){
		if(resultOk == Activity.RESULT_OK){
			mOriginalPath = filePath;
			File originalFile = new File(mOriginalPath);
            SettingModel settingModel = prefAccUtil.getSetting();
			if(CCNumberUtil.toLong(settingModel.WF_MAX_FILE_SIZE).compareTo(originalFile.length()) < 0){
				Intent intent = activity.getIntent();
				intent.putExtra("detail", WelfareConst.WF_FILE_SIZE_NG);
				activity.setResult(Activity.RESULT_OK, intent);
				activity.finish();
			}else{
				mItemFileType = WelfareUtil.getItemFileType(fileName);
			}
		}else{
			// result cancel
			activity.setResult(Activity.RESULT_CANCELED);
			activity.finish();
		}
	}

	@Override
	protected void onClickBackBtn(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}
}

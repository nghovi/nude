package asia.trente.officeletter.commons.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.services.document.listener.OnDownloadListener;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.android.util.DownloadFileManager;
import nguyenhoangviet.vpcorp.android.util.OpenDownloadedFile;
import nguyenhoangviet.vpcorp.android.view.ChiaseDownloadFileDialog;

/**
 * Created by tien on 8/18/2017.
 */

public class OLUtils{

	public static void showAlertDialog(Context context, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(messageId);
		builder.setPositiveButton(R.string.chiase_common_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public static String getFilesFolderPath(){
		File folder = new File(Environment.getExternalStorageDirectory(), OLConst.APP_FOLDER);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder.getAbsolutePath();
	}

	public static void downloadFilePrivate(final Activity activity, String fileUrl, final OnDownloadListener callback){
		if(AndroidUtil.verifyStoragePermissions(activity)){
			File file = new File(activity.getFilesDir(), OLConst.PDF_TEMP_NAME);
			final ProgressDialog dialog = new ProgressDialog(activity);
			dialog.setMessage(activity.getString(R.string.ol_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

			DownloadFileManager.downloadFile(activity, fileUrl, file, new DownloadFileManager.OnDownloadListener() {

				@Override
				public void onStart(){
					Log.d("LOG", "START UPLOADING");
				}

				@Override
				public void onSetMax(final int max){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){

						}
					});
				}

				@Override
				public void onProgress(final int current){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){

						}
					});
				}

				@Override
				public void onFinishDownload(){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){
							// dialog.fileDownloaded();
						}
					});
				}

				@Override
				public void onResponse(boolean isSuccess, final String path){
					dialog.dismiss();
					callback.onDownloadCompleted();
				}
			});

		}
	}

	public static void downloadFile(final Activity activity, String fileName, String fileUrl){
		if(AndroidUtil.verifyStoragePermissions(activity)){

			String filePath = OLUtils.getFilesFolderPath() + "/" + fileName;
			File file = new File(filePath);

			if(file.exists()){
				OpenDownloadedFile.downloadedFileDialog(file, activity);
			}else{
				final ChiaseDownloadFileDialog dialog = ChiaseDownloadFileDialog.startDialog(activity);
				DownloadFileManager.downloadFile(activity, fileUrl, file, new DownloadFileManager.OnDownloadListener() {

					@Override
					public void onStart(){
						Log.d("LOG", "START UPLOADING");
					}

					@Override
					public void onSetMax(final int max){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								dialog.setMax(max);
							}
						});
					}

					@Override
					public void onProgress(final int current){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								dialog.setCurrent(current);
							}
						});
					}

					@Override
					public void onFinishDownload(){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								// dialog.fileDownloaded();
							}
						});
					}

					@Override
					public void onResponse(boolean isSuccess, final String path){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								dialog.dismiss();
								OpenDownloadedFile.downloadedFileDialog(new File(path), activity);
							}
						});
					}
				});
			}
		}
	}

	private static void log(String msg){
		Log.e("OLUtils", msg);
	}
}

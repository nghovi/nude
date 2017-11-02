package nguyenhoangviet.vpcorp.android.util;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import nguyenhoangviet.vpcorp.android.R;
import nguyenhoangviet.vpcorp.android.view.ChiaseDialog;

/**
 * OpenDownloadedFile
 *
 * @author TrungND reference Spika open source
 */
public class OpenDownloadedFile{

	/**
	 * dialog for open file
	 *
	 * @param file file to open
	 * @param context
	 */
	public static void downloadedFileDialog(final File file, final Context context){

		final ChiaseDialog dialog = new ChiaseDialog(context);
		dialog.setContentView(R.layout.dialog_open_file);

		LinearLayout lnrOk = (LinearLayout)dialog.findViewById(R.id.lnr_id_ok);
		LinearLayout lnrCancel = (LinearLayout)dialog.findViewById(R.id.lnr_id_cancel);
		lnrOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				Uri uri = AndroidUtil.getUriFromFile(context, file);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

				if(uri.toString().contains(".doc") || uri.toString().contains(".docx")){
					// Word document
					intent.setDataAndType(uri, "application/msword");
				}else if(uri.toString().contains(".pdf")){
					// PDF file
					intent.setDataAndType(uri, "application/pdf");
				}else if(uri.toString().contains(".ppt") || uri.toString().contains(".pptx")){
					// Powerpoint file
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
				}else if(uri.toString().contains(".xls") || uri.toString().contains(".xlsx")){
					// Excel file
					intent.setDataAndType(uri, "application/vnd.ms-excel");
				}else if(uri.toString().contains(".zip")){
					// ZIP audio file
					intent.setDataAndType(uri, "application/zip");
				}else if(uri.toString().contains(".rar")){
					// ZIP audio file
					intent.setDataAndType(uri, "application/x-rar-compressed");
				}else if(uri.toString().contains(".gz")){
					// ZIP audio file
					intent.setDataAndType(uri, "application/gzip");
				}else if(uri.toString().contains(".rtf")){
					// RTF file
					intent.setDataAndType(uri, "application/rtf");
				}else if(uri.toString().contains(".wav") || uri.toString().contains(".mp3")){
					// WAV audio file
					intent.setDataAndType(uri, "audio/x-wav");
				}else if(uri.toString().contains(".gif")){
					// GIF file
					intent.setDataAndType(uri, "image/gif");
				}else if(uri.toString().contains(".jpg") || uri.toString().contains(".jpeg") || uri.toString().contains(".png")){
					// JPG file
					intent.setDataAndType(uri, "image/jpeg");
				}else if(uri.toString().contains(".txt")){
					// Text file
					intent.setDataAndType(uri, "text/plain");
				}else if(uri.toString().contains(".3gp") || uri.toString().contains(".mpg") || uri.toString().contains(".mpeg") || uri.toString().contains(".mpe") || uri.toString().contains(".mp4") || uri.toString().contains(".avi")){
					// Video files
					intent.setDataAndType(uri, "video/*");
				}else{
					// if you want you can also define the intent type for
					// any
					// other file

					// additionally use else clause below, to manage other
					// unknown extensions
					// in this case, Android will show all applications
					// installed on the device
					// so you can choose which application to use
					intent.setDataAndType(uri, "*/*");
				}

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				try{
					context.startActivity(intent);
				}catch(ActivityNotFoundException activityNotFound){
					Toast.makeText(context, "Cannot open file", Toast.LENGTH_SHORT).show();
				}

				dialog.dismiss();
			}
		});

		lnrCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				dialog.dismiss();
			}
		});

		dialog.show();
	}

}
